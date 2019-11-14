package com.rd.animation.type;

import androidx.annotation.NonNull;

import com.rd.animation.controller.ValueController;

public class ScaleDownAnimation extends ScaleAnimation {

    public ScaleDownAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    public ScaleAnimation with(int colorStart, int colorEnd, int radius, float scaleFactor) {
        return super.with(colorStart, colorEnd, (int) (radius * scaleFactor), radius, (int) (radius * scaleFactor));
    }
}

