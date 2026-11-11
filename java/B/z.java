package B;

import android.os.Bundle;
import android.util.Log;
import java.io.IOException;

public final class z implements Z.a {
    public static final z a = new z();

    private z() {
    }

    @Override
    public final Object a(Z.i iVar) throws IOException {
        if (iVar.m()) {
            return (Bundle) iVar.i();
        }
        if (Log.isLoggable("Rpc", 3)) {
            String strValueOf = String.valueOf(iVar.h());
            StringBuilder sb = new StringBuilder(strValueOf.length() + 22);
            sb.append("Error making request: ");
            sb.append(strValueOf);
            Log.d("Rpc", sb.toString());
        }
        throw new IOException("SERVICE_NOT_AVAILABLE", iVar.h());
    }
}
