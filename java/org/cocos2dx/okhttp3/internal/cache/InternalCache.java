package org.cocos2dx.okhttp3.internal.cache;

import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;

public interface InternalCache {
    Response get(Request request);

    CacheRequest put(Response response);

    void remove(Request request);

    void trackConditionalCacheHit();

    void trackResponse(CacheStrategy cacheStrategy);

    void update(Response response, Response response2);
}
