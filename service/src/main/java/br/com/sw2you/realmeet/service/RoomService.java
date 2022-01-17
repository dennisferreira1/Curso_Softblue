package br.com.sw2you.realmeet.service;

import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomValidator roomValidator;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, RoomValidator roomValidator) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomValidator = roomValidator;
    }

    public RoomDTO getRoom(Long id) {
        Room room = getActiveRoomOrThrow(id);
        return roomMapper.roomToRoomDto(room);
    }

    public RoomDTO createRoom(CreateRoomDTO createRoomDTO) {
        roomValidator.validate(createRoomDTO);
        var room = roomMapper.createRoomDTOtoRoom(createRoomDTO);
        roomRepository.save(room);
        return roomMapper.roomToRoomDto(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        getActiveRoomOrThrow(id);
        roomRepository.deactivate(id);
    }

    @Transactional
    public void updateRoom(Long id, UpdateRoomDTO updateRoomDTO) {
        roomValidator.validate(id, updateRoomDTO);
        getActiveRoomOrThrow(id);
        roomRepository.updateRoom(id, updateRoomDTO.getName(), updateRoomDTO.getSeats());
    }

    private Room getActiveRoomOrThrow(Long id) {
        requireNonNull(id);
        return roomRepository
            .findByIdAndActive(id, true)
            .orElseThrow(() -> new RoomNotFoundException("Room com id " + id + " não encontrada!"));
    }
}
