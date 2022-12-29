package logging.ui.fragments;

import arc.Core;
import logging.util.Translating;
import mindustry.ui.fragments.ChatFragment;

import static logging.ExtraVars.*;

/** Finally, no reflection */
public class ExtraChatFragment extends ChatFragment{
    public ExtraChatFragment(){
        super();

        //Remove the previous chat fragment
        ChatFragment defFrag = (ChatFragment)Core.scene.find(f -> f instanceof ChatFragment);
        defFrag.clear();
        defFrag.remove();

        Core.scene.add(this);
    }

    @Override
    public void addMessage(String message){
        super.addMessage(message);

        if(enableTranslation) Translating.translate(message, targetLang, translation -> {
            if(!translation.equals(message)) super.addMessage(translation);
        });
    }
}
