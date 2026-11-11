package B;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

final class r {
    private final Messenger a;
    private final h b;

    r(IBinder iBinder) throws RemoteException {
        String interfaceDescriptor = iBinder.getInterfaceDescriptor();
        if ("android.os.IMessenger".equals(interfaceDescriptor)) {
            this.a = new Messenger(iBinder);
            this.b = null;
        } else if ("com.google.android.gms.iid.IMessengerCompat".equals(interfaceDescriptor)) {
            this.b = new h(iBinder);
            this.a = null;
        } else {
            String strValueOf = String.valueOf(interfaceDescriptor);
            Log.w("MessengerIpcClient", strValueOf.length() != 0 ? "Invalid interface descriptor: ".concat(strValueOf) : new String("Invalid interface descriptor: "));
            throw new RemoteException();
        }
    }

    final void a(Message message) {
        Messenger messenger = this.a;
        if (messenger != null) {
            messenger.send(message);
            return;
        }
        h hVar = this.b;
        if (hVar == null) {
            throw new IllegalStateException("Both messengers are null");
        }
        hVar.b(message);
    }
}
