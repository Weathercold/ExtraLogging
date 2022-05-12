package logging.core;

import logging.ui.dialogs.*;

public class ExtraUI{
    public ExtraSettingsDialog settings;

    public void init(){
        settings = new ExtraSettingsDialog();
    }
}
