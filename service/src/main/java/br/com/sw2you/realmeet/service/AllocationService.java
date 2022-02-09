package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import org.springframework.stereotype.Service;

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
}
