package fit.hutech.Huy.controllers;

import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.entities.Category;
import fit.hutech.Huy.services.BookService;
import fit.hutech.Huy.services.CategoryService;
import fit.hutech.Huy.viewmodels.BookGetVm;
import fit.hutech.Huy.viewmodels.BookPostVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApiControllerTest {

    private BookService bookService;
    private CategoryService categoryService;
    private ApiController apiController;

    @BeforeEach
    void setup() {
        bookService = Mockito.mock(BookService.class);
        categoryService = Mockito.mock(CategoryService.class);
        apiController = new ApiController(bookService, categoryService);
    }

    @Test
    void createBook_success() {
        BookPostVm vm = new BookPostVm("Title", "Author", 100.0, 10, 1L);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        ResponseEntity<?> response = apiController.createBook(vm, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookService).addBook(any(Book.class));
    }

    @Test
    void createBook_validationError() {
        BookPostVm vm = new BookPostVm("", "", -1.0, -1, null);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("title", "error")));

        ResponseEntity<?> response = apiController.createBook(vm, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(bookService, never()).addBook(any());
    }

    @Test
    void createBook_categoryNotFound() {
        BookPostVm vm = new BookPostVm("Title", "Author", 100.0, 10, 99L);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.getCategoryById(99L)).thenReturn(null);

        ResponseEntity<?> response = apiController.createBook(vm, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(bookService, never()).addBook(any());
    }

    @Test
    void updateBook_success() {
        Long id = 1L;
        BookPostVm vm = new BookPostVm("New Title", "New Author", 200.0, 20, 1L);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setCategory(new Category());
        when(bookService.getBookById(id)).thenReturn(Optional.of(existingBook));

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        ResponseEntity<?> response = apiController.updateBook(id, vm, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Title", existingBook.getTitle());
        verify(bookService).updateBook(existingBook);
    }

    @Test
    void updateBook_bookNotFound() {
        Long id = 99L;
        BookPostVm vm = new BookPostVm("Title", "Author", 100.0, 10, 1L);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(bookService.getBookById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = apiController.updateBook(id, vm, bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    void updateBook_categoryNotFound() {
        Long id = 1L;
        BookPostVm vm = new BookPostVm("Title", "Author", 100.0, 10, 99L);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        
        Book existingBook = new Book();
        when(bookService.getBookById(id)).thenReturn(Optional.of(existingBook));
        when(categoryService.getCategoryById(99L)).thenReturn(null);

        ResponseEntity<?> response = apiController.updateBook(id, vm, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
