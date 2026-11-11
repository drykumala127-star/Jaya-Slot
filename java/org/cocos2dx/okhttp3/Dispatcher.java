package org.cocos2dx.okhttp3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.b;
import org.cocos2dx.okhttp3.internal.Util;

public final class Dispatcher {
    static final boolean $assertionsDisabled = false;

    @Nullable
    private ExecutorService executorService;

    @Nullable
    private Runnable idleCallback;
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private final Deque readyAsyncCalls = new ArrayDeque();
    private final Deque runningAsyncCalls = new ArrayDeque();
    private final Deque runningSyncCalls = new ArrayDeque();

    public Dispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private boolean promoteAndExecute() {
        int i;
        boolean z;
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            try {
                Iterator it = this.readyAsyncCalls.iterator();
                while (it.hasNext()) {
                    b.b bVar = (b.b) it.next();
                    if (this.runningAsyncCalls.size() >= this.maxRequests) {
                        break;
                    }
                    if (runningCallsForHost(bVar) < this.maxRequestsPerHost) {
                        it.remove();
                        arrayList.add(bVar);
                        this.runningAsyncCalls.add(bVar);
                    }
                }
                z = runningCallsCount() > 0;
            } catch (Throwable th) {
                throw th;
            }
        }
        int size = arrayList.size();
        for (i = 0; i < size; i++) {
            ((b.b) arrayList.get(i)).k(executorService());
        }
        return z;
    }

    private int runningCallsForHost(b.b bVar) {
        int i = 0;
        for (b.b bVar2 : this.runningAsyncCalls) {
            if (!bVar2.l().n && bVar2.m().equals(bVar.m())) {
                i++;
            }
        }
        return i;
    }

    public synchronized void cancelAll() {
        try {
            Iterator it = this.readyAsyncCalls.iterator();
            while (it.hasNext()) {
                ((b.b) it.next()).l().cancel();
            }
            Iterator it2 = this.runningAsyncCalls.iterator();
            while (it2.hasNext()) {
                ((b.b) it2.next()).l().cancel();
            }
            Iterator it3 = this.runningSyncCalls.iterator();
            while (it3.hasNext()) {
                ((b) it3.next()).cancel();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    void enqueue(b.b bVar) {
        synchronized (this) {
            this.readyAsyncCalls.add(bVar);
        }
        promoteAndExecute();
    }

    synchronized void executed(b bVar) {
        this.runningSyncCalls.add(bVar);
    }

    public synchronized ExecutorService executorService() {
        try {
            if (this.executorService == null) {
                this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.executorService;
    }

    void finished(b.b bVar) {
        finished(this.runningAsyncCalls, bVar);
    }

    public synchronized int getMaxRequests() {
        return this.maxRequests;
    }

    public synchronized int getMaxRequestsPerHost() {
        return this.maxRequestsPerHost;
    }

    public synchronized List queuedCalls() {
        ArrayList arrayList;
        try {
            arrayList = new ArrayList();
            Iterator it = this.readyAsyncCalls.iterator();
            while (it.hasNext()) {
                arrayList.add(((b.b) it.next()).l());
            }
        } catch (Throwable th) {
            throw th;
        }
        return Collections.unmodifiableList(arrayList);
    }

    public synchronized int queuedCallsCount() {
        return this.readyAsyncCalls.size();
    }

    public synchronized List runningCalls() {
        ArrayList arrayList;
        try {
            arrayList = new ArrayList();
            arrayList.addAll(this.runningSyncCalls);
            Iterator it = this.runningAsyncCalls.iterator();
            while (it.hasNext()) {
                arrayList.add(((b.b) it.next()).l());
            }
        } catch (Throwable th) {
            throw th;
        }
        return Collections.unmodifiableList(arrayList);
    }

    public synchronized int runningCallsCount() {
        return this.runningAsyncCalls.size() + this.runningSyncCalls.size();
    }

    public synchronized void setIdleCallback(@Nullable Runnable runnable) {
        this.idleCallback = runnable;
    }

    public void setMaxRequests(int i) {
        if (i >= 1) {
            synchronized (this) {
                this.maxRequests = i;
            }
            promoteAndExecute();
        } else {
            throw new IllegalArgumentException("max < 1: " + i);
        }
    }

    public void setMaxRequestsPerHost(int i) {
        if (i >= 1) {
            synchronized (this) {
                this.maxRequestsPerHost = i;
            }
            promoteAndExecute();
        } else {
            throw new IllegalArgumentException("max < 1: " + i);
        }
    }

    void finished(b bVar) {
        finished(this.runningSyncCalls, bVar);
    }

    private <T> void finished(Deque deque, Object obj) {
        Runnable runnable;
        synchronized (this) {
            if (deque.remove(obj)) {
                runnable = this.idleCallback;
            } else {
                throw new AssertionError("Call wasn't in-flight!");
            }
        }
        if (promoteAndExecute() || runnable == null) {
            return;
        }
        runnable.run();
    }

    public Dispatcher() {
    }
}
