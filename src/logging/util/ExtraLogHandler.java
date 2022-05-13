package logging.util;

import static logging.ExtraVars.*;
import static logging.util.ColorUtils.*;
import static mindustry.Vars.headless;
import static mindustry.Vars.ui;

import java.time.*;

import arc.*;
import arc.struct.*;
import arc.util.Log.*;
import mindustry.game.EventType.*;

/** I know you can just use a lambda for logger but this is way clearer
 * @author Weathercold
 */
public class ExtraLogHandler implements LogHandler{
    public String[] l = {"D", "I", "W", "E", "/"};
    public String[] L = {"[green]", "[royal]", "[yellow]", "[scarlet]", ""};

    public Seq<String> logBuffer = new Seq<>();
    public static boolean clientLoaded = false;
    
    static{Events.on(ClientLoadEvent.class, e -> clientLoaded = true);}

    public ExtraLogHandler(){
        Events.on(ClientLoadEvent.class, e -> logBuffer.each(ui.scriptfrag::addMessage));
    }
    
    @Override
    public void log(LogLevel level, String text){
        String time = timef.format(LocalTime.now());

        text = logf.replaceAll("\\$t", time)
                   .replaceAll("\\$L", L[level.ordinal()])
                   .replaceAll("\\$l", l[level.ordinal()])
                   .replaceAll("\\$m", text);

        //Java console
        System.out.println(formatColors(text, coloredTerminal));

        //In-game console
        if(!headless){
            if (clientLoaded) ui.scriptfrag.addMessage(formatCons(text));
            else logBuffer.add(formatCons(text));
        }
    }
}
