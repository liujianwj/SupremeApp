package zs.com.supremeapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/8/13.
 */

public class WidgetFlowLayout extends ViewGroup {

    private boolean isCenter;
    private boolean isFill; // 利用多余padding填充到每一个item中, 用于两端对齐.
    private boolean isFillWithoutLastLine; // 最后一行是否填充
    private int maxLine = 0; // 限制最多行数

    public WidgetFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.flow);
        isCenter = a.getBoolean(R.styleable.flow_center,false);
        isFill = a.getBoolean(R.styleable.flow_fill, false);
        isFillWithoutLastLine = a.getBoolean(R.styleable.flow_fill_without_last_line, false);
        maxLine = a.getInteger(R.styleable.flow_max_line, 0);
        a.recycle();
    }

    public WidgetFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WidgetFlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();

        int lineIndex = 0;
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            ViewGroup.LayoutParams params = child.getLayoutParams();
            ViewGroup.MarginLayoutParams lp = null;
            if (params instanceof ViewGroup.MarginLayoutParams) {
                lp = (ViewGroup.MarginLayoutParams) params;
            } else {
                lp = new ViewGroup.MarginLayoutParams(params);
            }

            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            // 换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                // 只有在行数限制之内才计算高度
                if (maxLine <= 0 || (maxLine > 0 && lineIndex < maxLine)) {
                    height += lineHeight;
                    lineIndex++;
                    lineHeight = childHeight;
                }
            } else { // 未换行
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 最后一个控件
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                if (maxLine <= 0 || (maxLine > 0 && lineIndex < maxLine)) {
                    height += lineHeight;
                    lineIndex++;
                }
            }
        }

        setMeasuredDimension(
                //
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()//
        );

    }

    /**
     * 存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    /**
     * 记录每行剩余距离,用来设置每行布局居中效果(按项目需求可去除)
     */
    private List<Integer> mLinePadding = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        mLinePadding.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            ViewGroup.MarginLayoutParams lp = null;
            if (params instanceof ViewGroup.MarginLayoutParams) {
                lp = (ViewGroup.MarginLayoutParams) params;
            } else {
                lp = new ViewGroup.MarginLayoutParams(params);
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {

                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                if(isCenter || isFill)
                    mLinePadding.add(width - getPaddingLeft() - getPaddingRight()-lineWidth);

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);

        }// for end
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        if(isCenter || isFill)
            mLinePadding.add(width-getPaddingLeft() - getPaddingRight()-lineWidth);


        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllViews.size();
        if (maxLine > 0 && lineNum > maxLine) {
            lineNum = maxLine;
        }

        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            int mPadding = isCenter&&!isFill?mLinePadding.get(i)/2:0;
            if (lineViews == null || lineViews.size() == 0) continue;
            int fill = isFill?mLinePadding.get(i)/lineViews.size():0;//空余padding平均分配
            int mode = isFill?mLinePadding.get(i)%lineViews.size():0;//除不尽的padding
            if (isFillWithoutLastLine && isFill && i == lineNum - 1) {
                mPadding = isCenter?mLinePadding.get(i)/2:0;
                fill = 0;
                mode = 0;
            }

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                ViewGroup.LayoutParams params = child.getLayoutParams();
                ViewGroup.MarginLayoutParams lp = null;
                if (params instanceof ViewGroup.MarginLayoutParams) {
                    lp = (ViewGroup.MarginLayoutParams) params;
                } else {
                    lp = new ViewGroup.MarginLayoutParams(params);
                }


                int lc = left + lp.leftMargin + mPadding;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth() + fill + (j < mode ? 1 : 0);//除不尽时,对前面的child进行宽度+1分配
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);
                lp.width = rc - lc;
                lp.height = bc - tc;
                //updateViewLayout(child, lp);
                /*if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) child;
                    viewGroup.measure(lp.width, lp.height);
                }*/

                left += child.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin + fill + (j < mode ? 1 : 0);
            }
            left = getPaddingLeft();
            top += lineHeight;
        }

    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}


