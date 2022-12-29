package logging.util;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log.*;
import mindustry.game.EventType.ClientLoadEvent;

import java.time.LocalTime;

import static logging.ExtraVars.*;
import static logging.util.ColorUtils.*;
import static mindustry.Vars.ui;
import static mindustry.Vars.*;

/**
 * I know you can just use a lambda for logger but this is way clearer
 * @author Weathercold
 */
public class ExtraLogHandler implements LogHandler{
    public static final String[]
        l = {"D", "I", "W", "E", "/"},
        L = {"[green]", "[royal]", "[yellow]", "[scarlet]", "[lightgray]"};
    public static boolean clientLoaded = false;

    static{
        Events.on(ClientLoadEvent.class, e -> clientLoaded = true);
    }

    public final Seq<String> logBuffer = new Seq<>();

    public ExtraLogHandler(){
        Events.on(ClientLoadEvent.class, e -> logBuffer.each(ui.consolefrag::addMessage));
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
            if(clientLoaded) ui.consolefrag.addMessage(formatCons(text));
            else logBuffer.add(formatCons(text));
        }
    }
}
