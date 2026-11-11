package org.cocos2dx.okio;

final class f {
    final byte[] a;
    int b;
    int c;
    boolean d;
    boolean e;
    f f;
    f g;

    f() {
        this.a = new byte[8192];
        this.e = true;
        this.d = false;
    }

    public final void a() {
        f fVar = this.g;
        if (fVar == this) {
            throw new IllegalStateException();
        }
        if (fVar.e) {
            int i = this.c - this.b;
            if (i > (8192 - fVar.c) + (fVar.d ? 0 : fVar.b)) {
                return;
            }
            g(fVar, i);
            b();
            g.a(this);
        }
    }

    public final f b() {
        f fVar = this.f;
        f fVar2 = fVar != this ? fVar : null;
        f fVar3 = this.g;
        fVar3.f = fVar;
        this.f.g = fVar3;
        this.f = null;
        this.g = null;
        return fVar2;
    }

    public final f c(f fVar) {
        fVar.g = this;
        fVar.f = this.f;
        this.f.g = fVar;
        this.f = fVar;
        return fVar;
    }

    final f d() {
        this.d = true;
        return new f(this.a, this.b, this.c, true, false);
    }

    public final f e(int i) {
        f fVarB;
        if (i <= 0 || i > this.c - this.b) {
            throw new IllegalArgumentException();
        }
        if (i >= 1024) {
            fVarB = d();
        } else {
            fVarB = g.b();
            System.arraycopy(this.a, this.b, fVarB.a, 0, i);
        }
        fVarB.c = fVarB.b + i;
        this.b += i;
        this.g.c(fVarB);
        return fVarB;
    }

    final f f() {
        return new f((byte[]) this.a.clone(), this.b, this.c, false, true);
    }

    public final void g(f fVar, int i) {
        if (!fVar.e) {
            throw new IllegalArgumentException();
        }
        int i2 = fVar.c;
        if (i2 + i > 8192) {
            if (fVar.d) {
                throw new IllegalArgumentException();
            }
            int i3 = fVar.b;
            if ((i2 + i) - i3 > 8192) {
                throw new IllegalArgumentException();
            }
            byte[] bArr = fVar.a;
            System.arraycopy(bArr, i3, bArr, 0, i2 - i3);
            fVar.c -= fVar.b;
            fVar.b = 0;
        }
        System.arraycopy(this.a, this.b, fVar.a, fVar.c, i);
        fVar.c += i;
        this.b += i;
    }

    f(byte[] bArr, int i, int i2, boolean z, boolean z2) {
        this.a = bArr;
        this.b = i;
        this.c = i2;
        this.d = z;
        this.e = z2;
    }
}
