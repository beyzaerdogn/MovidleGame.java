package com.example.filmdeneme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private List<Film> filmList;
    private int tahminhakkı;
    @Override
    public void start(Stage primaryStage) {
        filmList = readFilmDataFromCSV("D:\\filmdeneme\\target\\classes\\com\\example\\filmdeneme\\imdb_top_250 (1).csv");
        if (filmList.isEmpty()) {
            System.out.println("Film data could not be loaded from CSV.");
            return;
        }
        tahminhakkı=5;

        Label resultLabel = new Label();
        Label tahminlabel = new Label("tahmin hakkı"+tahminhakkı);
        TextField guessTextField = new TextField();
        Button guessButton = new Button("Guess");

        guessButton.setOnAction(e -> {
            if (tahminhakkı <= 0)
            { resultLabel.setText("Tahminleriniz tükendi! Oyun bitti.");
            return;
            }

            String guess = guessTextField.getText();
            boolean correctGuess = false;

            for (Film film : filmList) {
                if (film.getTitle().equalsIgnoreCase(guess)) {
                    resultLabel.setText("doğru bildiniz tebrikler!");
                    correctGuess = true;
                    break;
                }
            }

            if (!correctGuess) {
                StringBuilder suggestions = new StringBuilder("yanlış cevap :(");

                for (Film film : filmList) {
                    String title = film.getTitle();
                    if (isApproximateMatch(guess, title)) {
                        suggestions.append(title).append("\n");
                    }
                }

                resultLabel.setText(suggestions.toString());
                tahminhakkı--;
                if (tahminhakkı == 0) {
                    resultLabel.setText("Tahminleriniz tükendi! Oyun bitti.");
                }
            }else {
                tahminlabel.setVisible(false);
            }
        });

        HBox hbox = new HBox(60, guessTextField, guessButton);
        hbox.setPadding(new Insets(60));

        VBox vbox = new VBox(10, hbox, resultLabel);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Film Guessing Game");
        primaryStage.show();
    }

    private List<Film> readFilmDataFromCSV(String fileName) {
        List<Film> filmList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 8) {
                    Film film = new Film(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                    filmList.add(film);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filmList;
    }

    private boolean isApproximateMatch(String guess, String title) {
        return title.toLowerCase().contains(guess.toLowerCase());
    }
}