package logging;

import static logging.ExtraVars.*;
import static logging.util.ColorUtils.*;
import static logging.util.Translating.*;

import java.lang.reflect.Field;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Reflect;
import logging.ui.ExtraChatFragment;
import logging.util.ExtraLogHandler;
import mindustry.Vars;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.mod.Mod;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];
        
        if (enableMetaDebugging) Log.debug("[EL] Creating mod");
        
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
        if (enableMetaDebugging) Log.debug("[EL] Initializing");

        settings.init();
        if (enableTranslation) Vars.ui.chatfrag = new ExtraChatFragment();
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        if (enableMetaDebugging) Log.debug("[EL] Registering commands");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        if (enableMetaDebugging) Log.debug("[EL] Registering commands");

        handler.register("tl", "[lang] [message...]", Core.bundle.get("extra-logging.command.tl.description"), (String[] args, Player player) -> {
            switch (args.length){
                case 0:
                    Object message = ((Seq<Object>)Reflect.get(Vars.ui.chatfrag, "messages")).firstOpt();
                    if (message == null) return;
                    String cont = parseMsg(Reflect.get(message, "message"));
                    translate(cont, targetLang, translation -> Vars.ui.chatfrag.addMessage("", "Translation: " + translation));
                    break;
                case 1:
                    translate(args[0], "en", translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                    break;
                case 2:
                    if (supportedLangs.contains(args[0])) translate(args[1], args[0], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                    else translate(args[0] + args[1], "en", translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                    break;
            }
        });
    }
}
