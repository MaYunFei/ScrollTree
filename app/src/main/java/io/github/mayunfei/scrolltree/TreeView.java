package io.github.mayunfei.scrolltree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayunfei on 17-8-11.
 */

public class TreeView extends View {
    private final Bitmap treeBp;
    private final int treeHeight;
    private final int treeWidth;
    private final float treeLeft;
    private final Bitmap starBp;
    private final float centerX;
    private final int starHeight;
    private final int starWidth;
    private int screenWidth;
    private int screenHeight;
    private Paint paintR;
    private Paint paintG;
    private Paint paintB;
    private float [] ptsR,ptsG,ptsB;
    List<PointRec> pointRecs;

    private static final int MAX_DISTANCE_FOR_CLICK = 100;

    private Paint paint;
    private int mDownX;
    private int mDownY;
    private int mTempX;
    private int mTempY;
    OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TreeView(Context context) {
        this(context,null);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenHeight = DisPlayUtil.getScreenHeight(context);
        screenWidth = DisPlayUtil.getScreenWidth(context);
        paintR = new Paint();
        paintG = new Paint();
        paintB = new Paint();
        paint = new Paint();
        paint.setTextSize(45);

        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);

        paintR.setAntiAlias(true);// 设置没有锯齿
        paintG.setAntiAlias(true);// 设置没有锯齿
        paintB.setAntiAlias(true);// 设置没有锯齿
        // 设置画笔颜色为白色
        paintR.setColor(Color.RED);
        paintG.setColor(Color.GREEN);
        paintB.setColor(Color.BLUE);
        treeBp = BitmapFactory.decodeResource(getResources(), R.mipmap.tree);
        starBp = BitmapFactory.decodeResource(getResources(), R.mipmap.startbg_n);
        starHeight = starBp.getHeight();
        starWidth = starBp.getWidth();
        treeHeight = treeBp.getHeight();
        treeWidth = treeBp.getWidth();
        treeLeft = (screenWidth - treeWidth) / 2f;
        centerX = (treeWidth + treeLeft)/2;
        ptsR = new float[4];
        ptsG = new float[4];
        ptsB = new float[4];
        ptsR[0] = centerX;
        ptsR[1] = 0;
        ptsR[2] = centerX;
        ptsR[3] = treeHeight;
        ptsG[0] = centerX;
        ptsG[1] = treeHeight;
        ptsG[2] = centerX;
        ptsG[3] = treeHeight*2;
        ptsB[0] = centerX;
        ptsB[1] = treeHeight*2;
        ptsB[2] = centerX;
        ptsB[3] = treeHeight*3;

        L.d("screenHeight = " + screenHeight);
        L.d("treeHeight = " + treeHeight);
        pointRecs = new ArrayList<>();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(screenWidth,treeHeight*10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 10; i++) {
            canvas.drawBitmap(treeBp,treeLeft,treeHeight*(9-i),paintR);
            onDrawStar(canvas,i);
        }

//        canvas.drawBitmap(treeBp,threeLeft,0f,paintR);
//        canvas.drawBitmap(treeBp,threeLeft,threeHeight,paintR);
//        canvas.drawBitmap(treeBp,threeLeft,threeHeight*2,paintR);
//        canvas.drawLines(ptsR,paintR);
//        canvas.drawLines(ptsG,paintG);
//        canvas.drawLines(ptsB,paintB);
    }

    private void onDrawStar(Canvas canvas,int index){
        float Height1of2 = treeHeight / 2f; //半颗树高
        float Height1of4 = treeHeight / 4f; //半颗树高
        float Width1of2 = treeWidth/2f;

        float centerY = Height1of2 + treeHeight * (9-index) ;

        float topY = centerY - Height1of4;
        float boottomY = centerY + Height1of4;
        float starcenterx = centerX -Width1of2 * 0.2f;

        float LeftX =  starcenterx - Width1of2 * 0.2f;
        float RithtX = starcenterx + Width1of2 * 0.2f;

        int count = index * 5;
        float star1of2width = starWidth / 4f;
        float star1of2height = starHeight / 1.5f;
        canvas.drawBitmap(starBp,starcenterx,centerY,paint);
        canvas.drawText(count+3+"",starcenterx+star1of2width,centerY+star1of2height,paint);
        Rect rect3 = new Rect(Float.valueOf(starcenterx).intValue()
                ,Float.valueOf(centerY).intValue()
                ,Float.valueOf(starcenterx + starWidth).intValue(),Float.valueOf(centerY+starHeight).intValue());
        pointRecs.add(new PointRec(rect3,count+3));


        canvas.drawBitmap(starBp,LeftX,topY,paint);
        canvas.drawText(count+4+"",LeftX+star1of2width,topY+star1of2height,paint);

        Rect rect4 = new Rect(Float.valueOf(LeftX).intValue()
                ,Float.valueOf(topY).intValue()
                ,Float.valueOf(LeftX + starWidth).intValue(),Float.valueOf(topY+starHeight).intValue());
        pointRecs.add(new PointRec(rect4,count+4));

        canvas.drawBitmap(starBp,RithtX,topY,paint);
        canvas.drawText(count+5+"",RithtX+star1of2width,topY+star1of2height,paint);

        Rect rect5 = new Rect(Float.valueOf(RithtX).intValue()
                ,Float.valueOf(topY).intValue()
                ,Float.valueOf(RithtX + starWidth).intValue(),Float.valueOf(topY+starHeight).intValue());
        pointRecs.add(new PointRec(rect5,count+5));

        canvas.drawBitmap(starBp,LeftX,boottomY,paint);
        canvas.drawText(count+1+"",LeftX+star1of2width,boottomY+star1of2height,paint);

        Rect rect1 = new Rect(Float.valueOf(LeftX).intValue()
                ,Float.valueOf(boottomY).intValue()
                ,Float.valueOf(LeftX + starWidth).intValue(),Float.valueOf(boottomY+starHeight).intValue());
        pointRecs.add(new PointRec(rect1,count+1));

        canvas.drawBitmap(starBp,RithtX,boottomY,paint);
        canvas.drawText(count+2+"",RithtX+star1of2width,boottomY+star1of2height,paint);

        Rect rect2 = new Rect(Float.valueOf(RithtX).intValue()
                ,Float.valueOf(boottomY).intValue()
                ,Float.valueOf(RithtX + starWidth).intValue(),Float.valueOf(boottomY+starHeight).intValue());
        pointRecs.add(new PointRec(rect2,count+2));

    }

    private static class PointRec{
        Rect rect;
        int index;

        public PointRec(Rect rect, int index) {
            this.rect = rect;
            this.index = index;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 获取当前触控位置
        // LogUtils.d("触摸事件 event.getAction() = " + event.getAction());
        int x = (int) event.getX();
        int y = (int) event.getY();

        boolean isDown;
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:// 当用户是按下
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:// 移动
                break;
            case MotionEvent.ACTION_UP:
                mTempX = (int) event.getX();
                mTempY = (int) event.getY();
                if (onItemClickListener!=null){
                    if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                            || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                        // 抬起的距离 和按下的距离太远 不形成点击事件
                        // LogUtils.d("起的距离 和按下的距离太远 不形成点击事件");
                    } else {
                        isOnclick(mTempX, mTempY);
                    }
                }

                break;
        }

        return true;
    }

    private void isOnclick(int mTempX, int mTempY) {
        for (PointRec pointRec:pointRecs){
            if (pointRec.rect.contains(mTempX,mTempY)){
                onItemClickListener.onItemClick(pointRec.index);
                break;
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
