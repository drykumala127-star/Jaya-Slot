package c0;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.components.ComponentRegistrar;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class g {
    private final Object a;
    private final c b;

    private static class b implements c {
        private final Class a;

        b(Class cls, a aVar) {
            this(cls);
        }

        private Bundle b(Context context) {
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager == null) {
                    Log.w("ComponentDiscovery", "Context has no PackageManager.");
                    return null;
                }
                ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, (Class<?>) this.a), 128);
                if (serviceInfo != null) {
                    return serviceInfo.metaData;
                }
                Log.w("ComponentDiscovery", this.a + " has no service info.");
                return null;
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("ComponentDiscovery", "Application info not found.");
                return null;
            }
        }

        @Override
        public List a(Context context) {
            Bundle bundleB = b(context);
            if (bundleB == null) {
                Log.w("ComponentDiscovery", "Could not retrieve metadata, returning empty list of registrars.");
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            for (String str : bundleB.keySet()) {
                if ("com.google.firebase.components.ComponentRegistrar".equals(bundleB.get(str)) && str.startsWith("com.google.firebase.components:")) {
                    arrayList.add(str.substring(31));
                }
            }
            return arrayList;
        }

        private b(Class cls) {
            this.a = cls;
        }
    }

    interface c {
        List a(Object obj);
    }

    g(Object obj, c cVar) {
        this.a = obj;
        this.b = cVar;
    }

    public static ComponentRegistrar a(String str) {
        return e(str);
    }

    public static g c(Context context, Class cls) {
        return new g(context, new b(cls, null));
    }

    private static ComponentRegistrar d(String str) {
        try {
            Class<?> cls = Class.forName(str);
            if (ComponentRegistrar.class.isAssignableFrom(cls)) {
                return (ComponentRegistrar) cls.getDeclaredConstructor(null).newInstance(null);
            }
            throw new w(String.format("Class %s is not an instance of %s", str, "com.google.firebase.components.ComponentRegistrar"));
        } catch (ClassNotFoundException unused) {
            Log.w("ComponentDiscovery", String.format("Class %s is not an found.", str));
            return null;
        } catch (IllegalAccessException e) {
            throw new w(String.format("Could not instantiate %s.", str), e);
        } catch (InstantiationException e2) {
            throw new w(String.format("Could not instantiate %s.", str), e2);
        } catch (NoSuchMethodException e3) {
            throw new w(String.format("Could not instantiate %s", str), e3);
        } catch (InvocationTargetException e4) {
            throw new w(String.format("Could not instantiate %s", str), e4);
        }
    }

    private static ComponentRegistrar e(String str) {
        return d(str);
    }

    public List b() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.b.a(this.a).iterator();
        while (it.hasNext()) {
            arrayList.add(new f((String) it.next()));
        }
        return arrayList;
    }
}
