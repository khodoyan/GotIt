package pro.khodoian.gotit.view;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Class with two static methods to animate Expand/Collapse transformation of linear layout
 *
 * Source: http://stackoverflow.com/questions/4946295/android-expand-collapse-animation
 * Created by http://stackoverflow.com/users/508194/tom-esterez
 */
public class LinearLayoutExpandCollapseAnimation {
    public static final int DEFAULT_DURATION = 300;

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        // a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(DEFAULT_DURATION);
        v.startAnimation(a);
    }

    public static void collapse(final View view) {
        collapseAndExecuteRunnable(view, null);
    }

    public static void collapseAndExecuteRunnable (final View view, final Runnable runnable) {
        final int initialHeight = view.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    view.setVisibility(View.GONE);
                    if (runnable != null)
                        executeRunnable(runnable);
                }else{
                    view.getLayoutParams().height =
                            initialHeight - (int)(initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        //a.setDuration((int)(initialHeight / view.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(DEFAULT_DURATION);
        view.startAnimation(a);
    }

    public static void executeRunnable(Runnable runnable) {
        new Handler().post(runnable);
    }
}
