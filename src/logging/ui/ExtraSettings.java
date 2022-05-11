package logging.ui;

import static arc.Core.*;
import static logging.ExtraVars.*;

import arc.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.util.Log.*;
import arc.util.*;
import logging.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;

/** Credits to meep for template https://github.com/MEEPofFaith/testing-utilities-java/blob/master/src/testing/content/TUSettings.java */
public class ExtraSettings{
    public BaseDialog dialog;
    public SettingsTable table;

    public void init(){
        dialog = new BaseDialog("@extra-logging.displayName");
        dialog.addCloseButton();
        dialog.pane(b -> {});
        
        //region Basic
        
        dialog.cont.add(table = new SettingsTable());
        table.sliderPref("extra-loglevel", 0, 0, 4, v -> LogLevel.values()[v].name());
        table.checkPref("extra-coloredterminal", !OS.isWindows && !OS.isAndroid);
        table.checkPref("extra-enableeventlogging", false);
        if (!isFoo) table.checkPref("extra-enabletranslation", !isFoo);
        
        //endregion
        //region Advanced
        
        table.pref(new AreaTextSetting("extra-logformat", "[gray][$t][] &fb$L[$l][] $M$m[]", null));
        table.pref(new TextSetting("extra-timestampformat", "HH:mm:ss.SSS", null));
        table.checkPref("extra-enablemetadebugging", false);
        table.pref(new TextSetting("extra-metacolor", "[accent]", null));
        table.sliderPref("extra-eventloglevel", 0, 0, 4, v -> LogLevel.values()[v].name());
        
        //endregion
        //region Listeners

        Events.on(ResizeEvent.class, e -> {
            if(dialog.isShown() && scene.getDialog() == dialog){
                dialog.updateScrollFocus();
            }
        });

        //Add button on setting open
        Vars.ui.settings.shown(() -> {
            Table settingUi = (Table)((Group)((Group)(Vars.ui.settings.getChildren().get(1))).getChildren().get(0)).getChildren().get(0);
            settingUi.row();
            settingUi.button("@extra-logging.displayName", Styles.cleart, dialog::show);
        });

        Vars.ui.settings.hidden(ExtraVars::refreshenv);
    }
}
