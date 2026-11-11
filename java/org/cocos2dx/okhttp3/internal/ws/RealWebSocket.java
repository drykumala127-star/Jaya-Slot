package org.cocos2dx.okhttp3.internal.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.Call;
import org.cocos2dx.okhttp3.Callback;
import org.cocos2dx.okhttp3.EventListener;
import org.cocos2dx.okhttp3.OkHttpClient;
import org.cocos2dx.okhttp3.Protocol;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.WebSocket;
import org.cocos2dx.okhttp3.WebSocketListener;
import org.cocos2dx.okhttp3.internal.Internal;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.connection.StreamAllocation;
import org.cocos2dx.okhttp3.internal.ws.WebSocketReader;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.Okio;

public final class RealWebSocket implements WebSocket, WebSocketReader.FrameCallback {
    static final boolean $assertionsDisabled = false;
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000;
    private static final long MAX_QUEUE_SIZE = 16777216;
    private static final List ONLY_HTTP1 = Collections.singletonList(Protocol.HTTP_1_1);
    private boolean awaitingPong;
    private Call call;
    private ScheduledFuture cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    final WebSocketListener listener;
    private final Request originalRequest;
    private final long pingIntervalMillis;
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private String receivedCloseReason;
    private int receivedPingCount;
    private int receivedPongCount;
    private int sentPingCount;
    private Streams streams;
    private org.cocos2dx.okhttp3.internal.ws.a writer;
    private final Runnable writerRunnable;
    private final ArrayDeque pongQueue = new ArrayDeque();
    private final ArrayDeque messageAndCloseQueue = new ArrayDeque();
    private int receivedCloseCode = -1;

    public static abstract class Streams implements Closeable {
        public final boolean client;
        public final BufferedSink sink;
        public final BufferedSource source;

        public Streams(boolean z, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            this.client = z;
            this.source = bufferedSource;
            this.sink = bufferedSink;
        }
    }

    class a implements Runnable {
        a() {
        }

        @Override
        public void run() {
            do {
                try {
                } catch (IOException e) {
                    RealWebSocket.this.failWebSocket(e, null);
                    return;
                }
            } while (RealWebSocket.this.writeOneFrame());
        }
    }

    class b implements Callback {
        final Request a;

        b(Request request) {
            this.a = request;
        }

        @Override
        public void onFailure(Call call, IOException iOException) {
            RealWebSocket.this.failWebSocket(iOException, null);
        }

        @Override
        public void onResponse(Call call, Response response) {
            try {
                RealWebSocket.this.checkResponse(response);
                StreamAllocation streamAllocation = Internal.instance.streamAllocation(call);
                streamAllocation.noNewStreams();
                Streams streamsNewWebSocketStreams = streamAllocation.connection().newWebSocketStreams(streamAllocation);
                try {
                    RealWebSocket realWebSocket = RealWebSocket.this;
                    realWebSocket.listener.onOpen(realWebSocket, response);
                    RealWebSocket.this.initReaderAndWriter("OkHttp WebSocket " + this.a.url().redact(), streamsNewWebSocketStreams);
                    streamAllocation.connection().socket().setSoTimeout(0);
                    RealWebSocket.this.loopReader();
                } catch (Exception e) {
                    RealWebSocket.this.failWebSocket(e, null);
                }
            } catch (ProtocolException e2) {
                RealWebSocket.this.failWebSocket(e2, response);
                Util.closeQuietly(response);
            }
        }
    }

    final class c implements Runnable {
        c() {
        }

        @Override
        public void run() {
            RealWebSocket.this.cancel();
        }
    }

    static final class d {
        final int a;
        final ByteString b;
        final long c;

        d(int i, ByteString byteString, long j) {
            this.a = i;
            this.b = byteString;
            this.c = j;
        }
    }

    static final class e {
        final int a;
        final ByteString b;

        e(int i, ByteString byteString) {
            this.a = i;
            this.b = byteString;
        }
    }

    private final class f implements Runnable {
        f() {
        }

        @Override
        public void run() {
            RealWebSocket.this.writePingFrame();
        }
    }

    public RealWebSocket(Request request, WebSocketListener webSocketListener, Random random, long j) {
        if (!"GET".equals(request.method())) {
            throw new IllegalArgumentException("Request must be GET: " + request.method());
        }
        this.originalRequest = request;
        this.listener = webSocketListener;
        this.random = random;
        this.pingIntervalMillis = j;
        byte[] bArr = new byte[16];
        random.nextBytes(bArr);
        this.key = ByteString.of(bArr).base64();
        this.writerRunnable = new a();
    }

    private void runWriter() {
        ScheduledExecutorService scheduledExecutorService = this.executor;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.execute(this.writerRunnable);
        }
    }

    void awaitTermination(int i, TimeUnit timeUnit) throws InterruptedException {
        this.executor.awaitTermination(i, timeUnit);
    }

    @Override
    public void cancel() {
        this.call.cancel();
    }

    void checkResponse(Response response) throws ProtocolException {
        if (response.code() != 101) {
            throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
        }
        String strHeader = response.header("Connection");
        if (!"Upgrade".equalsIgnoreCase(strHeader)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + strHeader + "'");
        }
        String strHeader2 = response.header("Upgrade");
        if (!"websocket".equalsIgnoreCase(strHeader2)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + strHeader2 + "'");
        }
        String strHeader3 = response.header("Sec-WebSocket-Accept");
        String strBase64 = ByteString.encodeUtf8(this.key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").sha1().base64();
        if (strBase64.equals(strHeader3)) {
            return;
        }
        throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + strBase64 + "' but was '" + strHeader3 + "'");
    }

    @Override
    public boolean close(int i, String str) {
        return close(i, str, CANCEL_AFTER_CLOSE_MILLIS);
    }

    public void connect(OkHttpClient okHttpClient) {
        OkHttpClient okHttpClientBuild = okHttpClient.newBuilder().eventListener(EventListener.NONE).protocols(ONLY_HTTP1).build();
        Request requestBuild = this.originalRequest.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        Call callNewWebSocketCall = Internal.instance.newWebSocketCall(okHttpClientBuild, requestBuild);
        this.call = callNewWebSocketCall;
        callNewWebSocketCall.timeout().clearTimeout();
        this.call.enqueue(new b(requestBuild));
    }

    public void failWebSocket(Exception exc, @Nullable Response response) {
        synchronized (this) {
            try {
                if (this.failed) {
                    return;
                }
                this.failed = true;
                Streams streams = this.streams;
                this.streams = null;
                ScheduledFuture scheduledFuture = this.cancelFuture;
                if (scheduledFuture != null) {
                    scheduledFuture.cancel($assertionsDisabled);
                }
                ScheduledExecutorService scheduledExecutorService = this.executor;
                if (scheduledExecutorService != null) {
                    scheduledExecutorService.shutdown();
                }
                try {
                    this.listener.onFailure(this, exc, response);
                } finally {
                    Util.closeQuietly(streams);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void initReaderAndWriter(String str, Streams streams) {
        synchronized (this) {
            try {
                this.streams = streams;
                this.writer = new org.cocos2dx.okhttp3.internal.ws.a(streams.client, streams.sink, this.random);
                ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(str, $assertionsDisabled));
                this.executor = scheduledThreadPoolExecutor;
                if (this.pingIntervalMillis != 0) {
                    f fVar = new f();
                    long j = this.pingIntervalMillis;
                    scheduledThreadPoolExecutor.scheduleAtFixedRate(fVar, j, j, TimeUnit.MILLISECONDS);
                }
                if (!this.messageAndCloseQueue.isEmpty()) {
                    runWriter();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        this.reader = new WebSocketReader(streams.client, streams.source, this);
    }

    public void loopReader() {
        while (this.receivedCloseCode == -1) {
            this.reader.a();
        }
    }

    @Override
    public void onReadClose(int i, String str) {
        Streams streams;
        if (i == -1) {
            throw new IllegalArgumentException();
        }
        synchronized (this) {
            try {
                if (this.receivedCloseCode != -1) {
                    throw new IllegalStateException("already closed");
                }
                this.receivedCloseCode = i;
                this.receivedCloseReason = str;
                streams = null;
                if (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
                    Streams streams2 = this.streams;
                    this.streams = null;
                    ScheduledFuture scheduledFuture = this.cancelFuture;
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel($assertionsDisabled);
                    }
                    this.executor.shutdown();
                    streams = streams2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        try {
            this.listener.onClosing(this, i, str);
            if (streams != null) {
                this.listener.onClosed(this, i, str);
            }
        } finally {
            Util.closeQuietly(streams);
        }
    }

    @Override
    public void onReadMessage(String str) {
        this.listener.onMessage(this, str);
    }

    @Override
    public synchronized void onReadPing(ByteString byteString) {
        try {
            if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
                this.pongQueue.add(byteString);
                runWriter();
                this.receivedPingCount++;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override
    public synchronized void onReadPong(ByteString byteString) {
        this.receivedPongCount++;
        this.awaitingPong = $assertionsDisabled;
    }

    synchronized boolean pong(ByteString byteString) {
        try {
            if (!this.failed && (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty())) {
                this.pongQueue.add(byteString);
                runWriter();
                return true;
            }
            return $assertionsDisabled;
        } catch (Throwable th) {
            throw th;
        }
    }

    boolean processNextFrame() {
        try {
            this.reader.a();
            if (this.receivedCloseCode == -1) {
                return true;
            }
            return $assertionsDisabled;
        } catch (Exception e2) {
            failWebSocket(e2, null);
            return $assertionsDisabled;
        }
    }

    @Override
    public synchronized long queueSize() {
        return this.queueSize;
    }

    synchronized int receivedPingCount() {
        return this.receivedPingCount;
    }

    synchronized int receivedPongCount() {
        return this.receivedPongCount;
    }

    @Override
    public Request request() {
        return this.originalRequest;
    }

    @Override
    public boolean send(String str) {
        if (str != null) {
            return send(ByteString.encodeUtf8(str), 1);
        }
        throw new NullPointerException("text == null");
    }

    synchronized int sentPingCount() {
        return this.sentPingCount;
    }

    void tearDown() throws InterruptedException {
        ScheduledFuture scheduledFuture = this.cancelFuture;
        if (scheduledFuture != null) {
            scheduledFuture.cancel($assertionsDisabled);
        }
        this.executor.shutdown();
        this.executor.awaitTermination(10L, TimeUnit.SECONDS);
    }

    boolean writeOneFrame() {
        Streams streams;
        String str;
        int i;
        ByteString byteString;
        synchronized (this) {
            try {
                if (this.failed) {
                    return $assertionsDisabled;
                }
                org.cocos2dx.okhttp3.internal.ws.a aVar = this.writer;
                ByteString byteString2 = (ByteString) this.pongQueue.poll();
                Object obj = null;
                try {
                    if (byteString2 == null) {
                        Object objPoll = this.messageAndCloseQueue.poll();
                        if (objPoll instanceof d) {
                            i = this.receivedCloseCode;
                            str = this.receivedCloseReason;
                            if (i != -1) {
                                streams = this.streams;
                                this.streams = null;
                                this.executor.shutdown();
                            } else {
                                this.cancelFuture = this.executor.schedule(new c(), ((d) objPoll).c, TimeUnit.MILLISECONDS);
                                streams = null;
                            }
                            obj = objPoll;
                        } else {
                            if (objPoll == null) {
                                return $assertionsDisabled;
                            }
                            streams = null;
                            str = null;
                            obj = objPoll;
                        }
                        if (byteString2 != null) {
                            aVar.f(byteString2);
                        } else if (obj instanceof e) {
                            byteString = ((e) obj).b;
                            BufferedSink bufferedSinkBuffer = Okio.buffer(aVar.a(((e) obj).a, byteString.size()));
                            bufferedSinkBuffer.write(byteString);
                            bufferedSinkBuffer.close();
                            synchronized (this) {
                                try {
                                    this.queueSize -= (long) byteString.size();
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                        } else {
                            if (obj instanceof d) {
                                throw new AssertionError();
                            }
                            d dVar = (d) obj;
                            aVar.b(dVar.a, dVar.b);
                            if (streams != null) {
                                this.listener.onClosed(this, i, str);
                            }
                        }
                        Util.closeQuietly(streams);
                        return true;
                    }
                    streams = null;
                    str = null;
                    if (byteString2 != null) {
                        aVar.f(byteString2);
                    } else if (obj instanceof e) {
                        byteString = ((e) obj).b;
                        BufferedSink bufferedSinkBuffer2 = Okio.buffer(aVar.a(((e) obj).a, byteString.size()));
                        bufferedSinkBuffer2.write(byteString);
                        bufferedSinkBuffer2.close();
                        synchronized (this) {
                            this.queueSize -= (long) byteString.size();
                        }
                    } else {
                        if (obj instanceof d) {
                            throw new AssertionError();
                        }
                        d dVar2 = (d) obj;
                        aVar.b(dVar2.a, dVar2.b);
                        if (streams != null) {
                            this.listener.onClosed(this, i, str);
                        }
                    }
                    Util.closeQuietly(streams);
                    return true;
                } catch (Throwable th2) {
                    Util.closeQuietly(streams);
                    throw th2;
                }
                i = -1;
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    void writePingFrame() {
        synchronized (this) {
            try {
                if (this.failed) {
                    return;
                }
                org.cocos2dx.okhttp3.internal.ws.a aVar = this.writer;
                int i = this.awaitingPong ? this.sentPingCount : -1;
                this.sentPingCount++;
                this.awaitingPong = true;
                if (i == -1) {
                    try {
                        aVar.e(ByteString.EMPTY);
                        return;
                    } catch (IOException e2) {
                        failWebSocket(e2, null);
                        return;
                    }
                }
                failWebSocket(new SocketTimeoutException("sent ping but didn't receive pong within " + this.pingIntervalMillis + "ms (after " + (i - 1) + " successful ping/pongs)"), null);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    synchronized boolean close(int i, String str, long j) {
        ByteString byteStringEncodeUtf8;
        try {
            WebSocketProtocol.validateCloseCode(i);
            if (str != null) {
                byteStringEncodeUtf8 = ByteString.encodeUtf8(str);
                if (byteStringEncodeUtf8.size() > 123) {
                    throw new IllegalArgumentException("reason.size() > 123: " + str);
                }
            } else {
                byteStringEncodeUtf8 = null;
            }
            if (!this.failed && !this.enqueuedClose) {
                this.enqueuedClose = true;
                this.messageAndCloseQueue.add(new d(i, byteStringEncodeUtf8, j));
                runWriter();
                return true;
            }
            return $assertionsDisabled;
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override
    public void onReadMessage(ByteString byteString) {
        this.listener.onMessage(this, byteString);
    }

    @Override
    public boolean send(ByteString byteString) {
        if (byteString != null) {
            return send(byteString, 2);
        }
        throw new NullPointerException("bytes == null");
    }

    private synchronized boolean send(ByteString byteString, int i) {
        if (!this.failed && !this.enqueuedClose) {
            if (this.queueSize + ((long) byteString.size()) > MAX_QUEUE_SIZE) {
                close(1001, null);
                return $assertionsDisabled;
            }
            this.queueSize += (long) byteString.size();
            this.messageAndCloseQueue.add(new e(i, byteString));
            runWriter();
            return true;
        }
        return $assertionsDisabled;
    }
}
