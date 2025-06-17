package com.nuvei.nuvei_sdk;

import java.util.UUID;

public class Nuvei {
    private static  boolean TEST_MODE;
    private static String CLIENT_CODE;
    private static String CLIENT_KEY;



    public static void configEnvironment(boolean isDev, String CLIENT_CODE_APP, String CLIENT_KEY_APP){
        TEST_MODE =  isDev;
        CLIENT_CODE = CLIENT_CODE_APP;
        CLIENT_KEY = CLIENT_KEY_APP;
    }


    //implements the next methods
    /*
    * add cards
    * List cards
    * delete cards
    * debit with token
    * refund
    * callback
    *
    * */


    public static String getSessionId(){
        String sessionID = UUID.randomUUID().toString();
        return  sessionID.replace("-", "");
    }

}
