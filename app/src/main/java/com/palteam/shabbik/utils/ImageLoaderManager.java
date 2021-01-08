package com.palteam.shabbik.utils;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.palteam.shabbik.R;

public class ImageLoaderManager {
    private final String TAG = ImageLoaderManager.class.getSimpleName();

    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;

    public ImageLoaderManager() {

        initializeImageLoaderWithDefaultConfigurations();
    }

    /**
     * Initialize image loader with default configuration as needed for our
     * application.
     */
    public void initializeImageLoaderWithDefaultConfigurations() {

        // Set default images for no contact photo depends on application mode
        // (day/night).
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_default)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.profile_default)
                .showImageOnFail(R.drawable.profile_default)
                .cacheInMemory(true).build();

        imageLoader = ImageLoader.getInstance();

    }

    /**
     * Set the Image view using URI.
     */
    public void loadImage(ImageView imageView, Uri uri) {

        // Use image loader
        try {
            if (uri == null || uri.toString().length() == 0
                    || uri.toString().equals(IConstants.NULL_VALUE)) {

                // Empty URI, load default image.
                imageView.setImageResource(R.drawable.profile_default);

            } else {

                imageLoader.displayImage(uri.toString(), imageView,
                        displayImageOptions);
            }

        } catch (Exception e) {
            Log.d(TAG, "Invalid image uri: " + uri);
        }

    }
}
