package com.michaelpardo.chartview.axis;

public abstract class AbstractAxis implements ChartAxis {
	private float mMin;
	private float mMax;
	private float mSize;

	private int mTickTarget = 16;

	//////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDES
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean setBounds(float min, float max) {
		if (mMin != min || mMax != max) {
			mMin = min;
			mMax = max;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean setSize(float size) {
		if (mSize != size) {
			mSize = size;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float convertToPoint(float value) {
		return (float) ((mSize * (value - mMin)) / (mMax - mMin));
	}

	@Override
	public float convertToValue(float point) {
		return mMin + ((point * (mMax - mMin)) / mSize);
	}

	@Override
	public float[] getTickPoints() {
		final double range = mMax - mMin;

		// target about 16 ticks on screen, rounded to nearest power of 2
		final long tickJump = roundUpToPowerOfTwo(Math.round(range) / mTickTarget);
		final int tickCount = (int) (range / tickJump);
		final float[] tickPoints = new float[tickCount];
		float value = mMin;
		for (int i = 0; i < tickPoints.length; i++) {
			tickPoints[i] = convertToPoint(value);
			value += tickJump;
		}

		return tickPoints;
	}

	private static long roundUpToPowerOfTwo(long i) {
		// NOTE: borrowed from Hashtable.roundUpToPowerOfTwo()

		i--; // If input is a power of two, shift its high-order bit right

		// "Smear" the high-order bit all the way to the right
		i |= i >>> 1;
		i |= i >>> 2;
		i |= i >>> 4;
		i |= i >>> 8;
		i |= i >>> 16;
		i |= i >>> 32;

		i++;

		return i > 0 ? i : Long.MAX_VALUE;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public int getTickTarget() {
		return mTickTarget;
	}

	public void setTickTarget(int tickTarget) {
		mTickTarget = tickTarget;
	}
}
