package logging.util;

import arc.func.Cons;
import arc.func.ConsT;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.struct.StringMap;
import arc.util.Http;
import arc.util.Log;
import arc.util.Timer;
import arc.util.Http.HttpRequest;
import arc.util.Http.HttpResponse;
import arc.util.Http.HttpStatusException;
import arc.util.serialization.JsonWriter.OutputType;
import logging.ExtraVars;
import mindustry.io.JsonIO;

/** Partial wrapper for the <a href="https://libretranslate.com">LibreTranslate API</a><p>
 * Technically this falls into the <a href="https://github.com/LibreTranslate/LibreTranslate#can-i-use-your-api-server-at-libretranslatecom-for-my-application-in-production">"infrequent use"</a> category (I hope so)<p>
 * <!-- Is this how I'm supposed to write async --->
 * @author Weathercold
 */
public class Translating{
    public static volatile ObjectMap<String, Boolean> servers = ObjectMap.of(
        //"libretranslate.com", false, requires API key :(
        "translate.api.skitzen.com", false,
        "translate.mentality.rip", false, //sus link
        "translate.argosopentech.com", false,
        "trans.zillyhuhn.com", false
    );

    //Might break certain mods idk
    static{JsonIO.json.setOutputType(OutputType.json);}

    /** Get the language of the specified text, then run success if no errors occurred.
     * @param success The callback to run if no errors occurred.
    */
    public static void detect(String text, Cons<String> success){
        if (text == null){
            Log.err(new NullPointerException("Detect text cannot be null."));
            return;
        }

        buildSend(
            "/detect",
            "{\"q\":\"" + text + "\"}",
            res -> success.get(JsonIO.json.fromJson(StringMap[].class, res)[0].get("language"))
        );
    }

    /** Retrieve an array of supported languages, then run success if no errors occurred.
     * @param success The callback to run if no errors occurred.
     */
    public static void languages(Cons<Seq<String>> success){
        buildSend(
            "/languages",
            "", //no body
            res -> {
                StringMap[] langs = JsonIO.json.fromJson(StringMap[].class, res);
                Seq<String> codes = new Seq<>(langs.length);
                for (StringMap lang : langs) codes.add(lang.get("code"));
                success.get(codes);
            }
        );
    }

    /** detect() + translate() */
    public static void translate(String text, String target, Cons<String> success){
        detect(text, source -> translate(text, source, target, success));
    }

    /** Translate the specified text from the source language to the target language, then run success if no errors occurred.
     * @param source Language code of the source language.
     * @param target Language code of the target language.
     * @param success The callback to run if no errors occurred.
     */
    public static void translate(String text, String source, String target, Cons<String> success){
        if (text == null || source == null || target == null){
            Log.err(new NullPointerException("[EL] Translate arguments cannot be null."));
            return;
        }
        if (source == target){success.get(text); return;}

        buildSend(
            "/translate",
            JsonIO.json.toJson(StringMap.of(
                "q", text,
                "source", source,
                "target", target
            ), StringMap.class, String.class),
            res -> success.get(JsonIO.json.fromJson(StringMap.class, res).get("translatedText"))
        );
    }

    private static void buildSend(String api, String content, Cons<String> success){
        String server = servers.findKey(false, false);
        if (server == null){
            Log.warn("[EL] Rate limit reached on all servers. Aborting translation.");
            return;
        }
        ConsT<HttpResponse, Exception> successWrap = res -> {
            String cont = res.getResultAsString();
            Log.debug("[EL] Response from @:[]\n@", server, cont.replace("\n", ""));
            success.get(cont);
        };
        HttpRequest request = Http.post("https://" + server + api)
                                  .header("Content-Type", "application/json")
                                  .content(content);

        request.error(e -> {
            if (e instanceof HttpStatusException){
                HttpStatusException hse = (HttpStatusException)e;
                switch (hse.status){
                    case UNKNOWN_STATUS: // rate limit
                        Log.info("[EL] Rate limit reached with @, retrying...", server + api);
                        servers.put(server, true);
                        Timer.schedule(() -> servers.put(server, false), 60f);
                        buildSend(api, content, success);
                        break;
                    case BAD_REQUEST:
                        Log.warn("[EL] Bad request, aborting translation.[]\n@", content);
                        break;
                    default:
                        if (servers.size >= 2){
                            Log.warn("[EL] HTTP Response indicates error, retrying...[]\n@", hse);
                            servers.remove(server);
                            buildSend(api, content, success);
                        }else{
                            Log.err("[EL] HTTP Response indicates error, disabling translation for this session[]", hse);
                            ExtraVars.enableTranslation = false;
                        }
                }
            }else{
                Log.err("[EL] An unknown error occurred, disabling translation for this session[]", e);
                ExtraVars.enableTranslation = false;
            }
        }).submit(successWrap);
    }
}
