package logging.ui;

import arc.func.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import static arc.Core.*;

public class TextSetting extends Setting {
    String def;
    Cons<String> changed;

    public TextSetting(String name, String def, Cons<String> changed) {
        super(name);
        this.def = def;
        this.changed = changed;
    }

    @Override
    public void add(SettingsTable table) {
        TextField field = new TextField();

        field.update(() -> field.setText(settings.getString(name, def)));

        field.changed(() -> {
            settings.put(name, field.getText());
            if(changed != null){
                changed.get(field.getText());
            }
        });

        Table prefTable = table.table().left().padTop(3f).get();
        prefTable.add(field);
        prefTable.label(() -> title);
        addDesc(prefTable);
        table.row();
    }
}
