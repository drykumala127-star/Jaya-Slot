package org.cocos2dx.lib;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

public class Cocos2dxHandler extends Handler {
    public static final int HANDLER_SHOW_DIALOG = 1;
    private WeakReference mActivity;

    public static class DialogMessage {
        public String message;
        public String title;

        public DialogMessage(String str, String str2) {
            this.title = str;
            this.message = str2;
        }
    }

    class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    }

    public Cocos2dxHandler(Cocos2dxActivity cocos2dxActivity) {
        this.mActivity = new WeakReference(cocos2dxActivity);
    }

    private void showDialog(Message message) {
        Cocos2dxActivity cocos2dxActivity = (Cocos2dxActivity) this.mActivity.get();
        DialogMessage dialogMessage = (DialogMessage) message.obj;
        new AlertDialog.Builder(cocos2dxActivity).setTitle(dialogMessage.title).setMessage(dialogMessage.message).setPositiveButton("Ok", new a()).create().show();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what != 1) {
            return;
        }
        showDialog(message);
    }
}
