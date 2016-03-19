package com.dev.sim8500.githapp.presentation;


import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by sbernad on 29/10/14.
 */
public final class CircularFrameTransformBuilder
{
    public static Transformation build()
    {
        return new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap transformed = CircularFrameDrawable.fromBitmap(source);
                if(!source.equals(transformed))
                    source.recycle();

                return transformed;
            }

            @Override
            public String key() {
                return "circ_frame_default";
            }
        };
    }
}
