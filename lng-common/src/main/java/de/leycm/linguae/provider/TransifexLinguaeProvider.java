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

public class TransifexLinguaeProvider extends AbstractLinguaeProvider {
    private final String apiToken;
    private final String project;
    private final HttpClient client;
    private final Gson gson;

    public TransifexLinguaeProvider(final @NonNull HashMap<String, Function<String, Label>> labels,
                                    final @NonNull Function<String, Component> parser,
                                    final @NonNull PsPattern pattern,
                                    final @NonNull String apiToken,
                                    final @NonNull String project) {
        super(labels, parser, pattern);
        this.apiToken = apiToken;
        this.project = project;
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected @NonNull Map<String, String> loadLanguage(final @NonNull Locale lang) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://rest.api.transifex.com/resource_translations?filter[resource]=" +
                        project + "&filter[language]=" + lang.getLanguage()))
                .header("Authorization", "Bearer " + apiToken)
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
