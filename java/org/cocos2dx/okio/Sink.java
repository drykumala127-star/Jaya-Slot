package org.cocos2dx.okio;

import java.io.Closeable;
import java.io.Flushable;

public interface Sink extends Closeable, Flushable {
    @Override
    void close();

    void flush();

    Timeout timeout();

    void write(Buffer buffer, long j);
}
