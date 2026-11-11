package org.cocos2dx.lib;

import java.util.concurrent.CountDownLatch;

class b implements Runnable {
    private CountDownLatch i;
    private boolean[] j;
    private final int k;
    private final String l;

    b(CountDownLatch countDownLatch, boolean[] zArr, int i, String str) {
        this.i = countDownLatch;
        this.j = zArr;
        this.k = i;
        this.l = str;
    }

    @Override
    public void run() {
        this.j[0] = Cocos2dxWebViewHelper._shouldStartLoading(this.k, this.l);
        this.i.countDown();
    }
}
