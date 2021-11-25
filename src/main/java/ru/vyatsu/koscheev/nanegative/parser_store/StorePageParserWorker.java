package ru.vyatsu.koscheev.nanegative.parser_store;

import org.jsoup.nodes.Document;
import ru.vyatsu.koscheev.*;

import java.io.IOException;
import java.util.ArrayList;

public class StorePageParserWorker<T> {
    private Parser<T> parser;
    private ParserSettings parserSettings;
    private HtmlLoader loader;
    private boolean isActive;

    public final ArrayList<OnNewDataHandler<T>> onNewDataList;
    public final ArrayList<OnCompletedHandler> onCompletedList;

    public StorePageParserWorker(Parser<T> parser) {
        setParser(parser);

        this.onNewDataList = new ArrayList<>();
        this.onCompletedList = new ArrayList<>();
    }

    public Parser<T> getParser() { return this.parser; }

    public void setParserSettings(ParserSettings parserSettings) {
        this.parserSettings = parserSettings;
        loader = new StorePageHtmlLoader();
    }

    public ParserSettings getParserSettings() { return this.parserSettings; }

    public void setParser(Parser<T> parser) { this.parser = parser; }

    public void Start() throws IOException, InterruptedException {
        isActive = true;
        Worker();
    }

    public void Abort() {
        isActive = false;
    }

    protected void Worker() throws IOException {
        for (int i = parserSettings.getStartPoint(); i <= parserSettings.getEndPoint(); i++) {
            if (!isActive) {
                onCompletedList.get(0).OnCompleted(this);
                return;
            }

            Document document = loader.GetSourceByPageId(i);
            T result = parser.Parse(document, null);
            onNewDataList.get(0).OnNewData(this, result);
        }
        onCompletedList.get(0).OnCompleted(this);
        isActive = false;
    }
}
