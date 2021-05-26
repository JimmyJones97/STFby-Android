package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

import android.graphics.ColorMatrix;

/* renamed from: com.baidu.voicerecognition.android.ui.ColorFilterGenerator */
public class ColorFilterGenerator {
    private static float[] DELTA_INDEX = {0.0f, 0.01f, 0.02f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.1f, 0.11f, 0.12f, 0.14f, 0.15f, 0.16f, 0.17f, 0.18f, 0.2f, 0.21f, 0.22f, 0.24f, 0.25f, 0.27f, 0.28f, 0.3f, 0.32f, 0.34f, 0.36f, 0.38f, 0.4f, 0.42f, 0.44f, 0.46f, 0.48f, 0.5f, 0.53f, 0.56f, 0.59f, 0.62f, 0.65f, 0.68f, 0.71f, 0.74f, 0.77f, 0.8f, 0.83f, 0.86f, 0.89f, 0.92f, 0.95f, 0.98f, 1.0f, 1.06f, 1.12f, 1.18f, 1.24f, 1.3f, 1.36f, 1.42f, 1.48f, 1.54f, 1.6f, 1.66f, 1.72f, 1.78f, 1.84f, 1.9f, 1.96f, 2.0f, 2.12f, 2.25f, 2.37f, 2.5f, 2.62f, 2.75f, 2.87f, 3.0f, 3.2f, 3.4f, 3.6f, 3.8f, 4.0f, 4.3f, 4.7f, 4.9f, 5.0f, 5.5f, 6.0f, 6.5f, 6.8f, 7.0f, 7.3f, 7.5f, 7.8f, 8.0f, 8.4f, 8.7f, 9.0f, 9.4f, 9.6f, 9.8f, 10.0f};

    public static void adjustColor(ColorMatrix cm, float brightness, float contrast, float saturation, float hue) {
        adjustHue(cm, hue);
        adjustContrast(cm, contrast);
        adjustBrightness(cm, brightness);
        adjustSaturation(cm, saturation);
    }

    public static void adjustHue(ColorMatrix cm, float value) {
        float value2 = (cleanValue(value, 180.0f) / 180.0f) * 3.1415927f;
        if (value2 != 0.0f) {
            float cosVal = (float) Math.cos((double) value2);
            float sinVal = (float) Math.sin((double) value2);
            cm.postConcat(new ColorMatrix(new float[]{((1.0f - 0.213f) * cosVal) + 0.213f + ((-0.213f) * sinVal), ((-0.715f) * cosVal) + 0.715f + ((-0.715f) * sinVal), ((-0.072f) * cosVal) + 0.072f + ((1.0f - 0.072f) * sinVal), 0.0f, 0.0f, ((-0.213f) * cosVal) + 0.213f + (0.143f * sinVal), ((1.0f - 0.715f) * cosVal) + 0.715f + (0.14f * sinVal), ((-0.072f) * cosVal) + 0.072f + (-0.283f * sinVal), 0.0f, 0.0f, ((-0.213f) * cosVal) + 0.213f + ((-(1.0f - 0.213f)) * sinVal), ((-0.715f) * cosVal) + 0.715f + (sinVal * 0.715f), ((1.0f - 0.072f) * cosVal) + 0.072f + (sinVal * 0.072f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    public static void adjustBrightness(ColorMatrix cm, float value) {
        float value2 = cleanValue(value, 100.0f);
        if (value2 != 0.0f && !Float.isNaN(value2)) {
            cm.postConcat(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, value2, 0.0f, 1.0f, 0.0f, 0.0f, value2, 0.0f, 0.0f, 1.0f, 0.0f, value2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    public static void adjustContrast(ColorMatrix cm, float value) {
        float x;
        float x2;
        float value2 = cleanValue(value, 100.0f);
        if (value2 != 0.0f && !Float.isNaN(value2)) {
            if (value2 < 0.0f) {
                x2 = 127.0f + ((value2 / 100.0f) * 127.0f);
            } else {
                float x3 = value2 % 1.0f;
                if (x3 == 0.0f) {
                    x = DELTA_INDEX[(int) value2];
                } else {
                    x = (DELTA_INDEX[((int) value2) << 0] * (1.0f - x3)) + (DELTA_INDEX[(((int) value2) << 0) + 1] * x3);
                }
                x2 = (x * 127.0f) + 127.0f;
            }
            cm.postConcat(new ColorMatrix(new float[]{x2 / 127.0f, 0.0f, 0.0f, 0.0f, (127.0f - x2) * 0.5f, 0.0f, x2 / 127.0f, 0.0f, 0.0f, (127.0f - x2) * 0.5f, 0.0f, 0.0f, x2 / 127.0f, 0.0f, (127.0f - x2) * 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    public static void adjustSaturation(ColorMatrix cm, float value) {
        float value2 = cleanValue(value, 100.0f);
        if (value2 != 0.0f && !Float.isNaN(value2)) {
            float x = 1.0f + (value2 > 0.0f ? (3.0f * value2) / 100.0f : value2 / 100.0f);
            cm.postConcat(new ColorMatrix(new float[]{((1.0f - x) * 0.3086f) + x, (1.0f - x) * 0.6094f, (1.0f - x) * 0.082f, 0.0f, 0.0f, (1.0f - x) * 0.3086f, ((1.0f - x) * 0.6094f) + x, (1.0f - x) * 0.082f, 0.0f, 0.0f, (1.0f - x) * 0.3086f, (1.0f - x) * 0.6094f, ((1.0f - x) * 0.082f) + x, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}
