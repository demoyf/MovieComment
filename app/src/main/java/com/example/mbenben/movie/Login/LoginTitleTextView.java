package com.example.mbenben.movie.Login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.mbenben.movie.R;

/**
 * Created by alone on 2016/10/12.
 * 这个类用于自定义View，实现ViewPager切换的过程中，字体也随之变换
 */
public class LoginTitleTextView extends View implements LoginTextAttrbute {
    //    绘画字体时候的起始xy
    private int startX,startY;
    //    绘制的比例（即ViewPager已经移动的百分比）
    private float progress = 0;
    // 正常的颜色，还有被选中时候的颜色
    private int normalColor, changeColor;
    //    字体的宽高
    private int textWidth, textHeight;
    //    字体的大小
    private int textSize;
    //    要绘制的字体
    private String text;
    //      方向
    private int direction;
    //    画笔，因为需要通过它来获取到字体的宽高，所以在此定义
    private Paint mPaint;

    public LoginTitleTextView(Context context) {
        super(context);
    }

    public LoginTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public LoginTitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /*
    * @param context，attrs 用于获取定义的属性值。
    * 初始化一些内容
    * */
    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoginTitleTextView);
        text = ta.getString(R.styleable.LoginTitleTextView_text);
        changeColor = Color.BLUE;
        normalColor = Color.BLACK;
        textSize = (int) ta.getDimension(R.styleable.LoginTitleTextView_text_size, 30);
        progress = ta.getFloat(R.styleable.LoginTitleTextView_process, 0);
        direction = ta.getInt(R.styleable.LoginTitleTextView_direction, DIR_LEFT);
//        设置画笔的属性
        mPaint = new Paint();
        mPaint.setColor(normalColor);
        mPaint.setTextSize(textSize);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
//        注意要释放资源，否则会出大事
        ta.recycle();
//        测量字体的大小
        measureText();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        获取FontMetricsInt，通过它来获取文本的高
        Paint.FontMetricsInt metrics = mPaint.getFontMetricsInt();
        textHeight = metrics.bottom - metrics.top;
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
//        绘制字体开始的xy
        startX = (getMeasuredWidth()-textWidth)/2;
        startY = getMeasuredHeight()/2-metrics.descent+
                (metrics.bottom-metrics.top)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        通过判断当前的方向，分别绘制
        if (direction == DIR_LEFT) {
            drawLeftNormal(canvas);
            drawLeftChange(canvas);
        } else if (direction == DIR_RIGHT) {
            drawRightNormal(canvas);
            drawRightChange(canvas);
        }
    }
    /*
    * 绘制方向往右的text的变色部分
    * */
    private void drawRightChange(Canvas canvas) {
        drawText(canvas,changeColor,(int) (startX + textWidth * progress),startX+textWidth);
//        drawText(canvas,changeColor, (int) (startX+textWidth*(1-progress)),
//                startX+textWidth);
    }

    /*
    * 绘制方向往右的text的正常部分
    * */
    private void drawRightNormal(Canvas canvas) {

        drawText(canvas, normalColor, startX, (int) (startX + textWidth * progress));
//        drawText(canvas, normalColor, startX, (int) (startX + textWidth * (1 - progress)));
    }

    /*
    * 绘制方向往左的text的变色部分
    * */
    private void drawLeftChange(Canvas canvas) {
        drawText(canvas, changeColor, startX, (int) (startX + textWidth * progress));
    }
    /*
    * 绘制方向往左的text的正常部分
    * */
    private void drawLeftNormal(Canvas canvas) {
        drawText(canvas,normalColor,(int)
                (startX+textWidth*progress),startX+textWidth);
    }

    private void drawText(Canvas canvas, int color, int drawX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(drawX,0,endX,getMeasuredHeight());
        canvas.drawText(text,startX,startY,mPaint);
        canvas.restore();
    }

    //    测量字体宽度
    private void measureText() {
        if (!TextUtils.isEmpty(text)) {
            textWidth = (int) mPaint.measureText(text);
        }
    }

    /*
    *自定义的时候测量View的实际宽
    * */
    private int measureWidth(int widthSpec){
        int widthSize = MeasureSpec.getSize(widthSpec);
        int widthMode = MeasureSpec.getMode(widthSpec);
        int result = 0;
        switch (widthMode){
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(widthSize, textWidth);
                break;
        }
        return result+getPaddingLeft()+getPaddingRight();
    }
    /*
    * 测量View的高
    * @param heightSpec，高度的MeasureSpec
    * */
    private int measureHeight(int heightSpec){
        int heightSize = MeasureSpec.getSize(heightSpec);
        int heightMode = MeasureSpec.getMode(heightSpec);
        int result = 0;
        switch (heightMode){
//            精确的，返回值就是测试结果
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
//            不确定的，取测量结果的高和字体实际的高
            case MeasureSpec.AT_MOST:
                result = Math.min(heightSize, textHeight);
                break;
        }
        return result+getPaddingBottom()+getPaddingTop();
    }
    /*
    *下面几个方法分别是方向和进度的set
    *
    * */
    public void setDirection(int direction) {
        this.direction = direction;
//        设置的时候，还需要用到重绘的方法,相当于刷新视图
        invalidate();
    }


    public void setProcess(float progress) {
        this.progress = progress;
//        此处也需要重绘
        invalidate();
    }
}
