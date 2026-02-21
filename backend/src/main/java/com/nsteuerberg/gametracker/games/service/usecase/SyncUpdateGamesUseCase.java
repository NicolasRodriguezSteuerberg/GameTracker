package com.nsteuerberg.gametracker.games.service.usecase;

import com.nsteuerberg.gametracker.games.persistance.entity.GameEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.GenreEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.PlatformEntity;
import com.nsteuerberg.gametracker.games.persistance.entity.embed.ScoreData;
import com.nsteuerberg.gametracker.games.persistance.entity.embed.VideoData;
import com.nsteuerberg.gametracker.games.persistance.repository.GameRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.GenreRepository;
import com.nsteuerberg.gametracker.games.persistance.repository.PlatformRepository;
import com.nsteuerberg.gametracker.igdb.IgdbService;
import com.nsteuerberg.gametracker.igdb.dto.CommonDTO;
import com.nsteuerberg.gametracker.igdb.dto.CoverDTO;
import com.nsteuerberg.gametracker.igdb.dto.IgdbGameDTO;
import com.nsteuerberg.gametracker.igdb.dto.VideoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.nsteuerberg.gametracker.igdb.IgdbService.VALID_PLATFORM_IDS;

@Component
@RequiredArgsConstructor
public class SyncUpdateGamesUseCase implements ApplicationRunner {
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final IgdbService igdbService;
    private final int limit = 500;

    private final static Logger logger = LoggerFactory.getLogger(SyncUpdateGamesUseCase.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        execute();
    }

    @Scheduled(cron = "@daily")
    public void execute() {
        Instant lastSync = gameRepository.getLatestUpdate()
                .orElse(Instant.EPOCH);
        logger.info("Sync games, last sync: {}", lastSync);

        // Diccionarios para evitar buscar constantemente en base de datos
        HashMap<Long, GenreEntity> genreMap = new HashMap<>();
        HashMap<Long, PlatformEntity> platformMap = new HashMap<>();

        Long count = igdbService.getCount(lastSync);
        logger.info("Sync games, count: {}", count);
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
        // Recogemos los IDs de IGDB para buscar en la base de datos
        List<Long> igdbIds = gamesDto.stream()
                .map(IgdbGameDTO::id)
                .toList();
        List<GameEntity> existingGames = gameRepository.findByIgdbIdIn(igdbIds);

        // Para ayudar con el buscado, creamos un diccionario con el ID de IGDB como clave
        Map<Long, GameEntity> existingGamesMap = existingGames.stream()
                .collect(Collectors.toMap(GameEntity::getIgdbId, game -> game));

        // Creamos una lista con los juegos actualizados/nuevos a guardar
        List<GameEntity> resultGamesEntities = new ArrayList<>();
        for (IgdbGameDTO gameDTO: gamesDto) {
            // si no tiene lo que queremos, pasamos de el
            if (gameDTO.genres() == null || gameDTO.platforms() == null || gameDTO.cover() == null || gameDTO.first_release_date() == null) {
                continue;
            }

            // Recogemos los generos y las plataformas ya como entidad
            Set<GenreEntity> genreEntitySet = putGenres(gameDTO.genres(), genreMap);
            Set<PlatformEntity> platformEntities = putPlatforms(gameDTO.platforms(), platformMap);

            GameEntity gameEntity = existingGamesMap.getOrDefault(gameDTO.id(), new GameEntity());

            gameEntity.setIgdbId(gameDTO.id());
            gameEntity.setSlug(gameDTO.slug());
            gameEntity.setName(gameDTO.name());
            gameEntity.setSummary(gameDTO.summary());
            gameEntity.setStoryline(gameDTO.storyline());

            gameEntity.setScore(new ScoreData(gameDTO.total_rating(), gameDTO.total_rating_count()));

            gameEntity.setCoverUrl(getUrl(gameDTO.cover()));
            gameEntity.setVideos(getVideos(gameDTO.videos()));
            gameEntity.setScreenshots(getScreenshots(gameDTO.screenshots()));

            gameEntity.setFirstReleaseDate(Instant.ofEpochSecond(gameDTO.first_release_date()));
            gameEntity.setLastUpdated(Instant.ofEpochSecond(gameDTO.updated_at()));

            gameEntity.setGenres(genreEntitySet);
            gameEntity.setPlatforms(platformEntities);

            resultGamesEntities.add(gameEntity);
        }
        return resultGamesEntities;
    }

    private List<VideoData> getVideos(List<VideoDTO> videos) {
        if (videos == null) return new ArrayList<>();
        return videos.stream()
                .map(video -> new VideoData(video.video_id(), video.name()))
                .toList();
    }

    private List<String> getScreenshots(List<CoverDTO> screenshots) {
        if (screenshots == null) return new ArrayList<>();
        return screenshots.stream()
                .map(this::getUrl)
                .toList();
    }

    private String getUrl(CoverDTO cover) {
        String url = null;
        if (cover != null && cover.url() != null) {
            url = cover.url();
            if (url.startsWith("//")) {
                url = "https:" + url;
            }
            url = url.replace("t_thumb", "t_cover_big");
        }
        return url;
    }

    private Set<GenreEntity> putGenres(List<CommonDTO> genresDTO, Map<Long, GenreEntity> genresMap) {
        Set<GenreEntity> genreEntities = new LinkedHashSet<>();
        for (CommonDTO commonDTO: genresDTO) {
            // si no existe en el diccionario, buscamos en la base de datos, de no existir, lo guardamos
            GenreEntity entity = genresMap.computeIfAbsent(commonDTO.id(), id ->
                    genreRepository.findByIgdbId(id)
                            .orElseGet(() -> genreRepository.save(GenreEntity.builder()
                                    .igdbId(id)
                                    .name(commonDTO.name())
                                    .slug(commonDTO.slug())
                                    .build()
                            ))
            );
            genreEntities.add(entity);
        }
        return genreEntities;
    }

    private Set<PlatformEntity> putPlatforms(List<CommonDTO> platformsDTO, Map<Long, PlatformEntity> platformsMap) {
        Set<PlatformEntity> platformEntities = new LinkedHashSet<>();
        for (CommonDTO commonDTO: platformsDTO) {
            if (!VALID_PLATFORM_IDS.contains(commonDTO.id())) continue;
            PlatformEntity entity = platformsMap.computeIfAbsent(commonDTO.id(), id ->
                    platformRepository.findByIgdbId(id)
                            .orElseGet(() -> platformRepository.save(PlatformEntity.builder()
                                    .igdbId(id)
                                    .name(commonDTO.name())
                                    .slug(commonDTO.slug())
                                    .build()
                            ))
            );
            platformEntities.add(entity);
        }
        return platformEntities;
    }
}
