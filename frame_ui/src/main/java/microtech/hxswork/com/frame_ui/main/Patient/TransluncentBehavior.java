package microtech.hxswork.com.frame_ui.main.Patient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_ui.R;


/**
 * Created by microtech on 2017/11/20.状态栏颜色的变化和逻辑判断
 */

@SuppressWarnings("unused")
public class TransluncentBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    //注意这个变量一定要定义成类变量
    private int mOffset = 0;
    //延长滑动过程
    private static final int MORE = 100;

    public TransluncentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Toolbar child, View dependency) {
        return dependency.getId() == R.id.patient_toolbar;
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout
            , @NonNull Toolbar child
            , @NonNull View directTargetChild
            , @NonNull View target
            , int axes
            , int type) {
        return true;
    }

    @Override
    public void onNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout
            , @NonNull Toolbar toolbar
            , @NonNull View target
            , int dxConsumed
            , int dyConsumed
            , int dxUnconsumed
            , int dyUnconsumed
            , int type) {
        final int startOffset = 0;
        final Context context = Frame.getApplicationContext();
        final int endOffset = context.getResources().getDimensionPixelOffset(R.dimen.header_height) + MORE;
        mOffset += dyConsumed;
        if (mOffset <= startOffset) {
            toolbar.getBackground().setAlpha(0);
        } else if (mOffset > startOffset && mOffset < endOffset) {
            final float percent = (float) (mOffset - startOffset) / endOffset;
            final int alpha = Math.round(percent * 255);
            toolbar.getBackground().setAlpha(alpha);
        } else if (mOffset >= endOffset) {
            toolbar.getBackground().setAlpha(255);
        }
    }
}
