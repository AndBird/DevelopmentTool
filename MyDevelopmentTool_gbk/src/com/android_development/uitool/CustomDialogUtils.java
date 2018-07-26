package com.android_development.uitool;

import com.development.android.tool.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
		if(context == null){
			return null;
		}
		return showProgressDialog(context, null);
	}
	
	public static AlertDialog showProgressDialog(Context context, String msg){
		if(context == null){
			return null;
		}
		AlertDialog dialog = null;
		try{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        dialog = builder.create();
	        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_progressdialog_layout, null);
	        TextView textView = (TextView) view.findViewById(R.id.progressBar_text);
	        if(msg != null){
	        	textView.setText(msg);
	        }
	        
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
		if(context == null){
			return null;
		}
		Resources res = context.getResources();
		return showProgressDialog(context, res.getString(resId));
	}

	 public static void hideDialog(AlertDialog dialog){
    	if(dialog != null){
    		dialog.dismiss();
    		dialog = null;
    	}
	}
	 
	 
	public static AlertDialog showCustomDialog(Context context, String title, String msgStr, final CustomDialogClickListener listener){
		if(context == null){
			return null;
		}
		return showCustomDialog(context, title, msgStr, null, null, listener);
	}
	    
	public static AlertDialog showCustomDialog(Context context, int titleRes, int msgStrRes, final CustomDialogClickListener listener){
		if(context == null){
			return null;
		}
		Resources res = context.getResources();
		return showCustomDialog(context, res.getString(titleRes), res.getString(msgStrRes), null, null, listener);
	}
	
	
	public static AlertDialog showCustomDialog(Context context, String title, String msgStr, String cancel_button, String ok_button, final CustomDialogClickListener listener){
		if(context == null){
			return null;
		}
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView cancel = (TextView) view.findViewById(R.id.dlg_cancel);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(title);
				msgView.setText(msgStr);
				if(cancel_button != null){
					cancel.setText(cancel_button);
				}
				if(ok_button != null){
					confirm.setText(ok_button);
				}
				
				final AlertDialog tmpDialog = dialog;
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onCancelClick(v, tmpDialog);
						}else{
							if(tmpDialog != null){
								tmpDialog.dismiss();
							}
						}
					}
				});
				
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onOkClick(v, tmpDialog);
						}else{
							if(tmpDialog != null){
								tmpDialog.dismiss();
							}
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
		if(context == null){
			return null;
		}
		Resources res = context.getResources();
		return showCustomDialog(context, res.getString(titleRes), res.getString(msgStrRes), res.getString(cancel_button_res), res.getString(ok_button_res), listener);
	}
	
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, String title, String msgStr, String button, final CustomDialogSingleClickListener listener){
		if(context == null){
			return null;
		}
		AlertDialog dialog = null;
		try {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				dialog = builder.create();
				View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_single_button_layout, null);
				final TextView titleView = (TextView) view.findViewById(R.id.dlg_title);
				final TextView msgView = (TextView) view.findViewById(R.id.dlg_msg);
				final TextView confirm = (TextView) view.findViewById(R.id.dlg_confirm);

				titleView.setText(title);
				msgView.setText(msgStr);
				if(button != null){
					confirm.setText(button);
				}
				
				final AlertDialog tmpDialog = dialog;
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(listener != null){
							listener.onClick(v, tmpDialog);
						}else{
							if(tmpDialog != null){
								tmpDialog.dismiss();
							}
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
		if(context == null){
			return null;
		}
		Resources res = context.getResources();
		return showSingleButtonCustomDialog(context, res.getString(titleRes), res.getString(msgStrRes), res.getString(button_res), listener);
	}
	
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, String title, String msgStr, final CustomDialogSingleClickListener listener){
		if(context == null){
			return null;
		}
		return showSingleButtonCustomDialog(context, title, msgStr, null, listener);
	}
	 
	
	public static AlertDialog showSingleButtonCustomDialog(Context context, int titleRes, int msgStrRes, final CustomDialogSingleClickListener listener){
		if(context == null){
			return null;
		}
		Resources res = context.getResources();
		return showSingleButtonCustomDialog(context, res.getString(titleRes), res.getString(msgStrRes), null, listener);
	}
}
