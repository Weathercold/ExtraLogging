package logging.ui.dialogs;

import arc.util.Log.LogLevel;
import arc.util.OS;
import logging.ExtraVars;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;

import static logging.ExtraVars.isFoo;

public class ExtraSettingsDialog{
    @SuppressWarnings("unused")
    public SettingsTable t;

    @SuppressWarnings("ConstantConditions")
    public ExtraSettingsDialog(){
        Vars.ui.settings.addCategory("@extra-logging.displayname", Icon.wrench, st -> {
            t = st;

            //region Basic

            st.sliderPref("extra-loglevel", 0, 0, 4, v -> LogLevel.values()[v].name());
            st.checkPref("extra-coloredterminal", !OS.isWindows && !OS.isAndroid);
            st.checkPref("extra-enableeventlogging", false);
            if(!isFoo) st.checkPref("extra-enabletranslation", !isFoo);

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
