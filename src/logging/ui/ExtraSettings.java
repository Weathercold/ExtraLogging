package logging.ui;

import arc.scene.Group;
import arc.Core;
import arc.Events;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.OS;
import arc.util.Log.LogLevel;
import logging.ExtraVars;
import mindustry.Vars;
import mindustry.game.EventType.ResizeEvent;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;

/** Credits to meep for template https://github.com/MEEPofFaith/testing-utilities-java/blob/master/src/testing/content/TUSettings.java */
public class ExtraSettings{
    public BaseDialog dialog;
    public SettingsTable settings;

    public void init(){
        dialog = new BaseDialog("@extra-logging.displayName");
        dialog.addCloseButton();

        settings = new SettingsTable();
        settings.sliderPref("extra-loglevel", 0, 0, 4, v -> {
            LogLevel level = LogLevel.values()[v];
            Log.level = level;
            return level.name();
        });
        settings.checkPref("extra-coloredjavaconsole", !OS.isWindows && !OS.isAndroid, v -> {ExtraVars.coloredJavaConsole = v;});
        settings.row();
        settings.checkPref("extra-enablemetalogging", true, v -> {ExtraVars.enableMetaLogging = v;});
        settings.sliderPref("extra-metaloglevel", 0, 0, 4, v -> {
            LogLevel level = LogLevel.values()[v];
            ExtraVars.metaLogLevel = level;
            return level.name();
        });
        settings.row();
        settings.checkPref("extra-enableeventlogging", true, v -> {ExtraVars.enableEventLogging = v;});
        settings.sliderPref("extra-eventloglevel", 0, 0, 4, v -> {
            LogLevel level = LogLevel.values()[v];
            ExtraVars.eventLogLevel = level;
            return level.name();
        });

        dialog.cont.center().add(settings);

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
