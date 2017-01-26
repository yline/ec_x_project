package com.info.file.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.info.file.R;
import com.info.file.bean.FileInfos;
import com.yline.base.common.CommonListAdapter;

/**
 * Created by yline on 2017/1/23.
 */
public class FileListAdapter extends CommonListAdapter<FileInfos>
{
	public FileListAdapter(Context context)
	{
		super(context);
	}

	@Override
	public int getCount()
	{
		if (null == sList)
		{
			return 0;
		}
		return super.getCount();
	}

	@Override
	protected int getItemRes(int i)
	{
		return R.layout.item_file_list;
	}

	@Override
	protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
	{
		viewHolder.get(R.id.iv_type).setBackgroundResource(sList.get(i).getImageResId());
		viewHolder.setText(R.id.tv_name, sList.get(i).getFileName());
		viewHolder.setText(R.id.tv_info, sList.get(i).getFileMessage());
	}
}
