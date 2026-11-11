package c;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import androidx.core.graphics.drawable.IconCompat;
import java.util.ArrayList;

public abstract class i {

    public static class a {
        final Bundle a;
        private IconCompat b;
        private final D[] c;
        private final D[] d;
        private boolean e;
        boolean f;
        private final int g;
        private final boolean h;
        public int i;
        public CharSequence j;
        public PendingIntent k;

        public a(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            this(i != 0 ? IconCompat.b((Resources) null, "", i) : null, charSequence, pendingIntent);
        }

        public PendingIntent a() {
            return this.k;
        }

        public boolean b() {
            return this.e;
        }

        public Bundle c() {
            return this.a;
        }

        public int d() {
            return this.i;
        }

        public IconCompat e() {
            int i;
            if (this.b == null && (i = this.i) != 0) {
                this.b = IconCompat.b((Resources) null, "", i);
            }
            return this.b;
        }

        public D[] f() {
            return this.c;
        }

        public int g() {
            return this.g;
        }

        public boolean h() {
            return this.f;
        }

        public CharSequence i() {
            return this.j;
        }

        public boolean j() {
            return this.h;
        }

        public a(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent) {
            this(iconCompat, charSequence, pendingIntent, new Bundle(), null, null, true, 0, true, false);
        }

        a(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, D[] dArr, D[] dArr2, boolean z, int i, boolean z2, boolean z3) {
            this.f = true;
            this.b = iconCompat;
            if (iconCompat != null && iconCompat.g() == 2) {
                this.i = iconCompat.c();
            }
            this.j = e.d(charSequence);
            this.k = pendingIntent;
            this.a = bundle == null ? new Bundle() : bundle;
            this.c = dArr;
            this.d = dArr2;
            this.e = z;
            this.g = i;
            this.f = z2;
            this.h = z3;
        }
    }

    public static class b extends f {
        private Bitmap e;
        private Bitmap f;
        private boolean g;

        @Override
        public void b(h hVar) {
            Notification.BigPictureStyle bigPictureStyleBigPicture = new Notification.BigPictureStyle(hVar.a()).setBigContentTitle(this.b).bigPicture(this.e);
            if (this.g) {
                bigPictureStyleBigPicture.bigLargeIcon(this.f);
            }
            if (this.d) {
                bigPictureStyleBigPicture.setSummaryText(this.c);
            }
        }

        public b g(Bitmap bitmap) {
            this.f = bitmap;
            this.g = true;
            return this;
        }

        public b h(Bitmap bitmap) {
            this.e = bitmap;
            return this;
        }
    }

    public static class c extends f {
        private CharSequence e;

        @Override
        public void b(h hVar) {
            Notification.BigTextStyle bigTextStyleBigText = new Notification.BigTextStyle(hVar.a()).setBigContentTitle(this.b).bigText(this.e);
            if (this.d) {
                bigTextStyleBigText.setSummaryText(this.c);
            }
        }

        public c g(CharSequence charSequence) {
            this.e = e.d(charSequence);
            return this;
        }
    }

    public static final class d {
        public static Notification.BubbleMetadata a(d dVar) {
            return null;
        }
    }

    public static Bundle a(Notification notification) {
        return notification.extras;
    }

    public static class e {
        String A;
        Bundle B;
        int C;
        int D;
        Notification E;
        RemoteViews F;
        RemoteViews G;
        RemoteViews H;
        String I;
        int J;
        String K;
        long L;
        int M;
        boolean N;
        Notification O;
        public ArrayList P;
        public Context a;
        public ArrayList b;
        ArrayList c;
        CharSequence d;
        CharSequence e;
        PendingIntent f;
        PendingIntent g;
        RemoteViews h;
        Bitmap i;
        CharSequence j;
        int k;
        int l;
        boolean m;
        boolean n;
        f o;
        CharSequence p;
        CharSequence[] q;
        int r;
        int s;
        boolean t;
        String u;
        boolean v;
        String w;
        boolean x;
        boolean y;
        boolean z;

        public e(Context context, String str) {
            this.b = new ArrayList();
            this.c = new ArrayList();
            this.m = true;
            this.x = false;
            this.C = 0;
            this.D = 0;
            this.J = 0;
            this.M = 0;
            Notification notification = new Notification();
            this.O = notification;
            this.a = context;
            this.I = str;
            notification.when = System.currentTimeMillis();
            this.O.audioStreamType = -1;
            this.l = 0;
            this.P = new ArrayList();
            this.N = true;
        }

        protected static CharSequence d(CharSequence charSequence) {
            return (charSequence != null && charSequence.length() > 5120) ? charSequence.subSequence(0, 5120) : charSequence;
        }

        private Bitmap e(Bitmap bitmap) {
            if (bitmap == null || Build.VERSION.SDK_INT >= 27) {
                return bitmap;
            }
            Resources resources = this.a.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(b.a.b);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(b.a.a);
            if (bitmap.getWidth() <= dimensionPixelSize && bitmap.getHeight() <= dimensionPixelSize2) {
                return bitmap;
            }
            double dMin = Math.min(((double) dimensionPixelSize) / ((double) Math.max(1, bitmap.getWidth())), ((double) dimensionPixelSize2) / ((double) Math.max(1, bitmap.getHeight())));
            return Bitmap.createScaledBitmap(bitmap, (int) Math.ceil(((double) bitmap.getWidth()) * dMin), (int) Math.ceil(((double) bitmap.getHeight()) * dMin), true);
        }

        private void n(int i, boolean z) {
            if (z) {
                Notification notification = this.O;
                notification.flags = i | notification.flags;
            } else {
                Notification notification2 = this.O;
                notification2.flags = (~i) & notification2.flags;
            }
        }

        public e A(long j) {
            this.O.when = j;
            return this;
        }

        public e a(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            this.b.add(new a(i, charSequence, pendingIntent));
            return this;
        }

        public Notification b() {
            return new B(this).c();
        }

        public Bundle c() {
            if (this.B == null) {
                this.B = new Bundle();
            }
            return this.B;
        }

        public e f(boolean z) {
            n(16, z);
            return this;
        }

        public e g(String str) {
            this.I = str;
            return this;
        }

        public e h(int i) {
            this.C = i;
            return this;
        }

        public e i(PendingIntent pendingIntent) {
            this.f = pendingIntent;
            return this;
        }

        public e j(CharSequence charSequence) {
            this.e = d(charSequence);
            return this;
        }

        public e k(CharSequence charSequence) {
            this.d = d(charSequence);
            return this;
        }

        public e l(int i) {
            Notification notification = this.O;
            notification.defaults = i;
            if ((i & 4) != 0) {
                notification.flags |= 1;
            }
            return this;
        }

        public e m(PendingIntent pendingIntent) {
            this.O.deleteIntent = pendingIntent;
            return this;
        }

        public e o(Bitmap bitmap) {
            this.i = e(bitmap);
            return this;
        }

        public e p(int i, int i2, int i3) {
            Notification notification = this.O;
            notification.ledARGB = i;
            notification.ledOnMS = i2;
            notification.ledOffMS = i3;
            notification.flags = ((i2 == 0 || i3 == 0) ? 0 : 1) | (notification.flags & (-2));
            return this;
        }

        public e q(boolean z) {
            this.x = z;
            return this;
        }

        public e r(int i) {
            this.k = i;
            return this;
        }

        public e s(int i) {
            this.l = i;
            return this;
        }

        public e t(boolean z) {
            this.m = z;
            return this;
        }

        public e u(int i) {
            this.O.icon = i;
            return this;
        }

        public e v(Uri uri) {
            Notification notification = this.O;
            notification.sound = uri;
            notification.audioStreamType = -1;
            notification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
            return this;
        }

        public e w(f fVar) {
            if (this.o != fVar) {
                this.o = fVar;
                if (fVar != null) {
                    fVar.f(this);
                }
            }
            return this;
        }

        public e x(CharSequence charSequence) {
            this.O.tickerText = d(charSequence);
            return this;
        }

        public e y(long[] jArr) {
            this.O.vibrate = jArr;
            return this;
        }

        public e z(int i) {
            this.D = i;
            return this;
        }

        public e(Context context) {
            this(context, null);
        }
    }

    public static abstract class f {
        protected e a;
        CharSequence b;
        CharSequence c;
        boolean d = false;

        public abstract void b(h hVar);

        public RemoteViews c(h hVar) {
            return null;
        }

        public RemoteViews d(h hVar) {
            return null;
        }

        public RemoteViews e(h hVar) {
            return null;
        }

        public void f(e eVar) {
            if (this.a != eVar) {
                this.a = eVar;
                if (eVar != null) {
                    eVar.w(this);
                }
            }
        }

        public void a(Bundle bundle) {
        }
    }
}
