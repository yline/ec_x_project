package com.info.file;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.info.file.application.IApplication;
import com.info.file.helper.FileLoadRunnable;
import com.yline.application.task.PriorityRunnable;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application>
{
	public ApplicationTest()
	{
		super(Application.class);
	}

	public void testLoadRunnable()
	{
		FileLoadRunnable runnable = new FileLoadRunnable();
		IApplication.start(runnable, PriorityRunnable.Priority.UI_LOW);
	}
}