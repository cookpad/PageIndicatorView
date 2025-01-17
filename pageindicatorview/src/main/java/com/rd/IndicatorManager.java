package com.rd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.rd.animation.AnimationManager;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.Value;
import com.rd.draw.DrawManager;
import com.rd.draw.data.Indicator;
import com.rd.draw.drawer.type.ScaleCommon;

public class IndicatorManager implements ValueController.UpdateListener {

    private DrawManager drawManager;
    private AnimationManager animationManager;
    private Listener listener;

    interface Listener {
        void onIndicatorUpdated();
    }

    IndicatorManager(@Nullable Listener listener,
                     @NonNull ScaleCommon scaleCommon) {
        this.listener = listener;
        this.drawManager = new DrawManager(scaleCommon);
        this.animationManager = new AnimationManager(drawManager.indicator(), this, scaleCommon);
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public Indicator indicator() {
        return drawManager.indicator();
    }

    public DrawManager drawer() {
        return drawManager;
    }

    @Override
    public void onValueUpdated(@Nullable Value value) {
        drawManager.updateValue(value);
        if (listener != null) {
            listener.onIndicatorUpdated();
        }
    }
}
