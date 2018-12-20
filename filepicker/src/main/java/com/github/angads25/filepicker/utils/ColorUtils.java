package com.github.angads25.filepicker.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;

import com.github.angads25.filepicker.R;

public class ColorUtils {

    public static int getAccentColor(Context context) {
        int color;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue accentColor = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.colorAccent, accentColor, true);
            color = accentColor.data;
        } else {
            color = context.getResources().getColor(R.color.colorAccent);
        }

        return color;
    }

    public static int muteColor(int color) {
        return Color.argb(128, Color.red(color), Color.green(color), Color.blue(color));
    }

}
