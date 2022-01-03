package logging.util;

import arc.graphics.Colors;
import arc.util.ColorCodes;

public class ExtraUtils{
    public static String removeColorsANSI(String text){
        for (String value : ColorCodes.values){
            text = text.replace(value, "");
        }
        return text;
    }

    public static String removeColorNames(String text){
        for (String name : Colors.getColors().keys()){
            text = text.replace("[" + name + "]", "");
        }
        text = text.replace("[]", "");
        return text;
    }
}
