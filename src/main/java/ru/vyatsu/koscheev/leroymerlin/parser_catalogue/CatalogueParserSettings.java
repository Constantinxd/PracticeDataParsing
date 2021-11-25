package ru.vyatsu.koscheev.leroymerlin.parser_catalogue;

import ru.vyatsu.koscheev.ParserSettings;

public class CatalogueParserSettings{
    public String BASE_URL;
    public int mainCategory;
    public int subCategory;

    public CatalogueParserSettings(int mainCategory, int subCategory) {
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        BASE_URL = "https://kirov.leroymerlin.ru/catalogue/";
    }
}
