package logging.util;

import arc.struct.Seq;
import arc.struct.StringMap;
import arc.struct.ObjectMap.Entry;
import arc.util.Strings;

/** {@link arc.util.ColorCodes} sucks, use this one
 * @author Weathercold
 */
public class ColorUtils{
    public static String
    flush = "\033[H\033[2J",
    reset = "\u001B[0m",
    bold = "\u001B[1m",
    italic = "\u001B[3m",
    underline = "\u001B[4m",

    black = "\u001B[30m",
    red = "\u001B[31m",
    green = "\u001B[32m",
    yellow = "\u001B[33m",
    blue = "\u001B[34m",
    purple = "\u001B[35m",
    cyan = "\u001B[36m",
    lightBlack = "\u001b[90m",
    lightRed = "\u001B[91m",
    lightGreen = "\u001B[92m",
    lightYellow = "\u001B[93m",
    lightBlue = "\u001B[94m",
    lightMagenta = "\u001B[95m",
    lightCyan = "\u001B[96m",
    lightWhite = "\u001b[97m",
    white = "\u001B[37m",

    backDefault = "\u001B[49m",
    backRed = "\u001B[41m",
    backGreen = "\u001B[42m",
    backYellow = "\u001B[43m",
    backBlue = "\u001B[44m";

    public static StringMap
    codeMap = StringMap.of(
        "ff", flush,
        "fr", reset,
        "fb", bold,
        "fi", italic,
        "fu", underline,

        "k", black,
        "lk", lightBlack,
        "lw", lightWhite,
        "r", red,
        "g", green,
        "y", yellow,
        "b", blue,
        "p", purple,
        "c", cyan,
        "lr", lightRed,
        "lg", lightGreen,
        "ly", lightYellow,
        "lm", lightMagenta,
        "lb", lightBlue,
        "lc", lightCyan,
        "w", white,

        "bd", backDefault,
        "br", backRed,
        "bg", backGreen,
        "by", backYellow,
        "bb", backBlue
    ),

    nameMap = StringMap.of(
        "", reset,
        "clear", "",
        "black", black,

        "white", white,
        "lightgray", lightWhite,
        "gray", lightBlack,
        "darkgray", lightBlack,
        "lightgrey", lightWhite,
        "grey", lightBlack,
        "darkgrey", lightBlack,

        "blue", blue,
        "navy", blue,
        "royal", blue,
        "slate", blue,
        "sky", lightBlue,
        "cyan", cyan,
        "teal", cyan,

        "green", green,
        "acid", green,
        "lime", lightGreen,
        "forest", green,
        "olive", green,

        "yellow", yellow,
        "gold", yellow,
        "goldenrod", yellow,
        "orange", yellow,

        "brown", yellow,
        "tan", yellow,
        "brick", yellow,

        "red", red,
        "scarlet", red,
        "crimson", red,
        "coral", red,
        "salmon", red,
        "pink", lightRed,
        "magenta", lightMagenta,

        "purple", purple,
        "violet", purple,
        "maroon", purple,

        "accent", yellow,
        "unlaunched", purple,
        "highlight", lightYellow,
        "stat", lightBlack
    );

    public static Seq<String>
    colors = codeMap.values().toSeq(),
    codes = codeMap.keys().toSeq(),
    names = nameMap.keys().toSeq();

    public static String formatColors(String text, boolean useColors, Object... args){
        text = Strings.format(text, args);
        return useColors ? convertNames(convertCodes(text)) : removeColors(removeNames(removeCodes(text)));
    }

    /** Format for console output, ie. remove colors and codes. */
    public static String formatCons(String text, Object... args){
        return removeColors(removeCodes(Strings.format(text, args)));
    }
    
    /** Convert codes to colors. */
    public static String convertCodes(String text){
        for (Entry<String, String> e : codeMap) text = text.replace("&" + e.key, e.value);
        return text;
    }
    
    /** Convert names to colors. */
    public static String convertNames(String text){
        for (Entry<String, String> e : nameMap) text = text.replace("[" + e.key + "]", e.value);
        return text;
    }

    /** Remove all colors. */
    public static String removeColors(String text){
        for (String color : colors) text = text.replace(color, "");
        return text;
    }
    
    /** Remove all codes. */
    public static String removeCodes(String text){
        for (String code : codes) text = text.replace("&" + code, "");
        return text;
    }

    /** Remove all names. */
    public static String removeNames(String text){
        for (String name : names) text = text.replace("[" + name + "]", "");
        return text;
    }
}
