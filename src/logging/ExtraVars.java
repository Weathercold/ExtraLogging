package logging;

import arc.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Log.*;
import logging.core.*;
import logging.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.game.EventType.*;

import java.time.format.*;
import java.util.*;

import static logging.util.ExtraLog.info;

public class ExtraVars{
    public static final Seq<Class<?>> listeningEvents = Seq.with(
        FileTreeInitEvent.class,
        ContentInitEvent.class,
        WorldLoadEvent.class,
        ClientLoadEvent.class,
        ClientPreConnectEvent.class,
        StateChangeEvent.class,
        DisposeEvent.class
    );
    public static final ExtraUI ui = new ExtraUI();
    /** Whether to ensure Foo compatibility. */
    public static boolean isFoo;
    public static String logf;
    public static DateTimeFormatter timef;
    public static boolean coloredTerminal;
    public static boolean enableMetaDebugging;
    public static String metaColor;
    public static boolean enableEventLogging;
    public static LogLevel eventLogLevel;
    public static String targetLang = Locale.getDefault().getLanguage();
    public static Seq<String> supportedLangs = new Seq<>();
    public static boolean enableTranslation;

    static{
        init();
    }

    public static void init(){
        try{
            Reflect.get(Version.class, "clientVersion");
            isFoo = true;
        }catch(RuntimeException e){
            isFoo = false;
        }

        Translating.languages(langs -> {
            supportedLangs = langs;
            targetLang = langs.contains(targetLang) ? targetLang : "en";
        });

        if(isFoo) info("@extra-logging.footranslation");
        if(Vars.headless) info("@extra-logging.headlesstranslation");

        refreshenv();
    }

    public static void refreshenv(){
        Log.level = LogLevel.values()[Core.settings.getInt("extra-loglevel")];
        coloredTerminal = Core.settings.getBool("extra-coloredterminal", !OS.isWindows && !OS.isAndroid);
        enableEventLogging = Core.settings.getBool("extra-enableeventlogging", false);
        enableTranslation = Core.settings.getBool("extra-enabletranslation", !isFoo && !Vars.headless) && !isFoo && !Vars.headless;

        logf = Core.settings.getString("extra-logformat", "[gray][$t][] &fb$L[$l][] $M$m[]");
        try{
            timef = DateTimeFormatter.ofPattern(Core.settings.getString("extra-timestampformat", "HH:mm:ss.SSS"));
        }catch(Throwable ignored){
            timef = DateTimeFormatter.ISO_LOCAL_TIME;
        }
        enableMetaDebugging = Core.settings.getBool("extra-enablemetadebugging", false);
        metaColor = Core.settings.getString("extra-metacolor", "[accent]");
        eventLogLevel = LogLevel.values()[Core.settings.getInt("extra-eventloglevel", 0)];
    }
}