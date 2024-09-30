package com.daniel.ms_restaurants;

import com.daniel.ms_restaurants.application.dto.RoleResponse;
import com.daniel.ms_restaurants.application.dto.UserResponse;
import com.daniel.ms_restaurants.domain.exception.ErrorMessages;
import com.daniel.ms_restaurants.domain.model.Category;
import com.daniel.ms_restaurants.domain.model.Restaurant;

public class TestConstants {

    // Datos para categorías
    public static final long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "Main Course";
    public static final String CATEGORY_DESCRIPTION = "Main course description";

    // Objeto de ejemplo para Category (puede reutilizarse en múltiples pruebas)
    public static final Category CATEGORY = new Category(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);

    // Otros datos relacionados que puedan ser útiles para las pruebas de integración u otros casos de uso
    // Por ejemplo, datos para platos u órdenes
    public static final long DISH_ID = 1L;
    public static final String DISH_NAME = "Spaghetti";
    public static final int DISH_PRICE = 12;
    public static final String DISH_DESCRIPTION = "Delicious spaghetti with tomato sauce";



    // User constants
    public static final Long OWNER_ID = 1L;
    public static final Long NON_EXISTENT_OWNER_ID = 999L;
    public static final String OWNER_ROLE_NAME = "OWNER";
    public static final String OWNER_ROLE_VALUE = "ROLE_OWNER";
    public static final String NON_OWNER_ROLE_NAME = "CLIENT";

    // User details constants
    public static final String USER_NAME = "John";
    public static final String USER_LAST_NAME = "Doe";
    public static final String USER_EMAIL = "john.doe@example.com";
    public static final String USER_PASSWORD = "password123";
    public static final String USER_DOCUMENT_NUMBER = "123456789";
    public static final String USER_PHONE = "1234567890";


    // Restaurant constants
    public static final Long RESTAURANT_ID = 10L;
    public static final String RESTAURANT_NAME = "Test Restaurant";

    public static final int PAGE_SIZE = 10;  // Tamaño de página, número de ítems por página
    public static final int PAGE_NUMBER = 0; // Primer página

    public static final String OWNER_EMAIL = "owner@example.com";

    public static final long EMPLOYEE_ID = 101L;
    public static final String EMPLOYEE_NAME = "John";
    public static final String EMPLOYEE_LAST_NAME = "Doe";
    public static final String EMPLOYEE_DOCUMENT_NUMBER = "1234567890";
    public static final String EMPLOYEE_PHONE = "+1234567890";
    public static final String EMPLOYEE_EMAIL = "employee@example.com";
    public static final String EMPLOYEE_PASSWORD = "password123";
    public static final long EMPLOYEE_ROLE_ID = 2L;
    public static final String EMPLOYEE_ROLE_NAME = "ROLE_EMPLOYEE";

    // Error Messages


}
