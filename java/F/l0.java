package F;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public abstract class l0 {
    private static final Uri a;
    private static final Uri b;

    static {
        Uri uri = Uri.parse("https://plus.google.com/");
        a = uri;
        b = uri.buildUpon().appendPath("circles").appendPath("find").build();
    }

    public static Intent a() {
        Intent intent = new Intent("com.google.android.clockwork.home.UPDATE_ANDROID_WEAR_ACTION");
        intent.setPackage("com.google.android.wearable.app");
        return intent;
    }

    public static Intent b(String str, String str2) {
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri.Builder builderAppendQueryParameter = Uri.parse("market://details").buildUpon().appendQueryParameter("id", "com.google.android.gms");
        if (!TextUtils.isEmpty(str2)) {
            builderAppendQueryParameter.appendQueryParameter("pcampaignid", str2);
        }
        intent.setData(builderAppendQueryParameter.build());
        intent.setPackage("com.android.vending");
        intent.addFlags(524288);
        return intent;
    }

    public static Intent c(String str) {
        Uri uriFromParts = Uri.fromParts("package", "com.google.android.gms", null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(uriFromParts);
        return intent;
    }
}
