package org.cocos2dx.okio;

abstract class g {
    static f a;
    static long b;

    static void a(f fVar) {
        if (fVar.f != null || fVar.g != null) {
            throw new IllegalArgumentException();
        }
        if (fVar.d) {
            return;
        }
        synchronized (g.class) {
            try {
                long j = b;
                if (j + 8192 > 65536) {
                    return;
                }
                b = j + 8192;
                fVar.f = a;
                fVar.c = 0;
                fVar.b = 0;
                a = fVar;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    static f b() {
        synchronized (g.class) {
            try {
                f fVar = a;
                if (fVar == null) {
                    return new f();
                }
                a = fVar.f;
                fVar.f = null;
                b -= 8192;
                return fVar;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
