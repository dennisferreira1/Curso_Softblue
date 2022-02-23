package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.AllocationCannotBeDeletedException;
import br.com.sw2you.realmeet.exception.AllocationCannotBeUpdatedException;
import br.com.sw2you.realmeet.exception.AllocationNotFoundException;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final AllocationMapper allocationMapper;
    private final RoomRepository roomRepository;
    private final AllocationValidator allocationValidator;

    public AllocationService(
        AllocationRepository allocationRepository,
        AllocationMapper allocationMapper,
        RoomRepository roomRepository,
        AllocationValidator allocationValidator
    ) {
        this.allocationRepository = allocationRepository;
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationValidator = allocationValidator;
    }

    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        var room = roomRepository
            .findById(createAllocationDTO.getRoomId())
            .orElseThrow(() -> new RoomNotFoundException("Room not found: " + createAllocationDTO.getRoomId()));
        allocationValidator.validate(createAllocationDTO);
        var allocation = allocationMapper.CreateAllocationDTOtoAllocation(createAllocationDTO, room);
        allocationRepository.save(allocation);
        return allocationMapper.allocationToAllocationDto(allocation);
    }

    public void deleteAllocation(Long id) {
        var allocation = getAllocationOrThrow(id);

        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeDeletedException(id);
        }

        allocationRepository.delete(allocation);
    }

    @Transactional
    public void updateAllocation(Long id, UpdateAllocationDTO updateAllocationDTO) {
        allocationValidator.validate(id, updateAllocationDTO);
        var allocation = getAllocationOrThrow(id);

        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeUpdatedException(id);
        }
        allocationRepository.updateAllocation(
            id,
            updateAllocationDTO.getSubject(),
            updateAllocationDTO.getStartAt(),
            updateAllocationDTO.getEndAt()
        );
    }

    private boolean isAllocationInThePast(Allocation allocation) {
        return allocation.getEndAt().isBefore(DateUtils.now());
    }

    private Allocation getAllocationOrThrow(Long id) {
        Objects.requireNonNull(id);
        return allocationRepository
            .findById(id)
            .orElseThrow(() -> new AllocationNotFoundException("Allocation " + id + "not found"));
    }
}
