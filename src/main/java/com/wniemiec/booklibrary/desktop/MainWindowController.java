package com.wniemiec.booklibrary.desktop;

import com.sun.javafx.collections.ObservableSequentialListWrapper;
import com.wniemiec.booklibrary.business.book.BookRepository;
import com.wniemiec.booklibrary.business.book.BookSpecifications;
import com.wniemiec.booklibrary.business.reader.ReaderRepository;
import com.wniemiec.booklibrary.business.reader.ReaderSpecifications;
import com.wniemiec.booklibrary.desktop.book.AddEditBookController;
import com.wniemiec.booklibrary.desktop.book.BookModel;
import com.wniemiec.booklibrary.desktop.reader.AddEditReaderController;
import com.wniemiec.booklibrary.desktop.reader.ReaderModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.data.jpa.domain.Specifications;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainWindowController {

    private final BookRepository bookRepository = new BookRepository();
    private final ReaderRepository readerRepository = new ReaderRepository();

    @FXML
    private TextField bookSearchField;

    @FXML
    private TextField readerSearchField;

    @FXML
    private Button editBookButton;

    @FXML
    private Button editReaderButton;

    @FXML
    private TableView<BookModel> bookTable;

    @FXML
    private TableView<ReaderModel> readerTable;

    @FXML
    private void searchBooks(ActionEvent e) {
        String param = bookSearchField.getText();

        List<BookModel> models = bookRepository.findAll(Specifications
                .where(BookSpecifications.titleLike(param))
                .or(BookSpecifications.authorLike(param)))
                .stream()
                .map(BookModel::new)
                .collect(Collectors.toList());

        bookTable.setItems(new ObservableSequentialListWrapper<>(models));
    }

    @FXML
    private void searchReaders(ActionEvent e) {
        String param = readerSearchField.getText();
        BigDecimal pesel = BigDecimal.ZERO;

        if (param.matches("^[0-9]+$")) {
            pesel = BigDecimal.valueOf(Long.valueOf(param));
        }

        List<ReaderModel> models = readerRepository.findAll(Specifications
                .where(ReaderSpecifications.nameLike(param))
                .or(ReaderSpecifications.surnameLike(param))
                .or(ReaderSpecifications.peselEqual(pesel)))
                .stream()
                .map(ReaderModel::new)
                .collect(Collectors.toList());

        readerTable.setItems(new ObservableSequentialListWrapper<>(models));
    }

    @FXML
    private void addEditBook(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("book/add_edit_book_window.fxml"));
        Parent load = loader.load();

        Scene scene = new Scene(load);

        if (editBookButton.equals(e.getSource())) {
            BookModel bookModel = bookTable.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(bookModel)) {
                Long id = bookModel.getId();
                loader.<AddEditBookController>getController().loadBookToUpdate(id);
            }
        }

        stage.setScene(scene);
        stage.setTitle("Add Book");
        stage.show();
    }

    @FXML
    private void addEditReader(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reader/add_edit_reader_window.fxml"));
        Parent load = loader.load();

        Scene scene = new Scene(load);

        if (editReaderButton.equals(e.getSource())) {
            ReaderModel readerModel = readerTable.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(readerModel)) {
                Long id = readerModel.getId();
                loader.<AddEditReaderController>getController().loadReaderToUpdate(id);
            }
        }

        stage.setScene(scene);
        stage.setTitle("Add Reader");
        stage.show();
    }

    @FXML
    private void deleteBook() throws IOException {
        BookModel bookModel = bookTable.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(bookModel)) {
            Long bookId = bookModel.getId();
            bookRepository.delete(bookId);
        }
    }

    @FXML
    private void deleteReader() throws IOException {
        ReaderModel readerModel = readerTable.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(readerModel)) {
            Long readerId = readerModel.getId();
            readerRepository.delete(readerId);
        }
    }

}
