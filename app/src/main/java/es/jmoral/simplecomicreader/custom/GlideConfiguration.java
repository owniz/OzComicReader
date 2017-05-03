package es.jmoral.simplecomicreader.custom;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by owniz on 1/05/17.
 */

public class GlideConfiguration implements GlideModule {

    @Override
    public void registerComponents(Context context, Glide glide) {
        // unused
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }
}