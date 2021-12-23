package logging;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;

public class ExtraLogging extends Mod{

    public ExtraLogging(){
        Log.info("ExtraLogging.ExtraLogging()");

        Events.on(ClientLoadEvent.class, e -> Log.info("ClientLoadEvent"));

        Events.on(ContentInitEvent.class, e -> Log.info(""));

        Events.on(WorldLoadEvent.class, e -> Log.info("WorldLoadEvent"));
    }

    @Override
    public void init(){
        Log.info("ExtraLogging.init()");
    }

    @Override
    public void loadContent(){
        Log.info("ExtraLogging.loadContent()");
    }

}
