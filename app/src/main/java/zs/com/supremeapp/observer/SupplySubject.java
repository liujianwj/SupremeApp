package zs.com.supremeapp.observer;

import java.util.Map;


/**
 * 供应链观察者（不支持跨线程）
 * Created by 首乌 on 2017/3/4.
 */

public class SupplySubject extends Subject {
    private static SupplySubject supplySubject;

    private SupplySubject(){}

    public static SupplySubject getInstance(){
        if(supplySubject == null){
            synchronized (SupplySubject.class){
                if(supplySubject == null){
                    supplySubject = new SupplySubject();
                }
            }
        }
        return supplySubject;
    }

    public void change(Map<String, Object> params, String key){
        //状态发生改变，通知各个观察者
        this.notifyObservers(params, key);
    }
}
