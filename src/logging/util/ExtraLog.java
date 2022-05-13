package logging.util;

import static logging.ExtraVars.*;

import java.io.*;

import arc.*;
import arc.util.*;
import arc.util.Log.*;

/** {@link arc.util.Log} Wrapper
 * @author Weathercold
 */
public class ExtraLog{
    public static void log(LogLevel level, String text, Object... args){
        if (text.startsWith("@")){
            Log.log(level, metaColor + "[EL] " + Core.bundle.format(text, args));
        }else{
            Log.log(level, metaColor + "[EL] " + text, args);
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

    public static void err(String text, Object... args){
        log(LogLevel.err, text, args);
    }

    public static void err(String text, Throwable th){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        th.printStackTrace(pw);
        err(text + "\n" + sw);
    }
}