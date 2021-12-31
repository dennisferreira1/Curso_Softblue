package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilderDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.service.RoomService;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.MapperUtils;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import br.com.sw2you.realmeet.validator.RoomValidator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class RoomServiceUnitTest extends BaseUnitTest {
    private RoomService victim;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomValidator roomValidator;

    @BeforeEach
    void setupEach() {
        victim = new RoomService(roomRepository, MapperUtils.roomMapper(), roomValidator);
    }

    @Test
    void testGetRoomSuccess() {
        var room = newRoomBuilderDefault().id(ConstantsTest.DEFAULT_ROOM_ID).build();
        when(roomRepository.findByIdAndActive(ConstantsTest.DEFAULT_ROOM_ID, true)).thenReturn(Optional.of(room));
        var roomDto = victim.getRoom(ConstantsTest.DEFAULT_ROOM_ID);

        assertEquals(room.getId(), roomDto.getId());
        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getSeats(), roomDto.getSeats());
    }

    @Test
    void testGetRoomNotFound() {
        when(roomRepository.findByIdAndActive(ConstantsTest.DEFAULT_ROOM_ID, true)).thenReturn(Optional.empty());
        assertThrows(RoomNotFoundException.class, () -> victim.getRoom(ConstantsTest.DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomSuccess() {
        var createRoomDTO = TestDataCreator.newCreateRoomDTO();
        var roomDTO = victim.createRoom(createRoomDTO);

        assertEquals(roomDTO.getName(), createRoomDTO.getName());
        assertEquals(roomDTO.getSeats(), createRoomDTO.getSeats());
        Mockito.verify(roomRepository).save(any());
    }
}
