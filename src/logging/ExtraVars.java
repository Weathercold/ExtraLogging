package logging;

import java.util.Locale;

import arc.Core;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.OS;
import arc.util.Log.LogLevel;
import logging.ui.ExtraSettings;
import logging.util.Translating;
import mindustry.game.EventType.*;

@SuppressWarnings("unchecked")
public class ExtraVars{
    public static boolean coloredJavaConsole = Core.settings.getBool("extra-coloredjavaconsole", !OS.isWindows && !OS.isAndroid);

    public static boolean enableMetaLogging = Core.settings.getBool("extra-enablemetalogging", false);

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

    public static String lang = Locale.getDefault().getLanguage();
    public static boolean supportTranslation = false;
    public static boolean enableTranslation;

    public static ExtraSettings settings = new ExtraSettings();

    static{
        Translating.languages(langs -> {
            if (langs.contains(lang)) supportTranslation = true;
            else Log.warn("[EL] Translation is disabled because your current Mindustry display language (@) is not supported by LibreTranslate.", lang);
        });
        enableTranslation = Core.settings.getBool("extra-enabletranslation", true) && supportTranslation;
    }
}