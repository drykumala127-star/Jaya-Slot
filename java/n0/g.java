package n0;

import android.util.Base64;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;

public class g {
    private static final byte a = Byte.parseByte("01110000", 2);
    private static final byte b = Byte.parseByte("00001111", 2);

    private static String b(byte[] bArr) {
        return new String(Base64.encode(bArr, 11), Charset.defaultCharset()).substring(0, 22);
    }

    private static byte[] c(UUID uuid, byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.putLong(uuid.getMostSignificantBits());
        byteBufferWrap.putLong(uuid.getLeastSignificantBits());
        return byteBufferWrap.array();
    }

    public String a() {
        byte[] bArrC = c(UUID.randomUUID(), new byte[17]);
        byte b2 = bArrC[0];
        bArrC[16] = b2;
        bArrC[0] = (byte) ((b2 & b) | a);
        return b(bArrC);
    }
}
