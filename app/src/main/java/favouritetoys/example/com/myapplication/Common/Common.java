package favouritetoys.example.com.myapplication.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import favouritetoys.example.com.myapplication.Model.User;

/**
 * Created by Divya Gupta on 11-04-2018.
 */

public class Common {
    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public  static User currentUser;

    public static String convertCodeToStatus(String code) {
        if (code.equals("0"))
            return "Placed";

        else if (code.equals("0"))
            return "On the way";

        else
            return "Shipped";


    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;

                }
            }
        }
        return false;
    }

}
