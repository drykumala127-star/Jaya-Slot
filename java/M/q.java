package m;

import java.util.Set;

final class q implements k.g {
    private final Set a;
    private final p b;
    private final t c;

    q(Set set, p pVar, t tVar) {
        this.a = set;
        this.b = pVar;
        this.c = tVar;
    }

    @Override
    public k.f a(String str, Class cls, k.b bVar, k.e eVar) {
        if (this.a.contains(bVar)) {
            return new s(this.b, str, bVar, eVar, this.c);
        }
        throw new IllegalArgumentException(String.format("%s is not supported byt this factory. Supported encodings are: %s.", bVar, this.a));
    }
}
