package com.wangjie.wheelview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 7/1/14.
 */
public class WheelView extends ScrollView {

    public static final String TAG = WheelView.class.getSimpleName();
    public static final int OFF_SET_DEFAULT = 1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;
    //    private ScrollView scrollView;
    //    String[] items;
    List<String> items;
    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    int displayItemCount; // 每页显示的数量
    int selectedIndex = 1;
    int initialY;
    Runnable scrollerTask;
    int newCheck = 50;
    int itemHeight = 0;
    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;
    Paint paint;
    int viewWidth;
    private int drawable;
    private Context context;
    private LinearLayout views;
    private int scrollDirection = -1;
    private OnWheelViewListener onWheelViewListener;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private List<String> getItems() {
        return items;
    }

    public void setItems(List<String> list) {
//        drawable = drawableIcon;
        if (null == items) {
            items = new ArrayList<String>();
        }
        items.clear();
        items.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.add(0, "");
            items.add("");
        }

        initData();

    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;

//        scrollView = ((ScrollView)this.getParent());
//        Log.d(TAG, "scrollview: " + scrollView);
        Log.d(TAG, "parent: " + this.getParent());
//        this.setOrientation(VERTICAL);
        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {

                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
//                    Log.d(TAG, "initialY: " + initialY);
//                    Log.d(TAG, "remainder: " + remainder + ", divided: " + divided);
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSeletedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSeletedCallBack();
                                }
                            });
                        }


                    }


                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };


    }

    public void startScrollerTask() {

        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;

        for (String item : items) {
            views.addView(createView(item));
        }

        refreshItemView(0);
    }

    private TextView createView(String item) {

        Drawable editTextDrawable;

        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        tv.setText(item);

        tv.setCompoundDrawablePadding(25);

        if(tv.getText().equals("Água")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_water_bill, 0, 0, 0);
        }

        if(tv.getText().equals("Cartão")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_credit_card2, 0, 0, 0);
        }

        if(tv.getText().equals("Casa")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_casa, 0, 0, 0);
        }

        if(tv.getText().equals("Combustível")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_fuel, 0, 0, 0);
        }

        if(tv.getText().equals("Contas")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_bill, 0, 0, 0);
        }

        if(tv.getText().equals("Cosméticos")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_cosmetics, 0, 0, 0);
        }

        if(tv.getText().equals("Dependentes")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_dependent, 0, 0, 0);
        }

        if(tv.getText().equals("Diversão")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_leisure, 0, 0, 0);
        }

        if(tv.getText().equals("Educação")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_education, 0, 0, 0);
        }

        if(tv.getText().equals("Eletrônicos")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_eletronicos, 0, 0, 0);
        }

        if(tv.getText().equals("Empréstimo")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_loan, 0, 0, 0);
        }

        if(tv.getText().equals("Energia")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_energy, 0, 0, 0);
        }

        if(tv.getText().equals("Esportes")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_sports, 0, 0, 0);
        }

        if(tv.getText().equals("Mercado")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_supermercado, 0, 0, 0);
        }

        if(tv.getText().equals("Pets")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_pets, 0, 0, 0);
        }

        if(tv.getText().equals("Poupança")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_money_saving, 0, 0, 0);
        }

        if(tv.getText().equals("Presente")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_gift, 0, 0, 0);
        }

        if(tv.getText().equals("Salário")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_salary, 0, 0, 0);
        }

        if(tv.getText().equals("Saúde")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_health, 0, 0, 0);
        }

        if(tv.getText().equals("Serviços")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_servicos, 0, 0, 0);
        }

        if(tv.getText().equals("Transporte")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_transport, 0, 0, 0);
        }

        if(tv.getText().equals("Vestuário")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_vestuario, 0, 0, 0);
        }

        if(tv.getText().equals("Viagem")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_travel, 0, 0, 0);
        }

        if(tv.getText().equals("Outros")){
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_cat_outros, 0, 0, 0);
        }


        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(5);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            Log.d(TAG, "itemHeight: " + itemHeight);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Log.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

//        try {
//            Field field = ScrollView.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            OverScroller mScroller = (OverScroller) field.get(this);
//
//
//            if(mScroller.isFinished()){
//                Log.d(TAG, "isFinished...");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        refreshItemView(t);

        if (t > oldt) {
//            Log.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
//            Log.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP;

        }


    }

    public void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }

//            if(remainder > itemHeight / 2){
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, ">down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, ">up...position: " + position);
//                }
//            }else{
////                position = y / itemHeight + offset;
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, "<down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, "<up...position: " + position);
//                }
//            }
//        }

//        if(scrollDirection == SCROLL_DIRECTION_DOWN){
//            position = divided + offset;
//        }else if(scrollDirection == SCROLL_DIRECTION_UP){
//            position = divided + offset + 1;
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                //azul
//                itemView.setTextColor(Color.parseColor("#0288ce"));
                itemView.setTextColor(Color.parseColor("#C8444444"));
                Drawable[] drawable = itemView.getCompoundDrawables();
                if (drawable[0]!=null) {
                    drawable[0].setAlpha(255);
                }
//                highlightIcons(itemView);

            } else {
                //cinza
                itemView.setTextColor(Color.parseColor("#64444444"));
                Drawable[] drawable = itemView.getCompoundDrawables();
                if (drawable[0]!=null) {
                    drawable[0].setAlpha(80);
                }
//                fadeIcons(itemView);
            }

        }
    }

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
//            paint.setColor(Color.parseColor("#83cde6"));
            paint.setColor(Color.parseColor("#80b53a"));
            paint.setStrokeWidth(dip2px(1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth * 1 / 11, obtainSelectedAreaBorder()[0], viewWidth * 5 / Float.valueOf("5.5"), obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth * 1 / 11, obtainSelectedAreaBorder()[1], viewWidth * 5 / Float.valueOf("5.5"), obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        };

        super.setBackgroundDrawable(background);

    }

    public void highlightIcons(TextView itemView) {
        if (itemView.getText().equals("Técnico")) {
            itemView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.new_technical_sm, 0, 0, 0);
        }

        if (itemView.getText().equals("Superior")) {
            itemView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.new_graduation_sm, 0, 0, 0);
//            tv.setCompoundDrawablePadding(15);
        }
    }

    public void fadeIcons(TextView itemView) {
        if (itemView.getText().equals("Técnico")) {
            itemView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.new_technical_sm_fade, 0, 0, 0);
        }

        if (itemView.getText().equals("Superior")) {
            itemView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.new_graduation_sm_fade, 0, 0, 0);
//            tv.setCompoundDrawablePadding(15);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    public String getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

    public static class OnWheelViewListener {
        public void onSelected(int selectedIndex, String item) {
        }
    }

}
