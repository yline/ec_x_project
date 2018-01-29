package com.yline.file.module.file.helper;

import android.os.AsyncTask;

import com.yline.file.module.db.DbFileBeanManager;
import com.yline.file.module.file.model.FileModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileDbLoader extends AsyncTask<String, Void, List<FileModel>> {
    private FileHelper.LoadListener loadListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<FileModel> doInBackground(String... params) {
        String path = params[0];
        LogFileUtil.v(path);

        List<FileModel> resultList = new ArrayList<>();

        final File pathDir = new File(path);

        final File[] dirs = pathDir.listFiles(FileUtil.getsDirFilter());
        if (null != dirs) {
            Arrays.sort(dirs, FileUtil.getsComparator());

            for (File dirFile : dirs) {
                FileModel bean = DbFileBeanManager.getInstance().queryByAbsolutePath(dirFile.getAbsolutePath());
                // 更新数据
                if (null == bean) {
                    bean = new FileModel(dirFile.getName(), dirFile.getAbsolutePath(),
                            dirFile.listFiles(FileUtil.getsDirFilter()).length,
                            dirFile.listFiles(FileUtil.getsFileFilter()).length,
                            FileSizeUtil.getFileOrDirAutoSize(dirFile));

                    DbFileBeanManager.getInstance().insert(bean);
                }
                resultList.add(bean);
            }
        }

        final File[] files = pathDir.listFiles(FileUtil.getsFileFilter());
        if (null != files) {
            Arrays.sort(files, FileUtil.getsComparator());

            for (File file : files) {
                FileModel bean = DbFileBeanManager.getInstance().queryByAbsolutePath(file.getAbsolutePath());
                // 更新数据
                if (null == bean) {
                    bean = new FileModel(file.getName(), file.getAbsolutePath(), FileSizeUtil.getFileOrDirAutoSize(file));
                    DbFileBeanManager.getInstance().insert(bean);
                }
                resultList.add(bean);
            }
        }

        return resultList;
    }

    @Override
    protected void onPostExecute(List<FileModel> been) {
        super.onPostExecute(been);

        callLoadListener(been);
    }

    public void setLoadListener(FileHelper.LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    private void callLoadListener(List<FileModel> fileBeanList) {
        if (null != loadListener) {
            loadListener.onLoadFinish(fileBeanList);
        }
    }
}
