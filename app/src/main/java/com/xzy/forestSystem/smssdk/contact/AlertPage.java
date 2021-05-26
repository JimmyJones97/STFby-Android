package com.xzy.forestSystem.smssdk.contact;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import  com.xzy.forestSystem.mob.tools.FakeActivity;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: cn.smssdk.contact.a */
public class AlertPage extends FakeActivity implements View.OnClickListener {

    /* renamed from: a */
    private static AlertPage f72a;

    /* renamed from: b */
    private static boolean f73b;

    /* renamed from: c */
    private ArrayList<Runnable> f74c = new ArrayList<>();

    /* renamed from: d */
    private ArrayList<Runnable> f75d = new ArrayList<>();

    /* renamed from: e */
    private TextView f76e;

    /* renamed from: f */
    private TextView f77f;

    /* renamed from: g */
    private HashMap<String, Object> f78g = new HashMap<>();

    public AlertPage() {
        f72a = this;
        this.f78g.put("okActions", this.f74c);
        this.f78g.put("cancelActions", this.f75d);
        setResult(this.f78g);
    }

    /* renamed from: a */
    public static void m562a() {
        f73b = true;
    }

    /* renamed from: b */
    public static boolean m559b() {
        return f72a != null;
    }

    /* renamed from: a */
    public static void m560a(Runnable runnable, Runnable runnable2) {
        f72a.f74c.add(runnable);
        f72a.f75d.add(runnable2);
    }

    @Override //  com.xzy.forestSystem.mob.tools.FakeActivity
    public void onCreate() {
        if (f73b) {
            int styleRes = ResHelper.getStyleRes(this.activity, "smssdk_DialogStyle");
            if (styleRes > 0) {
                this.activity.setTheme(styleRes);
            } else {
                this.activity.setTheme(16973835);
            }
        }
        this.activity.setContentView(m557c());
    }

    /* renamed from: c */
    private LinearLayout m557c() {
        LinearLayout linearLayout = new LinearLayout(this.activity);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(-1);
        TextView textView = new TextView(this.activity);
        textView.setBackgroundColor(-13617865);
        int dipToPx = ResHelper.dipToPx(this.activity, 26);
        textView.setPadding(dipToPx, 0, dipToPx, 0);
        textView.setTextColor(-3158065);
        textView.setTextSize(1, 20.0f);
        textView.setText(m555d());
        textView.setGravity(16);
        linearLayout.addView(textView, new LinearLayout.LayoutParams(-1, ResHelper.dipToPx(this.activity, 52)));
        View view = new View(this.activity);
        view.setBackgroundColor(-15066083);
        linearLayout.addView(view, new LinearLayout.LayoutParams(-1, ResHelper.dipToPx(this.activity, 2)));
        TextView textView2 = new TextView(this.activity);
        int dipToPx2 = ResHelper.dipToPx(this.activity, 15);
        textView2.setPadding(dipToPx2, dipToPx2, dipToPx2, dipToPx2);
        textView2.setTextColor(-6710887);
        textView2.setTextSize(1, 18.0f);
        textView2.setText(m554e());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.weight = 1.0f;
        linearLayout.addView(textView2, layoutParams);
        LinearLayout linearLayout2 = new LinearLayout(this.activity);
        int dipToPx3 = ResHelper.dipToPx(this.activity, 5);
        linearLayout2.setPadding(dipToPx3, dipToPx3, dipToPx3, dipToPx3);
        linearLayout.addView(linearLayout2, new LinearLayout.LayoutParams(-1, -2));
        this.f76e = new TextView(this.activity);
        this.f76e.setTextColor(-6102899);
        this.f76e.setTextSize(1, 20.0f);
        this.f76e.setText(m553f());
        this.f76e.setBackgroundDrawable(m552g());
        this.f76e.setGravity(17);
        int dipToPx4 = ResHelper.dipToPx(this.activity, 48);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, dipToPx4);
        layoutParams2.weight = 1.0f;
        linearLayout2.addView(this.f76e, layoutParams2);
        this.f76e.setOnClickListener(this);
        linearLayout2.addView(new View(this.activity), new LinearLayout.LayoutParams(dipToPx3, -1));
        this.f77f = new TextView(this.activity);
        this.f77f.setTextColor(-1);
        this.f77f.setTextSize(1, 20.0f);
        this.f77f.setText(m551h());
        this.f77f.setBackgroundDrawable(m550i());
        this.f77f.setGravity(17);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, dipToPx4);
        layoutParams3.weight = 1.0f;
        linearLayout2.addView(this.f77f, layoutParams3);
        this.f77f.setOnClickListener(this);
        return linearLayout;
    }

    /* renamed from: d */
    private String m555d() {
        if ("zh".equals(DeviceHelper.getInstance(this.activity).getOSLanguage())) {
            return String.valueOf(new char[]{35686, 21578});
        }
        return "Warning";
    }

    /* renamed from: e */
    private String m554e() {
        String str;
        String appName = DeviceHelper.getInstance(this.activity).getAppName();
        if ("zh".equals(DeviceHelper.getInstance(this.activity).getOSLanguage())) {
            str = "\"%s\"" + String.valueOf(new char[]{24819, 35775, 38382, 24744, 30340, 36890, 20449, 24405});
        } else {
            str = "\"%s\" would like to access your contacts.";
        }
        return String.format(str, appName);
    }

    /* renamed from: f */
    private String m553f() {
        if ("zh".equals(DeviceHelper.getInstance(this.activity).getOSLanguage())) {
            return String.valueOf(new char[]{21462, 28040});
        }
        return "Cancel";
    }

    /* renamed from: g */
    private Drawable m552g() {
        return new ShapeDrawable(new Shape() { // from class: cn.smssdk.contact.a.1
            @Override // android.graphics.drawable.shapes.Shape
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(-6102899);
                RectF rectF = new RectF(0.0f, 0.0f, getWidth(), getHeight());
                int dipToPx = ResHelper.dipToPx(AlertPage.this.activity, 4);
                canvas.drawRoundRect(rectF, (float) dipToPx, (float) dipToPx, paint);
                paint.setColor(-1);
                int dipToPx2 = ResHelper.dipToPx(AlertPage.this.activity, 2);
                canvas.drawRoundRect(new RectF((float) dipToPx2, (float) dipToPx2, getWidth() - ((float) dipToPx2), getHeight() - ((float) dipToPx2)), (float) dipToPx2, (float) dipToPx2, paint);
            }
        });
    }

    /* renamed from: h */
    private String m551h() {
        if ("zh".equals(DeviceHelper.getInstance(this.activity).getOSLanguage())) {
            return String.valueOf(new char[]{32487, 32493});
        }
        return "OK";
    }

    /* renamed from: i */
    private Drawable m550i() {
        return new ShapeDrawable(new Shape() { // from class: cn.smssdk.contact.a.2
            @Override // android.graphics.drawable.shapes.Shape
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(-6102899);
                RectF rectF = new RectF(0.0f, 0.0f, getWidth(), getHeight());
                int dipToPx = ResHelper.dipToPx(AlertPage.this.activity, 4);
                canvas.drawRoundRect(rectF, (float) dipToPx, (float) dipToPx, paint);
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.f77f)) {
            this.f78g.put("res", true);
        }
        finish();
    }

    @Override //  com.xzy.forestSystem.mob.tools.FakeActivity
    public void onDestroy() {
        f72a = null;
        super.onDestroy();
    }
}
