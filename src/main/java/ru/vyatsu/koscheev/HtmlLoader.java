package ru.vyatsu.koscheev;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class HtmlLoader {
    public String url;

    public String getUrl() { return this.url; }

    public Document GetSource() throws IOException {
        return Jsoup.connect(url).get();
    }

    public Document GetSourceByPageId(int id) throws IOException {
        String currentUrl = url.replace("{CurrentId}", Integer.toString(id));
        return Jsoup.connect(currentUrl).get();
    }

    public Document GetSourceByUrl(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
