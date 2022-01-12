package logging;

import static logging.ExtraVars.*;

import java.lang.reflect.Field;

import arc.Core;
import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import logging.ui.ExtraChatFragment;
import logging.util.ExtraLogHandler;
import mindustry.Vars;
import mindustry.mod.Mod;

public class ExtraLogging extends Mod{
    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];
        
        if (enableMetaDebugging) Log.debug("[EL] ExtraLogging()");
        
        //Register events
        if (enableEventLogging) listeningEvents.each(c -> Events.on(c, e -> {
            String fields = "";
            for (Field field : c.getDeclaredFields()) try{
                fields += " " + field.getName() + "=" + field.get(e);
            }catch (IllegalArgumentException | IllegalAccessException ignored){}
            
            Log.log(eventLogLevel, "[EL] " + c.getSimpleName() + fields);
        }));
    }

    @Override
    public void init(){
        if (enableMetaDebugging) Log.debug("[EL] init()");

        settings.init();
        if (enableTranslation) Vars.ui.chatfrag = new ExtraChatFragment();
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        if (enableMetaDebugging) Log.debug("[EL] registerServerCommands()");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        if (enableMetaDebugging) Log.debug("[EL] registerClientCommands()");
    }
}
