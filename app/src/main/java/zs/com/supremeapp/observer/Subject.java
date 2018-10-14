package zs.com.supremeapp.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zs.com.supremeapp.observer.Observer;

/**
 * Created by 首乌 on 2017/3/4.
 */

public abstract class Subject {

    /**
     * 用来保存注册的观察者对象
     */
    private List<Observer> list = new ArrayList<Observer>();
    /**
     * 注册观察者对象
     * @param observer    观察者对象
     */
    public void attach(Observer observer){
        if(!list.contains(observer)){
            list.add(observer);
        }
    }
    /**
     * 删除观察者对象
     * @param observer    观察者对象
     */
    public void detach(Observer observer){
        list.remove(observer);
    }
    /**
     * 通知所有注册的观察者对象
     */
    public void notifyObservers(Map<String, Object> params, String key){
        for(Observer observer : list){
            observer.update(params, key);
        }
    }

}
