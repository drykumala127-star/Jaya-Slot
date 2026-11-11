package org.cocos2dx.okhttp3.internal.http2;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.Headers;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.http2.Header;
import org.cocos2dx.okio.AsyncTimeout;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;
import org.cocos2dx.okio.Timeout;

public final class Http2Stream {
    static final boolean $assertionsDisabled = false;
    long bytesLeftInWriteWindow;
    final Http2Connection connection;
    ErrorCode errorCode;
    private boolean hasResponseHeaders;
    private Header.a headersListener;
    private final Deque headersQueue;
    final int id;
    final c readTimeout;
    final a sink;
    private final b source;
    long unacknowledgedBytesRead = 0;
    final c writeTimeout;

    final class a implements Sink {
        private final Buffer i = new Buffer();
        boolean j;
        boolean k;

        a() {
        }

        private void a(boolean z) throws IOException {
            Http2Stream http2Stream;
            long jMin;
            Http2Stream http2Stream2;
            synchronized (Http2Stream.this) {
                Http2Stream.this.writeTimeout.enter();
                while (true) {
                    try {
                        http2Stream = Http2Stream.this;
                        if (http2Stream.bytesLeftInWriteWindow > 0 || this.k || this.j || http2Stream.errorCode != null) {
                            break;
                        } else {
                            http2Stream.waitForIo();
                        }
                    } catch (Throwable th) {
                        Http2Stream.this.writeTimeout.a();
                        throw th;
                    }
                }
                http2Stream.writeTimeout.a();
                Http2Stream.this.checkOutNotClosed();
                jMin = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.i.size());
                http2Stream2 = Http2Stream.this;
                http2Stream2.bytesLeftInWriteWindow -= jMin;
            }
            http2Stream2.writeTimeout.enter();
            try {
                Http2Stream http2Stream3 = Http2Stream.this;
                http2Stream3.connection.writeData(http2Stream3.id, z && jMin == this.i.size(), this.i, jMin);
            } finally {
                Http2Stream.this.writeTimeout.a();
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (Http2Stream.this) {
                try {
                    if (this.j) {
                        return;
                    }
                    if (!Http2Stream.this.sink.k) {
                        if (this.i.size() > 0) {
                            while (this.i.size() > 0) {
                                a(true);
                            }
                        } else {
                            Http2Stream http2Stream = Http2Stream.this;
                            http2Stream.connection.writeData(http2Stream.id, true, null, 0L);
                        }
                    }
                    synchronized (Http2Stream.this) {
                        this.j = true;
                    }
                    Http2Stream.this.connection.flush();
                    Http2Stream.this.cancelStreamIfNecessary();
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public void flush() throws IOException {
            synchronized (Http2Stream.this) {
                Http2Stream.this.checkOutNotClosed();
            }
            while (this.i.size() > 0) {
                a(false);
                Http2Stream.this.connection.flush();
            }
        }

        @Override
        public Timeout timeout() {
            return Http2Stream.this.writeTimeout;
        }

        @Override
        public void write(Buffer buffer, long j) throws IOException {
            this.i.write(buffer, j);
            while (this.i.size() >= 16384) {
                a(false);
            }
        }
    }

    private final class b implements Source {
        private final Buffer i = new Buffer();
        private final Buffer j = new Buffer();
        private final long k;
        boolean l;
        boolean m;

        b(long j) {
            this.k = j;
        }

        private void e(long j) {
            Http2Stream.this.connection.updateConnectionFlowControl(j);
        }

        void a(BufferedSource bufferedSource, long j) throws EOFException {
            boolean z;
            boolean z2;
            long size;
            while (j > 0) {
                synchronized (Http2Stream.this) {
                    z = this.m;
                    z2 = this.j.size() + j > this.k;
                }
                if (z2) {
                    bufferedSource.skip(j);
                    Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                }
                if (z) {
                    bufferedSource.skip(j);
                    return;
                }
                long j2 = bufferedSource.read(this.i, j);
                if (j2 == -1) {
                    throw new EOFException();
                }
                j -= j2;
                synchronized (Http2Stream.this) {
                    try {
                        if (this.l) {
                            size = this.i.size();
                            this.i.clear();
                        } else {
                            boolean z3 = this.j.size() == 0;
                            this.j.writeAll(this.i);
                            if (z3) {
                                Http2Stream.this.notifyAll();
                            }
                            size = 0;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (size > 0) {
                    e(size);
                }
            }
        }

        @Override
        public void close() {
            long size;
            synchronized (Http2Stream.this) {
                try {
                    this.l = true;
                    size = this.j.size();
                    this.j.clear();
                    if (!Http2Stream.access$000(Http2Stream.this).isEmpty()) {
                        Http2Stream.access$100(Http2Stream.this);
                    }
                    Http2Stream.this.notifyAll();
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (size > 0) {
                e(size);
            }
            Http2Stream.this.cancelStreamIfNecessary();
        }

        @Override
        public long read(Buffer buffer, long j) throws StreamResetException {
            ErrorCode errorCode;
            long j2;
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            while (true) {
                synchronized (Http2Stream.this) {
                    try {
                        Http2Stream.this.readTimeout.enter();
                        try {
                            Http2Stream http2Stream = Http2Stream.this;
                            errorCode = http2Stream.errorCode;
                            if (errorCode == null) {
                                errorCode = null;
                            }
                            if (this.l) {
                                throw new IOException("stream closed");
                            }
                            if (!Http2Stream.access$000(http2Stream).isEmpty()) {
                                Http2Stream.access$100(Http2Stream.this);
                            }
                            if (this.j.size() > 0) {
                                Buffer buffer2 = this.j;
                                j2 = buffer2.read(buffer, Math.min(j, buffer2.size()));
                                Http2Stream http2Stream2 = Http2Stream.this;
                                long j3 = http2Stream2.unacknowledgedBytesRead + j2;
                                http2Stream2.unacknowledgedBytesRead = j3;
                                if (errorCode != null || j3 < http2Stream2.connection.okHttpSettings.getInitialWindowSize() / 2) {
                                    break;
                                    break;
                                }
                                Http2Stream http2Stream3 = Http2Stream.this;
                                http2Stream3.connection.writeWindowUpdateLater(http2Stream3.id, http2Stream3.unacknowledgedBytesRead);
                                Http2Stream.this.unacknowledgedBytesRead = 0L;
                                break;
                            }
                            if (this.m || errorCode != null) {
                                j2 = -1;
                                break;
                            }
                            Http2Stream.this.waitForIo();
                            Http2Stream.this.readTimeout.a();
                        } catch (Throwable th) {
                            Http2Stream.this.readTimeout.a();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        throw th2;
                    }
                }
                if (j2 != -1) {
                    e(j2);
                    return j2;
                }
                if (errorCode == null) {
                    return -1L;
                }
                throw new StreamResetException(errorCode);
            }
            Http2Stream.this.readTimeout.a();
            if (j2 != -1) {
                e(j2);
                return j2;
            }
            if (errorCode == null) {
                return -1L;
            }
            throw new StreamResetException(errorCode);
        }

        @Override
        public Timeout timeout() {
            return Http2Stream.this.readTimeout;
        }
    }

    class c extends AsyncTimeout {
        c() {
        }

        public void a() throws IOException {
            if (exit()) {
                throw newTimeoutException(null);
            }
        }

        @Override
        protected IOException newTimeoutException(IOException iOException) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        @Override
        protected void timedOut() {
            Http2Stream.this.closeLater(ErrorCode.CANCEL);
        }
    }

    Http2Stream(int i, Http2Connection http2Connection, boolean z, boolean z2, @Nullable Headers headers) {
        ArrayDeque arrayDeque = new ArrayDeque();
        this.headersQueue = arrayDeque;
        this.readTimeout = new c();
        this.writeTimeout = new c();
        this.errorCode = null;
        if (http2Connection == null) {
            throw new NullPointerException("connection == null");
        }
        this.id = i;
        this.connection = http2Connection;
        this.bytesLeftInWriteWindow = http2Connection.peerSettings.getInitialWindowSize();
        b bVar = new b(http2Connection.okHttpSettings.getInitialWindowSize());
        this.source = bVar;
        a aVar = new a();
        this.sink = aVar;
        bVar.m = z2;
        aVar.k = z;
        if (headers != null) {
            arrayDeque.add(headers);
        }
        if (isLocallyInitiated() && headers != null) {
            throw new IllegalStateException("locally-initiated streams shouldn't have headers yet");
        }
        if (!isLocallyInitiated() && headers == null) {
            throw new IllegalStateException("remotely-initiated streams should have headers");
        }
    }

    static Deque access$000(Http2Stream http2Stream) {
        return http2Stream.headersQueue;
    }

    static Header.a access$100(Http2Stream http2Stream) {
        http2Stream.getClass();
        return null;
    }

    private boolean closeInternal(ErrorCode errorCode) {
        synchronized (this) {
            try {
                if (this.errorCode != null) {
                    return false;
                }
                if (this.source.m && this.sink.k) {
                    return false;
                }
                this.errorCode = errorCode;
                notifyAll();
                this.connection.removeStream(this.id);
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    void cancelStreamIfNecessary() {
        boolean z;
        boolean zIsOpen;
        synchronized (this) {
            try {
                b bVar = this.source;
                if (!bVar.m && bVar.l) {
                    a aVar = this.sink;
                    z = aVar.k || aVar.j;
                }
                zIsOpen = isOpen();
            } catch (Throwable th) {
                throw th;
            }
        }
        if (z) {
            close(ErrorCode.CANCEL);
        } else {
            if (zIsOpen) {
                return;
            }
            this.connection.removeStream(this.id);
        }
    }

    void checkOutNotClosed() throws IOException {
        a aVar = this.sink;
        if (aVar.j) {
            throw new IOException("stream closed");
        }
        if (aVar.k) {
            throw new IOException("stream finished");
        }
        if (this.errorCode != null) {
            throw new StreamResetException(this.errorCode);
        }
    }

    public void close(ErrorCode errorCode) {
        if (closeInternal(errorCode)) {
            this.connection.writeSynReset(this.id, errorCode);
        }
    }

    public void closeLater(ErrorCode errorCode) {
        if (closeInternal(errorCode)) {
            this.connection.writeSynResetLater(this.id, errorCode);
        }
    }

    public Http2Connection getConnection() {
        return this.connection;
    }

    public synchronized ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public int getId() {
        return this.id;
    }

    public Sink getSink() {
        synchronized (this) {
            try {
                if (!this.hasResponseHeaders && !isLocallyInitiated()) {
                    throw new IllegalStateException("reply before requesting the sink");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return this.sink;
    }

    public Source getSource() {
        return this.source;
    }

    public boolean isLocallyInitiated() {
        return this.connection.client == ((this.id & 1) == 1);
    }

    public synchronized boolean isOpen() {
        try {
            if (this.errorCode != null) {
                return false;
            }
            b bVar = this.source;
            if (bVar.m || bVar.l) {
                a aVar = this.sink;
                if ((aVar.k || aVar.j) && this.hasResponseHeaders) {
                    return false;
                }
            }
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    void receiveData(BufferedSource bufferedSource, int i) {
        this.source.a(bufferedSource, i);
    }

    void receiveFin() {
        boolean zIsOpen;
        synchronized (this) {
            this.source.m = true;
            zIsOpen = isOpen();
            notifyAll();
        }
        if (zIsOpen) {
            return;
        }
        this.connection.removeStream(this.id);
    }

    void receiveHeaders(List list) {
        boolean zIsOpen;
        synchronized (this) {
            this.hasResponseHeaders = true;
            this.headersQueue.add(Util.toHeaders(list));
            zIsOpen = isOpen();
            notifyAll();
        }
        if (zIsOpen) {
            return;
        }
        this.connection.removeStream(this.id);
    }

    synchronized void receiveRstStream(ErrorCode errorCode) {
        if (this.errorCode == null) {
            this.errorCode = errorCode;
            notifyAll();
        }
    }

    public synchronized void setHeadersListener(Header.a aVar) {
        if (!this.headersQueue.isEmpty() && aVar != null) {
            notifyAll();
        }
    }

    public synchronized Headers takeHeaders() {
        this.readTimeout.enter();
        while (this.headersQueue.isEmpty() && this.errorCode == null) {
            try {
                waitForIo();
            } catch (Throwable th) {
                this.readTimeout.a();
                throw th;
            }
        }
        this.readTimeout.a();
        if (this.headersQueue.isEmpty()) {
            throw new StreamResetException(this.errorCode);
        }
        return (Headers) this.headersQueue.removeFirst();
    }

    void waitForIo() throws InterruptedIOException {
        try {
            wait();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        }
    }

    public void writeHeaders(List list, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        if (list == null) {
            throw new NullPointerException("headers == null");
        }
        synchronized (this) {
            z2 = true;
            try {
                this.hasResponseHeaders = true;
                if (z) {
                    z3 = false;
                    z4 = false;
                } else {
                    this.sink.k = true;
                    z3 = true;
                    z4 = true;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (!z3) {
            synchronized (this.connection) {
                if (this.connection.bytesLeftInWriteWindow != 0) {
                    z2 = false;
                }
            }
            z3 = z2;
        }
        this.connection.writeSynReply(this.id, z4, list);
        if (z3) {
            this.connection.flush();
        }
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }
}
