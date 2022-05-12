package logging;

import static logging.ExtraVars.*;
import static logging.util.ColorUtils.*;
import static logging.util.ExtraLog.*;
import static logging.util.Translating.*;

import java.lang.reflect.*;

import arc.*;
import arc.struct.*;
import arc.util.*;
import logging.ui.fragments.*;
import logging.util.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.mod.*;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public ExtraLogging(){        
        Vars.enableConsole = true;
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];
        
        if (enableMetaDebugging) debug("Creating mod");
        
        //Register events
        if (enableEventLogging) listeningEvents.each(c -> Events.on(c, e -> {
            String fields = "";
            for (Field field : c.getDeclaredFields()) try{
                fields += " " + field.getName() + "=" + field.get(e);
            }catch (IllegalArgumentException | IllegalAccessException ignored){}
            
            log(eventLogLevel, c.getSimpleName() + fields);
        }));
    }

    @Override
    public void init(){
        if (enableMetaDebugging) debug("Initializing");

        ui.init();
        if (enableTranslation) Vars.ui.chatfrag = new ExtraChatFragment();
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        if (enableMetaDebugging) debug("Registering commands");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        if (enableMetaDebugging) debug("Registering commands");

        if (enableTranslation) handler.register("tl", "[lang] [message...]", Core.bundle.get("extra-logging.command.tl.description"), (String[] args, Player player) -> {
            switch (args.length){
                case 0 -> {
                    Object message = ((Seq<Object>)Reflect.get(Vars.ui.chatfrag, "messages")).firstOpt();
                    if (message == null) return;
                    String cont = parseMsg(Reflect.get(message, "message"));
                    translate(cont, targetLang, translation -> Vars.ui.chatfrag.addMessage("Translation: " + translation));
                }
                case 1 -> translate(args[0], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                case 2 -> {
                    if (supportedLangs.contains(args[0]))
                        translate(args[1], args[0], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                    else
                        translate(args[0] + args[1], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                }
            }
        });
    }
}
