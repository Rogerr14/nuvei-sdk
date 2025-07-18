package com.nuvei.nuvei_sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kount.api.DataCollector;
import com.nuvei.nuvei_sdk.add_card.model.CardRequestModel;
import com.nuvei.nuvei_sdk.add_card.model.CardResponseModel;
import com.nuvei.nuvei_sdk.add_card.services.IAddCardService;
import com.nuvei.nuvei_sdk.delete_card.model.DeleteCardRequestModel;
import com.nuvei.nuvei_sdk.delete_card.model.DeleteCardResponseModel;
import com.nuvei.nuvei_sdk.delete_card.service.IDeleteCardCallback;
import com.nuvei.nuvei_sdk.delete_card.service.IDeleteCardService;
import com.nuvei.nuvei_sdk.helpers.GlobalHelper;
import com.nuvei.nuvei_sdk.add_card.services.iAddCardCallback;
import com.nuvei.nuvei_sdk.interceptorHttp.InterceptorHttp;
import com.nuvei.nuvei_sdk.list_card.model.CardListResponseModel;
import com.nuvei.nuvei_sdk.list_card.services.IListCardCallback;
import com.nuvei.nuvei_sdk.list_card.services.IListCardsService;
import com.nuvei.nuvei_sdk.models.CardModel;
import com.nuvei.nuvei_sdk.models.ErrorResponse;
import com.nuvei.nuvei_sdk.models.ErrorModel;
import com.nuvei.nuvei_sdk.models.UserModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nuvei {
    private static  boolean TEST_MODE;
    private static String CLIENT_CODE;
    private static String CLIENT_KEY;

    private static String SERVER_CODE;
    private static String SERVER_KEY;

    static IAddCardService iAddCardService;
    static IListCardsService iListCardsService;
    static IDeleteCardService iDeleteCardService;
    static String MERCHANT_ID = "500005";
    static  int KOUNT_ENVIRONMENT = DataCollector.ENVIRONMENT_TEST;


    public static boolean isTestMode() {
        return TEST_MODE;
    }

    public static String getClientCode() {
        return CLIENT_CODE;
    }

    public static String getClientKey() {
        return CLIENT_KEY;
    }

    /**
     * Entry point library
     * Config environment to set a url scheme.
     * @param isDev false to production environment
     * @param CLIENT_CODE_APP provided by Nuvei
     * @param CLIENT_KEY_APP provided by Nuvei
     *
     * params {@param CLIENT_CODE_APP} and {@param CLIENT_KEY_APP} is config in app project
     *
     */


    public static void configEnvironment(boolean isDev, String CLIENT_CODE_APP, String CLIENT_KEY_APP, String SERVER_CODE_APP, String SERVER_KEY_APP){

       Log.v("asignacion", CLIENT_CODE_APP);
        TEST_MODE =  isDev;
        CLIENT_CODE = CLIENT_CODE_APP;
        CLIENT_KEY = CLIENT_KEY_APP;
        SERVER_CODE = SERVER_CODE_APP;
        SERVER_KEY = SERVER_KEY_APP;
        if(!TEST_MODE){
            KOUNT_ENVIRONMENT = DataCollector.ENVIRONMENT_PRODUCTION;
        }
    }

    /**
     *
     * @param mContext contexto from application
     * @param uid id used to identify to client
     * @param email client email,
     * @param cardModel model to card
     * @param iAddCardCallback
     */
    public static  void addCard(Context mContext, @NonNull final String uid, @NonNull final String email, @NonNull final CardModel cardModel, @NonNull iAddCardCallback iAddCardCallback){
        Log.v("METODO", CLIENT_CODE);
        InterceptorHttp client = new InterceptorHttp();
            iAddCardService = client.getClient(mContext, CLIENT_CODE, CLIENT_KEY).create(IAddCardService.class);
            UserModel userModel = new UserModel();
            userModel.setId(uid);
            userModel.setEmail(email);
            userModel.setFiscal_number(cardModel.getFiscal_number());
        CardRequestModel cardRequestModel = new CardRequestModel();

//        try{
//
//        //cardRequestModel.setSessionId(getSessionId(mContext));
//        }catch (Exception e){
//            Log.v("ocurrio un error", e.toString());
//        }
        cardRequestModel.setCard(cardModel);
        cardRequestModel.setUser(userModel);
        iAddCardService.addCard(cardRequestModel).enqueue(new Callback<CardResponseModel>() {
            @Override
            public void onResponse(Call<CardResponseModel> call, Response<CardResponseModel> response) {
                CardResponseModel cardResponseModel = response.body();
                if(response.isSuccessful()){
                    if(cardResponseModel != null) {
                        iAddCardCallback.onSuccess(cardResponseModel.getCard());
                    }else{
                        ErrorResponse errorResponse = new ErrorResponse("Error", "", "No information");
                        Gson gson = new GsonBuilder().create();
                        try{

                            ErrorModel errorModel = gson.fromJson(response.errorBody().toString(), ErrorModel.class);
                            iAddCardCallback.onError(errorModel.getError());
                            return;

                        }catch (Exception e){
                            try{
                                errorResponse = new ErrorResponse("Exception", "Http Code: " + response.code(), response.message());
                            }catch (Exception e2){

                            }
                        }
                        iAddCardCallback.onError(errorResponse);
                    }
                }else{
                    ErrorResponse errorResponse = new ErrorResponse("Error", "", "General Error");
                    Gson gson = new GsonBuilder().create();
                    try{

                        ErrorModel errorModel = gson.fromJson(response.errorBody().toString(), ErrorModel.class);
                        iAddCardCallback.onError(errorModel.getError());
                        return;

                    }catch (Exception e){
                        try{
                            errorResponse = new ErrorResponse("Exception", "Http Code: " + response.code(), response.message());
                        }catch (Exception e2){

                        }
                    }
                    iAddCardCallback.onError(errorResponse);
                }
            }
            @Override
            public void onFailure(Call<CardResponseModel> call, Throwable e) {
                ErrorResponse error
                        = new ErrorResponse("Network Exception",
                        "Invoked when a network exception occurred communicating to the server.", e.getLocalizedMessage());
                iAddCardCallback.onError(error);
            }
        });
    }



    public  static void getAllCards(Context context, String userID, IListCardCallback iListCardCallback){
        InterceptorHttp client = new InterceptorHttp();
        iListCardsService = client.getClient(context, SERVER_CODE, SERVER_KEY).create(IListCardsService.class);

        iListCardsService.getAllCards(userID).enqueue(new Callback<CardListResponseModel>() {
            @Override
            public void onResponse(Call<CardListResponseModel> call, Response<CardListResponseModel> response) {
                CardListResponseModel cardListResponseModel = response.body();
                if(response.isSuccessful()){
                    if(cardListResponseModel != null){
                        List<CardModel> cardAvailable = cardListResponseModel.getCards().stream()
                                .filter(cardModel -> cardModel.getStatus() != null && cardModel.getStatus().equals("valid"))
                                .collect(Collectors.toList());
                    iListCardCallback.onSuccess(cardAvailable);
                    }else{
                        ErrorResponse errorResponse = new ErrorResponse("Error", "", "No information");
                        Gson gson = new GsonBuilder().create();
                        try{

                            ErrorModel errorModel = gson.fromJson(response.errorBody().toString(), ErrorModel.class);
                            iListCardCallback.onError(errorModel.getError());
                            return;

                        }catch (Exception e){
                            try{
                                errorResponse = new ErrorResponse("Exception", "Http Code: " + response.code(), response.message());
                            }catch (Exception e2){

                            }
                        }
                        iListCardCallback.onError(errorResponse);
                    }
                }else{
                    ErrorResponse errorResponse = new ErrorResponse("Error", "", "General Error");
                    Gson gson = new GsonBuilder().create();
                    try{

                        ErrorModel errorModel = gson.fromJson(response.errorBody().toString(), ErrorModel.class);
                        iListCardCallback.onError(errorModel.getError());
                        return;

                    }catch (Exception e){
                        try{
                            errorResponse = new ErrorResponse("Exception", "Http Code: " + response.code(), response.message());
                        }catch (Exception e2){

                        }
                    }
                    iListCardCallback.onError(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<CardListResponseModel> call, Throwable e) {
                ErrorResponse error
                        = new ErrorResponse("Network Exception",
                        "Invoked when a network exception occurred communicating to the server.", e.getLocalizedMessage());
                iListCardCallback.onError(error);
            }
        });
    }


    public static void deleteCard(Context context, String userID, String tokenCard, IDeleteCardCallback iDeleteCardCallback){
        InterceptorHttp client = new InterceptorHttp();
        iDeleteCardService = client.getClient(context, SERVER_CODE, SERVER_KEY).create(IDeleteCardService.class);
        DeleteCardRequestModel deleteCardRequestModel = new DeleteCardRequestModel(tokenCard, userID);
        iDeleteCardService.deleteCard(deleteCardRequestModel).enqueue(new Callback<DeleteCardResponseModel>() {
            @Override
            public void onResponse(Call<DeleteCardResponseModel> call, Response<DeleteCardResponseModel> response) {
                DeleteCardResponseModel deleteCardResponseModel = response.body();
                if(response.isSuccessful()){
                    iDeleteCardCallback.onSuccess(deleteCardResponseModel.getMessage());
                }else{
                    ErrorResponse errorResponse = new ErrorResponse("Error", "", "General Error");
                    Gson gson = new GsonBuilder().create();
                    try {
                        ErrorModel errorModel = gson.fromJson(response.errorBody().toString(), ErrorModel.class);
                        iDeleteCardCallback.onError(errorModel.getError());
                        return;
                    }catch (Exception e){
                        try{
                            errorResponse =  new ErrorResponse("Exception", "Http Code" + response.code(), response.message());

                        }catch (Exception ex){

                        }
                    }
                    iDeleteCardCallback.onError(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<DeleteCardResponseModel> call, Throwable e) {
                ErrorResponse error
                        = new ErrorResponse("Network Exception",
                        "Invoked when a network exception occurred communicating to the server.", e.getLocalizedMessage());
                iDeleteCardCallback.onError(error);
            }
        });
    }



    //implements the next methods
    /*
    * delete cards-
    * debit with token-
    * refund
    * callback
    *
    * */


    public static String getSessionId(Context context){
        String sessionID = UUID.randomUUID().toString();
        final String deviceSessionID =sessionID.replace("-", "");
        final DataCollector dataCollector = DataCollector.getInstance();
       dataCollector.setDebug(TEST_MODE);
       dataCollector.setContext(context);
       dataCollector.setMerchantID(MERCHANT_ID);
       dataCollector.setEnvironment(KOUNT_ENVIRONMENT);
       dataCollector.setLocationCollectorConfig(DataCollector.LocationConfig.COLLECT);
       new Handler(Looper.getMainLooper()).post(new Runnable() {
           @Override
           public void run() {
               dataCollector.collectForSession(deviceSessionID, new DataCollector.CompletionHandler(){

                   @Override
                   public void completed(String s) {

                   }

                   @Override
                   public void failed(String s, DataCollector.Error error) {

                   }
               });
           }


       });
        return   deviceSessionID;
    }

}
