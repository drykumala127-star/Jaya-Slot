package org.cocos2dx.okio;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class Options extends AbstractList<ByteString> implements RandomAccess {
    final ByteString[] byteStrings;
    final int[] trie;

    private Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    private static void buildTrieRecursive(long j, Buffer buffer, int i, List list, int i2, int i3, List list2) {
        int iIntValue;
        int i4;
        int i5;
        int i6;
        int i7;
        Buffer buffer2;
        if (i2 >= i3) {
            throw new AssertionError();
        }
        for (int i8 = i2; i8 < i3; i8++) {
            if (((ByteString) list.get(i8)).size() < i) {
                throw new AssertionError();
            }
        }
        ByteString byteString = (ByteString) list.get(i2);
        ByteString byteString2 = (ByteString) list.get(i3 - 1);
        if (i == byteString.size()) {
            int i9 = i2 + 1;
            i4 = i9;
            iIntValue = ((Integer) list2.get(i2)).intValue();
            byteString = (ByteString) list.get(i9);
        } else {
            iIntValue = -1;
            i4 = i2;
        }
        if (byteString.getByte(i) == byteString2.getByte(i)) {
            int iMin = Math.min(byteString.size(), byteString2.size());
            int i10 = 0;
            for (int i11 = i; i11 < iMin && byteString.getByte(i11) == byteString2.getByte(i11); i11++) {
                i10++;
            }
            long jIntCount = 1 + j + ((long) intCount(buffer)) + 2 + ((long) i10);
            buffer.writeInt(-i10);
            buffer.writeInt(iIntValue);
            int i12 = i;
            while (true) {
                i5 = i + i10;
                if (i12 >= i5) {
                    break;
                }
                buffer.writeInt(byteString.getByte(i12) & 255);
                i12++;
            }
            if (i4 + 1 == i3) {
                if (i5 != ((ByteString) list.get(i4)).size()) {
                    throw new AssertionError();
                }
                buffer.writeInt(((Integer) list2.get(i4)).intValue());
                return;
            } else {
                Buffer buffer3 = new Buffer();
                buffer.writeInt((int) ((((long) intCount(buffer3)) + jIntCount) * (-1)));
                buildTrieRecursive(jIntCount, buffer3, i5, list, i4, i3, list2);
                buffer.write(buffer3, buffer3.size());
                return;
            }
        }
        int i13 = 1;
        for (int i14 = i4 + 1; i14 < i3; i14++) {
            if (((ByteString) list.get(i14 - 1)).getByte(i) != ((ByteString) list.get(i14)).getByte(i)) {
                i13++;
            }
        }
        long jIntCount2 = j + ((long) intCount(buffer)) + 2 + ((long) (i13 * 2));
        buffer.writeInt(i13);
        buffer.writeInt(iIntValue);
        for (int i15 = i4; i15 < i3; i15++) {
            byte b = ((ByteString) list.get(i15)).getByte(i);
            if (i15 == i4 || b != ((ByteString) list.get(i15 - 1)).getByte(i)) {
                buffer.writeInt(b & 255);
            }
        }
        Buffer buffer4 = new Buffer();
        int i16 = i4;
        while (i16 < i3) {
            byte b2 = ((ByteString) list.get(i16)).getByte(i);
            int i17 = i16 + 1;
            int i18 = i17;
            while (true) {
                if (i18 >= i3) {
                    i6 = i3;
                    break;
                } else {
                    if (b2 != ((ByteString) list.get(i18)).getByte(i)) {
                        i6 = i18;
                        break;
                    }
                    i18++;
                }
            }
            if (i17 == i6 && i + 1 == ((ByteString) list.get(i16)).size()) {
                buffer.writeInt(((Integer) list2.get(i16)).intValue());
                i7 = i6;
                buffer2 = buffer4;
            } else {
                buffer.writeInt((int) ((((long) intCount(buffer4)) + jIntCount2) * (-1)));
                i7 = i6;
                buffer2 = buffer4;
                buildTrieRecursive(jIntCount2, buffer4, i + 1, list, i16, i6, list2);
            }
            buffer4 = buffer2;
            i16 = i7;
        }
        Buffer buffer5 = buffer4;
        buffer.write(buffer5, buffer5.size());
    }

    private static int intCount(Buffer buffer) {
        return (int) (buffer.size() / 4);
    }

    public static Options of(ByteString... byteStringArr) {
        if (byteStringArr.length == 0) {
            return new Options(new ByteString[0], new int[]{0, -1});
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(byteStringArr));
        Collections.sort(arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(-1);
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.set(Collections.binarySearch(arrayList, byteStringArr[i2]), Integer.valueOf(i2));
        }
        if (((ByteString) arrayList.get(0)).size() == 0) {
            throw new IllegalArgumentException("the empty byte string is not a supported option");
        }
        int i3 = 0;
        while (i3 < arrayList.size()) {
            ByteString byteString = (ByteString) arrayList.get(i3);
            int i4 = i3 + 1;
            int i5 = i4;
            while (i5 < arrayList.size()) {
                ByteString byteString2 = (ByteString) arrayList.get(i5);
                if (!byteString2.startsWith(byteString)) {
                    break;
                }
                if (byteString2.size() == byteString.size()) {
                    throw new IllegalArgumentException("duplicate option: " + byteString2);
                }
                if (((Integer) arrayList2.get(i5)).intValue() > ((Integer) arrayList2.get(i3)).intValue()) {
                    arrayList.remove(i5);
                    arrayList2.remove(i5);
                } else {
                    i5++;
                }
            }
            i3 = i4;
        }
        Buffer buffer = new Buffer();
        buildTrieRecursive(0L, buffer, 0, arrayList, 0, arrayList.size(), arrayList2);
        int iIntCount = intCount(buffer);
        int[] iArr = new int[iIntCount];
        for (int i6 = 0; i6 < iIntCount; i6++) {
            iArr[i6] = buffer.readInt();
        }
        if (buffer.exhausted()) {
            return new Options((ByteString[]) byteStringArr.clone(), iArr);
        }
        throw new AssertionError();
    }

    @Override
    public final int size() {
        return this.byteStrings.length;
    }

    @Override
    public ByteString get(int i) {
        return this.byteStrings[i];
    }
}
