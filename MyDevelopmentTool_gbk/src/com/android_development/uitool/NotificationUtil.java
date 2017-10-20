package com.android_development.uitool;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


/**
 * ��Ϣ������ 3.0��ǰ����Notification�����õ���Notification.Builder
 * 3.0֮�󹹽�Notification�����õ���NotificationCompat.Builder
 * 
 * ֧��:��Ҫandroid-support-v4.jar
 * 
 */
public class NotificationUtil {
		/**
		 * ������׼Notification
		 * 
		 * @param context
		 * @param title
		 *            ��Ϣ�ı���
		 * @param content
		 *            ��Ϣ������
		 * @param ticker
		 *            ��һ�γ�������Ļ�Ϸ���֪ͨ��ʾ
		 * @param num
		 *            ��Ϣ����
		 * @param sIcon
		 *            ��Ϣ��Сͼ��
		 * @param bIcon
		 *            ��Ϣ�Ĵ�ͼ��
		 * @param cls
		 *            �����Ľ���
		 */
		public static void setNormalNotification(Context context, String title,
				String content, String ticker, int num, int sIcon, Bitmap bIcon,
				Class<?> cls) {
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
			mBuilder.setContentTitle(title);
			mBuilder.setContentText(content);
			mBuilder.setNumber(num);
			mBuilder.setSmallIcon(sIcon);
			mBuilder.setLargeIcon(bIcon);
			// ��һ����ʾ��Ϣ��ʱ����ʾ��֪ͨ����
			mBuilder.setTicker(ticker);
			// �Լ�ά��֪ͨ����ʧ
			mBuilder.setAutoCancel(true);
	
			Intent intent = new Intent(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mBuilder.setContentIntent(pendingIntent);
			manager.notify(0, mBuilder.build());
		}
	
		/**
		 * ��������ͼNotification
		 * 
		 * @param context
		 * @param title
		 *            ��Ϣ�ı���
		 * @param content
		 *            ��Ϣ������
		 * @param lines
		 *            ����ͼ��ʾ�����ı�
		 * @param ticker
		 *            ��һ�γ�������Ļ�Ϸ���֪ͨ��ʾ
		 * @param num
		 *            ��Ϣ����
		 * @param sIcon
		 *            ��Ϣ��Сͼ��
		 * @param bIcon
		 *            ��Ϣ�Ĵ�ͼ��
		 * @param cls
		 *            �����Ľ���
		 */
		public static void setBigNotification(Context context, String title,
				String content, String[] lines, String ticker, int num, int sIcon,
				Bitmap bIcon, Class<?> cls) {
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
			mBuilder.setNumber(num);
			mBuilder.setSmallIcon(sIcon);
			mBuilder.setLargeIcon(bIcon);
			// ��һ����ʾ��Ϣ��ʱ����ʾ��֪ͨ����
			mBuilder.setTicker(ticker);
			// �Լ�ά��֪ͨ����ʧ
			mBuilder.setAutoCancel(true);
	
			/**
			 * ��׿16���ϲ�֧�ִ���ͼNotification
			 * NotificationCompat.BigPictureStyle, ��ϸ�ڲ�����ʾһ��256dp�߶ȵ�λͼ��
			 * NotificationCompat.BigTextStyle����ϸ�ڲ�����ʾһ������ı��顣
			 * NotificationCompat.InboxStyle����ϸ�ڲ�����ʾһ�����ı���
			 * 
			 * ���������ʾһ��ͼƬ��ʹ��BigPictureStyle�����ģ� �����Ҫ��ʾһ�����ı���Ϣ�������ʹ��BigTextStyle��
			 * �������������ʾһ���ı�����Ϣ����ôʹ��InboxStyle���ɡ�
			 */
			NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
			for (String line : lines) {
				style.addLine(line);
			}
			style.setBigContentTitle(title);
			style.setSummaryText(content);
			mBuilder.setStyle(style);
			
//			if(Build.VERSION.SDK_INT >= 16){
//				//��ͼƬ
//				NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
//				style.setBigContentTitle(msg.text).bigPicture(bitmap);
//				builder.setStyle(style);
//			}
	
			Intent intent = new Intent(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mBuilder.setContentIntent(pendingIntent);
			manager.notify(0, mBuilder.build());
		}
	
		/**
		 * �����Զ���֪ͨ��ͼ��������
		 * 
		 * @param context
		 * @param layoutId
		 *            �Զ�����ͼID
		 * @param viewId
		 *            �Զ�����ͼ�ؼ�ID
		 * @param title
		 * @param content
		 * @param ticker
		 * @param sIcon
		 * @param cls
		 */
		public static void setViewNotification(Context context, int layoutId,
				int viewId, String title, String content, String ticker, int sIcon,
				Class<?> cls) {
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
			RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
			views.setImageViewResource(viewId, sIcon);
			views.setTextViewText(viewId, title);
			views.setTextViewText(viewId, content);
	
			mBuilder.setContent(views);
			// ��һ����ʾ��Ϣ��ʱ����ʾ��֪ͨ����
			mBuilder.setTicker(ticker);
			// �Լ�ά��֪ͨ����ʧ
			mBuilder.setAutoCancel(true);
	
			Intent intent = new Intent(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mBuilder.setContentIntent(pendingIntent);
			manager.notify(0, mBuilder.build());
		}
}
