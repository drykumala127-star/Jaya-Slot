package org.cocos2dx.okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class Okio {
    static final Logger logger = Logger.getLogger(Okio.class.getName());

    final class a implements Sink {
        final Timeout i;
        final OutputStream j;

        a(Timeout timeout, OutputStream outputStream) {
            this.i = timeout;
            this.j = outputStream;
        }

        @Override
        public void close() throws IOException {
            this.j.close();
        }

        @Override
        public void flush() throws IOException {
            this.j.flush();
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }

        public String toString() {
            return "sink(" + this.j + ")";
        }

        @Override
        public void write(Buffer buffer, long j) throws IOException {
            i.b(buffer.size, 0L, j);
            while (j > 0) {
                this.i.throwIfReached();
                f fVar = buffer.head;
                int iMin = (int) Math.min(j, fVar.c - fVar.b);
                this.j.write(fVar.a, fVar.b, iMin);
                int i = fVar.b + iMin;
                fVar.b = i;
                long j2 = iMin;
                j -= j2;
                buffer.size -= j2;
                if (i == fVar.c) {
                    buffer.head = fVar.b();
                    g.a(fVar);
                }
            }
        }
    }

    final class b implements Source {
        final Timeout i;
        final InputStream j;

        b(Timeout timeout, InputStream inputStream) {
            this.i = timeout;
            this.j = inputStream;
        }

        @Override
        public void close() throws IOException {
            this.j.close();
        }

        @Override
        public long read(Buffer buffer, long j) throws IOException {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            if (j == 0) {
                return 0L;
            }
            try {
                this.i.throwIfReached();
                f fVarWritableSegment = buffer.writableSegment(1);
                int i = this.j.read(fVarWritableSegment.a, fVarWritableSegment.c, (int) Math.min(j, 8192 - fVarWritableSegment.c));
                if (i == -1) {
                    return -1L;
                }
                fVarWritableSegment.c += i;
                long j2 = i;
                buffer.size += j2;
                return j2;
            } catch (AssertionError e) {
                if (Okio.isAndroidGetsocknameError(e)) {
                    throw new IOException(e);
                }
                throw e;
            }
        }

        @Override
        public Timeout timeout() {
            return this.i;
        }

        public String toString() {
            return "source(" + this.j + ")";
        }
    }

    final class c implements Sink {
        c() {
        }

        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }

        @Override
        public void write(Buffer buffer, long j) {
            buffer.skip(j);
        }
    }

    final class d extends AsyncTimeout {
        final Socket a;

        d(Socket socket) {
            this.a = socket;
        }

        @Override
        protected IOException newTimeoutException(IOException iOException) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        @Override
        protected void timedOut() {
            try {
                this.a.close();
            } catch (AssertionError e) {
                if (!Okio.isAndroidGetsocknameError(e)) {
                    throw e;
                }
                Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + this.a, (Throwable) e);
            } catch (Exception e2) {
                Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + this.a, (Throwable) e2);
            }
        }
    }

    private Okio() {
    }

    public static Sink appendingSink(File file) {
        if (file != null) {
            return sink(new FileOutputStream(file, true));
        }
        throw new IllegalArgumentException("file == null");
    }

    public static Sink blackhole() {
        return new c();
    }

    public static BufferedSource buffer(Source source) {
        return new e(source);
    }

    static boolean isAndroidGetsocknameError(AssertionError assertionError) {
        return (assertionError.getCause() == null || assertionError.getMessage() == null || !assertionError.getMessage().contains("getsockname failed")) ? false : true;
    }

    public static Sink sink(OutputStream outputStream) {
        return sink(outputStream, new Timeout());
    }

    public static Source source(InputStream inputStream) {
        return source(inputStream, new Timeout());
    }

    private static AsyncTimeout timeout(Socket socket) {
        return new d(socket);
    }

    public static BufferedSink buffer(Sink sink) {
        return new org.cocos2dx.okio.d(sink);
    }

    private static Sink sink(OutputStream outputStream, Timeout timeout) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        if (timeout != null) {
            return new a(timeout, outputStream);
        }
        throw new IllegalArgumentException("timeout == null");
    }

    private static Source source(InputStream inputStream, Timeout timeout) {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (timeout != null) {
            return new b(timeout, inputStream);
        }
        throw new IllegalArgumentException("timeout == null");
    }

    public static Sink sink(Socket socket) throws IOException {
        if (socket != null) {
            if (socket.getOutputStream() != null) {
                AsyncTimeout asyncTimeoutTimeout = timeout(socket);
                return asyncTimeoutTimeout.sink(sink(socket.getOutputStream(), asyncTimeoutTimeout));
            }
            throw new IOException("socket's output stream == null");
        }
        throw new IllegalArgumentException("socket == null");
    }

    public static Source source(File file) {
        if (file != null) {
            return source(new FileInputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }

    @IgnoreJRERequirement
    public static Source source(Path path, OpenOption... openOptionArr) {
        if (path != null) {
            return source(org.cocos2dx.okio.c.a(path, openOptionArr));
        }
        throw new IllegalArgumentException("path == null");
    }

    public static Source source(Socket socket) throws IOException {
        if (socket != null) {
            if (socket.getInputStream() != null) {
                AsyncTimeout asyncTimeoutTimeout = timeout(socket);
                return asyncTimeoutTimeout.source(source(socket.getInputStream(), asyncTimeoutTimeout));
            }
            throw new IOException("socket's input stream == null");
        }
        throw new IllegalArgumentException("socket == null");
    }

    public static Sink sink(File file) {
        if (file != null) {
            return sink(new FileOutputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }

    @IgnoreJRERequirement
    public static Sink sink(Path path, OpenOption... openOptionArr) {
        if (path != null) {
            return sink(org.cocos2dx.okio.b.a(path, openOptionArr));
        }
        throw new IllegalArgumentException("path == null");
    }
}
