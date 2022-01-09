package logging.ui;

import static logging.ExtraVars.*;

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

    /** This method is deprecated in Foo, which means it is only called on vanilla */
    @Override
    public void addMessage(String message, String sender){
        super.addMessage(message, sender);

        Translating.translate(message, lang, translation -> {
            if (!translation.equals(message)) super.addMessage(translation, "[gray]Translation[]");
        });
    }
}
