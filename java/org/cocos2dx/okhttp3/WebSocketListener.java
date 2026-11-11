package org.cocos2dx.okhttp3;

import javax.annotation.Nullable;
import org.cocos2dx.okio.ByteString;

public abstract class WebSocketListener {
    public void onClosed(WebSocket webSocket, int i, String str) {
    }

    public void onClosing(WebSocket webSocket, int i, String str) {
    }

    public void onFailure(WebSocket webSocket, Throwable th, @Nullable Response response) {
    }

    public void onMessage(WebSocket webSocket, String str) {
    }

    public void onOpen(WebSocket webSocket, Response response) {
    }

    public void onMessage(WebSocket webSocket, ByteString byteString) {
    }
}
