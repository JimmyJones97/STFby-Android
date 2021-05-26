package  com.xzy.forestSystem.gogisapi.Common;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.BaseTileInfo;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Downloader {
    public static int MAX_DOWNLOAD_TIME = 1;
    public static int REFRESH_MAP_INTERVAL = 4;
    public static final String Tag_Downloader_AllFinish = "Tag_Downloader_AllFinish";
    public static final String Tag_Downloader_Finish = "Tag_Downloader_Finish";
    public static final String Tag_Downloader_FinishAndRefresh = "Tag_Downloader_FinishAndRefresh";
    int currentTotalCount = 0;
    private ICallback m_Callback = null;
    private List<String> m_DownloadTileNameList = new ArrayList();
    private List<BaseTileInfo> m_DownloadTilesList = new ArrayList();
    private Handler m_Handler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Common.Downloader.1
        @Override // android.os.Handler
        public void handleMessage(Message paramMessage) {
            int tmpI;
            try {
                if (paramMessage.what == 0) {
                    int tmpI2 = Downloader.this.m_DownloadTileNameList.indexOf(String.valueOf(paramMessage.obj));
                    if (tmpI2 >= 0) {
                        Downloader.this.m_DownloadTileNameList.remove(tmpI2);
                        Downloader.this.m_DownloadTilesList.remove(tmpI2);
                    }
                } else if (paramMessage.what == 1 && (tmpI = Downloader.this.m_DownloadTileNameList.indexOf(String.valueOf(paramMessage.obj))) >= 0) {
                    BaseTileInfo tmpTileInfo = (BaseTileInfo) Downloader.this.m_DownloadTilesList.get(tmpI);
                    if (tmpTileInfo.TilesLayer != null) {
                        if (Downloader.this.m_DownloadTileNameList.size() == 1) {
                            if (Downloader.this.m_Callback != null) {
                                Downloader.this.m_Callback.OnClick(Downloader.Tag_Downloader_AllFinish, tmpTileInfo);
                            }
                        } else if (Downloader.this.m_Callback != null) {
                            if (Downloader.this.m_DownloadTileNameList.size() % Downloader.REFRESH_MAP_INTERVAL == 0) {
                                Downloader.this.m_Callback.OnClick(Downloader.Tag_Downloader_FinishAndRefresh, tmpTileInfo);
                            } else {
                                Downloader.this.m_Callback.OnClick(Downloader.Tag_Downloader_Finish, tmpTileInfo);
                            }
                        }
                    }
                    Downloader.this.m_DownloadTileNameList.remove(tmpI);
                    Downloader.this.m_DownloadTilesList.remove(tmpI);
                }
            } catch (Exception e) {
            }
        }
    };
    long startMili = System.currentTimeMillis();
    private ExecutorService threadPool;

    public Downloader() {
        if (PubVar.MapTile_Download_ThreadCount < 1) {
            PubVar.MapTile_Download_ThreadCount = 10;
        }
        this.threadPool = Executors.newFixedThreadPool(PubVar.MapTile_Download_ThreadCount);
        this.m_DownloadTilesList = new ArrayList();
        this.m_DownloadTileNameList = new ArrayList();
    }

    public void setCallback(ICallback pCallback) {
        this.m_Callback = pCallback;
    }

    public boolean DownloadTile(final BaseTileInfo tile) {
        try {
            String tmpKeyString = tile.GetTileName();
            int tmpI = this.m_DownloadTileNameList.indexOf(tmpKeyString);
            if (tmpI < 0 || tile.getData() == null) {
                Runnable runnable = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Common.Downloader.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (Downloader.this.downloadFile(tile)) {
                            Message localMessage = new Message();
                            localMessage.what = 1;
                            localMessage.obj = tile.GetTileName();
                            Downloader.this.m_Handler.sendMessage(localMessage);
                            return;
                        }
                        tile.HasDownloadTime++;
                        if (tile.HasDownloadTime < Downloader.MAX_DOWNLOAD_TIME) {
                            Downloader.this.DownloadTile(tile);
                            return;
                        }
                        Message localMessage2 = new Message();
                        localMessage2.what = 0;
                        localMessage2.obj = tile.GetTileName();
                        Downloader.this.m_Handler.sendMessage(localMessage2);
                    }
                };
                if (tmpI < 0) {
                    this.m_DownloadTileNameList.add(tmpKeyString);
                    this.m_DownloadTilesList.add(tile);
                }
                this.threadPool.execute(runnable);
                return true;
            }
        } catch (Exception e) {
            Common.Log("Downloader-DownloadTile", e.getLocalizedMessage());
        }
        return false;
    }

    public int getCurrentDownlodCount() {
        return this.m_DownloadTilesList.size();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean downloadFile(BaseTileInfo tile) {
        try {
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(tile.Url).openConnection();
            localHttpURLConnection.setRequestMethod("GET");
            localHttpURLConnection.setConnectTimeout(5000);
            localHttpURLConnection.setReadTimeout(5000);
            if (localHttpURLConnection.getResponseCode() != 200) {
                return false;
            }
            localHttpURLConnection.connect();
            InputStream localInputStream = localHttpURLConnection.getInputStream();
            if (localHttpURLConnection.getContentLength() > 100000) {
                tile.HasDownloadTime = MAX_DOWNLOAD_TIME;
                return false;
            }
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            byte[] tempBytes = new byte[4096];
            while (true) {
                int bytes_read = localInputStream.read(tempBytes);
                if (bytes_read <= 0) {
                    break;
                }
                localByteArrayOutputStream.write(tempBytes, 0, bytes_read);
            }
            if (localByteArrayOutputStream.size() <= 0) {
                return false;
            }
            byte[] tempResult = localByteArrayOutputStream.toByteArray();
            localByteArrayOutputStream.close();
            localInputStream.close();
            tile.setData(tempResult);
            return true;
        } catch (FileNotFoundException e) {
            tile.HasDownloadTime = MAX_DOWNLOAD_TIME;
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public synchronized void cancelTasks() {
        if (this.threadPool != null) {
            this.threadPool.shutdownNow();
            this.threadPool = null;
        }
    }
}
