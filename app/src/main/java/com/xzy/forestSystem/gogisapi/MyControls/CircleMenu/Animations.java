package  com.xzy.forestSystem.gogisapi.MyControls.CircleMenu;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class Animations {
    public static byte CENTERBOTTOM = 2;
    public static byte CENTERTOP = 6;
    public static byte LEFTBOTTOM = 3;
    public static byte LEFTCENTER = 4;
    public static byte LEFTTOP = 5;
    public static byte RIGHTBOTTOM = 1;
    public static byte RIGHTCENTER = 8;
    public static byte RIGHTTOP = 7;

    /* renamed from: R */
    public final int f501R;
    private final int amount;
    private ViewGroup clayout;
    private double fullangle = 180.0d;
    private boolean isOpen = false;

    /* renamed from: pc */
    private int f502pc;
    private List<ViewPropertyAnimator> viewAnimators = new ArrayList();
    private byte xOri = 1;
    private byte yOri = 1;

    public Animations(ViewGroup comlayout, int poscode, int radius) {
        this.f502pc = poscode;
        this.clayout = comlayout;
        this.amount = this.clayout.getChildCount();
        this.f501R = radius;
        for (int i = 0; i < this.amount; i++) {
            this.viewAnimators.add(this.clayout.getChildAt(i).animate());
        }
        if (poscode == RIGHTBOTTOM) {
            this.fullangle = 90.0d;
            this.xOri = -1;
            this.yOri = -1;
        } else if (poscode == CENTERBOTTOM) {
            this.fullangle = 180.0d;
            this.xOri = -1;
            this.yOri = -1;
        } else if (poscode == LEFTBOTTOM) {
            this.fullangle = 90.0d;
            this.xOri = 1;
            this.yOri = -1;
        } else if (poscode == LEFTCENTER) {
            this.fullangle = 180.0d;
            this.xOri = 1;
            this.yOri = -1;
        } else if (poscode == LEFTTOP) {
            this.fullangle = 90.0d;
            this.xOri = 1;
            this.yOri = 1;
        } else if (poscode == CENTERTOP) {
            this.fullangle = 180.0d;
            this.xOri = -1;
            this.yOri = 1;
        } else if (poscode == RIGHTTOP) {
            this.fullangle = 90.0d;
            this.xOri = -1;
            this.yOri = 1;
        } else if (poscode == RIGHTCENTER) {
            this.fullangle = 180.0d;
            this.xOri = -1;
            this.yOri = -1;
        }
    }

    /* access modifiers changed from: private */
    public class AnimListener implements Animator.AnimatorListener {
        private View target;

        public AnimListener(View _target) {
            this.target = _target;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animation) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            if (!Animations.this.isOpen) {
                this.target.setVisibility(4);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animation) {
        }
    }

    public void startAnimationsIn(int durationMillis) {
        double deltaY;
        double deltaX;
        this.isOpen = true;
        for (int i = 0; i < this.clayout.getChildCount(); i++) {
            LinearLayout inoutimagebutton = (LinearLayout) this.clayout.getChildAt(i);
            double offangle = this.fullangle / ((double) (this.amount - 1));
            if (this.f502pc == LEFTCENTER || this.f502pc == RIGHTCENTER) {
                deltaX = Math.sin(((((double) i) * offangle) * 3.141592653589793d) / 180.0d) * ((double) this.f501R);
                deltaY = Math.cos(((((double) i) * offangle) * 3.141592653589793d) / 180.0d) * ((double) this.f501R);
            } else {
                deltaY = Math.sin(((((double) i) * offangle) * 3.141592653589793d) / 180.0d) * ((double) this.f501R);
                deltaX = Math.cos(((((double) i) * offangle) * 3.141592653589793d) / 180.0d) * ((double) this.f501R);
            }
            ViewPropertyAnimator viewPropertyAnimator = this.viewAnimators.get(i);
            viewPropertyAnimator.setListener(null);
            inoutimagebutton.setVisibility(0);
            viewPropertyAnimator.x((float) (((double) inoutimagebutton.getLeft()) + (((double) this.xOri) * deltaX))).y((float) (((double) inoutimagebutton.getTop()) + (((double) this.yOri) * deltaY)));
        }
    }

    public void startAnimationsOut(int durationMillis) {
        this.isOpen = false;
        for (int i = 0; i < this.clayout.getChildCount(); i++) {
            LinearLayout inoutimagebutton = (LinearLayout) this.clayout.getChildAt(i);
            ViewPropertyAnimator viewPropertyAnimator = this.viewAnimators.get(i);
            viewPropertyAnimator.setListener(null);
            viewPropertyAnimator.x((float) inoutimagebutton.getLeft()).y((float) inoutimagebutton.getTop());
            viewPropertyAnimator.setListener(new AnimListener(inoutimagebutton));
        }
    }

    public int getPosCode() {
        return this.f502pc;
    }

    public static Animation getRotateAnimation(float fromDegrees, float toDegrees, int durationMillis) {
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, 1, 0.5f, 1, 0.5f);
        rotate.setDuration((long) durationMillis);
        rotate.setFillAfter(true);
        return rotate;
    }
}
