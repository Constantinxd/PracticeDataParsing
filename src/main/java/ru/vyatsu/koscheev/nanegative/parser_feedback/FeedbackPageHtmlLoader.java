package ru.vyatsu.koscheev.nanegative.parser_feedback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.vyatsu.koscheev.HtmlLoader;
import ru.vyatsu.koscheev.ParserSettings;
import ru.vyatsu.koscheev.nanegative.parser_store.StorePageSettings;

import java.io.IOException;

public class FeedbackPageHtmlLoader extends HtmlLoader {
    public FeedbackPageHtmlLoader() {
        this.url = ParserSettings.BASE_URL + ParserSettings.PREFIX + ParserSettings.GET_REQUEST;
    }

    public Document GetSourceByPageId(int id) throws IOException {
        String currentUrl = url.replace("{CurrentId}", Integer.toString(id));
        return Jsoup.connect(currentUrl).get();
    }
}
