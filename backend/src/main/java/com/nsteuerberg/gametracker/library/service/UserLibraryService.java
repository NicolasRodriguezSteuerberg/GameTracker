package com.nsteuerberg.gametracker.library.service;

import com.nsteuerberg.gametracker.library.persistance.repository.LibraryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLibraryService {
    private final LibraryEntryRepository libraryEntryRepository;
}
