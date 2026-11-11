package org.cocos2dx.okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;

public abstract class ResponseBody implements Closeable {

    @Nullable
    private Reader reader;

    class a extends ResponseBody {
        final MediaType i;
        final long j;
        final BufferedSource k;

        a(MediaType mediaType, long j, BufferedSource bufferedSource) {
            this.i = mediaType;
            this.j = j;
            this.k = bufferedSource;
        }

        @Override
        public long contentLength() {
            return this.j;
        }

        @Override
        public MediaType contentType() {
            return this.i;
        }

        @Override
        public BufferedSource source() {
            return this.k;
        }
    }

    static final class b extends Reader {
        private final BufferedSource i;
        private final Charset j;
        private boolean k;
        private Reader l;

        b(BufferedSource bufferedSource, Charset charset) {
            this.i = bufferedSource;
            this.j = charset;
        }

        @Override
        public void close() throws IOException {
            this.k = true;
            Reader reader = this.l;
            if (reader != null) {
                reader.close();
            } else {
                this.i.close();
            }
        }

        @Override
        public int read(char[] cArr, int i, int i2) throws IOException {
            if (this.k) {
                throw new IOException("Stream closed");
            }
            Reader reader = this.l;
            if (reader == null) {
                InputStreamReader inputStreamReader = new InputStreamReader(this.i.inputStream(), Util.bomAwareCharset(this.i, this.j));
                this.l = inputStreamReader;
                reader = inputStreamReader;
            }
            return reader.read(cArr, i, i2);
        }
    }

    private Charset charset() {
        MediaType mediaTypeContentType = contentType();
        return mediaTypeContentType != null ? mediaTypeContentType.charset(Util.UTF_8) : Util.UTF_8;
    }

    public static ResponseBody create(@Nullable MediaType mediaType, String str) {
        Charset charset = Util.UTF_8;
        if (mediaType != null) {
            Charset charset2 = mediaType.charset();
            if (charset2 == null) {
                mediaType = MediaType.parse(mediaType + "; charset=utf-8");
            } else {
                charset = charset2;
            }
        }
        Buffer bufferWriteString = new Buffer().writeString(str, charset);
        return create(mediaType, bufferWriteString.size(), bufferWriteString);
    }

    public final InputStream byteStream() {
        return source().inputStream();
    }

    public final byte[] bytes() throws IOException {
        long jContentLength = contentLength();
        if (jContentLength > 2147483647L) {
            throw new IOException("Cannot buffer entire body for content length: " + jContentLength);
        }
        BufferedSource bufferedSourceSource = source();
        try {
            byte[] byteArray = bufferedSourceSource.readByteArray();
            Util.closeQuietly(bufferedSourceSource);
            if (jContentLength == -1 || jContentLength == byteArray.length) {
                return byteArray;
            }
            throw new IOException("Content-Length (" + jContentLength + ") and stream length (" + byteArray.length + ") disagree");
        } catch (Throwable th) {
            Util.closeQuietly(bufferedSourceSource);
            throw th;
        }
    }

    public final Reader charStream() {
        Reader reader = this.reader;
        if (reader != null) {
            return reader;
        }
        b bVar = new b(source(), charset());
        this.reader = bVar;
        return bVar;
    }

    @Override
    public void close() {
        Util.closeQuietly(source());
    }

    public abstract long contentLength();

    @Nullable
    public abstract MediaType contentType();

    public abstract BufferedSource source();

    public final String string() {
        BufferedSource bufferedSourceSource = source();
        try {
            return bufferedSourceSource.readString(Util.bomAwareCharset(bufferedSourceSource, charset()));
        } finally {
            Util.closeQuietly(bufferedSourceSource);
        }
    }

    public static ResponseBody create(@Nullable MediaType mediaType, byte[] bArr) {
        return create(mediaType, bArr.length, new Buffer().write(bArr));
    }

    public static ResponseBody create(@Nullable MediaType mediaType, ByteString byteString) {
        return create(mediaType, byteString.size(), new Buffer().write(byteString));
    }

    public static ResponseBody create(@Nullable MediaType mediaType, long j, BufferedSource bufferedSource) {
        if (bufferedSource != null) {
            return new a(mediaType, j, bufferedSource);
        }
        throw new NullPointerException("source == null");
    }
}
