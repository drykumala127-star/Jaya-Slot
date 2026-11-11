package y;

import android.net.Uri;
import java.util.Map;

final class b extends Thread {
    final Map i;

    b(a aVar, Map map) {
        this.i = map;
    }

    @Override
    public final void run() {
        Map map = this.i;
        Uri.Builder builderBuildUpon = Uri.parse("https://pagead2.googlesyndication.com/pagead/gen_204?id=gmob-apps").buildUpon();
        for (String str : map.keySet()) {
            builderBuildUpon.appendQueryParameter(str, (String) map.get(str));
        }
        d.a(builderBuildUpon.build().toString());
    }
}
