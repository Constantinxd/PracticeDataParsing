package ru.vyatsu.koscheev.nanegative.parser_feedback;

import ru.vyatsu.koscheev.ParserSettings;

public class FeedbackPageSettings extends ParserSettings {
    public FeedbackPageSettings(int start, int end, String PREFIX) {
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://nanegative.ru";
        this.PREFIX = PREFIX;
        GET_REQUEST = "?page={CurrentId}";
    }
}
