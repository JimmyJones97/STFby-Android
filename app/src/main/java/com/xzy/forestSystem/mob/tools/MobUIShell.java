package com.xzy.forestSystem.mob.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import  com.xzy.forestSystem.mob.tools.utils.ReflectHelper;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.HashMap;

public class MobUIShell extends Activity {
    private static HashMap<String, FakeActivity> executors = new HashMap<>();
    public static int forceTheme;
    private FakeActivity executor;

    static {
        MobLog.getInstance().m70d("===============================", new Object[0]);
        MobLog.getInstance().m70d("MobTools " + "2017-06-06".replace("-0", "-").replace("-", FileSelector_Dialog.sFolder), new Object[0]);
        MobLog.getInstance().m70d("===============================", new Object[0]);
    }

    protected static String registerExecutor(Object executor2) {
        return registerExecutor(String.valueOf(System.currentTimeMillis()), executor2);
    }

    protected static String registerExecutor(String scheme, Object executor2) {
        executors.put(scheme, (FakeActivity) executor2);
        return scheme;
    }

    @Override // android.app.Activity, android.content.Context, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void setTheme(int resid) {
        if (forceTheme > 0) {
            super.setTheme(forceTheme);
        } else {
            super.setTheme(resid);
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        try {
            String launchTime = intent.getStringExtra("launch_time");
            String executorName = intent.getStringExtra("executor_name");
            this.executor = executors.remove(launchTime);
            if (this.executor == null) {
                this.executor = executors.remove(intent.getScheme());
                if (this.executor == null) {
                    this.executor = getDefault();
                    if (this.executor == null) {
                        MobLog.getInstance().m57w(new RuntimeException("Executor lost! launchTime = " + launchTime + ", executorName: " + executorName));
                        super.onCreate(savedInstanceState);
                        finish();
                        return;
                    }
                }
            }
            MobLog.getInstance().m64i("MobUIShell found executor: " + this.executor.getClass(), new Object[0]);
            this.executor.setActivity(this);
            super.onCreate(savedInstanceState);
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onCreate", new Object[0]);
            this.executor.onCreate();
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            super.onCreate(savedInstanceState);
            finish();
        }
    }

    public FakeActivity getDefault() {
        Object fa;
        try {
            String defaultActivity = getPackageManager().getActivityInfo(getComponentName(), 128).metaData.getString("defaultActivity");
            if (!TextUtils.isEmpty(defaultActivity)) {
                if (defaultActivity.startsWith(FileSelector_Dialog.sFolder)) {
                    defaultActivity = getPackageName() + defaultActivity;
                }
                String name = ReflectHelper.importClass(defaultActivity);
                if (!TextUtils.isEmpty(name) && (fa = ReflectHelper.newInstance(name, new Object[0])) != null && (fa instanceof FakeActivity)) {
                    return (FakeActivity) fa;
                }
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        return null;
    }

    @Override // android.app.Activity
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, (ViewGroup) null));
    }

    @Override // android.app.Activity
    public void setContentView(View view) {
        if (view != null) {
            super.setContentView(view);
            if (this.executor != null) {
                this.executor.setContentView(view);
            }
        }
    }

    @Override // android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view != null) {
            if (params == null) {
                super.setContentView(view);
            } else {
                super.setContentView(view, params);
            }
            if (this.executor != null) {
                this.executor.setContentView(view);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        if (this.executor == null) {
            super.onNewIntent(intent);
        } else {
            this.executor.onNewIntent(intent);
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStart() {
        if (this.executor != null) {
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onStart", new Object[0]);
            this.executor.onStart();
        }
        super.onStart();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        if (this.executor != null) {
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onResume", new Object[0]);
            this.executor.onResume();
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        if (this.executor != null) {
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onPause", new Object[0]);
            this.executor.onPause();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStop() {
        if (this.executor != null) {
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onStop", new Object[0]);
            this.executor.onStop();
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onRestart() {
        if (this.executor != null) {
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onRestart", new Object[0]);
            this.executor.onRestart();
        }
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        if (this.executor != null) {
            this.executor.sendResult();
            MobLog.getInstance().m70d(this.executor.getClass().getSimpleName() + " onDestroy", new Object[0]);
            this.executor.onDestroy();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.executor != null) {
            this.executor.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean ret = false;
        if (this.executor != null) {
            ret = this.executor.onKeyEvent(keyCode, event);
        }
        if (ret) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean ret = false;
        if (this.executor != null) {
            ret = this.executor.onKeyEvent(keyCode, event);
        }
        if (ret) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.executor != null) {
            this.executor.onConfigurationChanged(newConfig);
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (this.executor != null) {
            this.executor.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override // android.app.Activity
    public void finish() {
        if (this.executor == null || !this.executor.onFinish()) {
            super.finish();
        }
    }

    public Object getExecutor() {
        return this.executor;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean res = super.onCreateOptionsMenu(menu);
        if (this.executor != null) {
            return this.executor.onCreateOptionsMenu(menu);
        }
        return res;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = super.onOptionsItemSelected(item);
        if (this.executor != null) {
            return this.executor.onOptionsItemSelected(item);
        }
        return res;
    }
}
