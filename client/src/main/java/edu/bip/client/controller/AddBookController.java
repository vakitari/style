package edu.bip.client.controller;

import com.google.gson.Gson;
import edu.bip.client.entity.AuthorEntity;
import edu.bip.client.entity.BookEntity;
import edu.bip.client.entity.PublishingEntity;
import edu.bip.client.utils.HTTPUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static edu.bip.client.controller.ApplicationController.*;


public class AddBookController  {
    @FXML
    private TextField bookName_field;
    @FXML
    private ComboBox<AuthorEntity> bookAuthor_box;
    @FXML
    private ComboBox<PublishingEntity> bookPublisher_box;
    @FXML
    private TextField bookYear_field;
    @FXML
    private TextField bookChapter_field;
    static HTTPUtils http = new HTTPUtils();
    static Gson gson = new Gson();
    public static String api = "http://localhost:28242/api/v1/";

    private Stage editBookStage;

    private BookEntity book;
    private int bookID;
    private boolean okClicked = false;
    private ObservableList<AuthorEntity> authorsBookData = FXCollections.observableArrayList();
    private ObservableList<PublishingEntity> publishersBookData = FXCollections.observableArrayList();

    @FXML
    void initialize() throws Exception {

        if (authorsBookData.size() != authorsData.size() || publishersBookData.size() != publishersData.size()){
            updateMap();
        }
        updateComboBox();

    }

    private void updateMap(){
        authorsBookData.addAll(authorsData);
        publishersBookData.addAll(publishersData);
    }

    private void updateComboBox() throws Exception {
        bookAuthor_box.getItems().addAll(authorsBookData);
        bookPublisher_box.getItems().addAll(publishersBookData);
    }

    public void setDialogStage (Stage dialogStage) {this.editBookStage = dialogStage;}

    @FXML
    private void handleCancel() {editBookStage.close();}

    @FXML
    private void handleOk() throws IOException {
        if (isInputValid()) {
            book.setTitle(bookName_field.getText());
            book.setAuthor(bookAuthor_box.getValue());
            book.setPublishing(bookPublisher_box.getValue());
            book.setYear(bookYear_field.getText());
            book.setKing(bookChapter_field.getText());

            okClicked = true;
            editBookStage.close();
            booksData.set(bookID, book);

        }
    }
    public void setData(ObservableList<AuthorEntity> authorsData, ObservableList<PublishingEntity> publishersData){

        this.authorsBookData = authorsData;
        this.publishersBookData = publishersData;
        bookAuthor_box.getItems().addAll(authorsData);
        bookPublisher_box.getItems().addAll(publishersData);

    }

    private boolean isInputValid() {
        String errorMessage = "";
        try {
        if (!bookName_field.getText().matches("[\\sA-ZА-Яa-za-я]{3,20}") ||bookName_field.getText() == null || bookName_field.getText().length() == 0) errorMessage += "Некорректное значение названия книги!\n";
        if (bookAuthor_box.getValue() == null ) errorMessage += "Не обнаружен автор книги!\n";
        if (bookPublisher_box.getValue() == null ) errorMessage += "Не обнаружено издание книги!\n";
        if (bookYear_field.getText() == null || bookYear_field.getText().length() == 0  ) errorMessage += "Не обнаружен год выпуска книги!\n";
        if (!bookChapter_field.getText().matches("[\\sA-ZА-Яa-za-я]{3,20}") || bookChapter_field.getText() == null || bookChapter_field.getText().length() == 0) errorMessage += "Некорректное значение жанра книги!\n";
            else {
            try {
                Integer.parseInt(bookYear_field.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Некорректное значение года выпуски книги (Должен быть целочисленным)!\n";
            }
        }
        if (!bookYear_field.getText().matches("[\\d0-9]{4}") || bookYear_field.getText() == null) errorMessage += "Год выпуска введен некорректно! \n";
        }catch (Exception e){
            System.out.println(e);
            errorMessage += "Пустое поле!";
        }
        if (errorMessage.length() == 0) return true;
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(editBookStage);
            alert.setTitle("Ошибочка");
            alert.setHeaderText("Пожалуйста, укажите корректные значения текстовых полей");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
    public void setLabels(BookEntity bookIn, int book_id) {


        this.book = bookIn;
        this.bookID = book_id;

        bookName_field.setText(book.getTitle());
        bookAuthor_box.setValue(book.getAuthor());
        bookPublisher_box.setValue(book.getPublishing());
        bookYear_field.setText(book.getYear());
        bookChapter_field.setText(book.getKing());


    }
    public boolean isOkClicked(){return okClicked;}
}
