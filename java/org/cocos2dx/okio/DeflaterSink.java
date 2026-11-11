package org.cocos2dx.okio;

import java.util.zip.Deflater;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class DeflaterSink implements Sink {
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;

    public DeflaterSink(Sink sink, Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }

    @IgnoreJRERequirement
    private void deflate(boolean z) {
        f fVarWritableSegment;
        int iDeflate;
        Buffer buffer = this.sink.buffer();
        while (true) {
            fVarWritableSegment = buffer.writableSegment(1);
            if (z) {
                Deflater deflater = this.deflater;
                byte[] bArr = fVarWritableSegment.a;
                int i = fVarWritableSegment.c;
                iDeflate = deflater.deflate(bArr, i, 8192 - i, 2);
            } else {
                Deflater deflater2 = this.deflater;
                byte[] bArr2 = fVarWritableSegment.a;
                int i2 = fVarWritableSegment.c;
                iDeflate = deflater2.deflate(bArr2, i2, 8192 - i2);
            }
            if (iDeflate > 0) {
                fVarWritableSegment.c += iDeflate;
                buffer.size += (long) iDeflate;
                this.sink.emitCompleteSegments();
            } else if (this.deflater.needsInput()) {
                break;
            }
        }
        if (fVarWritableSegment.b == fVarWritableSegment.c) {
            buffer.head = fVarWritableSegment.b();
            g.a(fVarWritableSegment);
        }
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        finishDeflate();
        th = null;
        try {
            this.deflater.end();
        } catch (Throwable th) {
            if (th == null) {
                th = th;
            }
        }
        try {
            this.sink.close();
        } catch (Throwable th2) {
            if (th == null) {
                th = th2;
            }
        }
        this.closed = true;
        if (th != null) {
            i.f(th);
        }
    }

    void finishDeflate() {
        this.deflater.finish();
        deflate(false);
    }

    @Override
    public void flush() {
        deflate(true);
        this.sink.flush();
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        return "DeflaterSink(" + this.sink + ")";
    }

    @Override
    public void write(Buffer buffer, long j) {
        i.b(buffer.size, 0L, j);
        while (j > 0) {
            f fVar = buffer.head;
            int iMin = (int) Math.min(j, fVar.c - fVar.b);
            this.deflater.setInput(fVar.a, fVar.b, iMin);
            deflate(false);
            long j2 = iMin;
            buffer.size -= j2;
            int i = fVar.b + iMin;
            fVar.b = i;
            if (i == fVar.c) {
                buffer.head = fVar.b();
                g.a(fVar);
            }
            j -= j2;
        }
    }

    DeflaterSink(BufferedSink bufferedSink, Deflater deflater) {
        if (bufferedSink == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.sink = bufferedSink;
        this.deflater = deflater;
    }
}
