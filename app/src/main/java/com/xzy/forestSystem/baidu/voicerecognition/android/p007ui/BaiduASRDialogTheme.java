package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

/* renamed from: com.baidu.voicerecognition.android.ui.BaiduASRDialogTheme */
public class BaiduASRDialogTheme {
    private static final int BLUE_COLOR_STYLE = 1;
    private static final int DEEP_CONTRAST_STYLE = 33554432;
    private static final int GREEN_COLOR_STYLE = 3;
    private static final int LIGHT_CONTRAST_STYLE = 16777216;
    private static final int MASK_COLOR_STYLE = 16777215;
    private static final int MASK_CONTRAST_STYLE = -16777216;
    private static final int ORANGE_COLOR_STYLE = 4;
    private static final int RED_COLOR_STYLE = 2;
    public static final int THEME_BLUE_DEEPBG = 33554433;
    public static final int THEME_BLUE_LIGHTBG = 16777217;
    public static final int THEME_GREEN_DEEPBG = 33554435;
    public static final int THEME_GREEN_LIGHTBG = 16777219;
    public static final int THEME_ORANGE_DEEPBG = 33554436;
    public static final int THEME_ORANGE_LIGHTBG = 16777220;
    public static final int THEME_RED_DEEPBG = 33554434;
    public static final int THEME_RED_LIGHTBG = 16777218;

    public static boolean isDeepStyle(int theme) {
        if ((-16777216 & theme) == DEEP_CONTRAST_STYLE) {
            return true;
        }
        return false;
    }
}
