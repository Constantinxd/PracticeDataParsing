package ru.vyatsu.koscheev.nanegative.parser_store;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.vyatsu.koscheev.HtmlLoader;
import ru.vyatsu.koscheev.Parser;
import ru.vyatsu.koscheev.nanegative.model.Store;

import java.util.ArrayList;

public class StorePageParser implements Parser<ArrayList<Store>> {
    @Override
    public ArrayList<Store> Parse(Document document, HtmlLoader loader) {
        ArrayList<Store> list = new ArrayList<>();

        for (Element element : document.selectXpath("//div[@class='find-list-block']/div[@class='find-list-box']")) {
            int reviewsCount = Integer.parseInt(element.selectXpath("//span[@class='num']").text());

            if (reviewsCount > 0) {
                Store store = new Store();

                String avgRating = element.selectXpath("//span[@class='sro']").text();
                store.avgRating = Double.parseDouble(avgRating.substring(avgRating.length() - 2));
                store.title = element.selectXpath("//a[@class='ss']").text();
                store.link = element.selectXpath("//a[@class='ss']").attr("href");
                store.reviewsCount = reviewsCount;

                list.add(store);
            }
        }

        return list;
    }
}
