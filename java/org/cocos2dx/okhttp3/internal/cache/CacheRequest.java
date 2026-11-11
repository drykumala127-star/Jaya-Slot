package org.cocos2dx.okhttp3.internal.cache;

import org.cocos2dx.okio.Sink;

public interface CacheRequest {
    void abort();

    Sink body();
}
