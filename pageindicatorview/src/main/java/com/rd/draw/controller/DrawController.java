package com.rd.draw.controller;

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rd.animation.data.Value;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.draw.drawer.Drawer;
import com.rd.draw.drawer.type.ScaleCommon;
import com.rd.utils.CoordinatesUtils;

public class DrawController {

    private Value value;
    private Drawer drawer;
    private Indicator indicator;
    private ClickListener listener;

    public interface ClickListener {

        void onIndicatorClicked(int position);
    }

    public DrawController(@NonNull Indicator indicator,
                          @NonNull ScaleCommon scaleCommon) {
        this.indicator = indicator;
        this.drawer = new Drawer(indicator, scaleCommon);
    }

    public void updateValue(@Nullable Value value) {
        this.value = value;
    }

    public void setClickListener(@Nullable ClickListener listener) {
        this.listener = listener;
    }

    public void touch(@Nullable MotionEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                onIndicatorTouched(event.getX(), event.getY());
                break;
            default:
        }
    }

    private void onIndicatorTouched(float x, float y) {
        if (listener != null) {
            int position = CoordinatesUtils.getPosition(indicator, x, y);
            if (position >= 0) {
                listener.onIndicatorClicked(position);
            }
        }
    }

    public void draw(@NonNull Canvas canvas) {
        int count = indicator.getDisplayedCount();
        int offset = indicator.getSelectionOffset();
        int firstPosition = 0;
        int lastPosition = count;
        if (value != null && value.getProgress() > 0f && value.getProgress() < 1.0f) {
            int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
            int nextOffset = indicator.isInteractiveAnimation() ? indicator.getNextSelectionOffset(toPosition) : indicator.getSelectionOffset();
            int currOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() : indicator.getLastSelectionOffset();
            if (nextOffset > currOffset) {
                lastPosition += 1;
            }
            if (nextOffset < currOffset) {
                firstPosition -= 1;
            }
        }


        for (int position = firstPosition; position < lastPosition; position++) {
            int coordinateX = CoordinatesUtils.getXCoordinate(indicator, position);
            int coordinateY = CoordinatesUtils.getYCoordinate(indicator, position);
            int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
            int transitionX = 0;
            int transitionY = 0;
            if (value != null) {
                transitionX = (int) (CoordinatesUtils.getXTranslation(indicator, toPosition) * value.getProgress());
                transitionY = (int) (CoordinatesUtils.getYTranslation(indicator, toPosition) * value.getProgress());
            }
            drawIndicator(canvas, position + offset, coordinateX + transitionX, coordinateY + transitionY);
        }
    }

    private void drawIndicator(
            @NonNull Canvas canvas,
            int position,
            int coordinateX,
            int coordinateY) {

        boolean interactiveAnimation = indicator.isInteractiveAnimation();
        int selectedPosition = indicator.getSelectedPosition();
        int selectingPosition = indicator.getSelectingPosition();
        int lastSelectedPosition = indicator.getLastSelectedPosition();

        boolean selectedItem = !interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition);
        boolean selectingItem = interactiveAnimation && (position == selectedPosition || position == selectingPosition);
        boolean isSelectedItem = selectedItem | selectingItem;
        drawer.setup(position, coordinateX, coordinateY);

        if (value != null && isSelectedItem) {
            drawWithAnimation(canvas);
        } else {
            if (!isSelectedItem && indicator.getAnimationType() == AnimationType.SCALE) {
                drawer.drawUnselectedScale(canvas, value);
            } else if (!isSelectedItem && indicator.getAnimationType() != AnimationType.NONE && value != null) {
                drawer.drawUnselected(canvas, value);
            } else {
                drawer.drawBasic(canvas, isSelectedItem);
            }
        }
    }

    private void drawWithAnimation(@NonNull Canvas canvas) {
        AnimationType animationType = indicator.getAnimationType();
        switch (animationType) {
            case NONE:
                drawer.drawBasic(canvas, true);
                break;

            case COLOR:
                drawer.drawColor(canvas, value);
                break;

            case SCALE:
                drawer.drawScale(canvas, value);
                break;

            case WORM:
                drawer.drawWorm(canvas, value);
                break;

            case SLIDE:
                drawer.drawSlide(canvas, value);
                break;

            case FILL:
                drawer.drawFill(canvas, value);
                break;

            case THIN_WORM:
                drawer.drawThinWorm(canvas, value);
                break;

            case DROP:
                drawer.drawDrop(canvas, value);
                break;

            case SWAP:
                drawer.drawSwap(canvas, value);
                break;

            case SCALE_DOWN:
                drawer.drawScaleDown(canvas, value);
                break;
        }
    }
}
