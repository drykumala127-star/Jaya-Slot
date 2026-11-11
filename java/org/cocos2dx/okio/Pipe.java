package org.cocos2dx.okio;

import java.io.IOException;

public final class Pipe {
    final long maxBufferSize;
    boolean sinkClosed;
    boolean sourceClosed;
    final Buffer buffer = new Buffer();
    private final Sink sink = new a();
    private final Source source = new b();

    final class a implements Sink {
        final Timeout i = new Timeout();

        a() {
        }

        @Override
        public void close() {
            synchronized (Pipe.this.buffer) {
                try {
                    Pipe pipe = Pipe.this;
                    if (pipe.sinkClosed) {
                        return;
                    }
                    if (pipe.sourceClosed && pipe.buffer.size() > 0) {
                        throw new IOException("source is closed");
                    }
                    Pipe pipe2 = Pipe.this;
                    pipe2.sinkClosed = true;
                    pipe2.buffer.notifyAll();
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public void flush() {
            synchronized (Pipe.this.buffer) {
                try {
                    Pipe pipe = Pipe.this;
                    if (pipe.sinkClosed) {
                        throw new IllegalStateException("closed");
                    }
                    if (pipe.sourceClosed && pipe.buffer.size() > 0) {
                        throw new IOException("source is closed");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }

        @Override
        public void write(Buffer buffer, long j) {
            synchronized (Pipe.this.buffer) {
                try {
                    if (Pipe.this.sinkClosed) {
                        throw new IllegalStateException("closed");
                    }
                    while (j > 0) {
                        Pipe pipe = Pipe.this;
                        if (pipe.sourceClosed) {
                            throw new IOException("source is closed");
                        }
                        long size = pipe.maxBufferSize - pipe.buffer.size();
                        if (size == 0) {
                            this.i.waitUntilNotified(Pipe.this.buffer);
                        } else {
                            long jMin = Math.min(size, j);
                            Pipe.this.buffer.write(buffer, jMin);
                            j -= jMin;
                            Pipe.this.buffer.notifyAll();
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    final class b implements Source {
        final Timeout i = new Timeout();

        b() {
        }

        @Override
        public void close() {
            synchronized (Pipe.this.buffer) {
                Pipe pipe = Pipe.this;
                pipe.sourceClosed = true;
                pipe.buffer.notifyAll();
            }
        }

        @Override
        public long read(Buffer buffer, long j) {
            synchronized (Pipe.this.buffer) {
                try {
                    if (Pipe.this.sourceClosed) {
                        throw new IllegalStateException("closed");
                    }
                    while (Pipe.this.buffer.size() == 0) {
                        Pipe pipe = Pipe.this;
                        if (pipe.sinkClosed) {
                            return -1L;
                        }
                        this.i.waitUntilNotified(pipe.buffer);
                    }
                    long j2 = Pipe.this.buffer.read(buffer, j);
                    Pipe.this.buffer.notifyAll();
                    return j2;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }
    }

    public Pipe(long j) {
        if (j >= 1) {
            this.maxBufferSize = j;
            return;
        }
        throw new IllegalArgumentException("maxBufferSize < 1: " + j);
    }

    public final Sink sink() {
        return this.sink;
    }

    public final Source source() {
        return this.source;
    }
}
