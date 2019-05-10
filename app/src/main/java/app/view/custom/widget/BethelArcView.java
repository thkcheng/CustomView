package app.view.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用贝塞尔画四分之一圆
 * <p>
 * 参考资料: https://www.jianshu.com/p/3b2ac05ae3c7
 * https://www.jianshu.com/p/649e01120d93
 * https://www.jb51.net/article/136037.htm
 */
public class BethelArcView extends View {

    private Paint mPaint;
    private float mCenterX; //中心坐标X轴
    private float mCenterY; //中心坐标Y轴
    private float radius = 200;

    public BethelArcView(Context context) {
        this(context, null);
    }

    public BethelArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        // 画个坐标系
        mPaint.setARGB(80, 0, 0, 0); // 设置颜色和透明度
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);

        // 画个矩形
        mPaint.setARGB(80, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        // top = (屏幕的宽 / 2） - 内圆半径 - 内圆的描边宽度
        canvas.drawRect(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius, mPaint);


        // 画个圆
        mPaint.setARGB(80, 0, 0, 0);
        canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);

        // 算出三个点
        float startX = mCenterX;
        float startY = mCenterY - radius;
        float endX = mCenterX + radius;
        float endY = mCenterY;
//        float gX = mCenterX + radius;
//        float gY = mCenterY - radius;

        float center1X = mCenterX + radius / 2;
        float center1Y = mCenterY - radius;
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center1X, center1Y, 8, mPaint);

        float center2X = mCenterX + radius;
        float center2Y = mCenterY - radius / 2;
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center2X, center2Y, 8, mPaint);


        Path path = new Path();
        path.moveTo(startX, startY);
        //二阶贝塞尔
//        path.quadTo(center1X, center1Y, endX, endY);
        //三阶贝塞尔
        path.cubicTo(center1X, center1Y, center2X, center2Y, endX, endY);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, mPaint);
    }
}
