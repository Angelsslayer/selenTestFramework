package com.softserve.edu09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BookManagerTest {
    private BookManager bookManager;

    private static Stream<Arguments> provideYearAndExpectedCount() {
        return Stream.of(
                Arguments.of(2022, 2),
                Arguments.of(2021, 1),
                Arguments.of(2020, 0),
                Arguments.of(2023, 0)
        );
    }

    @BeforeEach
    void setUp() {
        bookManager = new BookManager();
        bookManager.addBook(new Book("The Great Adventure", "Alice Johnson", "Drama", 2022));
        bookManager.addBook(new Book("Space Odyssey", "Alice Johnson", "Fantastic", 2024));
        bookManager.addBook(new Book("Life's Journey", "Bob Smith", "Drama", 2021));
        bookManager.addBook(new Book("Science Explained", "Charlie Brown", "Science", 2019));
    }

    @Test
    @DisplayName("Add Book: Successfully adds a new unique book")
    void testAddBookPositive() {
        Book newBook = new Book("New Discoveries", "Diana Green", "Drama", 2024);
        bookManager.addBook(newBook);
        assertEquals(5, bookManager.size(), "The collection should contain 5 books after adding a new book");
    }

    @Test
    @DisplayName("Add Book: Throws exception when attempting to add null")
    void testAddBookNull() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.addBook(null),
                "Adding a null book should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Add Book: Throws exception when attempting to add a duplicate book")
    void testAddBookDuplicate() {
        Book duplicateBook = new Book("The Great Adventure", "Alice Johnson", "Drama", 2022);
        assertThrows(IllegalArgumentException.class, () -> bookManager.addBook(duplicateBook),
                "Adding a duplicate book should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("List All Authors: Returns a list of unique authors in the collection")
    void testListAllAuthors() {
        List<String> authors = bookManager.listOfAllAuthors();
        assertEquals(3, authors.size(), "The collection should contain 3 unique authors");
        assertTrue(authors.contains("Alice Johnson"), "Author list should contain 'Alice Johnson'");
        assertTrue(authors.contains("Bob Smith"), "Author list should contain 'Bob Smith'");
        assertTrue(authors.contains("Charlie Brown"), "Author list should contain 'Charlie Brown'");
    }

    @ParameterizedTest
    @CsvSource({
            "Drama, 2",
            "Fantastic, 1",
            "Science, 1"
    })
    @DisplayName("List Authors by Genre: Returns correct number of authors by genre")
    void testListAuthorsByGenre(String genre, int expected) {
        List<String> authors = bookManager.listAuthorsByGenre(genre);
        assertEquals(expected, authors.size(),
                String.format("The genre '%s' should have %d unique authors", genre, expected));
    }

    @ParameterizedTest(name = "{index}: for {0} => found {1} ")
    @MethodSource("provideYearAndExpectedCount")
    @DisplayName("List Authors By Year: Return correct number of Authors for the specified year")
    void testListAuthorsByYearFilterCheck(int year, int expectedSize) {
        bookManager.addBook(new Book("The new book", "Unknown Author", "Thriller", 2022));
        List<String> result = bookManager.listAuthorsByYear(year);
        assertEquals(expectedSize, result.size());
    }

    @Test
    @DisplayName("List Authors By Year: Return error for incorrect year")
    void testListAuthorsByYearThrowsError() {
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> bookManager.listAuthorsByYear(0));
        assertEquals("Year must be positive", result.getMessage());
    }

    @Test
    void testFindBookByAuthorPositiveSearch() {
        String searchAuthor = "Alice Johnson";
        Optional<Book> result = bookManager.findBookByAuthor(searchAuthor);
        assertTrue(result.isPresent(), "The book should be present");
        assertEquals(searchAuthor, result.get().getAuthor());
    }

    @Test
    void testFindBookByAuthorCaseInsensitiveSearch() {
        Optional<Book> result = bookManager.findBookByAuthor("alice johnSon");
        assertTrue(result.isPresent(), "The book should be present for case-insensitive search");
        assertEquals("Alice Johnson", result.get().getAuthor());
    }

    @Test
    void testFindBookByAuthorNegativeSearch() {
        Optional<Book> result = bookManager.findBookByAuthor("Unknown Author");
        assertTrue(result.isEmpty(), "No book should be found for an unknown author");
    }

    @Test
    void testFindBookByAuthorPartialMatchNotFound() {
        Optional<Book> result = bookManager.findBookByAuthor("Alice");
        assertTrue(result.isEmpty(), "The book should be present for partial match by author's name");
    }

    @Test
    void testFindBookByAuthorNullAuthor() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.findBookByAuthor(null));
    }

    @Test
    void testFindBookByAuthorEmptyAuthor() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.findBookByAuthor(""));
    }

    @ParameterizedTest(name = "{index}: for {0} => found {1} ")
    @MethodSource("provideYearAndExpectedCount")
    @DisplayName("Find Books By Year: Return correct number of Books for the specified year")
    void testFindBooksByYearFilterCheck(int year, int expectedSize) {
        bookManager.addBook(new Book("The new book", "Unknown Author", "Thriller", 2022));
        List<Book> result = bookManager.findBooksByYear(year);
        assertEquals(expectedSize, result.size());
    }

    @Test
    void testFindBooksByYearThrowsError() {
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> bookManager.findBooksByYear(0));
        assertEquals("Year must be positive", result.getMessage());
    }


    @ParameterizedTest(name = "{index}: genre {0} => found {1} ")
    @CsvSource({
            "Fantastic, 1",
            "Drama, 2",
            "TestGenre, 0"
    })
    void testFindBooksByGenrePositiveCheck(String genre, int resultSize) {
        List<Book> result = bookManager.findBooksByGenre(genre);
        assertEquals(resultSize, result.size());
    }

    @Test
    void testFindBooksByGenreCaseInsensitiveSearch() {
        List<Book> result = bookManager.findBooksByGenre("drAma");
        assertEquals(2, result.size(), "");
        assertTrue(result.stream().allMatch(x -> x.getGenre().equals("Drama")),
                "All of the found books should have genre 'Drama'");
    }

    @Test
    void testFindBooksByGenreTrowsException() {
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
                () -> bookManager.findBooksByGenre(" "));
        assertEquals("Genre cannot be null or empty", result.getMessage(),
                "Failed test with blank string");

        assertThrows(IllegalArgumentException.class, () -> bookManager.findBooksByGenre(null),
                "Failed test with null string");
    }

    @ParameterizedTest
    @CsvSource({
            "Alice Johnson, 2",
            "Bob Smith, 3",
            "Charlie, 4"
    })
    void testRemoveBooksByAuthorPositive(String author, int expectedRemainingBooks) {
        bookManager.removeBooksByAuthor(author);
        assertEquals(expectedRemainingBooks, bookManager.size(),
                "The number of remaining books after removing by author is incorrect");
    }

    @Test
    void testRemoveBooksByAuthorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.removeBooksByAuthor(null),
                "Expected exception for null author");
        assertThrows(IllegalArgumentException.class, () -> bookManager.removeBooksByAuthor(""),
                "Expected exception for empty author");
    }


    @ParameterizedTest(name = ("{index}: check with -> {0}"))
    @CsvSource({
            "Title",
            "title"
    })
    void testSortBooksByTitle(String testText) {
        List<Book> expected = bookManager.getBooks().stream()
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();

        bookManager.sortBooksByCriterion(testText);
        assertIterableEquals(expected, bookManager.getBooks(), "Failed to test with " + testText);
    }

    @ParameterizedTest(name = ("{index}: check with -> {0}"))
    @CsvSource({
            "Author",
            "author"
    })
    void testSortBooksByAuthor(String testText) {
        List<String> expectedAuthors = bookManager.getBooks().stream()
                .sorted(Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER))
                .map(Book::getAuthor)
                .toList();

        bookManager.sortBooksByCriterion(testText);
        List<String> actualAuthors = bookManager.getBooks().stream()
                .map(Book::getAuthor)
                .toList();

        assertLinesMatch(expectedAuthors, actualAuthors, "Failed to test with " + testText);
    }

    @ParameterizedTest(name = ("{index}: check with -> {0}"))
    @CsvSource({
            "Year",
            "year"
    })
    void testSortBooksByYear(String testText) {
        Integer[] expectedYears = bookManager.getBooks().stream()
                .sorted(Comparator.comparingInt(Book::getPublicationYear))
                .map(Book::getPublicationYear)
                .toArray(Integer[]::new);
        bookManager.sortBooksByCriterion(testText);
        Integer[] actualYears = bookManager.getBooks().stream()
                .map(Book::getPublicationYear)
                .toArray(Integer[]::new);
        assertArrayEquals(expectedYears, actualYears, "Failed to test with " + testText);
    }

    @Test
    void testSortBooksByCriterionThrowsExceptionForInvalidCriterion() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.sortBooksByCriterion("invalid"),
                "Expected exception for invalid criterion");
    }

    @Test
    void testMergeCollectionsAddsNonDuplicateBooks() {
        List<Book> otherBooks = List.of(
                new Book("New Adventure", "Author A", "Adventure", 2025),
                new Book("The Great Adventure", "Alice Johnson", "Drama", 2022)
        );
        bookManager.mergeCollections(otherBooks);

        assertEquals(5, bookManager.getBooks().size(),
                "The number of books after merging collections is incorrect");
        assertTrue(bookManager.getBooks().stream()
                        .anyMatch(b -> b.getTitle().equals("New Adventure") && b.getAuthor().equals("Author A")),
                "The new book was not added correctly");
    }

    @Test
    void testMergeCollectionsIgnoresNullBooks() {
        List<Book> otherBooks = new ArrayList<>();
        otherBooks.add(new Book("New Adventure", "Author A", "Adventure", 2025));
        otherBooks.add(null);
        bookManager.mergeCollections(otherBooks);

        assertEquals(5, bookManager.getBooks().size(),
                "The number of books after merging collections is incorrect (should ignore null)");
    }

    @Test
    void testMergeCollectionsThrowsExceptionForNullCollection() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.mergeCollections(null),
                "Expected exception for null collection");
    }

    @ParameterizedTest(name = ("{index}: check with <{0}> => expected <{1}>"))
    @CsvSource({
            "Drama, 2",
            "Science, 1",
            "Fantastic, 1",
            "Nonexistent, 0"
    })
    void testSubCollectionByGenre(String genre, int expectedCount) {
        List<Book> result = bookManager.subCollectionByGenre(genre);
        assertEquals(expectedCount, result.size(),
                String.format("The number of books in the sub-collection by genre '%s' is incorrect.", genre));
    }

    @Test
    void testSubCollectionByGenreThrowsExceptionForInvalidGenre() {
        assertThrows(IllegalArgumentException.class, () -> bookManager.subCollectionByGenre(null),
                "Expected exception for null genre");
        assertThrows(IllegalArgumentException.class, () -> bookManager.subCollectionByGenre(""),
                "Expected exception for empty genre");
    }

}