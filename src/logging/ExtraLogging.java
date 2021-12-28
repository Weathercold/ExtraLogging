package logging;

import java.lang.reflect.Field;

import arc.Events;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public static Seq<Class<? extends Object>> listeningEvents = Seq.with(
        FileTreeInitEvent.class,
        ContentInitEvent.class,
        WorldLoadEvent.class,
        ClientLoadEvent.class,
        ClientPreConnectEvent.class,
        StateChangeEvent.class
    );

    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.formatter = new ExtraLogFormatter();
        Log.level = Log.LogLevel.debug;
        
        Log.info("[EL] ExtraLogging()");

        registerEvents();
    }
    
    private void registerEvents(){
        listeningEvents.each(c -> Events.on(c, e -> {
            String dataString = "";
            for (Field datum : c.getDeclaredFields()){
                try {dataString += " " + datum.getName() + "=" + datum.get(e);}
                catch (IllegalArgumentException | IllegalAccessException err){}
            }
            
            Log.info("[EL] " + c.getSimpleName() + dataString);
        }));
    }

    @Override
    public void init(){
        Log.info("[EL] init()");
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        Log.info("[EL] registerServerCommands()");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        Log.info("[EL] registerClientCommands()");
    }
}
