package com.wildsmith.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * Provides utility methods for loading/manipulating {@link Bitmap}s.
 */
public class BitmapUtils {

    static final int DEFAULT_IMAGE_LENGTH = 1024;

    private static final int MAX_IMAGE_SIZE = DEFAULT_IMAGE_LENGTH * DEFAULT_IMAGE_LENGTH;

    /**
     * Load a {@link Bitmap} of arbitrarily large size sub-sampled to the requested width and height. If the requested width and height are
     * 0 then there will be NO sub-sampling and the {@link Bitmap} will be loaded at it's full resolution.
     *
     * @return the sub-sampled {@link Bitmap} from resources.
     */
    static Bitmap decodeSampledBitmapFromResource(@NonNull Resources resources, @DrawableRes int resourceId, int requestWidth,
        int requestedHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourceId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requestWidth, requestedHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resourceId, options);
    }

    /**
     * Load a {@link Bitmap} of arbitrarily large size sub-sampled to the requested width and height. If the requested width and height are
     * 0 then there will be NO sub-sampling and the {@link Bitmap} will be loaded at it's full resolution.
     *
     * @return the sub-sampled {@link Bitmap} from resources.
     */
    static Bitmap decodeSampledBitmapFromFile(@NonNull String imagePath, int requestWidth, int requestedHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requestWidth, requestedHeight);

        // Decode bitmap with inSampleSize se
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * @return the calculated sample size value that is a power of two based on the requested width and height. If the requested width and
     * height are 0 then the sample size will have a value of 1, meaning there will be no sub-sampling when the {@link Bitmap} is loaded
     * from memory.
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int requestWidth, int requestedHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (requestWidth <= 0 || requestedHeight <= 0) {
            return inSampleSize;
        }

        if (height > requestedHeight || width > requestWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested
            // height and width.
            while ((halfHeight / inSampleSize) > requestedHeight && (halfWidth / inSampleSize) > requestWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] createViewBitmapArray(@NonNull View view) {
        Bitmap bitmap = createViewBitmap(view);
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Nullable
    public static Bitmap createViewBitmap(@NonNull View view) {
        RectF bounds = new RectF();
        bounds.set(0, 0, view.getWidth(), view.getHeight());

        Matrix matrix = new Matrix();
        // TODO stop declaring these values over and over, also find out what transformMatrixToGlobal's source code is
//        matrix.reset();
//        view.transformMatrixToGlobal(matrix);
        matrix.mapRect(bounds);

        Bitmap bitmap = null;
        int bitmapWidth = Math.round(bounds.width());
        int bitmapHeight = Math.round(bounds.height());
        if (bitmapWidth > 0 && bitmapHeight > 0) {
            float scale = Math.min(1f, ((float) MAX_IMAGE_SIZE) / (bitmapWidth * bitmapHeight));
            bitmapWidth *= scale;
            bitmapHeight *= scale;
            matrix.postTranslate(-bounds.left, -bounds.top);
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.concat(matrix);
            view.draw(canvas);
        }
        return bitmap;
    }
}