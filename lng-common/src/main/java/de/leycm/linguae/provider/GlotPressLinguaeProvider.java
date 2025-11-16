package de.leycm.linguae.provider;

import de.leycm.linguae.AbstractLinguaeProvider;
import de.leycm.linguae.Label;
import de.leycm.linguae.placeholder.PsPattern;
import lombok.NonNull;

import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.function.Function;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;

public class GlotPressLinguaeProvider extends AbstractLinguaeProvider {
    private final String baseUrl;
    private final String project;
    private final HttpClient client;
    private final Gson gson;

    public GlotPressLinguaeProvider(final @NonNull HashMap<String, Function<String, Label>> labels,
                                    final @NonNull Function<String, Component> parser,
                                    final @NonNull PsPattern pattern,
                                    final @NonNull String baseUrl,
                                    final @NonNull String project) {
        super(labels, parser, pattern);
        this.baseUrl = baseUrl;
        this.project = project;
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected @NonNull Map<String, String> loadLanguage(final @NonNull Locale lang) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/translations/-export-translations?project=" +
                        project + "&locale=" + lang.toLanguageTag() + "&format=json"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), HashMap.class);
    }

    @Override
    protected @NonNull String handleMissingTranslation(final @NonNull String key,
                                                       final @NonNull Locale lang) {
        return "[" + lang.toLanguageTag() + ":" + key + "]";
    }

}
