package zs.com.supremeapp.network;

public interface INetWorkCallback<T> {

    void success(T t, Object... objects);

    void failure(int errorCode, String message);
}
