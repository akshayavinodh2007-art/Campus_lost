package com.campusfind.campusfind.repository;

import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.entity.LostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostReportRepository extends JpaRepository<LostReport, Long> {
    List<LostReport> findByCategory(Category category);
}
