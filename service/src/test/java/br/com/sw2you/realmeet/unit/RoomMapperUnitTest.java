package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.MapperUtils.roomMapper;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilderDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomMapperUnitTest extends BaseUnitTest {
    private RoomMapper victim;

    @BeforeEach
    void setupEach() {
        victim = roomMapper();
    }

    @Test
    void testFromEntityToDto() {
        var room = newRoomBuilderDefault().id(ConstantsTest.DEFAULT_ROOM_ID).build();
        var roomDto = victim.roomToRoomDto(room);

        assertEquals(room.getId(), roomDto.getId());
        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getSeats(), roomDto.getSeats());
    }

    @Test
    void testFromCreateRoomDTOToRoom() {
        var createRoomDTO = TestDataCreator.newCreateRoomDTO();
        var room = victim.createRoomDTOtoRoom(createRoomDTO);

        assertEquals(room.getName(), createRoomDTO.getName());
        assertEquals(room.getSeats(), createRoomDTO.getSeats());
    }
}
