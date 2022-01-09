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
    public String stmpl = "@ &lk[@]&fr @@&fr";
    public String[] stags = {"&lg&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]&fr", "   "};
    public String tmpl = "@ [grey][@][] @@";
    /** Why isn't terminal font monospaced */
    public String[] tags = {"[green][D][]", "[royal][I][] ", "[yellow][W][]", "[scarlet][E][]", "    "};
    public DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public Seq<String> logBuffer = new Seq<>();
    public static boolean clientLoaded = false;
    
    static{Events.on(ClientLoadEvent.class, e -> {clientLoaded = true;});}

    public ExtraLogHandler(){
        Events.on(ClientLoadEvent.class, e -> logBuffer.each(ui.scriptfrag::addMessage));
    }
    
    @Override
    public void log(LogLevel level, String text){
        String time = timef.format(LocalTime.now());
        String rtext = formatColors(stmpl, coloredJavaConsole, stags[level.ordinal()], time, text.startsWith("[EL]") ? "&ly" : "", text);
        System.out.println(rtext);

        if(!headless){
            String ftext = formatCons(tmpl, tags[level.ordinal()], time, text.startsWith("[EL]") ? "[accent]" : "", text);
            if (clientLoaded) ui.scriptfrag.addMessage(ftext);
            else logBuffer.add(ftext);
        }
    }
}