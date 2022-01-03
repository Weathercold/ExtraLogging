package logging.util;

import static mindustry.Vars.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import arc.Events;
import arc.graphics.Colors;
import arc.struct.Seq;
import arc.util.ColorCodes;
import arc.util.Log;
import arc.util.Log.LogHandler;
import arc.util.Log.LogLevel;
import mindustry.game.EventType.ClientLoadEvent;

/** I know you can just use a lambda for logger but this is way clearer */
public class ExtraLogHandler implements LogHandler{
    public String stmpl = "@ &lk[@]&fr @";
    public String[] stags = {"&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]&fr", "   "};
    public String tmpl = "@ [grey][@] [@]@";
    /** Why isn't terminal font monospaced */
    public String[] tags = {"[green][D]", "[royal][I] ", "[yellow][W]", "[scarlet][E]", "    "};
    public DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    Seq<String> logBuffer = new Seq<>();
    
    public ExtraLogHandler(){
        Events.on(ClientLoadEvent.class, e -> logBuffer.each(ui.scriptfrag::addMessage));
    }
    
    @Override
    public void log(LogLevel level, String text){
        String rtext = Log.format(stmpl, stags[level.ordinal()], timef.format(LocalTime.now()), removeColorNames(text));
        System.out.println(rtext);

        if(!headless){
            String ftext = removeColorsANSI(Log.formatColors(tmpl, false, tags[level.ordinal()], timef.format(LocalTime.now()), text.startsWith("[EL]") ? "accent" : "white", text));
            if (ui == null || ui.scriptfrag == null) logBuffer.add(ftext);
            else ui.scriptfrag.addMessage(ftext);
        }
    }

    public String removeColorsANSI(String text){
        for (String value : ColorCodes.values){
            text = text.replace(value, "");
        }
        return text;
    }

    public String removeColorNames(String text){
        for (String name : Colors.getColors().keys()){
            text = text.replace("[" + name + "]", "");
        }
        text = text.replace("[]", "");
        return text;
    }
}
