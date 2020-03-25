package com.aosama.it.network;

public class ApiUtilise {
    private ApiUtilise() {
    }

    public static IrApiService getAPIService() {

        return RetrofitClient.getClient().create(IrApiService.class);
    }
}