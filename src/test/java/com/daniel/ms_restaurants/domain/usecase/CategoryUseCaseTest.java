package com.daniel.ms_restaurants.domain.usecase;

import com.daniel.ms_restaurants.TestConstants;
import com.daniel.ms_restaurants.domain.exception.CategoryNotFoundException;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.spi.ICategoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryUseCaseTest {
    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        // Arrange
        when(categoryPersistencePort.getCategoryById(TestConstants.CATEGORY_ID)).thenReturn(Optional.of(TestConstants.CATEGORY));

        // Act
        Category result = categoryUseCase.getCategoryById(TestConstants.CATEGORY_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TestConstants.CATEGORY_ID, result.getId());
        assertEquals(TestConstants.CATEGORY_NAME, result.getName());
    }

    @Test
    void getCategoryById_ShouldThrowCategoryNotFoundException_WhenCategoryDoesNotExist() {
        // Arrange
        when(categoryPersistencePort.getCategoryById(TestConstants.CATEGORY_ID)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryUseCase.getCategoryById(TestConstants.CATEGORY_ID)
        );

        assertEquals(ErrorMessages.CATEGORY_NOT_FOUND.getMessage(TestConstants.CATEGORY_ID), exception.getMessage());
    }
}