package com.nsteuerberg.gametracker.games.service.usecase;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.GenreRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.PlatformRepository;
import com.nsteuerberg.gametracker.igdb.IgdbService;
import com.nsteuerberg.gametracker.igdb.dto.CommonDTO;
import com.nsteuerberg.gametracker.igdb.dto.IgdbGameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.nsteuerberg.gametracker.igdb.IgdbService.VALID_PLATFORM_IDS;

@Component
public class SyncUpdateGamesUseCase {
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final IgdbService igdbService;
    private final int limit = 500;

    private final static Logger logger = LoggerFactory.getLogger(SyncUpdateGamesUseCase.class);

    public SyncUpdateGamesUseCase(GameRepository gameRepository, GenreRepository genreRepository, PlatformRepository platformRepository, IgdbService igdbService) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.igdbService = igdbService;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void initOnStartUp() {
        new Thread(() -> execute()).start();
    }

    @Scheduled(cron = "@daily")
    public void execute() {
        Instant lastSync = gameRepository.getLatestUpdate()
                .orElse(Instant.EPOCH);
        logger.info("Sync games, last sync: {}", lastSync);

        HashMap<Long, GenreEntity> genreMap = new HashMap<>();
        HashMap<Long, PlatformEntity> platformMap = new HashMap<>();

        Long count = igdbService.getCount(lastSync);
        logger.info("Sync games, count: {}", count);
        System.out.println(count);
        int totalPages = (int) Math.ceil((double) count / limit);

        for (int i = 0; i < totalPages; i++) {
            long offset = i * limit;
            logger.info("Sync games, page: {} of {} pages", i + 1, totalPages);

            List<IgdbGameDTO> gamesDto = igdbService.getGames(lastSync, limit, offset);

            List<GameEntity> gameEntities = generateGameEntities(gamesDto, genreMap, platformMap);

            gameRepository.saveAll(gameEntities);
        }
    }

    private List<GameEntity> generateGameEntities(List<IgdbGameDTO> gamesDto, Map genreMap, Map platformMap) {
        List<Long> igdbIds = gamesDto.stream()
                .map(IgdbGameDTO::id)
                .toList();

        List<GameEntity> existingGames = gameRepository.findByIgdbIdIn(igdbIds);
        Map<Long, GameEntity> existingGamesMap = existingGames.stream()
                .collect(Collectors.toMap(GameEntity::getIgdbId, game -> game));

        List<GameEntity> resultGamesEntities = new ArrayList<>();
        for (IgdbGameDTO gameDTO: gamesDto) {
            if (gameDTO.genres() == null || gameDTO.platforms() == null || gameDTO.cover() == null || gameDTO.first_release_date() == null)
                continue;

            Set<GenreEntity> genreEntitySet = putGenres(gameDTO.genres(), genreMap);
            Set<PlatformEntity> platformEntities = putPlatforms(gameDTO.platforms(), platformMap);

            GameEntity gameEntity = existingGamesMap.getOrDefault(gameDTO.id(), new GameEntity());

            if (gameEntity.getId() == null) {
                gameEntity.setIgdbId(gameDTO.id());
            }

            gameEntity.setName(gameDTO.name());
            gameEntity.setSummary(gameDTO.summary());
            gameEntity.setCoverUrl(gameDTO.cover().url().replace("t_thumb", "t_cover_big"));
            gameEntity.setFirstReleaseDate(Instant.ofEpochSecond(gameDTO.first_release_date()));
            gameEntity.setLastUpdated(Instant.ofEpochSecond(gameDTO.updated_at()));
            gameEntity.setGenres(genreEntitySet);
            gameEntity.setPlatforms(platformEntities);

            resultGamesEntities.add(gameEntity);
        }
        return resultGamesEntities;
    }

    private Set<GenreEntity> putGenres(List<CommonDTO> genresDTO, Map<Long, GenreEntity> genresMap) {
        Set<GenreEntity> genreEntities = new HashSet<>();
        for (CommonDTO commonDTO: genresDTO) {
            GenreEntity entity = genresMap.computeIfAbsent(commonDTO.id(), id ->
                    genreRepository.findByIgdbId(id)
                            .orElseGet(() ->
                                    genreRepository.save(
                                            GenreEntity.builder()
                                                    .igdbId(id)
                                                    .name(commonDTO.name())
                                                    .build()
                                    )
                            )
            );
            genreEntities.add(entity);
        }
        return genreEntities;
    }

    private Set<PlatformEntity> putPlatforms(List<CommonDTO> platformsDTO, Map<Long, PlatformEntity> platformsMap) {
        Set<PlatformEntity> platformEntities = new HashSet<>();
        for (CommonDTO commonDTO: platformsDTO) {
            if (!VALID_PLATFORM_IDS.contains(commonDTO.id())) continue;
            PlatformEntity entity = platformsMap.computeIfAbsent(commonDTO.id(), id ->
                    platformRepository.findByIgdbId(id)
                            .orElseGet(() ->
                                    platformRepository.save(
                                            PlatformEntity.builder()
                                                    .igdbId(id)
                                                    .name(commonDTO.name())
                                                    .build()
                                    )
                            )
            );
            platformEntities.add(entity);
        }
        return platformEntities;
    }
}
