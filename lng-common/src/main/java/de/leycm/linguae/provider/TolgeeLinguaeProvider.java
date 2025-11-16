/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a> l <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
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

public class TolgeeLinguaeProvider extends AbstractLinguaeProvider {
    private final String apiKey;
    private final String projectId;
    private final String baseUrl;
    private final HttpClient client;
    private final Gson gson;

    public TolgeeLinguaeProvider(final @NonNull HashMap<String, Function<String, Label>> labels,
                                 final @NonNull Function<String, Component> parser,
                                 final @NonNull PsPattern pattern,
                                 final @NonNull String apiKey,
                                 final @NonNull String projectId,
                                 final @NonNull String baseUrl) {
        super(labels, parser, pattern);
        this.apiKey = apiKey;
        this.projectId = projectId;
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected @NonNull Map<String, String> loadLanguage(final @NonNull Locale lang) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/v2/projects/" + projectId + "/translations/" +
                        lang.toLanguageTag()))
                .header("X-API-Key", apiKey)
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
