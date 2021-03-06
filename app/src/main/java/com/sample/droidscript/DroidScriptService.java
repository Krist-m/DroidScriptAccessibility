package com.sample.droidscript;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;


/*
    AccessibilityService  used to fetch the screen action that is being performed by the user
    this is the service with will running on the android device for recard actions
 */
public class DroidScriptService extends AccessibilityService implements View.OnTouchListener {
    private static final String TAG = "DroidScriptService";
    private static final String AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN_CLASS_NAME = "DropDownListView";
    private static final String HANDLE_VIEW_CLASS_NAME = "com.android.launcher.HandleView";
    private static final String ICON_MENU_VIEW_CLASS_NAME = "com.android.internal.view.menu.IconMenuView";
    private static final String LAUNCHER_SCREEN = "com.sec.android.app.launcher";
    private static final String LOG_TAG = "Script+";
    private static final String LOG_TAG2 = "Script";
    private static final String SOFT_INPUT_WINDOW_CLASS_NAME = "android.inputmethodservice.SoftInputWindow";
    private static final String SPACE = " ";
    private static final String STATUS_BAR_EXPANDED_DIALOG_CLASS_NAME = "com.android.systemui";
    private static NotificationPanal sNotificationPanal = new NotificationPanal();

    private boolean mIsOnLauncher;
    private int mPendingEventList;
    private Selector mPevSelector;
    private String mTypedtext;

    public void onCreate() {
        super.onCreate();
        this.mIsOnLauncher = false;
    }

    @SuppressLint("WrongConstant")
    public void onServiceConnected() {
        this.mPendingEventList = 0;
        this.mTypedtext = null;
        this.mPevSelector = null;
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = -1;
        info.feedbackType = -1;
        info.notificationTimeout = 20;
        info.flags = 121;
        setServiceInfo(info);
        Log.w(LOG_TAG2, "$$$$$$$ Script Generator started $$$$$$$");
    }

    public void onDestroy() {
        Log.w(LOG_TAG2, "$$$$$$$ Script Generator ended $$$$$$$");
        super.onDestroy();
    }


    public synchronized void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent accessibilityEvent) {

        if (accessibilityEvent == null) {
            Log.i(LOG_TAG2, "Received null accessibility event.");
            return;
        }

        int eventType = accessibilityEvent.getEventType();
        String eventId = Integer.toHexString(eventType);
        switch (eventType) {
            case 1:
                android.util.Log.w(LOG_TAG2, "TYPE_VIEW_CLICKED " + eventId);
                processAccessibilityEventViewClickedType(accessibilityEvent);
                break;
            case 4:
                android.util.Log.w(LOG_TAG2, "TYPE_VIEW_SELECTED " + eventId);
                processAccessibilityEventViewSelectedType(accessibilityEvent);
                break;
            case 8:
                android.util.Log.w(LOG_TAG2, "TYPE_VIEW_FOCUSED " + eventId);
                processAccessibilityEventViewSelectedType(accessibilityEvent);
            case 16:
                android.util.Log.w(LOG_TAG2, "TYPE_VIEW_TEXT_CHANGED " + eventId);
                processAccessibilityEventViewSelectedType(accessibilityEvent);
                break;
            case 32:
                android.util.Log.w(LOG_TAG2, "TYPE_WINDOW_STATE_CHANGED " + eventId);
                processAccessibilityEventViewSelectedType(accessibilityEvent);
                break;
            case 64:
                android.util.Log.w(LOG_TAG2, "TYPE_NOTIFICATION_STATE_CHANGED " + eventId);
                processAccessibilityEventViewSelectedType(accessibilityEvent);
                break;
            case 4096:
                android.util.Log.w(LOG_TAG2, "TYPE_VIEW_SCROLLED " + eventId);
                processAccessibilityEventViewSelectedType(accessibilityEvent);
                break;
            default:
                android.util.Log.w(LOG_TAG2, "event type " + eventId);
                //processAccessibilityEventViewSelectedType(accessibilityEvent);
        }

    }

    public void checkForActivityLaunch(AccessibilityEvent event) {
        String pn = event.getPackageName().toString();
        String cn = event.getClassName().toString();
        Log.v(LOG_TAG2, "<<< class name=" + pn + " package=" + pn + ">>>");
        if (pn.equals(LAUNCHER_SCREEN)) {
            this.mIsOnLauncher = true;
        } else if (this.mIsOnLauncher && !pn.equals(STATUS_BAR_EXPANDED_DIALOG_CLASS_NAME)) {
            ScriptLogger.logkeyword("launch_" + pn.replace(".", "_"));
            ScriptLogger.loginst(10, String.valueOf(pn) + "/" + cn);
            this.mIsOnLauncher = false;
        }
    }

    public void onInterrupt() {
    }

    public void processAccessibilityEventViewClickedType(AccessibilityEvent event) {
        Class<?> clazz = loadAccessibilityEventSourceClass(event);
        if (clazz != null) {
            if (CheckBox.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on CheckBox");
                scriptCheckBoxClicked(event, clazz);
            } else if (EditText.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on EditText");
                processPendingEvents(1);
                scriptEditTextClicked(event, clazz);
            } else if (CompoundButton.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on CompoundButton");
                scriptCompoundButtonClicked(event, clazz);
            } else if (Button.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on Button");
                scriptButtonClicked(event, clazz);
            } else if (CheckedTextView.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on CheckedTextView");
                scriptCheckedTextViewClicked(event, clazz);
            } else if (TextView.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on TextView");
                scriptTextViewOrAdapterViewClicked(event, clazz);
            } else if (RelativeLayout.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on RelativeLayout");
                scriptTextViewOrAdapterViewClicked(event, clazz);
            } else if (Switch.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on Switch");
                scriptSwitchViewClicked(event, clazz);
            } else if (AdapterView.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on AdapterView");
                scriptTextViewOrAdapterViewClicked(event, clazz);
            } else {
                Log.v(LOG_TAG2, "event on " + clazz);
                scriptTextViewOrAdapterViewClicked(event, clazz);
            }
        }
    }

    private void scriptCheckBoxClicked(AccessibilityEvent event, Class<?> clazz) {
        int i = 11;
        Selector checkboxSel = new Selector(event, clazz);
        Selector selector = checkboxSel.getNearestTextView();
        if (selector == null) {
            ScriptLogger.loginst(11, "***", "***");
            return;
        }
        String sel = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        String nsel = checkboxSel.buildSelectorStr(Resource.buildViewKey(checkboxSel), true);
        ScriptLogger.logkeyword(checkboxSel.getKeyword(Selector.CLICK_OPERATION));
        if (selector.GetDirInfo() != 0) {
            i = 12;
        }
        ScriptLogger.loginst(i, sel, nsel);
    }

    private void processPendingEvents(int curEventType) {
        if ((this.mPendingEventList & 1) != 1) {
            return;
        }
        if (curEventType == 8 || curEventType == 1) {
            if (this.mPevSelector.isPassword()) {
                this.mTypedtext = "[@@EPW@@]"; //mock password
            }
            if (this.mTypedtext != null) {
                this.mTypedtext = null;
            }
            this.mPendingEventList &= -2;
        }
    }

    private void scriptEditViewType(Selector selector, String txt) {
        String str = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        ScriptLogger.logkeyword(selector.getKeyword(Selector.TYPE_OPERATION));
        ScriptLogger.loginst(4, str, txt.substring(1, txt.length() - 1));
    }

    private void scriptEditTextClicked(AccessibilityEvent event, Class<?> cls) {
        scriptEditTextFocused(event);
    }

    private void scriptTextViewOrAdapterViewClicked(AccessibilityEvent event, Class<?> clazz) {
        Selector selector = new Selector(event, clazz);
        String str = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        ScriptLogger.logkeyword(selector.getKeyword(Selector.CLICK_OPERATION));
        ScriptLogger.loginst(3, str);
    }

    private void scriptSwitchViewClicked(AccessibilityEvent event, Class<?> clazz) {
        Selector selector = new Selector(event, clazz);
        String str = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        ScriptLogger.logkeyword(selector.getKeyword(Selector.CLICK_OPERATION));
        ScriptLogger.loginst(3, str);
    }

    private void scriptCheckedTextViewClicked(AccessibilityEvent event, Class<?> clazz) {
        Selector selector = new Selector(event, clazz);
        String str = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        ScriptLogger.logkeyword(selector.getKeyword(Selector.CLICK_OPERATION));
        ScriptLogger.loginst(3, str);
    }

    private void scriptCompoundButtonClicked(AccessibilityEvent event, Class<?> clazz) {
        Selector selector = new Selector(event, clazz);
        String str = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        ScriptLogger.logkeyword(selector.getKeyword(Selector.CLICK_OPERATION));
        ScriptLogger.loginst(3, str);
    }

    private void scriptButtonClicked(AccessibilityEvent event, Class<?> clazz) {
        Selector selector = new Selector(event, clazz);
        String str = selector.buildSelectorStr(Resource.buildViewKey(selector), Resource.buildContainerViewKey(selector));
        ScriptLogger.logkeyword(selector.getKeyword(Selector.CLICK_OPERATION));
        ScriptLogger.loginst(3, str);
    }

    private void processAccessibilityEventViewFocusedType(AccessibilityEvent event) {
        Class<?> clazz = loadAccessibilityEventSourceClass(event);
        if (clazz != null) {
            if (CompoundButton.class.isAssignableFrom(clazz)) {
                Log.w(LOG_TAG2, "CompoundButton ");
                scriptCompoundButtonFocused(event, clazz);
            } else if (Button.class.isAssignableFrom(clazz)) {
                Log.w(LOG_TAG2, "Button ");
                scriptButtonFocused(event);
            } else if (EditText.class.isAssignableFrom(clazz)) {
                Log.v(LOG_TAG2, "event on EditText");
                scriptEditTextClicked(event, clazz);
            } else if (TextView.class.isAssignableFrom(clazz)) {
                Log.w(LOG_TAG2, "TextView ");
                scriptFrameLayoutOrTextViewOrWebViewFocused(event);
            } else if (WebView.class.isAssignableFrom(clazz)) {
                Log.w(LOG_TAG2, "WebView ");
                scriptFrameLayoutOrTextViewOrWebViewFocused(event);
            } else if (ImageView.class.isAssignableFrom(clazz)) {
                Log.w(LOG_TAG2, "ImageView ");
                scriptFrameLayoutOrTextViewOrWebViewFocused(event);
            } else if (FrameLayout.class.isAssignableFrom(clazz)) {
                Log.w(LOG_TAG2, "FrameLayout ");
                scriptFrameLayoutOrTextViewOrWebViewFocused(event);
            }
        }
    }

    private void scriptButtonFocused(AccessibilityEvent event) {
    }

    private void scriptCompoundButtonFocused(AccessibilityEvent event, Class<?> cls) {
    }

    private void scriptEditTextFocused(AccessibilityEvent event) {
        if ((this.mPendingEventList & 1) != 1) {
            Class<?> clazz = loadAccessibilityEventSourceClass(event);
            this.mPendingEventList |= 1;
            this.mPevSelector = new Selector(event, clazz);
            String str = this.mPevSelector.buildSelectorStr(Resource.buildViewKey(this.mPevSelector), Resource.buildContainerViewKey(this.mPevSelector));
            ScriptLogger.logkeyword(this.mPevSelector.getKeyword(Selector.CLICK_OPERATION));
            ScriptLogger.loginst(3, str);
            ScriptLogger.logEditBoxClick();
        }
    }

    private void scriptFrameLayoutOrTextViewOrWebViewFocused(AccessibilityEvent event) {
        scriptViewFocusedOrSelectedContent(event);
    }

    private void scriptViewFocusedOrSelectedContent(AccessibilityEvent event) {
        event.getClassName().equals(HANDLE_VIEW_CLASS_NAME);
    }

    public void processAccessibilityEventViewSelectedType(AccessibilityEvent event) {
        Class<?> clazz = loadAccessibilityEventSourceClass(event);
        if (clazz != null) {
            if (AdapterView.class.isAssignableFrom(clazz)) {
                scriptListViewOrWebViewSelected(event);
            } else if (WebView.class.isAssignableFrom(clazz)) {
                scriptListViewOrWebViewSelected(event);
            }
        }
    }

    private void scriptListViewOrWebViewSelected(AccessibilityEvent event) {
        scriptViewFocusedOrSelectedContent(event);
    }

    private void processAccessibilityEventNotificationStateChangedType(AccessibilityEvent event) {
        Class<?> clazz = loadAccessibilityEventSourceClass(event);
        if (clazz != null) {
            Log.e(LOG_TAG2, "processAccessibilityEventNotificationStateChangedType ");
            if (Notification.class.isAssignableFrom(clazz)) {
                scriptNotification(event);
            } else {
                scriptToastPopUp(event);
            }
        }
    }

    private void scriptToastPopUp(AccessibilityEvent event) {
        List<CharSequence> messages = event.getText();
        if (messages.size() > 0) {
            String toastMsg = messages.toString().replace("[", "").replace("]", "");
            ScriptLogger.loginst(5, toastMsg);
            Log.v(LOG_TAG2, "Captured Toast message :'" + toastMsg);
            return;
        }
        Log.e(LOG_TAG2, "Toast Message is empty. ");
    }

    private void scriptNotification(AccessibilityEvent event) {
        Log.e(LOG_TAG2, "scriptNotification ");
        Parcelable data = event.getParcelableData();
        if (data instanceof Notification) {
            Log.d(LOG_TAG2, "Received notification");
            Notification notification = (Notification) data;
            Log.d(LOG_TAG2, "ticker: " + notification.tickerText);
            //Log.d(LOG_TAG2, "icon: " + notification.icon);
            String s = event.getText().toString().replace("[", "").replace("]", "");
            if (s.length() > 0) {
                Log.d(LOG_TAG2, "notification: " + Integer.toString(s.length()));
                ScriptLogger.loginst(7, null, s);
            }
        }
    }

    private void processAccessibilityEventWindowStateChangedType(AccessibilityEvent event) {
        checkForActivityLaunch(event);
        Class<?> clazz = loadAccessibilityEventSourceClass(event);
        if (clazz != null) {
            if (event.getClassName().equals(SOFT_INPUT_WINDOW_CLASS_NAME)) {
                scriptInputMethod(event);
            } else if (Activity.class.isAssignableFrom(clazz)) {
                scriptActivityStarted(event);
            } else if (event.getPackageName().equals(STATUS_BAR_EXPANDED_DIALOG_CLASS_NAME)) {
                scriptStatusBar(event);
            } else if (Dialog.class.isAssignableFrom(clazz)) {
                scriptAlertDialog(event);
            } else if (event.getClassName().equals(AUTO_COMPLETE_TEXT_VIEW_DROP_DOWN_CLASS_NAME)) {
                scriptAutoCompletion(event);
            } else if (event.getClassName().equals(ICON_MENU_VIEW_CLASS_NAME)) {
                scriptOptionsMenu(event);
            } else if (LinearLayout.class.isAssignableFrom(clazz)) {
                scriptShortMessage(event);
            }
        }
    }

    private void processAccessibilityEventWindowChangedType(AccessibilityEvent event) {
    }

    private void scriptActivityStarted(AccessibilityEvent event) {
    }

    private void scriptAlertDialog(AccessibilityEvent event) {
    }

    private void scriptAutoCompletion(AccessibilityEvent event) {
    }

    private void scriptOptionsMenu(AccessibilityEvent event) {
    }

    private void scriptInputMethod(AccessibilityEvent event) {
    }

    private void scriptSlidingDrawer(AccessibilityEvent uiEvent) {
    }

    private void scriptStatusBar(AccessibilityEvent event) {
        sNotificationPanal.logEventOpenStatusBar();
        this.mPendingEventList |= 2;
    }

    private void scriptShortMessage(AccessibilityEvent event) {
    }

    private void processAccessibilityEventViewTextChangedType(AccessibilityEvent uiEvent) {
        if (uiEvent.getText().size() != 0) {
            String text = uiEvent.getText().toString();
            if (text == null) {
                text = "";
            }
            Log.w(LOG_TAG2, text);
            updateTypeMsg(text);
            ScriptLogger.logTypePending();
        }
    }

    private void updateTypeMsg(String str) {
        this.mTypedtext = str;
    }

    private CharSequence getCompoundButtonState(AccessibilityEvent event) {
        return null;
    }

    private void scriptCurrentItemPosition(AccessibilityEvent event) {
    }

    private CharSequence composeMessage(List<CharSequence> text, int beginIndex, int endIndex) {
        int size = text.size();
        if (size == 1) {
            return text.get(0);
        }
        if (size <= 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = beginIndex; i < endIndex; i++) {
            builder.append(text.get(i));
            builder.append(SPACE);
        }
        return builder;
    }

    private Class<?> loadAccessibilityEventSourceClass(AccessibilityEvent event) {
        String className = event.getClassName().toString();
        try {
            return getClassLoader().loadClass(className);
        } catch (ClassNotFoundException cnfe) {
            try {
                return getApplicationContext().createPackageContext(event.getPackageName().toString(), 3).getClassLoader().loadClass(className);
            } catch (PackageManager.NameNotFoundException nnfe) {
                Log.e(LOG_TAG2, "Error during loading an event source class: " + event.getClassName() + SPACE + nnfe);
                return null;
            } catch (ClassNotFoundException e) {
                Log.e(LOG_TAG2, "Error during loading an event source class: " + event.getClassName() + SPACE + cnfe);
                return null;
            }
        }
    }

    private int getCompoundButtonTemplateResourceId(Class<?> cls) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean onGesture(int gestureId) {
        Log.v(LOG_TAG2, "---onGesture---");
        return super.onGesture(gestureId);
    }

    /* access modifiers changed from: protected */
    public boolean onKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 3:
                    ScriptLogger.loginst(0, (String[]) null);
                    break;
                case 4:
                    ScriptLogger.loginst(1, (String[]) null);
                    break;
                case 82:
                    ScriptLogger.loginst(2, (String[]) null);
                    break;
                default:
                    Log.v(LOG_TAG2, "---unhandled onKeyEvent---");
                    break;
            }
        }
        return super.onKeyEvent(event);
    }

    public boolean onTouch(View v, MotionEvent event) {
        Log.v(LOG_TAG2, "touched " + String.valueOf(event.getX()) + "," + String.valueOf(event.getY()));
        return false;
    }

    class WSClogger extends Thread {
        WSClogger() {
        }

        public void run() {
            super.run();
            while (true) {
                ScriptLogger.logWindowStateChange();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
