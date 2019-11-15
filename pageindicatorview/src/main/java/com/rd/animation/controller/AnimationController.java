package com.rd.animation.controller;

import androidx.annotation.NonNull;
import com.rd.animation.type.AnimationType;
import com.rd.animation.type.BaseAnimation;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;
import com.rd.draw.drawer.type.ScaleCommon;
import com.rd.utils.CoordinatesUtils;

public class AnimationController {

    private ValueController valueController;
    private ValueController.UpdateListener listener;

    private BaseAnimation runningAnimation;
    private Indicator indicator;
    private ScaleCommon scaleCommon;

    private float progress;
    private boolean isInteractive;

    public AnimationController(@NonNull Indicator indicator,
                               @NonNull ValueController.UpdateListener listener,
                               @NonNull ScaleCommon scaleCommon) {
        this.valueController = new ValueController(listener);
        this.listener = listener;
        this.indicator = indicator;
        this.scaleCommon = scaleCommon;
    }

    public void interactive(float progress) {
        this.isInteractive = true;
        this.progress = progress;
        animate();
    }

    public void basic() {
        this.isInteractive = false;
        this.progress = 0;
        animate();
    }

    public void end() {
        if (runningAnimation != null) {
            runningAnimation.end();
        }
    }

    private void animate() {
        AnimationType animationType = indicator.getAnimationType();
        switch (animationType) {
            case NONE:
                listener.onValueUpdated(null);
                break;

            case COLOR:
                colorAnimation();
                break;

            case SCALE:
                scaleAnimation();
                break;

            case WORM:
                wormAnimation();
                break;

            case FILL:
                fillAnimation();
                break;

            case SLIDE:
                slideAnimation();
                break;

            case THIN_WORM:
                thinWormAnimation();
                break;

            case DROP:
                dropAnimation();
                break;

            case SWAP:
                swapAnimation();
                break;

            case SCALE_DOWN:
                scaleDownAnimation();
                break;
        }
    }

    private void colorAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .color()
                .with(unselectedColor, selectedColor)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void scaleAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadius();
        long animationDuration = indicator.getAnimationDuration();

        int selectingPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
        int deselectingPosition = indicator.isInteractiveAnimation()? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();

        BaseAnimation animation = valueController
                .scale()
                .with(unselectedColor,
                        selectedColor,
                        radiusPx,
                        scaleCommon.getMinRadius(selectingPosition, indicator),
                        scaleCommon.getMinRadius(deselectingPosition, indicator))
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void wormAnimation() {
        int from = CoordinatesUtils.getCoordinate(indicator, getFromPositionDisplayed(indicator));
        int to = CoordinatesUtils.getCoordinate(indicator, getToPositionDisplayed(indicator));

        int radiusPx = indicator.getRadius();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .worm()
                .with(from, to, radiusPx, isRightSide(indicator))
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void slideAnimation() {
        int from = CoordinatesUtils.getCoordinate(indicator, getFromPositionDisplayed(indicator));
        int to = CoordinatesUtils.getCoordinate(indicator, getToPositionDisplayed(indicator));
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .slide()
                .with(from, to)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void fillAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadius();
        int strokePx = indicator.getStroke();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .fill()
                .with(unselectedColor, selectedColor, radiusPx, strokePx)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void thinWormAnimation() {
        int from = CoordinatesUtils.getCoordinate(indicator, getFromPositionDisplayed(indicator));
        int to = CoordinatesUtils.getCoordinate(indicator, getToPositionDisplayed(indicator));

        int radiusPx = indicator.getRadius();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .thinWorm()
                .with(from, to, radiusPx, isRightSide(indicator))
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void dropAnimation() {
        int widthFrom = CoordinatesUtils.getCoordinate(indicator, getFromPositionDisplayed(indicator));
        int widthTo = CoordinatesUtils.getCoordinate(indicator, getToPositionDisplayed(indicator));

        int paddingTop = indicator.getPaddingTop();
        int paddingLeft = indicator.getPaddingLeft();
        int padding = indicator.getOrientation() == Orientation.HORIZONTAL ? paddingTop : paddingLeft;

        int radius = indicator.getRadius();
        int heightFrom = radius * 3 + padding;
        int heightTo = radius + padding;

        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .drop()
                .duration(animationDuration)
                .with(widthFrom, widthTo, heightFrom, heightTo, radius);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void swapAnimation() {
        int from = CoordinatesUtils.getCoordinate(indicator, getFromPositionDisplayed(indicator));
        int to = CoordinatesUtils.getCoordinate(indicator, getToPositionDisplayed(indicator));
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .swap()
                .with(from, to)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void scaleDownAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadius();
        float scaleFactor = indicator.getScaleFactor();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .scaleDown()
                .with(unselectedColor, selectedColor, radiusPx, scaleFactor)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private boolean isRightSide(@NonNull Indicator indicator) {
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
        return toPosition > fromPosition;
    }

    private int getFromPositionDisplayed(@NonNull Indicator indicator) {
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int fromOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() : indicator.getLastSelectionOffset();
        return fromPosition - fromOffset;
    }

    private int getToPositionDisplayed(@NonNull Indicator indicator) {
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
        int toOffset = indicator.getSelectionOffset();
        return toPosition - toOffset;
    }
}

