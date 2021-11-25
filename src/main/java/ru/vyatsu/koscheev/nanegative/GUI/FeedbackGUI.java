package ru.vyatsu.koscheev.nanegative.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import ru.vyatsu.koscheev.OnCompletedHandler;
import ru.vyatsu.koscheev.OnNewDataHandler;
import ru.vyatsu.koscheev.nanegative.model.Feedback;
import ru.vyatsu.koscheev.nanegative.parser_feedback.FeedbackPageParser;
import ru.vyatsu.koscheev.nanegative.parser_feedback.FeedbackPageParserWorker;
import ru.vyatsu.koscheev.nanegative.parser_feedback.FeedbackPageSettings;

import java.io.IOException;
import java.util.ArrayList;

public class FeedbackGUI extends Application
        implements OnNewDataHandler<ArrayList<Feedback>>, OnCompletedHandler {
    private FeedbackPageParserWorker<ArrayList<Feedback>> parser;
    private VBox hBox;
    private int firstPageNum, lastPageNum;
    private String PREFIX;
    private String pageName;
    private int count;

    public FeedbackGUI(int firstPageNum, int lastPageNum, String PREFIX, String pageName) {
        this.firstPageNum = firstPageNum;
        this.lastPageNum = lastPageNum;
        this.PREFIX = PREFIX;
        this.pageName = pageName;
    }

    @Override
    public void OnNewData(Object sender, ArrayList<Feedback> args) {
        for (Feedback feedback : args) {
            hBox.getChildren().add(new Text("#" + ++count));
            hBox.getChildren().add(new TextFlow(new Text("Оценка:\t " + feedback.rating)));
            hBox.getChildren().add(new TextFlow(new Text("Плюсы:\t " + feedback.plus)));
            hBox.getChildren().add(new TextFlow(new Text("Минусы:\t " + feedback.minus)));
            hBox.getChildren().add(new TextFlow(new Text("Отзыв:\t " + feedback.reviewBody + "\n")));
        }
    }

    private void loadFeedbacks(int start, int end, String PREFIX) {
        parser = new FeedbackPageParserWorker<>(new FeedbackPageParser());
        parser.setParserSettings(new FeedbackPageSettings(start, end, PREFIX));
        parser.onCompletedList.add(this);
        parser.onNewDataList.add(this);

        try {
            parser.Start();
            Thread.sleep(5000);
            parser.Abort();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void OnCompleted(Object sender) {
    }

    @Override
    public void start(Stage stage) {
        count = 0;
        hBox = new VBox();

        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        stage.setScene(new Scene(scrollPane));
        stage.setTitle(pageName);
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.setHeight(600);
        stage.setWidth(600);
        stage.show();
        loadFeedbacks(firstPageNum, lastPageNum, PREFIX);
    }
}
