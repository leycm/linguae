package de.leycm.linguae.provider;

import de.leycm.linguae.AbstractLinguaeProvider;
import de.leycm.linguae.Label;
import de.leycm.linguae.placeholder.PsPattern;
import lombok.NonNull;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;

import java.io.Reader;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public class JsonFileLinguaeProvider extends AbstractLinguaeProvider {

    private final String basePath;
    private final Gson gson;
    private final HttpClient client;

    public JsonFileLinguaeProvider(final @NonNull HashMap<String, Function<String, Label>> labels,
                                   final @NonNull Function<String, Component> parser,
                                   final @NonNull PsPattern pattern,
                                   final @NonNull String basePath) {
        super(labels, parser, pattern);
        this.basePath = basePath.endsWith("/") ? basePath : basePath + "/";
        this.gson = new Gson();
        this.client = HttpClient.newHttpClient();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected @NonNull Map<String, String> loadLanguage(final @NonNull Locale lang) throws Exception {
        String fileName = lang.toLanguageTag().replace("-", "_") + ".json";
        if (basePath.startsWith("http://") || basePath.startsWith("https://")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(basePath + fileName))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), HashMap.class);
        } else {
            Path path = Paths.get(basePath + fileName);
            try (Reader reader = Files.newBufferedReader(path)) {
                return gson.fromJson(reader, HashMap.class);
            }
        }
    }

    @Override
    protected @NonNull String handleMissingTranslation(final @NonNull String key,
                                                       final @NonNull Locale lang) {
        return "[" + lang.toLanguageTag() + ":" + key + "]";
    }
}
