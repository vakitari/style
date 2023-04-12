package edu.bip.client.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.bip.client.Application;
import edu.bip.client.entity.AuthorEntity;
import edu.bip.client.entity.BookEntity;
import edu.bip.client.entity.PublishingEntity;
import edu.bip.client.utils.HTTPUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;



public class ApplicationController {

    public static String api = "http://localhost:28242/api/v1/";
    public static ObservableList<BookEntity> booksData = FXCollections.observableArrayList();
    public static ObservableList<AuthorEntity> authorsData = FXCollections.observableArrayList();
    public static ObservableList<PublishingEntity> publishersData = FXCollections.observableArrayList();
    static HTTPUtils http = new HTTPUtils();
    static Gson gson = new Gson();

    //книги:
    @FXML
    public TableView<BookEntity> tableBooks;

    @FXML
    private TableColumn<BookEntity, String> bookName;
    @FXML
    private TableColumn<BookEntity, String> bookAuthor;
    @FXML
    private TableColumn<BookEntity, String> bookPublisher;
    @FXML
    private TableColumn<BookEntity, String> bookYear;
    @FXML
    private TableColumn<BookEntity, String> bookChapter;

    //авторы:
    @FXML
    private TableView<AuthorEntity> tableAuthors;

    @FXML
    private TableColumn<AuthorEntity, String> authorLastname;
    @FXML
    private TableColumn<AuthorEntity, String> authorName;
    @FXML
    private TableColumn<AuthorEntity, String> authorSurname;

    //издатели:
    @FXML
    private TableView<PublishingEntity> tablePublishers;

    @FXML
    private TableColumn<PublishingEntity, String> publisherCity;
    @FXML
    private TableColumn<PublishingEntity, String> publisherPublisher;

    @FXML
    private void initialize() throws Exception {
        getDataBooks();
        getDataAuthors();
        getDataPublishers();
        updateTableBooks();
        updateTableAuthors();
        updateTablePublishers();
    }

    private void updateTableBooks() throws Exception {
        bookName.setCellValueFactory(new PropertyValueFactory<BookEntity, String>("title"));
        bookAuthor.setCellValueFactory(new PropertyValueFactory<BookEntity, String>("author"));
        bookPublisher.setCellValueFactory(new PropertyValueFactory<BookEntity, String>("publishing"));
        bookYear.setCellValueFactory(new PropertyValueFactory<BookEntity, String>("year"));
        bookChapter.setCellValueFactory(new PropertyValueFactory<BookEntity, String>("king"));
        tableBooks.setItems(booksData);
    }
    private void updateTableAuthors() throws Exception {
        authorName.setCellValueFactory(new PropertyValueFactory<AuthorEntity, String>("name"));
        authorSurname.setCellValueFactory(new PropertyValueFactory<AuthorEntity, String>("surName"));
        authorLastname.setCellValueFactory(new PropertyValueFactory<AuthorEntity, String>("lastName"));
        tableAuthors.setItems(authorsData);
    }
    private void updateTablePublishers() throws Exception {
        publisherPublisher.setCellValueFactory(new PropertyValueFactory<PublishingEntity, String>("publisher"));
        publisherCity.setCellValueFactory(new PropertyValueFactory<PublishingEntity, String>("city"));
        tablePublishers.setItems(publishersData);
    }

    @FXML
    private void click_newBook() throws IOException {
        try {
        BookEntity tempBook = new BookEntity();
        booksData.add(tempBook);
        Application.showPersonEditDialog(tempBook, booksData.size()-1);
            if(tempBook.getTitle() == null){
                booksData.remove(booksData.size() - 1);
            } else{
                Long id = addBook(tempBook);
                tempBook.setId(id);
            }
        }catch (Exception e){}
    }
    @FXML
    private void click_newPublisher() throws Exception {
        PublishingEntity tempPublishing = new PublishingEntity();
        publishersData.add(tempPublishing);
        Application.showPublisherAddDialog(tempPublishing, publishersData.size()-1);
        tablePublishers.getItems().clear();
        getDataPublishers();
    }

    @FXML
    private void click_newAuthor() throws Exception {
        AuthorEntity tempAuthor = new AuthorEntity();
        authorsData.add(tempAuthor);
        Application.showAuthorAddDialog(tempAuthor, authorsData.size()-1);
        tableAuthors.getItems().clear();
        getDataAuthors();
    }

    @FXML
    private void click_removeBook() throws IOException {
        BookEntity selectedPerson = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            System.out.println(selectedPerson.getId());
            System.out.println(http.delete(api+"book/delete/?id=", selectedPerson.getId()));
            booksData.remove(selectedPerson);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Отсутсвует выбраный пользователь!");
            alert.setContentText("Пожалуйста, выберите пользователя из таблицы");
            alert.showAndWait();
        }
    }
    @FXML
    private void click_removePublisher() throws IOException {
        PublishingEntity selectedPublisher = tablePublishers.getSelectionModel().getSelectedItem();
        if (selectedPublisher != null) {
            System.out.println(selectedPublisher.getId());
            System.out.println(http.delete(api+"publisher/delete/?id=", selectedPublisher.getId()));
            publishersData.remove(selectedPublisher);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Отсутсвует выбраный издатель!");
            alert.setContentText("Пожалуйста, выберите издателя из таблицы.");
            alert.showAndWait();
        }
    }

    @FXML
    private void click_removeAuthor() throws IOException {
        AuthorEntity selectedAuthor = tableAuthors.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null) {
            System.out.println(selectedAuthor.getId());
            System.out.println(http.delete(api+"author/delete/?id=", selectedAuthor.getId()));
            authorsData.remove(selectedAuthor);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Отсутсвует выбраный автор!");
            alert.setContentText("Пожалуйста, выберите автора из таблицы.");
            alert.showAndWait();
        }
    }

    @FXML
    private void click_editBook() throws IOException {
        BookEntity selectedPerson = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            Application.showPersonEditDialog(selectedPerson, booksData.indexOf(selectedPerson));
            http.put(api + "book/update", gson.toJson(selectedPerson).toString());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Отсутсвует выбраная книга!");
            alert.setContentText("Пожалуйста, выберите книгу из таблицы.");
            alert.showAndWait();
        }
    }
    @FXML
    private void click_editPublisher() {
        PublishingEntity selectedPublisher = tablePublishers.getSelectionModel().getSelectedItem();
        if (selectedPublisher != null)
            Application.showPublisherEditDialog(selectedPublisher, publishersData.indexOf(selectedPublisher));
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Отсутсвует выбраный издатель!");
            alert.setContentText("Пожалуйста, выберите издателя из таблицы.");
            alert.showAndWait();
        }
    }

    @FXML
    private void click_editAuthor() {
        AuthorEntity selectedAuthor = tableAuthors.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null)
            Application.showAuthorEditDialog(selectedAuthor, authorsData.indexOf(selectedAuthor));
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Отсутсвует выбраный издатель!");
            alert.setContentText("Пожалуйста, выберите издателя из таблицы.");
            alert.showAndWait();
        }
    }

    public static void getDataBooks() throws Exception {
        String res = http.get(api,"book/all");
        System.out.println(res);

        JsonObject base = gson.fromJson(res, JsonObject.class);
        JsonArray dataArr = base.getAsJsonArray("data");

        for (int i = 0; i < dataArr.size(); i++) {
            BookEntity newBook = gson.fromJson(dataArr.get(i).toString(), BookEntity.class);
            booksData.add(newBook);
        }
    }

    public static void getDataAuthors() throws Exception {
        String res = http.get(api,"author/all");
        System.out.println(res);

        JsonObject base = gson.fromJson(res, JsonObject.class);
        JsonArray dataArr = base.getAsJsonArray("data");

        for (int i = 0; i < dataArr.size(); i++) {
            AuthorEntity newAuthor = gson.fromJson(dataArr.get(i).toString(), AuthorEntity.class);
            authorsData.add(newAuthor);
        }
    }

    public static void getDataPublishers() throws Exception {
        String res = http.get(api,"publisher/all");
        System.out.println(res);

        JsonObject base = gson.fromJson(res, JsonObject.class);
        JsonArray dataArr = base.getAsJsonArray("data");
        for (int i = 0; i < dataArr.size(); i++) {
            PublishingEntity newPublisher = gson.fromJson(dataArr.get(i).toString(), PublishingEntity.class);
            publishersData.add(newPublisher);
        }
    }



    public static Long addBook(BookEntity book) throws IOException {
        System.out.println(book.toString());
        book.setId(null);
        String res = http.post(api+"book/add", gson.toJson(book).toString());
        JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
        Long tempId = jsonObject.getAsJsonObject("data").get("id").getAsLong();
        return tempId;
    }

}