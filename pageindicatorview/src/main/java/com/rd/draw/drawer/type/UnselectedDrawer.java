package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.rd.animation.data.Value;
import com.rd.draw.data.Indicator;

public class UnselectedDrawer extends BaseDrawer {
    public UnselectedDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(@NonNull Canvas canvas, int position, int coordinateX, int coordinateY, @Nullable Value value) {
        int color = indicator.getUnselectedColor();
        int radius = indicator.getRadius();

        int currentOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() : indicator.getLastSelectionOffset();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
        int nextOffset = indicator.isInteractiveAnimation() ? indicator.getNextSelectionOffset(toPosition) : indicator.getSelectionOffset();

        boolean isItemAppearing = position < currentOffset || position >= currentOffset + indicator.getDisplayedCount();
        boolean isItemDisappearing = position < nextOffset || position >= nextOffset + indicator.getDisplayedCount();
        float progress = value != null ? value.getProgress() : 0f;
        if (isItemAppearing) {
            color = ColorUtils.setAlphaComponent(color, (int) (Color.alpha(color) * progress));
        }
        if (isItemDisappearing) {
            color = ColorUtils.setAlphaComponent(color, (int) (Color.alpha(color) * (1f - progress)));
        }

        paint.setColor(color);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);
    }
}
