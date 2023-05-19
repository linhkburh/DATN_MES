package frwk.utils;



import java.util.HashMap;
import java.util.Map;


public class ApplicationContext {
    public static final String USER = "user_dang_nhap";
    public static final String USER_RIGHT = "Quyen_user_dang_nhap";
    public static final String SYS_RIGHT = "Quyen_khai_bao_trong_db";
    public static final String MNU_GROUP = "Nhom menu";
    public static final String TEN_DANG_NHAP = "user_dang_nhap";
    public static final String APPLICATIONCONTEXT = "appContext";
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public Object setAttribute(String key, Object value) {
        return attributes.put(key, value);
    }

    public Object getAttribute(String key) {

        return attributes.get(key);
    }
}
