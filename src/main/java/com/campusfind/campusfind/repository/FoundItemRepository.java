package com.campusfind.campusfind.repository;

import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.entity.FoundItem;
import com.campusfind.campusfind.entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    List<FoundItem> findByStatus(ItemStatus status);
    List<FoundItem> findByCategoryAndStatus(Category category, ItemStatus status);
    long countByStatus(ItemStatus status);
}
