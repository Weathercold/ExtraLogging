package logging.ui;

import arc.func.*;
import arc.scene.ui.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;

import static arc.Core.*;

public class AreaTextSetting extends TextSetting {
    String def;
    Cons<String> changed;
    
    public AreaTextSetting(String name, String def, Cons<String> changed) {
        super(name, def, changed);
    }

    @Override
    public void add(SettingsTable table) {
        TextArea area = new TextArea("");
        area.setPrefRows(5);

        area.update(() -> {
            area.setText(settings.getString(name, def));
            area.setWidth(table.getWidth());
        });

        area.changed(() -> {
            settings.put(name, area.getText());
            if(changed != null){
                changed.get(area.getText());
            }
        });

        addDesc(table.label(() -> title).left().padTop(3f).get());
        table.row().add(area).left();
        table.row();
    }
    
}
