package c0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

abstract class q {

    private static class b {
        private final d a;
        private final Set b = new HashSet();
        private final Set c = new HashSet();

        b(d dVar) {
            this.a = dVar;
        }

        void a(b bVar) {
            this.b.add(bVar);
        }

        void b(b bVar) {
            this.c.add(bVar);
        }

        d c() {
            return this.a;
        }

        Set d() {
            return this.b;
        }

        boolean e() {
            return this.b.isEmpty();
        }

        boolean f() {
            return this.c.isEmpty();
        }

        void g(b bVar) {
            this.c.remove(bVar);
        }
    }

    private static class c {
        private final Class a;
        private final boolean b;

        c(Class cls, boolean z, a aVar) {
            this(cls, z);
        }

        static boolean a(c cVar) {
            return cVar.b;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return cVar.a.equals(this.a) && cVar.b == this.b;
        }

        public int hashCode() {
            return ((this.a.hashCode() ^ 1000003) * 1000003) ^ Boolean.valueOf(this.b).hashCode();
        }

        private c(Class cls, boolean z) {
            this.a = cls;
            this.b = z;
        }
    }

    static void a(List list) {
        Set<b> setC = c(list);
        Set setB = b(setC);
        int i = 0;
        while (!setB.isEmpty()) {
            b bVar = (b) setB.iterator().next();
            setB.remove(bVar);
            i++;
            for (b bVar2 : bVar.d()) {
                bVar2.g(bVar);
                if (bVar2.f()) {
                    setB.add(bVar2);
                }
            }
        }
        if (i == list.size()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (b bVar3 : setC) {
            if (!bVar3.f() && !bVar3.e()) {
                arrayList.add(bVar3.c());
            }
        }
        throw new s(arrayList);
    }

    private static Set b(Set set) {
        HashSet hashSet = new HashSet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            b bVar = (b) it.next();
            if (bVar.f()) {
                hashSet.add(bVar);
            }
        }
        return hashSet;
    }

    private static Set c(List list) {
        Set<b> set;
        HashMap map = new HashMap(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            d dVar = (d) it.next();
            b bVar = new b(dVar);
            for (Class cls : dVar.h()) {
                c cVar = new c(cls, !dVar.n(), null);
                if (!map.containsKey(cVar)) {
                    map.put(cVar, new HashSet());
                }
                Set set2 = (Set) map.get(cVar);
                if (!set2.isEmpty() && !c.a(cVar)) {
                    throw new IllegalArgumentException(String.format("Multiple components provide %s.", cls));
                }
                set2.add(bVar);
            }
        }
        Iterator it2 = map.values().iterator();
        while (it2.hasNext()) {
            for (b bVar2 : (Set) it2.next()) {
                for (r rVar : bVar2.c().e()) {
                    if (rVar.d() && (set = (Set) map.get(new c(rVar.b(), rVar.f(), null))) != null) {
                        for (b bVar3 : set) {
                            bVar2.a(bVar3);
                            bVar3.b(bVar2);
                        }
                    }
                }
            }
        }
        HashSet hashSet = new HashSet();
        Iterator it3 = map.values().iterator();
        while (it3.hasNext()) {
            hashSet.addAll((Set) it3.next());
        }
        return hashSet;
    }
}
