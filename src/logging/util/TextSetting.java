package logging.util;

import arc.func.Cons;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.Setting;

import static arc.Core.*;

public class TextSetting extends Setting {
    public String def;
    public Cons<String> changed;

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
