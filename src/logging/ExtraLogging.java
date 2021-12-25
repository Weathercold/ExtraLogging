package logging;

import java.lang.reflect.Field;

import arc.Events;
import arc.func.Cons;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.mod.Mod;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public static Seq<Class<? extends Object>> listeningEvents = Seq.with(
        //In chronological order
        FileTreeInitEvent.class,
        ContentInitEvent.class,
        WorldLoadEvent.class,
        ClientLoadEvent.class,
        ClientPreConnectEvent.class,
        StateChangeEvent.class,
        PlayEvent.class
    );

    public ExtraLogging(){        
        Log.formatter = new ExtraLogFormatter();
        
        Log.info("[EL] " + ExtraLogging.class);

        registerEvents();
        wrapMethods();
    }
    
    private void registerEvents(){
        listeningEvents.each(c -> Events.on(c, e -> {
            String dataString = "";
            for (Field datum : c.getDeclaredFields()){
                try {dataString += " " + datum.get(e);}
                catch (IllegalArgumentException | IllegalAccessException err){}
            }
            
            Log.info("[EL] " + c.getName() + dataString);
        }));
    }

    private void wrapMethods(){
        
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
