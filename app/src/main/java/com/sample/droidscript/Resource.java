package com.sample.droidscript;

import android.util.Log;

public class Resource {
    public static final int CONTAINER_KEY = 1;
    private static final String LOG_TAG = "Script+";
    private static final String LOG_TAG2 = "Script";
    public static final int VIEW_KEY = 0;
    private static final String fmtstr = "<%s name=\"%s\"%s>%s</%s>";

    public static String buildViewKey(Selector selector) {
        String tag = "";
        String pakName = "";
        String resourcekey = generateKey(selector.getSelectorData());
        switch (selector.getSelectorType()) {
            case 0:
                tag = String.valueOf(tag) + "text";
                break;
            case 1:
                tag = String.valueOf(tag) + "id";
                if (selector.getPackageName() != null) {
                    pakName = String.valueOf(pakName) + " package=\"" + selector.getPackageName() + "\"";
                    break;
                }
                break;
            case 2:
                tag = String.valueOf(tag) + "description";
                break;
            case 3:
                tag = String.valueOf(tag) + "coordinates";
                break;
            case 4:
                tag = String.valueOf(tag) + "className";
                break;
            default:
                Log.w(LOG_TAG, "Unhandled selector type");
                break;
        }
        Log.v(LOG_TAG, String.format(fmtstr, new Object[]{tag, resourcekey, pakName, selector.getSelectorData(), tag}));
        return resourcekey;
    }

    public static String buildContainerViewKey(Selector selector) {
        String tag = "";
        String pakName = "";
        String resourcekey = null;
        if (selector.getContainerId() != null) {
            resourcekey = generateKey(selector.getContainerId());
            if (selector.isScrollable()) {
                switch (selector.getContainerType()) {
                    case 1:
                        tag = String.valueOf(tag) + "id";
                        if (!(selector == null || selector.getContainerId() == null)) {
                            pakName = String.valueOf(pakName) + " package=\"" + selector.getContainerPackageName() + "\"";
                            break;
                        }
                    default:
                        Log.w(LOG_TAG, "Unhandled container type");
                        break;
                }
                Log.v(LOG_TAG, String.format(fmtstr, new Object[]{tag, resourcekey, pakName, selector.getContainerId(), tag}));
            }
        }
        return resourcekey;
    }

    private static String formatkeyword(String rkw) {
        String[] sl = rkw.split("(?=\\p{Upper})");
        String fs = sl[0];
        for (int i = 1; i < sl.length; i++) {
            if (sl[i] != " ") {
                fs = String.valueOf(fs) + "_" + sl[i].trim();
            }
        }
        return fs;
    }

    private static String generateKey(String veiwsel) {
        return ("VIEW_" + formatkeyword(veiwsel.replace("id/", "")).toUpperCase() + "_" + getTimeStamp()).trim().replace(" ", "_").replace("__", "_").replace(",", "").replaceAll("[^A-Za-z0-9_\\s]", "");
    }

    private static String getTimeStamp() {
        return Long.valueOf(System.currentTimeMillis() / 1000).toString();
    }
}