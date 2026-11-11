package B;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;

public final class h implements Parcelable {
    public static final Parcelable.Creator CREATOR = new f();
    Messenger i;

    public h(IBinder iBinder) {
        this.i = new Messenger(iBinder);
    }

    public final IBinder a() {
        Messenger messenger = this.i;
        messenger.getClass();
        return messenger.getBinder();
    }

    public final void b(Message message) {
        Messenger messenger = this.i;
        messenger.getClass();
        messenger.send(message);
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return a().equals(((h) obj).a());
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public final int hashCode() {
        return a().hashCode();
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        Messenger messenger = this.i;
        messenger.getClass();
        parcel.writeStrongBinder(messenger.getBinder());
    }
}
