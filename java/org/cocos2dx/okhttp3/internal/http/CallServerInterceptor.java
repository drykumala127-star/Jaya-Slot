package org.cocos2dx.okhttp3.internal.http;

import java.net.ProtocolException;
import org.cocos2dx.okhttp3.Interceptor;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.connection.RealConnection;
import org.cocos2dx.okhttp3.internal.connection.StreamAllocation;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.ForwardingSink;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;

public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    static final class a extends ForwardingSink {
        long i;

        a(Sink sink) {
            super(sink);
        }

        @Override
        public void write(Buffer buffer, long j) {
            super.write(buffer, j);
            this.i += j;
        }
    }

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws ProtocolException {
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        HttpCodec httpCodecHttpStream = realInterceptorChain.httpStream();
        StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        RealConnection realConnection = (RealConnection) realInterceptorChain.connection();
        Request request = realInterceptorChain.request();
        long jCurrentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpCodecHttpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        Response.Builder responseHeaders = null;
        if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
            if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                httpCodecHttpStream.flushRequest();
                realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                responseHeaders = httpCodecHttpStream.readResponseHeaders(true);
            }
            if (responseHeaders == null) {
                realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                a aVar = new a(httpCodecHttpStream.createRequestBody(request, request.body().contentLength()));
                BufferedSink bufferedSinkBuffer = Okio.buffer(aVar);
                request.body().writeTo(bufferedSinkBuffer);
                bufferedSinkBuffer.close();
                realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), aVar.i);
            } else if (!realConnection.isMultiplexed()) {
                streamAllocation.noNewStreams();
            }
        }
        httpCodecHttpStream.finishRequest();
        if (responseHeaders == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            responseHeaders = httpCodecHttpStream.readResponseHeaders(false);
        }
        Response responseBuild = responseHeaders.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int iCode = responseBuild.code();
        if (iCode == 100) {
            responseBuild = httpCodecHttpStream.readResponseHeaders(false).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            iCode = responseBuild.code();
        }
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), responseBuild);
        Response responseBuild2 = (this.forWebSocket && iCode == 101) ? responseBuild.newBuilder().body(Util.EMPTY_RESPONSE).build() : responseBuild.newBuilder().body(httpCodecHttpStream.openResponseBody(responseBuild)).build();
        if ("close".equalsIgnoreCase(responseBuild2.request().header("Connection")) || "close".equalsIgnoreCase(responseBuild2.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((iCode != 204 && iCode != 205) || responseBuild2.body().contentLength() <= 0) {
            return responseBuild2;
        }
        throw new ProtocolException("HTTP " + iCode + " had non-zero Content-Length: " + responseBuild2.body().contentLength());
    }
}
