package org.cocos2dx.okhttp3.internal.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.cocos2dx.okhttp3.Address;
import org.cocos2dx.okhttp3.Call;
import org.cocos2dx.okhttp3.CertificatePinner;
import org.cocos2dx.okhttp3.Connection;
import org.cocos2dx.okhttp3.ConnectionPool;
import org.cocos2dx.okhttp3.ConnectionSpec;
import org.cocos2dx.okhttp3.EventListener;
import org.cocos2dx.okhttp3.Handshake;
import org.cocos2dx.okhttp3.HttpUrl;
import org.cocos2dx.okhttp3.Interceptor;
import org.cocos2dx.okhttp3.OkHttpClient;
import org.cocos2dx.okhttp3.Protocol;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.Route;
import org.cocos2dx.okhttp3.internal.Internal;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.Version;
import org.cocos2dx.okhttp3.internal.http.HttpCodec;
import org.cocos2dx.okhttp3.internal.http.HttpHeaders;
import org.cocos2dx.okhttp3.internal.http1.Http1Codec;
import org.cocos2dx.okhttp3.internal.http2.ErrorCode;
import org.cocos2dx.okhttp3.internal.http2.Http2Codec;
import org.cocos2dx.okhttp3.internal.http2.Http2Connection;
import org.cocos2dx.okhttp3.internal.http2.Http2Stream;
import org.cocos2dx.okhttp3.internal.platform.Platform;
import org.cocos2dx.okhttp3.internal.tls.OkHostnameVerifier;
import org.cocos2dx.okhttp3.internal.ws.RealWebSocket;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Source;
import org.cocos2dx.okio.Timeout;

public final class RealConnection extends Http2Connection.Listener implements Connection {
    private static final int MAX_TUNNEL_ATTEMPTS = 21;
    private static final String NPE_THROW_WITH_NULL = "throw with null exception";
    private final ConnectionPool connectionPool;
    private Handshake handshake;
    private Http2Connection http2Connection;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    private BufferedSink sink;
    private Socket socket;
    private BufferedSource source;
    public int successCount;
    public int allocationLimit = 1;
    public final List allocations = new ArrayList();
    public long idleAtNanos = Long.MAX_VALUE;

    class a extends RealWebSocket.Streams {
        final StreamAllocation i;

        a(boolean z, BufferedSource bufferedSource, BufferedSink bufferedSink, StreamAllocation streamAllocation) {
            super(z, bufferedSource, bufferedSink);
            this.i = streamAllocation;
        }

        @Override
        public void close() {
            StreamAllocation streamAllocation = this.i;
            streamAllocation.streamFinished(true, streamAllocation.codec(), -1L, null);
        }
    }

    public RealConnection(ConnectionPool connectionPool, Route route) {
        this.connectionPool = connectionPool;
        this.route = route;
    }

    private void connectSocket(int i, int i2, Call call, EventListener eventListener) throws IOException {
        Proxy proxy = this.route.proxy();
        this.rawSocket = (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP) ? this.route.address().socketFactory().createSocket() : new Socket(proxy);
        eventListener.connectStart(call, this.route.socketAddress(), proxy);
        this.rawSocket.setSoTimeout(i2);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), i);
            try {
                this.source = Okio.buffer(Okio.source(this.rawSocket));
                this.sink = Okio.buffer(Okio.sink(this.rawSocket));
            } catch (NullPointerException e) {
                if (NPE_THROW_WITH_NULL.equals(e.getMessage())) {
                    throw new IOException(e);
                }
            }
        } catch (ConnectException e2) {
            ConnectException connectException = new ConnectException("Failed to connect to " + this.route.socketAddress());
            connectException.initCause(e2);
            throw connectException;
        }
    }

    private void connectTls(ConnectionSpecSelector connectionSpecSelector) throws Throwable {
        SSLSocket sSLSocket;
        Address address = this.route.address();
        SSLSocket sSLSocket2 = null;
        try {
            try {
                sSLSocket = (SSLSocket) address.sslSocketFactory().createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
            } catch (AssertionError e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            ConnectionSpec connectionSpecConfigureSecureSocket = connectionSpecSelector.configureSecureSocket(sSLSocket);
            if (connectionSpecConfigureSecureSocket.supportsTlsExtensions()) {
                Platform.get().configureTlsExtensions(sSLSocket, address.url().host(), address.protocols());
            }
            sSLSocket.startHandshake();
            SSLSession session = sSLSocket.getSession();
            Handshake handshake = Handshake.get(session);
            if (address.hostnameVerifier().verify(address.url().host(), session)) {
                address.certificatePinner().check(address.url().host(), handshake.peerCertificates());
                String selectedProtocol = connectionSpecConfigureSecureSocket.supportsTlsExtensions() ? Platform.get().getSelectedProtocol(sSLSocket) : null;
                this.socket = sSLSocket;
                this.source = Okio.buffer(Okio.source(sSLSocket));
                this.sink = Okio.buffer(Okio.sink(this.socket));
                this.handshake = handshake;
                this.protocol = selectedProtocol != null ? Protocol.get(selectedProtocol) : Protocol.HTTP_1_1;
                Platform.get().afterHandshake(sSLSocket);
                return;
            }
            List listPeerCertificates = handshake.peerCertificates();
            if (listPeerCertificates.isEmpty()) {
                throw new SSLPeerUnverifiedException("Hostname " + address.url().host() + " not verified (no certificates)");
            }
            X509Certificate x509Certificate = (X509Certificate) listPeerCertificates.get(0);
            throw new SSLPeerUnverifiedException("Hostname " + address.url().host() + " not verified:\n    certificate: " + CertificatePinner.pin(x509Certificate) + "\n    DN: " + x509Certificate.getSubjectDN().getName() + "\n    subjectAltNames: " + OkHostnameVerifier.allSubjectAltNames(x509Certificate));
        } catch (AssertionError e2) {
            e = e2;
            if (!Util.isAndroidGetsocknameError(e)) {
                throw e;
            }
            throw new IOException(e);
        } catch (Throwable th2) {
            th = th2;
            sSLSocket2 = sSLSocket;
            if (sSLSocket2 != null) {
                Platform.get().afterHandshake(sSLSocket2);
            }
            Util.closeQuietly((Socket) sSLSocket2);
            throw th;
        }
    }

    private void connectTunnel(int i, int i2, int i3, Call call, EventListener eventListener) throws IOException {
        Request requestCreateTunnelRequest = createTunnelRequest();
        HttpUrl httpUrlUrl = requestCreateTunnelRequest.url();
        for (int i4 = 0; i4 < MAX_TUNNEL_ATTEMPTS; i4++) {
            connectSocket(i, i2, call, eventListener);
            requestCreateTunnelRequest = createTunnel(i2, i3, requestCreateTunnelRequest, httpUrlUrl);
            if (requestCreateTunnelRequest == null) {
                return;
            }
            Util.closeQuietly(this.rawSocket);
            this.rawSocket = null;
            this.sink = null;
            this.source = null;
            eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), null);
        }
    }

    private Request createTunnel(int i, int i2, Request request, HttpUrl httpUrl) throws IOException {
        String str = "CONNECT " + Util.hostHeader(httpUrl, true) + " HTTP/1.1";
        while (true) {
            Http1Codec http1Codec = new Http1Codec(null, null, this.source, this.sink);
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            this.source.timeout().timeout(i, timeUnit);
            this.sink.timeout().timeout(i2, timeUnit);
            http1Codec.writeRequest(request.headers(), str);
            http1Codec.finishRequest();
            Response responseBuild = http1Codec.readResponseHeaders(false).request(request).build();
            long jContentLength = HttpHeaders.contentLength(responseBuild);
            if (jContentLength == -1) {
                jContentLength = 0;
            }
            Source sourceNewFixedLengthSource = http1Codec.newFixedLengthSource(jContentLength);
            Util.skipAll(sourceNewFixedLengthSource, Integer.MAX_VALUE, timeUnit);
            sourceNewFixedLengthSource.close();
            int iCode = responseBuild.code();
            if (iCode == 200) {
                if (this.source.buffer().exhausted() && this.sink.buffer().exhausted()) {
                    return null;
                }
                throw new IOException("TLS tunnel buffered too many bytes!");
            }
            if (iCode != 407) {
                throw new IOException("Unexpected response code for CONNECT: " + responseBuild.code());
            }
            Request requestAuthenticate = this.route.address().proxyAuthenticator().authenticate(this.route, responseBuild);
            if (requestAuthenticate == null) {
                throw new IOException("Failed to authenticate with proxy");
            }
            if ("close".equalsIgnoreCase(responseBuild.header("Connection"))) {
                return requestAuthenticate;
            }
            request = requestAuthenticate;
        }
    }

    private Request createTunnelRequest() {
        Request requestBuild = new Request.Builder().url(this.route.address().url()).method("CONNECT", null).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
        Request requestAuthenticate = this.route.address().proxyAuthenticator().authenticate(this.route, new Response.Builder().request(requestBuild).protocol(Protocol.HTTP_1_1).code(407).message("Preemptive Authenticate").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(-1L).header("Proxy-Authenticate", "OkHttp-Preemptive").build());
        return requestAuthenticate != null ? requestAuthenticate : requestBuild;
    }

    private void establishProtocol(ConnectionSpecSelector connectionSpecSelector, int i, Call call, EventListener eventListener) throws Throwable {
        if (this.route.address().sslSocketFactory() != null) {
            eventListener.secureConnectStart(call);
            connectTls(connectionSpecSelector);
            eventListener.secureConnectEnd(call, this.handshake);
            if (this.protocol == Protocol.HTTP_2) {
                startHttp2(i);
                return;
            }
            return;
        }
        List listProtocols = this.route.address().protocols();
        Protocol protocol = Protocol.H2_PRIOR_KNOWLEDGE;
        if (!listProtocols.contains(protocol)) {
            this.socket = this.rawSocket;
            this.protocol = Protocol.HTTP_1_1;
        } else {
            this.socket = this.rawSocket;
            this.protocol = protocol;
            startHttp2(i);
        }
    }

    private void startHttp2(int i) throws SocketException {
        this.socket.setSoTimeout(0);
        Http2Connection http2ConnectionBuild = new Http2Connection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).pingIntervalMillis(i).build();
        this.http2Connection = http2ConnectionBuild;
        http2ConnectionBuild.start();
    }

    public static RealConnection testConnection(ConnectionPool connectionPool, Route route, Socket socket, long j) {
        RealConnection realConnection = new RealConnection(connectionPool, route);
        realConnection.socket = socket;
        realConnection.idleAtNanos = j;
        return realConnection;
    }

    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }

    public void connect(int i, int i2, int i3, int i4, boolean z, Call call, EventListener eventListener) throws Throwable {
        if (this.protocol != null) {
            throw new IllegalStateException("already connected");
        }
        List listConnectionSpecs = this.route.address().connectionSpecs();
        ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(listConnectionSpecs);
        if (this.route.address().sslSocketFactory() == null) {
            if (!listConnectionSpecs.contains(ConnectionSpec.CLEARTEXT)) {
                throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
            }
            String strHost = this.route.address().url().host();
            if (!Platform.get().isCleartextTrafficPermitted(strHost)) {
                throw new RouteException(new UnknownServiceException("CLEARTEXT communication to " + strHost + " not permitted by network security policy"));
            }
        } else if (this.route.address().protocols().contains(Protocol.H2_PRIOR_KNOWLEDGE)) {
            throw new RouteException(new UnknownServiceException("H2_PRIOR_KNOWLEDGE cannot be used with HTTPS"));
        }
        RouteException routeException = null;
        while (true) {
            try {
                if (this.route.requiresTunnel()) {
                    connectTunnel(i, i2, i3, call, eventListener);
                    if (this.rawSocket == null) {
                        break;
                    }
                } else {
                    try {
                        connectSocket(i, i2, call, eventListener);
                    } catch (IOException e) {
                        e = e;
                        Util.closeQuietly(this.socket);
                        Util.closeQuietly(this.rawSocket);
                        this.socket = null;
                        this.rawSocket = null;
                        this.source = null;
                        this.sink = null;
                        this.handshake = null;
                        this.protocol = null;
                        this.http2Connection = null;
                        eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, e);
                        if (routeException == null) {
                            routeException = new RouteException(e);
                        } else {
                            routeException.addConnectException(e);
                        }
                        if (z) {
                            throw routeException;
                        }
                        if (connectionSpecSelector.connectionFailed(e)) {
                            throw routeException;
                        }
                    }
                }
                try {
                    establishProtocol(connectionSpecSelector, i4, call, eventListener);
                    eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
                    break;
                } catch (IOException e2) {
                    e = e2;
                    Util.closeQuietly(this.socket);
                    Util.closeQuietly(this.rawSocket);
                    this.socket = null;
                    this.rawSocket = null;
                    this.source = null;
                    this.sink = null;
                    this.handshake = null;
                    this.protocol = null;
                    this.http2Connection = null;
                    eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, e);
                    if (routeException == null) {
                        routeException = new RouteException(e);
                    } else {
                        routeException.addConnectException(e);
                    }
                    if (z) {
                        throw routeException;
                    }
                    if (connectionSpecSelector.connectionFailed(e)) {
                        throw routeException;
                    }
                }
            } catch (IOException e3) {
                e = e3;
            }
        }
        if (this.route.requiresTunnel() && this.rawSocket == null) {
            throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
        }
        if (this.http2Connection != null) {
            synchronized (this.connectionPool) {
                this.allocationLimit = this.http2Connection.maxConcurrentStreams();
            }
        }
    }

    @Override
    public Handshake handshake() {
        return this.handshake;
    }

    public boolean isEligible(Address address, @Nullable Route route) {
        if (this.allocations.size() >= this.allocationLimit || this.noNewStreams || !Internal.instance.equalsNonHost(this.route.address(), address)) {
            return false;
        }
        if (address.url().host().equals(route().address().url().host())) {
            return true;
        }
        if (this.http2Connection == null || route == null) {
            return false;
        }
        Proxy.Type type = route.proxy().type();
        Proxy.Type type2 = Proxy.Type.DIRECT;
        if (type != type2 || this.route.proxy().type() != type2 || !this.route.socketAddress().equals(route.socketAddress()) || route.address().hostnameVerifier() != OkHostnameVerifier.INSTANCE || !supportsUrl(address.url())) {
            return false;
        }
        try {
            address.certificatePinner().check(address.url().host(), handshake().peerCertificates());
            return true;
        } catch (SSLPeerUnverifiedException unused) {
            return false;
        }
    }

    public boolean isHealthy(boolean z) {
        if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            return false;
        }
        if (this.http2Connection != null) {
            return !r0.isShutdown();
        }
        if (z) {
            try {
                int soTimeout = this.socket.getSoTimeout();
                try {
                    this.socket.setSoTimeout(1);
                    if (this.source.exhausted()) {
                        this.socket.setSoTimeout(soTimeout);
                        return false;
                    }
                    this.socket.setSoTimeout(soTimeout);
                    return true;
                } catch (Throwable th) {
                    this.socket.setSoTimeout(soTimeout);
                    throw th;
                }
            } catch (SocketTimeoutException unused) {
            } catch (IOException unused2) {
                return false;
            }
        }
        return true;
    }

    public boolean isMultiplexed() {
        return this.http2Connection != null;
    }

    public HttpCodec newCodec(OkHttpClient okHttpClient, Interceptor.Chain chain, StreamAllocation streamAllocation) throws SocketException {
        if (this.http2Connection != null) {
            return new Http2Codec(okHttpClient, chain, streamAllocation, this.http2Connection);
        }
        this.socket.setSoTimeout(chain.readTimeoutMillis());
        Timeout timeout = this.source.timeout();
        long timeoutMillis = chain.readTimeoutMillis();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        timeout.timeout(timeoutMillis, timeUnit);
        this.sink.timeout().timeout(chain.writeTimeoutMillis(), timeUnit);
        return new Http1Codec(okHttpClient, streamAllocation, this.source, this.sink);
    }

    public RealWebSocket.Streams newWebSocketStreams(StreamAllocation streamAllocation) {
        return new a(true, this.source, this.sink, streamAllocation);
    }

    @Override
    public void onSettings(Http2Connection http2Connection) {
        synchronized (this.connectionPool) {
            this.allocationLimit = http2Connection.maxConcurrentStreams();
        }
    }

    @Override
    public void onStream(Http2Stream http2Stream) {
        http2Stream.close(ErrorCode.REFUSED_STREAM);
    }

    @Override
    public Protocol protocol() {
        return this.protocol;
    }

    @Override
    public Route route() {
        return this.route;
    }

    @Override
    public Socket socket() {
        return this.socket;
    }

    public boolean supportsUrl(HttpUrl httpUrl) {
        if (httpUrl.port() != this.route.address().url().port()) {
            return false;
        }
        if (httpUrl.host().equals(this.route.address().url().host())) {
            return true;
        }
        return this.handshake != null && OkHostnameVerifier.INSTANCE.verify(httpUrl.host(), (X509Certificate) this.handshake.peerCertificates().get(0));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.route.address().url().host());
        sb.append(":");
        sb.append(this.route.address().url().port());
        sb.append(", proxy=");
        sb.append(this.route.proxy());
        sb.append(" hostAddress=");
        sb.append(this.route.socketAddress());
        sb.append(" cipherSuite=");
        Handshake handshake = this.handshake;
        sb.append(handshake != null ? handshake.cipherSuite() : "none");
        sb.append(" protocol=");
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }
}
