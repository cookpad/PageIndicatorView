package com.rd.utils;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

import static java.lang.Math.abs;

public class CoordinatesUtils {

	@SuppressWarnings("UnnecessaryLocalVariable")
	public static int getCoordinate(@Nullable Indicator indicator, int position) {
		if (indicator == null) {
			return 0;
		}

		if (indicator.getOrientation() == Orientation.HORIZONTAL) {
			return getXCoordinate(indicator, position);
		} else {
			return getYCoordinate(indicator, position);
		}
	}

    public static int getYTranslation(@Nullable Indicator indicator, int toSelectedPosition) {
        if (indicator == null || indicator.getOrientation() == Orientation.HORIZONTAL) {
            return 0;
        }
        int oldOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() :
                indicator.getLastSelectionOffset();
        int newOffset = indicator.isInteractiveAnimation() ? indicator.getNextSelectionOffset(toSelectedPosition) :
                indicator.getSelectionOffset();
        int nextPositionWithoutOffset = getYCoordinate(indicator, toSelectedPosition - oldOffset);
        return getYCoordinate(indicator, toSelectedPosition - newOffset) - nextPositionWithoutOffset;
    }

    public static int getXTranslation(@Nullable Indicator indicator, int toSelectedPosition) {
        if (indicator == null || indicator.getOrientation() == Orientation.VERTICAL) {
            return 0;
        }
        int oldOffset = indicator.isInteractiveAnimation() ? indicator.getSelectionOffset() :
                indicator.getLastSelectionOffset();
        int newOffset = indicator.isInteractiveAnimation() ? indicator.getNextSelectionOffset(toSelectedPosition) :
                indicator.getSelectionOffset();
        int nextPositionWithoutOffset = getXCoordinate(indicator, toSelectedPosition - oldOffset);
        return getXCoordinate(indicator, toSelectedPosition - newOffset) - nextPositionWithoutOffset;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int getXCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

		int coordinate;
		if (indicator.getOrientation() == Orientation.HORIZONTAL) {
			coordinate = getHorizontalCoordinate(indicator, position);
		} else {
			coordinate = getVerticalCoordinate(indicator);
		}

		coordinate += indicator.getPaddingLeft();
		return coordinate;
	}

	public static int getYCoordinate(@Nullable Indicator indicator, int position) {
		if (indicator == null) {
			return 0;
		}

		int coordinate;
		if (indicator.getOrientation() == Orientation.HORIZONTAL) {
			coordinate = getVerticalCoordinate(indicator);
		} else {
			coordinate = getHorizontalCoordinate(indicator, position);
		}

		coordinate += indicator.getPaddingTop();
		return coordinate;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public static int getPosition(@Nullable Indicator indicator, float x, float y) {
		if (indicator == null) {
			return -1;
		}

		float lengthCoordinate;
		float heightCoordinate;

		if (indicator.getOrientation() == Orientation.HORIZONTAL) {
			lengthCoordinate = x;
			heightCoordinate = y;
		} else {
			lengthCoordinate = y;
			heightCoordinate = x;
		}

		return getFitPosition(indicator, lengthCoordinate, heightCoordinate);
	}

	private static int getFitPosition(@NonNull Indicator indicator, float lengthCoordinate, float heightCoordinate) {
		int count = indicator.getDisplayedCount();
		int radius = indicator.getRadius();
		int stroke = indicator.getStroke();
		int padding = indicator.getPadding();

		int height = indicator.getOrientation() == Orientation.HORIZONTAL ? indicator.getHeight() : indicator.getWidth();
		int length = getFirstItemCoordinate(indicator) - radius - stroke / 2;

		for (int i = 0; i < count; i++) {
			int indicatorPadding = i > 0 ? padding : padding / 2;
			int startValue = length;

			length += radius * 2 + (stroke / 2) + indicatorPadding;
			int endValue = length;

			boolean fitLength = lengthCoordinate >= startValue && lengthCoordinate <= endValue;
			boolean fitHeight = heightCoordinate >= 0 && heightCoordinate <= height;

            if (fitLength && fitHeight) {
                return i + indicator.getSelectionOffset();
            }
        }

        return -1;
    }

    private static int getFirstItemCoordinate(@NonNull Indicator indicator) {
	    if (indicator.getDisplayedCount() < indicator.getCount()) {
	        return 3 * indicator.getRadius() + indicator.getPadding() + 3 * indicator.getStroke() / 2;
        } else {
	        return indicator.getRadius() + (indicator.getStroke() / 2);
        }
    }

    private static int getHorizontalCoordinate(@NonNull Indicator indicator, int position) {
        int radius = indicator.getRadius();
        int stroke = indicator.getStroke();
        int padding = indicator.getPadding();

		int sign = Integer.signum(position);
		int coordinate = getFirstItemCoordinate(indicator);
		for (int i = 0; i < abs(position); i++) {
			coordinate += sign * (2 * radius + padding + stroke);
		}

		return coordinate;
	}

	private static int getVerticalCoordinate(@NonNull Indicator indicator) {
		int radius = indicator.getRadius();
		int coordinate;

		if (indicator.getAnimationType() == AnimationType.DROP) {
			coordinate = radius * 3;
		} else {
			coordinate = radius;
		}

		return coordinate;
	}

	public static Pair<Integer, Float> getProgress(@NonNull Indicator indicator, int position, float positionOffset, boolean isRtl) {
		int count = indicator.getCount();
		int selectedPosition = indicator.getSelectedPosition();

		if (isRtl) {
			position = (count - 1) - position;
		}

		if (position < 0) {
			position = 0;

		} else if (position > count - 1) {
			position = count - 1;
		}

		boolean isOverscrolled = Math.abs(position - selectedPosition) > 1;

		if (isOverscrolled) {
			selectedPosition = position;
			indicator.setSelectedPosition(selectedPosition);
		}

		boolean slideToRightSide = selectedPosition == position && positionOffset != 0;
		int selectingPosition;
		float selectingProgress;

		if (slideToRightSide) {
			selectingPosition = isRtl ? position - 1 : position + 1;
			selectingProgress = positionOffset;

		} else {
			selectingPosition = position;
			selectingProgress = 1 - positionOffset;
		}

		if (selectingProgress > 1) {
			selectingProgress = 1;

		} else if (selectingProgress < 0) {
			selectingProgress = 0;
		}

		return new Pair<>(selectingPosition, selectingProgress);
	}
}
