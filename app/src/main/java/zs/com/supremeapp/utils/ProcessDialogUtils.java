package zs.com.supremeapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import zs.com.supremeapp.R;


public class ProcessDialogUtils {

	/**
	 * 加载类型，通用型(隐藏文字)
	 */
	public static final int LOAD_TYPE_COMMON = 0;
	/**
	 * 加载类型，通用型(显示文字)
	 */
	public static final int LOAD_TYPE_COMMON_SHOW = 1;

	//提示框展现时间
	public static final long DEFAULT_SHOW_TIME = 15 * 1000;

	private Dialog loadingDialog;
	private Context context ;
	private Handler mTimeHandler = new Handler();
	private TimeRunnable mTimeRunnable;
	private  boolean isSetSystemInputShow;
	public ProcessDialogUtils(Context context) {
		this.context = context;
	}


	public void createAndShow(String msg) {
		createAndShow(msg, false, LOAD_TYPE_COMMON, DEFAULT_SHOW_TIME, false);

	}

	public void createAndShow(String msg, int loadType, long duration, boolean isSetSystemInputShow, boolean stopAble){
		this.isSetSystemInputShow=isSetSystemInputShow;
		createAndShow(msg, false, loadType, duration, stopAble);
	}

	/**
	 *
	 * @param msg  提示文本
	 * @param cancelable  是否可取消
	 * @param loadType  loading视图类型
	 * @param duration  展现时常，超过时常消失
	 * @param stopAble  是否可点击物理返回键强制关闭
	 */
	public void createAndShow(String msg, boolean cancelable, int loadType, long duration, boolean stopAble){
		switch (loadType){
			case LOAD_TYPE_COMMON:
				createCommonDialog(cancelable, duration);
				break;
			case LOAD_TYPE_COMMON_SHOW:
				createCommonShowDialog(msg, cancelable, duration);
				break;
		}
		if (stopAble) {
			setStopAble();
		}
	}

	private void createCommonDialog(boolean cancelable, long duration){
		if(isValidContext(context)){
			dismiss();
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.customprogressdialog_view, null);// 得到加载view
			LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
			// main.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
			TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					context, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);

			loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
			loadingDialog.setCancelable(cancelable);
			loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
			//设置dialog和系统软键盘共存
			if (isSetSystemInputShow) {
				loadingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			}
			tipTextView.setVisibility(View.GONE);// 隐藏加载信息
			loadingDialog.show();
			postTimeRunnable(duration);
		}
	}

	private void createCommonShowDialog(String msg, boolean cancelable, long duration){
		if(isValidContext(context)){
			dismiss();
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.customprogressdialog_view, null);// 得到加载view
			LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
			// main.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
			TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					context, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);

			loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
			loadingDialog.setCancelable(cancelable);
			loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

			tipTextView.setVisibility(View.VISIBLE);// 显示加载信息
			tipTextView.setText(msg);// 设置加载信息
			loadingDialog.show();
			postTimeRunnable(duration);
		}
	}

	/**
	 * 消失.
	 */
	public void dismissDialog() {
		if (loadingDialog != null && context!=null && loadingDialog.isShowing()) {
//			if(isValidContext(context)){
				removeTimeRunnable();
				loadingDialog.dismiss();
//			}
		}
	}

	private void dismiss(){
		if (loadingDialog != null && context!=null && loadingDialog.isShowing()) {
			removeTimeRunnable();
			loadingDialog.dismiss();
		}
	}

	private void postTimeRunnable(long duration){
		if(duration > 0){
			if(mTimeRunnable == null){
				mTimeRunnable = new TimeRunnable(this);
			}
			mTimeHandler.postDelayed(mTimeRunnable, duration);
		}
	}

	private void removeTimeRunnable(){
		if(mTimeHandler != null && mTimeRunnable != null){
			mTimeHandler.removeCallbacks(mTimeRunnable);
		}
	}

	private static boolean  isValidContext (Context c){
		Activity a = (Activity)c;
		if(Build.VERSION.SDK_INT >= 17){
			if (a.isDestroyed() || a.isFinishing()){
				return false;
			}else{
				return true;
			}
		}else{
			if (a.isFinishing()){
				return false;
			}else{
				return true;
			}
		}
	}

	/**
	 * 可以点击物理返回键.
	 */
	public void setStopAble() {
		if (loadingDialog != null && context!=null) {
			if(isValidContext(context)) {
				loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
							dismissDialog();
							Activity activity = (Activity)context;
							activity.finish();
						}
						return false;
					}
				});
			}
		}
	}


	private static class TimeRunnable implements Runnable{

		WeakReference<ProcessDialogUtils> weakReference;

		public TimeRunnable(ProcessDialogUtils processDialogUtils) {
			this.weakReference = new WeakReference<>(processDialogUtils);
		}

		@Override
		public void run() {
			ProcessDialogUtils processDialogUtils = weakReference.get();
			if(processDialogUtils == null){
				return;
			}
			processDialogUtils.dismissDialog();
		}
	}
}

