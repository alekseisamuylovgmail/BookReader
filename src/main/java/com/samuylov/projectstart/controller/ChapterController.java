package com.samuylov.projectstart.controller;

import com.samuylov.projectstart.dto.ChapterDto;
import com.samuylov.projectstart.service.ChapterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/book/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/list/{bookId}")
    public ResponseEntity getAllChaptersByBookId(@PathVariable final Long bookId) {
        return new ResponseEntity<>(chapterService.getAllByBookId(bookId), HttpStatus.FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity createChapter(@RequestBody final ChapterDto chapterDto) {
        chapterService.createChapter(chapterDto);
        return new ResponseEntity<>("Chapter created.", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{bookId}/{chapterId}")
    public ResponseEntity deleteChapter(@PathVariable final Long bookId, @PathVariable final Long chapterId) {
        chapterService.deleteChapter(bookId, chapterId);
        return new ResponseEntity<>("Chapter deleted.", HttpStatus.OK);
    }

    @GetMapping("/chapter/{bookId}/{chapterId}")
    public ResponseEntity getChapterByBookIdAndChapterId(@PathVariable final Long bookId, @PathVariable final Long chapterId) {
        ChapterDto chapterDto = chapterService.getChapterByBookIdAndNumber(bookId, chapterId);
        if (chapterDto != null) {
            return new ResponseEntity<>(chapterDto, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Chapter not found.", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{chapterId}")
    public ResponseEntity updateChapter(@PathVariable final Long chapterId, @RequestBody final ChapterDto chapterDto) {
        if (chapterService.updateChapter(chapterId, chapterDto)) {
            return new ResponseEntity<>("Chapter updated.", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Chapter not found.", HttpStatus.NOT_MODIFIED);
    }

}