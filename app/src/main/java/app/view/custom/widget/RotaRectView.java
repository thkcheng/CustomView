package app.view.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class RotaRectView extends View {

    private Paint mPaint;
    private float mCenterX; //中心坐标X轴
    private float mCenterY; //中心坐标Y轴

    private float mLeft = 0f;
    private float mTop = 0f;
    private float mRight = 0f;
    private float mBottom = 0f;

    private float nLeft = 0f;
    private float nTop = 0f;
    private float nRight = 0f;
    private float nBottom = 0f;

    private float nLength;

    private float radius = 200;

    private float spacing = 20;


    private int[] mRectColors;

    private boolean isHandler = false;

    public RotaRectView(Context context) {
        super(context);
    }

    public RotaRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿

        mRectColors = new int[]{Color.RED, Color.BLUE, Color.BLUE, Color.BLUE, Color.WHITE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};
//        mRectColors = new int[]{Color.RED, Color.BLUE, Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE};

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mLeft = mCenterX - radius;
        mTop = mCenterY - radius;
        mRight = mCenterX + radius;
        mBottom = mCenterY + radius;

        nLength = ((radius * 2) - spacing * 2) / 3;

        // 画个坐标系
        mPaint.setARGB(80, 0, 0, 0); // 设置颜色和透明度
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);

        // 画个矩形
        mPaint.setARGB(80, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);

        mPaint.setStyle(Paint.Style.FILL);

        nLeft = mLeft;
        nTop = mTop;
        nRight = mRight - nLength * 2 - spacing * 2;
        nBottom = mBottom - nLength * 2 - spacing * 2;
        canvas.drawRect(nLeft, nTop, nRight, nBottom, mPaint);


        for (int i = 0; i < mRectColors.length; i++) {
            mPaint.setColor(mRectColors[i]);

            switch (i) {
                case 0:
                    nLeft = mLeft;
                    nTop = mTop;
                    nRight = mRight - nLength * 2 - spacing * 2;
                    nBottom = mBottom - nLength * 2 - spacing * 2;
                    break;
                case 1:
                    nLeft = mLeft + nLength + spacing;
                    nTop = mTop;
                    nRight = mRight - nLength - spacing;
                    nBottom = mBottom - nLength * 2 - spacing * 2;
                    break;
                case 2:
                    nLeft = mLeft + nLength * 2 + spacing * 2;
                    nTop = mTop;
                    nRight = mRight;
                    nBottom = mBottom - nLength * 2 - spacing * 2;
                    break;
                case 3:
                    nLeft = mLeft;
                    nTop = mTop + nLength + spacing;
                    nRight = mRight - nLength * 2 - spacing * 2;
                    nBottom = mBottom - nLength - spacing;
                    break;
                case 4:
                    nLeft = mLeft + nLength + spacing;
                    nTop = mTop + nLength + spacing;
                    nRight = mRight - nLength - spacing;
                    nBottom = mBottom - nLength - spacing;
                    break;
                case 5:
                    nLeft = mLeft + nLength * 2 + spacing * 2;
                    nTop = mTop + nLength + spacing;
                    nRight = mRight;
                    nBottom = mBottom - nLength - spacing;
                    break;
                case 6:
                    nLeft = mLeft;
                    nTop = mTop + nLength * 2 + spacing * 2;
                    nRight = mRight - nLength * 2 - spacing * 2;
                    nBottom = mBottom;
                    break;
                case 7:
                    nLeft = mLeft + nLength + spacing;
                    nTop = mTop + nLength * 2 + spacing * 2;
                    nRight = mRight - nLength - spacing;
                    nBottom = mBottom;
                    break;
                case 8:
                    nLeft = mLeft + nLength * 2 + spacing * 2;
                    nTop = mTop + nLength * 2 + spacing * 2;
                    nRight = mRight;
                    nBottom = mBottom;
                    break;
            }

            canvas.drawRect(nLeft, nTop, nRight, nBottom, mPaint);
        }

        if (!isHandler) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_START_CHANGE, 1000);
        }
    }

    private int startIndex = 0;

    public static final int MESSAGE_START_CHANGE = 110;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_START_CHANGE:
                    isHandler = true;
                    mRectColors[startIndex] = Color.RED;
                    for (int i = 0; i < mRectColors.length; i++) {
                        if (i != startIndex && i != 4) {
                            mRectColors[i] = Color.BLUE;
                        }
                    }
                    if (startIndex == 0) {
                        startIndex = 1;
                    } else if (startIndex == 1) {
                        startIndex = 2;
                    } else if (startIndex == 2) {
                        startIndex = 5;
                    } else if (startIndex == 5) {
                        startIndex = 8;
                    } else if (startIndex == 8) {
                        startIndex = 7;
                    } else if (startIndex == 7) {
                        startIndex = 6;
                    } else if (startIndex == 6) {
                        startIndex = 3;
                    } else if (startIndex == 3) {
                        startIndex = 0;
                    }
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(MESSAGE_START_CHANGE, 300);
                    break;
            }
        }
    };
}
