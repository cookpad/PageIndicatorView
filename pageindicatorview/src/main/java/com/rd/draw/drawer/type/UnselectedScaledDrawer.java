package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

import com.rd.animation.data.Value;
import com.rd.draw.data.Indicator;

public class UnselectedScaledDrawer extends UnselectedDrawer {

    @NonNull
    private final ScaleCommon common;

    public UnselectedScaledDrawer(@NonNull Paint paint, @NonNull Indicator indicator, @NonNull ScaleCommon common) {
        super(paint, indicator);
        this.common = common;
    }

    public void draw(@NonNull Canvas canvas, int position, int coordinateX, int coordinateY, Value value) {
        int color = indicator.getUnselectedColor();

        int currentOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() : indicator.getLastSelectionOffset();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();
        int nextOffset = indicator.isInteractiveAnimation() ? indicator.getNextSelectionOffset(toPosition) : indicator.getSelectionOffset();

        boolean isItemAppearing = position < currentOffset || position >= currentOffset + indicator.getDisplayedCount();
        boolean isItemDisappearing = position < nextOffset || position >= nextOffset + indicator.getDisplayedCount();
        boolean isAdjacentItemAppearing = position == currentOffset && nextOffset < currentOffset
                || position == currentOffset + indicator.getDisplayedCount() - 1 && nextOffset > currentOffset;
        boolean isAdjacentItemDisappearing =
                position == nextOffset && nextOffset > currentOffset
                        || position == nextOffset + indicator.getDisplayedCount() - 1 && nextOffset < currentOffset;
        final int minRadius = common.getMinRadius(position, indicator);
        final int maxRadius = common.getMaxRadius(position, indicator);
        if (isItemAppearing) {
            color = ColorUtils.setAlphaComponent(color, (int) (Color.alpha(color) * value.getProgress()));
        }
        if (isItemDisappearing) {
            color = ColorUtils.setAlphaComponent(color, (int) (Color.alpha(color) * (1f - value.getProgress())));
        }
        float radius = maxRadius;
        if (isItemAppearing || isAdjacentItemAppearing) {
            radius = minRadius + (maxRadius - minRadius) * value.getProgress();
        }
        if (isItemDisappearing || isAdjacentItemDisappearing) {
            radius = minRadius + (maxRadius - minRadius) * (1f - value.getProgress());
        }

        paint.setColor(color);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);
    }
}

