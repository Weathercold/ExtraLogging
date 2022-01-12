package logging.ui;

import static logging.ExtraVars.*;

import arc.scene.Group;
import arc.Core;
import arc.Events;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.OS;
import arc.util.Log.LogLevel;
import logging.util.TextSetting;
import mindustry.Vars;
import mindustry.game.EventType.ResizeEvent;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;

/** Credits to meep for template https://github.com/MEEPofFaith/testing-utilities-java/blob/master/src/testing/content/TUSettings.java */
public class ExtraSettings{
    public BaseDialog dialog;
    public SettingsTable settings;
    public TextButton advButton;

    public void init(){
        dialog = new BaseDialog("@extra-logging.displayName");
        dialog.addCloseButton();
        dialog.pane(b -> {});
        
        //region Basic
        
        dialog.cont.add(settings = new SettingsTable());
        settings.sliderPref("extra-loglevel", 0, 0, 4, v -> {
            LogLevel level = LogLevel.values()[v];
            Log.level = level;
            return level.name();
        });
        settings.checkPref("extra-coloredjavaconsole", !OS.isWindows && !OS.isAndroid, v -> coloredJavaConsole = v);
        settings.checkPref("extra-enableeventlogging", false, v -> enableEventLogging = v);
        if (!isFoo) settings.checkPref("extra-enabletranslation", true, v -> enableTranslation = v);
        //else basicSettings.add("@extra-logging.fooNotice", Color.gray);
        
        //endregion
        //region Advanced
        
        settings.row();
        settings.pref(new TextSetting("extra-metacolor", "[accent]", v -> metaColor = v));
        settings.checkPref("extra-enablemetadebugging", false, v -> enableMetaDebugging = v);
        settings.sliderPref("extra-eventloglevel", 0, 0, 4, v -> {
            LogLevel level = LogLevel.values()[v];
            eventLogLevel = level;
            return level.name();
        });
        
        //endregion
        //region Listeners

        Events.on(ResizeEvent.class, e -> {
            if(dialog.isShown() && Core.scene.getDialog() == dialog){
                dialog.updateScrollFocus();
            }
        });

        //Add button on setting open
        Vars.ui.settings.shown(() -> {
            Table settingUi = (Table)((Group)((Group)(Vars.ui.settings.getChildren().get(1))).getChildren().get(0)).getChildren().get(0);
            settingUi.row();
            settingUi.button("@extra-logging.displayName", Styles.cleart, dialog::show);
        });
    }
}
