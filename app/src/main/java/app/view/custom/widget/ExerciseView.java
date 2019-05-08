package app.view.custom.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ExerciseView extends View {

    private Paint mPaint;

    private float mCenterX; //中心坐标X轴
    private float mCenterY; //中心坐标Y轴

    private int radius = 300; //圆半径
    private int ringWidth = 30; //圆环宽度

    private float mBallX; // 小球中心坐标X轴
    private float mBallY; // 小球中心坐标Y轴
    private float mBallRadius = 20; // 小球半径
    private float mBallRingWidth = 15; // 小球半径

    private String mTitleValue = "运动消耗";
    private int kCalValue = 0;  // 最大值100
    private String mUnitValue = "Kcal";

    private Rect kCalValueRect = new Rect();

    private float sweepAngle = 0; //扫描度数

    private ValueAnimator mAnimator; //属性动画

    public ExerciseView(Context context) {
        this(context, null);
    }

    public ExerciseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mBallX = mCenterX;
        mBallY = mCenterY - radius;

        Log.e("xxx", "默认X = " + mBallX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 呃..先画个坐标系便于直观计算
        mPaint.setStrokeWidth(1);
        mPaint.setARGB(20, 0, 0, 0); // 设置颜色和透明度
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);

        drawInCircle(canvas);
        drawBall(canvas);
        drawKcal(canvas);
        drawLocus(canvas);
    }

    /**
     * 绘制轨迹圆
     */
    private void drawInCircle(Canvas canvas) {
        // 绘制半透明内圆   
        mPaint.setARGB(50, 54, 174, 255);
        mPaint.setStyle(Paint.Style.STROKE); // 描边效果
        mPaint.setStrokeWidth(ringWidth);    // 描边宽度
        canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
    }

    /**
     * 绘制小球
     */
    private void drawBall(Canvas canvas) {
        mPaint.setARGB(250, 54, 174, 255);
        mPaint.setStyle(Paint.Style.STROKE);   // 描边效果
        mPaint.setStrokeWidth(mBallRingWidth); // 描边宽度
        canvas.drawCircle(mBallX, mBallY, mBallRadius, mPaint);
    }

    /**
     * 绘制轨迹路线
     */
    private void drawLocus(Canvas canvas) {
        // 呃..先画个矩形系便于画出外围圆弧
        mPaint.setStrokeWidth(1);
        mPaint.setARGB(20, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        // top = (屏幕的宽 / 2） - 内圆半径 - 内圆的描边宽度
        canvas.drawRect(mCenterX - radius - ringWidth / 2, mCenterY - radius - ringWidth / 2, mCenterX + radius + ringWidth / 2, mCenterY + radius + ringWidth / 2, mPaint);

        // 设置内圆外弧形的矩形 (弧线的形成依赖于该矩形)
        RectF rectF = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
        // 绘制内圆外的弧形
        mPaint.setStrokeWidth(ringWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果
        mPaint.setARGB(250, 54, 174, 255);
        canvas.drawArc(rectF, 270, sweepAngle, false, mPaint);
    }


    /**
     * 绘制文字Kcal
     */
    private void drawKcal(Canvas canvas) {
        mPaint.setARGB(250, 54, 174, 255);
        mPaint.setStrokeWidth(1); // 描边宽度
        mPaint.setStyle(Paint.Style.FILL);   // 填充效果
//      mPaint.setFakeBoldText(true); //设置是否为粗体文字
        // 获取标题值的宽度
        mPaint.setTextSize(70);
        mPaint.getTextBounds(mTitleValue, 0, mTitleValue.length(), kCalValueRect);
        canvas.drawText(mTitleValue, mCenterX - kCalValueRect.width() / 2, mCenterY - radius / 3, mPaint);
        // 获取千卡值的宽度
        mPaint.setTextSize(120);
        mPaint.getTextBounds(String.valueOf(kCalValue), 0, String.valueOf(kCalValue).length(), kCalValueRect);
        canvas.drawText(String.valueOf(kCalValue), mCenterX - kCalValueRect.width() / 2, mCenterY + radius / 4, mPaint);
        // 获取单位值的宽度
        mPaint.setTextSize(50);
        mPaint.getTextBounds(mUnitValue, 0, mUnitValue.length(), kCalValueRect);
        canvas.drawText(mUnitValue, mCenterX - kCalValueRect.width() / 2, mCenterY + radius / 2, mPaint);
    }

    private void startAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
        mAnimator.setRepeatCount(1);
        mAnimator.setDuration(3000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if (sweepAngle == 0 || sweepAngle <= 180) {
                    sweepAngle++;
                    // 改变kcal的值  100 = 360°
                    float value = (float) (sweepAngle / 3.6);
                    kCalValue = (int) Math.ceil(value);
                    // 改变小球的位置 圆点坐标(X) + 半径radius * cos(起始angle270 * );
                    mBallX = (float) (Math.cos((sweepAngle + 270) * Math.PI / 180) * radius + mCenterX);
                    mBallY = (float) (Math.sin((sweepAngle + 270) * Math.PI / 180) * radius + mCenterY);
                    Log.e("xxx", "改变X = " + mBallX);
                    invalidate();
                }
            }
        });
        mAnimator.start();
    }

    public void start() {
        startAnimator();
    }
}
