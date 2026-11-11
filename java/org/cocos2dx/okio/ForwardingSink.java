package org.cocos2dx.okio;

public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = sink;
    }

    @Override
    public void close() {
        this.delegate.close();
    }

    public final Sink delegate() {
        return this.delegate;
    }

    @Override
    public void flush() {
        this.delegate.flush();
    }

    @Override
    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
    }

    @Override
    public void write(Buffer buffer, long j) {
        this.delegate.write(buffer, j);
    }
}
