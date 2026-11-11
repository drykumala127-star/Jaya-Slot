package org.cocos2dx.okhttp3;

import java.io.File;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Source;

public abstract class RequestBody {

    class a extends RequestBody {
        final MediaType a;
        final ByteString b;

        a(MediaType mediaType, ByteString byteString) {
            this.a = mediaType;
            this.b = byteString;
        }

        @Override
        public long contentLength() {
            return this.b.size();
        }

        @Override
        public MediaType contentType() {
            return this.a;
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) {
            bufferedSink.write(this.b);
        }
    }

    class b extends RequestBody {
        final MediaType a;
        final int b;
        final byte[] c;
        final int d;

        b(MediaType mediaType, int i, byte[] bArr, int i2) {
            this.a = mediaType;
            this.b = i;
            this.c = bArr;
            this.d = i2;
        }

        @Override
        public long contentLength() {
            return this.b;
        }

        @Override
        public MediaType contentType() {
            return this.a;
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) {
            bufferedSink.write(this.c, this.d, this.b);
        }
    }

    class c extends RequestBody {
        final MediaType a;
        final File b;

        c(MediaType mediaType, File file) {
            this.a = mediaType;
            this.b = file;
        }

        @Override
        public long contentLength() {
            return this.b.length();
        }

        @Override
        public MediaType contentType() {
            return this.a;
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) {
            Source source = null;
            try {
                source = Okio.source(this.b);
                bufferedSink.writeAll(source);
            } finally {
                Util.closeQuietly(source);
            }
        }
    }

    public static RequestBody create(@Nullable MediaType mediaType, String str) {
        Charset charset = Util.UTF_8;
        if (mediaType != null) {
            Charset charset2 = mediaType.charset();
            if (charset2 == null) {
                mediaType = MediaType.parse(mediaType + "; charset=utf-8");
            } else {
                charset = charset2;
            }
        }
        return create(mediaType, str.getBytes(charset));
    }

    public long contentLength() {
        return -1L;
    }

    @Nullable
    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink bufferedSink);

    public static RequestBody create(@Nullable MediaType mediaType, ByteString byteString) {
        return new a(mediaType, byteString);
    }

    public static RequestBody create(@Nullable MediaType mediaType, byte[] bArr) {
        return create(mediaType, bArr, 0, bArr.length);
    }

    public static RequestBody create(@Nullable MediaType mediaType, byte[] bArr, int i, int i2) {
        if (bArr != null) {
            Util.checkOffsetAndCount(bArr.length, i, i2);
            return new b(mediaType, i2, bArr, i);
        }
        throw new NullPointerException("content == null");
    }

    public static RequestBody create(@Nullable MediaType mediaType, File file) {
        if (file != null) {
            return new c(mediaType, file);
        }
        throw new NullPointerException("file == null");
    }
}
