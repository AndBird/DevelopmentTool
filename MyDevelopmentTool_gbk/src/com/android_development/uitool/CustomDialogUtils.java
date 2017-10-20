package com.android_development.uitool;

import com.development.android.tool.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialogUtils {
	private static final String TAG = CustomDialogUtils.class.getName();
	
	public interface CustomDialogSingleClickListener{
		void onClick(View v, AlertDialog dialog);
	}
	
	public interface CustomDialogClickListener{
		void onOkClick(View v, AlertDialog dialog);
		void onCancelClick(View v, AlertDialog dialog);
	}
	
	public static AlertDialog showProgressDialog(Context context){
		AlertDialog dialog = null;
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        dialog = builder.create();
	        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_progressdialog_layout, null);
	        TextView textView = (TextView) view.findViewById(R.id.progressBar_text);
	        textView.setVisibility(View.GONE);
	        
	        dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        dialog.show();
	
	        Window window = dialog.getWindow();
	        window.setGravity(Gravity.CENTER);
	        window.setContentView(view);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return dialog;
	}
	
	public static AlertDialog showProgressDialog(Context context, String msg){
		AlertDialog dialog = null;
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        dialog = builder.create();
	        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_progressdialog_layout, null);
	        TextView textView = (TextView) view.findViewById(R.id.progressBar_text);
	        textView.setText(msg);
	        
	        dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        dialog.show();
	
	        Window window = dialog.getWindow();
	        window.setGravity(Gravity.CENTER);
	        window.setContentView(view);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return dialog;
	}
	
	public static AlertDialog showProgressDialog(Context context, int resId){
		AlertDialog dialog = null;
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        dialog = builder.create();
	        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_progressdialog_layout, null);
	        TextView textView = (TextView) view.findViewById(R.id.progressBar_text);
	        textView.setText(resId);
	        
	        dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        dialog.show();
	
	        Window window = dialog.getWindow();
	        window.setGravity(Gravity.CENTER);
	        window.setContentView(view);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return dialog;
	}

	 public static void hideDialog(AlertDialog dialog){
    	if(dialog != null){
    		dialog.dismiss();
    		dialog = null;
    	}
	}
	 
	 
	public static AlertDialog showCustomDialog(Context context, String title, String msgStr, final CustomDialogClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView cancel = (TextView) view.findViewById(R.id.dlg_cancel);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(title);
				msgView.setText(msgStr);
				final AlertDialog tmpDialog = dialog;
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onCancelClick(v, tmpDialog);
						}
					}
				});
				
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onOkClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.CENTER);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	    
	public static AlertDialog showCustomDialog(Context context, int titleRes, int msgStrRes, final CustomDialogClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView cancel = (TextView) view.findViewById(R.id.dlg_cancel);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(titleRes);
				msgView.setText(msgStrRes);
				
				final AlertDialog tmpDialog = dialog;
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onCancelClick(v, tmpDialog);
						}
					}
				});
				
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onOkClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.CENTER);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	
	
	public static AlertDialog showCustomDialog(Context context, String title, String msgStr, String cancel_button, String ok_button, final CustomDialogClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView cancel = (TextView) view.findViewById(R.id.dlg_cancel);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(title);
				msgView.setText(msgStr);
				cancel.setText(cancel_button);
				confirm.setText(ok_button);
				
				final AlertDialog tmpDialog = dialog;
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onCancelClick(v, tmpDialog);
						}
					}
				});
				
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onOkClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.CENTER);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	    
	public static AlertDialog showCustomDialog(Context context, int titleRes, int msgStrRes, int cancel_button_res, int ok_button_res, final CustomDialogClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView cancel = (TextView) view.findViewById(R.id.dlg_cancel);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(titleRes);
				msgView.setText(msgStrRes);
				cancel.setText(cancel_button_res);
				confirm.setText(ok_button_res);
				
				final AlertDialog tmpDialog = dialog;
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onCancelClick(v, tmpDialog);
						}
					}
				});
				
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onOkClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.NO_GRAVITY);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, String title, String msgStr, String button, final CustomDialogSingleClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_single_button_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(title);
				msgView.setText(msgStr);
				confirm.setText(button);
				
				final AlertDialog tmpDialog = dialog;
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.CENTER);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	 
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, int titleRes, int msgStrRes, int button_res, final CustomDialogSingleClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_single_button_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(titleRes);
				msgView.setText(msgStrRes);
				confirm.setText(button_res);
				
				final AlertDialog tmpDialog = dialog;
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.NO_GRAVITY);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, String title, String msgStr, final CustomDialogSingleClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_single_button_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(title);
				msgView.setText(msgStr);
				
				final AlertDialog tmpDialog = dialog;
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.CENTER);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
	 
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, int titleRes, int msgStrRes, final CustomDialogSingleClickListener listener){
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_dialog_single_button_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(titleRes);
				msgView.setText(msgStrRes);
				
				final AlertDialog tmpDialog = dialog;
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onClick(v, tmpDialog);
						}
					}
				});
				
				dialog.show();
				Window window = dialog.getWindow();
			    window.setGravity(Gravity.NO_GRAVITY);
			    window.setContentView(view);
			} catch (Exception e) {
				dialog = null;
				e.printStackTrace();
			}
			return dialog;
		}
}
