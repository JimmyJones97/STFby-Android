package  com.xzy.forestSystem.gogisapi.Workspace;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.Date;

public class ProjectWorkspace {
    private AbstractC0383CoordinateSystem m_CoorSystem = null;
    private ProjectDatabase m_ProjectDB = null;
    private String m_ProjectName = "";
    private String m_Project_CreateTime = "";

    public AbstractC0383CoordinateSystem GetCoorSystem() {
        if (this.m_CoorSystem == null) {
            this.m_CoorSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem("");
        }
        return this.m_CoorSystem;
    }

    public String GetProjectCreateTime() {
        return this.m_Project_CreateTime;
    }

    public String GetProjectDataFileName() {
        return String.valueOf(GetProjectFullName()) + "/TAData.dbx";
    }

    public String GetProjectDataPreviewImageName() {
        return String.valueOf(GetProjectFullName()) + "/DataPreview.jpg";
    }

    public String GetProjectFullName() {
        return String.valueOf(PubVar.m_SystemPath) + "/Data/" + this.m_ProjectName;
    }

    public void LoadProjectInfo() {
        try {
            SQLiteReader tempReader = this.m_ProjectDB.GetSQLiteDatabase().Query("select * from T_Project where ID IS NOT NULL order by id desc limit 1");
            if (tempReader != null && tempReader.Read()) {
                this.m_Project_CreateTime = tempReader.GetString("CreateTime");
                this.m_CoorSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem(tempReader.GetString("CoorSystem"));
                if (this.m_CoorSystem.getIsProjectionCoord()) {
                    String tempStr = tempReader.GetString("CenterMeridian");
                    if (tempStr != null && !tempStr.equals("")) {
                        ((ProjectionCoordinateSystem) this.m_CoorSystem).SetCenterMeridian(Float.parseFloat(tempStr));
                    }
                    String tempStr2 = tempReader.GetString("F1");
                    if (tempStr2 != null && !tempStr2.equals("")) {
                        ((ProjectionCoordinateSystem) this.m_CoorSystem).SetDaiHao(Float.parseFloat(tempStr2));
                    }
                    String tempStr3 = tempReader.GetString("F2");
                    if (tempStr3 != null && !tempStr3.equals("")) {
                        if (tempStr3.contains(FileSelector_Dialog.sFolder)) {
                            tempStr3 = tempStr3.substring(0, tempStr3.indexOf(FileSelector_Dialog.sFolder));
                        }
                        ((ProjectionCoordinateSystem) this.m_CoorSystem).SetFenQu(Integer.parseInt(tempStr3));
                    }
                }
                String tempStr4 = tempReader.GetString("F3");
                if (tempStr4 != null && !tempStr4.equals("")) {
                    this.m_CoorSystem.setSpheroidName(tempStr4);
                }
                try {
                    String tempStr5 = tempReader.GetString("F4");
                    if (tempStr5 != null && !tempStr5.equals("")) {
                        double tmpA = Double.parseDouble(tempStr5);
                        String tempStr6 = tempReader.GetString("F5");
                        if (tempStr6 != null && !tempStr6.equals("")) {
                            double tmpF = Double.parseDouble(tempStr6);
                            this.m_CoorSystem.SetA(tmpA);
                            this.m_CoorSystem.Setf(tmpF);
                            this.m_CoorSystem.SetB(tmpA * (1.0d - (1.0d / tmpF)));
                        }
                    }
                } catch (Exception e) {
                }
                this.m_CoorSystem.GetPMTranslate().SetPMCoorTransMethodName(tempReader.GetString("PMTransMethod"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP31(tempReader.GetString("P31"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP32(tempReader.GetString("P32"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP33(tempReader.GetString("P33"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP41(tempReader.GetString("P41"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP42(tempReader.GetString("P42"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP43(tempReader.GetString("P43"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP44(tempReader.GetString("P44"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP71(tempReader.GetString("P71"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP72(tempReader.GetString("P72"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP73(tempReader.GetString("P73"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP74(tempReader.GetString("P74"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP75(tempReader.GetString("P75"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP76(tempReader.GetString("P76"));
                this.m_CoorSystem.GetPMTranslate().SetTransToP77(tempReader.GetString("P77"));
                this.m_CoorSystem.GetPMTranslate().SetBiasX(tempReader.GetString("BiasX"));
                this.m_CoorSystem.GetPMTranslate().SetBiasY(tempReader.GetString("BiasY"));
                this.m_CoorSystem.GetPMTranslate().SetBiasZ(tempReader.GetString("BiasZ"));
                tempReader.Close();
            }
            SQLiteReader tempReader2 = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("select * from T_CoorSystem where name = '" + this.m_CoorSystem.GetName() + "'");
            if (tempReader2 != null && tempReader2.Read()) {
                if (this.m_CoorSystem.getIsProjectionCoord()) {
                    ((ProjectionCoordinateSystem) this.m_CoorSystem).SetEasting(Double.parseDouble(tempReader2.GetString("Easting")));
                }
                tempReader2.Close();
            }
        } catch (Exception e2) {
        }
    }

    public void SaveProjectInfo() {
        try {
            this.m_ProjectDB.GetSQLiteDatabase().Query("select * from T_Project where ID IS NOT NULL order by id desc limit 1");
            StringBuilder tempSB = new StringBuilder();
            tempSB.append("Update T_Project Set CoorSystem='");
            tempSB.append(String.valueOf(this.m_CoorSystem.GetName()) + "'");
            if (this.m_CoorSystem.getIsProjectionCoord()) {
                tempSB.append(",CenterMeridian='");
                tempSB.append(((ProjectionCoordinateSystem) this.m_CoorSystem).GetCenterMeridian());
                tempSB.append("' ");
                tempSB.append(",F1='");
                tempSB.append(((ProjectionCoordinateSystem) this.m_CoorSystem).getDaiHao());
                tempSB.append("',F2='");
                tempSB.append(((ProjectionCoordinateSystem) this.m_CoorSystem).getFenQu());
                tempSB.append("'");
            }
            tempSB.append(",PMTransMethod='" + this.m_CoorSystem.GetPMTranslate().GetPMCoorTransMethodName() + "',P31='" + this.m_CoorSystem.GetPMTranslate().GetTransToP31() + "',P32='" + this.m_CoorSystem.GetPMTranslate().GetTransToP32() + "',P33='" + this.m_CoorSystem.GetPMTranslate().GetTransToP33() + "',P41='" + this.m_CoorSystem.GetPMTranslate().GetTransToP41() + "',P42='" + this.m_CoorSystem.GetPMTranslate().GetTransToP42() + "',P43='" + this.m_CoorSystem.GetPMTranslate().GetTransToP43() + "',P44='" + this.m_CoorSystem.GetPMTranslate().GetTransToP44() + "',P71='" + this.m_CoorSystem.GetPMTranslate().GetTransToP71() + "',P72='" + this.m_CoorSystem.GetPMTranslate().GetTransToP72() + "',P73='" + this.m_CoorSystem.GetPMTranslate().GetTransToP73() + "',P74='" + this.m_CoorSystem.GetPMTranslate().GetTransToP74() + "',P75='" + this.m_CoorSystem.GetPMTranslate().GetTransToP75() + "',P76='" + this.m_CoorSystem.GetPMTranslate().GetTransToP76() + "',P77='" + this.m_CoorSystem.GetPMTranslate().GetTransToP77() + "',BiasX='" + this.m_CoorSystem.GetPMTranslate().getBiasX() + "',BiasY='" + this.m_CoorSystem.GetPMTranslate().getBiasY() + "',BiasZ='" + this.m_CoorSystem.GetPMTranslate().getBiasZ());
            tempSB.append("' Where ProjectName='" + this.m_ProjectName + "'");
            this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSB.toString());
            PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Update T_Project Set ModifyTime='" + String.valueOf(new Date().getTime()) + "' Where ProjectName='" + this.m_ProjectName + "'");
        } catch (Exception e) {
        }
    }

    public void SetBindProject(ProjectDatabase tmpProject) {
        this.m_ProjectDB = tmpProject;
    }

    public void SetProjectName(String paramString) {
        this.m_ProjectName = paramString;
    }

    public String getSelectionLayers() {
        String result = "";
        SQLiteReader tempReader = this.m_ProjectDB.GetSQLiteDatabase().Query("Select F3 From T_Project Where ProjectName='" + this.m_ProjectName + "' Limit 1");
        if (tempReader != null && tempReader.Read()) {
            result = tempReader.GetString(0);
            if (result == null) {
                result = "";
            }
            tempReader.Close();
        }
        return result;
    }

    public void SaveSelectionLayers(String layersID) {
        this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Update T_Project Set F3='" + layersID + "' Where ProjectName='" + this.m_ProjectName + "'");
    }

    public String getProjectName() {
        return this.m_ProjectName;
    }
}
