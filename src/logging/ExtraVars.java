package logging;

import arc.Core;
import arc.struct.Seq;
import arc.util.Log.LogLevel;
import logging.ui.ExtraSettings;
import mindustry.game.EventType.*;

@SuppressWarnings("unchecked")
public class ExtraVars{
    public static boolean enableMetaLogging = Core.settings.getBool("extra-enablemetalogging", true);
    public static LogLevel metaLogLevel = LogLevel.values()[Core.settings.getInt("extra-metaloglevel", 0)];

    public static boolean enableEventLogging = Core.settings.getBool("extra-enableeventlogging", true);
    public static LogLevel eventLogLevel = LogLevel.values()[Core.settings.getInt("extra-metaloglevel", 0)];
    public static Seq<Class<? extends Object>> listeningEvents = Seq.with(
        FileTreeInitEvent.class,
        ContentInitEvent.class,
        WorldLoadEvent.class,
        ClientLoadEvent.class,
        ClientPreConnectEvent.class,
        StateChangeEvent.class
    );

    public static ExtraSettings settings = new ExtraSettings();
}
