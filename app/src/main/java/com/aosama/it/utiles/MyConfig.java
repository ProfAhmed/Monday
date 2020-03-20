package com.aosama.it.utiles;

public class MyConfig {
    public static final String BASE_URL = "https://tmms2020.herokuapp.com/";
    public static final String SIGNIN_URL = BASE_URL + "user/signin";
    public static final String CHANGE_PASSWORD_URL = BASE_URL +
            "user/change-password";
    public static final String BOARDS = BASE_URL + "board";

    public static final String NESTED = BASE_URL + "board/nested";
//    public static final String NESTED = BASE_URL + "board/nested"+"?id=BOR9139288889";

    public class MyPrefs {
        public static final String FIREBASE_TOKEN = "firebase_token";
        public static final String TOKEN = "token";
        public static final String IS_LOGIN = "is_login";
        public static final String LOCAL_LANG = "local_lang";
        static final String PREFS_NAME = "monday";
        public static final String LANG = "lang";
    }
}
