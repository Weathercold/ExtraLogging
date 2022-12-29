package logging.core;

import logging.ui.dialogs.*;

public class ExtraUI{
    @SuppressWarnings("unused")
    public ExtraSettingsDialog settings;

    public void init(){
        settings = new ExtraSettingsDialog();
    }
}
