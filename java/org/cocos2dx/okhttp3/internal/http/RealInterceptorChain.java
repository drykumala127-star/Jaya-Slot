package org.cocos2dx.okhttp3.internal.http;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.cocos2dx.okhttp3.Call;
import org.cocos2dx.okhttp3.Connection;
import org.cocos2dx.okhttp3.EventListener;
import org.cocos2dx.okhttp3.Interceptor;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.connection.RealConnection;
import org.cocos2dx.okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Interceptor.Chain {
    private final Call call;
    private int calls;
    private final int connectTimeout;
    private final RealConnection connection;
    private final EventListener eventListener;
    private final HttpCodec httpCodec;
    private final int index;
    private final List interceptors;
    private final int readTimeout;
    private final Request request;
    private final StreamAllocation streamAllocation;
    private final int writeTimeout;

    public RealInterceptorChain(List list, StreamAllocation streamAllocation, HttpCodec httpCodec, RealConnection realConnection, int i, Request request, Call call, EventListener eventListener, int i2, int i3, int i4) {
        this.interceptors = list;
        this.connection = realConnection;
        this.streamAllocation = streamAllocation;
        this.httpCodec = httpCodec;
        this.index = i;
        this.request = request;
        this.call = call;
        this.eventListener = eventListener;
        this.connectTimeout = i2;
        this.readTimeout = i3;
        this.writeTimeout = i4;
    }

    @Override
    public Call call() {
        return this.call;
    }

    @Override
    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }

    @Override
    public Connection connection() {
        return this.connection;
    }

    public EventListener eventListener() {
        return this.eventListener;
    }

    public HttpCodec httpStream() {
        return this.httpCodec;
    }

    @Override
    public Response proceed(Request request) {
        return proceed(request, this.streamAllocation, this.httpCodec, this.connection);
    }

    @Override
    public int readTimeoutMillis() {
        return this.readTimeout;
    }

    @Override
    public Request request() {
        return this.request;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }

    @Override
    public Interceptor.Chain withConnectTimeout(int i, TimeUnit timeUnit) {
        return new RealInterceptorChain(this.interceptors, this.streamAllocation, this.httpCodec, this.connection, this.index, this.request, this.call, this.eventListener, Util.checkDuration("timeout", i, timeUnit), this.readTimeout, this.writeTimeout);
    }

    @Override
    public Interceptor.Chain withReadTimeout(int i, TimeUnit timeUnit) {
        return new RealInterceptorChain(this.interceptors, this.streamAllocation, this.httpCodec, this.connection, this.index, this.request, this.call, this.eventListener, this.connectTimeout, Util.checkDuration("timeout", i, timeUnit), this.writeTimeout);
    }

    @Override
    public Interceptor.Chain withWriteTimeout(int i, TimeUnit timeUnit) {
        return new RealInterceptorChain(this.interceptors, this.streamAllocation, this.httpCodec, this.connection, this.index, this.request, this.call, this.eventListener, this.connectTimeout, this.readTimeout, Util.checkDuration("timeout", i, timeUnit));
    }

    @Override
    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }

    public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec, RealConnection realConnection) {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        this.calls++;
        if (this.httpCodec != null && !this.connection.supportsUrl(request.url())) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port");
        }
        if (this.httpCodec != null && this.calls > 1) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once");
        }
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, streamAllocation, httpCodec, realConnection, this.index + 1, request, this.call, this.eventListener, this.connectTimeout, this.readTimeout, this.writeTimeout);
        Interceptor interceptor = (Interceptor) this.interceptors.get(this.index);
        Response responseIntercept = interceptor.intercept(realInterceptorChain);
        if (httpCodec != null && this.index + 1 < this.interceptors.size() && realInterceptorChain.calls != 1) {
            throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
        }
        if (responseIntercept == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }
        if (responseIntercept.body() != null) {
            return responseIntercept;
        }
        throw new IllegalStateException("interceptor " + interceptor + " returned a response with no body");
    }
}
