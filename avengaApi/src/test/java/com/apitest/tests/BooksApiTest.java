package com.apitest.tests;

import com.apitest.base.BaseTest;
import com.apitest.helpers.TestDataGenerator;
import com.apitest.pojo.Book;
import com.apitest.utils.RestUtils;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class BooksApiTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(BooksApiTest.class);
    private static Integer createdBookId;

    @Test(priority = 1)
    public void testGetAllBooks() {
        // Arrange
        logger.info("Testing GET all books");

        // Act
        Response response = RestUtils.get("/api/v1/Books", 200, "schemas/books-array-schema.json");

        // Assert
        List<Book> books = response.jsonPath().getList("", Book.class);
        Assert.assertNotNull(books);
        Assert.assertFalse(books.isEmpty());
        logger.info("Retrieved {} books", books.size());
    }

    @Test(priority = 2)
    public void testGetBookById() {
        // Arrange
        logger.info("Testing GET book by ID");
        
        // Act
        Response response = RestUtils.get("/api/v1/Books/1", 200, "schemas/book-schema.json");

        // Assert
        Book book = response.as(Book.class);
        Assert.assertNotNull(book);
        Assert.assertNotNull(book.getId());
        Assert.assertNotNull(book.getTitle());
        logger.info("Retrieved book: {}", book.getTitle());
    }

    @Test(priority = 3)
    public void testCreateBook() {
        // Arrange
        logger.info("Creating new book");
        Book newBook = Book.builder()
                .title("The Art of Software Testing")
                .description("A comprehensive guide to software testing practices and methodologies")
                .pageCount(432)
                .excerpt("This book covers various testing techniques and best practices...")
                .publishDate("2024-01-15T00:00:00")
                .build();

        // Act
        Response response = RestUtils.post("/api/v1/Books", newBook, 200, "schemas/book-schema.json");

        // Assert
        Book createdBook = response.as(Book.class);
        createdBookId = createdBook.getId();
        
        Assert.assertNotNull(createdBook.getId());
        Assert.assertEquals(createdBook.getTitle(), newBook.getTitle());
        logger.info("Created book with ID: {}", createdBook.getId());
    }

    @Test(priority = 4, dependsOnMethods = "testCreateBook")
    public void testUpdateBook() {
        logger.info("Updating book");
        if (createdBookId == null) {
            Assert.fail("Cannot update book - created book ID is null");
        }

        Book updatedBook = Book.builder()
                .id(createdBookId)
                .title("The Art of Software Testing - Updated Edition")
                .description("An updated comprehensive guide to software testing")
                .pageCount(500)
                .excerpt("This updated edition includes new chapters on modern testing practices...")
                .publishDate("2024-02-20T00:00:00")
                .build();

        Response response = RestUtils.put("/api/v1/Books/" + createdBookId, updatedBook, 200, null);

        Book bookAfterUpdate = response.as(Book.class);
        Assert.assertEquals(bookAfterUpdate.getTitle(), updatedBook.getTitle());
        logger.info("Updated book with ID: {}", createdBookId);
    }

    @Test(priority = 5, dependsOnMethods = "testUpdateBook")
    public void testDeleteBook() {
        logger.info("Deleting book");
        if (createdBookId == null) {
            Assert.fail("Cannot delete book - created book ID is null");
        }

        RestUtils.delete("/api/v1/Books/" + createdBookId, 200, null);
        RestUtils.get("/api/v1/Books/" + createdBookId, 404, null);
        logger.info("Deleted book with ID: {}", createdBookId);
    }

    @Test(priority = 6)
    public void testGetBookWithInvalidId() {
        logger.info("Testing with negative ID");
        RestUtils.get("/api/v1/Books/-1", 404, null);
    }

    @Test(priority = 7)
    public void testGetNonExistentBook() {
        logger.info("Testing non-existent book");
        RestUtils.get("/api/v1/Books/99999", 404, null);
    }

    @Test(priority = 8)
    public void testUpdateNonExistentBook() {
        logger.info("Testing update non-existent book");
        Book bookToUpdate = Book.builder()
                .title("Non-existent Book")
                .description("This book doesn't exist")
                .pageCount(100)
                .publishDate("2024-01-01T00:00:00")
                .build();

        RestUtils.put("/api/v1/Books/99999", bookToUpdate, 200, null);
    }

    @Test(priority = 9)
    public void testDeleteNonExistentBook() {
        logger.info("Testing delete non-existent book");
        RestUtils.delete("/api/v1/Books/99999", 200, null);
    }

    @Test(priority = 10)
    public void testCreateBookWithMinimalData() {
        logger.info("Testing with minimal data");
        Book minimalBook = Book.builder()
                .title("Minimal Book")
                .build();

        Response response = RestUtils.post("/api/v1/Books", minimalBook, 200, "schemas/book-schema.json");

        Book createdBook = response.as(Book.class);
        Assert.assertNotNull(createdBook.getId());
        Assert.assertEquals(createdBook.getTitle(), minimalBook.getTitle());
        
        RestUtils.delete("/api/v1/Books/" + createdBook.getId(), 200, null);
    }

    @Test(priority = 11)
    public void testCreateBookWithSpecialCharacters() {
        logger.info("Testing special characters");
        Book specialBook = Book.builder()
                .title("Test Book: Special Characters! @#$%^&*()")
                .description("Testing with special characters")
                .pageCount(100)
                .build();

        Response response = RestUtils.post("/api/v1/Books", specialBook, 200, "schemas/book-schema.json");

        Book createdBook = response.as(Book.class);
        Assert.assertNotNull(createdBook.getId());
        RestUtils.delete("/api/v1/Books/" + createdBook.getId(), 200, null);
    }

    @Test(priority = 12)
    public void testUpdateBookWithOnlyTitle() {
        logger.info("Testing partial update");
        Book newBook = Book.builder()
                .title("Original Title")
                .description("Original Description")
                .pageCount(200)
                .build();

        Response createResponse = RestUtils.post("/api/v1/Books", newBook, 200, "schemas/book-schema.json");
        Book createdBook = createResponse.as(Book.class);
        Integer bookId = createdBook.getId();

        Book updatedBook = Book.builder()
                .id(bookId)
                .title("Updated Title")
                .build();

        Response updateResponse = RestUtils.put("/api/v1/Books/" + bookId, updatedBook, 200, null);
        Book updatedResult = updateResponse.as(Book.class);
        
        Assert.assertEquals(updatedResult.getTitle(), "Updated Title");
        
        RestUtils.delete("/api/v1/Books/" + bookId, 200, null);
    }
    
    @DataProvider(name = "bookTestData")
    public Object[][] getBookTestData() {
        return new Object[][] {
            {TestDataGenerator.createValidBook(), "Valid book"},
            {TestDataGenerator.createBookWithSpecialCharacters(), "Book with special characters"},
            {TestDataGenerator.createMinimalBook(), "Minimal book"}
        };
    }
    
    @Test(priority = 13, dataProvider = "bookTestData")
    public void testCreateBooksWithDataProvider(Book book, String testDescription) {
        // Arrange
        logger.info("Testing data-driven: " + testDescription);
        
        // Act
        Response response = RestUtils.post("/api/v1/Books", book, 200, "schemas/book-schema.json");
        
        // Assert
        Book createdBook = response.as(Book.class);
        Assert.assertNotNull(createdBook.getId());
        Assert.assertEquals(createdBook.getTitle(), book.getTitle());
        
        // Cleanup
        RestUtils.delete("/api/v1/Books/" + createdBook.getId(), 200, null);
    }
}

