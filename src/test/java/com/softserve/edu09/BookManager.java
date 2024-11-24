package com.softserve.edu09;
import java.util.*;
import java.util.stream.Collectors;

/**Task Description:
 * <ol>
 *   <li>Print List of Authors:
 *       Print the list of all authors in the collection to the console.
 *   </li>
 *   <li>List Authors by Genre:
 *       Print the list of authors who have written books in a given genre.
 *   </li>
 *   <li>List Authors by Publication Year:
 *       Print the list of authors whose books were published in a given year.
 *   </li>
 *   <li>Find Book by Author:
 *       Find a book in the collection written by a given author.
 *   </li>
 *   <li>Find Books by Publication Year:
 *       Find all books that were written in a given year.
 *   </li>
 *   <li>Find Books by Genre:
 *       Find all books that belong to a given genre.
 *   </li>
 *   <li>Remove Books by Author:
 *       Remove from the collection all books written by a given author.
 *   </li>
 *   <li>Sort Collection by Criterion:
 *       Sort the book collection by a given criterion (e.g., title, author, or year of publication).
 *   </li>
 *   <li>Merge Book Collections:
 *       Combine two book collections into one.
 *   </li>
 *   <li>Subcollection of Books by Genre:
 *       Create a subcollection of books from a given genre.
 *   </li>
 * </ol>
 *
 * Implementation Recommendations:
 * <ul>
 *   <li>Use ArrayList to store the book collection.</li>
 *   <li>For sorting, you can use Collections.sort() or stream methods with an appropriate comparator.</li>
 * </ul>
 */

public class BookManager {
    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        if (books.stream()
                .anyMatch(b -> b.getTitle().equalsIgnoreCase(book.getTitle())
                        && b.getAuthor().equalsIgnoreCase(book.getAuthor()))) {
            throw new IllegalArgumentException("Duplicate book entry");
        }
        books.add(book);
    }

    public int size() {
        return books.size();
    }

    public List<Book> getBooks(){
        return new ArrayList<>(books);
    }

    public List<String> listOfAllAuthors() {
        return books.stream()
                .map(Book::getAuthor)
                .distinct()
                .collect(Collectors.toList());
    }


    public List<String> listAuthorsByGenre(String genre) {
        if (genre == null || genre.isBlank()) throw new IllegalArgumentException("Genre cannot be null or empty");
        return books.stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .map(Book::getAuthor)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> listAuthorsByYear(int year) {
        if (year <= 0) throw new IllegalArgumentException("Year must be positive");
        return books.stream()
                .filter(b -> b.getPublicationYear() == year)
                .map(Book::getAuthor)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Book> findBookByAuthor(String author) {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author cannot be null or empty");
        return books.stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                .findFirst();
    }

    public List<Book> findBooksByYear(int year) {
        if (year <= 0) throw new IllegalArgumentException("Year must be positive");
        return books.stream()
                .filter(b -> b.getPublicationYear() == year)
                .collect(Collectors.toList());
    }


    public List<Book> findBooksByGenre(String genre) {
        if (genre == null || genre.isBlank()) throw new IllegalArgumentException("Genre cannot be null or empty");
        return books.stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public void removeBooksByAuthor(String author) {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author cannot be null or empty");
        books.removeIf(b -> b.getAuthor().equalsIgnoreCase(author));
    }

    public void sortBooksByCriterion(String criterion) {
        Comparator<Book> comparator = switch (criterion.toLowerCase()) {
            case "title" -> Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "author" -> Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
            case "year" -> Comparator.comparingInt(Book::getPublicationYear);
            default -> throw new IllegalArgumentException("Invalid criterion. Use 'title', 'author', or 'year'");
        };
        books.sort(comparator);
    }


    public void mergeCollections(List<Book> otherBooks) {
        if (otherBooks == null) throw new IllegalArgumentException("Other collection cannot be null");
        for (Book book : otherBooks) {
            if (book == null) continue;
            if (books.stream()
                    .noneMatch(b -> b.getTitle().equalsIgnoreCase(book.getTitle())
                            && b.getAuthor().equalsIgnoreCase(book.getAuthor()))) {
                books.add(book);
            }
        }
    }

    public List<Book> subCollectionByGenre(String genre) {
        if (genre == null || genre.isBlank()) throw new IllegalArgumentException("Genre cannot be null or empty");
        return books.stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
}