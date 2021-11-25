package ru.vyatsu.koscheev;

import org.jsoup.nodes.Document;

public interface Parser<T> {
    T Parse(Document document, HtmlLoader loader);
}