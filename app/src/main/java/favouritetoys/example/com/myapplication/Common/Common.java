package favouritetoys.example.com.myapplication.Common;

import favouritetoys.example.com.myapplication.Model.User;

/**
 * Created by Divya Gupta on 11-04-2018.
 */

public class Common {
    public  static User currentUser;

    public static String convertCodeToStatus(String code) {
        if (code.equals("0"))
            return "Placed";

        else if (code.equals("0"))
            return "On the way";

        else
            return "Shipped";


    }

}
