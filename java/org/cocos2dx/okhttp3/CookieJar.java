package org.cocos2dx.okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
    public static final CookieJar NO_COOKIES = new a();

    class a implements CookieJar {
        a() {
        }

        @Override
        public List loadForRequest(HttpUrl httpUrl) {
            return Collections.emptyList();
        }

        @Override
        public void saveFromResponse(HttpUrl httpUrl, List list) {
        }
    }

    List loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List list);
}
