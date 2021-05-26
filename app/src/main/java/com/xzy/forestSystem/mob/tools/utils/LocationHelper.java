package com.xzy.forestSystem.mob.tools.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;

public class LocationHelper implements LocationListener, Handler.Callback {
    private boolean gpsRequesting;
    private int gpsTimeoutSec;
    private Handler handler;

    /* renamed from: lm */
    private LocationManager f382lm;
    private boolean networkRequesting;
    private int networkTimeoutSec;
    private Location res;

    public LocationHelper() {
        MobHandlerThread thread = new MobHandlerThread();
        thread.start();
        this.handler = new Handler(thread.getLooper(), this);
    }

    public Location getLocation(Context context) throws Throwable {
        return getLocation(context, 0);
    }

    public Location getLocation(Context context, int GPSTimeoutSec) throws Throwable {
        return getLocation(context, GPSTimeoutSec, 0);
    }

    public Location getLocation(Context context, int GPSTimeoutSec, int networkTimeoutSec2) throws Throwable {
        return getLocation(context, GPSTimeoutSec, networkTimeoutSec2, true);
    }

    public Location getLocation(Context context, int GPSTimeoutSec, int networkTimeoutSec2, boolean useLastKnown) throws Throwable {
        boolean preferGPS;
        boolean preferNetwork = true;
        this.gpsTimeoutSec = GPSTimeoutSec;
        this.networkTimeoutSec = networkTimeoutSec2;
        this.f382lm = (LocationManager) context.getSystemService("location");
        if (this.f382lm == null) {
            return null;
        }
        synchronized (this) {
            this.handler.sendEmptyMessageDelayed(0, 50);
            wait();
        }
        if (this.res == null && useLastKnown) {
            if (GPSTimeoutSec != 0) {
                preferGPS = true;
            } else {
                preferGPS = false;
            }
            if (networkTimeoutSec2 == 0) {
                preferNetwork = false;
            }
            if (preferGPS && this.f382lm.isProviderEnabled("gps")) {
                this.res = this.f382lm.getLastKnownLocation("gps");
            } else if (preferNetwork && this.f382lm.isProviderEnabled("network")) {
                this.res = this.f382lm.getLastKnownLocation("network");
            }
        }
        return this.res;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        if (msg.what == 0) {
            onRequest();
            return false;
        } else if (this.gpsRequesting) {
            onGPSTimeout();
            return false;
        } else if (!this.networkRequesting) {
            return false;
        } else {
            this.f382lm.removeUpdates(this);
            synchronized (this) {
                notifyAll();
            }
            this.handler.getLooper().quit();
            return false;
        }
    }

    private void onRequest() {
        boolean preferGPS;
        boolean preferNetwork;
        if (this.gpsTimeoutSec != 0) {
            preferGPS = true;
        } else {
            preferGPS = false;
        }
        if (this.networkTimeoutSec != 0) {
            preferNetwork = true;
        } else {
            preferNetwork = false;
        }
        if (preferGPS && this.f382lm.isProviderEnabled("gps")) {
            this.gpsRequesting = true;
            this.f382lm.requestLocationUpdates("gps", 1000, 0.0f, this);
            if (this.gpsTimeoutSec > 0) {
                this.handler.sendEmptyMessageDelayed(1, (long) (this.gpsTimeoutSec * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
            }
        } else if (!preferNetwork || !this.f382lm.isProviderEnabled("network")) {
            synchronized (this) {
                notifyAll();
            }
            this.handler.getLooper().quit();
        } else {
            this.networkRequesting = true;
            this.f382lm.requestLocationUpdates("network", 1000, 0.0f, this);
            if (this.networkTimeoutSec > 0) {
                this.handler.sendEmptyMessageDelayed(1, (long) (this.networkTimeoutSec * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
            }
        }
    }

    private void onGPSTimeout() {
        boolean preferNetwork = false;
        this.f382lm.removeUpdates(this);
        this.gpsRequesting = false;
        if (this.networkTimeoutSec != 0) {
            preferNetwork = true;
        }
        if (!preferNetwork || !this.f382lm.isProviderEnabled("network")) {
            synchronized (this) {
                notifyAll();
            }
            this.handler.getLooper().quit();
            return;
        }
        this.networkRequesting = true;
        this.f382lm.requestLocationUpdates("network", 1000, 0.0f, this);
        if (this.networkTimeoutSec > 0) {
            this.handler.sendEmptyMessageDelayed(1, (long) (this.networkTimeoutSec * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        synchronized (this) {
            this.f382lm.removeUpdates(this);
            this.res = location;
            notifyAll();
        }
        this.handler.getLooper().quit();
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String provider) {
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String provider) {
    }
}
