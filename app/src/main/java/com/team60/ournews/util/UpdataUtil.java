package com.team60.ournews.util;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.team60.ournews.R;
import com.team60.ournews.module.model.CheckUpdateResult;
import com.team60.ournews.module.ui.dialog.DownloadDialog;
import com.team60.ournews.module.ui.dialog.UpdateDialog;

import java.io.File;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.team60.ournews.common.Constants.SHARED_PREFERENCES_VERSION;

/**
 * Created by Administrator
 * Created on 2017/5/18 15:34
 */

public class UpdataUtil {
    private static UpdataUtil updataUtil;

    private final int NOTIFY_ID = 101;

    private String UPDATA_TASK_NAME = "";
    private NotificationManager mManager;
    private NotificationCompat.Builder mBuilder;
    private DownloadDialog mDownloadDialog;

    public static UpdataUtil newInstance() {
        if (updataUtil == null) {
            synchronized (UpdataUtil.class) {
                if (updataUtil == null) {
                    updataUtil = new UpdataUtil();
                }
            }
        }
        return updataUtil;
    }

    public void showUpdataDialog(final Context context, final CheckUpdateResult result) {
        if (mManager == null)
            mManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int nowVersion = MyUtil.getVersionCode(context);
        if (nowVersion != -1) {
            final SharedPreferences versionSP = MyUtil.getSharedPreferences(context, SHARED_PREFERENCES_VERSION);
            if (nowVersion < result.getData().getNowVersion()
                    && versionSP.getInt("IgnoreVersion", -1) != result.getData().getNowVersion()) {
                final boolean isForced = nowVersion < result.getData().getMinVersion();
                UpdateDialog.create(context, isForced)
                        .setUpdateInfo(result)
                        .setOnClickListener(new UpdateDialog.OnClickListener() {
                            @Override
                            public void onUpdateClick() {
                                if (!DownloadManager.isDownload(UPDATA_TASK_NAME)) {
                                    downloadNewVersion(context, result.getData().getFileName(), isForced);
                                }
                            }

                            @Override
                            public void onIgnoreClick() {
                                versionSP.edit().putInt("IgnoreVersion", result.getData().getNowVersion()).apply();
                            }
                        })
                        .show();
            }
        }
    }

    public void destroy() {
        if (mManager != null) mManager.cancelAll();
        DownloadManager.stopAllTask();
    }

    private void downloadNewVersion(final Context context, String url, final boolean isForced) {
        String appName = context.getString(R.string.app_name);
        File file = context.getExternalCacheDir();
        file = new File(file, appName + File.separator + "NewVersion");
        if (FileUtil.createDir(file)) {
            file = new File(file, appName + ".apk");
            if (FileUtil.deleteFile(file)) {
                UPDATA_TASK_NAME = DownloadManager.create(url, file
                        , new DownloadManager.DownloadManagerListener() {
                            @Override
                            public void onStart() {
                                showDownloadDialog(context, isForced);
                            }

                            @Override
                            public void onProgress(int progress) {
                                if (mBuilder == null) {
                                    mDownloadDialog.setProgress(progress);
                                } else {
                                    mBuilder.setProgress(100, progress, true)
                                            .setContentText(context.getString(R.string.is_download_new_version) + " " + progress + "%");
                                    mManager.notify(NOTIFY_ID, mBuilder.build());
                                }
                            }

                            @Override
                            public void onError(Throwable t, File file) {
                                t.printStackTrace();
                                FileUtil.deleteFile(file);
                                Toast.makeText(context, context.getString(R.string.download_error), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(File file) {
                                MyUtil.installAPK(context, file);
                            }

                            @Override
                            public void onComplete() {
                                mManager.cancel(NOTIFY_ID);
                                mDownloadDialog.dismiss();
                                mBuilder = null;
                            }
                        }).start();
            }
        }
    }

    private void showDownloadDialog(final Context context, boolean isForced) {
        if (mDownloadDialog == null) {
            mDownloadDialog = DownloadDialog.create(context);
            mDownloadDialog.setOnClickListener(new DownloadDialog.OnClickListener() {
                @Override
                public void onCancelClick() {
                    DownloadManager.stopTask(UPDATA_TASK_NAME);
                }

                @Override
                public void onBackgroundClick() {
                    mDownloadDialog.dismiss();
                    showNotification(context);
                }
            });
        }
        mDownloadDialog.setForced(isForced);
        mDownloadDialog.show();
    }

    private void showNotification(Context context) {
        mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.is_download_new_version))
                .setSmallIcon(R.drawable.min_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setProgress(100, 0, true)
                .setAutoCancel(false);
        mManager.notify(NOTIFY_ID, mBuilder.build());
    }
}
