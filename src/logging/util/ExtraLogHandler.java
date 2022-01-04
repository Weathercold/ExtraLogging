package logging.util;

import static logging.ExtraVars.coloredJavaConsole;
import static logging.util.ColorUtils.*;
import static mindustry.Vars.headless;
import static mindustry.Vars.ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log.LogHandler;
import arc.util.Log.LogLevel;
import mindustry.game.EventType.ClientLoadEvent;

/** I know you can just use a lambda for logger but this is way clearer
 * @author Weathercold
 */
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
        String rtext = formatColors(stmpl, coloredJavaConsole, stags[level.ordinal()], timef.format(LocalTime.now()), removeNames(text));
        System.out.println(rtext);

        if(!headless){
            String ftext = formatCons(tmpl, tags[level.ordinal()], timef.format(LocalTime.now()), text.startsWith("[EL]") ? "accent" : "white", text);
            if (ui == null || ui.scriptfrag == null) logBuffer.add(ftext);
            else ui.scriptfrag.addMessage(ftext);
        }
    }
}
