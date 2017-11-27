package roeevakrat.betterme;

import android.content.Context;

/**
 * Created by Administrator on 21/11/2017.
 */

public abstract class ServerHandler {

    protected Context context;
    protected String logFeedback;
    protected String storageFeedback;

    public ServerHandler(Context context){
        this.context = context;
        this.logFeedback = this.storageFeedback ="Please try again";
    }

    public abstract boolean tryRegister(String username, String password);
    public abstract boolean tryLogin(String username, String password);

    public abstract boolean tryUploadData(UserData data);
    public abstract UserData tryRetrieveData();

    public String getLogFeedback() {
        return logFeedback;
    }

    public String getStorageFeedback() {
        return storageFeedback;
    }
}
