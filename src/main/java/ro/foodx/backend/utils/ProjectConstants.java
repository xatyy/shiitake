package ro.foodx.backend.utils;

import java.util.Locale;

public class ProjectConstants {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Locale ROMANIAN_LOCALE = new Locale.Builder().setLanguage("ro").setRegion("RO").build();

    private ProjectConstants() {

        throw new UnsupportedOperationException();
    }
}
