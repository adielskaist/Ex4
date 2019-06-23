package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback,
        View.OnTouchListener {
    private float centerX;

    private float centerY;
    private float baseRadius;

    private float hatRadius;
    private Paint colors;

    private JoystickListener joystickCallback;

    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    private void setupDimensions() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;

        colors = new Paint();
    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = this.getHolder().lockCanvas();

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawRGB(255,255,255);
            colors.setARGB(255, 50, 50, 50);
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 100, 100, 100);
            canvas.drawCircle(newX, newY, hatRadius, colors);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(this)) {
            if (motionEvent.getAction() != motionEvent.ACTION_UP) {
                float displacement = (float) Math.sqrt(Math.pow(motionEvent.getX() - centerX, 2)
                        + Math.pow(motionEvent.getY() - centerY, 2));
                if (displacement < baseRadius) {
                    drawJoystick(motionEvent.getX(), motionEvent.getY());
                    joystickCallback.onJoystickMoved(
                            (motionEvent.getX() - centerX) / baseRadius,
                            (motionEvent.getY() - centerY) / baseRadius, getId());
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (motionEvent.getX() - centerX) * ratio;
                    float constrainedY = centerY + (motionEvent.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved(
                            (constrainedX - centerX) / baseRadius,
                            (constrainedY - centerY) / baseRadius, getId());
                }
            } else if (motionEvent.getAction() == motionEvent.ACTION_UP) {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0, 0, getId());
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int source);
    }

}
