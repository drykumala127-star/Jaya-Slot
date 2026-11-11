package org.cocos2dx.javascript;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjUtil {
    public static AppActivity context = (AppActivity) Cocos2dxActivity.getContext();

    class a implements Runnable {
        final String i;
        final String j;

        a(String str, String str2) {
            this.i = str;
            this.j = str2;
        }

        @Override
        public void run() {
            Cocos2dxJavascriptJavaBridge.evalString(this.i + "(" + this.j + ")");
        }
    }

    class b implements Runnable {
        final String i;

        b(String str) {
            this.i = str;
        }

        @Override
        public void run() {
            Toast.makeText(ProjUtil.context, this.i, 1).show();
        }
    }

    public static String bitmapToBase64(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(compressFormat, i, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return null;
            } catch (Exception e2) {
                return null;
            }
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        }
    }

    public static void callJS(String str, JSONObject jSONObject, String str2) {
        if (str == null || jSONObject == null || str2 == null) {
            return;
        }
        try {
            jSONObject.put("cbName", str);
            context.runOnGLThread(new a(str2, jSONObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean checksignture(Context context2) {
        boolean z = false;
        try {
            boolean zEquals = false;
            for (Signature signature : context2.getPackageManager().getPackageInfo(context2.getPackageName(), 64).signatures) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                    messageDigest.update(signature.toByteArray());
                    zEquals = "3jMaDJlNnNJSjitwqDprP53dyxc=".equals(Base64.encodeToString(messageDigest.digest(), 2));
                } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException unused) {
                    z = zEquals;
                    return z;
                }
            }
            return zEquals;
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException unused2) {
        }
    }

    public static String getAnroidId() {
        return Settings.System.getString(context.getContentResolver(), "android_id");
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getGSFID() {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), null, null, new String[]{"android_id"}, null);
        if (cursorQuery.moveToFirst() && cursorQuery.getColumnCount() >= 2) {
            try {
                return Long.toHexString(Long.parseLong(cursorQuery.getString(1)));
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    public static byte[] getHtmlByteArray(String str) {
        return inputStreamToByte(getHtmlStream(str));
    }

    public static InputStream getHtmlStream(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                return httpURLConnection.getInputStream();
            }
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String getPackagePath(Context context2) {
        if (context2 != null) {
            return context2.getPackageCodePath();
        }
        return null;
    }

    private static String getRealPathFromURI(Uri uri, Context context2) {
        Cursor cursorQuery = context2.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        int columnIndexOrThrow = cursorQuery.getColumnIndexOrThrow("_data");
        cursorQuery.moveToFirst();
        String string = cursorQuery.getString(columnIndexOrThrow);
        cursorQuery.close();
        return string;
    }

    public static String getSignture() {
        String strEncodeToString = "";
        try {
            for (Signature signature : context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                strEncodeToString = Base64.encodeToString(messageDigest.digest(), 2);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException unused) {
        }
        return strEncodeToString;
    }

    public static String getSystempVision() {
        return Build.VERSION.RELEASE;
    }

    public static String imageToBase64(String str) throws Throwable {
        FileInputStream fileInputStream;
        ?? IsEmpty = TextUtils.isEmpty(str);
        String strEncodeToString = null;
        strEncodeToString = null;
        strEncodeToString = null;
        ?? r1 = 0;
        try {
            try {
            } catch (Throwable th) {
                th = th;
                r1 = IsEmpty;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (IsEmpty != 0) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(str);
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                strEncodeToString = Base64.encodeToString(bArr, 16);
                fileInputStream.close();
                IsEmpty = fileInputStream;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                IsEmpty = fileInputStream;
                if (fileInputStream != null) {
                    fileInputStream.close();
                    IsEmpty = fileInputStream;
                }
            }
        } catch (Exception e3) {
            e = e3;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
        }
        return strEncodeToString;
        if (r1 != 0) {
            try {
                r1.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        throw th;
    }

    public static byte[] inputStreamToByte(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int i = inputStream.read();
                if (i == -1) {
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    return byteArray;
                }
                byteArrayOutputStream.write(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAppInstall(Context context2, String str) {
        PackageInfo packageInfo;
        try {
            packageInfo = context2.getPackageManager().getPackageInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static boolean isCloner(Context context2) {
        try {
            String str = context2.getPackageManager().getPackageInfo(context2.getPackageName(), 0).applicationInfo.packageName;
            String absolutePath = context2.getFilesDir().getAbsolutePath();
            String str2 = "/data/data/" + str + "/files";
            String str3 = "/data/user/0/" + str + "/files";
            if (checksignture(context2)) {
                return (str2.equals(absolutePath) || str3.equals(absolutePath)) ? false : true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject map2json(Map map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry entry : map.entrySet()) {
            jSONObject.put((String) entry.getKey(), (String) entry.getValue());
        }
        return jSONObject;
    }

    public static void phoneShock(String str) {
        ((Vibrator) context.getSystemService("vibrator")).vibrate(Integer.parseInt(str));
    }

    public static String readApk(File file) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bArr = new byte[2];
            long length = randomAccessFile.length() - ((long) 2);
            randomAccessFile.seek(length);
            randomAccessFile.readFully(bArr);
            int iStream2Short = stream2Short(bArr, 0);
            byte[] bArr2 = new byte[iStream2Short];
            randomAccessFile.seek(length - ((long) iStream2Short));
            randomAccessFile.readFully(bArr2);
            return new String(bArr2, "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static File saveBitmap(Context context2, Bitmap bitmap, String str, String str2, String str3) {
        File file = new File(Build.VERSION.SDK_INT >= 29 ? context2.getExternalCacheDir() : Environment.getExternalStorageDirectory(), str3);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file, str);
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return file2;
    }

    public static boolean saveImageToGallery(Context context2, Bitmap bitmap, String str) {
        try {
            savePhotoToMedia(context2, saveBitmap(context2, bitmap, str, Environment.DIRECTORY_PICTURES, "myQrcode"), str);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void savePhotoToMedia(Context context2, File file, String str) {
        updatePhotoMedia(new File(getRealPathFromURI(Uri.parse(MediaStore.Images.Media.insertImage(context2.getContentResolver(), file.getAbsolutePath(), str, (String) null)), context2)), context2);
    }

    public static int saveToAlumb(String str) {
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str);
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(".jpg");
        return saveImageToGallery(context, bitmapDecodeFile, sb.toString()) ? 1 : 0;
    }

    private static byte[] short2Stream(short s) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(2);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        byteBufferAllocate.putShort(s);
        byteBufferAllocate.flip();
        return byteBufferAllocate.array();
    }

    public static void showToast(String str) {
        context.runOnUiThread(new b(str));
    }

    private static short stream2Short(byte[] bArr, int i) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(2);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        byteBufferAllocate.put(bArr[i]);
        byteBufferAllocate.put(bArr[i + 1]);
        return byteBufferAllocate.getShort(0);
    }

    private static void updatePhotoMedia(File file, Context context2) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        context2.sendBroadcast(intent);
    }

    public static void callJS(String str, JSONObject jSONObject) {
        callJS(str, jSONObject, "cc.vv.PlatformApiMgr.trigerCallback");
    }

    public static void callJS(String str) {
        callJS(str, new JSONObject());
    }
}
