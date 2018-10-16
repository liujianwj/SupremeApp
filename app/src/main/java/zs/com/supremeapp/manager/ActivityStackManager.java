package zs.com.supremeapp.manager;

import android.app.Activity;
import android.util.Log;

import java.util.ListIterator;
import java.util.Stack;

/**
 * Created by liujian on 2018/10/15.
 */

public class ActivityStackManager {

    private static final String TAG="activity_manager";
    private static Stack<Activity> activityStack;
    private static ActivityStackManager instance;

    public static ActivityStackManager getInstance() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {
                    instance = new ActivityStackManager();
                    activityStack = new Stack<>();
                }
            }
        }
        return instance;
    }

    /**
     * 添加activity入栈
     * @param activity
     */
    public  void addStackActivity(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 结束指定activity
     * @param cls
     */
    public void popActivity(Activity cls) {
        try {
            ListIterator<Activity> listIterator = activityStack.listIterator();
            while (listIterator.hasNext()) {
                Activity activity = listIterator.next();
                if (activity.hashCode() ==cls.hashCode()) {
                    listIterator.remove();
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 销毁除了activity和AppSplash以外的activity
     *
     * @param cls
     */
    // TODO: 2018/4/26 接入登录后删除该方法
    public void finishActivityExcept(Class cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(activityStack.firstElement().getClass()) || activity.getClass().equals(cls)) {
                    continue;
                }
                activity.finish();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 销毁除了AppSplash所有的Activity.
     */
    public void finishAllLiveActivity() {
        try {
            for (Activity activity : activityStack) {
                if (!activity.getClass().getSimpleName().equals(activityStack.firstElement().getClass().getSimpleName())) {
                    activity.finish( );
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     *  销毁所有的activity
     */
    public void finishAllActivity() {
        try {
            for (Activity activity : activityStack) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void popTopActivity() {
        try {
            Activity activity = activityStack.lastElement();
            popActivity(activity);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
