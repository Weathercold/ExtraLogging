package logging;

import static logging.ExtraVars.*;

import java.lang.reflect.Field;

import arc.Core;
import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import logging.util.ExtraLogHandler;
import logging.util.Translating;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.ui.fragments.ChatFragment;

public class ExtraLogging extends Mod{
    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];
        
        if (enableMetaLogging) Log.debug("[EL] ExtraLogging()");
        //Register events
        if (enableEventLogging) listeningEvents.each(c -> Events.on(c, e -> {
            String fields = "";
            for (Field field : c.getDeclaredFields()){
                try {fields += " " + field.getName() + "=" + field.get(e);}
                catch (IllegalArgumentException | IllegalAccessException err){}
            }
            
            Log.log(eventLogLevel, "[EL] " + c.getSimpleName() + fields);
        }));
    }

    @Override
    public void init(){
        if (enableMetaLogging) Log.debug("[EL] init()");
        settings.init();
        //Override chat fragment
        if (enableTranslation) Vars.ui.chatfrag = new ChatFragment(){
            @Override
            public void addMessage(String message, String sender){
                super.addMessage(message, sender);
                if (enableTranslation) Translating.translate(message, lang, translation -> {if (!translation.equals(message)) super.addMessage(translation, "[gray]Translation");});
            }
        };
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        if (enableMetaLogging) Log.debug("[EL] registerServerCommands()");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        if (enableMetaLogging) Log.debug("[EL] registerClientCommands()");
    }
}
