package J;

import android.os.Process;
import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class l {
    private static String a;
    private static int b;

    public static String a() throws Throwable {
        BufferedReader bufferedReader;
        if (a == null) {
            int iMyPid = b;
            if (iMyPid == 0) {
                iMyPid = Process.myPid();
                b = iMyPid;
            }
            String strTrim = null;
            strTrim = null;
            strTrim = null;
            BufferedReader bufferedReader2 = null;
            if (iMyPid > 0) {
                try {
                    String str = "/proc/" + iMyPid + "/cmdline";
                    StrictMode.ThreadPolicy threadPolicyAllowThreadDiskReads = StrictMode.allowThreadDiskReads();
                    try {
                        bufferedReader = new BufferedReader(new FileReader(str));
                        StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskReads);
                    } catch (Throwable th) {
                        StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskReads);
                        throw th;
                    }
                } catch (IOException unused) {
                    bufferedReader = null;
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    String line = bufferedReader.readLine();
                    F.n.i(line);
                    strTrim = line.trim();
                } catch (IOException unused2) {
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader2 = bufferedReader;
                    j.a(bufferedReader2);
                    throw th;
                }
                j.a(bufferedReader);
            }
            a = strTrim;
        }
        return a;
    }
}
