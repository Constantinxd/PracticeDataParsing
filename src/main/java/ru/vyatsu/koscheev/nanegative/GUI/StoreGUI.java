package ru.vyatsu.koscheev.nanegative.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import ru.vyatsu.koscheev.OnCompletedHandler;
import ru.vyatsu.koscheev.OnNewDataHandler;
import ru.vyatsu.koscheev.nanegative.parser_store.StorePageParserWorker;
import ru.vyatsu.koscheev.nanegative.model.Store;
import ru.vyatsu.koscheev.nanegative.parser_store.StorePageParser;
import ru.vyatsu.koscheev.nanegative.parser_store.StorePageSettings;

import java.io.IOException;
import java.util.ArrayList;

public class StoreGUI extends Application
        implements OnNewDataHandler<ArrayList<Store>>, OnCompletedHandler {
    private StorePageParserWorker<ArrayList<Store>> parser;
    private VBox hBox;
    private int count;

    @Override
    public void OnNewData(Object sender, ArrayList<Store> args) {
        for (Store store : args) {
            hBox.getChildren().add(new Text("#" + ++count));

            Hyperlink title = new Hyperlink(store.title);
            title.setOnAction(e -> {
                launchForm(1, (int)Math.ceil(store.reviewsCount / 30d), store.link, store.title);
            });
            hBox.getChildren().add(new TextFlow(title));

            hBox.getChildren().add(new TextFlow(new Text("Средняя оценка - " + store.avgRating + "  Количество отзывов - " + store.reviewsCount + "\n")));
        }
    }

    @Override
    public void OnCompleted(Object sender) {
    }

    private void loadStores() {
        hBox.getChildren().clear();
        count = 0;

        int start = 1;
        int end = 10;

        parser = new StorePageParserWorker<>(new StorePageParser());
        parser.setParserSettings(new StorePageSettings(start, end));
        parser.onCompletedList.add(this);
        parser.onNewDataList.add(this);

        try {
            parser.Start();
            Thread.sleep(3000);
            parser.Abort();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void launchForm(int firstPageNum, int lastPageNum, String PREFIX, String pageName) {
        Platform.runLater(() -> new FeedbackGUI(firstPageNum, lastPageNum, PREFIX, pageName).start(new Stage()));
    }

    @Override
    public void start(Stage stage) {
        count = 0;
        hBox = new VBox();

        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        stage.setScene(new Scene(scrollPane));
        stage.setTitle("Nanegative.ru");
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.setHeight(600);
        stage.setWidth(600);
        stage.show();
        loadStores();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
