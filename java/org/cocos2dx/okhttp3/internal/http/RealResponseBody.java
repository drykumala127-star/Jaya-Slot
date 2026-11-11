package org.cocos2dx.okhttp3.internal.http;

import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.MediaType;
import org.cocos2dx.okhttp3.ResponseBody;
import org.cocos2dx.okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
    private final long contentLength;

    @Nullable
    private final String contentTypeString;
    private final BufferedSource source;

    public RealResponseBody(@Nullable String str, long j, BufferedSource bufferedSource) {
        this.contentTypeString = str;
        this.contentLength = j;
        this.source = bufferedSource;
    }

    @Override
    public long contentLength() {
        return this.contentLength;
    }

    @Override
    public MediaType contentType() {
        String str = this.contentTypeString;
        if (str != null) {
            return MediaType.parse(str);
        }
        return null;
    }

    @Override
    public BufferedSource source() {
        return this.source;
    }
}
