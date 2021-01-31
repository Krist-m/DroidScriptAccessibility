package com.sample.droidscript;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckedTextView;
import java.util.Arrays;
import java.util.List;

public class Selector {
    public static final int CLASS = 4;
    public static final String CLICK_OPERATION = "click";
    public static final int COORDINATES = 3;
    public static final int DESCRIPTION = 2;
    private static final String LOG_TAG = "Script+";
    private static final String LOG_TAG2 = "Script";
    public static final int NONE = -1;
    public static final int RESOURCEID = 1;
    private static final List<String> SelectorStr = Arrays.asList(new String[]{"text", "resource_id", "discription", "coordinates"});
    public static final int TEXT = 0;
    public static final String TYPE_OPERATION = "type";
    private AccessibilityEvent mAccessEvent;
    private String mContainerId;
    private String mContainerPackageName;
    private int mContainertype;
    private boolean mIsHorizontalscroll;
    private boolean mIsPassword;
    private String mPackageName;
    private int mPosInfo;
    private boolean mScrollable;
    private String mSelectordata;
    private int mSelectortype;

    public Selector(AccessibilityEvent event, Class<?> clazz) {
        this.mAccessEvent = event;
        this.mSelectortype = -1;
        this.mSelectordata = null;
        this.mPackageName = null;
        this.mScrollable = false;
        this.mIsHorizontalscroll = false;
        this.mContainerId = null;
        this.mContainertype = -1;
        this.mContainerPackageName = null;
        this.mPosInfo = 1;
        this.mIsPassword = event.isPassword();
        fetchSelectorInfoFromEvent(clazz);
    }

    private Selector() {
        this.mAccessEvent = null;
        this.mSelectortype = -1;
        this.mSelectordata = null;
        this.mPackageName = null;
        this.mScrollable = false;
        this.mIsHorizontalscroll = false;
        this.mContainerId = null;
        this.mContainertype = -1;
        this.mContainerPackageName = null;
    }

    private void fetchSelectorTypeAndData() {
        if (this.mAccessEvent != null) {
            AccessibilityNodeInfo source = this.mAccessEvent.getSource();
            this.mPackageName = this.mAccessEvent.getPackageName().toString();
            Log.v(LOG_TAG2, this.mPackageName);
            if (source == null) {
                Log.v(LOG_TAG2, "mAccessEvent.getSource() returns null-2");
            }
            if (source != null && source.getContentDescription() != null) {
                Log.v(LOG_TAG2, "-DESCRIPTION-");
                this.mSelectortype = 2;
                this.mSelectordata = source.getContentDescription().toString();
            } else if (source != null && source.getViewIdResourceName() != null) {
                Log.v(LOG_TAG2, "-RESOURCEID-");
                this.mSelectortype = 1;
                this.mSelectordata = source.getViewIdResourceName().split(":")[1];
            } else if (this.mAccessEvent.getContentDescription() != null) {
                Log.v(LOG_TAG2, "-DESCRIPTION-");
                this.mSelectortype = 2;
                this.mSelectordata = this.mAccessEvent.getContentDescription().toString();
            } else if (this.mAccessEvent.getText() != null && this.mAccessEvent.getText().size() > 0) {
                Log.v(LOG_TAG2, "-TEXT-");
                List<CharSequence> lselector = this.mAccessEvent.getText();
                if (lselector.size() > 0 && lselector.get(0).toString().length() > 0) {
                    this.mSelectortype = 0;
                    this.mSelectordata = lselector.get(0).toString();
                }
            } else if (this.mAccessEvent.getClassName() == null || this.mAccessEvent.getClassName().length() <= 0) {
                this.mSelectortype = 3;
            } else {
                Log.v(LOG_TAG2, "-CLASS-");
                Log.v(LOG_TAG2, "***" + this.mAccessEvent.getClassName());
                this.mSelectortype = 4;
                this.mSelectordata = this.mAccessEvent.getClassName().toString();
            }
        }
    }

    private void fetchSelectorForScrollViewIdIfExists() {
        AccessibilityNodeInfo pv = this.mAccessEvent.getSource();
        while (pv != null) {
            pv = pv.getParent();
            if (pv != null && pv.isScrollable()) {
                this.mScrollable = true;
                if (pv.getClassName().toString().equals("android.widget.HorizontalScrollView")) {
                    this.mIsHorizontalscroll = true;
                }
                if (pv == null || pv.getViewIdResourceName() == null) {
                    this.mAccessEvent.getContentDescription();
                    return;
                }
                String[] ls = pv.getViewIdResourceName().split(":");
                this.mContainerPackageName = ls[0];
                this.mContainerId = ls[1];
                this.mContainertype = 1;
                return;
            }
        }
    }

    private void fetchSelectorFromTypeText() {
        if (!(this.mAccessEvent == null || this.mAccessEvent.getText() == null)) {
            List<CharSequence> lselector = this.mAccessEvent.getText();
            if (lselector.size() > 0) {
                this.mSelectortype = 0;
            }
            this.mSelectordata = lselector.get(0).toString();
        }
        this.mPackageName = this.mAccessEvent.getPackageName().toString();
    }

    private void fetchSelectorInfoFromEvent(Class<?> clazz) {
        if (CheckedTextView.class.isAssignableFrom(clazz)) {
            fetchSelectorFromTypeText();
        } else {
            fetchSelectorTypeAndData();
        }
        fetchSelectorForScrollViewIdIfExists();
    }

    public String baseSelectorStr(String resourceKey, String ContainerKey, boolean onlyResouce) {
        String str;
        if (resourceKey != null) {
            str = "r." + resourceKey.toLowerCase();
        } else {
            String str2 = String.valueOf(SelectorStr.get(this.mSelectortype)) + "='";
            if (this.mPackageName != null) {
                str2 = String.valueOf(str2) + this.mPackageName + ":";
            }
            str = String.valueOf(str2) + this.mSelectordata + "'";
        }
        if (!this.mScrollable || onlyResouce) {
            return str;
        }
        String str3 = String.valueOf(str) + ", isScrollable=True";
        if (this.mIsHorizontalscroll) {
            str3 = String.valueOf(str3) + ", vertical = False";
        }
        if (ContainerKey != null) {
            return String.valueOf(str3) + ", container_res=r." + ContainerKey.toLowerCase();
        }
        if (this.mContainerId != null) {
            return String.valueOf(str3) + ", container_res='" + this.mContainerId + "'";
        }
        return str3;
    }

    public String buildSelectorStr(String resourceKey, String ContainerKey) {
        return baseSelectorStr(resourceKey, ContainerKey, false);
    }

    public String buildSelectorStr(String resourceKey) {
        return baseSelectorStr(resourceKey, (String) null, false);
    }

    public String buildSelectorStr(String resourceKey, boolean onlyResouce) {
        return baseSelectorStr(resourceKey, (String) null, onlyResouce);
    }

    public final String getPackageName() {
        return this.mPackageName;
    }

    public final int getSelectorType() {
        return this.mSelectortype;
    }

    public final String getSelectorData() {
        return this.mSelectordata;
    }

    public boolean isScrollable() {
        return this.mScrollable;
    }

    public final String getContainerId() {
        return this.mContainerId;
    }

    public final int getContainerType() {
        return this.mContainertype;
    }

    public final String getContainerPackageName() {
        return this.mContainerPackageName;
    }

    private NodeInfo recursive_search_for_nearest_text_view(AccessibilityNodeInfo pnode, AccessibilityNodeInfo cn) {
        int cc = pnode.getChildCount();
        int dir = 0;
        int i = 0;
        while (i < cc) {
            AccessibilityNodeInfo childnode = pnode.getChild(i);
            if (cn.equals(childnode)) {
                dir = 1;
            }
            if (childnode.getText() == null || childnode.getText().length() <= 0) {
                i++;
            } else {
                Log.v(LOG_TAG2, "--" + childnode.getText());
                return new NodeInfo(childnode, dir);
            }
        }
        if (pnode.getParent() != null) {
            return recursive_search_for_nearest_text_view(pnode.getParent(), pnode);
        }
        return null;
    }

    public Selector getNearestTextView() {
        Selector s = null;
        AccessibilityNodeInfo pv = this.mAccessEvent.getSource();
        if (pv == null) {
            Log.v(LOG_TAG, "#DUE TO ISSUE IN ANDROID PLATFORM UNABLE TO GET RESOUCE ID FOR THE CHECKBOX DO IT MANUALLY");
        } else {
            NodeInfo nearestNode = recursive_search_for_nearest_text_view(pv.getParent(), pv);
            if (nearestNode != null) {
                s = new Selector();
                if (nearestNode.mNode.getText() != null) {
                    CharSequence lselector = nearestNode.mNode.getText();
                    s.mSelectortype = 0;
                    s.mSelectordata = lselector.toString();
                    s.mContainerId = this.mContainerId;
                    s.mContainerPackageName = this.mContainerPackageName;
                    s.mContainertype = this.mContainertype;
                    s.mScrollable = this.mScrollable;
                    s.mPosInfo = nearestNode.mDirInfo;
                }
            }
        }
        return s;
    }

    public int GetDirInfo() {
        return this.mPosInfo;
    }

    public String getKeyword(String oper) {
        return (String.valueOf(this.mPackageName.replace(".", "_").toLowerCase()) + "_" + this.mSelectordata.replace(" ", "_").replace("id/", "").toLowerCase() + "_" + oper).replaceAll("[^A-Za-z0-9_\\s]", "");
    }

    public boolean isPassword() {
        return this.mIsPassword;
    }
}