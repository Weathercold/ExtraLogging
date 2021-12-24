package logging;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import arc.util.Log;
import arc.util.Log.DefaultLogFormatter;
import arc.util.Strings;

public class ExtraLogFormatter extends DefaultLogFormatter{
    public static DateTimeFormatter timef = DateTimeFormatter.ISO_LOCAL_TIME;
    public static String tmpl = "[grey]@ [@]@";

    @Override
    public String format(String text, boolean useColors, Object... args){
        text = Strings.format(text, args);
        String ftext = Strings.format(tmpl, timef.format(LocalTime.now()), (text.startsWith("[EL]") ? "blue" : ""), text);
        
        return useColors ? Log.addColors(ftext) : Log.removeColors(ftext);
    }
}
