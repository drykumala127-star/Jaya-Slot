package org.cocos2dx.okio;

public abstract class ForwardingSource implements Source {
    private final Source delegate;

    public ForwardingSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = source;
    }

    @Override
    public void close() {
        this.delegate.close();
    }

    public final Source delegate() {
        return this.delegate;
    }

    @Override
    public long read(Buffer buffer, long j) {
        return this.delegate.read(buffer, j);
    }

    @Override
    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
    }
}
