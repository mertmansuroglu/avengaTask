package com.apitest.helpers;

import com.apitest.pojo.Author;
import com.apitest.pojo.Book;
import com.github.javafaker.Faker;

public class TestDataGenerator {
    
    private static final Faker faker = new Faker();
    public static Book createValidBook() {
        java.time.LocalDate publishDate = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS)
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        
        return Book.builder()
                .title(faker.book().title())
                .description(faker.lorem().sentence())
                .pageCount(faker.number().numberBetween(50, 1000))
                .excerpt(faker.lorem().paragraph())
                .publishDate(publishDate.atStartOfDay().toString())
                .build();
    }
    
    public static Author createValidAuthor() {
        return Author.builder()
                .idBook(faker.number().numberBetween(1, 100))
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .build();
    }
    
    public static Book createBookWithSpecialCharacters() {
        return Book.builder()
                .title("Test Book: Special Chars! @#$%^&*()")
                .description("Description with special chars")
                .pageCount(faker.number().numberBetween(50, 1000))
                .build();
    }
    
    public static Book createMinimalBook() {
        return Book.builder()
                .title(faker.book().title())
                .build();
    }
}

