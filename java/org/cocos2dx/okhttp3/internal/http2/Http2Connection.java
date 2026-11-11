package org.cocos2dx.okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.Protocol;
import org.cocos2dx.okhttp3.internal.NamedRunnable;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.http2.b;
import org.cocos2dx.okhttp3.internal.platform.Platform;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.Okio;

public final class Http2Connection implements Closeable {
    static final boolean $assertionsDisabled = false;
    static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    private static final ExecutorService listenerExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));
    private boolean awaitingPong;
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set currentPushRequests;
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    int nextStreamId;
    final Settings peerSettings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final h readerRunnable;
    boolean shutdown;
    final Socket socket;
    final org.cocos2dx.okhttp3.internal.http2.c writer;
    private final ScheduledExecutorService writerExecutor;
    final Map streams = new LinkedHashMap();
    long unacknowledgedBytesRead = 0;
    Settings okHttpSettings = new Settings();

    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new a();

        class a extends Listener {
            a() {
            }

            @Override
            public void onStream(Http2Stream http2Stream) {
                http2Stream.close(ErrorCode.REFUSED_STREAM);
            }
        }

        public void onSettings(Http2Connection http2Connection) {
        }

        public abstract void onStream(Http2Stream http2Stream);
    }

    class a extends NamedRunnable {
        final int i;
        final ErrorCode j;

        a(String str, Object[] objArr, int i, ErrorCode errorCode) {
            super(str, objArr);
            this.i = i;
            this.j = errorCode;
        }

        @Override
        public void execute() {
            try {
                Http2Connection.this.writeSynReset(this.i, this.j);
            } catch (IOException unused) {
                Http2Connection.access$000(Http2Connection.this);
            }
        }
    }

    class b extends NamedRunnable {
        final int i;
        final long j;

        b(String str, Object[] objArr, int i, long j) {
            super(str, objArr);
            this.i = i;
            this.j = j;
        }

        @Override
        public void execute() {
            try {
                Http2Connection.this.writer.x(this.i, this.j);
            } catch (IOException unused) {
                Http2Connection.access$000(Http2Connection.this);
            }
        }
    }

    class c extends NamedRunnable {
        final int i;
        final List j;

        c(String str, Object[] objArr, int i, List list) {
            super(str, objArr);
            this.i = i;
            this.j = list;
        }

        @Override
        public void execute() {
            if (Http2Connection.this.pushObserver.onRequest(this.i, this.j)) {
                try {
                    Http2Connection.this.writer.t(this.i, ErrorCode.CANCEL);
                    synchronized (Http2Connection.this) {
                        try {
                            Http2Connection.this.currentPushRequests.remove(Integer.valueOf(this.i));
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                } catch (IOException unused) {
                }
            }
        }
    }

    class d extends NamedRunnable {
        final int i;
        final List j;
        final boolean k;

        d(String str, Object[] objArr, int i, List list, boolean z) {
            super(str, objArr);
            this.i = i;
            this.j = list;
            this.k = z;
        }

        @Override
        public void execute() {
            boolean zOnHeaders = Http2Connection.this.pushObserver.onHeaders(this.i, this.j, this.k);
            if (zOnHeaders) {
                try {
                    Http2Connection.this.writer.t(this.i, ErrorCode.CANCEL);
                } catch (IOException unused) {
                    return;
                }
            }
            if (zOnHeaders || this.k) {
                synchronized (Http2Connection.this) {
                    try {
                        Http2Connection.this.currentPushRequests.remove(Integer.valueOf(this.i));
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
    }

    class e extends NamedRunnable {
        final int i;
        final Buffer j;
        final int k;
        final boolean l;

        e(String str, Object[] objArr, int i, Buffer buffer, int i2, boolean z) {
            super(str, objArr);
            this.i = i;
            this.j = buffer;
            this.k = i2;
            this.l = z;
        }

        @Override
        public void execute() {
            try {
                boolean zOnData = Http2Connection.this.pushObserver.onData(this.i, this.j, this.k, this.l);
                if (zOnData) {
                    Http2Connection.this.writer.t(this.i, ErrorCode.CANCEL);
                }
                if (zOnData || this.l) {
                    synchronized (Http2Connection.this) {
                        Http2Connection.this.currentPushRequests.remove(Integer.valueOf(this.i));
                    }
                }
            } catch (IOException unused) {
            }
        }
    }

    class f extends NamedRunnable {
        final int i;
        final ErrorCode j;

        f(String str, Object[] objArr, int i, ErrorCode errorCode) {
            super(str, objArr);
            this.i = i;
            this.j = errorCode;
        }

        @Override
        public void execute() {
            Http2Connection.this.pushObserver.onReset(this.i, this.j);
            synchronized (Http2Connection.this) {
                Http2Connection.this.currentPushRequests.remove(Integer.valueOf(this.i));
            }
        }
    }

    final class g extends NamedRunnable {
        final boolean i;
        final int j;
        final int k;

        g(boolean z, int i, int i2) {
            super("OkHttp %s ping %08x%08x", Http2Connection.this.hostname, Integer.valueOf(i), Integer.valueOf(i2));
            this.i = z;
            this.j = i;
            this.k = i2;
        }

        @Override
        public void execute() {
            Http2Connection.this.writePing(this.i, this.j, this.k);
        }
    }

    class h extends NamedRunnable implements b.b {
        final org.cocos2dx.okhttp3.internal.http2.b i;

        class a extends NamedRunnable {
            final Http2Stream i;

            a(String str, Object[] objArr, Http2Stream http2Stream) {
                super(str, objArr);
                this.i = http2Stream;
            }

            @Override
            public void execute() {
                try {
                    Http2Connection.this.listener.onStream(this.i);
                } catch (IOException e) {
                    Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.hostname, e);
                    try {
                        this.i.close(ErrorCode.PROTOCOL_ERROR);
                    } catch (IOException unused) {
                    }
                }
            }
        }

        class b extends NamedRunnable {
            final boolean i;
            final Settings j;

            b(String str, Object[] objArr, boolean z, Settings settings) {
                super(str, objArr);
                this.i = z;
                this.j = settings;
            }

            @Override
            public void execute() {
                h.this.k(this.i, this.j);
            }
        }

        class c extends NamedRunnable {
            c(String str, Object... objArr) {
                super(str, objArr);
            }

            @Override
            public void execute() {
                Http2Connection http2Connection = Http2Connection.this;
                http2Connection.listener.onSettings(http2Connection);
            }
        }

        h(org.cocos2dx.okhttp3.internal.http2.b bVar) {
            super("OkHttp %s", Http2Connection.this.hostname);
            this.i = bVar;
        }

        @Override
        public void a(boolean z, int i, int i2, List list) {
            if (Http2Connection.this.pushedStream(i)) {
                Http2Connection.this.pushHeadersLater(i, list, z);
                return;
            }
            synchronized (Http2Connection.this) {
                try {
                    Http2Stream stream = Http2Connection.this.getStream(i);
                    if (stream != null) {
                        stream.receiveHeaders(list);
                        if (z) {
                            stream.receiveFin();
                            return;
                        }
                        return;
                    }
                    Http2Connection http2Connection = Http2Connection.this;
                    if (http2Connection.shutdown) {
                        return;
                    }
                    if (i <= http2Connection.lastGoodStreamId) {
                        return;
                    }
                    if (i % 2 == http2Connection.nextStreamId % 2) {
                        return;
                    }
                    Http2Stream http2Stream = new Http2Stream(i, Http2Connection.this, Http2Connection.$assertionsDisabled, z, Util.toHeaders(list));
                    Http2Connection http2Connection2 = Http2Connection.this;
                    http2Connection2.lastGoodStreamId = i;
                    http2Connection2.streams.put(Integer.valueOf(i), http2Stream);
                    Http2Connection.access$100().execute(new a("OkHttp %s stream %d", new Object[]{Http2Connection.this.hostname, Integer.valueOf(i)}, http2Stream));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public void b() {
        }

        @Override
        public void c(int i, long j) {
            if (i == 0) {
                synchronized (Http2Connection.this) {
                    Http2Connection http2Connection = Http2Connection.this;
                    http2Connection.bytesLeftInWriteWindow += j;
                    http2Connection.notifyAll();
                }
                return;
            }
            Http2Stream stream = Http2Connection.this.getStream(i);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(j);
                }
            }
        }

        @Override
        public void d(boolean z, Settings settings) {
            try {
                Http2Connection.access$200(Http2Connection.this).execute(new b("OkHttp %s ACK Settings", new Object[]{Http2Connection.this.hostname}, z, settings));
            } catch (RejectedExecutionException unused) {
            }
        }

        @Override
        public void e(int i, int i2, List list) {
            Http2Connection.this.pushRequestLater(i2, list);
        }

        @Override
        protected void execute() throws Throwable {
            ErrorCode errorCode;
            ErrorCode errorCode2 = ErrorCode.INTERNAL_ERROR;
            try {
                try {
                    try {
                        this.i.f(this);
                        while (this.i.e(Http2Connection.$assertionsDisabled, this)) {
                        }
                        ErrorCode errorCode3 = ErrorCode.NO_ERROR;
                        try {
                            errorCode2 = ErrorCode.CANCEL;
                            Http2Connection.this.close(errorCode3, errorCode2);
                            errorCode = errorCode3;
                        } catch (IOException unused) {
                            errorCode2 = ErrorCode.PROTOCOL_ERROR;
                            Http2Connection http2Connection = Http2Connection.this;
                            http2Connection.close(errorCode2, errorCode2);
                            errorCode = http2Connection;
                        }
                    } catch (Throwable th) {
                        th = th;
                        try {
                            Http2Connection.this.close(errorCode, errorCode2);
                        } catch (IOException unused2) {
                        }
                        Util.closeQuietly(this.i);
                        throw th;
                    }
                } catch (IOException unused3) {
                } catch (Throwable th2) {
                    th = th2;
                    errorCode = errorCode2;
                    Http2Connection.this.close(errorCode, errorCode2);
                    Util.closeQuietly(this.i);
                    throw th;
                }
            } catch (IOException unused4) {
            }
            Util.closeQuietly(this.i);
        }

        @Override
        public void f(int i, ErrorCode errorCode, ByteString byteString) {
            Http2Stream[] http2StreamArr;
            byteString.size();
            synchronized (Http2Connection.this) {
                http2StreamArr = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
            }
            for (Http2Stream http2Stream : http2StreamArr) {
                if (http2Stream.getId() > i && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    Http2Connection.this.removeStream(http2Stream.getId());
                }
            }
        }

        @Override
        public void g(int i, ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(i)) {
                Http2Connection.this.pushResetLater(i, errorCode);
                return;
            }
            Http2Stream http2StreamRemoveStream = Http2Connection.this.removeStream(i);
            if (http2StreamRemoveStream != null) {
                http2StreamRemoveStream.receiveRstStream(errorCode);
            }
        }

        @Override
        public void h(boolean z, int i, int i2) {
            if (!z) {
                try {
                    Http2Connection.access$200(Http2Connection.this).execute(Http2Connection.this.new g(true, i, i2));
                } catch (RejectedExecutionException unused) {
                }
            } else {
                synchronized (Http2Connection.this) {
                    Http2Connection.access$302(Http2Connection.this, Http2Connection.$assertionsDisabled);
                    Http2Connection.this.notifyAll();
                }
            }
        }

        @Override
        public void i(int i, int i2, int i3, boolean z) {
        }

        @Override
        public void j(boolean z, int i, BufferedSource bufferedSource, int i2) throws IOException {
            if (Http2Connection.this.pushedStream(i)) {
                Http2Connection.this.pushDataLater(i, bufferedSource, i2, z);
                return;
            }
            Http2Stream stream = Http2Connection.this.getStream(i);
            if (stream == null) {
                Http2Connection.this.writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                long j = i2;
                Http2Connection.this.updateConnectionFlowControl(j);
                bufferedSource.skip(j);
                return;
            }
            stream.receiveData(bufferedSource, i2);
            if (z) {
                stream.receiveFin();
            }
        }

        void k(boolean z, Settings settings) {
            Http2Stream[] http2StreamArr;
            long j;
            synchronized (Http2Connection.this.writer) {
                synchronized (Http2Connection.this) {
                    try {
                        int initialWindowSize = Http2Connection.this.peerSettings.getInitialWindowSize();
                        if (z) {
                            Http2Connection.this.peerSettings.clear();
                        }
                        Http2Connection.this.peerSettings.merge(settings);
                        int initialWindowSize2 = Http2Connection.this.peerSettings.getInitialWindowSize();
                        http2StreamArr = null;
                        if (initialWindowSize2 == -1 || initialWindowSize2 == initialWindowSize) {
                            j = 0;
                        } else {
                            j = initialWindowSize2 - initialWindowSize;
                            if (!Http2Connection.this.streams.isEmpty()) {
                                http2StreamArr = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                try {
                    Http2Connection http2Connection = Http2Connection.this;
                    http2Connection.writer.a(http2Connection.peerSettings);
                } catch (IOException unused) {
                    Http2Connection.access$000(Http2Connection.this);
                }
            }
            if (http2StreamArr != null) {
                for (Http2Stream http2Stream : http2StreamArr) {
                    synchronized (http2Stream) {
                        http2Stream.addBytesToWriteWindow(j);
                    }
                }
            }
            Http2Connection.access$100().execute(new c("OkHttp %s settings", Http2Connection.this.hostname));
        }
    }

    Http2Connection(Builder builder) {
        Settings settings = new Settings();
        this.peerSettings = settings;
        this.currentPushRequests = new LinkedHashSet();
        this.pushObserver = builder.pushObserver;
        boolean z = builder.client;
        this.client = z;
        this.listener = builder.listener;
        int i = z ? 1 : 2;
        this.nextStreamId = i;
        if (z) {
            this.nextStreamId = i + 2;
        }
        if (z) {
            this.okHttpSettings.set(7, OKHTTP_CLIENT_WINDOW_SIZE);
        }
        String str = builder.hostname;
        this.hostname = str;
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", str), $assertionsDisabled));
        this.writerExecutor = scheduledThreadPoolExecutor;
        if (builder.pingIntervalMillis != 0) {
            g gVar = new g($assertionsDisabled, 0, 0);
            int i2 = builder.pingIntervalMillis;
            scheduledThreadPoolExecutor.scheduleAtFixedRate(gVar, i2, i2, TimeUnit.MILLISECONDS);
        }
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", str), true));
        settings.set(7, 65535);
        settings.set(5, 16384);
        this.bytesLeftInWriteWindow = settings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new org.cocos2dx.okhttp3.internal.http2.c(builder.sink, z);
        this.readerRunnable = new h(new org.cocos2dx.okhttp3.internal.http2.b(builder.source, z));
    }

    static void access$000(Http2Connection http2Connection) {
        http2Connection.failConnection();
    }

    static ExecutorService access$100() {
        return listenerExecutor;
    }

    static ScheduledExecutorService access$200(Http2Connection http2Connection) {
        return http2Connection.writerExecutor;
    }

    static boolean access$302(Http2Connection http2Connection, boolean z) {
        http2Connection.awaitingPong = z;
        return z;
    }

    private void failConnection() {
        try {
            ErrorCode errorCode = ErrorCode.PROTOCOL_ERROR;
            close(errorCode, errorCode);
        } catch (IOException unused) {
        }
    }

    private synchronized void pushExecutorExecute(NamedRunnable namedRunnable) {
        if (!isShutdown()) {
            this.pushExecutor.execute(namedRunnable);
        }
    }

    synchronized void awaitPong() {
        while (this.awaitingPong) {
            wait();
        }
    }

    @Override
    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    public void flush() {
        this.writer.flush();
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    synchronized Http2Stream getStream(int i) {
        return (Http2Stream) this.streams.get(Integer.valueOf(i));
    }

    public synchronized boolean isShutdown() {
        return this.shutdown;
    }

    public synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
    }

    public Http2Stream newStream(List list, boolean z) {
        return newStream(0, list, z);
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    void pushDataLater(int i, BufferedSource bufferedSource, int i2, boolean z) throws IOException {
        Buffer buffer = new Buffer();
        long j = i2;
        bufferedSource.require(j);
        bufferedSource.read(buffer, j);
        if (buffer.size() == j) {
            pushExecutorExecute(new e("OkHttp %s Push Data[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, buffer, i2, z));
            return;
        }
        throw new IOException(buffer.size() + " != " + i2);
    }

    void pushHeadersLater(int i, List list, boolean z) {
        try {
            pushExecutorExecute(new d("OkHttp %s Push Headers[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, list, z));
        } catch (RejectedExecutionException unused) {
        }
    }

    void pushRequestLater(int i, List list) {
        synchronized (this) {
            try {
                if (this.currentPushRequests.contains(Integer.valueOf(i))) {
                    writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                    return;
                }
                this.currentPushRequests.add(Integer.valueOf(i));
                try {
                    pushExecutorExecute(new c("OkHttp %s Push Request[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, list));
                } catch (RejectedExecutionException unused) {
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void pushResetLater(int i, ErrorCode errorCode) {
        pushExecutorExecute(new f("OkHttp %s Push Reset[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, errorCode));
    }

    public Http2Stream pushStream(int i, List list, boolean z) {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        }
        return newStream(i, list, z);
    }

    boolean pushedStream(int i) {
        if (i == 0 || (i & 1) != 0) {
            return $assertionsDisabled;
        }
        return true;
    }

    synchronized Http2Stream removeStream(int i) {
        Http2Stream http2Stream;
        http2Stream = (Http2Stream) this.streams.remove(Integer.valueOf(i));
        notifyAll();
        return http2Stream;
    }

    public void setSettings(Settings settings) {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
            }
            this.writer.u(settings);
        }
    }

    public void shutdown(ErrorCode errorCode) {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    return;
                }
                this.shutdown = true;
                this.writer.o(this.lastGoodStreamId, errorCode, Util.EMPTY_BYTE_ARRAY);
            }
        }
    }

    public void start() {
        start(true);
    }

    synchronized void updateConnectionFlowControl(long j) {
        long j2 = this.unacknowledgedBytesRead + j;
        this.unacknowledgedBytesRead = j2;
        if (j2 >= this.okHttpSettings.getInitialWindowSize() / 2) {
            writeWindowUpdateLater(0, this.unacknowledgedBytesRead);
            this.unacknowledgedBytesRead = 0L;
        }
    }

    public void writeData(int i, boolean z, Buffer buffer, long j) {
        long j2;
        int iMin;
        long j3;
        if (j == 0) {
            this.writer.f(z, i, buffer, 0);
            return;
        }
        while (j > 0) {
            synchronized (this) {
                while (true) {
                    try {
                        try {
                            j2 = this.bytesLeftInWriteWindow;
                            if (j2 <= 0) {
                                if (!this.streams.containsKey(Integer.valueOf(i))) {
                                    throw new IOException("stream closed");
                                }
                                wait();
                            }
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                            throw new InterruptedIOException();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                iMin = Math.min((int) Math.min(j, j2), this.writer.q());
                j3 = iMin;
                this.bytesLeftInWriteWindow -= j3;
            }
            j -= j3;
            this.writer.f((z && j == 0) ? true : $assertionsDisabled, i, buffer, iMin);
        }
    }

    void writePing(boolean z, int i, int i2) {
        boolean z2;
        if (!z) {
            synchronized (this) {
                z2 = this.awaitingPong;
                this.awaitingPong = true;
            }
            if (z2) {
                failConnection();
                return;
            }
        }
        try {
            this.writer.r(z, i, i2);
        } catch (IOException unused) {
            failConnection();
        }
    }

    void writePingAndAwaitPong() {
        writePing($assertionsDisabled, 1330343787, -257978967);
        awaitPong();
    }

    void writeSynReply(int i, boolean z, List list) {
        this.writer.v(z, i, list);
    }

    void writeSynReset(int i, ErrorCode errorCode) {
        this.writer.t(i, errorCode);
    }

    void writeSynResetLater(int i, ErrorCode errorCode) {
        try {
            this.writerExecutor.execute(new a("OkHttp %s stream %d", new Object[]{this.hostname, Integer.valueOf(i)}, i, errorCode));
        } catch (RejectedExecutionException unused) {
        }
    }

    void writeWindowUpdateLater(int i, long j) {
        try {
            this.writerExecutor.execute(new b("OkHttp Window Update %s stream %d", new Object[]{this.hostname, Integer.valueOf(i)}, i, j));
        } catch (RejectedExecutionException unused) {
        }
    }

    private Http2Stream newStream(int i, List list, boolean z) {
        int i2;
        Http2Stream http2Stream;
        boolean z2;
        boolean z3 = !z;
        synchronized (this.writer) {
            try {
                synchronized (this) {
                    try {
                        if (this.nextStreamId > 1073741823) {
                            shutdown(ErrorCode.REFUSED_STREAM);
                        }
                        if (this.shutdown) {
                            throw new ConnectionShutdownException();
                        }
                        i2 = this.nextStreamId;
                        this.nextStreamId = i2 + 2;
                        http2Stream = new Http2Stream(i2, this, z3, $assertionsDisabled, null);
                        z2 = (!z || this.bytesLeftInWriteWindow == 0 || http2Stream.bytesLeftInWriteWindow == 0) ? true : $assertionsDisabled;
                        if (http2Stream.isOpen()) {
                            this.streams.put(Integer.valueOf(i2), http2Stream);
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (i == 0) {
                    this.writer.w(z3, i2, i, list);
                } else {
                    if (this.client) {
                        throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                    }
                    this.writer.s(i, i2, list);
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
        if (z2) {
            this.writer.flush();
        }
        return http2Stream;
    }

    void close(ErrorCode errorCode, ErrorCode errorCode2) throws IOException {
        Http2Stream[] http2StreamArr = null;
        try {
            shutdown(errorCode);
            e = null;
        } catch (IOException e2) {
            e = e2;
        }
        synchronized (this) {
            try {
                if (!this.streams.isEmpty()) {
                    http2StreamArr = (Http2Stream[]) this.streams.values().toArray(new Http2Stream[this.streams.size()]);
                    this.streams.clear();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (http2StreamArr != null) {
            for (Http2Stream http2Stream : http2StreamArr) {
                try {
                    http2Stream.close(errorCode2);
                } catch (IOException e3) {
                    if (e != null) {
                        e = e3;
                    }
                }
            }
        }
        try {
            this.writer.close();
        } catch (IOException e4) {
            if (e == null) {
                e = e4;
            }
        }
        try {
            this.socket.close();
        } catch (IOException e5) {
            e = e5;
        }
        this.writerExecutor.shutdown();
        this.pushExecutor.shutdown();
        if (e != null) {
            throw e;
        }
    }

    void start(boolean z) {
        if (z) {
            this.writer.e();
            this.writer.u(this.okHttpSettings);
            if (this.okHttpSettings.getInitialWindowSize() != 65535) {
                this.writer.x(0, r5 - 65535);
            }
        }
        new Thread(this.readerRunnable).start();
    }

    public static class Builder {
        boolean client;
        String hostname;
        int pingIntervalMillis;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;
        Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        PushObserver pushObserver = PushObserver.CANCEL;

        public Builder(boolean z) {
            this.client = z;
        }

        public Http2Connection build() {
            return new Http2Connection(this);
        }

        public Builder listener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder pingIntervalMillis(int i) {
            this.pingIntervalMillis = i;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }

        public Builder socket(Socket socket) {
            return socket(socket, ((InetSocketAddress) socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }

        public Builder socket(Socket socket, String str, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            this.socket = socket;
            this.hostname = str;
            this.source = bufferedSource;
            this.sink = bufferedSink;
            return this;
        }
    }
}
