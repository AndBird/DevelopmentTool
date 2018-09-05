package com.status.systembartool;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * ����LOLLIPOP�汾
 *
 * @version 0.5
 * @since 0.3
 */

class StatusBarLollipopImpl implements IStatusBar {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setStatusBarColor(Window window, int color) {
        //ȡ������͸��״̬��,ʹ ContentView ���ݲ��ٸ���״̬��
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //��Ҫ������� flag ���ܵ��� setStatusBarColor ������״̬����ɫ
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //����״̬����ɫ
        window.setStatusBarColor(color);
    }

}
