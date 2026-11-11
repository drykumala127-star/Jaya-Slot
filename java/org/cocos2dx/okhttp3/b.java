package org.cocos2dx.okhttp3;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.internal.NamedRunnable;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.cache.CacheInterceptor;
import org.cocos2dx.okhttp3.internal.connection.ConnectInterceptor;
import org.cocos2dx.okhttp3.internal.connection.StreamAllocation;
import org.cocos2dx.okhttp3.internal.http.BridgeInterceptor;
import org.cocos2dx.okhttp3.internal.http.CallServerInterceptor;
import org.cocos2dx.okhttp3.internal.http.RealInterceptorChain;
import org.cocos2dx.okhttp3.internal.http.RetryAndFollowUpInterceptor;
import org.cocos2dx.okhttp3.internal.platform.Platform;
import org.cocos2dx.okio.AsyncTimeout;
import org.cocos2dx.okio.Timeout;

final class b implements Call {
    final OkHttpClient i;
    final RetryAndFollowUpInterceptor j;
    final AsyncTimeout k;
    private EventListener l;
    final Request m;
    final boolean n;
    private boolean o;

    class a extends AsyncTimeout {
        a() {
        }

        @Override
        protected void timedOut() {
            b.this.cancel();
        }
    }

    final class b extends NamedRunnable {
        private final Callback i;

        b(Callback callback) {
            super("OkHttp %s", b.this.i());
            this.i = callback;
        }

        @Override
        protected void execute() {
            Throwable th;
            boolean z;
            IOException e;
            b.this.k.enter();
            try {
                try {
                    z = true;
                    try {
                        this.i.onResponse(b.this, b.this.d());
                    } catch (IOException e2) {
                        e = e2;
                        IOException iOExceptionK = b.this.k(e);
                        if (z) {
                            Platform.get().log(4, "Callback failure for " + b.this.l(), iOExceptionK);
                        } else {
                            b.a(b.this).callFailed(b.this, iOExceptionK);
                            this.i.onFailure(b.this, iOExceptionK);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        b.this.cancel();
                        if (!z) {
                            this.i.onFailure(b.this, new IOException("canceled due to " + th));
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    b.this.i.dispatcher().finished(this);
                    throw th3;
                }
            } catch (IOException e3) {
                e = e3;
                z = false;
            } catch (Throwable th4) {
                th = th4;
                z = false;
            }
            b.this.i.dispatcher().finished(this);
        }

        void k(ExecutorService executorService) {
            try {
                try {
                    executorService.execute(this);
                } catch (RejectedExecutionException e) {
                    InterruptedIOException interruptedIOException = new InterruptedIOException("executor rejected");
                    interruptedIOException.initCause(e);
                    b.a(b.this).callFailed(b.this, interruptedIOException);
                    this.i.onFailure(b.this, interruptedIOException);
                    b.this.i.dispatcher().finished(this);
                }
            } catch (Throwable th) {
                b.this.i.dispatcher().finished(this);
                throw th;
            }
        }

        b l() {
            return b.this;
        }

        String m() {
            return b.this.m.url().host();
        }
    }

    private b(OkHttpClient okHttpClient, Request request, boolean z) {
        this.i = okHttpClient;
        this.m = request;
        this.n = z;
        this.j = new RetryAndFollowUpInterceptor(okHttpClient, z);
        a aVar = new a();
        this.k = aVar;
        aVar.timeout(okHttpClient.callTimeoutMillis(), TimeUnit.MILLISECONDS);
    }

    static EventListener a(b bVar) {
        return bVar.l;
    }

    private void b() {
        this.j.setCallStackTrace(Platform.get().getStackTraceForCloseable("response.body().close()"));
    }

    static b g(OkHttpClient okHttpClient, Request request, boolean z) {
        b bVar = new b(okHttpClient, request, z);
        bVar.l = okHttpClient.eventListenerFactory().create(bVar);
        return bVar;
    }

    @Override
    public b clone() {
        return g(this.i, this.m, this.n);
    }

    @Override
    public void cancel() {
        this.j.cancel();
    }

    Response d() throws IOException {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.i.interceptors());
        arrayList.add(this.j);
        arrayList.add(new BridgeInterceptor(this.i.cookieJar()));
        arrayList.add(new CacheInterceptor(this.i.internalCache()));
        arrayList.add(new ConnectInterceptor(this.i));
        if (!this.n) {
            arrayList.addAll(this.i.networkInterceptors());
        }
        arrayList.add(new CallServerInterceptor(this.n));
        Response responseProceed = new RealInterceptorChain(arrayList, null, null, null, 0, this.m, this, this.l, this.i.connectTimeoutMillis(), this.i.readTimeoutMillis(), this.i.writeTimeoutMillis()).proceed(this.m);
        if (!this.j.isCanceled()) {
            return responseProceed;
        }
        Util.closeQuietly(responseProceed);
        throw new IOException("Canceled");
    }

    @Override
    public void enqueue(Callback callback) {
        synchronized (this) {
            if (this.o) {
                throw new IllegalStateException("Already Executed");
            }
            this.o = true;
        }
        b();
        this.l.callStart(this);
        this.i.dispatcher().enqueue(new b(callback));
    }

    @Override
    public Response execute() {
        synchronized (this) {
            if (this.o) {
                throw new IllegalStateException("Already Executed");
            }
            this.o = true;
        }
        b();
        this.k.enter();
        this.l.callStart(this);
        try {
            try {
                this.i.dispatcher().executed(this);
                Response responseD = d();
                if (responseD == null) {
                    throw new IOException("Canceled");
                }
                this.i.dispatcher().finished(this);
                return responseD;
            } catch (IOException e) {
                IOException iOExceptionK = k(e);
                this.l.callFailed(this, iOExceptionK);
                throw iOExceptionK;
            }
        } catch (Throwable th) {
            this.i.dispatcher().finished(this);
            throw th;
        }
        this.i.dispatcher().finished(this);
        throw th;
    }

    String i() {
        return this.m.url().redact();
    }

    @Override
    public boolean isCanceled() {
        return this.j.isCanceled();
    }

    @Override
    public synchronized boolean isExecuted() {
        return this.o;
    }

    StreamAllocation j() {
        return this.j.streamAllocation();
    }

    IOException k(IOException iOException) {
        if (!this.k.exit()) {
            return iOException;
        }
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    String l() {
        StringBuilder sb = new StringBuilder();
        sb.append(isCanceled() ? "canceled " : "");
        sb.append(this.n ? "web socket" : "call");
        sb.append(" to ");
        sb.append(i());
        return sb.toString();
    }

    @Override
    public Request request() {
        return this.m;
    }

    @Override
    public Timeout timeout() {
        return this.k;
    }
}
