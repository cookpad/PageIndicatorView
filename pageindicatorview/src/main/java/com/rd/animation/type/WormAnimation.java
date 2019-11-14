package com.rd.animation.type;

import androidx.annotation.NonNull;

import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.WormAnimationValue;

public class WormAnimation extends BaseWormAnimation<WormAnimationValue> {
    public WormAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    protected WormAnimationValue createValue() {
        return new WormAnimationValue();
    }
}
