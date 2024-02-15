package uk.co.ecsitsolutions.oneM2Mrepo;

import java.util.HashMap;
import java.util.Map;

public class OneM2MWrapper {



    public static Map<String, Object> createOneM2MWrapper(String m2mObjectType, Object m2mModel) {
        Map<String, Object> oneM2MObject;
        oneM2MObject = new HashMap<String, Object>();
        oneM2MObject.put(m2mObjectType, m2mModel);
        return oneM2MObject;
    }
}
