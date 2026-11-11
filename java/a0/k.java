package a0;

import F.n;
import F.q;
import J.m;
import android.content.Context;
import android.text.TextUtils;

public final class k {
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;
    private final String g;

    private k(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        n.m(!m.a(str), "ApplicationId must be set.");
        this.b = str;
        this.a = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = str6;
        this.g = str7;
    }

    public static k a(Context context) {
        q qVar = new q(context);
        String strA = qVar.a("google_app_id");
        if (TextUtils.isEmpty(strA)) {
            return null;
        }
        return new k(strA, qVar.a("google_api_key"), qVar.a("firebase_database_url"), qVar.a("ga_trackingId"), qVar.a("gcm_defaultSenderId"), qVar.a("google_storage_bucket"), qVar.a("project_id"));
    }

    public String b() {
        return this.a;
    }

    public String c() {
        return this.b;
    }

    public String d() {
        return this.e;
    }

    public String e() {
        return this.g;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        return F.m.a(this.b, kVar.b) && F.m.a(this.a, kVar.a) && F.m.a(this.c, kVar.c) && F.m.a(this.d, kVar.d) && F.m.a(this.e, kVar.e) && F.m.a(this.f, kVar.f) && F.m.a(this.g, kVar.g);
    }

    public int hashCode() {
        return F.m.b(this.b, this.a, this.c, this.d, this.e, this.f, this.g);
    }

    public String toString() {
        return F.m.c(this).a("applicationId", this.b).a("apiKey", this.a).a("databaseUrl", this.c).a("gcmSenderId", this.e).a("storageBucket", this.f).a("projectId", this.g).toString();
    }
}
