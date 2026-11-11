package B;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public final class j implements Handler.Callback {
    public final q a;

    public j(q qVar) {
        this.a = qVar;
    }

    @Override
    public final boolean handleMessage(Message message) {
        q qVar = this.a;
        int i = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            StringBuilder sb = new StringBuilder(41);
            sb.append("Received response to request: ");
            sb.append(i);
            Log.d("MessengerIpcClient", sb.toString());
        }
        synchronized (qVar) {
            try {
                t tVar = (t) qVar.e.get(i);
                if (tVar == null) {
                    StringBuilder sb2 = new StringBuilder(50);
                    sb2.append("Received response for unknown request: ");
                    sb2.append(i);
                    Log.w("MessengerIpcClient", sb2.toString());
                    return true;
                }
                qVar.e.remove(i);
                qVar.f();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    tVar.c(new u(4, "Not supported by GmsCore", null));
                    return true;
                }
                tVar.a(data);
                return true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
