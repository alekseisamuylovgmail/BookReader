package com.samuylov.projectstart.repository;

import com.samuylov.projectstart.entity.BookDbo;
import com.samuylov.projectstart.entity.ChapterDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterDbo, Long> {
    ChapterDbo findByNumber(Long number);

    void deleteByBookIdAndNumber(Long bookId, Long number);

    ChapterDbo findByBookIdAndNumber(Long bookId, Long number);
}