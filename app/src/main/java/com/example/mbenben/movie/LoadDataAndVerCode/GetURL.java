package com.example.mbenben.movie.LoadDataAndVerCode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by alone on 2016/10/12.
 * 通过传入的数据，获取到URL，否则每次都要自己拼装
 */
public class GetURL {
    //      修改等级返回的json 对应的 key
    public static final String LEVEL_JSON_KEY = "level";
    //    修改积分返回的json 对应的key
    public static final String INTEGRAL_JSON_KEY = "integral";
    //    获取头像返回json数据的key
    public static final String ICON_URL_JSON_KEY = "url";
    //    修改信息返回json数据的key
    public static final String UPDATE_URL_JSON_KEY = "update";
    private static final String LOGIN_URL = "http://119.29.194.92:8080/movie/login!login?";
    private static final String REG_IS_NULL = "http://119.29.194.92:8080/movie/login!isNull?phone=";
    private static final String REG_URL = "http://119.29.194.92:8080/movie/login!register?phone=";
    private static final String UPDATE_PASSWORD =
            "http://119.29.194.92:8080/movie/login!updatePassword?phone=";
    private static final String UPLOAD_ICON_IMAGE =
            "http://119.29.194.92:8080/movie/upload!upload?phone=";
    private static final String GET_USER_INFO =
            "http://119.29.194.92:8080/movie/login!getInformation?phone=";
    private static final String GET_USER_ICON =
            "http://119.29.194.92:8080/movie/upload!getPictureURL?phone=";
    private static final String UPDATE_USER_BIRTHDAY =
            "http://119.29.194.92:8080/movie/login!update?phone=";
    private static final String UPDATE_USER_SEX =
            "http://119.29.194.92:8080/movie/login!update?phone=";
    private static final String UPDATE_USER_AUTOGRAPH =
            "http://119.29.194.92:8080/movie/login!update?phone=";
    private static final String UPDATE_USER_HOBBY_LABEL =
            "http://119.29.194.92:8080/movie/login!update?phone=";
    private static final String UPDATE_USER_NAME =
            "http://119.29.194.92:8080/movie/login!update?phone=";
    private static final String UPDATE_USER_ADDRESS =
            "http://119.29.194.92:8080/movie/login!update?phone=";
    private static final String UPDATE_USER_LEVEL =
            "http://119.29.194.92:8080/movie/login!updateLevel?phone=";
    private static final String UPDATE_USER_INTEGRAL =
            "http://119.29.194.92:8080/movie/login!updateIntegral?phone=";

    private static final String GET_HOT_CRITIC_FIRST_URL =
            "http://119.29.194.92:8080/movie/critic!getHot";
    //    更加新的
    private static final String GET_HOT_NEW_URL =
            "http://119.29.194.92:8080/movie/critic!getHot2?id=";
    //    当前id之前的。
    private static final String GET_HOT_BEFORE_URL =
            "http://119.29.194.92:8080/movie/critic!getHot3?id=";

    //    模糊查询电影
    private static final String SEARCH_MOVIE_NAME_URL =
            "http://119.29.194.92:8080/movie/critic!getMovieName?name=";
//    查询电影信息
    private static final String SEARCH_MOVIE_INFO_URL =
            "http://op.juhe.cn/onebox/movie/video?key=e712295ae7ca460ec31624dd3dfe2094&q=";
//    获取评论
    private static final String GET_WHO_CRITIC_IT_URL =
            "http://119.29.194.92:8080/movie/critic!getCommentCriticInformation?id=";
//    随机获取电影名
    private static final String GeT_RADOM_MOVIE =
            "http://119.29.194.92:8080/movie/critic!getRondomMovieName";
//    更新二维码
    private static final String UPDATE_USER_erweima =
            "http://119.29.194.92:8080/movie/login!getQRCode?phone=";
//    下拉获取新的电影名
    private static final String GET_NEW_MOVIE_NAME =
            "http://119.29.194.92:8080/movie/critic!getUpMovieName?id=";
//      模糊查询影评
    private static final String SEARCH_CRITIC_BY_NAME =
            "http://119.29.194.92:8080/movie/critic!getPublishCriticForName?name=";

    public static String getSearchCriticByName(String name) {
        try {
            name = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SEARCH_CRITIC_BY_NAME + name;
    }


    public static String getGetNewMovieName(int id) {
        return GET_NEW_MOVIE_NAME + id;
    }
    public static String getGeT_RADOM_MOVIE() {
        return GeT_RADOM_MOVIE;
    }
    /*
    * 根据id获取评论
    * */
    public static String getGetWhoCriticItUrl(int id) {
        return GET_WHO_CRITIC_IT_URL + id;
    }
    /*
    * 查询电影URL
    * */
    public static String getSearchMovieInfoUrl(String name){
        try {
            name = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SEARCH_MOVIE_INFO_URL+name;
    }
    /*
    * 查询电影名字，模糊查询
    * */
    public static String getSearchMovieNameUrl(String name) {
        return SEARCH_MOVIE_NAME_URL + name;
    }

    /*
        * 根据传入的id获取之前的
        * */
    public static String getGetHotBeforeUrl(String id) {
        return GET_HOT_BEFORE_URL + id;
    }
    /*
    * 根据id获取更加新的
    * */
    public static String getGetHotNewUrl(String id) {
        return GET_HOT_NEW_URL + id;
    }

    /*
    * 第一次获取
    * */
    public static String getGetHotCriticFirstUrl() {
        return GET_HOT_CRITIC_FIRST_URL;
    }
    /*
    * 登陆的URL
    * @param phone 电话号码
    * @param password 登陆的密码
    * */
    public static String getLoginURL(String phone, String password) {
        return LOGIN_URL + "phone=" + phone + "&password=" + password;
    }

    /*
    * 判断要注册的账号是否为空
    * @param phone 要注册的电话号码
    * */
    public static String getRegIsNullURL(String phone) {
        return REG_IS_NULL + phone;
    }
    /*
    * 注册的URL
    * @param phone，password 电话号码，密码
    * */
    public static String getRegUrl(String phone, String password) {
        return REG_URL + phone + "&password=" + password;
    }

    /*
    * 获取修改密码的URL
    * */
    public static String getUpdatePasswordURL(String phone, String password) {
        return UPDATE_PASSWORD + phone + "&password=" + password;
    }

    /*
    * 获取上传头像的URL
    * */
    public static String getUploadIconImageURL(String phone) {
        return UPLOAD_ICON_IMAGE + phone;
    }

    /*
    * 获取用户的信息
    *
    * */
    public static String getGetUserInfoURL(String phone) {
        return GET_USER_INFO + phone;
    }

    /*
    * 获取用户的头像
    * */
    public static String getGetUserIconURL(String phone) {
        return GET_USER_ICON + phone;
    }

    /*
    * 修改用户的生日
    *
    * */
    public static String getUpdateUserBirthdayURL(String phone, String birthday) {
        return UPDATE_USER_BIRTHDAY + phone + "&birthday=" + birthday;
    }

    /*
    * 修改用户的性别
    * */
    public static String getUpdateUserSexURL(String phone, String sex) {
        return UPDATE_USER_SEX + phone + "&sex=" + sex;
    }

    /*
    * 修改用户的个性签名
    * */
    public static String getUpdateUserAutographURL(String phone, String autograph) {
        return UPDATE_USER_AUTOGRAPH + phone + "&autograph=" + autograph;
    }

    /*
    * 修改用户的喜好标签
    * */
    public static String getUpdateUserHobbyLabelURL(String phone, String hobbyLabel) {
        return UPDATE_USER_HOBBY_LABEL + phone + "&label=" + hobbyLabel;
    }

    /*
    * 修改昵称
    *
    * */
    public static String getUpdateUserNameURL(String phone, String name) {
        return UPDATE_USER_NAME + phone + "&name=" + name;
    }

    /*
    * 修改用户的地区
    *
    * */
    public static String getUpdateUserAddressURL(String phone, String address) {
        return UPDATE_USER_ADDRESS + phone + "&address=" + address;
    }

    /*
    * 修改用户的等级
    *
    * */
    public static String getUpdateUserLevelURL(String phone, int level) {
        return UPDATE_USER_LEVEL + phone + "&level=" + level;
    }

    /*
    * 修改用户的积分
    * */
    public static String getUpdateUserIntegralURL(String phone, int integral) {
        return UPDATE_USER_INTEGRAL + phone + "&integral=" + integral;
    }
    /*
        * 获取二维码URL
        * */
    public static String getUpdateerweimalURL(String phone) {
        return UPDATE_USER_erweima + phone;
    }
}
