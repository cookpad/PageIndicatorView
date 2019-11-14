package com.rd.animation.type;

import android.animation.Animator;
import android.animation.ValueAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rd.animation.controller.ValueController;
import com.rd.animation.data.Value;

public abstract class BaseAnimation<T extends Animator, V extends Value> {

    public static final int DEFAULT_ANIMATION_TIME = 350;
    protected long animationDuration = DEFAULT_ANIMATION_TIME;

    protected ValueController.UpdateListener listener;
    protected T animator;
    private V value;

    public BaseAnimation(@Nullable ValueController.UpdateListener listener) {
        this.listener = listener;
        animator = createAnimator();
        value = createValue();
    }

    @NonNull
    public abstract T createAnimator();

    @NonNull
    protected abstract V createValue();

    @NonNull
    protected V getValue() {
        return value;
    }

    public final void progress(float progress) {
        getValue().setProgress(progress);
        onProgress(progress);
    }

    protected abstract void onProgress(float progress);

    public BaseAnimation duration(long duration) {
        animationDuration = duration;

        if (animator instanceof ValueAnimator) {
            animator.setDuration(animationDuration);
        }

        return this;
    }

    public void start() {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    public void end() {
        if (animator != null && animator.isStarted()) {
            animator.end();
        }
    }
}
