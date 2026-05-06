package com.example.item_service.controller;

import com.example.item_service.dto.CreateItemRequestDto;
import com.example.item_service.dto.ItemResponseDto;
import com.example.item_service.dto.UpdateItemRequestDto;
import com.example.item_service.enums.ItemCategory;
import com.example.item_service.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Product/Item management for supply chain")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "Register a new item")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(
            @Valid @RequestBody CreateItemRequestDto dto,
            @RequestHeader("X-User-Id") Long supplierId,
            @RequestHeader("X-User-Roles") String roles) {

        // Only SUPPLIER or ADMIN can create
        if (!roles.contains("SUPPLIER") && !roles.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createItem(dto, supplierId));
    }

    @Operation(summary = "List all items (paginated)")
    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> getAllItems(Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    @Operation(summary = "Get item by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @Operation(summary = "Update item details")
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long id,
            @RequestBody UpdateItemRequestDto dto,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Roles") String roles) {
        return ResponseEntity.ok(itemService.updateItem(id, dto, userId, roles));
    }

    @Operation(summary = "Soft-delete item")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Roles") String roles) {
        itemService.deleteItem(id, userId, roles);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get items by supplier")
    @GetMapping("/supplier/{id}")
    public ResponseEntity<List<ItemResponseDto>> getItemsBySupplier(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemsBySupplier(id));
    }

    @Operation(summary = "Get items by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ItemResponseDto>> getItemsByCategory(
            @PathVariable ItemCategory category,
            Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category, pageable));
    }
}