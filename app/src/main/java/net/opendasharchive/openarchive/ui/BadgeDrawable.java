package net.opendasharchive.openarchive.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import net.opendasharchive.openarchive.R;

public class BadgeDrawable extends Drawable {

    private Paint mBadgePaint;
    private Paint mBadgePaint1;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();

    private String mCount = "";

    public BadgeDrawable(Context context) {

        float mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);

        int color = ContextCompat.getColor(context.getApplicationContext(), R.color.oablue);

        mBadgePaint = new Paint();
        mBadgePaint.setColor(color);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.STROKE);
        mBadgePaint.setStrokeWidth(5);

        mTextPaint = new Paint();
        mTextPaint.setColor(color);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {

        Rect bounds = getBounds();
        float width = 56;//bounds.right - bounds.left;
        float height = 56;//bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */
        if (width > 0) {

            float radius = (float)((Math.max(width, height) / 2));
            float centerX = (width - radius);
            float centerY = radius;

            // Draw badge count text inside the circle.
            mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
            float textHeight = mTxtRect.bottom - mTxtRect.top;
            float textY = centerY + (textHeight / 2f);

            canvas.drawText(mCount, 0, centerY/2, mTextPaint);
            canvas.drawCircle(0, 0, (int) (radius), mBadgePaint);
        }
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(String count) {
        mCount = count;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}