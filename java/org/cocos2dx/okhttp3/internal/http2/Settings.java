package org.cocos2dx.okhttp3.internal.http2;

import java.util.Arrays;

public final class Settings {
    static final int COUNT = 10;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
    static final int ENABLE_PUSH = 2;
    static final int HEADER_TABLE_SIZE = 1;
    static final int INITIAL_WINDOW_SIZE = 7;
    static final int MAX_CONCURRENT_STREAMS = 4;
    static final int MAX_FRAME_SIZE = 5;
    static final int MAX_HEADER_LIST_SIZE = 6;
    private int set;
    private final int[] values = new int[COUNT];

    void clear() {
        this.set = 0;
        Arrays.fill(this.values, 0);
    }

    int get(int i) {
        return this.values[i];
    }

    boolean getEnablePush(boolean z) {
        return ((this.set & MAX_CONCURRENT_STREAMS) != 0 ? this.values[ENABLE_PUSH] : z ? HEADER_TABLE_SIZE : 0) == HEADER_TABLE_SIZE;
    }

    int getHeaderTableSize() {
        if ((this.set & ENABLE_PUSH) != 0) {
            return this.values[HEADER_TABLE_SIZE];
        }
        return -1;
    }

    int getInitialWindowSize() {
        return (this.set & 128) != 0 ? this.values[INITIAL_WINDOW_SIZE] : DEFAULT_INITIAL_WINDOW_SIZE;
    }

    int getMaxConcurrentStreams(int i) {
        return (this.set & 16) != 0 ? this.values[MAX_CONCURRENT_STREAMS] : i;
    }

    int getMaxFrameSize(int i) {
        return (this.set & 32) != 0 ? this.values[MAX_FRAME_SIZE] : i;
    }

    int getMaxHeaderListSize(int i) {
        return (this.set & 64) != 0 ? this.values[MAX_HEADER_LIST_SIZE] : i;
    }

    boolean isSet(int i) {
        return ((HEADER_TABLE_SIZE << i) & this.set) != 0;
    }

    void merge(Settings settings) {
        for (int i = 0; i < COUNT; i += HEADER_TABLE_SIZE) {
            if (settings.isSet(i)) {
                set(i, settings.get(i));
            }
        }
    }

    Settings set(int i, int i2) {
        if (i >= 0) {
            int[] iArr = this.values;
            if (i < iArr.length) {
                this.set = (HEADER_TABLE_SIZE << i) | this.set;
                iArr[i] = i2;
            }
        }
        return this;
    }

    int size() {
        return Integer.bitCount(this.set);
    }
}
