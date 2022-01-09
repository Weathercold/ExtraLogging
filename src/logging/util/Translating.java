package logging.util;

import static logging.ExtraVars.*;
import static logging.util.ColorUtils.formatColors;

import arc.func.Cons;
import arc.func.ConsT;
import arc.struct.Seq;
import arc.struct.StringMap;
import arc.util.Http;
import arc.util.Log;
import arc.util.Http.HttpRequest;
import arc.util.Http.HttpResponse;
import arc.util.Http.HttpStatusException;
import logging.ExtraVars;
import mindustry.io.JsonIO;

/** Partial wrapper for the <a href="https://libretranslate.com">LibreTranslate API</a><p>
 * Technically this falls into the <a href="https://github.com/LibreTranslate/LibreTranslate#can-i-use-your-api-server-at-libretranslatecom-for-my-application-in-production">"infrequent use"</a> category (I hope so)<p>
 * @author Weathercold
 */
public class Translating{
    public static Seq<String> servers = Seq.with(
        //"libretranslate.com", requires API key :(
        "translate.argosopentech.com",
        "translate.api.skitzen.com",
        "trans.zillyhuhn.com",
        "translate.mentality.rip" //sus link
    );

    /** Get the language of the specified text, then run success if no errors occurred.
     * @param success The callback to run if no errors occurred.
    */
    public static void detect(String text, Cons<String> success){
        if (text == null){success.get(null); return;}

        text = formatColors(text, false);
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
        if (text == null || source == null || target == null){success.get(null); return;}
        if (source == target){success.get(text); return;}

        text = formatColors(text, false);
        Log.debug(JsonIO.json.toJson(StringMap.of(
            "q", text,
            "source", source,
            "target", target
        )));
        /*buildSend(
            "/translate",
            JsonIO.json.toJson(StringMap.of(
                "q", text,
                "source", source,
                "target", target
            )),
            res -> success.get(JsonIO.json.fromJson(StringMap.class, res).get("translatedText"))
        );*/
    }

    private static void buildSend(String api, String content, Cons<String> success){
        ConsT<HttpResponse, Exception> successWrap = res -> {
            String cont = res.getResultAsString();
            if (enableMetaDebugging) Log.debug("[EL] Response from @:[]\n@", servers.first(), cont);
            success.get(cont);
        };
        HttpRequest request = Http.post("https://" + servers.first() + api)
                                  .header("Content-Type", "application/json")
                                  .content(content);
        request.error(e -> {
            if (e instanceof HttpStatusException && servers.size >= 2){
                HttpStatusException hse = (HttpStatusException)e;
                Log.warn("[EL] Response from @ indicates error (@ @), retrying with @:[]\n@",
                         servers.remove(0) + api, hse.status.code, hse.status, servers.first(), hse.response.getResultAsString().replace("\n", ""));
                request.url("https://" + servers.first() + api).submit(successWrap);
            }
            else if (e instanceof HttpStatusException){
                HttpStatusException hse = (HttpStatusException)e;
                Log.err("[EL] Response from @ indicates error (@ @), disabling translation for this session:[]\n@",
                        servers.first() + api, hse.status.code, hse.status, hse.response.getResultAsString().replace("\n", ""));
                ExtraVars.enableTranslation = false;
            }
            else{
                Log.err("[EL] An unknown error occurred, disabling translation for this session:[]\n" + e);
                ExtraVars.enableTranslation = false;
            }
        }).submit(successWrap);
    }
}
