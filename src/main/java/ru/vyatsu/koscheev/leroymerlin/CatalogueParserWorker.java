package ru.vyatsu.koscheev.leroymerlin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.vyatsu.koscheev.*;
import ru.vyatsu.koscheev.leroymerlin.model.Product;
import ru.vyatsu.koscheev.leroymerlin.parser_catalogue.CatalogueParser;
import ru.vyatsu.koscheev.leroymerlin.parser_catalogue.CatalogueParserSettings;
import ru.vyatsu.koscheev.leroymerlin.save.SaveXml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CatalogueParserWorker<T> {
    private Parser<T> parser;
    private CatalogueParserSettings parserSettings;
    private WebDriver driver;
    public SaveXml save;

    public CatalogueParserWorker(Parser<T> parser) {
        setParser(parser);
    }

    public void setParserSettings(CatalogueParserSettings parserSettings) {
        this.parserSettings = parserSettings;
        setDriverSettings();
    }

    private void setDriverSettings() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get(parserSettings.BASE_URL);
    }

    public void setParser(Parser<T> parser) { this.parser = parser; }

    public void Start() throws IOException {
        Worker();
    }

    private List<String> getListOfHrefValue(By by) {
        List<String> links = new ArrayList<>();

        for (var l : driver.findElements(by)) {
            links.add(l.getAttribute("href"));
        }

        return links;
    }

    private String getSectionName(String link) {
        String sectionName = link.substring(0, link.length() - 1);
        sectionName = sectionName.substring(sectionName.lastIndexOf('/') + 1);

        return sectionName;
    }

    protected void Worker() throws IOException {
        List<String> links1 = getListOfHrefValue(By.xpath("//nav[@class='leftmenu-small']//li[not(contains(@class,'banner'))]//a"));
        String l1 = links1.get(parserSettings.mainCategory - 1);
        driver.get(l1);

        List<String> links2 = getListOfHrefValue(By.xpath("//div[@data-qa-aside-section]//a[@data-qa='catalog-link']"));
        String l2 = links2.get(parserSettings.subCategory - 1);
        driver.get(l2);

        save = new SaveXml(getSectionName(l1) + "_" + getSectionName(l2) + ".xml");
        save.writeStartDocument();

        System.out.println(getSectionName(l1));
        save.writeStartElement("main-category", getSectionName(l1), 0);

        System.out.println("\t" + getSectionName(l2));
        save.writeStartElement("sub-category", getSectionName(l2), 1);

        List<String> links3 = getListOfHrefValue(By.xpath("//div[@data-qa-aside-section]//a[@data-qa='catalog-link']"));

        for (var l3 : links3) {
            System.out.println("\t\t" + getSectionName(l3));
            driver.get(l3);
            save.writeStartElement("category", getSectionName(l3), 2);

            List<String> links4 = getListOfHrefValue(By.xpath("//div[@data-qa-product]//a[@data-qa-product-image]"));

            for (var l4 : links4) {
                System.out.println("\t\t\t" + getSectionName(l4));
                Document document = Jsoup.connect(l4).get();
                T product = parser.Parse(document, null);
                save.OnNewData(this, (Product) product);
            }

            while (isElementPresent(By.xpath("//a[@data-qa-pagination-item='right']"))) {
                driver.get(driver.findElement(By.xpath("//a[@data-qa-pagination-item='right']")).getAttribute("href"));
                links4 = getListOfHrefValue(By.xpath("//div[@data-qa-product]//a[@data-qa-product-image]"));

                for (var l4 : links4) {
                    System.out.println("\t\t\t" + getSectionName(l4));
                    Document document = Jsoup.connect(l4).get();
                    T product = parser.Parse(document, null);
                    save.OnNewData(this, (Product) product);
                }
            }

            save.writeEndElement(2);
        }

        save.writeEndElement(1);
        save.writeEndElement(0);
        save.writeEndDocument();

        driver.quit();
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        CatalogueParserWorker<Product> parser = new CatalogueParserWorker<>(new CatalogueParser());
        //mainCategory = 12 - Кухни, subCategory = 1 - Мебель для кухни
        parser.setParserSettings(new CatalogueParserSettings(12, 1));
        parser.Start();
    }
}
