package com.android.launcher3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.yline.base.BaseEditText;

public class FolderEditText extends BaseEditText
{
    private Folder mFolder;
    
    public FolderEditText(Context context)
    {
        super(context);
    }
    
    public FolderEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public FolderEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public void setFolder(Folder folder)
    {
        mFolder = folder;
    }
    
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        // Catch the back button on the soft keyboard so that we can just close the activity
        if (event.getKeyCode() == android.view.KeyEvent.KEYCODE_BACK)
        {
            mFolder.doneEditingFolderName(true);
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
