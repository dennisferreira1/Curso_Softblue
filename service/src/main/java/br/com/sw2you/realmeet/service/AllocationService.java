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
import br.com.sw2you.realmeet.util.Constants;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.util.PageUtils;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final AllocationMapper allocationMapper;
    private final RoomRepository roomRepository;
    private final AllocationValidator allocationValidator;
    private final int maxLimit;
    private final NotificationEmailService notificationEmailService;

    public AllocationService(
        NotificationEmailService notificationEmailService,
        AllocationRepository allocationRepository,
        AllocationMapper allocationMapper,
        RoomRepository roomRepository,
        AllocationValidator allocationValidator,
        @Value(Constants.ALLOCATIONS_MAX_FILTER_LIMIT) int maxLimit
    ) {
        this.notificationEmailService = notificationEmailService;
        this.allocationRepository = allocationRepository;
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationValidator = allocationValidator;
        this.maxLimit = maxLimit;
    }

    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        var room = roomRepository
            .findById(createAllocationDTO.getRoomId())
            .orElseThrow(() -> new RoomNotFoundException("Room not found: " + createAllocationDTO.getRoomId()));
        allocationValidator.validate(createAllocationDTO);
        var allocation = allocationMapper.CreateAllocationDTOtoAllocation(createAllocationDTO, room);
        allocationRepository.save(allocation);
        notificationEmailService.notifyAllocationCreated(allocation);
        return allocationMapper.allocationToAllocationDto(allocation);
    }

    public void deleteAllocation(Long id) {
        var allocation = getAllocationOrThrow(id);

        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeDeletedException(id);
        }

        allocationRepository.delete(allocation);
        notificationEmailService.notifyAllocationDeleted(allocation);
    }

    @Transactional
    public void updateAllocation(Long allocationId, UpdateAllocationDTO updateAllocationDTO) {
        var allocation = getAllocationOrThrow(allocationId);
        allocationValidator.validate(allocationId, allocation.getRoom().getId(), updateAllocationDTO);

        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeUpdatedException(allocationId);
        }
        allocationRepository.updateAllocation(
            allocationId,
            updateAllocationDTO.getSubject(),
            updateAllocationDTO.getStartAt(),
            updateAllocationDTO.getEndAt()
        );
        notificationEmailService.notifyAllocationUpdated(getAllocationOrThrow(allocationId));
    }

    public List<AllocationDTO> listAllocations(
        String employeeEmail,
        Long roomId,
        LocalDate startAt,
        LocalDate endAt,
        String orderBy,
        Integer limit,
        Integer page
    ) {
        Pageable pageable = PageUtils.newPageable(page, limit, maxLimit, orderBy, Allocation.SORTABLE_FIELDS);

        var allocations = allocationRepository.findAllWithFilters(
            employeeEmail,
            roomId,
            Objects.isNull(startAt) ? null : startAt.atTime(LocalTime.MIN).atOffset(DateUtils.DEFAULT_TIMEZONE),
            Objects.isNull(endAt) ? null : endAt.atTime(LocalTime.MAX).atOffset(DateUtils.DEFAULT_TIMEZONE),
            pageable
        );
        return allocations
            .stream()
            .map(a -> allocationMapper.allocationToAllocationDto(a))
            .collect(Collectors.toList());
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
