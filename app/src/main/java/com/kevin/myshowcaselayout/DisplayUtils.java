package com.kevin.myshowcaselayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 */
public class DisplayUtils
{

	/**
	 * 获取当前显示的分辨率的情况
	 *
	 * @return
	 */
	@SuppressLint("NewApi")
	public static DisplayMetrics getDisplaySize(Context content)
	{
		return content.getResources().getDisplayMetrics();
	}
}
