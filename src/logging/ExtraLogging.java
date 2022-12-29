package logging;

import arc.*;
import arc.struct.*;
import arc.util.*;
import logging.ui.fragments.*;
import logging.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.mod.*;

import java.lang.reflect.*;

import static logging.ExtraVars.*;
import static logging.util.ColorUtils.parseMsg;
import static logging.util.ExtraLog.*;
import static logging.util.Translating.translate;

@SuppressWarnings("unchecked")
public class ExtraLogging extends Mod{
    public ExtraLogging(){
        Log.logger = new ExtraLogHandler();
        Log.level = Log.LogLevel.values()[Core.settings.getInt("extra-loglevel", 0)];

        debug("Creating mod");

        //Register events
        if(enableEventLogging) listeningEvents.each(c -> Events.on(c, e -> {
            StringBuilder fields = new StringBuilder();
            for(Field field : c.getDeclaredFields())
                try{
                    fields.append(" ").append(field.getName()).append("=").append(field.get(e));
                }catch(IllegalArgumentException | IllegalAccessException ignored){
                }

            log(eventLogLevel, c.getSimpleName() + fields);
        }));
    }

    @Override
    public void init(){
        debug("Initializing");

        ui.init();
        if(enableTranslation) Vars.ui.chatfrag = new ExtraChatFragment();
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        debug("Registering commands");
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        debug("Registering commands");

        if(enableTranslation)
            handler.register("tl", "[lang] [message...]", Core.bundle.get("extra-logging.command.tl.description"), (String[] args, Player player) -> {
                switch(args.length){
                    case 0 -> {
                        Object message = ((Seq<Object>)Reflect.get(Vars.ui.chatfrag, "messages")).firstOpt();
                        if(message == null) return;
                        String cont = parseMsg(Reflect.get(message, "message"));
                        translate(cont, targetLang, translation -> Vars.ui.chatfrag.addMessage("Translation: " + translation));
                    }
                    case 1 ->
                        translate(args[0], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                    case 2 -> {
                        if(supportedLangs.contains(args[0]))
                            translate(args[1], args[0], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                        else
                            translate(args[0] + args[1], translation -> Call.sendChatMessage(translation + " [gray](translated)"));
                    }
                }
            });
    }
}
