package com.dev.sim8500.githapp.presentation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by sbernad on 21/10/14.
 */

/**
 *  implementation taken from Romain Guy's blog:
 *  http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
 */

public class CircularFrameDrawable extends Drawable {
    private static final boolean USE_VIGNETTE = false;

    private final float mRadius;
    private final RectF mRect = new RectF();
    private final BitmapShader mBitmapShader;
    private final Paint mPaint;
    private final int mMargin;
    private final int bitmapWidth;
    private final int bitmapHeight;

    CircularFrameDrawable(Bitmap bitmap, int margin) {
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        mRadius = Math.min(bitmapWidth, bitmapHeight);

        mBitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);

        mMargin = margin;
        mRect.set(mMargin, mMargin, bitmapWidth - mMargin, bitmapHeight - mMargin);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);

        if (USE_VIGNETTE) {
            RadialGradient vignette = new RadialGradient(
                    mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
                    new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
                    Shader.TileMode.CLAMP);

            Matrix oval = new Matrix();
            oval.setScale(1.0f, 0.7f);
            vignette.setLocalMatrix(oval);

            mPaint.setShader(
                    new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawOval(mRect, mPaint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    public static Bitmap fromBitmap(Bitmap source)
    {
        CircularFrameDrawable drw = new CircularFrameDrawable(source, 0);

        return drw.toBitmap();
    }

    public Bitmap toBitmap()
    {
        Bitmap resBmp;
        try {
            resBmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas resCanvas = new Canvas(resBmp);
            draw(resCanvas);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            resBmp = null;
        }
        return resBmp;
    }
}