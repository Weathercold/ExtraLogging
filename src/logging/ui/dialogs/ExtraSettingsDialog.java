package logging.ui.dialogs;

import static logging.ExtraVars.*;

import arc.util.Log.*;
import arc.util.*;
import logging.*;
import mindustry.*;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.SettingsMenuDialog.*;

public class ExtraSettingsDialog{
    public SettingsTable t;

    public ExtraSettingsDialog(){
        Vars.ui.settings.addCategory("@extra-logging.displayname", Icon.wrench, st -> {
            t = st;

            //region Basic
            
            st.sliderPref("extra-loglevel", 0, 0, 4, v -> LogLevel.values()[v].name());
            st.checkPref("extra-coloredterminal", !OS.isWindows && !OS.isAndroid);
            st.checkPref("extra-enableeventlogging", false);
            if (!isFoo) st.checkPref("extra-enabletranslation", !isFoo);
        
            //endregion
            //region Advanced
            
            st.labelWrap("");
            st.areaTextPref("extra-logformat", "[gray][$t][] &fb$L[$l][] $m[]", null);
            st.textPref("extra-timestampformat", "HH:mm:ss.SSS", null);
            st.checkPref("extra-enablemetadebugging", false);
            st.textPref("extra-metacolor", "[accent]", null);
            st.sliderPref("extra-eventloglevel", 0, 0, 4, v -> LogLevel.values()[v].name());
            //endregion

        });
        
        //region Listeners

        Vars.ui.settings.hidden(ExtraVars::refreshenv);
    }
}
