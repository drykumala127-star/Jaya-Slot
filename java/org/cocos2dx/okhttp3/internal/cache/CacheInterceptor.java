package org.cocos2dx.okhttp3.internal.cache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.Headers;
import org.cocos2dx.okhttp3.Interceptor;
import org.cocos2dx.okhttp3.Protocol;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.internal.Internal;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.cache.CacheStrategy;
import org.cocos2dx.okhttp3.internal.http.HttpHeaders;
import org.cocos2dx.okhttp3.internal.http.HttpMethod;
import org.cocos2dx.okhttp3.internal.http.RealResponseBody;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;
import org.cocos2dx.okio.Timeout;

public final class CacheInterceptor implements Interceptor {
    final InternalCache cache;

    class a implements Source {
        boolean i;
        final BufferedSource j;
        final CacheRequest k;
        final BufferedSink l;

        a(BufferedSource bufferedSource, CacheRequest cacheRequest, BufferedSink bufferedSink) {
            this.j = bufferedSource;
            this.k = cacheRequest;
            this.l = bufferedSink;
        }

        @Override
        public void close() {
            if (!this.i && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                this.i = true;
                this.k.abort();
            }
            this.j.close();
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            try {
                long j2 = this.j.read(buffer, j);
                if (j2 != -1) {
                    buffer.copyTo(this.l.buffer(), buffer.size() - j2, j2);
                    this.l.emitCompleteSegments();
                    return j2;
                }
                if (!this.i) {
                    this.i = true;
                    this.l.close();
                }
                return -1L;
            } catch (IOException e) {
                if (!this.i) {
                    this.i = true;
                    this.k.abort();
                }
                throw e;
            }
        }

        @Override
        public Timeout timeout() {
            return this.j.timeout();
        }
    }

    public CacheInterceptor(InternalCache internalCache) {
        this.cache = internalCache;
    }

    private Response cacheWritingResponse(CacheRequest cacheRequest, Response response) {
        Sink sinkBody;
        if (cacheRequest == null || (sinkBody = cacheRequest.body()) == null) {
            return response;
        }
        return response.newBuilder().body(new RealResponseBody(response.header("Content-Type"), response.body().contentLength(), Okio.buffer(new a(response.body().source(), cacheRequest, Okio.buffer(sinkBody))))).build();
    }

    private static Headers combine(Headers headers, Headers headers2) {
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String strName = headers.name(i);
            String strValue = headers.value(i);
            if ((!"Warning".equalsIgnoreCase(strName) || !strValue.startsWith("1")) && (isContentSpecificHeader(strName) || !isEndToEnd(strName) || headers2.get(strName) == null)) {
                Internal.instance.addLenient(builder, strName, strValue);
            }
        }
        int size2 = headers2.size();
        for (int i2 = 0; i2 < size2; i2++) {
            String strName2 = headers2.name(i2);
            if (!isContentSpecificHeader(strName2) && isEndToEnd(strName2)) {
                Internal.instance.addLenient(builder, strName2, headers2.value(i2));
            }
        }
        return builder.build();
    }

    static boolean isContentSpecificHeader(String str) {
        return "Content-Length".equalsIgnoreCase(str) || "Content-Encoding".equalsIgnoreCase(str) || "Content-Type".equalsIgnoreCase(str);
    }

    static boolean isEndToEnd(String str) {
        return ("Connection".equalsIgnoreCase(str) || "Keep-Alive".equalsIgnoreCase(str) || "Proxy-Authenticate".equalsIgnoreCase(str) || "Proxy-Authorization".equalsIgnoreCase(str) || "TE".equalsIgnoreCase(str) || "Trailers".equalsIgnoreCase(str) || "Transfer-Encoding".equalsIgnoreCase(str) || "Upgrade".equalsIgnoreCase(str)) ? false : true;
    }

    private static Response stripBody(Response response) {
        return (response == null || response.body() == null) ? response : response.newBuilder().body(null).build();
    }

    @Override
    public Response intercept(Interceptor.Chain chain) {
        InternalCache internalCache = this.cache;
        Response response = internalCache != null ? internalCache.get(chain.request()) : null;
        CacheStrategy cacheStrategy = new CacheStrategy.Factory(System.currentTimeMillis(), chain.request(), response).get();
        Request request = cacheStrategy.networkRequest;
        Response response2 = cacheStrategy.cacheResponse;
        InternalCache internalCache2 = this.cache;
        if (internalCache2 != null) {
            internalCache2.trackResponse(cacheStrategy);
        }
        if (response != null && response2 == null) {
            Util.closeQuietly(response.body());
        }
        if (request == null && response2 == null) {
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        if (request == null) {
            return response2.newBuilder().cacheResponse(stripBody(response2)).build();
        }
        try {
            Response responseProceed = chain.proceed(request);
            if (responseProceed == null && response != null) {
                Util.closeQuietly(response.body());
            }
            if (response2 != null) {
                if (responseProceed.code() == 304) {
                    Response responseBuild = response2.newBuilder().headers(combine(response2.headers(), responseProceed.headers())).sentRequestAtMillis(responseProceed.sentRequestAtMillis()).receivedResponseAtMillis(responseProceed.receivedResponseAtMillis()).cacheResponse(stripBody(response2)).networkResponse(stripBody(responseProceed)).build();
                    responseProceed.body().close();
                    this.cache.trackConditionalCacheHit();
                    this.cache.update(response2, responseBuild);
                    return responseBuild;
                }
                Util.closeQuietly(response2.body());
            }
            Response responseBuild2 = responseProceed.newBuilder().cacheResponse(stripBody(response2)).networkResponse(stripBody(responseProceed)).build();
            if (this.cache != null) {
                if (HttpHeaders.hasBody(responseBuild2) && CacheStrategy.isCacheable(responseBuild2, request)) {
                    return cacheWritingResponse(this.cache.put(responseBuild2), responseBuild2);
                }
                if (HttpMethod.invalidatesCache(request.method())) {
                    try {
                        this.cache.remove(request);
                    } catch (IOException unused) {
                    }
                }
            }
            return responseBuild2;
        } catch (Throwable th) {
            if (response != null) {
                Util.closeQuietly(response.body());
            }
            throw th;
        }
    }
}
