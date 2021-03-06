package com.wniemiec.booklibrary.desktop;

import com.wniemiec.booklibrary.business.author.Author;
import com.wniemiec.booklibrary.business.book.Book;
import com.wniemiec.booklibrary.business.publisher.Publisher;
import com.wniemiec.booklibrary.business.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExampleQuery {
    private ExampleQuery(){}

    public static void run() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Author author = new Author();
        author.setName("Nicolas");
        author.setSurname("Sparks");
        author.setId((Long) session.save(author));

        Publisher publisher = new Publisher();
        publisher.setName("GREG");
        publisher.setId((Long) session.save(publisher));

        Book book = new Book();
        book.setTitle("Anioł stróż");
        book.setDescription("Julie zostaje wdową w wieku dwudziestu pięciu lat.");
        book.setISBN("029382");
        book.setPublisher(publisher);
        book.setAuthors(Stream.of(author).collect(Collectors.toSet()));
        session.save(book);

        book = new Book();
        book.setTitle("Bezpieczna przystań");
        book.setDescription("Super ksiazka");
        book.setISBN("029383");
        book.setAuthors(Stream.of(author).collect(Collectors.toSet()));
        session.save(book);
        book = new Book();

        book.setTitle("Dla ciebie wszystko");
        book.setDescription("Najlepsza ksiazka na swiecie");
        book.setISBN("029384");
        book.setPublisher(publisher);
        book.setAuthors(Stream.of(author).collect(Collectors.toSet()));
        session.save(book);

        session.flush();
        transaction.commit();
        session.close();
    }
}
