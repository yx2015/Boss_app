package com.xyl.boss_app.widgets;

/**
 * Created by ahuhxl on 2015/12/17.
 *
 * @Description:动画显示数字
 */

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class RiseNumberView extends TextView {
    int duration = 500;
    float number;
    private final DecimalFormat mFormat;

    public RiseNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    public void showNumberWithAnimation(final double number) {
        BigDecimal b = new BigDecimal(Double.toString(number));
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number", 0, b.floatValue());
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                RiseNumberView.this.setText(mFormat.format(number));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
        setText(mFormat.format((double) number));
    }
}