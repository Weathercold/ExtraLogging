package logging.ui;

import static logging.ExtraVars.*;

import java.lang.reflect.Constructor;

import arc.Core;
import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Reflect;
import logging.util.Translating;
import mindustry.ui.fragments.ChatFragment;

/** It's hard to do compatibility with Foo <!-- or should I say, Vanilla client? --> */
public class ExtraChatFragment extends ChatFragment{
    public static Constructor<? extends Object> msgCons;
    static{
        try{msgCons = ChatFragment.class.getDeclaredClasses()[1].getConstructor(String.class, String.class);}
        catch (NoSuchMethodException e){
            isFoo = true;
            msgCons = ChatFragment.class.getDeclaredClasses()[1].getConstructors()[0];
        }
    }

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
                        try{
                            Object chatMsg;
                            if (!isFoo) chatMsg = msgCons.newInstance(translation, "[gray]Translation[]");
                            else chatMsg = msgCons.newInstance(translation, "[gray]Translation[]", Color.blue, "", translation);
                            super.insert(index, (T)chatMsg);
                        }catch (Throwable e){Log.err(e);}
                    }
                });
            }
        }
    }
}
