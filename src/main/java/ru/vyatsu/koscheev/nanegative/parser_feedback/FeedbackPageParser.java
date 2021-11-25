package ru.vyatsu.koscheev.nanegative.parser_feedback;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.vyatsu.koscheev.HtmlLoader;
import ru.vyatsu.koscheev.Parser;
import ru.vyatsu.koscheev.nanegative.model.Feedback;
import ru.vyatsu.koscheev.nanegative.model.Store;

import java.util.ArrayList;

public class FeedbackPageParser implements Parser<ArrayList<Feedback>> {
    @Override
    public ArrayList<Feedback> Parse(Document document, HtmlLoader loader) {
        ArrayList<Feedback> list = new ArrayList<>();

        for (Element element : document.selectXpath("//div[@class='reviewers-block']/div[@class='reviewers-box']")) {
            Feedback feedback = new Feedback();

            feedback.rating = Double.parseDouble(element.selectXpath("//span[@itemprop='ratingValue']").text());
            feedback.plus = element.selectXpath("//td[@itemprop='pro']").text();
            feedback.minus = element.selectXpath("//td[@itemprop='contra']").text();
            feedback.reviewBody = element.selectXpath("//td[@itemprop='reviewBody']").text();

            list.add(feedback);
        }

        return list;
    }
}
