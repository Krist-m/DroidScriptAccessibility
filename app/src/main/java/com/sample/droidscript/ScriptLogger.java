package com.sample.droidscript;

import android.util.Log;

public class ScriptLogger {
    public static final int INST_CHCECKBOX_LEFT_SIDE = 12;
    public static final int INST_CHCECKBOX_RIGHT_SIDE = 11;
    public static final int INST_CHECKBOX_CLICK = 6;
    public static final int INST_CLICK = 3;
    public static final int INST_GOTO_HOME = 0;
    public static final int INST_NOTIFICATION = 7;
    public static final int INST_PRESS_BACK = 1;
    public static final int INST_PRESS_MENU = 2;
    public static final int INST_START_ACTIVITY = 10;
    public static final int INST_STATUSBAR_CLOSE = 9;
    public static final int INST_STATUSBAR_OPEN = 8;
    public static final int INST_TOAST = 5;
    public static final int INST_TYPE = 4;
    private static final String LOG_TAG = "Script+";

    public static void logkeyword(String keyword) {
        Log.v(LOG_TAG, "def " + keyword + "(self):");
    }

    public static void logWindowStateChange() {
        Log.v(LOG_TAG, "+WSC+");
    }

    public static void logTypePending() {
        Log.v(LOG_TAG, "+TP+");
    }

    public static void logEditBoxClick() {
        Log.v(LOG_TAG, "+EBT+");
    }

    public static void loginst(int event, String... args) {
        switch (event) {
            case 0:
                Log.v(LOG_TAG, "self.goto_home()");
                return;
            case 1:
                Log.v(LOG_TAG, "self.press_back()");
                return;
            case 2:
                Log.v(LOG_TAG, "self.press_menu()");
                return;
            case 3:
                Log.v(LOG_TAG, "self.view(" + args[0] + ").click()");
                return;
            case 4:
                Log.v(LOG_TAG, "self.view(" + args[0] + ").type(\"" + args[1] + "\")");
                return;
            case 5:
                Log.v(LOG_TAG, "#self.check_toast(\"" + args[0] + "\", self.get_toast())");
                return;
            case 7:
                Log.v(LOG_TAG, "#self.is_notification_exist(\"" + args[0] + "\")");
                return;
            case 8:
                Log.v(LOG_TAG, "self.open_status_bar()");
                return;
            case 9:
                Log.v(LOG_TAG, "self.close_status_bar()");
                return;
            case 10:
                Log.v(LOG_TAG, "self.start_activity( '" + args[0] + "', '" + args[0] + "' )");
                return;
            case INST_CHCECKBOX_RIGHT_SIDE /*11*/:
                Log.v(LOG_TAG, "self.view(" + args[0] + ").right(" + args[1] + ").click()");
                return;
            case INST_CHCECKBOX_LEFT_SIDE /*12*/:
                Log.v(LOG_TAG, "self.view(" + args[0] + ").left(" + args[1] + ").click()");
                return;
            default:
                return;
        }
    }
}
