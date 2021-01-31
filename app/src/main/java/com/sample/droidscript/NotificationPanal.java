package com.sample.droidscript;

public class NotificationPanal {
    public boolean isOpen = false;

    public void logEventOpenStatusBar() {
        this.isOpen = true;
        ScriptLogger.loginst(8, new String[0]);
    }

    public void logEventCloseStatusBar() {
        this.isOpen = false;
        ScriptLogger.loginst(9, new String[0]);
    }
}