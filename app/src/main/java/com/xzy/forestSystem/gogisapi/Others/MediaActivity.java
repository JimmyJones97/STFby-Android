package  com.xzy.forestSystem.gogisapi.Others;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import  com.xzy.forestSystem.stub.StubApp;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;

public class MediaActivity extends Activity {
    public static ICallback BindCallbak = null;
    private String saveFilePath = "";
    private long tokenDate = 0;

    @Override // android.app.Activity
    public native void onCreate(Bundle bundle);

    static {
        StubApp.interface11(2094);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
        if (getResources().getConfiguration().orientation == 2) {
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case -1:
                switch (requestCode) {
                    case 1:
                        if (MediaManag_Dialog._Callback != null) {
                            MediaManag_Dialog._Callback.OnClick("Image", new Object[]{this.saveFilePath, Long.valueOf(this.tokenDate)});
                            break;
                        }
                        break;
                    case 2:
                        if (MediaManag_Dialog._Callback != null) {
                            MediaManag_Dialog._Callback.OnClick("Video", new Object[]{this.saveFilePath, Long.valueOf(this.tokenDate)});
                            break;
                        }
                        break;
                    case 3:
                        if (BindCallbak != null) {
                            BindCallbak.OnClick("QRResult", data.getExtras().getString(SynthesizeResultDb.KEY_RESULT));
                            break;
                        }
                        break;
                }
        }
        finish();
    }
}
