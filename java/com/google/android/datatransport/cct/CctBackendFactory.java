package com.google.android.datatransport.cct;

import androidx.annotation.Keep;
import n.h;
import n.m;

@Keep
public class CctBackendFactory implements n.d {
    @Override
    public m create(h hVar) {
        return new d(hVar.b(), hVar.e(), hVar.d());
    }
}
