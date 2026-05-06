package com.example.item_service.repository;

import com.example.item_service.entity.Item;
import com.example.item_service.enums.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.supplierId = :sid AND i.isActive = true")
    List<Item> findActiveItemsBySupplier(@Param("sid") Long supplierId);

    @Query("SELECT i FROM Item i WHERE i.category = :cat AND i.isActive = true ORDER BY i.createdAt DESC")
    Page<Item> findByCategoryActive(@Param("cat") ItemCategory category, Pageable pageable);

    Page<Item> findByIsActiveTrue(Pageable pageable);
}
