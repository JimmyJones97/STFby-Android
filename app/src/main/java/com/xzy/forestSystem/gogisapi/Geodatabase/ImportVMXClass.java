package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportVMXClass {
    public List<FeatureLayer> ImportFile(String filepath, CustomeProgressDialog progressDialog) {
        List<FeatureLayer> tmpLoadLayerList = new ArrayList<>();
        List<FeatureLayer> tempLayers = new ArrayList<>();
        Handler myHandler = progressDialog.myHandler;
        try {
            File tempFile = new File(filepath);
            if (tempFile.exists()) {
                SQLiteDBHelper tempDatabase = new SQLiteDBHelper();
                tempDatabase.setDatabaseName(tempFile.getAbsolutePath());
                Message msg = myHandler.obtainMessage();
                msg.what = 6;
                msg.obj = new Object[]{"开始读取图层信息...", 0, ""};
                myHandler.sendMessage(msg);
                SQLiteReader tempSqlReader = tempDatabase.Query("Select SortID,Name,LayerID,Type,Visible,Transparent,IfLabel,LabelField,LabelFont,LabelScaleMin,LabelScaleMax,FieldList,MinX,MinY,MaxX,MaxY,VisibleScaleMin,VisibleScaleMax,Selectable,Editable,Snapable,RenderType,SimpleRender,UniqueValueField,UniqueValueList,UniqueSymbolList,UniqueDefaultSymbol From T_Layer");
                if (tempSqlReader != null) {
                    while (tempSqlReader.Read()) {
                        FeatureLayer tempLayer = new FeatureLayer();
                        if (tempLayer.ReadLayerInfo(tempSqlReader)) {
                            FeatureLayer tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tempLayer.GetLayerID());
                            if (tmpLayer2 != null) {
                                Message msg2 = myHandler.obtainMessage();
                                msg2.what = 2;
                                msg2.obj = "图层[" + tmpLayer2.GetLayerName() + "]已经存在.";
                                myHandler.sendMessage(msg2);
                            } else {
                                tempLayers.add(tempLayer);
                            }
                        }
                    }
                    tempSqlReader.Close();
                    Message msg3 = myHandler.obtainMessage();
                    msg3.what = 6;
                    msg3.obj = new Object[]{"读取图层信息完成,共有图层 " + tempLayers.size() + "个.", 100, ""};
                    myHandler.sendMessage(msg3);
                    if (!progressDialog.isCancel) {
                        if (tempLayers.size() > 0) {
                            SQLiteDBHelper tempDatabase02 = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
                            int tempLayerIndex = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayersCount();
                            tempDatabase02.ExecuteSQL("attach DATABASE '" + tempFile.getAbsolutePath() + "' AS TempSource");
                            int tempLayerTid = 0;
                            for (FeatureLayer tempLayer2 : tempLayers) {
                                tempLayerTid++;
                                try {
                                    Message msg4 = myHandler.obtainMessage();
                                    msg4.what = 6;
                                    msg4.obj = new Object[]{"正在读取第" + tempLayerTid + "个图层【" + tempLayer2.GetLayerName() + "】的数据... ", 0, ""};
                                    myHandler.sendMessage(msg4);
                                    tempLayer2.SetEditMode(EEditMode.NEW);
                                    tempLayer2.SetLayerIndex(tempLayerIndex);
                                    String tmpTableName = String.valueOf(tempLayer2.GetLayerID()) + "_D";
                                    tempDatabase02.ExecuteSQL("Create Table " + tmpTableName + " As Select * From TempSource." + tmpTableName);
                                    String tmpTableName2 = String.valueOf(tempLayer2.GetLayerID()) + "_I";
                                    tempDatabase02.ExecuteSQL("Create Table " + tmpTableName2 + " As Select * From TempSource." + tmpTableName2);
                                    tmpLoadLayerList.add(tempLayer2);
                                    tempLayer2.SaveLayerInfo();
                                    if (progressDialog.isCancel) {
                                        break;
                                    }
                                } catch (Exception e) {
                                }
                            }
                            tempDatabase02.ExecuteSQL("DETACH DATABASE TempSource");
                        } else {
                            Message msg5 = myHandler.obtainMessage();
                            msg5.what = 1;
                            msg5.obj = "图层数目为0.";
                            myHandler.sendMessage(msg5);
                        }
                    }
                    return tempLayers;
                }
                Message msg6 = myHandler.obtainMessage();
                msg6.what = 1;
                msg6.obj = "数据文件不存在.请检查路径:\r\n" + filepath;
                myHandler.sendMessage(msg6);
            }
        } catch (Exception e2) {
        }
        if (tmpLoadLayerList.size() > 0) {
            Message msg7 = myHandler.obtainMessage();
            msg7.what = 6;
            msg7.obj = new Object[]{"开始转换数据,请稍候...", 0, ""};
            myHandler.sendMessage(msg7);
            SQLiteDBHelper tempDatabase2 = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
            int tid = -1;
            for (FeatureLayer tmpLayer : tmpLoadLayerList) {
                tid++;
                String tmpTableName3 = String.valueOf(tmpLayer.GetLayerID()) + "_D";
                String tempSql2 = "Update " + tmpTableName3 + " Set SYS_GEO=? Where SYS_ID=";
                SQLiteReader tmpSqlReader = tempDatabase2.Query("Select SYS_ID,SYS_GEO From " + tmpTableName3);
                if (tmpSqlReader != null) {
                    Message msg8 = myHandler.obtainMessage();
                    msg8.what = 6;
                    msg8.obj = new Object[]{"正在转换第" + tid + "个图层【" + tmpLayer.GetLayerName() + "】的数据... ", 0, ""};
                    myHandler.sendMessage(msg8);
                    do {
                        try {
                            if (!tmpSqlReader.Read()) {
                                break;
                            }
                            tempDatabase2.ExecuteSQL(String.valueOf(tempSql2) + String.valueOf(tmpSqlReader.GetInt32(0)), new Object[]{Common.ConvertGeoToBytes(Common.ConvertBytesToGeometry(tmpSqlReader.GetBlob(1), tmpLayer.GetLayerType(), false))});
                        } catch (Exception e3) {
                        }
                    } while (!progressDialog.isCancel);
                    tmpSqlReader.Close();
                }
                if (progressDialog.isCancel) {
                    break;
                }
            }
        }
        return tempLayers;
    }

    public List<FeatureLayer> ImportFile2(String filepath, CustomeProgressDialog progressDialog) {
        List<FeatureLayer> tempLayers = new ArrayList<>();
        try {
            File tempFile = new File(filepath);
            if (tempFile.exists()) {
                Handler myHandler = progressDialog.myHandler;
                SQLiteDBHelper tempDatabase = new SQLiteDBHelper();
                tempDatabase.setDatabaseName(tempFile.getAbsolutePath());
                Message msg = myHandler.obtainMessage();
                msg.what = 6;
                msg.obj = new Object[]{"开始读取图层信息...", 0, ""};
                myHandler.sendMessage(msg);
                SQLiteReader tempSqlReader = tempDatabase.Query("Select SortID,Name,LayerID,Type,Visible,Transparent,IfLabel,LabelField,LabelFont,LabelScaleMin,LabelScaleMax,FieldList,MinX,MinY,MaxX,MaxY,VisibleScaleMin,VisibleScaleMax,Selectable,Editable,Snapable,RenderType,SimpleRender,UniqueValueField,UniqueValueList,UniqueSymbolList,UniqueDefaultSymbol From T_Layer");
                if (tempSqlReader != null) {
                    while (tempSqlReader.Read()) {
                        FeatureLayer tempLayer = new FeatureLayer();
                        if (tempLayer.ReadLayerInfo(tempSqlReader)) {
                            FeatureLayer tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tempLayer.GetLayerID());
                            if (tmpLayer2 != null) {
                                Message msg2 = myHandler.obtainMessage();
                                msg2.what = 2;
                                msg2.obj = "图层[" + tmpLayer2.GetLayerName() + "]已经存在.";
                                myHandler.sendMessage(msg2);
                            } else {
                                tempLayers.add(tempLayer);
                            }
                        }
                    }
                    Message msg3 = myHandler.obtainMessage();
                    msg3.what = 6;
                    msg3.obj = new Object[]{"读取图层信息完成,共有图层 " + tempLayers.size() + "个.", 100, ""};
                    myHandler.sendMessage(msg3);
                    if (!progressDialog.isCancel) {
                        if (tempLayers.size() > 0) {
                            SQLiteDBHelper tempDatabase02 = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
                            int tempLayerIndex = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayersCount();
                            int tempLayerTid = 1;
                            for (FeatureLayer tempLayer2 : tempLayers) {
                                Message msg4 = myHandler.obtainMessage();
                                msg4.what = 6;
                                msg4.obj = new Object[]{"正在读取第个" + tempLayerTid + "图层【" + tempLayer2.GetLayerName() + "】的数据... ", 0, ""};
                                myHandler.sendMessage(msg4);
                                PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tempLayer2.GetLayerID());
                                tempLayer2.SetEditMode(EEditMode.NEW);
                                tempLayer2.SetLayerIndex(tempLayerIndex);
                                String tempTableName = String.valueOf(tempLayer2.GetLayerID()) + "_D";
                                List<String> tempFieldsList = new ArrayList<>();
                                List<String> tempFieldsTypeList = new ArrayList<>();
                                SQLiteReader tempSqlReader2 = tempDatabase.Query("pragma table_info ('" + tempTableName + "')");
                                if (tempSqlReader2 != null) {
                                    while (tempSqlReader2.Read()) {
                                        String tempFieldName01 = tempSqlReader2.GetString(1);
                                        if (!tempFieldName01.equals("SYS_GEO")) {
                                            String tempFieldType01 = tempSqlReader2.GetString(2);
                                            tempFieldsList.add(tempFieldName01);
                                            tempFieldsTypeList.add(tempFieldType01);
                                        }
                                    }
                                }
                                String tempFieldsStr = Common.CombineStrings(",", tempFieldsList);
                                int tempGeoTotalCount = 0;
                                SQLiteReader tempSqlReader3 = tempDatabase.Query("Select COUNT(*) From " + tempTableName);
                                if (tempSqlReader3 != null && tempSqlReader3.Read()) {
                                    tempGeoTotalCount = tempSqlReader3.GetInt32(0);
                                }
                                if (tempGeoTotalCount > 0) {
                                    String tempSql2 = "Insert Into " + tempLayer2.GetLayerID() + "_D (SYS_GEO," + tempFieldsStr + ") Values (?,";
                                    SQLiteReader tempSqlReader4 = tempDatabase.Query("Select SYS_GEO," + tempFieldsStr + " From " + tempTableName);
                                    if (tempSqlReader4 != null) {
                                        int tempFieldsCount = tempFieldsList.size();
                                        while (tempSqlReader4.Read()) {
                                            try {
                                                tempDatabase02.ExecuteSQL(String.valueOf(tempSql2) + Common.JoinSQL(",", tempSqlReader4.GetValues(1, tempFieldsCount), tempFieldsTypeList) + ")", new Object[]{Common.ConvertGeoToBytes(Common.ConvertBytesToGeometry(tempSqlReader4.GetBlob(0), tempLayer2.GetLayerType(), false))});
                                            } catch (Exception e) {
                                            }
                                        }
                                        tempSqlReader4.Close();
                                    }
                                    String tempSql22 = "Insert Into " + tempLayer2.GetLayerID() + "_I (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) Values (";
                                    SQLiteReader tempSqlReader5 = tempDatabase.Query("Select SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY From " + tempLayer2.GetLayerID() + "_I");
                                    if (tempSqlReader5 != null) {
                                        tempFieldsList.size();
                                        tempDatabase02.BeginTransaction();
                                        while (tempSqlReader5.Read()) {
                                            try {
                                                tempDatabase02.ExecuteSQL(String.valueOf(tempSql22) + tempSqlReader5.GetInt32(0) + "," + tempSqlReader5.GetInt32(1) + "," + tempSqlReader5.GetInt32(2) + "," + tempSqlReader5.GetDouble(3) + "," + tempSqlReader5.GetDouble(4) + "," + tempSqlReader5.GetDouble(5) + "," + tempSqlReader5.GetDouble(6) + ")");
                                            } catch (Exception e2) {
                                            } finally {
                                                tempDatabase02.EndTransaction();
                                            }
                                        }
                                        tempDatabase02.SetTransactionSuccessful();
                                        tempSqlReader5.Close();
                                    }
                                    Message msg5 = myHandler.obtainMessage();
                                    msg5.what = 5;
                                    msg5.obj = new Object[]{"导入数据 [" + (0 + 1) + FileSelector_Dialog.sRoot + tempGeoTotalCount + "]", Integer.valueOf(100 / tempGeoTotalCount)};
                                    myHandler.sendMessage(msg5);
                                    if (progressDialog.isCancel) {
                                        break;
                                    }
                                } else {
                                    Message msg6 = myHandler.obtainMessage();
                                    msg6.what = 5;
                                    msg6.obj = new Object[]{"数据数目为0.", 100};
                                    myHandler.sendMessage(msg6);
                                }
                                Message msg7 = myHandler.obtainMessage();
                                msg7.what = 2;
                                msg7.obj = "正在保存图层信息...";
                                myHandler.sendMessage(msg7);
                                tempLayer2.SaveLayerInfo();
                                tempLayerIndex++;
                                tempLayerTid++;
                            }
                        } else {
                            Message msg8 = myHandler.obtainMessage();
                            msg8.what = 1;
                            msg8.obj = "图层数目为0.";
                            myHandler.sendMessage(msg8);
                        }
                    }
                } else {
                    Message msg9 = myHandler.obtainMessage();
                    msg9.what = 1;
                    msg9.obj = "数据文件不存在.请检查路径:\r\n" + filepath;
                    myHandler.sendMessage(msg9);
                }
            }
        } catch (Exception e3) {
        }
        return tempLayers;
    }
}
