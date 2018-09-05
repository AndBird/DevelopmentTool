package com.status.systembartool;

import android.view.Window;

/**
 * ×´Ì¬À¸½Ó¿Ú
 *
 * @version 0.3
 * @since 0.3
 */

interface IStatusBar {
    /**
     * Set the status bar color
     *
     * @param window The window to set the status bar color
     * @param color  Color value
     */
    void setStatusBarColor(Window window, int color);
}
