package com.xzy.forestSystem.mob.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.ReflectHelper;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import  com.xzy.forestSystem.mob.tools.utils.UIHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class FakeActivity {
    private static Class<? extends Activity> shellClass;
    protected Activity activity;
    private View contentView;
    private HashMap<String, Object> result;
    private FakeActivity resultReceiver;

    public static void setShell(Class<? extends Activity> shellClass2) {
        shellClass = shellClass2;
    }

    public static void registerExecutor(String scheme, Object executor) {
        if (shellClass != null) {
            try {
                Method registerExecutor = shellClass.getMethod("registerExecutor", String.class, Object.class);
                registerExecutor.setAccessible(true);
                registerExecutor.invoke(null, scheme, executor);
            } catch (Throwable t) {
                MobLog.getInstance().m57w(t);
            }
        } else {
            MobUIShell.registerExecutor(scheme, executor);
        }
    }

    public void setActivity(Activity activity2) {
        this.activity = activity2;
    }

    /* access modifiers changed from: protected */
    public boolean disableScreenCapture() {
        if (this.activity == null || Build.VERSION.SDK_INT < 11) {
            return false;
        }
        this.activity.getWindow().setFlags(8192, 8192);
        return true;
    }

    public void setContentViewLayoutResName(String name) {
        int resId;
        if (this.activity != null && (resId = ResHelper.getLayoutRes(this.activity, name)) > 0) {
            this.activity.setContentView(resId);
        }
    }

    public void setContentView(View view) {
        this.contentView = view;
    }

    public View getContentView() {
        return this.contentView;
    }

    public <T extends View> T findViewById(int id) {
        if (this.activity == null) {
            return null;
        }
        return (T) this.activity.findViewById(id);
    }

    public <T extends View> T findViewByResName(View view, String name) {
        int resId;
        if (this.activity != null && (resId = ResHelper.getIdRes(this.activity, name)) > 0) {
            return (T) view.findViewById(resId);
        }
        return null;
    }

    public <T extends View> T findViewByResName(String name) {
        int resId;
        if (this.activity != null && (resId = ResHelper.getIdRes(this.activity, name)) > 0) {
            return (T) findViewById(resId);
        }
        return null;
    }

    public void onCreate() {
    }

    public void onNewIntent(Intent intent) {
    }

    public void onStart() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public void onRestart() {
    }

    public boolean onFinish() {
        return false;
    }

    public void onDestroy() {
    }

    public final void finish() {
        if (this.activity != null) {
            this.activity.finish();
        }
    }

    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void startActivity(Intent intent) {
        startActivityForResult(intent, -1);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (this.activity != null) {
            this.activity.startActivityForResult(intent, requestCode);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public Context getContext() {
        return this.activity;
    }

    public void show(Context context, Intent i) {
        showForResult(context, i, null);
    }

    public void showForResult(final Context context, Intent i, FakeActivity resultReceiver2) {
        final Intent iExec;
        this.resultReceiver = resultReceiver2;
        String launchTime = null;
        if (shellClass != null) {
            iExec = new Intent(context, shellClass);
            try {
                Method registerExecutor = shellClass.getMethod("registerExecutor", Object.class);
                registerExecutor.setAccessible(true);
                launchTime = (String) registerExecutor.invoke(null, this);
            } catch (Throwable t) {
                MobLog.getInstance().m57w(t);
            }
        } else {
            iExec = new Intent(context, MobUIShell.class);
            launchTime = MobUIShell.registerExecutor(this);
        }
        iExec.putExtra("launch_time", launchTime);
        iExec.putExtra("executor_name", getClass().getName());
        if (i != null) {
            iExec.putExtras(i);
        }
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            showActivity(context, iExec);
        } else {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class:  com.xzy.forestSystem.mob.tools.FakeActivity.1
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message msg) {
                    FakeActivity.this.showActivity(context, iExec);
                    return false;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showActivity(Context cxt, Intent i) {
        if (!(cxt instanceof Activity)) {
            Activity act = DeviceHelper.getInstance(cxt).getTopActivity();
            if (act == null) {
                i.addFlags(268435456);
            } else {
                cxt = act;
            }
        }
        cxt.startActivity(i);
    }

    public FakeActivity getParent() {
        return this.resultReceiver;
    }

    public final void setResult(HashMap<String, Object> result2) {
        this.result = result2;
    }

    public void sendResult() {
        if (this.resultReceiver != null) {
            this.resultReceiver.onResult(this.result);
        }
    }

    public void onResult(HashMap<String, Object> hashMap) {
    }

    public void runOnUIThread(final Runnable r) {
        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class:  com.xzy.forestSystem.mob.tools.FakeActivity.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message msg) {
                r.run();
                return false;
            }
        });
    }

    public void runOnUIThread(final Runnable r, long delayMillis) {
        UIHandler.sendEmptyMessageDelayed(0, delayMillis, new Handler.Callback() { // from class:  com.xzy.forestSystem.mob.tools.FakeActivity.3
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message msg) {
                r.run();
                return false;
            }
        });
    }

    public void setRequestedOrientation(int requestedOrientation) {
        if (this.activity != null) {
            this.activity.setRequestedOrientation(requestedOrientation);
        }
    }

    public void requestPortraitOrientation() {
        setRequestedOrientation(1);
    }

    public void requestLandscapeOrientation() {
        setRequestedOrientation(0);
    }

    public int getOrientation() {
        return this.activity.getResources().getConfiguration().orientation;
    }

    public void requestFullScreen(boolean fullScreen) {
        if (this.activity != null) {
            if (fullScreen) {
                this.activity.getWindow().addFlags(1024);
                this.activity.getWindow().clearFlags(2048);
            } else {
                this.activity.getWindow().addFlags(2048);
                this.activity.getWindow().clearFlags(1024);
            }
            this.activity.getWindow().getDecorView().requestLayout();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void requestPermissions(String[] permissions, int requestCode) {
        if (this.activity != null && Build.VERSION.SDK_INT >= 23) {
            try {
                ReflectHelper.invokeInstanceMethod(this.activity, "requestPermissions", permissions, Integer.valueOf(requestCode));
            } catch (Throwable t) {
                MobLog.getInstance().m69d(t);
            }
        }
    }
}
