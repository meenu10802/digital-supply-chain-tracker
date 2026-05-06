package com.example.item_service.service;

import com.example.item_service.dto.CreateItemRequestDto;
import com.example.item_service.dto.ItemResponseDto;
import com.example.item_service.dto.UpdateItemRequestDto;
import com.example.item_service.entity.Item;
import com.example.item_service.enums.ItemCategory;
import com.example.item_service.exception.ResourceNotFoundException;
import com.example.item_service.mapper.ItemMapper;
import com.example.item_service.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public ItemResponseDto createItem(CreateItemRequestDto dto, Long supplierId) {
        log.info("Creating item with SKU: {} for supplierId: {}", dto.getSku(), supplierId);
        Item item = itemMapper.toEntity(dto);
        item.setSupplierId(supplierId);
        Item saved = itemRepository.save(item);
        log.info("Item created with id: {}", saved.getId());
        return itemMapper.toResponseDto(saved);
    }

    public Page<ItemResponseDto> getAllItems(Pageable pageable) {
        log.debug("Fetching all active items");
        return itemRepository.findByIsActiveTrue(pageable)
                .map(itemMapper::toResponseDto);
    }

    public ItemResponseDto getItemById(Long id) {
        log.debug("Fetching item by id: {}", id);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return itemMapper.toResponseDto(item);
    }

    @Transactional
    public ItemResponseDto updateItem(Long id, UpdateItemRequestDto dto, Long requesterId, String requesterRoles) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        // Only owner supplier or ADMIN can update
        if (!item.getSupplierId().equals(requesterId) && !requesterRoles.contains("ADMIN")) {
            throw new SecurityException("You are not authorized to update this item");
        }

        itemMapper.updateEntityFromDto(dto, item);
        Item updated = itemRepository.save(item);
        log.info("Item updated with id: {}", updated.getId());
        return itemMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteItem(Long id, Long requesterId, String requesterRoles) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        if (!item.getSupplierId().equals(requesterId) && !requesterRoles.contains("ADMIN")) {
            throw new SecurityException("You are not authorized to delete this item");
        }

        item.setIsActive(false);
        itemRepository.save(item);
        log.info("Item soft-deleted with id: {}", id);
    }

    public List<ItemResponseDto> getItemsBySupplier(Long supplierId) {
        log.debug("Fetching items for supplierId: {}", supplierId);
        return itemRepository.findActiveItemsBySupplier(supplierId)
                .stream()
                .map(itemMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Page<ItemResponseDto> getItemsByCategory(ItemCategory category, Pageable pageable) {
        log.debug("Fetching items by category: {}", category);
        return itemRepository.findByCategoryActive(category, pageable)
                .map(itemMapper::toResponseDto);
    }
}
