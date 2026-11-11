package org.cocos2dx.okhttp3.internal.http1;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.Headers;
import org.cocos2dx.okhttp3.HttpUrl;
import org.cocos2dx.okhttp3.OkHttpClient;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.ResponseBody;
import org.cocos2dx.okhttp3.internal.Internal;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.connection.RealConnection;
import org.cocos2dx.okhttp3.internal.connection.StreamAllocation;
import org.cocos2dx.okhttp3.internal.http.HttpCodec;
import org.cocos2dx.okhttp3.internal.http.HttpHeaders;
import org.cocos2dx.okhttp3.internal.http.RealResponseBody;
import org.cocos2dx.okhttp3.internal.http.RequestLine;
import org.cocos2dx.okhttp3.internal.http.StatusLine;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ForwardingTimeout;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;
import org.cocos2dx.okio.Timeout;

public final class Http1Codec implements HttpCodec {
    private static final int HEADER_LIMIT = 262144;
    private static final int STATE_CLOSED = 6;
    private static final int STATE_IDLE = 0;
    private static final int STATE_OPEN_REQUEST_BODY = 1;
    private static final int STATE_OPEN_RESPONSE_BODY = 4;
    private static final int STATE_READING_RESPONSE_BODY = 5;
    private static final int STATE_READ_RESPONSE_HEADERS = 3;
    private static final int STATE_WRITING_REQUEST_BODY = 2;
    final OkHttpClient client;
    final BufferedSink sink;
    final BufferedSource source;
    final StreamAllocation streamAllocation;
    int state = STATE_IDLE;
    private long headerLimit = 262144;

    private final class c implements Sink {
        private final ForwardingTimeout i;
        private boolean j;

        c() {
            this.i = new ForwardingTimeout(Http1Codec.this.sink.timeout());
        }

        @Override
        public synchronized void close() {
            if (this.j) {
                return;
            }
            this.j = true;
            Http1Codec.this.sink.writeUtf8("0\r\n\r\n");
            Http1Codec.this.detachTimeout(this.i);
            Http1Codec.this.state = Http1Codec.STATE_READ_RESPONSE_HEADERS;
        }

        @Override
        public synchronized void flush() {
            if (this.j) {
                return;
            }
            Http1Codec.this.sink.flush();
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }

        @Override
        public void write(Buffer buffer, long j) {
            if (this.j) {
                throw new IllegalStateException("closed");
            }
            if (j == 0) {
                return;
            }
            Http1Codec.this.sink.writeHexadecimalUnsignedLong(j);
            Http1Codec.this.sink.writeUtf8("\r\n");
            Http1Codec.this.sink.write(buffer, j);
            Http1Codec.this.sink.writeUtf8("\r\n");
        }
    }

    private class d extends b {
        private final HttpUrl m;
        private long n;
        private boolean o;

        d(HttpUrl httpUrl) {
            super(Http1Codec.this, null);
            this.n = -1L;
            this.o = true;
            this.m = httpUrl;
        }

        private void e() throws ProtocolException {
            if (this.n != -1) {
                Http1Codec.this.source.readUtf8LineStrict();
            }
            try {
                this.n = Http1Codec.this.source.readHexadecimalUnsignedLong();
                String strTrim = Http1Codec.this.source.readUtf8LineStrict().trim();
                if (this.n < 0 || !(strTrim.isEmpty() || strTrim.startsWith(";"))) {
                    throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.n + strTrim + "\"");
                }
                if (this.n == 0) {
                    this.o = false;
                    HttpHeaders.receiveHeaders(Http1Codec.this.client.cookieJar(), this.m, Http1Codec.this.readHeaders());
                    a(true, null);
                }
            } catch (NumberFormatException e) {
                throw new ProtocolException(e.getMessage());
            }
        }

        @Override
        public void close() {
            if (this.j) {
                return;
            }
            if (this.o && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                a(false, null);
            }
            this.j = true;
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            if (this.j) {
                throw new IllegalStateException("closed");
            }
            if (!this.o) {
                return -1L;
            }
            long j2 = this.n;
            if (j2 == 0 || j2 == -1) {
                e();
                if (!this.o) {
                    return -1L;
                }
            }
            long j3 = super.read(buffer, Math.min(j, this.n));
            if (j3 != -1) {
                this.n -= j3;
                return j3;
            }
            ProtocolException protocolException = new ProtocolException("unexpected end of stream");
            a(false, protocolException);
            throw protocolException;
        }
    }

    private final class e implements Sink {
        private final ForwardingTimeout i;
        private boolean j;
        private long k;

        e(long j) {
            this.i = new ForwardingTimeout(Http1Codec.this.sink.timeout());
            this.k = j;
        }

        @Override
        public void close() throws ProtocolException {
            if (this.j) {
                return;
            }
            this.j = true;
            if (this.k > 0) {
                throw new ProtocolException("unexpected end of stream");
            }
            Http1Codec.this.detachTimeout(this.i);
            Http1Codec.this.state = Http1Codec.STATE_READ_RESPONSE_HEADERS;
        }

        @Override
        public void flush() {
            if (this.j) {
                return;
            }
            Http1Codec.this.sink.flush();
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }

        @Override
        public void write(Buffer buffer, long j) throws ProtocolException {
            if (this.j) {
                throw new IllegalStateException("closed");
            }
            Util.checkOffsetAndCount(buffer.size(), 0L, j);
            if (j <= this.k) {
                Http1Codec.this.sink.write(buffer, j);
                this.k -= j;
                return;
            }
            throw new ProtocolException("expected " + this.k + " bytes but received " + j);
        }
    }

    private class f extends b {
        private long m;

        f(long j) {
            super(Http1Codec.this, null);
            this.m = j;
            if (j == 0) {
                a(true, null);
            }
        }

        @Override
        public void close() {
            if (this.j) {
                return;
            }
            if (this.m != 0 && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                a(false, null);
            }
            this.j = true;
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            if (this.j) {
                throw new IllegalStateException("closed");
            }
            long j2 = this.m;
            if (j2 == 0) {
                return -1L;
            }
            long j3 = super.read(buffer, Math.min(j2, j));
            if (j3 == -1) {
                ProtocolException protocolException = new ProtocolException("unexpected end of stream");
                a(false, protocolException);
                throw protocolException;
            }
            long j4 = this.m - j3;
            this.m = j4;
            if (j4 == 0) {
                a(true, null);
            }
            return j3;
        }
    }

    private class g extends b {
        private boolean m;

        g() {
            super(Http1Codec.this, null);
        }

        @Override
        public void close() {
            if (this.j) {
                return;
            }
            if (!this.m) {
                a(false, null);
            }
            this.j = true;
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            if (this.j) {
                throw new IllegalStateException("closed");
            }
            if (this.m) {
                return -1L;
            }
            long j2 = super.read(buffer, j);
            if (j2 != -1) {
                return j2;
            }
            this.m = true;
            a(true, null);
            return -1L;
        }
    }

    public Http1Codec(OkHttpClient okHttpClient, StreamAllocation streamAllocation, BufferedSource bufferedSource, BufferedSink bufferedSink) {
        this.client = okHttpClient;
        this.streamAllocation = streamAllocation;
        this.source = bufferedSource;
        this.sink = bufferedSink;
    }

    private String readHeaderLine() {
        String utf8LineStrict = this.source.readUtf8LineStrict(this.headerLimit);
        this.headerLimit -= (long) utf8LineStrict.length();
        return utf8LineStrict;
    }

    @Override
    public void cancel() {
        RealConnection realConnectionConnection = this.streamAllocation.connection();
        if (realConnectionConnection != null) {
            realConnectionConnection.cancel();
        }
    }

    @Override
    public Sink createRequestBody(Request request, long j) {
        if ("chunked".equalsIgnoreCase(request.header("Transfer-Encoding"))) {
            return newChunkedSink();
        }
        if (j != -1) {
            return newFixedLengthSink(j);
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }

    void detachTimeout(ForwardingTimeout forwardingTimeout) {
        Timeout timeoutDelegate = forwardingTimeout.delegate();
        forwardingTimeout.setDelegate(Timeout.NONE);
        timeoutDelegate.clearDeadline();
        timeoutDelegate.clearTimeout();
    }

    @Override
    public void finishRequest() {
        this.sink.flush();
    }

    @Override
    public void flushRequest() {
        this.sink.flush();
    }

    public boolean isClosed() {
        return this.state == STATE_CLOSED;
    }

    public Sink newChunkedSink() {
        if (this.state == STATE_OPEN_REQUEST_BODY) {
            this.state = STATE_WRITING_REQUEST_BODY;
            return new c();
        }
        throw new IllegalStateException("state: " + this.state);
    }

    public Source newChunkedSource(HttpUrl httpUrl) {
        if (this.state == STATE_OPEN_RESPONSE_BODY) {
            this.state = STATE_READING_RESPONSE_BODY;
            return new d(httpUrl);
        }
        throw new IllegalStateException("state: " + this.state);
    }

    public Sink newFixedLengthSink(long j) {
        if (this.state == STATE_OPEN_REQUEST_BODY) {
            this.state = STATE_WRITING_REQUEST_BODY;
            return new e(j);
        }
        throw new IllegalStateException("state: " + this.state);
    }

    public Source newFixedLengthSource(long j) {
        if (this.state == STATE_OPEN_RESPONSE_BODY) {
            this.state = STATE_READING_RESPONSE_BODY;
            return new f(j);
        }
        throw new IllegalStateException("state: " + this.state);
    }

    public Source newUnknownLengthSource() {
        if (this.state != STATE_OPEN_RESPONSE_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation == null) {
            throw new IllegalStateException("streamAllocation == null");
        }
        this.state = STATE_READING_RESPONSE_BODY;
        streamAllocation.noNewStreams();
        return new g();
    }

    @Override
    public ResponseBody openResponseBody(Response response) {
        StreamAllocation streamAllocation = this.streamAllocation;
        streamAllocation.eventListener.responseBodyStart(streamAllocation.call);
        String strHeader = response.header("Content-Type");
        if (!HttpHeaders.hasBody(response)) {
            return new RealResponseBody(strHeader, 0L, Okio.buffer(newFixedLengthSource(0L)));
        }
        if ("chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return new RealResponseBody(strHeader, -1L, Okio.buffer(newChunkedSource(response.request().url())));
        }
        long jContentLength = HttpHeaders.contentLength(response);
        return jContentLength != -1 ? new RealResponseBody(strHeader, jContentLength, Okio.buffer(newFixedLengthSource(jContentLength))) : new RealResponseBody(strHeader, -1L, Okio.buffer(newUnknownLengthSource()));
    }

    public Headers readHeaders() {
        Headers.Builder builder = new Headers.Builder();
        while (true) {
            String headerLine = readHeaderLine();
            if (headerLine.length() == 0) {
                return builder.build();
            }
            Internal.instance.addLenient(builder, headerLine);
        }
    }

    @Override
    public Response.Builder readResponseHeaders(boolean z) throws IOException {
        int i = this.state;
        if (i != STATE_OPEN_REQUEST_BODY && i != STATE_READ_RESPONSE_HEADERS) {
            throw new IllegalStateException("state: " + this.state);
        }
        try {
            StatusLine statusLine = StatusLine.parse(readHeaderLine());
            Response.Builder builderHeaders = new Response.Builder().protocol(statusLine.protocol).code(statusLine.code).message(statusLine.message).headers(readHeaders());
            if (z && statusLine.code == 100) {
                return null;
            }
            if (statusLine.code == 100) {
                this.state = STATE_READ_RESPONSE_HEADERS;
                return builderHeaders;
            }
            this.state = STATE_OPEN_RESPONSE_BODY;
            return builderHeaders;
        } catch (EOFException e2) {
            IOException iOException = new IOException("unexpected end of stream on " + this.streamAllocation);
            iOException.initCause(e2);
            throw iOException;
        }
    }

    public void writeRequest(Headers headers, String str) {
        if (this.state != 0) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.sink.writeUtf8(str).writeUtf8("\r\n");
        int size = headers.size();
        for (int i = STATE_IDLE; i < size; i += STATE_OPEN_REQUEST_BODY) {
            this.sink.writeUtf8(headers.name(i)).writeUtf8(": ").writeUtf8(headers.value(i)).writeUtf8("\r\n");
        }
        this.sink.writeUtf8("\r\n");
        this.state = STATE_OPEN_REQUEST_BODY;
    }

    @Override
    public void writeRequestHeaders(Request request) {
        writeRequest(request.headers(), RequestLine.get(request, this.streamAllocation.connection().route().proxy().type()));
    }

    private abstract class b implements Source {
        protected final ForwardingTimeout i;
        protected boolean j;
        protected long k;

        private b() {
            this.i = new ForwardingTimeout(Http1Codec.this.source.timeout());
            this.k = 0L;
        }

        protected final void a(boolean z, IOException iOException) {
            Http1Codec http1Codec = Http1Codec.this;
            int i = http1Codec.state;
            if (i == Http1Codec.STATE_CLOSED) {
                return;
            }
            if (i != Http1Codec.STATE_READING_RESPONSE_BODY) {
                throw new IllegalStateException("state: " + Http1Codec.this.state);
            }
            http1Codec.detachTimeout(this.i);
            Http1Codec http1Codec2 = Http1Codec.this;
            http1Codec2.state = Http1Codec.STATE_CLOSED;
            StreamAllocation streamAllocation = http1Codec2.streamAllocation;
            if (streamAllocation != null) {
                streamAllocation.streamFinished(!z, http1Codec2, this.k, iOException);
            }
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            try {
                long j2 = Http1Codec.this.source.read(buffer, j);
                if (j2 > 0) {
                    this.k += j2;
                }
                return j2;
            } catch (IOException e) {
                a(false, e);
                throw e;
            }
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }

        b(Http1Codec http1Codec, a aVar) {
            this();
        }
    }
}
