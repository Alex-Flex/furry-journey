package com.alexflex.soft.itipgu.logic;

//класс для новостей с сайта ИТИ
public class MessageITI {
    public static final int GET_TITLE = 1;
    public static final int GET_DATE = 2;
    public static final int GET_MESSAGE = 3;

    private String title;
    private String date;
    private String message;

    MessageITI(String title, String date, String message){
        this.title = title;
        this.date = date;
        this.message = message;
    }

    //классический getter, юзаем конмтанты этого
    //класса для работы с этим методом
    public String getSomething(int item){
        switch (item) {
            case GET_TITLE:
                return this.title;
            case GET_DATE:
                return this.date;
            case GET_MESSAGE:
                return this.message;
        }
        return null;
    }
}
