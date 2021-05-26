package  com.xzy.forestSystem.gogisapi.Common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XHashMap {
    private List<HashValueObject> m_HashMap = new ArrayList();

    public void AddValue(String paramString, HashValueObject paramHashValueObject) {
        Iterator localIterator = this.m_HashMap.iterator();
        while (true) {
            if (localIterator.hasNext()) {
                HashValueObject localHashValueObject = (HashValueObject) localIterator.next();
                if (localHashValueObject.Key.equals(paramString)) {
                    this.m_HashMap.remove(localHashValueObject);
                    break;
                }
            } else {
                break;
            }
        }
        paramHashValueObject.Key = paramString;
        this.m_HashMap.add(paramHashValueObject);
    }

    public void Delete(String paramString) {
        for (HashValueObject localHashValueObject : this.m_HashMap) {
            if (localHashValueObject.Key.equals(paramString)) {
                this.m_HashMap.remove(localHashValueObject);
                return;
            }
        }
    }

    public List<String> GetStringList() {
        ArrayList localArrayList = new ArrayList();
        for (HashValueObject localHashValueObject : this.m_HashMap) {
            if (localHashValueObject.ShowOnMap) {
                localArrayList.add(localHashValueObject.LabelText);
            }
        }
        return localArrayList;
    }

    public HashValueObject GetValue(String paramString, boolean paramBoolean) {
        for (HashValueObject localHashValueObject1 : this.m_HashMap) {
            if (localHashValueObject1.Key.equals(paramString)) {
                return localHashValueObject1;
            }
        }
        if (!paramBoolean) {
            return null;
        }
        HashValueObject localHashValueObject2 = new HashValueObject();
        localHashValueObject2.Key = paramString;
        this.m_HashMap.add(localHashValueObject2);
        return localHashValueObject2;
    }

    public HashValueObject GetValue(String paramString) {
        return GetValue(paramString, false);
    }
}
