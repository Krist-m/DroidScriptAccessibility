package com.sample.droidscript;

import android.view.accessibility.AccessibilityNodeInfo;

public class NodeInfo {
    public static final int LEFT_SIDE = 1;
    public static final int RIGHT_SIDE = 0;
    public int mDirInfo;
    public AccessibilityNodeInfo mNode;

    public NodeInfo(AccessibilityNodeInfo node, int sidedir) {
        this.mNode = node;
        this.mDirInfo = sidedir;
    }
}