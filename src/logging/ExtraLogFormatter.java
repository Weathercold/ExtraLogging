package logging;

import arc.util.Log.DefaultLogFormatter;

public class ExtraLogFormatter extends DefaultLogFormatter{
    @Override
    public String format(String text, boolean useColors, Object... args){
        String ftext = "[grey]" + "" + (text.startsWith("[EL]") ? " [blue]" : " ") + text;
        return super.format(ftext, useColors, args);
    }
}
