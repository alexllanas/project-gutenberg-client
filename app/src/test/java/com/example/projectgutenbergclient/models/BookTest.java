package com.example.projectgutenbergclient.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookTest {

    /*
       Compare two equal Notes
    */
    @Test
    void areBooksEqual_identicalProperties_returnTrue() {
        // Arrange
        List<Author> authors1 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        List<Author> authors2 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        ArrayList<String> subjects1 = new ArrayList<>();
        subjects1.add("horror");
        subjects1.add("mystery");
        subjects1.add("comedy");

        ArrayList<String> subjects2 = new ArrayList<>();
        subjects2.add("horror");
        subjects2.add("mystery");
        subjects2.add("comedy");

        Formats formats1 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");
        Formats formats2 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");

        Book book1 = new Book(123, "book title", authors1, subjects1, formats1);
        Book book2 = new Book(123, "book title", authors2, subjects2, formats2);

        // Act

        // Assert
        assertEquals(book1, book2);
        System.out.println("The Books are equal!");
    }


    /*
       Compare books with different IDs
    */
    @Test
    void areBooksEqual_differentIds_returnFalse() {
        // Arrange
        List<Author> authors1 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        List<Author> authors2 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));

        ArrayList<String> subjects1 = new ArrayList<>();
        subjects1.add("horror");
        subjects1.add("mystery");
        subjects1.add("comedy");
        ArrayList<String> subjects2 = new ArrayList<>();
        subjects2.add("horror");
        subjects2.add("mystery");
        subjects2.add("comedy");

        Formats formats1 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");
        Formats formats2 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");

        Book book1 = new Book(123, "book title", authors1, subjects1, formats1);
        Book book2 = new Book(1000, "book title", authors2, subjects2, formats2);

        // Act

        // Assert
        assertNotEquals(book1, book2);
        System.out.println("The Books are not equal!");
    }

    /*
      Compare books with different titles
   */
    @Test
    void areBooksEqual_differentTitles_returnFalse() {
        // Arrange
        List<Author> authors1 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        List<Author> authors2 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));

        ArrayList<String> subjects1 = new ArrayList<>();
        subjects1.add("horror");
        subjects1.add("mystery");
        subjects1.add("comedy");
        ArrayList<String> subjects2 = new ArrayList<>();
        subjects2.add("horror");
        subjects2.add("mystery");
        subjects2.add("comedy");

        Formats formats1 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");
        Formats formats2 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");

        Book book1 = new Book(123, "Alice in Wonderland", authors1, subjects1, formats1);
        Book book2 = new Book(123, "Faust", authors2, subjects2, formats2);

        // Act

        // Assert
        assertNotEquals(book1, book2);
        System.out.println("The Books have different titles!");
    }

    /*
     Compare books with different authors
  */
    @Test
    void areBooksEqual_differentAuthors_returnFalse() {
        List<Author> authors1 = Arrays.asList(new Author("Dickens"), new Author("Tolkien"));
        List<Author> authors2 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));

        ArrayList<String> subjects1 = new ArrayList<>();
        subjects1.add("horror");
        subjects1.add("mystery");
        subjects1.add("comedy");
        ArrayList<String> subjects2 = new ArrayList<>();
        subjects2.add("horror");
        subjects2.add("mystery");
        subjects2.add("comedy");

        Formats formats1 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");
        Formats formats2 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");

        Book book1 = new Book(123, "book title", authors1, subjects1, formats1);
        Book book2 = new Book(123, "book title", authors2, subjects2, formats2);

        // Act

        // Assert
        assertNotEquals(book1, book2);
        System.out.println("The Books have different authors!");
    }

    /*
     Compare books with different subjects
  */
    @Test
    void areBooksEqual_differentSubjects_returnFalse() {
        List<Author> authors1 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        List<Author> authors2 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        
        ArrayList<String> subjects1 = new ArrayList<>();
        subjects1.add("horror");
        subjects1.add("mystery");
        subjects1.add("comedy");
        ArrayList<String> subjects2 = new ArrayList<>();
        subjects2.add("crime");
        subjects2.add("mystery");
        subjects2.add("drama");

        Formats formats1 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");
        Formats formats2 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");

        Book book1 = new Book(123, "book title", authors1, subjects1, formats1);
        Book book2 = new Book(123, "book title", authors2, subjects2, formats2);

        // Act

        // Assert
        assertNotEquals(book1, book2);
        System.out.println("The Books have different subjects!");
    }

    /*
     Compare books with different Formats
  */
    @Test
    void areBooksEqual_differentFormats_returnFalse() {
        List<Author> authors1 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));
        List<Author> authors2 = Arrays.asList(new Author("Goethe"), new Author("Tolkien"));

        ArrayList<String> subjects1 = new ArrayList<>();
        subjects1.add("horror");
        subjects1.add("mystery");
        subjects1.add("comedy");
        ArrayList<String> subjects2 = new ArrayList<>();
        subjects2.add("horror");
        subjects2.add("mystery");
        subjects2.add("comedy");

        Formats formats1 = new Formats("image", "epub", "rdf", "html", "ebook", "zip", "utf8", "ascii");
        Formats formats2 = new Formats("apple", "epub", "rdf", "rhubarb", "ebook", "zip", "peas&carrots", "ascii");

        Book book1 = new Book(123, "Faust", authors1, subjects1, formats1);
        Book book2 = new Book(123, "Faust", authors2, subjects2, formats2);

        // Act

        // Assert
        assertNotEquals(book1, book2);
        System.out.println("The Books have different formats!");
    }
}
