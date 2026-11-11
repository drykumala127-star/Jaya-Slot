package org.cocos2dx.okhttp3;

import javax.annotation.Nullable;

public interface Authenticator {
    public static final Authenticator NONE = new a();

    class a implements Authenticator {
        a() {
        }

        @Override
        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    @Nullable
    Request authenticate(@Nullable Route route, Response response);
}
