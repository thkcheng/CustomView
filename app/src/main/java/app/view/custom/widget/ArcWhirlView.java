package app.view.custom.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class ArcWhirlView extends View {

    private Paint mPaint;

    private float mCenterX; //中心坐标X轴
    private float mCenterY; //中心坐标Y轴

    private int radius = 200; //内圆半径
    private int ringWidth = 10 * 3; //圆环宽度

    float overlapAngle = 0;    //和内圆重叠弧形的起始角度
    float peripheryAngle = 95; //外围弧形的起始角度

    private int mDist = 12; // 圆弧和内圆的距离

    private ValueAnimator mAnimator; //属性动画

    public ArcWhirlView(Context context) {
        this(context, null);
    }

    public ArcWhirlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 呃..先画个坐标系便于直观计算
        mPaint.setStrokeWidth(1);
        mPaint.setARGB(20, 0, 0, 0); // 设置颜色和透明度
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        // 抗锯齿第二种方案
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG));

        drawCircle(canvas);

        drawOverlapArc(canvas);

        // 呃..再画个矩形系便于画出外围圆弧
        mPaint.setStrokeWidth(1);
        mPaint.setARGB(10, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        // top = (屏幕的宽 / 2） - 内圆半径 - 内院的描边宽度 - 圆弧和内圆的距离
        canvas.drawRect(mCenterX - radius - ringWidth - mDist, mCenterY - radius - ringWidth - mDist, mCenterX + radius + ringWidth + mDist, mCenterY + radius + ringWidth + mDist, mPaint);

        drawPeripheryArc(canvas);

        openAnimator();
    }

    /**
     * 绘制内圆
     */
    private void drawCircle(Canvas canvas) {
        // 绘制半透明内圆   
        mPaint.setARGB(50, 54, 174, 255);
        mPaint.setStyle(Paint.Style.STROKE); // 描边效果
        mPaint.setStrokeWidth(ringWidth);    // 描边宽度
        canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
    }

    /**
     * 绘制重叠的圆弧
     */
    private void drawOverlapArc(Canvas canvas) {
        // 设置重叠弧形的矩形 (弧线的形成依赖于该矩形)
        RectF rectF = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
        // 绘制和内圆重叠的弧形
        mPaint.setARGB(250, 54, 174, 255);
        mPaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果
        canvas.drawArc(rectF, overlapAngle, 90, false, mPaint);
        canvas.drawArc(rectF, overlapAngle + 180, 90, false, mPaint);
    }

    /**
     * 绘制外围的圆弧
     */
    private void drawPeripheryArc(Canvas canvas) {
        // 设置内圆外弧形的矩形 (弧线的形成依赖于该矩形)
        RectF rectF = new RectF(mCenterX - radius - ringWidth - mDist, mCenterY - radius - ringWidth - mDist, mCenterX + radius + ringWidth + mDist, mCenterY + radius + ringWidth + mDist);
        // 绘制内圆外的弧形
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果
        mPaint.setARGB(250, 54, 174, 255);
        //从95°开始-扫描80° (给每个弧线中间留出 10°的空隙)
        canvas.drawArc(rectF, peripheryAngle, 80, false, mPaint);
        //185°开始-扫描80°
        canvas.drawArc(rectF, peripheryAngle + 90, 80, false, mPaint);
        //275°开始-扫描80°
        canvas.drawArc(rectF, peripheryAngle + 180, 80, false, mPaint);
        //275° + 90°开始-扫描80°
        canvas.drawArc(rectF, peripheryAngle + 270, 80, false, mPaint);
    }

    /**
     * 开启旋转属性动画
     */
    private void openAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2 / 4));
        mAnimator.setRepeatCount(1);
        mAnimator.setDuration(100);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                overlapAngle += 0.1f;
                peripheryAngle -= 0.1f;
                invalidate();
            }
        });
        mAnimator.start();
    }
}
