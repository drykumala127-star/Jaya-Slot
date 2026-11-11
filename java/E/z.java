package E;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public final class z extends Fragment implements e {
    private static final WeakHashMap d = new WeakHashMap();
    private final Map a = Collections.synchronizedMap(new a.a());
    private int b = 0;
    private Bundle c;

    static int d(z zVar) {
        return zVar.b;
    }

    static Bundle e(z zVar) {
        return zVar.c;
    }

    public static z f(Activity activity) {
        z zVar;
        WeakHashMap weakHashMap = d;
        WeakReference weakReference = (WeakReference) weakHashMap.get(activity);
        if (weakReference != null && (zVar = (z) weakReference.get()) != null) {
            return zVar;
        }
        try {
            z zVar2 = (z) activity.getFragmentManager().findFragmentByTag("LifecycleFragmentImpl");
            if (zVar2 == null || zVar2.isRemoving()) {
                zVar2 = new z();
                activity.getFragmentManager().beginTransaction().add(zVar2, "LifecycleFragmentImpl").commitAllowingStateLoss();
            }
            weakHashMap.put(activity, new WeakReference(zVar2));
            return zVar2;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Fragment with tag LifecycleFragmentImpl is not a LifecycleFragmentImpl", e);
        }
    }

    @Override
    public final void a(String str, LifecycleCallback lifecycleCallback) {
        if (this.a.containsKey(str)) {
            throw new IllegalArgumentException("LifecycleCallback with tag " + str + " already added to this fragment.");
        }
        this.a.put(str, lifecycleCallback);
        if (this.b > 0) {
            new R.e(Looper.getMainLooper()).post(new y(this, lifecycleCallback, str));
        }
    }

    @Override
    public final LifecycleCallback b(String str, Class cls) {
        return (LifecycleCallback) cls.cast(this.a.get(str));
    }

    @Override
    public final Activity c() {
        return getActivity();
    }

    @Override
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        Iterator it = this.a.values().iterator();
        while (it.hasNext()) {
            ((LifecycleCallback) it.next()).a(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override
    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Iterator it = this.a.values().iterator();
        while (it.hasNext()) {
            ((LifecycleCallback) it.next()).e(i, i2, intent);
        }
    }

    @Override
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.b = 1;
        this.c = bundle;
        for (Map.Entry entry : this.a.entrySet()) {
            ((LifecycleCallback) entry.getValue()).f(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        this.b = 5;
        Iterator it = this.a.values().iterator();
        while (it.hasNext()) {
            ((LifecycleCallback) it.next()).g();
        }
    }

    @Override
    public final void onResume() {
        super.onResume();
        this.b = 3;
        Iterator it = this.a.values().iterator();
        while (it.hasNext()) {
            ((LifecycleCallback) it.next()).h();
        }
    }

    @Override
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle == null) {
            return;
        }
        for (Map.Entry entry : this.a.entrySet()) {
            Bundle bundle2 = new Bundle();
            ((LifecycleCallback) entry.getValue()).i(bundle2);
            bundle.putBundle((String) entry.getKey(), bundle2);
        }
    }

    @Override
    public final void onStart() {
        super.onStart();
        this.b = 2;
        Iterator it = this.a.values().iterator();
        while (it.hasNext()) {
            ((LifecycleCallback) it.next()).j();
        }
    }

    @Override
    public final void onStop() {
        super.onStop();
        this.b = 4;
        Iterator it = this.a.values().iterator();
        while (it.hasNext()) {
            ((LifecycleCallback) it.next()).k();
        }
    }
}
