package com.example.android.pinprogress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgress extends View {
	private int mMax;
	private int mProgress;
	private Paint mProgressTextPaint;
	private Paint mCirclePaint;
	private Paint mProgressPaint;
	private Rect mTempRect = new Rect();
	private RectF mTempRectF = new RectF();

	private int mDrawableSize;
	private int mInnerSize;
	private Rect clip;

	public CircleProgress(Context context) {
		super(context);
		init(context, null, 0);
	}

	public CircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public CircleProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		setWillNotDraw(false);
		mMax = 100;
		mProgress = 0;

		final Resources res = getResources();
		int circleColor = res
				.getColor(R.color.pin_progress_default_circle_color);
		int progressColor = res
				.getColor(R.color.pin_progress_default_progress_color);

		if (attrs != null) {
			// Attribute initialization
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.PinProgressButton, defStyle, 0);

			mMax = a.getInteger(R.styleable.PinProgressButton_max, mMax);
			mProgress = a.getInteger(R.styleable.PinProgressButton_progress,
					mProgress);

			circleColor = a.getColor(R.styleable.PinProgressButton_circleColor,
					circleColor);
			progressColor = a.getColor(
					R.styleable.PinProgressButton_progressColor, progressColor);

			a.recycle();
		}
		Drawable mShadowDrawable = res.getDrawable(R.drawable.pin_progress_shadow);
		mDrawableSize = mShadowDrawable.getIntrinsicWidth();

		mInnerSize = res.getDimensionPixelSize(R.dimen.pin_progress_inner_size);

		mCirclePaint = new Paint();
		mCirclePaint.setColor(circleColor);
		mCirclePaint.setAntiAlias(true);

		mProgressPaint = new Paint();
		mProgressPaint.setColor(progressColor);
		mProgressPaint.setAntiAlias(true);

		mProgressTextPaint = new Paint();
		mProgressTextPaint.setColor(res.getColor(android.R.color.black));
		mProgressTextPaint.setAntiAlias(true);
		mProgressTextPaint.setTextSize(12);
		clip = new Rect();
	}

	/**
	 * Returns the maximum download progress value.
	 */
	public int getMax() {
		return mMax;
	}

	/**
	 * Sets the maximum download progress value. Defaults to 100.
	 */
	public void setMax(int max) {
		mMax = max;
		invalidate();
	}

	/**
	 * Returns the current download progress from 0 to max.
	 */
	public int getProgress() {
		return mProgress;
	}

	/**
	 * Sets the current download progress (between 0 and max).
	 * 
	 * @see #setMax(int)
	 */
	public void setProgress(int progress) {
		mProgress = progress;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(resolveSize(mDrawableSize, widthMeasureSpec),
				resolveSize(mDrawableSize, heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mTempRect.set(0, 0, mDrawableSize, mDrawableSize);
		mTempRect.offset((getWidth() - mDrawableSize) / 2,
				(getHeight() - mDrawableSize) / 2);

		mTempRectF.set(-0.5f, -0.5f, mInnerSize + 0.5f, mInnerSize + 0.5f);
		mTempRectF.offset((getWidth() - mInnerSize) / 2,
				(getHeight() - mInnerSize) / 2);

		canvas.getClipBounds(clip);
		canvas.drawArc(mTempRectF, 0, 360, true, mCirclePaint);
		canvas.drawArc(mTempRectF, -90, 360 * mProgress / mMax, true,
				mProgressPaint);

		// 中心座標
		float centerX = getWidth() / 2;
		float centerY = getHeight() / 2;
		// 中心線の描画(中心確認用)
		// Paint centerLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG);
		// canvas.drawLine(0, centerY, getWidth(), centerY, centerLinePaint);
		// canvas.drawLine(centerX, 0, centerX, getHeight(), centerLinePaint);
		String progressText = Integer.toString(mProgress) + "%";
		FontMetrics fontMetrics = mProgressTextPaint.getFontMetrics();
		float textWidth = mProgressTextPaint.measureText(progressText);
		float baseX = centerX - textWidth / 2;
		float baseY = centerY - (fontMetrics.ascent + fontMetrics.descent) / 2;
		canvas.drawText(progressText, baseX, baseY, mProgressTextPaint);
	}

	/**
	 * A {@link Parcelable} representing the {@link HpPinProgressButton}'s
	 * state.
	 */
	public static class SavedState extends BaseSavedState {
		private int mProgress;
		private int mMax;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			mProgress = in.readInt();
			mMax = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(mProgress);
			out.writeInt(mMax);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel parcel) {
				return new SavedState(parcel);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		if (isSaveEnabled()) {
			SavedState ss = new SavedState(superState);
			ss.mMax = mMax;
			ss.mProgress = mProgress;
			return ss;
		}
		return superState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		mMax = ss.mMax;
		mProgress = ss.mProgress;
	}
}
