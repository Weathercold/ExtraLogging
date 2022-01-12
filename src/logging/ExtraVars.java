package logging;

import java.util.Locale;

import arc.Core;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.OS;
import arc.util.Reflect;
import arc.util.Log.LogLevel;
import logging.ui.ExtraSettings;
import logging.util.Translating;
import mindustry.Vars;
import mindustry.core.Version;
import mindustry.game.EventType.*;

@SuppressWarnings("unchecked")
public class ExtraVars{
    /** Whether to ensure Foo compatibility. */
    public static boolean isFoo;
    
    public static boolean coloredJavaConsole = Core.settings.getBool("extra-coloredjavaconsole", !OS.isWindows && !OS.isAndroid);
    
    public static boolean enableMetaDebugging = Core.settings.getBool("extra-enablemetadebugging", false);
    
    public static boolean enableEventLogging = Core.settings.getBool("extra-enableeventlogging", false);
    public static LogLevel eventLogLevel = LogLevel.values()[Core.settings.getInt("extra-metaloglevel", 0)];
    public static Seq<Class<? extends Object>> listeningEvents = Seq.with(
        FileTreeInitEvent.class,
        ContentInitEvent.class,
        WorldLoadEvent.class,
        ClientLoadEvent.class,
        ClientPreConnectEvent.class,
        StateChangeEvent.class,
        DisposeEvent.class
        );
        
    public static String targetLang = Locale.getDefault().getLanguage();
    public static Seq<String> supportedLangs;
    public static boolean enableTranslation = Core.settings.getBool("extra-enabletranslation", true);

    public static ExtraSettings settings = new ExtraSettings();

    static{
        try{
            Reflect.get(Version.class, "clientVersion");
            Log.info("[EL] Foo has built-in translation (yes, also by me). Disabling EL's translation.");
            enableTranslation = false;
            isFoo = true;
        }
        catch (RuntimeException e){
            isFoo = false;
        
            if (!Vars.headless) Translating.languages(langs -> {
                supportedLangs = langs;
                targetLang = langs.contains(targetLang) ? targetLang : "en";
            });
            else{
                Log.info("[EL] Translation doesn't work on headless servers.");
                enableTranslation = false;
            }
        }
    }
}