package logging.util;

import arc.Core;
import arc.util.Log;
import arc.util.Log.LogLevel;

/** {@link arc.util.Log} Wrapper
 * @author Weathercold
 */
public class ExtraLog{
    public static void log(LogLevel level, String text){
        Log.log(level, "[EL] " + (text.startsWith("@") ? Core.bundle.get(text.substring(1)) : text));
    }

    public static void debug(String text){
        log(LogLevel.debug, text);
    }

    public static void info(String text){
        log(LogLevel.info, text);
    }

    public static void warn(String text){
        log(LogLevel.warn, text);
    }

    public static void err(String text){
        log(LogLevel.err, text);
    }
}