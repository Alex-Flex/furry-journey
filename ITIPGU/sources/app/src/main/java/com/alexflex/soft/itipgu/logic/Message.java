package com.alexflex.soft.itipgu.logic;

import java.util.ArrayList;

//такие вот объекты будут составлять новостную ленту
public class Message {

    private String caption; //подпись
    private String message; //само сообщение
    private String date;    //дата выхода поста
    private String views;   //кол-во просмотров
    private String link;    //ссылка на страницу с подробностями
    private String image;   //ссылка на картинку, если есть

    public static final int GET_CAPTION = 1;
    public static final int GET_MESSAGE = 2;
    public static final int GET_DATE = 3;
    public static final int GET_VIEWS = 4;
    public static final int GET_LINK = 5;
    public static final int GET_IMAGE = 6;

    Message(String caption, String message, String date, String views, String link, String image){
        this.caption = caption;
        this.message = remakeMessageText(message);
        this.date = date;
        this.views = views;
        this.link = "http://tehnikum.ucoz.ru" + link;
        this.image = image;
    }

    //публичный getter, классика ;)
    public String getSomething(int yourChoice){
        switch (yourChoice) {
            case GET_CAPTION:
                return caption;
            case GET_MESSAGE:
                return message;
            case GET_DATE:
                return date;
            case GET_VIEWS:
                return views;
            case GET_LINK:
                return link;
            case GET_IMAGE:
                return image;
        }
        return null;
    }

    /*
     * Обработка текста сообщений с сайта на предмет
     * слов типа "читать дальше", "добавил" и т. д.
     */
    private String remakeMessageText(String string) {
        if (string.contains("Читать дальше")) {
            char[] chars = string.toCharArray();
            ArrayList<Character> result = new ArrayList<>();
            for (int i = 0; i < chars.length; i++) {
                if(chars[i] == '.' && chars[i+1] == '.') break;
                result.add(chars[i]);
            }
            chars = null;
            StringBuilder str = new StringBuilder();
            for(int i=0;i<result.size();i++){
                str.append((char) result.get(i));
            }
            return str.toString();
        }
        return null;
    }
}
