package logging.util;

import arc.Core;
import arc.util.Log;
import arc.util.Log.LogLevel;

/** {@link arc.util.Log} Wrapper
 * @author Weathercold
 */
public class ExtraLog{
    public static void log(LogLevel level, String text, Object... args){
        if (text.startsWith("@")){
            Log.Log(level, "[EL] " + Core.bundle.format(text, args));
        }else{
            Log.log(level, "[EL] " + text, args);
        }
    }

    public static void debug(String text, Object... args){
        log(LogLevel.debug, text, args);
    }

    public static void info(String text, Object... args){
        log(LogLevel.info, text, args);
    }

    public static void warn(String text, Object... args){
        log(LogLevel.warn, text, args);
    }

    public static void err(String text){
        log(LogLevel.err, text, args);
    }
}