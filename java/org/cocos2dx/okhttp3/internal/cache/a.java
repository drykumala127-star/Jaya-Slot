package org.cocos2dx.okhttp3.internal.cache;

import java.io.IOException;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.ForwardingSink;
import org.cocos2dx.okio.Sink;

abstract class a extends ForwardingSink {
    private boolean i;

    a(Sink sink) {
        super(sink);
    }

    protected abstract void a(IOException iOException);

    @Override
    public void close() {
        if (this.i) {
            return;
        }
        try {
            super.close();
        } catch (IOException e) {
            this.i = true;
            a(e);
        }
    }

    @Override
    public void flush() {
        if (this.i) {
            return;
        }
        try {
            super.flush();
        } catch (IOException e) {
            this.i = true;
            a(e);
        }
    }

    @Override
    public void write(Buffer buffer, long j) {
        if (this.i) {
            buffer.skip(j);
            return;
        }
        try {
            super.write(buffer, j);
        } catch (IOException e) {
            this.i = true;
            a(e);
        }
    }
}
