package com.shlomi.instagramapp.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InetConnection {
    private boolean available;

    public InetConnection(int timeout) {
        this.available = this.checkInternet(timeout);
    }

    public boolean isInternetAvailable(){
        return this.available;
    }

    private boolean checkInternet(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        }
        catch (InterruptedException e) {e.printStackTrace();}
        catch (ExecutionException e) {e.printStackTrace();}
        catch (TimeoutException e) {e.printStackTrace();}
        return inetAddress != null && !inetAddress.equals("");
    }
}
