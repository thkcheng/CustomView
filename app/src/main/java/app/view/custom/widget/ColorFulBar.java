package app.view.custom.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * 渐变荧光拖动条
 */
public class ColorFulBar extends View {

    private Paint mPaint;

    private int radius = 200; //内圆半径
    private int ringWidth = 30; //圆环宽度
    private float mCenterX; //中心坐标X轴
    private float mCenterY; //中心坐标Y轴

    private float startX; //起始x坐标
    private float startY; //起始y坐标

    private float endX; //结束x坐标
    private float endY; //结束y坐标

    private float progessX; //进度x坐标
    private float progessY; //进度y坐标

    private float progessBollRadius = 22f; //进度球半径

    private float maxLength = 100f; //进度条总长度
    private float averageLength; // 平均长度

    private float maxProgress = 100f; //默认最大进度
    private float currentProgress = 0f; //当前进度

    private float mDist; // 当前手指点击的坐标距离进度球圆心的距离

    private float MOVE_OFFSET_DEFAULT = 20f; //默认偏移量
    private float MOVE_OFFSET = MOVE_OFFSET_DEFAULT; //滑动时偏移量

    private boolean isCurrentProgress = false; //是否直接跳到某一个进度

    private boolean isDrag = false; //是否拖拽

    private boolean isFromUser = false; //是否用户拖拽的

    private int[] colors = {0xFFFF9731, 0xFFFFC339, 0xFFFFFA44};

    private OnColorFulBarChangeListener mOnColorFulBarChangeListener;

    public ColorFulBar(Context context) {
        this(context, null);
    }

    public ColorFulBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 描边效果
        mPaint.setStrokeWidth(ringWidth);    // 描边宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); //圆角
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        startX = 30f;
        startY = getHeight() / 2;

        endX = getWidth() - 30f;
        endY = startY;

        progessX = 30f;
        progessY = startY;

        maxLength = getWidth() - startX * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

//        LinearGradient gradient = new LinearGradient(mCenterX, 0, mCenterX, mCenterY * 2, colors, null, Shader.TileMode.MIRROR);
//        mPaint.setShader(gradient);
//        mPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID)); //设置发光
//        canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);

        // 画默认条
        mPaint.setShader(null);
        mPaint.setMaskFilter(null);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(7f);    // 描边宽度
        canvas.drawLine(startX, startY, endX, endY, mPaint);

        // 画渐变进度条
        mPaint.setStrokeWidth(10f);    // 描边宽度
        LinearGradient gradient2 = new LinearGradient(100, 100, getWidth() - 100, 100, colors, null, Shader.TileMode.MIRROR);
        mPaint.setShader(gradient2);
        mPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID)); //设置发光
        canvas.drawLine(startX, startY, progessX, progessY, mPaint);

        // 画白色进度球
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setShader(null);
        mPaint.setMaskFilter(null);
        canvas.drawCircle(progessX, progessY, progessBollRadius, mPaint);

        // 画橙色进度球
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(colors[0]);
        canvas.drawCircle(progessX, progessY, 7, mPaint);
    }

    /**
     * 设置最大进度 默认为100
     *
     * @param maxProgress 最大进度
     */
    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        checkAverageLength();
    }

    /**
     * 设置当前进度
     *
     * @param currentProgress 当前进度
     */
    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = currentProgress;

        if (currentProgress > this.maxProgress) {
            Toast.makeText(getContext(), "请重新设置当前进度", Toast.LENGTH_LONG).show();
            return;
        }

        checkAverageLength();

        checkCurrentProgress();

        isCurrentProgress = true;

        progessX = averageLength * currentProgress + startX;

        invalidate();

        setColorFulBarChangeListener(false);
    }


    /**
     * 开始播放
     */
    public void startProgress() {
        if (isHandler) {
            return;
        }
        checkAverageLength();

        mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS);
    }

    /**
     * 暂停播放
     */
    public void stopProgress() {
        isHandler = false;
        mHandler.removeMessages(MESSAGE_START_PROGRESS);
    }

    /**
     * 设置监听
     */
    public void setColorFulBarChangeListener(OnColorFulBarChangeListener changeListener) {
        this.mOnColorFulBarChangeListener = changeListener;
    }

    /**
     * 计算进度球移动的X坐标
     */
    private void getProgessX() {
        if (isCurrentProgress) {
            //开启过setCurrentProgress再开启hanlder时加过startX就不用再加了
            progessX = averageLength * currentProgress + startX / 2;
        } else if (isDrag) {
            progessX = averageLength * currentProgress;
        } else {
            progessX = averageLength * currentProgress + startX;
        }
        invalidate();
    }

    /**
     * 根据坐标x和平均长度反向计算出当前进度
     */
    private void getCurrentProgress() {
        checkAverageLength();
        progessX = progessX - startX;
        currentProgress = progessX / averageLength;
    }

    /**
     * 检查平均长度是否正常
     */
    private float checkAverageLength() {
        if (averageLength == 0f) {
            averageLength = maxLength / maxProgress; //不可为0
        }
        return averageLength;
    }

    /**
     * 检查当前进度是否正常
     */
    private float checkCurrentProgress() {
        if (currentProgress == 0f) {
            currentProgress = 1f; //不可为0
        }
        return currentProgress;
    }


    /**
     * @param isFromUser 如果进度更改是由用户发起的
     */
    private void setColorFulBarChangeListener(boolean isFromUser) {
        if (mOnColorFulBarChangeListener != null) {
            mOnColorFulBarChangeListener.onProgressChanged(this, currentProgress, isFromUser);
        }
    }

    /**
     * 重写该方法使进度跟随手指移动
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDist = (float) Math.hypot(event.getX() - progessX, event.getY() - progessY);
                if (mDist < (progessBollRadius + MOVE_OFFSET)) {
                    mHandler.removeMessages(MESSAGE_START_PROGRESS);
                    MOVE_OFFSET = getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算当前手指是否点击在进度球上
                mDist = (float) Math.hypot(event.getX() - progessX, event.getY() - progessY);
                // 如果点击的距离在合适的范围内并且没有超过边界值 则改变(滑动)
                if (mDist < (progessBollRadius + MOVE_OFFSET)) {
                    if (event.getX() >= startX && event.getX() <= endX) {
                        isDrag = true;
                        isFromUser = true;
                        progessX = event.getX() + startX;
                        getCurrentProgress();
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isFromUser = false;
                MOVE_OFFSET = MOVE_OFFSET_DEFAULT;
                if (isHandler) {
                    mHandler.sendEmptyMessageDelayed(MESSAGE_START_PROGRESS, 1000);
                }
                break;
        }
        return true;
    }


    public static final int MESSAGE_START_PROGRESS = 110;
    private boolean isHandler = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_START_PROGRESS:
                    isHandler = true;
                    if (currentProgress == 0f) {
                        currentProgress = currentProgress + 1f;
                    }
                    getProgessX();
                    currentProgress += 1;
                    if (currentProgress >= maxProgress) {
                        mHandler.removeMessages(MESSAGE_START_PROGRESS);
                        isHandler = false;
                    } else {
                        mHandler.sendEmptyMessageDelayed(MESSAGE_START_PROGRESS, 1000);
                    }
                    setColorFulBarChangeListener(isFromUser);
                    break;
            }
        }
    };


    public interface OnColorFulBarChangeListener {

        /**
         * 进度条改变
         *
         * @param colorFulBar 当前view
         * @param progress    当前进度
         * @param fromUser    如果进度更改是由用户发起的，则为True
         */
        void onProgressChanged(ColorFulBar colorFulBar, float progress, boolean fromUser);
    }
}
