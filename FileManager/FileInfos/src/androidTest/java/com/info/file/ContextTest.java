package com.info.file;

import android.test.AndroidTestCase;

import com.info.file.application.IApplication;
import com.info.file.helper.FileLoadRunnable;
import com.yline.application.task.PriorityRunnable;

/**
 * Created by yline on 2017/1/27.
 */
public class ContextTest extends AndroidTestCase
{
	public void testLoadRunnable()
	{
		FileLoadRunnable runnable = new FileLoadRunnable();
		IApplication.start(runnable, PriorityRunnable.Priority.UI_LOW);
	}
}
