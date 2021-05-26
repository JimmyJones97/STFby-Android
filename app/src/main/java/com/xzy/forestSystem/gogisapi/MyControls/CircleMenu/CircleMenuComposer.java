package  com.xzy.forestSystem.gogisapi.MyControls.CircleMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimator;

import java.util.HashMap;
import java.util.Map;


@SuppressLint({"ViewConstructor"})
public class CircleMenuComposer extends RelativeLayout {
    static final int BUTTON_DEFAULT = 2130837802;
    static final int BUTTON_SELECTED = 2130837801;
    public static byte CENTERBOTTOM = 2;
    public static byte CENTERTOP = 6;
    public static byte LEFTBOTTOM = 3;
    public static byte LEFTCENTER = 4;
    public static byte LEFTTOP = 5;
    public static byte RIGHTBOTTOM = 1;
    public static byte RIGHTCENTER = 8;
    public static byte RIGHTTOP = 7;
    private boolean areButtonsShowing = false;
    HashMap<String, Integer> buttonIDs = new HashMap<>();
    private boolean clickItemCollapse = false;
    private ImageView cross;
    private int duretime = ChartViewportAnimator.FAST_ANIMATION_DURATION;
    private boolean hasInit = false;
    private LinearLayout[] llayouts;
    private HashMap<String, View> m_ExtraViewIDs = new HashMap<>();
    private Animations myani;
    private Context mycontext;
    private RelativeLayout rlButton;

    public CircleMenuComposer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mycontext = context;
    }

    public CircleMenuComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mycontext = context;
    }

    public CircleMenuComposer(Context context) {
        super(context);
        this.mycontext = context;
    }

    @SuppressLint("WrongConstant")
    public void init(int[] imgResId, String[] buttonTags, int showhideButtonId, int crossId, byte pCode, int radius, int durationMillis, int itemIDFirst, int itemBackgroundID) {
        this.duretime = durationMillis;
        int align1 = 12;
        int align2 = 14;
        if (pCode == RIGHTBOTTOM) {
            align1 = 11;
            align2 = 12;
        } else if (pCode == CENTERBOTTOM) {
            align1 = 14;
            align2 = 12;
        } else if (pCode == LEFTBOTTOM) {
            align1 = 9;
            align2 = 12;
        } else if (pCode == LEFTCENTER) {
            align1 = 9;
            align2 = 15;
        } else if (pCode == LEFTTOP) {
            align1 = 9;
            align2 = 10;
        } else if (pCode == CENTERTOP) {
            align1 = 14;
            align2 = 10;
        } else if (pCode == RIGHTTOP) {
            align1 = 11;
            align2 = 10;
        } else if (pCode == RIGHTCENTER) {
            align1 = 11;
            align2 = 15;
        }
        RelativeLayout.LayoutParams thislps = (RelativeLayout.LayoutParams) getLayoutParams();
        Bitmap mBottom = BitmapFactory.decodeResource(this.mycontext.getResources(), imgResId[0]);
        if (pCode == CENTERBOTTOM || pCode == CENTERTOP) {
            if (!(thislps.width == -1 || thislps.width == -2 || ((double) thislps.width) >= (((double) (mBottom.getWidth() + radius)) + (((double) radius) * 0.1d)) * 2.0d)) {
                thislps.width = (int) (((((double) radius) * 1.1d) + ((double) mBottom.getWidth())) * 2.0d);
            }
        } else if (!(thislps.width == -1 || thislps.width == -2 || ((double) thislps.width) >= ((double) (mBottom.getWidth() + radius)) + (((double) radius) * 0.1d))) {
            thislps.width = (int) ((((double) radius) * 1.1d) + ((double) mBottom.getWidth()));
        }
        if (pCode == LEFTCENTER || pCode == RIGHTCENTER) {
            if (!(thislps.height == -1 || thislps.height == -2 || ((double) thislps.height) >= (((double) (mBottom.getHeight() + radius)) + (((double) radius) * 0.1d)) * 2.0d)) {
                thislps.width = (int) (((((double) radius) * 1.1d) + ((double) mBottom.getHeight())) * 2.0d);
            }
        } else if (!(thislps.height == -1 || thislps.height == -2 || ((double) thislps.height) >= ((double) (mBottom.getHeight() + radius)) + (((double) radius) * 0.1d))) {
            thislps.height = (int) ((((double) radius) * 1.1d) + ((double) mBottom.getHeight()));
        }
        setLayoutParams(thislps);
        RelativeLayout rl1 = new RelativeLayout(this.mycontext);
        this.rlButton = new RelativeLayout(this.mycontext);
        this.llayouts = new LinearLayout[imgResId.length];
        for (int i = 0; i < imgResId.length; i++) {
            ImageView img = new ImageView(this.mycontext);
            img.setImageResource(imgResId[i]);
            img.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            if (itemBackgroundID != 0) {
                img.setBackgroundResource(itemBackgroundID);
            }
            this.llayouts[i] = new LinearLayout(this.mycontext);
            this.llayouts[i].setId(itemIDFirst + i);
            this.llayouts[i].addView(img);
            this.buttonIDs.put(buttonTags[i], Integer.valueOf(itemIDFirst + i));
            RelativeLayout.LayoutParams rlps = new RelativeLayout.LayoutParams(-2, -2);
            rlps.alignWithParent = true;
            rlps.addRule(align1, -1);
            rlps.addRule(align2, -1);
            this.llayouts[i].setLayoutParams(rlps);
            this.llayouts[i].setVisibility(4);
            rl1.addView(this.llayouts[i]);
        }
        RelativeLayout.LayoutParams rlps1 = new RelativeLayout.LayoutParams(-1, -1);
        rlps1.alignWithParent = true;
        rlps1.addRule(align1, -1);
        rlps1.addRule(align2, -1);
        rl1.setLayoutParams(rlps1);
        RelativeLayout.LayoutParams buttonlps = new RelativeLayout.LayoutParams(-2, -2);
        buttonlps.alignWithParent = true;
        buttonlps.addRule(align1, -1);
        buttonlps.addRule(align2, -1);
        this.rlButton.setLayoutParams(buttonlps);
        this.rlButton.setBackgroundResource(showhideButtonId);
        this.cross = new ImageView(this.mycontext);
        this.cross.setImageResource(crossId);
        if (itemBackgroundID != 0) {
            this.cross.setBackgroundResource(itemBackgroundID);
        }
        RelativeLayout.LayoutParams crosslps = new RelativeLayout.LayoutParams(-2, -2);
        crosslps.alignWithParent = true;
        crosslps.addRule(13, -1);
        this.cross.setLayoutParams(crosslps);
        this.rlButton.addView(this.cross);
        this.myani = new Animations(rl1, pCode, radius);
        this.rlButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.CircleMenu.CircleMenuComposer.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (CircleMenuComposer.this.areButtonsShowing) {
                    CircleMenuComposer.this.myani.startAnimationsOut(CircleMenuComposer.this.duretime);
                    CircleMenuComposer.this.cross.startAnimation(Animations.getRotateAnimation(-270.0f, 0.0f, CircleMenuComposer.this.duretime));
                } else {
                    CircleMenuComposer.this.myani.startAnimationsIn(CircleMenuComposer.this.duretime);
                    CircleMenuComposer.this.cross.startAnimation(Animations.getRotateAnimation(0.0f, -270.0f, CircleMenuComposer.this.duretime));
                }
                CircleMenuComposer.this.areButtonsShowing = !CircleMenuComposer.this.areButtonsShowing;
            }
        });
        this.cross.startAnimation(Animations.getRotateAnimation(0.0f, 360.0f, PieChartRotationAnimator.FAST_ANIMATION_DURATION));
        addView(rl1);
        addView(this.rlButton);
        this.hasInit = true;
    }

    public void collapse() {
        this.myani.startAnimationsOut(this.duretime);
        this.cross.startAnimation(Animations.getRotateAnimation(-270.0f, 0.0f, this.duretime));
        this.areButtonsShowing = false;
    }

    public void expand() {
        this.myani.startAnimationsIn(this.duretime);
        this.cross.startAnimation(Animations.getRotateAnimation(0.0f, -270.0f, this.duretime));
        this.areButtonsShowing = true;
    }

    public boolean isInit() {
        return this.hasInit;
    }

    public boolean isShow() {
        return this.areButtonsShowing;
    }

    public void setButtonsOnClickListener(final View.OnClickListener l) {
        if (this.llayouts != null) {
            for (int i = 0; i < this.llayouts.length; i++) {
                if (this.llayouts[i] != null) {
                    this.llayouts[i].setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.CircleMenu.CircleMenuComposer.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            if (CircleMenuComposer.this.clickItemCollapse) {
                                CircleMenuComposer.this.collapse();
                            }
                            l.onClick(view);
                        }
                    });
                }
            }
        }
    }

    public void SetClickItemCollapse(boolean value) {
        this.clickItemCollapse = value;
    }

    public void UpdateItemImage(int index, int imgResId) {
        View tmpView;
        if (index >= 0 && index < this.llayouts.length && (tmpView = this.llayouts[index].getChildAt(0)) != null && (tmpView instanceof ImageView)) {
            ((ImageView) tmpView).setImageResource(imgResId);
        }
    }

    public void SetItemTag(int index, Object tag) {
        View tmpView;
        if (index >= 0 && index < this.llayouts.length && (tmpView = this.llayouts[index]) != null) {
            tmpView.setTag(tag);
        }
    }

    public void SetButtonSelectedStatus(String button, boolean isSeleted) {
        View tmpView2;
        if (button == null) {
            for (Map.Entry<String, Integer> entry : this.buttonIDs.entrySet()) {
                View tempView = findViewById(entry.getValue().intValue());
                if (!(tempView == null || (tmpView2 = ((LinearLayout) tempView).getChildAt(0)) == null)) {
                    tmpView2.setBackgroundResource(R.drawable.mb_button_selector);
                    tmpView2.setSelected(false);
                }
            }
            for (Map.Entry<String, View> entry2 : this.m_ExtraViewIDs.entrySet()) {
                View tempView2 = entry2.getValue();
                if (tempView2 != null) {
                    tempView2.setBackgroundResource(R.drawable.mb_button_selector);
                    tempView2.setSelected(false);
                }
            }
        } else if (this.buttonIDs.containsKey(button)) {
            View tempView3 = findViewById(this.buttonIDs.get(button).intValue());
            if (tempView3 != null) {
                View tmpView22 = ((LinearLayout) tempView3).getChildAt(0);
                if (tmpView22 == null) {
                    return;
                }
                if (isSeleted) {
                    SetButtonSelectedStatus(null, false);
                    tmpView22.setSelected(true);
                    tmpView22.setBackgroundResource(R.drawable.mb_button_selected);
                    return;
                }
                tmpView22.setBackgroundResource(R.drawable.mb_button_selector);
                tmpView22.setSelected(false);
                return;
            }
            View tempView4 = this.m_ExtraViewIDs.get(button);
            if (tempView4 == null) {
                return;
            }
            if (isSeleted) {
                SetButtonSelectedStatus(null, false);
                tempView4.setSelected(true);
                tempView4.setBackgroundResource(R.drawable.mb_button_selected);
                return;
            }
            tempView4.setBackgroundResource(R.drawable.mb_button_selector);
            tempView4.setSelected(false);
        }
    }

    public void SetButtonsSelectedStatus(String exceptButton, boolean isSeleted) {
        View tempView;
        View tempView2;
        View tmpView2;
        for (Map.Entry<String, Integer> entry : this.buttonIDs.entrySet()) {
            if (!(exceptButton.equals(entry.getKey()) || (tempView2 = findViewById(entry.getValue().intValue())) == null || (tmpView2 = ((LinearLayout) tempView2).getChildAt(0)) == null)) {
                tmpView2.setBackgroundResource(R.drawable.mb_button_selector);
                tmpView2.setSelected(false);
            }
        }
        for (Map.Entry<String, View> entry2 : this.m_ExtraViewIDs.entrySet()) {
            if (!exceptButton.equals(entry2.getKey()) && (tempView = entry2.getValue()) != null) {
                tempView.setBackgroundResource(R.drawable.mb_button_selector);
                tempView.setSelected(false);
            }
        }
    }

    public void addButtonTag(String buttonName, int buttonID, View view) {
        this.buttonIDs.put(buttonName, Integer.valueOf(buttonID));
        this.m_ExtraViewIDs.put(buttonName, view);
    }
}
