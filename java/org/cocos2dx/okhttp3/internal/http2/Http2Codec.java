package org.cocos2dx.okhttp3.internal.http2;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.Headers;
import org.cocos2dx.okhttp3.Interceptor;
import org.cocos2dx.okhttp3.OkHttpClient;
import org.cocos2dx.okhttp3.Protocol;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.ResponseBody;
import org.cocos2dx.okhttp3.internal.Internal;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.connection.StreamAllocation;
import org.cocos2dx.okhttp3.internal.http.HttpCodec;
import org.cocos2dx.okhttp3.internal.http.HttpHeaders;
import org.cocos2dx.okhttp3.internal.http.RealResponseBody;
import org.cocos2dx.okhttp3.internal.http.RequestLine;
import org.cocos2dx.okhttp3.internal.http.StatusLine;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.ForwardingSource;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;
import org.cocos2dx.okio.Timeout;

public final class Http2Codec implements HttpCodec {
    private final Interceptor.Chain chain;
    private final Http2Connection connection;
    private final Protocol protocol;
    private Http2Stream stream;
    final StreamAllocation streamAllocation;
    private static final String CONNECTION = "connection";
    private static final String HOST = "host";
    private static final String KEEP_ALIVE = "keep-alive";
    private static final String PROXY_CONNECTION = "proxy-connection";
    private static final String TE = "te";
    private static final String TRANSFER_ENCODING = "transfer-encoding";
    private static final String ENCODING = "encoding";
    private static final String UPGRADE = "upgrade";
    private static final List HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE, ":method", ":path", ":scheme", ":authority");
    private static final List HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, TE, TRANSFER_ENCODING, ENCODING, UPGRADE);

    class a extends ForwardingSource {
        boolean i;
        long j;

        a(Source source) {
            super(source);
            this.i = false;
            this.j = 0L;
        }

        private void a(IOException iOException) {
            if (this.i) {
                return;
            }
            this.i = true;
            Http2Codec http2Codec = Http2Codec.this;
            http2Codec.streamAllocation.streamFinished(false, http2Codec, this.j, iOException);
        }

        @Override
        public void close() {
            super.close();
            a(null);
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            try {
                long j2 = delegate().read(buffer, j);
                if (j2 > 0) {
                    this.j += j2;
                }
                return j2;
            } catch (IOException e) {
                a(e);
                throw e;
            }
        }
    }

    public Http2Codec(OkHttpClient okHttpClient, Interceptor.Chain chain, StreamAllocation streamAllocation, Http2Connection http2Connection) {
        this.chain = chain;
        this.streamAllocation = streamAllocation;
        this.connection = http2Connection;
        List listProtocols = okHttpClient.protocols();
        Protocol protocol = Protocol.H2_PRIOR_KNOWLEDGE;
        this.protocol = listProtocols.contains(protocol) ? protocol : Protocol.HTTP_2;
    }

    public static List http2HeadersList(Request request) {
        Headers headers = request.headers();
        ArrayList arrayList = new ArrayList(headers.size() + 4);
        arrayList.add(new Header(Header.TARGET_METHOD, request.method()));
        arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(request.url())));
        String strHeader = request.header("Host");
        if (strHeader != null) {
            arrayList.add(new Header(Header.TARGET_AUTHORITY, strHeader));
        }
        arrayList.add(new Header(Header.TARGET_SCHEME, request.url().scheme()));
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            ByteString byteStringEncodeUtf8 = ByteString.encodeUtf8(headers.name(i).toLowerCase(Locale.US));
            if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(byteStringEncodeUtf8.utf8())) {
                arrayList.add(new Header(byteStringEncodeUtf8, headers.value(i)));
            }
        }
        return arrayList;
    }

    public static Response.Builder readHttp2HeadersList(Headers headers, Protocol protocol) throws ProtocolException {
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        StatusLine statusLine = null;
        for (int i = 0; i < size; i++) {
            String strName = headers.name(i);
            String strValue = headers.value(i);
            if (strName.equals(":status")) {
                statusLine = StatusLine.parse("HTTP/1.1 " + strValue);
            } else if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(strName)) {
                Internal.instance.addLenient(builder, strName, strValue);
            }
        }
        if (statusLine != null) {
            return new Response.Builder().protocol(protocol).code(statusLine.code).message(statusLine.message).headers(builder.build());
        }
        throw new ProtocolException("Expected ':status' header not present");
    }

    @Override
    public void cancel() {
        Http2Stream http2Stream = this.stream;
        if (http2Stream != null) {
            http2Stream.closeLater(ErrorCode.CANCEL);
        }
    }

    @Override
    public Sink createRequestBody(Request request, long j) {
        return this.stream.getSink();
    }

    @Override
    public void finishRequest() {
        this.stream.getSink().close();
    }

    @Override
    public void flushRequest() {
        this.connection.flush();
    }

    @Override
    public ResponseBody openResponseBody(Response response) {
        StreamAllocation streamAllocation = this.streamAllocation;
        streamAllocation.eventListener.responseBodyStart(streamAllocation.call);
        return new RealResponseBody(response.header("Content-Type"), HttpHeaders.contentLength(response), Okio.buffer(new a(this.stream.getSource())));
    }

    @Override
    public Response.Builder readResponseHeaders(boolean z) throws ProtocolException {
        Response.Builder http2HeadersList = readHttp2HeadersList(this.stream.takeHeaders(), this.protocol);
        if (z && Internal.instance.code(http2HeadersList) == 100) {
            return null;
        }
        return http2HeadersList;
    }

    @Override
    public void writeRequestHeaders(Request request) {
        if (this.stream != null) {
            return;
        }
        Http2Stream http2StreamNewStream = this.connection.newStream(http2HeadersList(request), request.body() != null);
        this.stream = http2StreamNewStream;
        Timeout timeout = http2StreamNewStream.readTimeout();
        long timeoutMillis = this.chain.readTimeoutMillis();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        timeout.timeout(timeoutMillis, timeUnit);
        this.stream.writeTimeout().timeout(this.chain.writeTimeoutMillis(), timeUnit);
    }
}
