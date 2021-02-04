package com.sample.droidscript;

import android.util.Log;

public class ScriptLogger {

    public static final int INST_GOTO_HOME = 0;
    public static final int INST_PRESS_BACK = INST_GOTO_HOME + 1;
    public static final int INST_PRESS_MENU = INST_PRESS_BACK + 1;
    public static final int INST_CLICK = INST_PRESS_MENU + 1;
    public static final int INST_TYPE = INST_CLICK + 1;
    public static final int INST_TOAST = INST_TYPE + 1;
    public static final int INST_CHECKBOX_CLICK = INST_TOAST + 1;
    public static final int INST_NOTIFICATION = INST_CHECKBOX_CLICK + 1;
    public static final int INST_STATUSBAR_OPEN = INST_NOTIFICATION + 1;
    public static final int INST_STATUSBAR_CLOSE = INST_STATUSBAR_OPEN + 1;
    public static final int INST_START_ACTIVITY = INST_STATUSBAR_CLOSE + 1;
    public static final int INST_CHCECKBOX_RIGHT_SIDE = INST_START_ACTIVITY + 1;
    public static final int INST_CHCECKBOX_LEFT_SIDE = INST_CHCECKBOX_RIGHT_SIDE + 1;

    private static final String LOG_TAG = "Script+";

    public static void logkeyword(String keyword) {
        //Log.v(LOG_TAG, keyword);
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
