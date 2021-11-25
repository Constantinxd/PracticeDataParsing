package ru.vyatsu.koscheev;

public abstract class ParserSettings {
    // Адрес сайта
    public static String BASE_URL;

    // префих страницы
    public static String PREFIX;

    // GET запрос
    public static String GET_REQUEST;

    // начало пагинации
    protected int startPoint;

    // конец пагинации
    protected int endPoint;

    public int getStartPoint() { return this.startPoint; }

    public int getEndPoint() { return this.endPoint; }
}
