package ru.vyatsu.koscheev.leroymerlin.parser_catalogue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.vyatsu.koscheev.HtmlLoader;
import ru.vyatsu.koscheev.Parser;
import ru.vyatsu.koscheev.leroymerlin.model.Feedback;
import ru.vyatsu.koscheev.leroymerlin.model.Product;

import java.util.ArrayList;

public class CatalogueParser implements Parser<Product> {
    @Override
    public Product Parse(Document document, HtmlLoader loader) {
        Product product = new Product();
        ArrayList<Feedback> feedbacks = new ArrayList<>();

        try {
            product.name = document.selectXpath("//h1[@slot='title']").text();
            product.feedbacks = feedbacks;

            if (document.selectXpath("//uc-prp-pages-collector[@slot='main-content']").size() > 0) {
                feedbacks.addAll(ParseFeedbacks(document));

                while (document.selectXpath("//a[@slot='show-more']").size() > 0) {
                    document = Jsoup.connect(document.selectXpath("//a[@slot='show-more']").get(0).attr("href")).get();
                    feedbacks.addAll(ParseFeedbacks(document));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    public ArrayList<Feedback> ParseFeedbacks(Document document) {
        ArrayList<Feedback> feedbacks = new ArrayList<>();

        for (Element r : document.selectXpath("//uc-prp-pages-collector[@slot='main-content']//*[@itemprop='review']")) {
            Feedback feedback = new Feedback();

            feedback.review = r.selectXpath("//ui-typo-16[@slot='review-text']").text();
            for (Element e : r.selectXpath("//uc-prp-term-group[@slot='term-list']")) {
                Element review = e.selectXpath("//ui-typo-16[@slot='term']").get(0);
                if (review != null) {
                    if (review.text().equals("Достоинства:"))
                        feedback.reviewProns = e.selectXpath("//ui-typo-16[@slot='desc']").text();
                    else if (review.text().equals("Недостатки:"))
                        feedback.reviewCons = e.selectXpath("//ui-typo-16[@slot='desc']").text();
                }
            }
            feedbacks.add(feedback);
        }

        return feedbacks;
    }
}
