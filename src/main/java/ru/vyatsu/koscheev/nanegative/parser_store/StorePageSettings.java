package ru.vyatsu.koscheev.nanegative.parser_store;

import ru.vyatsu.koscheev.ParserSettings;

public class StorePageSettings extends ParserSettings {
    public StorePageSettings(int start, int end) {
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://nanegative.ru";
        PREFIX = "internet-magaziny";
        GET_REQUEST = "?page={CurrentId}";
    }
}
