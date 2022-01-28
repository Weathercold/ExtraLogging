package logging;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import arc.struct.Seq;
import arc.util.Log;
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
    
    public static String logf;
    public static DateTimeFormatter timef;
    public static boolean coloredTerminal;
    
    public static boolean enableMetaDebugging;
    public static String metaColor;
    
    public static boolean enableEventLogging;
    public static LogLevel eventLogLevel;
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
    public static Seq<String> supportedLangs = new Seq<>();
    public static boolean enableTranslation;

    public static ExtraSettings settings = new ExtraSettings();

    static{
        settings.update();

        try{
            Reflect.get(Version.class, "clientVersion");
            isFoo = true;
        }
        catch (RuntimeException e){isFoo = false;}

        Translating.languages(langs -> {
            supportedLangs = langs;
            targetLang = langs.contains(targetLang) ? targetLang : "en";
        });
        
        if (isFoo) Log.info("[EL] Translation is disabled because Foo has its own implementation (yes, also by me). Use that instead.");
        if (Vars.headless) Log.info("[EL] Translation doesn't work on headless servers.");
    }
}