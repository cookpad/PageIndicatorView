package com.rd.draw.drawer.type;

import androidx.annotation.NonNull;

import com.rd.draw.data.Indicator;

public class ScaleCommon {
    public int getMinRadius(int position, @NonNull Indicator indicator) {
        final float initialRadius = indicator.getRadius();
        final float scaleFactor = indicator.getScaleFactor();

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
        return isItemAppearing || isItemDisappearing ?
                0 :
                isAdjacentItemAppearing || isAdjacentItemDisappearing ?
                        (int) (initialRadius * scaleFactor * scaleFactor) :
                        getMaxRadius(position, indicator);
    }

    public int getMaxRadius(int position, @NonNull Indicator indicator) {
        final float initialRadius = indicator.getRadius();
        final float scaleFactor = indicator.getScaleFactor();

        int currentOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() : indicator.getLastSelectionOffset();
        return position > 0 && position <= currentOffset
                || position < indicator.getCount() - 1 && position >= currentOffset + indicator.getDisplayedCount() - 1
                ? (int) (initialRadius * scaleFactor * scaleFactor)
                : (int) (initialRadius * scaleFactor);
    }
}
