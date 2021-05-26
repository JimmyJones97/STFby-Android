package  com.xzy.forestSystem.gogisapi.Display;

import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TextPositionAdjust {
    private List<RectangleF> TextRectangleList = new ArrayList();
    private List<String> TextUIDList = new ArrayList();

    private void AddRectangle(RectangleF TextRectangle, String UID) {
        this.TextRectangleList.add(TextRectangle);
        this.TextUIDList.add(UID);
    }

    public void AddRectangle(int ScreenX, int ScreenY) {
        AddRectangle(new RectangleF(((float) ScreenX) - 3.0f, ((float) ScreenY) - 3.0f, 3.0f * 2.0f, 2.0f * 3.0f), UUID.randomUUID().toString());
    }

    public void AdjustPosition(RectangleF newRectangle, String UID, float PointX, float PointY, BasicValue paramX, BasicValue paramY) {
        float X = newRectangle.f467X;
        float Y = newRectangle.f468Y;
        int index = this.TextUIDList.indexOf(UID);
        if (index >= 0) {
            X = this.TextRectangleList.get(index).f467X;
            Y = this.TextRectangleList.get(index).f468Y;
        } else {
            boolean flag = true;
            RectangleF rect = new RectangleF();
            int i = 0;
            while (true) {
                if (i > 7) {
                    break;
                }
                flag = true;
                rect = SetPosition(newRectangle, i, PointX, PointY);
                Iterator<RectangleF> it = this.TextRectangleList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().IntersectsWith(rect)) {
                            flag = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (flag) {
                    X = rect.f467X;
                    Y = rect.f468Y;
                    break;
                }
                i++;
            }
            if (flag) {
                AddRectangle(rect, UID);
            } else {
                AddRectangle(newRectangle, UID);
            }
        }
        paramX.setValue((double) X);
        paramY.setValue((double) Y);
    }

    public void ClearPosition() {
        this.TextRectangleList.clear();
        this.TextUIDList.clear();
    }

    private RectangleF SetPosition(RectangleF RF, int Type, float PointX, float PointY) {
        RectangleF ef = new RectangleF(RF.f467X, RF.f468Y, RF.Width, RF.Height);
        float num = ef.f467X - PointX;
        switch (Type) {
            case 1:
                ef.f467X = (ef.f467X - ef.Width) - ((ef.f467X - PointX) * 2.0f);
                break;
            case 2:
                ef.f468Y = (ef.f468Y - (ef.Height / 2.0f)) - num;
                ef.f467X -= ef.Width / 2.0f;
                break;
            case 3:
                ef.f468Y = ef.f468Y + (ef.Height / 2.0f) + num;
                ef.f467X -= ef.Width / 2.0f;
                break;
            case 4:
                ef.f468Y = (ef.f468Y - (ef.Height / 2.0f)) - num;
                break;
            case 5:
                ef.f468Y = ef.f468Y + (ef.Height / 2.0f) + num;
                break;
            case 6:
                ef.f468Y = (ef.f468Y - (ef.Height / 2.0f)) - num;
                ef.f467X = (ef.f467X - ef.Width) - ((ef.f467X - PointX) * 2.0f);
                break;
            case 7:
                ef.f468Y = ef.f468Y + (ef.Height / 2.0f) + num;
                ef.f467X = (ef.f467X - ef.Width) - ((ef.f467X - PointX) * 2.0f);
                break;
        }
        return ef;
    }
}
