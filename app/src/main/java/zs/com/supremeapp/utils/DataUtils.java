package zs.com.supremeapp.utils;

import java.util.List;

/**
 * Created by liujian on 2018/9/7.
 */

public class DataUtils {

    public static <T> boolean isListEmpty(List<T> list){
        return (list == null || list.isEmpty());
    }

    public static String nullToEmpty(String value){
        if(value == null){
            return "";
        }
        return value;
    }

}
