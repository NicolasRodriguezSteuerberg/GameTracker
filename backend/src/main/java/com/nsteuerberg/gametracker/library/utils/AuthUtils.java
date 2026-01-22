package com.nsteuerberg.gametracker.library.utils;

import com.nsteuerberg.gametracker.library.service.exceptions.InvalidUserIdException;
import com.nsteuerberg.gametracker.library.service.exceptions.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    /**
     *
     * @param userIdOrMe
     * @param authentication
     * @return ID que se pidió en la URL (me = id del usuario autenticado, o id ajeno).
     */
    public Long resolveUserId(String userIdOrMe, Authentication authentication) {
        Long currentUserId = getCurrentUserId(authentication);
        return parseId(userIdOrMe, currentUserId);
    }

    /**
     * Valida que el userId de la URL coincida con el usuario logueado.
     * @param userIdOrMe parametro de la url (id del usuario o me)
     * @param authentication usuario autenticado
     * @return Devuelve el ID del usuario logueado (Long)
     */
    public Long validateAndGetUserId(String userIdOrMe, Authentication authentication) {
        Long currentUserId = getCurrentUserId(authentication);
        Long requestId = parseId(userIdOrMe, currentUserId);
        if (!requestId.equals(currentUserId)) {
            throw new UnauthorizedAccessException("Can't modify others library");
        }
        return requestId;
    }

    private Long parseId(String userIdOrMe, Long currentUserId) {
        if ("me".equalsIgnoreCase(userIdOrMe)) {
            return currentUserId;
        }
        try {
            return Long.parseLong(userIdOrMe);
        } catch (NumberFormatException e) {
            throw new InvalidUserIdException("Given user ID isn't valid: " + userIdOrMe);
        }
    }

    private Long getCurrentUserId(Authentication authentication) {
        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Critical error: Token doesn't have valid numeric ID.");
        }
    }
}
