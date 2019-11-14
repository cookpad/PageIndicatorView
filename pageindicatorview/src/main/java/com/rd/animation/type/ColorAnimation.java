package com.rd.animation.type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ColorAnimationValue;

public class ColorAnimation extends BaseColorAnimation<ColorAnimationValue> {

    public ColorAnimation(@Nullable ValueController.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    protected ColorAnimationValue createValue() {
        return new ColorAnimationValue();
    }
}
