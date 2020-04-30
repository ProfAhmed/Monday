package com.aosama.it.utiles;

import com.aosama.it.models.responses.boards.UserBoard;

import java.util.List;

public class MyConfig {
    public static final String BASE_URL = "http://wayak.org/";
    //    public static final String BASE_URL = "https://tmms2020.herokuapp.com/";
    public static final String SIGNIN_URL = BASE_URL + "user/signin";
    public static final String CHANGE_PASSWORD_URL = BASE_URL +
            "user/change-password";
    public static final String BOARDS = BASE_URL + "board";

    public static final String NESTED = BASE_URL + "board/nested";
    public static final String ADD_COMMENT = BASE_URL + "task/comment";
    public static final String COMMENTS_URL = BASE_URL + "task/comment/";
    public static final String ADD_ATTACH_GENERAL = BASE_URL + "task/attachgeneral/";
    public static final String NOTIFICATION_URL = BASE_URL + "task/userTaskNotification";
    public static final String SUBCOMMENTS_URL = BASE_URL + "task/getcommentsub/";
    public static final String ADD_SUB_COMMENT = BASE_URL + "task/commentsub";
    public static final String GET_FILE = BASE_URL + "file/file/";
    public static final String MAILS = BASE_URL + "mail";
    public static final String ADD_MAIL = BASE_URL + "mail";
    //    public static final String NESTED = BASE_URL + "board/nested"+"?id=BOR9139288889";
    public static List<UserBoard> userBoards = null;
    public static String GET_users_URL = BASE_URL + "user/allusers";

    public static class MyPrefs {
        public static final String FIREBASE_TOKEN = "firebase_token";
        public static final String TOKEN = "token";
        public static final String IS_LOGIN = "is_login";
        public static final String LOCAL_LANG = "local_lang";
        public static final String NAME = "user_name";
        public static final String IMAGE = "user_pic";
        public static final String SHORT_NAME = "short_name";
        public static final String USER_ID = "user_id";
        static final String PREFS_NAME = "monday";
        public static final String LANG = "lang";
    }
}
