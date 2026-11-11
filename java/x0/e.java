package x0;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Map;

public abstract class e {
    public static byte[] a(File file, int i) {
        ByteBuffer byteBuffer;
        Map mapB = b(file);
        if (mapB == null || (byteBuffer = (ByteBuffer) mapB.get(Integer.valueOf(i))) == null) {
            return null;
        }
        return c(byteBuffer);
    }

    private static Map b(File file) {
        FileChannel channel;
        RandomAccessFile randomAccessFile;
        Map mapF = null;
        try {
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
                try {
                    channel = randomAccessFile.getChannel();
                } catch (IOException unused) {
                    channel = null;
                } catch (Throwable th) {
                    th = th;
                    channel = null;
                }
            } catch (IOException | f unused2) {
            }
            try {
                mapF = a.f((ByteBuffer) a.b(channel).a());
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException unused3) {
                    }
                }
            } catch (IOException unused4) {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException unused5) {
                    }
                }
                if (randomAccessFile != null) {
                }
                return mapF;
            } catch (Throwable th2) {
                th = th2;
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException unused6) {
                    }
                }
                if (randomAccessFile == null) {
                    throw th;
                }
                try {
                    randomAccessFile.close();
                    throw th;
                } catch (IOException unused7) {
                    throw th;
                }
            }
        } catch (IOException unused8) {
            channel = null;
            randomAccessFile = null;
        } catch (Throwable th3) {
            th = th3;
            channel = null;
            randomAccessFile = null;
        }
        randomAccessFile.close();
        return mapF;
    }

    private static byte[] c(ByteBuffer byteBuffer) {
        byte[] bArrArray = byteBuffer.array();
        int iArrayOffset = byteBuffer.arrayOffset();
        return Arrays.copyOfRange(bArrArray, byteBuffer.position() + iArrayOffset, iArrayOffset + byteBuffer.limit());
    }

    public static String d(File file, int i) {
        byte[] bArrA = a(file, i);
        if (bArrA == null) {
            return null;
        }
        try {
            return new String(bArrA, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
