package org.cocos2dx.javascript;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageUtils {
    private static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = 1;
        if (i == 0 && i2 == 0) {
            return 1;
        }
        int i4 = options.outHeight;
        int i5 = options.outWidth;
        if (i4 > i2 || i5 > i) {
            int i6 = i4 / 2;
            int i7 = i5 / 2;
            while (i6 / i3 >= i2 && i7 / i3 >= i) {
                i3 *= 2;
            }
        }
        return i3;
    }

    public static String getBase64WithHeader(Context context, Uri uri) {
        String strUriToBase64 = uriToBase64(context, uri);
        if (strUriToBase64 == null) {
            return null;
        }
        return "data:image/" + (uri.toString().toLowerCase().endsWith(".png") ? "png" : uri.toString().toLowerCase().endsWith(".webp") ? "webp" : "jpeg") + ";base64," + strUriToBase64;
    }

    private static Bitmap.CompressFormat getImageFormat(String str) {
        return str.toLowerCase().endsWith(".png") ? Bitmap.CompressFormat.PNG : str.toLowerCase().endsWith(".webp") ? Bitmap.CompressFormat.WEBP : Bitmap.CompressFormat.JPEG;
    }

    public static String uriToBase64(Context context, Uri uri) {
        return uriToBase64(context, uri, 0, 0, 80);
    }

    public static String uriToBase64(Context context, Uri uri, int i, int i2, int i3) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStreamOpenInputStream;
        InputStream inputStream = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            inputStreamOpenInputStream = contentResolver.openInputStream(uri);
            try {
                if (inputStreamOpenInputStream == null) {
                    Toast.makeText(context, "Can't open", 0).show();
                    if (inputStreamOpenInputStream != null) {
                        try {
                            inputStreamOpenInputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                inputStreamOpenInputStream.close();
                options.inSampleSize = calculateInSampleSize(options, i, i2);
                options.inJustDecodeBounds = false;
                inputStreamOpenInputStream = contentResolver.openInputStream(uri);
                Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                if (bitmapDecodeStream == null) {
                    Toast.makeText(context, "decode fail", 0).show();
                    if (inputStreamOpenInputStream != null) {
                        try {
                            inputStreamOpenInputStream.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    return null;
                }
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    try {
                        bitmapDecodeStream.compress(getImageFormat(uri.toString()), i3, byteArrayOutputStream);
                        String strEncodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                        bitmapDecodeStream.recycle();
                        inputStreamOpenInputStream.close();
                        byteArrayOutputStream.close();
                        try {
                            inputStreamOpenInputStream.close();
                            byteArrayOutputStream.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                        return strEncodeToString;
                    } catch (Exception e4) {
                        e = e4;
                        e.printStackTrace();
                        Toast.makeText(context, "process fail: " + e.getMessage(), 0).show();
                        if (inputStreamOpenInputStream != null) {
                            try {
                                inputStreamOpenInputStream.close();
                                if (byteArrayOutputStream != null) {
                                    byteArrayOutputStream.close();
                                }
                            } catch (Exception e5) {
                                e5.printStackTrace();
                                return null;
                            }
                        } else if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    inputStream = inputStreamOpenInputStream;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                            if (byteArrayOutputStream != null) {
                                byteArrayOutputStream.close();
                            }
                        } catch (Exception e6) {
                            e6.printStackTrace();
                            throw th;
                        }
                    } else if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    throw th;
                }
            } catch (Exception e7) {
                e = e7;
                byteArrayOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
                inputStream = inputStreamOpenInputStream;
                if (inputStream != null) {
                    inputStream.close();
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } else if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e8) {
            e = e8;
            byteArrayOutputStream = null;
            inputStreamOpenInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = null;
            if (inputStream != null) {
                inputStream.close();
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } else if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
    }
}
