package org.cocos2dx.okio;

import java.io.Closeable;

public interface Source extends Closeable {
    @Override
    void close();

    long read(Buffer buffer, long j);

    Timeout timeout();
}
