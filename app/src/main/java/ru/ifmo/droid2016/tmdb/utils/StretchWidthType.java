package ru.ifmo.droid2016.tmdb.utils;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.facebook.drawee.drawable.ScalingUtils;

/**
 * Created by Roman on 19/11/2016.
 */

public class StretchWidthType implements ScalingUtils.ScaleType {
    @Override
    public Matrix getTransform(Matrix outTransform, Rect parentBounds, int childWidth, int childHeight, float focusX, float focusY) {
        float scale = (float) parentBounds.width() / childWidth;
        Log.d("parent width", String.valueOf(parentBounds.width()));
        Log.d("child width", String.valueOf(childWidth));
        float dx = parentBounds.left;
        float dy = parentBounds.top;
        outTransform.setScale(scale, scale);
        outTransform.postTranslate(dx, dy);
        return outTransform;
    }
}
