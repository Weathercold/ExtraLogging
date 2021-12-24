package logging;

import rhino.json.JsonParser;
import arc.Events;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public static final Seq<Class<? extends Object>> listeningEvents = Seq.with(
        ClientLoadEvent.class,
        ContentInitEvent.class,
        FileTreeInitEvent.class,
        WorldLoadEvent.class,
        PlayEvent.class
    );

    public final Seq<String> listeningMethods = Seq.with(
    );

    public ExtraLogging() throws NoSuchMethodException, SecurityException{
        Log.formatter = new ExtraLogFormatter();

        Log.info("[EL] ExtraLogging.ExtraLogging()");

        listeningEvents.each(c -> Events.on(c, e -> Log.info("[EL] " + c.getName()) ));
    }

    @Override
    public void init(){
        Log.info("[EL] ExtraLogging.init()");

        Vars.enableConsole = true;
    }

    @Override
    public void loadContent(){
        Log.info("[EL] ExtraLogging.loadContent()");
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        Log.info("[EL] ExtraLogging.registerServerCommands()");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        Log.info("[EL] ExtraLogging.registerClientCommands()");
    }
}
