package zs.com.supremeapp.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by liujian on 2018/8/4.
 */

public class NetWorkUtils {
    private static final String DEFAULT_NO_IP = "127.0.0.1";

    private static final String WIFI_NAME="wlan0";

    /**判断当前网络是否畅通*/
    public static boolean isNetworkActive(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 得到当前ip地址.
     * @return
     */
    public static String getLocalIpAddress() {
        String ipAddress= DEFAULT_NO_IP;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            &&!inetAddress.isLinkLocalAddress()){
                        ipAddress = inetAddress.getHostAddress().toString();
                        if(intf.getName().equals(WIFI_NAME)){
                            return ipAddress;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipAddress;
    }
}
