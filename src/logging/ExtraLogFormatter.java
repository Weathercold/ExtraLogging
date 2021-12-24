package logging;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import arc.util.Log.DefaultLogFormatter;

public class ExtraLogFormatter extends DefaultLogFormatter{
    DateTimeFormatter timef = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public String format(String text, boolean useColors, Object... args){
        String ftext = "[grey]" + timef.format(Instant.now()) + (text.startsWith("[EL]") ? " [blue]" : " []") + text;
        return super.format(ftext, useColors, args);
    }
}
