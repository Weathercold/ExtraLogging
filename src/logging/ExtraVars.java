package logging;

import java.time.format.DateTimeFormatter;
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
    
    public static DateTimeFormatter timef;
    public static boolean coloredJavaConsole = Core.settings.getBool("extra-coloredjavaconsole", !OS.isWindows && !OS.isAndroid);
    
    public static boolean enableMetaDebugging = Core.settings.getBool("extra-enablemetadebugging", false);
    public static String metaColor = Core.settings.getString("extra-metacolor", "[accent]");
    
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
        try{timef = DateTimeFormatter.ofPattern(Core.settings.getString("extra-timestampformat", "HH:mm:ss.SSS"));}
        catch (Throwable e){timef = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");}

        try{
            Reflect.get(Version.class, "clientVersion");
            isFoo = true;
        }
        catch (RuntimeException e){isFoo = false;}
        
        enableTranslation = Core.settings.getBool("extra-enabletranslation", true) && !isFoo && !Vars.headless;

        Translating.languages(langs -> {
            supportedLangs = langs;
            targetLang = langs.contains(targetLang) ? targetLang : "en";
        });
        
        if (isFoo) Log.info("[EL] Translation is disabled because Foo has its own implementation (yes, also by me). Use that instead.");
        if (Vars.headless) Log.info("[EL] Translation doesn't work on headless servers.");
    }
}