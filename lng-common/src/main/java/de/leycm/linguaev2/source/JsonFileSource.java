package de.leycm.linguaev2.source;

import de.leycm.linguae.source.LinguaeSource;
import lombok.NonNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JsonFileSource implements LinguaeSource {
    @Override
    public @NonNull List<Locale> getSupportedLanguages() {
        return List.of();
    }

    @Override
    public @NonNull Map<String, String> loadLanguage(@NonNull Locale locale) throws Exception {
        return Map.of();
    }

    @Override
    public boolean supportsLanguage(@NonNull Locale locale) {
        return false;
    }
}
