package org.cocos2dx.okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.cocos2dx.okhttp3.internal.Util;

final class b extends Platform {
    final Method a;
    final Method b;

    b(Method method, Method method2) {
        this.a = method;
        this.b = method2;
    }

    public static b a() {
        try {
            return new b(SSLParameters.class.getMethod("setApplicationProtocols", String[].class), SSLSocket.class.getMethod("getApplicationProtocol", null));
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    @Override
    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List list) {
        try {
            SSLParameters sSLParameters = sSLSocket.getSSLParameters();
            List listAlpnProtocolNames = Platform.alpnProtocolNames(list);
            this.a.invoke(sSLParameters, listAlpnProtocolNames.toArray(new String[listAlpnProtocolNames.size()]));
            sSLSocket.setSSLParameters(sSLParameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw Util.assertionError("unable to set ssl parameters", e);
        }
    }

    @Override
    public String getSelectedProtocol(SSLSocket sSLSocket) {
        try {
            String str = (String) this.b.invoke(sSLSocket, null);
            if (str == null || str.equals("")) {
                return null;
            }
            return str;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw Util.assertionError("unable to get selected protocols", e);
        }
    }

    @Override
    public X509TrustManager trustManager(SSLSocketFactory sSLSocketFactory) {
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
    }
}
