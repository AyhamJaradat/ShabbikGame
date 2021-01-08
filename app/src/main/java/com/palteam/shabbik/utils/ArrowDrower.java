package com.palteam.shabbik.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;
import android.widget.ImageView;

import com.palteam.shabbik.R;

public class ArrowDrower {

    /*
     * Parameters to draw Line with finger move
     */
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private float downx = 0, downy = 0, upx = 0, upy = 0;
    private Path mPath;
    private boolean doItOnce;

    private PointF startPoint, endPoint;

    public ArrowDrower(Activity activity) {

        this.imageView = (ImageView) activity
                .findViewById(R.id.onTopImageForDraw);

        int imageWidth = 400;
        int imageHeight = 400;
        this.bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                Bitmap.Config.ARGB_4444);

        //   bitmap = Methods.decodeSampledBitmapFromResource(context.getResources(), R.drawable.character_bg, imageWidth, imageHeight).copy(Bitmap.Config.ARGB_8888, true);

        bitmap.eraseColor(Color.TRANSPARENT);
        this.canvas = new Canvas(bitmap);
        this.paint = new Paint();
        this.mPath = new Path();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.rgb(232, 232, 232));
        // Color.rgb(247, 226, 159)
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);
        paint.setShadowLayer(5, 5, 5, Color.rgb(80, 80, 80));
        this.imageView.setImageBitmap(bitmap);
        doItOnce = true;

    }

    public boolean isDoItOnce() {
        return doItOnce;
    }

    public void setDoItOnce(boolean doItOnce) {
        this.doItOnce = doItOnce;
    }

    public void clearDrawing() {

        Log.i("ArrowDrower", "clearDrawing");
        // if (bitmap != null) {
        bitmap.eraseColor(Color.TRANSPARENT);
        this.imageView.invalidate();
        // }
    }

    public void initilizeBitmap() {
        Log.i("ArrowDrower", "initilizeBitmap");

        int imageWidth = this.imageView.getWidth();
        int imageHeight = this.imageView.getHeight();

        if (imageWidth <= 0 || imageHeight <= 0) {
            imageWidth = 400;
            imageHeight = 400;
            Log.e("ArrowDrower Daoud", "222222222222222");
        }
        Log.i("arrow dime", "" + imageWidth + "  " + imageHeight);
        // this.bitmap.recycle();
        this.bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                Bitmap.Config.ARGB_4444);

        //    bitmap = Methods.decodeSampledBitmapFromResource(this.context.getResources(), R.drawable.character_bg, imageWidth, imageHeight).copy(Bitmap.Config.ARGB_8888, true);
        Log.i("arrow dime after ", "" +  bitmap.getWidth() + "  " + bitmap.getHeight());
        bitmap.eraseColor(Color.TRANSPARENT);
        this.canvas = new Canvas(bitmap);

        this.imageView.setImageBitmap(bitmap);
        // this.imageView.setVisibility(View.INVISIBLE);
        //this.imageView.invalidate();
    }

    public float getDownx() {
        return downx;
    }

    public void setDownx(float downx) {
        this.downx = downx;
    }

    public float getDowny() {
        return downy;
    }

    public void setDowny(float downy) {
        this.downy = downy;
    }

    public float getUpx() {
        return upx;
    }

    public void setUpx(float upx) {
        this.upx = upx;
    }

    public float getUpy() {
        return upy;
    }

    public void setUpy(float upy) {
        this.upy = upy;
    }

    public void drawArrow(int myMargin) {
        Log.i("ArrowDrower", "drawArrow");
        int[] viewCoords = new int[2];
        this.imageView.getLocationOnScreen(viewCoords);
        downx = downx - viewCoords[0];
        downy = downy - viewCoords[1];
        upx = upx - viewCoords[0];
        upy = upy - viewCoords[1];
        float margin = myMargin;
        float shortDownY = 0, shortDownX = 0, shortUpY = 0, shortUpX = 0;
        float slope = calculateSlope(downx, downy, upx, upy);
        if (slope == -5) {
            // Vertical line
            shortDownX = downx;
            shortUpX = upx;
            if (downy < upy) {
                // going down
                shortDownY = downy + margin;
                shortUpY = upy - margin;
            } else {
                // going up
                shortDownY = downy - margin;
                shortUpY = upy + margin;
            }
        } else if (slope == -10) {
            // Horizontal line
            shortDownY = downy;
            shortUpY = upy;
            if (downx < upx) {
                // going right
                shortDownX = downx + margin;
                shortUpX = upx - margin;
            } else {
                // going left
                shortDownX = downx - margin;
                shortUpX = upx + margin;
            }

        } else {
            // diagonal
            if (downy <= upy && downx <= upx) {
                // going down
                slope = 1;

                shortDownX = downx + margin;
                shortDownY = calculateY(slope, shortDownX, upx, upy);

                shortUpX = upx - margin;
                shortUpY = calculateY(slope, shortUpX, upx, upy);

            } else if (downy <= upy && downx > upx) {
                // working good
                slope = -1;
                shortDownX = downx - margin;
                shortDownY = calculateY(slope, shortDownX, upx, upy);

                shortUpX = upx + margin;
                shortUpY = calculateY(slope, shortUpX, upx, upy);

            } else if (downy > upy && downx <= upx) {
                // going up
                // working good
                slope = -1;
                shortDownX = downx + margin;
                shortDownY = calculateY(slope, shortDownX, upx, upy);
                shortUpX = upx - margin;
                shortUpY = calculateY(slope, shortUpX, upx, upy);

            } else if (downy > upy && downx > upx) {
                slope = 1;
                shortDownX = downx - margin;
                shortDownY = calculateY(slope, shortDownX, upx, upy);
                shortUpX = upx + margin;
                shortUpY = calculateY(slope, shortUpX, upx, upy);
            }

        }

        mPath.reset();
        mPath.moveTo(shortDownX, shortDownY);

        startPoint = new PointF(shortDownX, shortDownY);
        endPoint = new PointF(shortUpX, shortUpY);

        this.imageView.invalidate();

        mPath.lineTo(shortUpX, shortUpY);

        float deltaX = endPoint.x - startPoint.x;
        float deltaY = endPoint.y - startPoint.y;
        float frac = (float) 0.1;
        float point_x_1 = startPoint.x
                + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = startPoint.y
                + (float) ((1 - frac) * deltaY - frac * deltaX);
        float point_x_2 = endPoint.x;
        float point_y_2 = endPoint.y;
        float point_x_3 = startPoint.x
                + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = startPoint.y
                + (float) ((1 - frac) * deltaY + frac * deltaX);
        mPath.moveTo(point_x_1, point_y_1);
        mPath.lineTo(point_x_2, point_y_2);
        mPath.lineTo(point_x_3, point_y_3);
        mPath.lineTo(point_x_1, point_y_1);
        mPath.lineTo(point_x_1, point_y_1);
        this.canvas.drawPath(mPath, paint);

        this.imageView.invalidate();

    }

    /*
     * private float calculateX(float slope, float y1, float rX2, float rY2) {
     * float x1 = ((y1 - rY2) / slope) + rX2; return x1; }
     */

    private float calculateY(float slope, float x1, float rX2, float rY2) {

        float y1 = (slope * (x1 - rX2)) + rY2;
        return y1;
    }

    private float calculateSlope(float x1, float y1, float x2, float y2) {

        if (x1 == x2) {
            return -5;
        } else if (y1 == y2) {
            return -10;
        } else {
            return ((y1 - y2) / (x1 - x2));
        }
    }

}
