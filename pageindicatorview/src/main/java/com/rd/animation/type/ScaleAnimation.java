package com.rd.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ScaleAnimationValue;

public class ScaleAnimation extends BaseColorAnimation<ScaleAnimationValue> {

    public static final float DEFAULT_SCALE_FACTOR = 0.7f;
    public static final float MIN_SCALE_FACTOR = 0.3f;
    public static final float MAX_SCALE_FACTOR = 1;

    static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    static final String ANIMATION_SCALE = "ANIMATION_SCALE";

    int selectedRadius;
    int selectingStartRadius;
    int deselectingEndRadius;

    public ScaleAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    protected ScaleAnimationValue createValue() {
        return new ScaleAnimationValue();
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(BaseAnimation.DEFAULT_ANIMATION_TIME);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return animator;
    }

    @NonNull
    public ScaleAnimation with(int colorStart,
                               int colorEnd,
                               int selectedRadius,
                               int selectingStartRadius,
                               int deselectingEndRadius) {
        if (animator != null && hasChanges(colorStart, colorEnd, selectedRadius, selectingStartRadius, deselectingEndRadius)) {

            this.colorStart = colorStart;
            this.colorEnd = colorEnd;

            this.selectedRadius = selectedRadius;
            this.selectingStartRadius = selectingStartRadius;
            this.deselectingEndRadius = deselectingEndRadius;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true);

            PropertyValuesHolder scaleHolder = createScalePropertyHolder(selectingStartRadius, selectedRadius);
            PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(selectedRadius, deselectingEndRadius);

            animator.setValues(colorHolder, reverseColorHolder, scaleHolder, scaleReverseHolder);
        }

        return this;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
        int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);

        ScaleAnimationValue value = getValue();
        value.setColor(color);
        value.setColorReverse(colorReverse);

        value.setRadius(radius);
        value.setRadiusReverse(radiusReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @NonNull
    protected PropertyValuesHolder createScalePropertyHolder(int startRadius, int endRadius) {
        String propertyName = startRadius < endRadius ? ANIMATION_SCALE : ANIMATION_SCALE_REVERSE;
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadius, endRadius);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStart, int colorEnd, int selectedRadiusValue, int selectingStartRadiusValue, int deselectingEndRadiusValue) {
        if (this.colorStart != colorStart) {
            return true;
        }

        if (this.colorEnd != colorEnd) {
            return true;
        }

        if (selectedRadius != selectedRadiusValue) {
            return true;
        }

        if (selectingStartRadius != selectingStartRadiusValue) {
            return true;
        }
        if (deselectingEndRadius != deselectingEndRadiusValue) {
            return true;
        }

        return false;
    }
}

