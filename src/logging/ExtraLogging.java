package logging;

import static logging.ExtraVars.*;

import java.lang.reflect.Field;

import arc.Core;
import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import logging.util.ExtraLogHandler;
import mindustry.Vars;
import mindustry.mod.Mod;

public class ExtraLogging extends Mod{
    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];
        
        if (enableMetaLogging) Log.logger.log(metaLogLevel, "[EL] ExtraLogging()");

        if (!enableEventLogging) return;
        listeningEvents.each(c -> Events.on(c, e -> {
            String fields = "";
            for (Field field : c.getDeclaredFields()){
                try {fields += " " + field.getName() + "=" + field.get(e);}
                catch (IllegalArgumentException | IllegalAccessException err){}
            }
            
            Log.logger.log(eventLogLevel, "[EL] " + c.getSimpleName() + fields);
        }));
    }

    @Override
    public void init(){
        if (enableMetaLogging) Log.logger.log(metaLogLevel, "[EL] init()");

        settings.init();
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        if (enableMetaLogging) Log.logger.log(metaLogLevel, "[EL] registerServerCommands()");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        if (enableMetaLogging) Log.logger.log(metaLogLevel, "[EL] registerClientCommands()");
    }
}
