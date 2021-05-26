package  com.xzy.forestSystem.gogisapi.Common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.BaseTileInfo;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WebMapTilesDownloader {
    private int _NeedRefreshTileCount = 0;
    private List<BaseTileInfo> _NeedSaveTiles = new ArrayList();
    private Timer _SaveTimer = null;
    private XBaseTilesLayer _TilesLayer = null;
    TimerTask _saveTask = new TimerTask() { // from class:  com.xzy.forestSystem.gogisapi.Common.WebMapTilesDownloader.1
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (WebMapTilesDownloader.this._NeedSaveTiles.size() > 0) {
                WebMapTilesDownloader.this._TilesLayer.saveTilesCache(WebMapTilesDownloader.this._NeedSaveTiles);
            }
        }
    };
    private List<BaseTileInfo> m_DownLoadFileList = new ArrayList();
    private int m_DownloadThreadCount = 5;
    private List<WebMapTilesDownload> m_DownloadThreadList = new ArrayList();
    private int m_TotalCount = 0;
    Handler myHander = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Common.WebMapTilesDownloader.2
        /* JADX INFO: finally extract failed */
        @Override // android.os.Handler
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what == 1) {
                WebMapTilesDownloader.this._NeedRefreshTileCount++;
                try {
                    Bundle localBundle = paramMessage.getData();
                    BaseTileInfo tempTile = (BaseTileInfo) localBundle.getSerializable("Tile");
                    byte[] arrayOfByte = localBundle.getByteArray("ImageByte");
                    if (!WebMapTilesDownloader.this._TilesLayer.saveTileCache(tempTile, arrayOfByte) && WebMapTilesDownloader.this.returnHandler == null) {
                        Common.ShowToast("保存缓存数据失败.");
                    }
                    if (WebMapTilesDownloader.this.returnHandler != null) {
                        Message localMessage = new Message();
                        localMessage.what = 1;
                        WebMapTilesDownloader.this.returnHandler.sendMessage(localMessage);
                    } else if (!(tempTile == null || arrayOfByte == null)) {
                        WebMapTilesDownloader.this._TilesLayer.ShowImage(PubVar._Map, tempTile, arrayOfByte);
                    }
                } catch (Exception e) {
                    if (WebMapTilesDownloader.this.returnHandler != null) {
                        Message localMessage2 = new Message();
                        localMessage2.what = 1;
                        WebMapTilesDownloader.this.returnHandler.sendMessage(localMessage2);
                    } else if (!(0 == 0 || 0 == 0)) {
                        WebMapTilesDownloader.this._TilesLayer.ShowImage(PubVar._Map, null, null);
                    }
                } catch (Throwable th) {
                    if (WebMapTilesDownloader.this.returnHandler != null) {
                        Message localMessage3 = new Message();
                        localMessage3.what = 1;
                        WebMapTilesDownloader.this.returnHandler.sendMessage(localMessage3);
                    } else if (!(0 == 0 || 0 == 0)) {
                        WebMapTilesDownloader.this._TilesLayer.ShowImage(PubVar._Map, null, null);
                    }
                    throw th;
                }
            } else if (paramMessage.what == 2) {
                WebMapTilesDownloader.this._NeedRefreshTileCount++;
                if (WebMapTilesDownloader.this.returnHandler != null) {
                    Message localMessage4 = new Message();
                    localMessage4.what = 2;
                    WebMapTilesDownloader.this.returnHandler.sendMessage(localMessage4);
                }
            } else if (paramMessage.what == 3) {
                PubVar._Map.RefreshFastRasterLayers();
            }
            super.handleMessage(paramMessage);
        }
    };
    private Handler returnHandler = null;

    public WebMapTilesDownloader() {
        int count = this.m_DownloadThreadCount;
        for (int i = 0; i < count; i++) {
            WebMapTilesDownload tempTilesDownload = new WebMapTilesDownload();
            tempTilesDownload.setCallbackHandler(this.myHander);
            this.m_DownloadThreadList.add(tempTilesDownload);
        }
    }

    public WebMapTilesDownloader(int threadCount) {
        this.m_DownloadThreadCount = threadCount;
        int count = this.m_DownloadThreadCount;
        for (int i = 0; i < count; i++) {
            WebMapTilesDownload tempTilesDownload = new WebMapTilesDownload();
            tempTilesDownload.setCallbackHandler(this.myHander);
            this.m_DownloadThreadList.add(tempTilesDownload);
        }
    }

    public void setReturnHandler(Handler handler) {
        this.returnHandler = handler;
    }

    public void StartUpLoad() {
        this._NeedRefreshTileCount = 0;
        this.m_TotalCount = this.m_DownLoadFileList.size();
        if (this._SaveTimer == null) {
            this._SaveTimer = new Timer();
            this._SaveTimer.schedule(this._saveTask, 0, 3000);
        }
        if (this.m_DownloadThreadList.size() > 0 && this.m_DownLoadFileList.size() > 0) {
            int count = this.m_DownloadThreadList.size();
            int count2 = this.m_DownLoadFileList.size();
            int tempJ1 = 0;
            int tempJCount = count2 / count;
            int tempJ2 = tempJCount;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    tempJ2 = count2;
                }
                WebMapTilesDownload tempTilesDownload = this.m_DownloadThreadList.get(i);
                for (int j = tempJ1; j < tempJ2; j++) {
                    tempTilesDownload.AddDownLoadFile(this.m_DownLoadFileList.get(j));
                }
                new Thread(this.m_DownloadThreadList.get(i)).start();
                tempJ1 += tempJCount;
                tempJ2 += tempJCount;
            }
        }
    }

    public void setTilesLayer(XBaseTilesLayer TilesLayer) {
        this._TilesLayer = TilesLayer;
    }

    public void setDownloadFileList(List<BaseTileInfo> paramList) {
        this.m_DownLoadFileList = paramList;
    }

    public void StopDownload() {
        int count = this.m_DownloadThreadCount;
        for (int i = 0; i < count; i++) {
            this.m_DownloadThreadList.get(i).IsStop = true;
        }
    }
}
