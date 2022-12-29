package logging.util;

import arc.*;
import arc.util.*;
import arc.util.Log.*;

import java.io.*;

import static arc.util.Log.LogLevel.debug;
import static arc.util.Log.LogLevel.err;
import static arc.util.Log.LogLevel.info;
import static arc.util.Log.LogLevel.warn;
import static logging.ExtraVars.*;

/**
 * {@link arc.util.Log} Wrapper
 * @author Weathercold
 */
@SuppressWarnings("unused")
public class ExtraLog{
    public static void log(LogLevel level, String text, Object... args){
        if(level == debug && !enableMetaDebugging) return;
        if(text.startsWith("@"))
            Log.log(level, metaColor + "[EL][] " + Core.bundle.format(text, args));
        else
            Log.log(level, metaColor + "[EL][] " + text, args);
    }

    public static void logList(LogLevel level, Object... args){
        if(level == debug && !enableMetaDebugging) return;
        var build = new StringBuilder().append(metaColor).append("[EL][] ");
        for(Object o : args) build.append(o).append(", ");
        Log.log(level, build.substring(0, build.length() - 1));
    }

    public static void logTh(LogLevel level, Throwable th){
        if(level == debug && !enableMetaDebugging) return;
        StringWriter sw = new StringWriter().append(metaColor).append("[EL][] ");
        th.printStackTrace(new PrintWriter(sw));
        Log.log(level, "" + sw);
    }

    public static void logTh(LogLevel level, String text, Throwable th){
        if(level == debug && !enableMetaDebugging) return;
        StringWriter sw = new StringWriter().append(metaColor).append("[EL][] ");
        th.printStackTrace(new PrintWriter(sw));
        Log.log(level, text + "\n" + sw);
    }

    public static void debug(String text, Object... args){
        log(debug, text, args);
    }

    public static void debugList(Object... args){
        logList(debug, args);
    }

    public static void info(String text, Object... args){
        log(info, text, args);
    }

    public static void infoList(Object... args){
        logList(info, args);
    }

    public static void warn(String text, Object... args){
        log(warn, text, args);
    }

    public static void warn(Throwable th){
        logTh(warn, th);
    }

    public static void warn(String text, Throwable th){
        logTh(warn, text, th);
    }

    public static void err(String text, Object... args){
        log(LogLevel.err, text, args);
    }

    public static void err(Throwable th){
        logTh(err, th);
    }

    public static void err(String text, Throwable th){
        logTh(err, text, th);
    }
}