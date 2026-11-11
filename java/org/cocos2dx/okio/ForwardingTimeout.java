package org.cocos2dx.okio;

import java.util.concurrent.TimeUnit;

public class ForwardingTimeout extends Timeout {
    private Timeout delegate;

    public ForwardingTimeout(Timeout timeout) {
        if (timeout == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = timeout;
    }

    @Override
    public Timeout clearDeadline() {
        return this.delegate.clearDeadline();
    }

    @Override
    public Timeout clearTimeout() {
        return this.delegate.clearTimeout();
    }

    @Override
    public long deadlineNanoTime() {
        return this.delegate.deadlineNanoTime();
    }

    public final Timeout delegate() {
        return this.delegate;
    }

    @Override
    public boolean hasDeadline() {
        return this.delegate.hasDeadline();
    }

    public final ForwardingTimeout setDelegate(Timeout timeout) {
        if (timeout == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = timeout;
        return this;
    }

    @Override
    public void throwIfReached() {
        this.delegate.throwIfReached();
    }

    @Override
    public Timeout timeout(long j, TimeUnit timeUnit) {
        return this.delegate.timeout(j, timeUnit);
    }

    @Override
    public long timeoutNanos() {
        return this.delegate.timeoutNanos();
    }

    @Override
    public Timeout deadlineNanoTime(long j) {
        return this.delegate.deadlineNanoTime(j);
    }
}
