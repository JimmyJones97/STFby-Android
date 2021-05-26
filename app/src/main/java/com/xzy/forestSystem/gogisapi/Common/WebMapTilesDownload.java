package  com.xzy.forestSystem.gogisapi.Common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.BaseTileInfo;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebMapTilesDownload implements Runnable {
    public boolean IsStop = false;
    private Handler _Handler = null;
    private List<BaseTileInfo> _TileList = new ArrayList();

    @Override // java.lang.Runnable
    public void run() {
        StartDownload();
    }

    private boolean StartDownloadFile(BaseTileInfo tile) {
        if (this.IsStop) {
            return true;
        }
        boolean tempDownload = false;
        try {
            try {
                HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(tile.Url).openConnection();
                localHttpURLConnection.setRequestMethod("GET");
                if (localHttpURLConnection.getResponseCode() == 200) {
                    localHttpURLConnection.connect();
                    InputStream localInputStream = localHttpURLConnection.getInputStream();
                    localHttpURLConnection.getContentLength();
                    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] tempBytes = new byte[8192];
                    while (true) {
                        int bytes_read = localInputStream.read(tempBytes);
                        if (bytes_read <= 0) {
                            break;
                        }
                        localByteArrayOutputStream.write(tempBytes, 0, bytes_read);
                    }
                    if (localByteArrayOutputStream.size() > 0) {
                        byte[] tempResult = localByteArrayOutputStream.toByteArray();
                        localByteArrayOutputStream.close();
                        localInputStream.close();
                        tempDownload = true;
                        Message localMessage = new Message();
                        localMessage.what = 1;
                        Bundle localBundle = new Bundle();
                        localBundle.putByteArray("ImageByte", tempResult);
                        localBundle.putSerializable("Tile", tile);
                        localMessage.setData(localBundle);
                        this._Handler.sendMessage(localMessage);
                    }
                }
            } catch (Exception ex) {
                Common.Log("StartDownloadFile", ex.getLocalizedMessage());
            }
            if (!tempDownload) {
                Message localMessage2 = new Message();
                localMessage2.what = 2;
                localMessage2.obj = tile.GetTileName();
                this._Handler.sendMessage(localMessage2);
            }
            this._TileList.remove(tile);
            if (this._TileList.size() > 0) {
                StartDownloadFile(this._TileList.get(0));
            } else {
                Message localMessage3 = new Message();
                localMessage3.what = 3;
                this._Handler.sendMessage(localMessage3);
            }
        } catch (Exception ex2) {
            Common.Log("StartDownloadFile", ex2.getLocalizedMessage());
            if (0 == 0) {
                Message localMessage4 = new Message();
                localMessage4.what = 2;
                localMessage4.obj = tile.GetTileName();
                this._Handler.sendMessage(localMessage4);
            }
            this._TileList.remove(tile);
            if (this._TileList.size() > 0) {
                StartDownloadFile(this._TileList.get(0));
            } else {
                Message localMessage5 = new Message();
                localMessage5.what = 3;
                this._Handler.sendMessage(localMessage5);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                Message localMessage6 = new Message();
                localMessage6.what = 2;
                localMessage6.obj = tile.GetTileName();
                this._Handler.sendMessage(localMessage6);
            }
            this._TileList.remove(tile);
            if (this._TileList.size() > 0) {
                StartDownloadFile(this._TileList.get(0));
            } else {
                Message localMessage7 = new Message();
                localMessage7.what = 3;
                this._Handler.sendMessage(localMessage7);
            }
            throw th;
        }
        return true;
    }

    public void AddDownLoadFile(BaseTileInfo paramOverMapTile) {
        this._TileList.add(0, paramOverMapTile);
    }

    public void StartDownload() {
        if (this._TileList.size() > 0) {
            StartDownloadFile(this._TileList.get(0));
        }
    }

    public void setCallbackHandler(Handler paramHandler) {
        this._Handler = paramHandler;
    }
}
