package roeevakrat.betterme;

import android.content.Context;

/**
 * Created by Administrator on 21/11/2017.
 */

public abstract class ServerHandler {

    protected Context context;
    protected String feedback;

    public ServerHandler(Context context){
        this.context = context;
        this.feedback = "No login/register attempted";
    }

    public abstract boolean tryRegister(String username, String password);
    public abstract boolean tryLogin(String username, String password);

    public abstract boolean tryUploadData(BetterMeUserData data);
    public abstract BetterMeUserData tryRetrieveData();
    public abstract String getServerUID();

    public String getFeedback(){
        return feedback;
    }
}
