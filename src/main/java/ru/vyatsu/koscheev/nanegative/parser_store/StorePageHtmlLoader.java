package ru.vyatsu.koscheev.nanegative.parser_store;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.vyatsu.koscheev.HtmlLoader;
import ru.vyatsu.koscheev.ParserSettings;

import java.io.IOException;

public class StorePageHtmlLoader extends HtmlLoader {
    public StorePageHtmlLoader() {
        this.url = ParserSettings.BASE_URL + "/" + ParserSettings.PREFIX + ParserSettings.GET_REQUEST;
    }

    public Document GetSourceByPageId(int id) throws IOException {
        String currentUrl = url.replace("{CurrentId}", Integer.toString(id));
        return Jsoup.connect(currentUrl).get();
    }
}
