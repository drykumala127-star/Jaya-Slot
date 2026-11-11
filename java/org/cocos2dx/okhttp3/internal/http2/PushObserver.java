package org.cocos2dx.okhttp3.internal.http2;

import java.util.List;
import org.cocos2dx.okio.BufferedSource;

public interface PushObserver {
    public static final PushObserver CANCEL = new a();

    class a implements PushObserver {
        a() {
        }

        @Override
        public boolean onData(int i, BufferedSource bufferedSource, int i2, boolean z) {
            bufferedSource.skip(i2);
            return true;
        }

        @Override
        public boolean onHeaders(int i, List list, boolean z) {
            return true;
        }

        @Override
        public boolean onRequest(int i, List list) {
            return true;
        }

        @Override
        public void onReset(int i, ErrorCode errorCode) {
        }
    }

    boolean onData(int i, BufferedSource bufferedSource, int i2, boolean z);

    boolean onHeaders(int i, List list, boolean z);

    boolean onRequest(int i, List list);

    void onReset(int i, ErrorCode errorCode);
}
