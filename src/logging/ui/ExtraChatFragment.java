package logging.ui;

import static logging.ExtraVars.*;

import arc.Core;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Reflect;
import logging.util.Translating;
import mindustry.ui.fragments.ChatFragment;

/** It's hard to do compatibility with Foo <!-- or should I say, Vanilla client? --> */
public class ExtraChatFragment extends ChatFragment{
    public static Class<?> ChatMsg = ChatFragment.class.getDeclaredClasses()[0];

    public ExtraChatFragment(){
        super();

        //In that Light I find Deliverance (points at reflection)
        Reflect.set(ChatFragment.class, this, "messages", new ExtraMessages<>());

        //Remove the chat fragment
        ChatFragment defFrag = (ChatFragment)Core.scene.find(f -> f instanceof ChatFragment);
        defFrag.clear();
        defFrag.remove();
        defFrag = null;

        Core.scene.add(this);
    }

    @SuppressWarnings("unchecked")
    public static class ExtraMessages<T> extends Seq<T>{
        @Override
        public void insert(int index, T value){
            super.insert(index, value);

            if (enableTranslation){
                String message = Reflect.get(value, "message");
                Translating.translate(message, lang, translation -> {
                    if (!translation.equals(message)){
                        Object chatMsg;
                        try{
                            chatMsg = ChatMsg.getConstructor(String.class, String.class).newInstance();
                            super.insert(index, (T)chatMsg);
                        }catch (Exception e){Log.err(e);}
                    }
                });
            }
        }
    }
}
