package B;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.TimeUnit;

public final class l implements Runnable {
    public final q i;

    public l(q qVar) {
        this.i = qVar;
    }

    @Override
    public final void run() {
        t tVar;
        q qVar = this.i;
        while (true) {
            synchronized (qVar) {
                try {
                    if (qVar.a != 2) {
                        return;
                    }
                    if (qVar.d.isEmpty()) {
                        qVar.f();
                        return;
                    } else {
                        tVar = (t) qVar.d.poll();
                        qVar.e.put(tVar.a, tVar);
                        w.e(qVar.f).schedule(new o(qVar, tVar), 30L, TimeUnit.SECONDS);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                String strValueOf = String.valueOf(tVar);
                StringBuilder sb = new StringBuilder(strValueOf.length() + 8);
                sb.append("Sending ");
                sb.append(strValueOf);
                Log.d("MessengerIpcClient", sb.toString());
            }
            Context contextA = w.a(qVar.f);
            Messenger messenger = qVar.b;
            Message messageObtain = Message.obtain();
            messageObtain.what = tVar.c;
            messageObtain.arg1 = tVar.a;
            messageObtain.replyTo = messenger;
            Bundle bundle = new Bundle();
            bundle.putBoolean("oneWay", tVar.b());
            bundle.putString("pkg", contextA.getPackageName());
            bundle.putBundle("data", tVar.d);
            messageObtain.setData(bundle);
            try {
                qVar.c.a(messageObtain);
            } catch (RemoteException e) {
                qVar.a(2, e.getMessage());
            }
        }
    }
}
