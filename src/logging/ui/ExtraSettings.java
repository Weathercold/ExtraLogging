package logging.ui;

import static arc.Core.scene;
import static arc.Core.settings;
import static logging.ExtraVars.*;

import java.time.format.DateTimeFormatter;

import arc.scene.Group;
import arc.Events;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.OS;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.game.EventType.ResizeEvent;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;

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

        Vars.ui.settings.hidden(this::update);
    }

    public void update(){
        Log.level = LogLevel.values()[settings.getInt("extra-loglevel")];
        coloredTerminal = settings.getBool("extra-coloredterminal");
        enableEventLogging = settings.getBool("extra-enableeventlogging");
        enableTranslation = settings.getBool("extra-enabletranslation") && !isFoo && !Vars.headless;

        logf = settings.getString("extra-logformat");
        try{timef = DateTimeFormatter.ofPattern(settings.getString("extra-timestampformat"));}
        catch (Throwable ignored){timef = DateTimeFormatter.ISO_LOCAL_TIME;}
        enableMetaDebugging = settings.getBool("extra-enablemetadebugging");
        metaColor = settings.getString("extra-metacolor");
        eventLogLevel = LogLevel.values()[settings.getInt("extra-eventloglevel")];
    }
}
