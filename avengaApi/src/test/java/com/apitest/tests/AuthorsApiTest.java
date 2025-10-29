package com.apitest.tests;

import com.apitest.base.BaseTest;
import com.apitest.pojo.Author;
import com.apitest.utils.RestUtils;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class AuthorsApiTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(AuthorsApiTest.class);
    private static Integer createdAuthorId;

    @Test(priority = 1)
    public void testGetAllAuthors() {
        // Arrange
        logger.info("Testing GET all authors");
        
        // Act
        Response response = RestUtils.get("/api/v1/Authors", 200, "schemas/authors-array-schema.json");

        // Assert
        List<Author> authors = response.jsonPath().getList("", Author.class);
        Assert.assertNotNull(authors);
        Assert.assertFalse(authors.isEmpty());
        logger.info("Retrieved {} authors", authors.size());
    }

    @Test(priority = 2)
    public void testGetAuthorById() {
        // Arrange
        logger.info("Testing GET author by ID");
        
        // Act
        Response response = RestUtils.get("/api/v1/Authors/1", 200, "schemas/author-schema.json");

        // Assert
        Author author = response.as(Author.class);
        Assert.assertNotNull(author);
        Assert.assertNotNull(author.getId());
        Assert.assertNotNull(author.getFirstName());
        logger.info("Retrieved author: {} {}", author.getFirstName(), author.getLastName());
    }

    @Test(priority = 3)
    public void testCreateAuthor() {
        // Arrange
        logger.info("Creating new author");
        Author newAuthor = Author.builder()
                .idBook(1)
                .firstName("John")
                .lastName("Doe")
                .build();

        // Act
        Response response = RestUtils.post("/api/v1/Authors", newAuthor, 200, "schemas/author-schema.json");

        // Assert
        Author createdAuthor = response.as(Author.class);
        createdAuthorId = createdAuthor.getId();
        
        Assert.assertNotNull(createdAuthor.getId());
        Assert.assertEquals(createdAuthor.getFirstName(), newAuthor.getFirstName());
        logger.info("Created author with ID: {}", createdAuthor.getId());
    }

    @Test(priority = 4, dependsOnMethods = "testCreateAuthor")
    public void testUpdateAuthor() {
        logger.info("Updating author");
        if (createdAuthorId == null) {
            Assert.fail("Cannot update author - created author ID is null");
        }

        Author updatedAuthor = Author.builder()
                .id(createdAuthorId)
                .idBook(1)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        Response response = RestUtils.put("/api/v1/Authors/" + createdAuthorId, updatedAuthor, 200, null);

        Author authorAfterUpdate = response.as(Author.class);
        Assert.assertEquals(authorAfterUpdate.getFirstName(), updatedAuthor.getFirstName());
        
        logger.info("Successfully updated author with ID: {}", createdAuthorId);
    }

    @Test(priority = 5, dependsOnMethods = "testUpdateAuthor")
    public void testDeleteAuthor() {
        logger.info("Deleting author");
        if (createdAuthorId == null) {
            Assert.fail("Cannot delete author - created author ID is null");
        }

        RestUtils.delete("/api/v1/Authors/" + createdAuthorId, 200, null);
        RestUtils.get("/api/v1/Authors/" + createdAuthorId, 404, null);
        logger.info("Deleted author with ID: {}", createdAuthorId);
    }

    @Test(priority = 6)
    public void testGetAuthorWithInvalidId() {
        logger.info("Testing with negative ID");
        RestUtils.get("/api/v1/Authors/-1", 404, null);
    }

    @Test(priority = 7)
    public void testGetNonExistentAuthor() {
        logger.info("Testing non-existent author");
        RestUtils.get("/api/v1/Authors/99999", 404, null);
    }

    @Test(priority = 8)
    public void testUpdateNonExistentAuthor() {
        logger.info("Testing update non-existent author");
        Author authorToUpdate = Author.builder()
                .idBook(1)
                .firstName("Non-existent")
                .lastName("Author")
                .build();

        RestUtils.put("/api/v1/Authors/99999", authorToUpdate, 200, null);
    }

    @Test(priority = 9)
    public void testDeleteNonExistentAuthor() {
        logger.info("Testing delete non-existent author");
        RestUtils.delete("/api/v1/Authors/99999", 200, null);
    }

    @Test(priority = 10)
    public void testCreateAuthorWithMinimalData() {
        logger.info("Testing with minimal data");
        Author minimalAuthor = Author.builder()
                .firstName("Minimal")
                .lastName("Author")
                .build();

        Response response = RestUtils.post("/api/v1/Authors", minimalAuthor, 200, "schemas/author-schema.json");

        Author createdAuthor = response.as(Author.class);
        Assert.assertNotNull(createdAuthor.getId());
        Assert.assertEquals(createdAuthor.getFirstName(), minimalAuthor.getFirstName());
        
        RestUtils.delete("/api/v1/Authors/" + createdAuthor.getId(), 200, null);
    }

    @Test(priority = 11)
    public void testCreateAuthorWithSpecialCharacters() {
        logger.info("Testing special characters");
        Author specialAuthor = Author.builder()
                .idBook(1)
                .firstName("José")
                .lastName("O'Connor-Smith")
                .build();

        Response response = RestUtils.post("/api/v1/Authors", specialAuthor, 200, "schemas/author-schema.json");

        Author createdAuthor = response.as(Author.class);
        Assert.assertNotNull(createdAuthor.getId());
        RestUtils.delete("/api/v1/Authors/" + createdAuthor.getId(), 200, null);
    }
}

