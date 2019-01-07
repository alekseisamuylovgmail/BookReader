package com.samuylov.projectstart.service;

import com.samuylov.projectstart.converter.BookConverter;
import com.samuylov.projectstart.dto.BookDto;
import com.samuylov.projectstart.dto.ChapterDto;
import com.samuylov.projectstart.dto.CommentDto;
import com.samuylov.projectstart.dto.ReviewDto;
import com.samuylov.projectstart.entity.BookEntity;
import com.samuylov.projectstart.enumeration.SortType;
import com.samuylov.projectstart.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final ChapterService chapterService;
    private final Map<SortType, Sort> sortMap;

    @Autowired
    public BookService(final BookRepository bookRepository, final BookConverter bookConverter,
                       final CommentService commentService, final ReviewService reviewService,
                       @Lazy final ChapterService chapterService, final Map<SortType, Sort> sortMap) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.chapterService = chapterService;
        this.sortMap = sortMap;
    }

    public void createBook(final BookDto bookDto) {
        bookDto.setRating(0);
        bookRepository.save(bookConverter.convertToEntity(bookDto));
    }

    public List<BookDto> getBooksList() {
        return bookRepository.findAll().stream().map(bookConverter::convertToDto).collect(Collectors.toList());
    }

    public BookDto getBookById(final Long bookId) {
        final BookDto bookDto = bookConverter.convertToDto(bookRepository.getOne(bookId));

        if (bookDto == null) {
            return null;
        }

        final List<CommentDto> comments = commentService.getAllByBookId(bookId);
        final List<ReviewDto> reviews = reviewService.getAllByBookId(bookId);
        final List<ChapterDto> chapters = chapterService.getAllByBookId(bookId);
        bookDto.setComments(comments);
        bookDto.setReviews(reviews);
        bookDto.setChapters(chapters);

        return bookDto;
    }

    public boolean updateBook(final Long bookId, final BookDto bookDto) {
        final BookEntity oldBook = bookRepository.getOne(bookId);
        final BookEntity bookDbo = bookConverter.convertToEntity(bookDto);

        if (oldBook == null) {
            return false;
        }

        oldBook.setName(bookDbo.getName());
        oldBook.setDescription(bookDbo.getDescription());
        bookRepository.save(oldBook);

        return true;
    }

    public List<BookDto> getBooksList(final SortType sortType) {
        final Sort sort = sortMap.get(sortType);
        return bookRepository.findAll(sort).stream().map(bookConverter::convertToDto).collect(Collectors.toList());
    }

    public boolean incrementRating(final Long bookId) {
        final BookDto bookDto = bookConverter.convertToDto(bookRepository.getOne(bookId));

        if (bookDto != null) {
            bookDto.setRating(bookDto.getRating() + 1);
            bookRepository.save(bookConverter.convertToEntity(bookDto));
            return true;
        }
        return false;
    }

    public boolean decrementRating(final Long bookId) {
        final BookDto bookDto = bookConverter.convertToDto(bookRepository.getOne(bookId));

        if (bookDto != null) {
            bookDto.setRating(bookDto.getRating() - 1);
            bookRepository.save(bookConverter.convertToEntity(bookDto));
            return true;
        }
        return false;
    }

    public boolean isBookContains(final Long bookId) {
        final BookEntity bookEntity = bookRepository.getOne(bookId);
        return bookEntity != null;
    }

    public boolean deleteBook(final Long bookId) {
        final BookEntity bookEntity = bookRepository.getOne(bookId);
        if (bookEntity == null) {
            return false;
        }

        chapterService.deleteAllChaptersByBookId(bookId);
        commentService.deleteAllCommentsByBookId(bookId);
        reviewService.deleteAllReviewsByBookId(bookId);
        bookRepository.delete(bookEntity);

        return true;
    }
}
