package logging.ui;

import static arc.Core.settings;

import arc.func.Cons;
import arc.scene.ui.TextArea;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable;


public class AreaTextSetting extends TextSetting{
    String def;
    Cons<String> changed;
    
    public AreaTextSetting(String name, String def, Cons<String> changed){
        super(name, def, changed);
    }

    @Override
    public void add(SettingsTable table){
        TextArea area = new TextArea("");
        area.setPrefRows(5);

        area.update(() -> {
            area.setText(settings.getString(name, def));
            area.setWidth(table.getWidth());
        });

        area.changed(() -> {
            settings.put(name, area.getText());
            if (changed != null) changed.get(area.getText());
        });

        addDesc(table.label(() -> title).left().padTop(3f).get());
        table.row().add(area).left();
        table.row();
    }
    
}