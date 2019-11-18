package com.rd.animation;

import androidx.annotation.NonNull;
import com.rd.animation.controller.AnimationController;
import com.rd.animation.controller.ValueController;
import com.rd.draw.data.Indicator;
import com.rd.draw.drawer.type.ScaleCommon;

public class AnimationManager {

    private AnimationController animationController;

    public AnimationManager(@NonNull Indicator indicator,
                            @NonNull ValueController.UpdateListener listener,
                            @NonNull ScaleCommon scaleCommon) {
        this.animationController = new AnimationController(indicator, listener, scaleCommon);
    }

    public void basic() {
        if (animationController != null) {
            animationController.end();
            animationController.basic();
        }
    }

    public void interactive(float progress) {
        if (animationController != null) {
            animationController.interactive(progress);
        }
    }

    public void end() {
        if (animationController != null) {
            animationController.end();
        }
    }
}
