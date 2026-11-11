package com.google.android.datatransport.cct;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import l.j;
import l.k;
import l.l;
import l.m;
import l.n;
import l.o;
import l.p;
import m.i;
import n.f;
import n.g;
import n.m;

final class d implements m {
    private final f0.a a;
    private final ConnectivityManager b;
    private final Context c;
    final URL d;
    private final w.a e;
    private final w.a f;
    private final int g;

    static final class a {
        final URL a;
        final j b;
        final String c;

        a(URL url, j jVar, String str) {
            this.a = url;
            this.b = jVar;
            this.c = str;
        }

        a a(URL url) {
            return new a(url, this.b, this.c);
        }
    }

    static final class b {
        final int a;
        final URL b;
        final long c;

        b(int i, URL url, long j) {
            this.a = i;
            this.b = url;
            this.c = j;
        }
    }

    d(Context context, w.a aVar, w.a aVar2, int i) {
        this.a = j.b();
        this.c = context;
        this.b = (ConnectivityManager) context.getSystemService("connectivity");
        this.d = n(com.google.android.datatransport.cct.a.c);
        this.e = aVar2;
        this.f = aVar;
        this.g = i;
    }

    public static b c(d dVar, a aVar) {
        return dVar.e(aVar);
    }

    public static a d(a aVar, b bVar) {
        return l(aVar, bVar);
    }

    private b e(a aVar) throws IOException {
        q.a.f("CctTransportBackend", "Making request to: %s", aVar.a);
        HttpURLConnection httpURLConnection = (HttpURLConnection) aVar.a.openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(this.g);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("User-Agent", String.format("datatransport/%s android/", "3.1.8"));
        httpURLConnection.setRequestProperty("Content-Encoding", "gzip");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        String str = aVar.c;
        if (str != null) {
            httpURLConnection.setRequestProperty("X-Goog-Api-Key", str);
        }
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            try {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(outputStream);
                try {
                    this.a.a(aVar.b, new BufferedWriter(new OutputStreamWriter(gZIPOutputStream)));
                    gZIPOutputStream.close();
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    int responseCode = httpURLConnection.getResponseCode();
                    q.a.f("CctTransportBackend", "Status Code: %d", Integer.valueOf(responseCode));
                    q.a.b("CctTransportBackend", "Content-Type: %s", httpURLConnection.getHeaderField("Content-Type"));
                    q.a.b("CctTransportBackend", "Content-Encoding: %s", httpURLConnection.getHeaderField("Content-Encoding"));
                    if (responseCode == 302 || responseCode == 301 || responseCode == 307) {
                        return new b(responseCode, new URL(httpURLConnection.getHeaderField("Location")), 0L);
                    }
                    if (responseCode != 200) {
                        return new b(responseCode, null, 0L);
                    }
                    InputStream inputStream = httpURLConnection.getInputStream();
                    try {
                        InputStream inputStreamM = m(inputStream, httpURLConnection.getHeaderField("Content-Encoding"));
                        try {
                            b bVar = new b(responseCode, null, n.b(new BufferedReader(new InputStreamReader(inputStreamM))).c());
                            if (inputStreamM != null) {
                                inputStreamM.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            return bVar;
                        } catch (Throwable th) {
                            if (inputStreamM != null) {
                                try {
                                    inputStreamM.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                            }
                        }
                        throw th3;
                    }
                } catch (Throwable th5) {
                    try {
                        gZIPOutputStream.close();
                    } catch (Throwable th6) {
                        th5.addSuppressed(th6);
                    }
                    throw th5;
                }
            } catch (Throwable th7) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable th8) {
                        th7.addSuppressed(th8);
                    }
                }
                throw th7;
            }
        } catch (f0.b e) {
            e = e;
            q.a.d("CctTransportBackend", "Couldn't encode request, returning with 400", e);
            return new b(400, null, 0L);
        } catch (ConnectException e2) {
            e = e2;
            q.a.d("CctTransportBackend", "Couldn't open connection, returning with 500", e);
            return new b(500, null, 0L);
        } catch (UnknownHostException e3) {
            e = e3;
            q.a.d("CctTransportBackend", "Couldn't open connection, returning with 500", e);
            return new b(500, null, 0L);
        } catch (IOException e4) {
            e = e4;
            q.a.d("CctTransportBackend", "Couldn't encode request, returning with 400", e);
            return new b(400, null, 0L);
        }
    }

    private static int f(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return o.b.j.b();
        }
        int subtype = networkInfo.getSubtype();
        if (subtype == -1) {
            return o.b.D.b();
        }
        if (o.b.a(subtype) != null) {
            return subtype;
        }
        return 0;
    }

    private static int g(NetworkInfo networkInfo) {
        return networkInfo == null ? o.c.B.b() : networkInfo.getType();
    }

    private static int h(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            q.a.d("CctTransportBackend", "Unable to find version code for package", e);
            return -1;
        }
    }

    private j i(f fVar) {
        l.a aVarJ;
        HashMap map = new HashMap();
        for (m.j jVar : fVar.b()) {
            String strJ = jVar.j();
            if (map.containsKey(strJ)) {
                ((List) map.get(strJ)).add(jVar);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(jVar);
                map.put(strJ, arrayList);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry entry : map.entrySet()) {
            m.j jVar2 = (m.j) ((List) entry.getValue()).get(0);
            m.a aVarB = l.m.a().f(p.j).g(this.f.a()).h(this.e.a()).b(k.a().c(k.b.k).b(l.a.a().m(Integer.valueOf(jVar2.g("sdk-version"))).j(jVar2.b("model")).f(jVar2.b("hardware")).d(jVar2.b("device")).l(jVar2.b("product")).k(jVar2.b("os-uild")).h(jVar2.b("manufacturer")).e(jVar2.b("fingerprint")).c(jVar2.b("country")).g(jVar2.b("locale")).i(jVar2.b("mcc_mnc")).b(jVar2.b("application_build")).a()).a());
            try {
                aVarB.i(Integer.parseInt((String) entry.getKey()));
            } catch (NumberFormatException unused) {
                aVarB.j((String) entry.getKey());
            }
            ArrayList arrayList3 = new ArrayList();
            for (m.j jVar3 : (List) entry.getValue()) {
                i iVarE = jVar3.e();
                k.b bVarB = iVarE.b();
                if (bVarB.equals(k.b.b("proto"))) {
                    aVarJ = l.j(iVarE.a());
                } else if (bVarB.equals(k.b.b("json"))) {
                    aVarJ = l.i(new String(iVarE.a(), Charset.forName("UTF-8")));
                } else {
                    q.a.g("CctTransportBackend", "Received event of unsupported encoding %s. Skipping...", bVarB);
                }
                aVarJ.c(jVar3.f()).d(jVar3.k()).h(jVar3.h("tz-offset")).e(o.a().c(o.c.a(jVar3.g("net-type"))).b(o.b.a(jVar3.g("mobile-subtype"))).a());
                if (jVar3.d() != null) {
                    aVarJ.b(jVar3.d());
                }
                arrayList3.add(aVarJ.a());
            }
            aVarB.c(arrayList3);
            arrayList2.add(aVarB.a());
        }
        return j.a(arrayList2);
    }

    private static TelephonyManager j(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    static long k() {
        Calendar.getInstance();
        return TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis()) / 1000;
    }

    private static a l(a aVar, b bVar) {
        URL url = bVar.b;
        if (url == null) {
            return null;
        }
        q.a.b("CctTransportBackend", "Following redirect to: %s", url);
        return aVar.a(bVar.b);
    }

    private static InputStream m(InputStream inputStream, String str) {
        return "gzip".equals(str) ? new GZIPInputStream(inputStream) : inputStream;
    }

    private static URL n(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url: " + str, e);
        }
    }

    @Override
    public m.j a(m.j jVar) {
        NetworkInfo activeNetworkInfo = this.b.getActiveNetworkInfo();
        return jVar.l().a("sdk-version", Build.VERSION.SDK_INT).c("model", Build.MODEL).c("hardware", Build.HARDWARE).c("device", Build.DEVICE).c("product", Build.PRODUCT).c("os-uild", Build.ID).c("manufacturer", Build.MANUFACTURER).c("fingerprint", Build.FINGERPRINT).b("tz-offset", k()).a("net-type", g(activeNetworkInfo)).a("mobile-subtype", f(activeNetworkInfo)).c("country", Locale.getDefault().getCountry()).c("locale", Locale.getDefault().getLanguage()).c("mcc_mnc", j(this.c).getSimOperator()).c("application_build", Integer.toString(h(this.c))).d();
    }

    @Override
    public g b(f fVar) {
        j jVarI = i(fVar);
        URL urlN = this.d;
        if (fVar.c() != null) {
            try {
                com.google.android.datatransport.cct.a aVarE = com.google.android.datatransport.cct.a.e(fVar.c());
                strF = aVarE.f() != null ? aVarE.f() : null;
                if (aVarE.g() != null) {
                    urlN = n(aVarE.g());
                }
            } catch (IllegalArgumentException unused) {
                return g.a();
            }
        }
        try {
            b bVar = (b) r.b.a(5, new a(urlN, jVarI, strF), new com.google.android.datatransport.cct.b(this), new c());
            int i = bVar.a;
            if (i == 200) {
                return g.e(bVar.c);
            }
            if (i < 500 && i != 404) {
                return i == 400 ? g.d() : g.a();
            }
            return g.f();
        } catch (IOException e) {
            q.a.d("CctTransportBackend", "Could not make request to the backend", e);
            return g.f();
        }
    }

    d(Context context, w.a aVar, w.a aVar2) {
        this(context, aVar, aVar2, 130000);
    }
}
