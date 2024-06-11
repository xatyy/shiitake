package ro.foodx.backend.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    public static String getJwtTokenFromCookies(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .findFirst();
            if (jwtCookie.isPresent()) {
                return jwtCookie.get().getValue();
            }
        }
        return null;
    }
}
