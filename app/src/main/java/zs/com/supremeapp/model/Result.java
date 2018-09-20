package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/9/4.
 */

public class Result<T> {
    private int flag;
    private String msg;
    private T data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
