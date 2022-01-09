package logging;

import static logging.ExtraVars.*;

import java.lang.reflect.Field;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Reflect;
import logging.ui.ExtraChatFragment;
import logging.util.ExtraLogHandler;
import logging.util.Translating;
import mindustry.Vars;
import mindustry.mod.Mod;

public class ExtraLogging extends Mod{
    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];
        
        if (enableMetaDebugging) Log.debug("[EL] ExtraLogging()");
        Log.info("[EL] Current Mindustry language: @", lang); //Not meta
        
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
        if (enableTranslation && !isFoo) Vars.ui.chatfrag = new ExtraChatFragment();
        else if (enableTranslation) try{
            Events.on(Class.forName("mindustry.game.EventType$PlayerChatEventClient"), e -> {
                String message = Reflect.get(e, "message");
                Translating.translate(message, lang, translation -> {
                    if (!translation.equals(message)) Reflect.invoke(Vars.ui.chatfrag, "addMessage", new Object[]{translation, "[gray]Translation[]", Color.sky}, String.class, String.class, Color.class);
                });
            });
        }catch (ClassNotFoundException e){Log.err(e);}
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
