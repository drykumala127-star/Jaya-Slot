package org.cocos2dx.okhttp3.internal.http2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.ByteString;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Source;

abstract class a {
    static final Header[] a;
    static final Map b;

    static final class a {
        private final List a;
        private final BufferedSource b;
        private final int c;
        private int d;
        Header[] e;
        int f;
        int g;
        int h;

        a(int i, Source source) {
            this(i, i, source);
        }

        private void a() {
            int i = this.d;
            int i2 = this.h;
            if (i < i2) {
                if (i == 0) {
                    b();
                } else {
                    d(i2 - i);
                }
            }
        }

        private void b() {
            Arrays.fill(this.e, (Object) null);
            this.f = this.e.length - 1;
            this.g = 0;
            this.h = 0;
        }

        private int c(int i) {
            return this.f + 1 + i;
        }

        private int d(int i) {
            int i2;
            int i3 = 0;
            if (i > 0) {
                int length = this.e.length;
                while (true) {
                    length--;
                    i2 = this.f;
                    if (length < i2 || i <= 0) {
                        break;
                    }
                    int i4 = this.e[length].hpackSize;
                    i -= i4;
                    this.h -= i4;
                    this.g--;
                    i3++;
                }
                Header[] headerArr = this.e;
                System.arraycopy(headerArr, i2 + 1, headerArr, i2 + 1 + i3, this.g);
                this.f += i3;
            }
            return i3;
        }

        private ByteString f(int i) throws IOException {
            if (h(i)) {
                return a.a[i].name;
            }
            int iC = c(i - a.a.length);
            if (iC >= 0) {
                Header[] headerArr = this.e;
                if (iC < headerArr.length) {
                    return headerArr[iC].name;
                }
            }
            throw new IOException("Header index too large " + (i + 1));
        }

        private void g(int i, Header header) {
            this.a.add(header);
            int i2 = header.hpackSize;
            if (i != -1) {
                i2 -= this.e[c(i)].hpackSize;
            }
            int i3 = this.d;
            if (i2 > i3) {
                b();
                return;
            }
            int iD = d((this.h + i2) - i3);
            if (i == -1) {
                int i4 = this.g + 1;
                Header[] headerArr = this.e;
                if (i4 > headerArr.length) {
                    Header[] headerArr2 = new Header[headerArr.length * 2];
                    System.arraycopy(headerArr, 0, headerArr2, headerArr.length, headerArr.length);
                    this.f = this.e.length - 1;
                    this.e = headerArr2;
                }
                int i5 = this.f;
                this.f = i5 - 1;
                this.e[i5] = header;
                this.g++;
            } else {
                this.e[i + c(i) + iD] = header;
            }
            this.h += i2;
        }

        private boolean h(int i) {
            return i >= 0 && i <= a.a.length - 1;
        }

        private int i() {
            return this.b.readByte() & 255;
        }

        private void l(int i) throws IOException {
            if (h(i)) {
                this.a.add(a.a[i]);
                return;
            }
            int iC = c(i - a.a.length);
            if (iC >= 0) {
                Header[] headerArr = this.e;
                if (iC < headerArr.length) {
                    this.a.add(headerArr[iC]);
                    return;
                }
            }
            throw new IOException("Header index too large " + (i + 1));
        }

        private void n(int i) {
            g(-1, new Header(f(i), j()));
        }

        private void o() {
            g(-1, new Header(a.a(j()), j()));
        }

        private void p(int i) throws IOException {
            this.a.add(new Header(f(i), j()));
        }

        private void q() throws IOException {
            this.a.add(new Header(a.a(j()), j()));
        }

        public List e() {
            ArrayList arrayList = new ArrayList(this.a);
            this.a.clear();
            return arrayList;
        }

        ByteString j() {
            int i = i();
            boolean z = (i & 128) == 128;
            int iM = m(i, 127);
            return z ? ByteString.of(d.f().c(this.b.readByteArray(iM))) : this.b.readByteString(iM);
        }

        void k() throws IOException {
            while (!this.b.exhausted()) {
                byte b = this.b.readByte();
                int i = b & 255;
                if (i == 128) {
                    throw new IOException("index == 0");
                }
                if ((b & 128) == 128) {
                    l(m(i, 127) - 1);
                } else if (i == 64) {
                    o();
                } else if ((b & 64) == 64) {
                    n(m(i, 63) - 1);
                } else if ((b & 32) == 32) {
                    int iM = m(i, 31);
                    this.d = iM;
                    if (iM < 0 || iM > this.c) {
                        throw new IOException("Invalid dynamic table size update " + this.d);
                    }
                    a();
                } else if (i == 16 || i == 0) {
                    q();
                } else {
                    p(m(i, 15) - 1);
                }
            }
        }

        int m(int i, int i2) {
            int i3 = i & i2;
            if (i3 < i2) {
                return i3;
            }
            int i4 = 0;
            while (true) {
                int i5 = i();
                if ((i5 & 128) == 0) {
                    return i2 + (i5 << i4);
                }
                i2 += (i5 & 127) << i4;
                i4 += 7;
            }
        }

        a(int i, int i2, Source source) {
            this.a = new ArrayList();
            this.e = new Header[8];
            this.f = r0.length - 1;
            this.g = 0;
            this.h = 0;
            this.c = i;
            this.d = i2;
            this.b = Okio.buffer(source);
        }
    }

    static final class b {
        private final Buffer a;
        private final boolean b;
        private int c;
        private boolean d;
        int e;
        int f;
        Header[] g;
        int h;
        int i;
        int j;

        b(Buffer buffer) {
            this(4096, true, buffer);
        }

        private void a() {
            int i = this.f;
            int i2 = this.j;
            if (i < i2) {
                if (i == 0) {
                    b();
                } else {
                    c(i2 - i);
                }
            }
        }

        private void b() {
            Arrays.fill(this.g, (Object) null);
            this.h = this.g.length - 1;
            this.i = 0;
            this.j = 0;
        }

        private int c(int i) {
            int i2;
            int i3 = 0;
            if (i > 0) {
                int length = this.g.length;
                while (true) {
                    length--;
                    i2 = this.h;
                    if (length < i2 || i <= 0) {
                        break;
                    }
                    int i4 = this.g[length].hpackSize;
                    i -= i4;
                    this.j -= i4;
                    this.i--;
                    i3++;
                }
                Header[] headerArr = this.g;
                System.arraycopy(headerArr, i2 + 1, headerArr, i2 + 1 + i3, this.i);
                Header[] headerArr2 = this.g;
                int i5 = this.h;
                Arrays.fill(headerArr2, i5 + 1, i5 + 1 + i3, (Object) null);
                this.h += i3;
            }
            return i3;
        }

        private void d(Header header) {
            int i = header.hpackSize;
            int i2 = this.f;
            if (i > i2) {
                b();
                return;
            }
            c((this.j + i) - i2);
            int i3 = this.i + 1;
            Header[] headerArr = this.g;
            if (i3 > headerArr.length) {
                Header[] headerArr2 = new Header[headerArr.length * 2];
                System.arraycopy(headerArr, 0, headerArr2, headerArr.length, headerArr.length);
                this.h = this.g.length - 1;
                this.g = headerArr2;
            }
            int i4 = this.h;
            this.h = i4 - 1;
            this.g[i4] = header;
            this.i++;
            this.j += i;
        }

        void e(int i) {
            this.e = i;
            int iMin = Math.min(i, 16384);
            int i2 = this.f;
            if (i2 == iMin) {
                return;
            }
            if (iMin < i2) {
                this.c = Math.min(this.c, iMin);
            }
            this.d = true;
            this.f = iMin;
            a();
        }

        void f(ByteString byteString) {
            if (!this.b || d.f().e(byteString) >= byteString.size()) {
                h(byteString.size(), 127, 0);
                this.a.write(byteString);
                return;
            }
            Buffer buffer = new Buffer();
            d.f().d(byteString, buffer);
            ByteString byteString2 = buffer.readByteString();
            h(byteString2.size(), 127, 128);
            this.a.write(byteString2);
        }

        void g(List list) {
            int length;
            int length2;
            int i;
            int length3;
            if (this.d) {
                int i2 = this.c;
                if (i2 < this.f) {
                    h(i2, 31, 32);
                }
                this.d = false;
                this.c = Integer.MAX_VALUE;
                h(this.f, 31, 32);
            }
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                Header header = (Header) list.get(i3);
                ByteString asciiLowercase = header.name.toAsciiLowercase();
                ByteString byteString = header.value;
                Integer num = (Integer) a.b.get(asciiLowercase);
                if (num != null) {
                    int iIntValue = num.intValue();
                    length2 = iIntValue + 1;
                    if (length2 > 1 && length2 < 8) {
                        Header[] headerArr = a.a;
                        if (Util.equal(headerArr[iIntValue].value, byteString)) {
                            length = length2;
                        } else if (Util.equal(headerArr[length2].value, byteString)) {
                            length2 = iIntValue + 2;
                            length = length2;
                        }
                        if (length2 == -1) {
                            length3 = this.g.length;
                            for (i = this.h + 1; i < length3; i++) {
                                if (!Util.equal(this.g[i].name, asciiLowercase)) {
                                    if (Util.equal(this.g[i].value, byteString)) {
                                        length2 = (i - this.h) + a.a.length;
                                        break;
                                    } else if (length == -1) {
                                        length = (i - this.h) + a.a.length;
                                    }
                                }
                            }
                        }
                        if (length2 != -1) {
                            h(length2, 127, 128);
                        } else if (length == -1) {
                            this.a.writeByte(64);
                            f(asciiLowercase);
                            f(byteString);
                            d(header);
                        } else if (asciiLowercase.startsWith(Header.PSEUDO_PREFIX) || Header.TARGET_AUTHORITY.equals(asciiLowercase)) {
                            h(length, 63, 64);
                            f(byteString);
                            d(header);
                        } else {
                            h(length, 15, 0);
                            f(byteString);
                        }
                    }
                    length = length2;
                } else {
                    length = -1;
                }
                length2 = -1;
                if (length2 == -1) {
                    length3 = this.g.length;
                    while (i < length3) {
                        if (!Util.equal(this.g[i].name, asciiLowercase)) {
                            if (Util.equal(this.g[i].value, byteString)) {
                                length2 = (i - this.h) + a.a.length;
                                break;
                            } else if (length == -1) {
                                length = (i - this.h) + a.a.length;
                            }
                        }
                    }
                }
                if (length2 != -1) {
                    h(length2, 127, 128);
                } else if (length == -1) {
                    this.a.writeByte(64);
                    f(asciiLowercase);
                    f(byteString);
                    d(header);
                } else if (asciiLowercase.startsWith(Header.PSEUDO_PREFIX)) {
                    h(length, 63, 64);
                    f(byteString);
                    d(header);
                } else {
                    h(length, 63, 64);
                    f(byteString);
                    d(header);
                }
            }
        }

        void h(int i, int i2, int i3) {
            if (i < i2) {
                this.a.writeByte(i | i3);
                return;
            }
            this.a.writeByte(i3 | i2);
            int i4 = i - i2;
            while (i4 >= 128) {
                this.a.writeByte(128 | (i4 & 127));
                i4 >>>= 7;
            }
            this.a.writeByte(i4);
        }

        b(int i, boolean z, Buffer buffer) {
            this.c = Integer.MAX_VALUE;
            this.g = new Header[8];
            this.h = r0.length - 1;
            this.i = 0;
            this.j = 0;
            this.e = i;
            this.f = i;
            this.b = z;
            this.a = buffer;
        }
    }

    static {
        Header header = new Header(Header.TARGET_AUTHORITY, "");
        ByteString byteString = Header.TARGET_METHOD;
        Header header2 = new Header(byteString, "GET");
        Header header3 = new Header(byteString, "POST");
        ByteString byteString2 = Header.TARGET_PATH;
        Header header4 = new Header(byteString2, "/");
        Header header5 = new Header(byteString2, "/index.html");
        ByteString byteString3 = Header.TARGET_SCHEME;
        Header header6 = new Header(byteString3, "http");
        Header header7 = new Header(byteString3, "https");
        ByteString byteString4 = Header.RESPONSE_STATUS;
        a = new Header[]{header, header2, header3, header4, header5, header6, header7, new Header(byteString4, "200"), new Header(byteString4, "204"), new Header(byteString4, "206"), new Header(byteString4, "304"), new Header(byteString4, "400"), new Header(byteString4, "404"), new Header(byteString4, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), new Header("www-authenticate", "")};
        b = b();
    }

    static ByteString a(ByteString byteString) throws IOException {
        int size = byteString.size();
        for (int i = 0; i < size; i++) {
            byte b2 = byteString.getByte(i);
            if (b2 >= 65 && b2 <= 90) {
                throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + byteString.utf8());
            }
        }
        return byteString;
    }

    private static Map b() {
        LinkedHashMap linkedHashMap = new LinkedHashMap(a.length);
        int i = 0;
        while (true) {
            Header[] headerArr = a;
            if (i >= headerArr.length) {
                return Collections.unmodifiableMap(linkedHashMap);
            }
            if (!linkedHashMap.containsKey(headerArr[i].name)) {
                linkedHashMap.put(headerArr[i].name, Integer.valueOf(i));
            }
            i++;
        }
    }
}
