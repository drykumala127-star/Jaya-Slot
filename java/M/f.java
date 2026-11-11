package m;

import android.content.Context;
import m.v;
import u.N;
import u.V;

final class f extends v {
    private y0.a i;
    private y0.a j;
    private y0.a k;
    private y0.a l;
    private y0.a m;
    private y0.a n;
    private y0.a o;
    private y0.a p;
    private y0.a q;
    private y0.a r;
    private y0.a s;
    private y0.a t;
    private y0.a u;

    private static final class b implements v.a {
        private Context a;

        private b() {
        }

        @Override
        public v a() {
            o.d.a(this.a, Context.class);
            return new f(this.a, null);
        }

        @Override
        public b b(Context context) {
            this.a = (Context) o.d.b(context);
            return this;
        }

        b(a aVar) {
            this();
        }
    }

    f(Context context, a aVar) {
        this(context);
    }

    public static v.a f() {
        return new b(null);
    }

    private void h(Context context) {
        this.i = o.a.a(l.a());
        o.b bVarA = o.c.a(context);
        this.j = bVarA;
        n.j jVarA = n.j.a(bVarA, w.c.a(), w.d.a());
        this.k = jVarA;
        this.l = o.a.a(n.l.a(this.j, jVarA));
        this.m = V.a(this.j, u.g.a(), u.i.a());
        this.n = o.a.a(u.h.a(this.j));
        this.o = o.a.a(N.a(w.c.a(), w.d.a(), u.j.a(), this.m, this.n));
        s.g gVarB = s.g.b(w.c.a());
        this.p = gVarB;
        s.i iVarA = s.i.a(this.j, this.o, gVarB, w.d.a());
        this.q = iVarA;
        y0.a aVar = this.i;
        y0.a aVar2 = this.l;
        y0.a aVar3 = this.o;
        this.r = s.d.a(aVar, aVar2, iVarA, aVar3, aVar3);
        y0.a aVar4 = this.j;
        y0.a aVar5 = this.l;
        y0.a aVar6 = this.o;
        this.s = t.s.a(aVar4, aVar5, aVar6, this.q, this.i, aVar6, w.c.a(), w.d.a(), this.o);
        y0.a aVar7 = this.i;
        y0.a aVar8 = this.o;
        this.t = t.w.a(aVar7, aVar8, this.q, aVar8);
        this.u = o.a.a(w.a(w.c.a(), w.d.a(), this.r, this.s, this.t));
    }

    @Override
    u.d a() {
        return (u.d) this.o.get();
    }

    @Override
    u e() {
        return (u) this.u.get();
    }

    private f(Context context) {
        h(context);
    }
}
