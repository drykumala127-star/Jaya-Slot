package org.cocos2dx.okhttp3.internal.http;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.cocos2dx.okhttp3.Challenge;
import org.cocos2dx.okhttp3.Cookie;
import org.cocos2dx.okhttp3.CookieJar;
import org.cocos2dx.okhttp3.Headers;
import org.cocos2dx.okhttp3.HttpUrl;
import org.cocos2dx.okhttp3.Request;
import org.cocos2dx.okhttp3.Response;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okio.Buffer;
import org.cocos2dx.okio.ByteString;

public final class HttpHeaders {
    private static final ByteString QUOTED_STRING_DELIMITERS = ByteString.encodeUtf8("\"\\");
    private static final ByteString TOKEN_DELIMITERS = ByteString.encodeUtf8("\t ,=");

    private HttpHeaders() {
    }

    public static long contentLength(Response response) {
        return contentLength(response.headers());
    }

    public static boolean hasBody(Response response) {
        if (response.request().method().equals("HEAD")) {
            return false;
        }
        int iCode = response.code();
        return (((iCode >= 100 && iCode < 200) || iCode == 204 || iCode == 304) && contentLength(response) == -1 && !"chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) ? false : true;
    }

    public static boolean hasVaryAll(Response response) {
        return hasVaryAll(response.headers());
    }

    private static void parseChallengeHeader(List list, Buffer buffer) {
        String quotedString;
        while (true) {
            String token = null;
            while (true) {
                if (token == null) {
                    skipWhitespaceAndCommas(buffer);
                    token = readToken(buffer);
                    if (token == null) {
                        return;
                    }
                }
                boolean zSkipWhitespaceAndCommas = skipWhitespaceAndCommas(buffer);
                String token2 = readToken(buffer);
                if (token2 == null) {
                    if (buffer.exhausted()) {
                        list.add(new Challenge(token, Collections.emptyMap()));
                        return;
                    }
                    return;
                }
                int iSkipAll = skipAll(buffer, (byte) 61);
                boolean zSkipWhitespaceAndCommas2 = skipWhitespaceAndCommas(buffer);
                if (zSkipWhitespaceAndCommas || !(zSkipWhitespaceAndCommas2 || buffer.exhausted())) {
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    int iSkipAll2 = iSkipAll + skipAll(buffer, (byte) 61);
                    while (true) {
                        if (token2 == null) {
                            token2 = readToken(buffer);
                            if (skipWhitespaceAndCommas(buffer)) {
                                break;
                            }
                            iSkipAll2 = skipAll(buffer, (byte) 61);
                            if (iSkipAll2 == 0) {
                                break;
                            }
                            if (iSkipAll2 <= 1 || skipWhitespaceAndCommas(buffer)) {
                                return;
                            }
                            quotedString = (buffer.exhausted() && buffer.getByte(0L) == 34) ? readQuotedString(buffer) : readToken(buffer);
                            if (quotedString != null || ((String) linkedHashMap.put(token2, quotedString)) != null) {
                                return;
                            }
                            if (skipWhitespaceAndCommas(buffer) && !buffer.exhausted()) {
                                return;
                            } else {
                                token2 = null;
                            }
                        } else if (iSkipAll2 == 0) {
                            break;
                            break;
                        } else {
                            if (iSkipAll2 <= 1) {
                                return;
                            }
                            if (buffer.exhausted()) {
                            }
                            if (quotedString != null) {
                                return;
                            }
                            if (skipWhitespaceAndCommas(buffer)) {
                            }
                            token2 = null;
                        }
                    }
                    list.add(new Challenge(token, linkedHashMap));
                    token = token2;
                } else {
                    list.add(new Challenge(token, Collections.singletonMap(null, token2 + repeat('=', iSkipAll))));
                }
            }
        }
    }

    public static List parseChallenges(Headers headers, String str) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < headers.size(); i++) {
            if (str.equalsIgnoreCase(headers.name(i))) {
                parseChallengeHeader(arrayList, new Buffer().writeUtf8(headers.value(i)));
            }
        }
        return arrayList;
    }

    public static int parseSeconds(String str, int i) {
        try {
            long j = Long.parseLong(str);
            if (j > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            if (j < 0) {
                return 0;
            }
            return (int) j;
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    private static String readQuotedString(Buffer buffer) {
        if (buffer.readByte() != 34) {
            throw new IllegalArgumentException();
        }
        Buffer buffer2 = new Buffer();
        while (true) {
            long jIndexOfElement = buffer.indexOfElement(QUOTED_STRING_DELIMITERS);
            if (jIndexOfElement == -1) {
                return null;
            }
            if (buffer.getByte(jIndexOfElement) == 34) {
                buffer2.write(buffer, jIndexOfElement);
                buffer.readByte();
                return buffer2.readUtf8();
            }
            if (buffer.size() == jIndexOfElement + 1) {
                return null;
            }
            buffer2.write(buffer, jIndexOfElement);
            buffer.readByte();
            buffer2.write(buffer, 1L);
        }
    }

    private static String readToken(Buffer buffer) {
        try {
            long jIndexOfElement = buffer.indexOfElement(TOKEN_DELIMITERS);
            if (jIndexOfElement == -1) {
                jIndexOfElement = buffer.size();
            }
            if (jIndexOfElement != 0) {
                return buffer.readUtf8(jIndexOfElement);
            }
            return null;
        } catch (EOFException unused) {
            throw new AssertionError();
        }
    }

    public static void receiveHeaders(CookieJar cookieJar, HttpUrl httpUrl, Headers headers) {
        if (cookieJar == CookieJar.NO_COOKIES) {
            return;
        }
        List all = Cookie.parseAll(httpUrl, headers);
        if (all.isEmpty()) {
            return;
        }
        cookieJar.saveFromResponse(httpUrl, all);
    }

    private static String repeat(char c, int i) {
        char[] cArr = new char[i];
        Arrays.fill(cArr, c);
        return new String(cArr);
    }

    private static int skipAll(Buffer buffer, byte b) {
        int i = 0;
        while (!buffer.exhausted() && buffer.getByte(0L) == b) {
            i++;
            buffer.readByte();
        }
        return i;
    }

    public static int skipUntil(String str, int i, String str2) {
        while (i < str.length() && str2.indexOf(str.charAt(i)) == -1) {
            i++;
        }
        return i;
    }

    public static int skipWhitespace(String str, int i) {
        char cCharAt;
        while (i < str.length() && ((cCharAt = str.charAt(i)) == ' ' || cCharAt == '\t')) {
            i++;
        }
        return i;
    }

    private static boolean skipWhitespaceAndCommas(Buffer buffer) {
        boolean z = false;
        while (!buffer.exhausted()) {
            byte b = buffer.getByte(0L);
            if (b != 44) {
                if (b != 32 && b != 9) {
                    break;
                }
                buffer.readByte();
            } else {
                buffer.readByte();
                z = true;
            }
        }
        return z;
    }

    private static long stringToLong(String str) {
        if (str == null) {
            return -1L;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return -1L;
        }
    }

    private static Set varyFields(Response response) {
        return varyFields(response.headers());
    }

    public static Headers varyHeaders(Response response) {
        return varyHeaders(response.networkResponse().request().headers(), response.headers());
    }

    public static boolean varyMatches(Response response, Headers headers, Request request) {
        for (String str : varyFields(response)) {
            if (!Util.equal(headers.values(str), request.headers(str))) {
                return false;
            }
        }
        return true;
    }

    public static long contentLength(Headers headers) {
        return stringToLong(headers.get("Content-Length"));
    }

    public static boolean hasVaryAll(Headers headers) {
        return varyFields(headers).contains("*");
    }

    public static Set varyFields(Headers headers) {
        Set setEmptySet = Collections.emptySet();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            if ("Vary".equalsIgnoreCase(headers.name(i))) {
                String strValue = headers.value(i);
                if (setEmptySet.isEmpty()) {
                    setEmptySet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
                }
                for (String str : strValue.split(",")) {
                    setEmptySet.add(str.trim());
                }
            }
        }
        return setEmptySet;
    }

    public static Headers varyHeaders(Headers headers, Headers headers2) {
        Set setVaryFields = varyFields(headers2);
        if (setVaryFields.isEmpty()) {
            return new Headers.Builder().build();
        }
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String strName = headers.name(i);
            if (setVaryFields.contains(strName)) {
                builder.add(strName, headers.value(i));
            }
        }
        return builder.build();
    }
}
