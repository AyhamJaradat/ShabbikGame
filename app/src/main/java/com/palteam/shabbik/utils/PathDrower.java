package com.palteam.shabbik.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

import com.palteam.shabbik.R;

public class PathDrower {
    /*
     * Parameters to draw Line with finger move
     */
    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private float downx = 0, downy = 0, upx = 0, upy = 0;
    private boolean doItOnce;

    public PathDrower(Activity activity) {

        this.imageView = (ImageView) activity
                .findViewById(R.id.backImageForDraw);

//		int imageWidth = 400;
//		int imageHeight = 400;
//		this.bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
//				Bitmap.Config.ARGB_4444);
//		this.canvas = new Canvas(bitmap);
        this.paint = new Paint();

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.rgb(247, 158, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);
//		this.imageView.setImageBitmap(bitmap);

        doItOnce = true;

    }

    public void drawLine() {
        int[] viewCoords = new int[2];

        this.imageView.getLocationOnScreen(viewCoords);

        this.canvas.drawLine(downx - viewCoords[0], downy - viewCoords[1], upx
                - viewCoords[0], upy - viewCoords[1], paint);

        this.imageView.invalidate();

    }

    public void clearDrawing() {

        // this.imageView.setDrawingCacheEnabled(false);
        // this.canvas.drawColor(Color.TRANSPARENT);
        bitmap.eraseColor(Color.TRANSPARENT);
        this.imageView.invalidate();
        // this.imageView.setDrawingCacheEnabled(true);

    }

    public void initilizeBitmap(Context context) {

        int imageWidth = this.imageView.getWidth();
        int imageHeight = this.imageView.getHeight();

        if (imageWidth <= 0 || imageHeight <= 0) {
            imageWidth = 400;
            imageHeight = 400;
        }

//		this.bitmap.recycle();
        this.bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                Bitmap.Config.ARGB_4444);
        //bitmap.reconfigure(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        //  bitmap = Methods.decodeSampledBitmapFromResource(context.getResources(), R.drawable.character_bg, imageWidth, imageHeight).copy(Bitmap.Config.ARGB_8888, true);
        bitmap.eraseColor(Color.TRANSPARENT);

        this.canvas = new Canvas(bitmap);

        this.imageView.setImageBitmap(bitmap);

        this.imageView.invalidate();
    }

    public boolean isDoItOnce() {
        return doItOnce;
    }

    public void setDoItOnce(boolean doItOnce) {
        this.doItOnce = doItOnce;
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

}
