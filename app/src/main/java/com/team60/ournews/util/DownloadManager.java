package com.team60.ournews.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Misutesu on 2017/4/21 0021.
 */

public class DownloadManager {

    public interface DownloadManagerListener {
        void onStart();

        void onProgress(int progress);

        void onError(Throwable t, File file);

        void onSuccess(File file);

        void onComplete();
    }

    private static Map<String, DownloadTask> downloadList;

    public static void init() {
        if (downloadList == null) {
            synchronized (DownloadManager.class) {
                if (downloadList == null) {
                    downloadList = new HashMap<>();
                }
            }
        }
    }

    public static boolean isDownload(String taskName) {
        checkInit();
        if (taskName == null) return false;
        DownloadTask task = downloadList.get(taskName);
        return task != null;
    }

    public static void stopTask(String taskName) {
        checkInit();
        if (taskName == null) return;
        DownloadTask task = downloadList.get(taskName);
        if (task != null) {
            task.cancel();
            downloadList.remove(taskName);
        }
    }

    public static void stopAllTask() {
        checkInit();
        for (String key : downloadList.keySet()) {
            downloadList.get(key).cancel();
        }
        downloadList.clear();
    }

    private static void checkInit() {
        if (downloadList == null)
            throw new UnsupportedOperationException("No Init DownloadManager");
    }

    public static DownloadTask create(@Nullable String url, @Nullable File file
            , @Nullable DownloadManagerListener mDownloadManagerListener) {
        checkInit();
        return new DownloadTask(url, file, mDownloadManagerListener);
    }

    public static class DownloadTask {

        private InputStream is;
        private FileOutputStream fos;

        private String url;
        private File file;
        private DownloadManagerListener mDownloadManagerListener;

        private Disposable mDisposable;
        private boolean isCancel;

        private DownloadTask(String url, File file, DownloadManagerListener downloadManagerListener) {
            this.url = url;
            this.file = file;
            mDownloadManagerListener = downloadManagerListener;
        }

        public String start() {
            getDisposable();
            String taskName = url + System.currentTimeMillis();
            downloadList.put(taskName, this);
            return taskName;
        }

        private void cancel() {
            if (mDisposable != null) mDisposable.dispose();
            this.isCancel = true;
        }

        private void getDisposable() {
            mDisposable = Flowable.create(new FlowableOnSubscribe<Integer>() {
                @Override
                public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                    Request request = new Request.Builder()
                            .header("Accept-Encoding", "identity")
                            .url(url)
                            .build();
                    OkHttpClient client = OkHttpUtil.getOkHttpClientForDownload();
                    Response response = client.newCall(request).execute();
                    byte[] buf = new byte[2048];
                    int len;
                    long sum = 0;
                    is = response.body().byteStream();
                    long allLength = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    int oldProgress = -1;
                    while ((len = is.read(buf)) != -1) {
                        if (!isCancel) {
                            sum += len;
                            fos.write(buf, 0, len);
                            if (allLength != -1) {
                                int progress = (int) (100 * sum / allLength);
                                if (oldProgress != progress) {
                                    oldProgress = progress;
                                    emitter.onNext(progress);
                                }
                            }
                        } else {
                            emitter.onComplete();
                            return;
                        }
                    }
                    emitter.onNext(-1);
                    emitter.onComplete();
                }
            }, BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<Integer>() {
                        @Override
                        protected void onStart() {
                            mDownloadManagerListener.onStart();
                            request(1);
                        }

                        @Override
                        public void onNext(Integer progress) {
                            request(1);
                            if (progress != -1) {
                                mDownloadManagerListener.onProgress(progress);
                            } else {
                                mDownloadManagerListener.onSuccess(file);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            mDownloadManagerListener.onError(t, file);
                            onComplete();
                        }

                        @Override
                        public void onComplete() {
                            mDownloadManagerListener.onComplete();
                            if (fos != null) {
                                try {
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }
}
