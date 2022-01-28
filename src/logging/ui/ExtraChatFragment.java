package logging.ui;

import static logging.ExtraVars.*;
import static mindustry.Vars.*;

import arc.Core;
import logging.util.Translating;
import mindustry.ui.fragments.ChatFragment;

/** Finally, no reflection */
public class ExtraChatFragment extends ChatFragment{
    public ExtraChatFragment(){
        super();

        //Remove the previous chat fragment
        ChatFragment defFrag = (ChatFragment)Core.scene.find(f -> f instanceof ChatFragment);
        defFrag.clear();
        defFrag.remove();
        defFrag = null;

        Core.scene.add(this);
    }

    @Override
    public void addMessage(String message, String sender){
        super.addMessage(message, sender);

        if (enableTranslation && sender != player.name) Translating.translate(message, targetLang, translation -> {
            if (!translation.equals(message)) super.addMessage(translation, "Translation");
        });
    }
}
