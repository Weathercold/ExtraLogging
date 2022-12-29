package logging.util;

import arc.func.Cons;
import arc.struct.*;
import arc.util.*;
import arc.util.Http.*;
import arc.util.serialization.JsonWriter.OutputType;
import mindustry.io.JsonIO;

import static logging.ExtraVars.enableTranslation;
import static logging.util.ExtraLog.*;

/**
 * Partial wrapper for the <a href=https://libretranslate.com>LibreTranslate API</a>
 * <!-- Is this how I'm supposed to write async --->
 * @author Weathercold
 */
@SuppressWarnings("unused")
public class Translating{
    /**
     * List of mirrors can be found <a href=https://github.com/LibreTranslate/LibreTranslate#mirrors>here</a>. If you
     * see a mirror not working, please make a pr.
     */
    public static final ObjectMap<String, Boolean> servers = ObjectMap.of(
        //"libretranslate.com", false, requires API key :(
        "translate.argosopentech.com", false,
        "translate.api.skitzen.com", false,
        "libretranslate.de", false,
        "libretranslate.pussthecat.org", false,
        "translate.fortytwo-it.com", false,
        "translate.terraprint.co", false
    );

    // Might break certain mods idk
    static{
        if(enableTranslation) JsonIO.json.setOutputType(OutputType.json);
    }


    /**
     * Retrieve an array of supported languages.
     * @param success The callback to run if no errors occurred.
     */
    public static void languages(Cons<Seq<String>> success){
        fetch(
            "/languages",
            res -> {
                StringMap[] langs = JsonIO.json.fromJson(StringMap[].class, res);
                Seq<String> codes = new Seq<>(langs.length);
                for(StringMap lang : langs) codes.add(lang.get("code"));
                success.get(codes);
            }
        );
    }

    /**
     * Get the language of the specified text.
     * @param success The callback to run if no errors occurred.
     */
    public static void detect(String text, Cons<String> success){
        if(text == null){
            Log.err(new NullPointerException("Detect text cannot be null."));
            return;
        }

        fetch(
            "/detect",
            StringMap.of("q", text),
            res -> success.get(JsonIO.json.fromJson(StringMap[].class, res)[0].get("language"))
        );
    }

    public static void translate(String text, Cons<String> success){
        translate(text, "auto", "en", success);
    }

    public static void translate(String text, String target, Cons<String> success){
        translate(text, "auto", target, success);
    }

    /**
     * Translate the specified text from the source language to the target language.
     * @param source Source language code.
     * @param target target language code.
     * @param success The callback to run if no errors occurred.
     */
    public static void translate(String text, String source, String target, Cons<String> success){
        if(text == null || source == null || target == null){
            Log.err(new NullPointerException("Translate arguments cannot be null."));
            return;
        }
        if(source.equals(target)){
            success.get(text);
            return;
        }

        fetch(
            "/translate",
            StringMap.of(
                "q", text,
                "source", source,
                "target", target
            ),
            res -> {
                String translation = JsonIO.json.fromJson(StringMap.class, res).get("translatedText");
                if(translation.length() <= 256)
                    success.get(translation);
                else
                    warn("Translation is too long (@chars)", translation.length());
            }
        );
    }

    private static void fetch(String api, Cons<String> success){
        fetch(api, HttpMethod.GET, null, success);
    }

    private static void fetch(String api, @Nullable StringMap body, Cons<String> success){
        fetch(api, HttpMethod.POST, body, success);
    }

    private static void fetch(String api, HttpMethod method, @Nullable StringMap body, Cons<String> success){
        String server = servers.findKey(false, false);
        if(server == null){
            warn("Rate limit reached on all servers. Aborting translation.");
            return;
        }

        Http.post("https://" + server + api)
            .method(method)
            .header("Content-Type", "application/json")
            .content(JsonIO.json.toJson(body, StringMap.class, String.class))
            .error(e -> {
                if(e instanceof HttpStatusException hse){
                    switch(hse.status){
                        case BAD_REQUEST -> warn("Bad request, aborting translation: @", body);
                        case INTERNAL_SERVER_ERROR -> warn("Server-side error, aborting translation: @", body);
                        case UNKNOWN_STATUS -> { // most likely rate limit
                            Log.info("Rate limit reached with @, retrying...", server + api);
                            servers.put(server, true);
                            Timer.schedule(() -> servers.put(server, false), 60f);
                            fetch(api, body, success);
                        }
                        default -> {
                            if(servers.size >= 2){
                                warn("HTTP Response indicates error, retrying: @", hse);
                                servers.remove(server);
                                fetch(api, body, success);
                            }else{
                                err("HTTP Response indicates error, disabling translation for this session", hse);
                                enableTranslation = false;
                            }
                        }
                    }
                }else{
                    err("An unknown error occurred, disabling translation for this session", e);
                    enableTranslation = false;
                }
            })
            .submit(response -> {
                String result = response.getResultAsString();
                debug("Response from @:\n@", server, result.replace("\n", ""));
                success.get(result);
            });
    }
}