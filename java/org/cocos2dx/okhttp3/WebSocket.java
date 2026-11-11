package org.cocos2dx.okhttp3;

import javax.annotation.Nullable;
import org.cocos2dx.okio.ByteString;

public interface WebSocket {

    public interface Factory {
        WebSocket newWebSocket(Request request, WebSocketListener webSocketListener);
    }

    void cancel();

    boolean close(int i, @Nullable String str);

    long queueSize();

    Request request();

    boolean send(String str);

    boolean send(ByteString byteString);
}
