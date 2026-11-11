package x0;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class a {
    private static void a(ByteBuffer byteBuffer) {
        if (byteBuffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    public static d b(FileChannel fileChannel) {
        return c(fileChannel, d(fileChannel));
    }

    public static d c(FileChannel fileChannel, long j) throws f, IOException {
        if (j < 32) {
            throw new f("APK too small for APK Signing Block. ZIP Central Directory offset: " + j);
        }
        fileChannel.position(j - 24);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        fileChannel.read(byteBufferAllocate);
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        byteBufferAllocate.order(byteOrder);
        if (byteBufferAllocate.getLong(8) != 2334950737559900225L || byteBufferAllocate.getLong(16) != 3617552046287187010L) {
            throw new f("No APK Signing Block before ZIP Central Directory");
        }
        long j2 = byteBufferAllocate.getLong(0);
        if (j2 < byteBufferAllocate.capacity() || j2 > 2147483639) {
            throw new f("APK Signing Block size out of range: " + j2);
        }
        int i = (int) (8 + j2);
        long j3 = j - ((long) i);
        if (j3 < 0) {
            throw new f("APK Signing Block offset out of range: " + j3);
        }
        fileChannel.position(j3);
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(i);
        fileChannel.read(byteBufferAllocate2);
        byteBufferAllocate2.order(byteOrder);
        long j4 = byteBufferAllocate2.getLong(0);
        if (j4 == j2) {
            return d.b(byteBufferAllocate2, Long.valueOf(j3));
        }
        throw new f("APK Signing Block sizes in header and footer do not match: " + j4 + " vs " + j2);
    }

    public static long d(FileChannel fileChannel) {
        return e(fileChannel, h(fileChannel));
    }

    public static long e(FileChannel fileChannel, long j) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        fileChannel.position((fileChannel.size() - j) - 6);
        fileChannel.read(byteBufferAllocate);
        return byteBufferAllocate.getInt(0);
    }

    public static Map f(ByteBuffer byteBuffer) throws f {
        a(byteBuffer);
        ByteBuffer byteBufferI = i(byteBuffer, 8, byteBuffer.capacity() - 24);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int i = 0;
        while (byteBufferI.hasRemaining()) {
            i++;
            if (byteBufferI.remaining() < 8) {
                throw new f("Insufficient data to read size of APK Signing Block entry #" + i);
            }
            long j = byteBufferI.getLong();
            if (j < 4 || j > 2147483647L) {
                throw new f("APK Signing Block entry #" + i + " size out of range: " + j);
            }
            int i2 = (int) j;
            int iPosition = byteBufferI.position() + i2;
            if (i2 > byteBufferI.remaining()) {
                throw new f("APK Signing Block entry #" + i + " size out of range: " + i2 + ", available: " + byteBufferI.remaining());
            }
            linkedHashMap.put(Integer.valueOf(byteBufferI.getInt()), g(byteBufferI, i2 - 4));
            byteBufferI.position(iPosition);
        }
        return linkedHashMap;
    }

    private static ByteBuffer g(ByteBuffer byteBuffer, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("size: " + i);
        }
        int iLimit = byteBuffer.limit();
        int iPosition = byteBuffer.position();
        int i2 = i + iPosition;
        if (i2 < iPosition || i2 > iLimit) {
            throw new BufferUnderflowException();
        }
        byteBuffer.limit(i2);
        try {
            ByteBuffer byteBufferSlice = byteBuffer.slice();
            byteBufferSlice.order(byteBuffer.order());
            byteBuffer.position(i2);
            return byteBufferSlice;
        } finally {
            byteBuffer.limit(iLimit);
        }
    }

    public static long h(FileChannel fileChannel) throws IOException {
        long size = fileChannel.size();
        if (size < 22) {
            throw new IOException("APK too small for ZIP End of Central Directory (EOCD) record");
        }
        long j = size - 22;
        long jMin = Math.min(j, 65535L);
        int i = 0;
        while (true) {
            long j2 = i;
            if (j2 > jMin) {
                throw new IOException("ZIP End of Central Directory (EOCD) record not found");
            }
            long j3 = j - j2;
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
            fileChannel.position(j3);
            fileChannel.read(byteBufferAllocate);
            ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
            byteBufferAllocate.order(byteOrder);
            if (byteBufferAllocate.getInt(0) == 101010256) {
                ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(2);
                fileChannel.position(j3 + 20);
                fileChannel.read(byteBufferAllocate2);
                byteBufferAllocate2.order(byteOrder);
                short s = byteBufferAllocate2.getShort(0);
                if (s == i) {
                    return s;
                }
            }
            i++;
        }
    }

    private static ByteBuffer i(ByteBuffer byteBuffer, int i, int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("start: " + i);
        }
        if (i2 < i) {
            throw new IllegalArgumentException("end < start: " + i2 + " < " + i);
        }
        int iCapacity = byteBuffer.capacity();
        if (i2 > byteBuffer.capacity()) {
            throw new IllegalArgumentException("end > capacity: " + i2 + " > " + iCapacity);
        }
        int iLimit = byteBuffer.limit();
        int iPosition = byteBuffer.position();
        try {
            byteBuffer.position(0);
            byteBuffer.limit(i2);
            byteBuffer.position(i);
            ByteBuffer byteBufferSlice = byteBuffer.slice();
            byteBufferSlice.order(byteBuffer.order());
            return byteBufferSlice;
        } finally {
            byteBuffer.position(0);
            byteBuffer.limit(iLimit);
            byteBuffer.position(iPosition);
        }
    }
}
