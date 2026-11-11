package org.cocos2dx.lib;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.Call;
import org.cocos2dx.okhttp3.Callback;
import org.cocos2dx.okhttp3.OkHttpClient;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;

public class Cocos2dxDownloader {
    private static ConcurrentHashMap _resumingSupport = new ConcurrentHashMap();
    private int _countOfMaxProcessingTasks;
    private int _id;
    private String _tempFileNameSuffix;
    private OkHttpClient _httpClient = null;
    private ConcurrentHashMap _taskMap = new ConcurrentHashMap();
    private Queue _taskQueue = new LinkedList();
    private int _runningTaskCount = 0;

    class a implements Runnable {
        final int i;
        final long j;
        final long k;
        final long l;

        a(int i, long j, long j2, long j3) {
            this.i = i;
            this.j = j;
            this.k = j2;
            this.l = j3;
        }

        @Override
        public void run() {
            Cocos2dxDownloader cocos2dxDownloader = Cocos2dxDownloader.this;
            cocos2dxDownloader.nativeOnProgress(Cocos2dxDownloader.access$000(cocos2dxDownloader), this.i, this.j, this.k, this.l);
        }
    }

    class b implements Runnable {
        final int i;
        final int j;
        final String k;
        final byte[] l;

        b(int i, int i2, String str, byte[] bArr) {
            this.i = i;
            this.j = i2;
            this.k = str;
            this.l = bArr;
        }

        @Override
        public void run() {
            Cocos2dxDownloader cocos2dxDownloader = Cocos2dxDownloader.this;
            cocos2dxDownloader.nativeOnFinish(Cocos2dxDownloader.access$000(cocos2dxDownloader), this.i, this.j, this.k, this.l);
        }
    }

    class c implements Runnable {
        String i = null;
        String j = null;
        File k = null;
        File l = null;
        long m = 0;
        final String n;
        final String o;
        final Cocos2dxDownloader p;
        final String[] q;
        final int r;

        class a implements Callback {
            a() {
            }

            @Override
            public void onFailure(Call call, IOException iOException) {
                c cVar = c.this;
                Cocos2dxDownloader.access$400(cVar.p, cVar.r, 0, iOException.toString(), null);
            }

            @Override
            public void onResponse(Call call, Response response) throws Throwable {
                Throwable th;
                FileOutputStream fileOutputStream;
                InputStream inputStreamByteStream;
                FileOutputStream fileOutputStream2;
                String str;
                byte[] bArr = new byte[4096];
                InputStream inputStream = null;
                FileOutputStream fileOutputStream3 = null;
                try {
                    try {
                        c cVar = c.this;
                        Cocos2dxDownloader.access$400(cVar.p, cVar.r, -2, response.message(), null);
                        return;
                    } catch (IOException e) {
                        Log.e("Cocos2dxDownloader", e.toString());
                        return;
                    }
                } catch (IOException e2) {
                    e = e2;
                    inputStreamByteStream = null;
                    fileOutputStream2 = null;
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = null;
                }
                if (response.code() >= 200 && response.code() <= 206) {
                    long jContentLength = response.body().contentLength();
                    if (c.this.n.length() > 0 && !Cocos2dxDownloader.access$200().containsKey(c.this.j)) {
                        if (jContentLength > 0) {
                            Cocos2dxDownloader.access$200().put(c.this.j, Boolean.TRUE);
                        } else {
                            Cocos2dxDownloader.access$200().put(c.this.j, Boolean.FALSE);
                        }
                    }
                    long j = c.this.m;
                    inputStreamByteStream = response.body().byteStream();
                    try {
                        if (inputStreamByteStream != null) {
                            inputStreamByteStream.close();
                        }
                        if (fileOutputStream3 != null) {
                            fileOutputStream3.close();
                            return;
                        }
                        return;
                    } catch (IOException e3) {
                        e = e3;
                        fileOutputStream2 = null;
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = null;
                    }
                    if (c.this.n.length() > 0) {
                        fileOutputStream2 = c.this.m > 0 ? new FileOutputStream(c.this.k, true) : new FileOutputStream(c.this.k, false);
                        while (true) {
                            try {
                                int i = inputStreamByteStream.read(bArr);
                                if (i == -1) {
                                    break;
                                }
                                long j2 = i;
                                long j3 = j + j2;
                                fileOutputStream2.write(bArr, 0, i);
                                c cVar2 = c.this;
                                fileOutputStream = fileOutputStream2;
                                long j4 = jContentLength;
                                try {
                                    Cocos2dxDownloader.access$500(cVar2.p, cVar2.r, j2, j3, jContentLength);
                                    jContentLength = j4;
                                    j = j3;
                                    fileOutputStream2 = fileOutputStream;
                                } catch (IOException e4) {
                                    e = e4;
                                    fileOutputStream2 = fileOutputStream;
                                } catch (Throwable th4) {
                                    th = th4;
                                    th = th;
                                    inputStream = inputStreamByteStream;
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e5) {
                                            Log.e("Cocos2dxDownloader", e5.toString());
                                            throw th;
                                        }
                                    }
                                    if (fileOutputStream != null) {
                                        throw th;
                                    }
                                    fileOutputStream.close();
                                    throw th;
                                }
                            } catch (IOException e6) {
                                e = e6;
                            } catch (Throwable th5) {
                                th = th5;
                                fileOutputStream = fileOutputStream2;
                            }
                            try {
                                e.printStackTrace();
                                c cVar3 = c.this;
                                Cocos2dxDownloader.access$400(cVar3.p, cVar3.r, 0, e.toString(), null);
                                if (inputStreamByteStream != null) {
                                    inputStreamByteStream.close();
                                }
                                if (fileOutputStream2 != null) {
                                    fileOutputStream2.close();
                                    return;
                                }
                                return;
                            } catch (Throwable th6) {
                                th = th6;
                                inputStream = inputStreamByteStream;
                                fileOutputStream = fileOutputStream2;
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (fileOutputStream != null) {
                                    throw th;
                                }
                                fileOutputStream.close();
                                throw th;
                            }
                        }
                        FileOutputStream fileOutputStream4 = fileOutputStream2;
                        fileOutputStream4.flush();
                        if (!c.this.l.exists()) {
                            c cVar4 = c.this;
                            cVar4.k.renameTo(cVar4.l);
                            str = null;
                        } else if (c.this.l.isDirectory()) {
                            str = null;
                        } else if (c.this.l.delete()) {
                            c cVar42 = c.this;
                            cVar42.k.renameTo(cVar42.l);
                            str = null;
                        } else {
                            str = "Can't remove old file:" + c.this.l.getAbsolutePath();
                        }
                        if (str == null) {
                            c cVar5 = c.this;
                            Cocos2dxDownloader.access$400(cVar5.p, cVar5.r, 0, null, null);
                            Cocos2dxDownloader.access$600(c.this.p);
                        } else {
                            c cVar6 = c.this;
                            Cocos2dxDownloader.access$400(cVar6.p, cVar6.r, 0, str, null);
                        }
                        fileOutputStream3 = fileOutputStream4;
                    } else {
                        ByteArrayOutputStream byteArrayOutputStream = jContentLength > 0 ? new ByteArrayOutputStream((int) jContentLength) : new ByteArrayOutputStream(4096);
                        while (true) {
                            int i2 = inputStreamByteStream.read(bArr);
                            if (i2 == -1) {
                                break;
                            }
                            long j5 = i2;
                            long j6 = j + j5;
                            byteArrayOutputStream.write(bArr, 0, i2);
                            c cVar7 = c.this;
                            Cocos2dxDownloader.access$500(cVar7.p, cVar7.r, j5, j6, jContentLength);
                            j = j6;
                            jContentLength = jContentLength;
                        }
                        c cVar8 = c.this;
                        Cocos2dxDownloader.access$400(cVar8.p, cVar8.r, 0, null, byteArrayOutputStream.toByteArray());
                        Cocos2dxDownloader.access$600(c.this.p);
                    }
                }
            }
        }

        class b implements Runnable {
            final String i;

            b(String str) {
                this.i = str;
            }

            @Override
            public void run() {
                Cocos2dxDownloader cocos2dxDownloader = c.this.p;
                cocos2dxDownloader.nativeOnFinish(Cocos2dxDownloader.access$000(cocos2dxDownloader), c.this.r, 0, this.i, null);
            }
        }

        c(String str, String str2, Cocos2dxDownloader cocos2dxDownloader, String[] strArr, int i) {
            this.n = str;
            this.o = str2;
            this.p = cocos2dxDownloader;
            this.q = strArr;
            this.r = i;
        }

        @Override
        public void run() {
            Call callNewCall;
            Request.Builder builderUrl;
            int i;
            String[] strArr;
            if (this.n.length() > 0) {
                try {
                    this.i = new URI(this.o).getHost();
                    File file = new File(this.n + Cocos2dxDownloader.access$100(this.p));
                    this.k = file;
                    if (!file.isDirectory()) {
                        File parentFile = this.k.getParentFile();
                        if (parentFile.isDirectory() || parentFile.mkdirs()) {
                            File file2 = new File(this.n);
                            this.l = file2;
                            if (!file2.isDirectory()) {
                                long length = this.k.length();
                                this.j = this.i.startsWith("www.") ? this.i.substring(4) : this.i;
                                if (length > 0) {
                                    if (Cocos2dxDownloader.access$200().containsKey(this.j) && ((Boolean) Cocos2dxDownloader.access$200().get(this.j)).booleanValue()) {
                                        this.m = length;
                                    } else {
                                        try {
                                            PrintWriter printWriter = new PrintWriter(this.k);
                                            printWriter.print("");
                                            printWriter.close();
                                        } catch (FileNotFoundException unused) {
                                        }
                                    }
                                }
                                builderUrl = new Request.Builder().url(this.o);
                                i = 0;
                                while (true) {
                                    strArr = this.q;
                                    if (i < strArr.length / 2) {
                                        break;
                                    }
                                    int i2 = i * 2;
                                    builderUrl.addHeader(strArr[i2], strArr[i2 + 1]);
                                    i++;
                                }
                                if (this.m > 0) {
                                    builderUrl.addHeader("RANGE", "bytes=" + this.m + "-");
                                }
                                callNewCall = Cocos2dxDownloader.access$300(this.p).newCall(builderUrl.build());
                                callNewCall.enqueue(new a());
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e2) {
                    e2.printStackTrace();
                }
                callNewCall = null;
            } else {
                builderUrl = new Request.Builder().url(this.o);
                i = 0;
                while (true) {
                    strArr = this.q;
                    if (i < strArr.length / 2) {
                        break;
                        break;
                    } else {
                        int i22 = i * 2;
                        builderUrl.addHeader(strArr[i22], strArr[i22 + 1]);
                        i++;
                    }
                }
                if (this.m > 0) {
                    builderUrl.addHeader("RANGE", "bytes=" + this.m + "-");
                }
                callNewCall = Cocos2dxDownloader.access$300(this.p).newCall(builderUrl.build());
                callNewCall.enqueue(new a());
            }
            if (callNewCall != null) {
                Cocos2dxDownloader.access$700(this.p).put(Integer.valueOf(this.r), callNewCall);
                return;
            }
            Cocos2dxHelper.runOnGLThread(new b("Can't create DownloadTask for " + this.o));
        }
    }

    class d implements Runnable {
        final int j;

        d(int i) {
            this.j = i;
        }

        @Override
        public void run() {
            for (Map.Entry entry : Cocos2dxDownloader.access$700(Cocos2dxDownloader.this).entrySet()) {
                Object key = entry.getKey();
                Call call = (Call) entry.getValue();
                if (call != null && Integer.parseInt(key.toString()) == this.j) {
                    call.cancel();
                    Cocos2dxDownloader.access$700(Cocos2dxDownloader.this).remove(Integer.valueOf(this.j));
                    Cocos2dxDownloader.access$600(Cocos2dxDownloader.this);
                    return;
                }
            }
        }
    }

    class e implements Runnable {
        e() {
        }

        @Override
        public void run() {
            Iterator it = Cocos2dxDownloader.access$700(Cocos2dxDownloader.this).entrySet().iterator();
            while (it.hasNext()) {
                Call call = (Call) ((Map.Entry) it.next()).getValue();
                if (call != null) {
                    call.cancel();
                }
            }
        }
    }

    public static void abort(Cocos2dxDownloader cocos2dxDownloader, int i) {
        Cocos2dxHelper.getActivity().runOnUiThread(cocos2dxDownloader.new d(i));
    }

    static int access$000(Cocos2dxDownloader cocos2dxDownloader) {
        return cocos2dxDownloader._id;
    }

    static String access$100(Cocos2dxDownloader cocos2dxDownloader) {
        return cocos2dxDownloader._tempFileNameSuffix;
    }

    static ConcurrentHashMap access$200() {
        return _resumingSupport;
    }

    static OkHttpClient access$300(Cocos2dxDownloader cocos2dxDownloader) {
        return cocos2dxDownloader._httpClient;
    }

    static void access$400(Cocos2dxDownloader cocos2dxDownloader, int i, int i2, String str, byte[] bArr) {
        cocos2dxDownloader.onFinish(i, i2, str, bArr);
    }

    static void access$500(Cocos2dxDownloader cocos2dxDownloader, int i, long j, long j2, long j3) {
        cocos2dxDownloader.onProgress(i, j, j2, j3);
    }

    static void access$600(Cocos2dxDownloader cocos2dxDownloader) {
        cocos2dxDownloader.runNextTaskIfExists();
    }

    static ConcurrentHashMap access$700(Cocos2dxDownloader cocos2dxDownloader) {
        return cocos2dxDownloader._taskMap;
    }

    public static void cancelAllRequests(Cocos2dxDownloader cocos2dxDownloader) {
        Cocos2dxHelper.getActivity().runOnUiThread(cocos2dxDownloader.new e());
    }

    public static Cocos2dxDownloader createDownloader(int i, int i2, String str, int i3) {
        Cocos2dxDownloader cocos2dxDownloader = new Cocos2dxDownloader();
        cocos2dxDownloader._id = i;
        if (i2 > 0) {
            cocos2dxDownloader._httpClient = new OkHttpClient().newBuilder().followRedirects(true).followSslRedirects(true).callTimeout(i2, TimeUnit.SECONDS).build();
        } else {
            cocos2dxDownloader._httpClient = new OkHttpClient().newBuilder().followRedirects(true).followSslRedirects(true).build();
        }
        cocos2dxDownloader._tempFileNameSuffix = str;
        cocos2dxDownloader._countOfMaxProcessingTasks = i3;
        return cocos2dxDownloader;
    }

    public static void createTask(Cocos2dxDownloader cocos2dxDownloader, int i, String str, String str2, String[] strArr) {
        cocos2dxDownloader.enqueueTask(new c(str2, str, cocos2dxDownloader, strArr, i));
    }

    private void enqueueTask(Runnable runnable) {
        synchronized (this._taskQueue) {
            try {
                if (this._runningTaskCount < this._countOfMaxProcessingTasks) {
                    Cocos2dxHelper.getActivity().runOnUiThread(runnable);
                    this._runningTaskCount++;
                } else {
                    this._taskQueue.add(runnable);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void onFinish(int i, int i2, String str, byte[] bArr) {
        if (((Call) this._taskMap.get(Integer.valueOf(i))) == null) {
            return;
        }
        this._taskMap.remove(Integer.valueOf(i));
        this._runningTaskCount--;
        Cocos2dxHelper.runOnGLThread(new b(i, i2, str, bArr));
        runNextTaskIfExists();
    }

    private void onProgress(int i, long j, long j2, long j3) {
        Cocos2dxHelper.runOnGLThread(new a(i, j, j2, j3));
    }

    private void runNextTaskIfExists() {
        synchronized (this._taskQueue) {
            while (this._runningTaskCount < this._countOfMaxProcessingTasks && this._taskQueue.size() > 0) {
                try {
                    Cocos2dxHelper.getActivity().runOnUiThread((Runnable) this._taskQueue.poll());
                    this._runningTaskCount++;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    native void nativeOnFinish(int i, int i2, int i3, String str, byte[] bArr);

    native void nativeOnProgress(int i, int i2, long j, long j2, long j3);
}
