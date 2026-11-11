package org.cocos2dx.okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.Headers;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.cache.CacheRequest;
import org.cocos2dx.okhttp3.internal.cache.CacheStrategy;
import org.cocos2dx.okhttp3.internal.cache.DiskLruCache;
import org.cocos2dx.okhttp3.internal.cache.InternalCache;
import org.cocos2dx.okhttp3.internal.http.HttpHeaders;
import org.cocos2dx.okhttp3.internal.http.HttpMethod;
import org.cocos2dx.okhttp3.internal.http.StatusLine;
import org.cocos2dx.okhttp3.internal.io.FileSystem;
import org.cocos2dx.okhttp3.internal.platform.Platform;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.ForwardingSink;
import org.cocos2dx.okio.ForwardingSource;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;

public final class Cache implements Closeable, Flushable {
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    final DiskLruCache cache;
    private int hitCount;
    final InternalCache internalCache;
    private int networkCount;
    private int requestCount;
    int writeAbortCount;
    int writeSuccessCount;

    class a implements InternalCache {
        a() {
        }

        @Override
        public Response get(Request request) {
            return Cache.this.get(request);
        }

        @Override
        public CacheRequest put(Response response) {
            return Cache.this.put(response);
        }

        @Override
        public void remove(Request request) {
            Cache.this.remove(request);
        }

        @Override
        public void trackConditionalCacheHit() {
            Cache.this.trackConditionalCacheHit();
        }

        @Override
        public void trackResponse(CacheStrategy cacheStrategy) {
            Cache.this.trackResponse(cacheStrategy);
        }

        @Override
        public void update(Response response, Response response2) {
            Cache.this.update(response, response2);
        }
    }

    class b implements Iterator {
        final Iterator a;
        String b;
        boolean c;

        b() {
            this.a = Cache.this.cache.snapshots();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String str = this.b;
            this.b = null;
            this.c = true;
            return str;
        }

        @Override
        public boolean hasNext() {
            if (this.b != null) {
                return true;
            }
            this.c = false;
            while (this.a.hasNext()) {
                DiskLruCache.Snapshot snapshot = (DiskLruCache.Snapshot) this.a.next();
                try {
                    this.b = Okio.buffer(snapshot.getSource(Cache.ENTRY_METADATA)).readUtf8LineStrict();
                    snapshot.close();
                    return true;
                } catch (IOException unused) {
                    snapshot.close();
                } catch (Throwable th) {
                    snapshot.close();
                    throw th;
                }
            }
            return false;
        }

        @Override
        public void remove() {
            if (!this.c) {
                throw new IllegalStateException("remove() before next()");
            }
            this.a.remove();
        }
    }

    private final class c implements CacheRequest {
        private final DiskLruCache.Editor a;
        private Sink b;
        private Sink c;
        boolean d;

        class a extends ForwardingSink {
            final Cache i;
            final DiskLruCache.Editor j;

            a(Sink sink, Cache cache, DiskLruCache.Editor editor) {
                super(sink);
                this.i = cache;
                this.j = editor;
            }

            @Override
            public void close() {
                synchronized (Cache.this) {
                    try {
                        c cVar = c.this;
                        if (cVar.d) {
                            return;
                        }
                        cVar.d = true;
                        Cache.this.writeSuccessCount += Cache.ENTRY_BODY;
                        super.close();
                        this.j.commit();
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }

        c(DiskLruCache.Editor editor) {
            this.a = editor;
            Sink sinkNewSink = editor.newSink(Cache.ENTRY_BODY);
            this.b = sinkNewSink;
            this.c = new a(sinkNewSink, Cache.this, editor);
        }

        @Override
        public void abort() {
            synchronized (Cache.this) {
                try {
                    if (this.d) {
                        return;
                    }
                    this.d = true;
                    Cache.this.writeAbortCount += Cache.ENTRY_BODY;
                    Util.closeQuietly(this.b);
                    try {
                        this.a.abort();
                    } catch (IOException unused) {
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public Sink body() {
            return this.c;
        }
    }

    private static class d extends ResponseBody {
        final DiskLruCache.Snapshot i;
        private final BufferedSource j;
        private final String k;
        private final String l;

        class a extends ForwardingSource {
            final DiskLruCache.Snapshot i;

            a(Source source, DiskLruCache.Snapshot snapshot) {
                super(source);
                this.i = snapshot;
            }

            @Override
            public void close() {
                this.i.close();
                super.close();
            }
        }

        d(DiskLruCache.Snapshot snapshot, String str, String str2) {
            this.i = snapshot;
            this.k = str;
            this.l = str2;
            this.j = Okio.buffer(new a(snapshot.getSource(Cache.ENTRY_BODY), snapshot));
        }

        @Override
        public long contentLength() {
            try {
                String str = this.l;
                if (str != null) {
                    return Long.parseLong(str);
                }
                return -1L;
            } catch (NumberFormatException unused) {
                return -1L;
            }
        }

        @Override
        public MediaType contentType() {
            String str = this.k;
            if (str != null) {
                return MediaType.parse(str);
            }
            return null;
        }

        @Override
        public BufferedSource source() {
            return this.j;
        }
    }

    public Cache(File file, long j) {
        this(file, j, FileSystem.SYSTEM);
    }

    private void abortQuietly(@Nullable DiskLruCache.Editor editor) {
        if (editor != null) {
            try {
                editor.abort();
            } catch (IOException unused) {
            }
        }
    }

    public static String key(HttpUrl httpUrl) {
        return ByteString.encodeUtf8(httpUrl.toString()).md5().hex();
    }

    static int readInt(BufferedSource bufferedSource) throws IOException {
        try {
            long decimalLong = bufferedSource.readDecimalLong();
            String utf8LineStrict = bufferedSource.readUtf8LineStrict();
            if (decimalLong >= 0 && decimalLong <= 2147483647L && utf8LineStrict.isEmpty()) {
                return (int) decimalLong;
            }
            throw new IOException("expected an int but was \"" + decimalLong + utf8LineStrict + "\"");
        } catch (NumberFormatException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    @Override
    public void close() {
        this.cache.close();
    }

    public void delete() {
        this.cache.delete();
    }

    public File directory() {
        return this.cache.getDirectory();
    }

    public void evictAll() {
        this.cache.evictAll();
    }

    @Override
    public void flush() {
        this.cache.flush();
    }

    @Nullable
    Response get(Request request) {
        try {
            DiskLruCache.Snapshot snapshot = this.cache.get(key(request.url()));
            if (snapshot == null) {
                return null;
            }
            try {
                e eVar = new e(snapshot.getSource(ENTRY_METADATA));
                Response responseD = eVar.d(snapshot);
                if (eVar.b(request, responseD)) {
                    return responseD;
                }
                Util.closeQuietly(responseD.body());
                return null;
            } catch (IOException unused) {
                Util.closeQuietly(snapshot);
                return null;
            }
        } catch (IOException unused2) {
        }
    }

    public synchronized int hitCount() {
        return this.hitCount;
    }

    public void initialize() {
        this.cache.initialize();
    }

    public boolean isClosed() {
        return this.cache.isClosed();
    }

    public long maxSize() {
        return this.cache.getMaxSize();
    }

    public synchronized int networkCount() {
        return this.networkCount;
    }

    @Nullable
    CacheRequest put(Response response) {
        DiskLruCache.Editor editorEdit;
        String strMethod = response.request().method();
        if (HttpMethod.invalidatesCache(response.request().method())) {
            try {
                remove(response.request());
            } catch (IOException unused) {
            }
            return null;
        }
        if (!strMethod.equals("GET") || HttpHeaders.hasVaryAll(response)) {
            return null;
        }
        e eVar = new e(response);
        try {
            editorEdit = this.cache.edit(key(response.request().url()));
            if (editorEdit == null) {
                return null;
            }
            try {
                eVar.f(editorEdit);
                return new c(editorEdit);
            } catch (IOException unused2) {
                abortQuietly(editorEdit);
                return null;
            }
        } catch (IOException unused3) {
            editorEdit = null;
        }
    }

    void remove(Request request) {
        this.cache.remove(key(request.url()));
    }

    public synchronized int requestCount() {
        return this.requestCount;
    }

    public long size() {
        return this.cache.size();
    }

    synchronized void trackConditionalCacheHit() {
        this.hitCount += ENTRY_BODY;
    }

    synchronized void trackResponse(CacheStrategy cacheStrategy) {
        try {
            this.requestCount += ENTRY_BODY;
            if (cacheStrategy.networkRequest != null) {
                this.networkCount += ENTRY_BODY;
            } else if (cacheStrategy.cacheResponse != null) {
                this.hitCount += ENTRY_BODY;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    void update(Response response, Response response2) {
        DiskLruCache.Editor editorEdit;
        e eVar = new e(response2);
        try {
            editorEdit = ((d) response.body()).i.edit();
            if (editorEdit != null) {
                try {
                    eVar.f(editorEdit);
                    editorEdit.commit();
                } catch (IOException unused) {
                    abortQuietly(editorEdit);
                }
            }
        } catch (IOException unused2) {
            editorEdit = null;
        }
    }

    public Iterator urls() {
        return new b();
    }

    public synchronized int writeAbortCount() {
        return this.writeAbortCount;
    }

    public synchronized int writeSuccessCount() {
        return this.writeSuccessCount;
    }

    Cache(File file, long j, FileSystem fileSystem) {
        this.internalCache = new a();
        this.cache = DiskLruCache.create(fileSystem, file, VERSION, ENTRY_COUNT, j);
    }

    private static final class e {
        private static final String k = Platform.get().getPrefix() + "-Sent-Millis";
        private static final String l = Platform.get().getPrefix() + "-Received-Millis";
        private final String a;
        private final Headers b;
        private final String c;
        private final Protocol d;
        private final int e;
        private final String f;
        private final Headers g;
        private final Handshake h;
        private final long i;
        private final long j;

        e(Source source) {
            try {
                BufferedSource bufferedSourceBuffer = Okio.buffer(source);
                this.a = bufferedSourceBuffer.readUtf8LineStrict();
                this.c = bufferedSourceBuffer.readUtf8LineStrict();
                Headers.Builder builder = new Headers.Builder();
                int i = Cache.readInt(bufferedSourceBuffer);
                for (int i2 = Cache.ENTRY_METADATA; i2 < i; i2 += Cache.ENTRY_BODY) {
                    builder.addLenient(bufferedSourceBuffer.readUtf8LineStrict());
                }
                this.b = builder.build();
                StatusLine statusLine = StatusLine.parse(bufferedSourceBuffer.readUtf8LineStrict());
                this.d = statusLine.protocol;
                this.e = statusLine.code;
                this.f = statusLine.message;
                Headers.Builder builder2 = new Headers.Builder();
                int i3 = Cache.readInt(bufferedSourceBuffer);
                for (int i4 = Cache.ENTRY_METADATA; i4 < i3; i4 += Cache.ENTRY_BODY) {
                    builder2.addLenient(bufferedSourceBuffer.readUtf8LineStrict());
                }
                String str = k;
                String str2 = builder2.get(str);
                String str3 = l;
                String str4 = builder2.get(str3);
                builder2.removeAll(str);
                builder2.removeAll(str3);
                this.i = str2 != null ? Long.parseLong(str2) : 0L;
                this.j = str4 != null ? Long.parseLong(str4) : 0L;
                this.g = builder2.build();
                if (a()) {
                    String utf8LineStrict = bufferedSourceBuffer.readUtf8LineStrict();
                    if (utf8LineStrict.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + utf8LineStrict + "\"");
                    }
                    this.h = Handshake.get(!bufferedSourceBuffer.exhausted() ? TlsVersion.forJavaName(bufferedSourceBuffer.readUtf8LineStrict()) : TlsVersion.SSL_3_0, CipherSuite.forJavaName(bufferedSourceBuffer.readUtf8LineStrict()), c(bufferedSourceBuffer), c(bufferedSourceBuffer));
                } else {
                    this.h = null;
                }
                source.close();
            } catch (Throwable th) {
                source.close();
                throw th;
            }
        }

        private boolean a() {
            return this.a.startsWith("https://");
        }

        private List c(BufferedSource bufferedSource) throws IOException {
            int i = Cache.readInt(bufferedSource);
            if (i == -1) {
                return Collections.emptyList();
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                ArrayList arrayList = new ArrayList(i);
                for (int i2 = Cache.ENTRY_METADATA; i2 < i; i2 += Cache.ENTRY_BODY) {
                    String utf8LineStrict = bufferedSource.readUtf8LineStrict();
                    Buffer buffer = new Buffer();
                    buffer.write(ByteString.decodeBase64(utf8LineStrict));
                    arrayList.add(certificateFactory.generateCertificate(buffer.inputStream()));
                }
                return arrayList;
            } catch (CertificateException e) {
                throw new IOException(e.getMessage());
            }
        }

        private void e(BufferedSink bufferedSink, List list) throws IOException {
            try {
                bufferedSink.writeDecimalLong(list.size()).writeByte(10);
                int size = list.size();
                for (int i = Cache.ENTRY_METADATA; i < size; i += Cache.ENTRY_BODY) {
                    bufferedSink.writeUtf8(ByteString.of(((Certificate) list.get(i)).getEncoded()).base64()).writeByte(10);
                }
            } catch (CertificateEncodingException e) {
                throw new IOException(e.getMessage());
            }
        }

        public boolean b(Request request, Response response) {
            return this.a.equals(request.url().toString()) && this.c.equals(request.method()) && HttpHeaders.varyMatches(response, this.b, request);
        }

        public Response d(DiskLruCache.Snapshot snapshot) {
            String str = this.g.get("Content-Type");
            String str2 = this.g.get("Content-Length");
            return new Response.Builder().request(new Request.Builder().url(this.a).method(this.c, null).headers(this.b).build()).protocol(this.d).code(this.e).message(this.f).headers(this.g).body(new d(snapshot, str, str2)).handshake(this.h).sentRequestAtMillis(this.i).receivedResponseAtMillis(this.j).build();
        }

        public void f(DiskLruCache.Editor editor) throws IOException {
            BufferedSink bufferedSinkBuffer = Okio.buffer(editor.newSink(Cache.ENTRY_METADATA));
            bufferedSinkBuffer.writeUtf8(this.a).writeByte(10);
            bufferedSinkBuffer.writeUtf8(this.c).writeByte(10);
            bufferedSinkBuffer.writeDecimalLong(this.b.size()).writeByte(10);
            int size = this.b.size();
            for (int i = Cache.ENTRY_METADATA; i < size; i += Cache.ENTRY_BODY) {
                bufferedSinkBuffer.writeUtf8(this.b.name(i)).writeUtf8(": ").writeUtf8(this.b.value(i)).writeByte(10);
            }
            bufferedSinkBuffer.writeUtf8(new StatusLine(this.d, this.e, this.f).toString()).writeByte(10);
            bufferedSinkBuffer.writeDecimalLong(this.g.size() + Cache.ENTRY_COUNT).writeByte(10);
            int size2 = this.g.size();
            for (int i2 = Cache.ENTRY_METADATA; i2 < size2; i2 += Cache.ENTRY_BODY) {
                bufferedSinkBuffer.writeUtf8(this.g.name(i2)).writeUtf8(": ").writeUtf8(this.g.value(i2)).writeByte(10);
            }
            bufferedSinkBuffer.writeUtf8(k).writeUtf8(": ").writeDecimalLong(this.i).writeByte(10);
            bufferedSinkBuffer.writeUtf8(l).writeUtf8(": ").writeDecimalLong(this.j).writeByte(10);
            if (a()) {
                bufferedSinkBuffer.writeByte(10);
                bufferedSinkBuffer.writeUtf8(this.h.cipherSuite().javaName()).writeByte(10);
                e(bufferedSinkBuffer, this.h.peerCertificates());
                e(bufferedSinkBuffer, this.h.localCertificates());
                bufferedSinkBuffer.writeUtf8(this.h.tlsVersion().javaName()).writeByte(10);
            }
            bufferedSinkBuffer.close();
        }

        e(Response response) {
            this.a = response.request().url().toString();
            this.b = HttpHeaders.varyHeaders(response);
            this.c = response.request().method();
            this.d = response.protocol();
            this.e = response.code();
            this.f = response.message();
            this.g = response.headers();
            this.h = response.handshake();
            this.i = response.sentRequestAtMillis();
            this.j = response.receivedResponseAtMillis();
        }
    }
}
