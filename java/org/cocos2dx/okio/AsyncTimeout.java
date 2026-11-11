package org.cocos2dx.okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class AsyncTimeout extends Timeout {
    private static final long IDLE_TIMEOUT_MILLIS;
    private static final long IDLE_TIMEOUT_NANOS;
    private static final int TIMEOUT_WRITE_SIZE = 65536;

    @Nullable
    static AsyncTimeout head;
    private boolean inQueue;

    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;

    class a implements Sink {
        final Sink i;

        a(Sink sink) {
            this.i = sink;
        }

        @Override
        public void close() throws IOException {
            AsyncTimeout.this.enter();
            try {
                try {
                    this.i.close();
                    AsyncTimeout.this.exit(true);
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                }
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
                throw th;
            }
        }

        @Override
        public void flush() throws IOException {
            AsyncTimeout.this.enter();
            try {
                try {
                    this.i.flush();
                    AsyncTimeout.this.exit(true);
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                }
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
                throw th;
            }
        }

        @Override
        public Timeout timeout() {
            return AsyncTimeout.this;
        }

        public String toString() {
            return "AsyncTimeout.sink(" + this.i + ")";
        }

        @Override
        public void write(Buffer buffer, long j) throws IOException {
            i.b(buffer.size, 0L, j);
            while (true) {
                long j2 = 0;
                if (j <= 0) {
                    return;
                }
                f fVar = buffer.head;
                while (j2 < 65536) {
                    j2 += (long) (fVar.c - fVar.b);
                    if (j2 >= j) {
                        j2 = j;
                        break;
                    }
                    fVar = fVar.f;
                }
                AsyncTimeout.this.enter();
                try {
                    try {
                        this.i.write(buffer, j2);
                        j -= j2;
                        AsyncTimeout.this.exit(true);
                    } catch (IOException e) {
                        throw AsyncTimeout.this.exit(e);
                    }
                } catch (Throwable th) {
                    AsyncTimeout.this.exit(false);
                    throw th;
                }
            }
        }
    }

    class b implements Source {
        final Source i;

        b(Source source) {
            this.i = source;
        }

        @Override
        public void close() throws IOException {
            try {
                try {
                    this.i.close();
                    AsyncTimeout.this.exit(true);
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                }
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
                throw th;
            }
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            AsyncTimeout.this.enter();
            try {
                try {
                    long j2 = this.i.read(buffer, j);
                    AsyncTimeout.this.exit(true);
                    return j2;
                } catch (IOException e) {
                    throw AsyncTimeout.this.exit(e);
                }
            } catch (Throwable th) {
                AsyncTimeout.this.exit(false);
                throw th;
            }
        }

        @Override
        public Timeout timeout() {
            return AsyncTimeout.this;
        }

        public String toString() {
            return "AsyncTimeout.source(" + this.i + ")";
        }
    }

    private static final class c extends Thread {
        c() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        @Override
        public void run() {
            throw new UnsupportedOperationException("Method not generated: org.cocos2dx.okio.AsyncTimeout.c.run():void");
        }
    }

    static {
        long millis = TimeUnit.SECONDS.toMillis(60L);
        IDLE_TIMEOUT_MILLIS = millis;
        IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(millis);
    }

    @Nullable
    static AsyncTimeout awaitTimeout() throws InterruptedException {
        AsyncTimeout asyncTimeout = head.next;
        if (asyncTimeout == null) {
            long jNanoTime = System.nanoTime();
            AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
            if (head.next != null || System.nanoTime() - jNanoTime < IDLE_TIMEOUT_NANOS) {
                return null;
            }
            return head;
        }
        long jRemainingNanos = asyncTimeout.remainingNanos(System.nanoTime());
        if (jRemainingNanos > 0) {
            long j = jRemainingNanos / 1000000;
            AsyncTimeout.class.wait(j, (int) (jRemainingNanos - (1000000 * j)));
            return null;
        }
        head.next = asyncTimeout.next;
        asyncTimeout.next = null;
        return asyncTimeout;
    }

    private static synchronized boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        AsyncTimeout asyncTimeout2 = head;
        while (asyncTimeout2 != null) {
            AsyncTimeout asyncTimeout3 = asyncTimeout2.next;
            if (asyncTimeout3 == asyncTimeout) {
                asyncTimeout2.next = asyncTimeout.next;
                asyncTimeout.next = null;
                return false;
            }
            asyncTimeout2 = asyncTimeout3;
        }
        return true;
    }

    private long remainingNanos(long j) {
        return this.timeoutAt - j;
    }

    private static synchronized void scheduleTimeout(AsyncTimeout asyncTimeout, long j, boolean z) {
        try {
            if (head == null) {
                head = new AsyncTimeout();
                new c().start();
            }
            long jNanoTime = System.nanoTime();
            if (j != 0 && z) {
                asyncTimeout.timeoutAt = Math.min(j, asyncTimeout.deadlineNanoTime() - jNanoTime) + jNanoTime;
            } else if (j != 0) {
                asyncTimeout.timeoutAt = j + jNanoTime;
            } else {
                if (!z) {
                    throw new AssertionError();
                }
                asyncTimeout.timeoutAt = asyncTimeout.deadlineNanoTime();
            }
            long jRemainingNanos = asyncTimeout.remainingNanos(jNanoTime);
            AsyncTimeout asyncTimeout2 = head;
            while (true) {
                AsyncTimeout asyncTimeout3 = asyncTimeout2.next;
                if (asyncTimeout3 == null || jRemainingNanos < asyncTimeout3.remainingNanos(jNanoTime)) {
                    break;
                    break;
                }
                asyncTimeout2 = asyncTimeout2.next;
            }
            asyncTimeout.next = asyncTimeout2.next;
            asyncTimeout2.next = asyncTimeout;
            if (asyncTimeout2 == head) {
                AsyncTimeout.class.notify();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        long jTimeoutNanos = timeoutNanos();
        boolean zHasDeadline = hasDeadline();
        if (jTimeoutNanos != 0 || zHasDeadline) {
            this.inQueue = true;
            scheduleTimeout(this, jTimeoutNanos, zHasDeadline);
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }

    protected IOException newTimeoutException(@Nullable IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    public final Sink sink(Sink sink) {
        return new a(sink);
    }

    public final Source source(Source source) {
        return new b(source);
    }

    protected void timedOut() {
    }

    final void exit(boolean z) throws IOException {
        if (exit() && z) {
            throw newTimeoutException(null);
        }
    }

    final IOException exit(IOException iOException) {
        return !exit() ? iOException : newTimeoutException(iOException);
    }
}
