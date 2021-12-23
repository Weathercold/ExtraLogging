package logging;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public Seq<Class<? extends Object>> listeningEvents = Seq.with(
        ClientLoadEvent.class,
        ContentInitEvent.class,
        FileTreeInitEvent.class,
        WorldLoadEvent.class,
        PlayEvent.class
    );
    
    public ExtraLogging(){
        Log.info("ExtraLogging.ExtraLogging()");

        listeningEvents.each(c -> Events.on(c, e -> Log.info(c.getName()) ));
    }

    @Override
    public void init(){
        Log.info("ExtraLogging.init()");

        Vars.enableConsole = true;
    }

    @Override
    public void loadContent(){
        Log.info("ExtraLogging.loadContent()");
    }

}
