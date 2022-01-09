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
    public static String lang = Locale.getDefault().getLanguage();

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

    public static boolean supportTranslation;
    public static boolean enableTranslation = false;

    public static ExtraSettings settings = new ExtraSettings();

    static{
        try{Reflect.get(Version.class, "clientVersion"); isFoo = true;}
        catch (RuntimeException e){isFoo = false;}
        
        Translating.languages(langs -> {
            supportTranslation = langs.contains(lang);
            
            if (!supportTranslation && Core.settings.getBool("extra-enabletranslation", true)) Log.warn("[EL] Translation is disabled because your current Mindustry display language is not supported by LibreTranslate.");
            else if (!supportTranslation) Log.info("[EL] Translation is disabled because your current Mindustry display language is not supported by LibreTranslate.");
            else if (Vars.headless && Core.settings.getBool("extra-enabletranslation", true)) Log.warn("[EL] Translation doesn't work on headless servers.");
            else enableTranslation = true;
        });
    }
}