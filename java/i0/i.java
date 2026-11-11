package i0;

class i implements f0.g {
    private boolean a = false;
    private boolean b = false;
    private f0.c c;
    private final f d;

    i(f fVar) {
        this.d = fVar;
    }

    private void a() {
        if (this.a) {
            throw new f0.b("Cannot encode a second value in the ValueEncoderContext");
        }
        this.a = true;
    }

    @Override
    public f0.g b(String str) {
        a();
        this.d.g(this.c, str, this.b);
        return this;
    }

    @Override
    public f0.g c(boolean z) {
        a();
        this.d.l(this.c, z, this.b);
        return this;
    }

    void d(f0.c cVar, boolean z) {
        this.a = false;
        this.c = cVar;
        this.b = z;
    }
}
