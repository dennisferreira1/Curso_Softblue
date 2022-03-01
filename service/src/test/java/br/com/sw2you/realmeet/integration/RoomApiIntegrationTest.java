package br.com.sw2you.realmeet.integration;

import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.api.facade.RoomApi;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

public class RoomApiIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private RoomApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testGetRoomSuccess() {
        var room = TestDataCreator.newRoomBuilderDefault().build();
        roomRepository.saveAndFlush(room);

        Assertions.assertNotNull(room.getId());
        Assertions.assertTrue(room.getActive());

        var roomDto = api.getRoom(ConstantsTest.TEST_CLIENT_API_KEY, ConstantsTest.DEFAULT_ROOM_ID);

        assertEquals(room.getId(), roomDto.getId());
        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getSeats(), roomDto.getSeats());
    }

    @Test
    void testGetRoomInactive() {
        var room = TestDataCreator.newRoomBuilderDefault().active(false).build();
        roomRepository.saveAndFlush(room);
        assertFalse(room.getActive());
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> api.getRoom(ConstantsTest.TEST_CLIENT_API_KEY, room.getId())
        );
    }

    @Test
    void testGetRoomNotExist() {
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> api.getRoom(ConstantsTest.TEST_CLIENT_API_KEY, ConstantsTest.DEFAULT_ROOM_ID)
        );
    }

    @Test
    void testCreateRoomSuccess() {
        var createRoomDTO = TestDataCreator.newCreateRoomDTO();
        var roomDTO = api.createRoom(ConstantsTest.TEST_CLIENT_API_KEY, createRoomDTO);

        assertEquals(createRoomDTO.getName(), roomDTO.getName());
        assertEquals(createRoomDTO.getSeats(), roomDTO.getSeats());
        assertNotNull(roomDTO.getId());

        var room = roomRepository.findById(roomDTO.getId()).orElseThrow();
        assertEquals(room.getName(), roomDTO.getName());
        assertEquals(room.getSeats(), roomDTO.getSeats());
    }

    @Test
    void testCreateRoomValidationError() {
        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () ->
                api.createRoom(
                    ConstantsTest.TEST_CLIENT_API_KEY,
                    (CreateRoomDTO) TestDataCreator.newCreateRoomDTO().name(null)
                )
        );
    }

    @Test
    void testDeleteRoomSuccess() {
        var room = TestDataCreator.newRoomBuilderDefault().build();
        var roomId = roomRepository.saveAndFlush(room).getId();

        api.deleteRoom(ConstantsTest.TEST_CLIENT_API_KEY, roomId);
        assertFalse(roomRepository.findById(roomId).orElseThrow().getActive());
    }

    @Test
    void testDeleteRoomDoesNotExist() {
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> api.deleteRoom(ConstantsTest.TEST_CLIENT_API_KEY, 1L)
        );
    }

    @Test
    void testUpdateRoomSuccess() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());
        var updateRoomDTO = new UpdateRoomDTO().name(room.getName() + "_").seats(room.getSeats() + 1);

        api.updateRoom(ConstantsTest.TEST_CLIENT_API_KEY, room.getId(), updateRoomDTO);
        var updatedRoom = roomRepository.findById(room.getId()).orElseThrow();

        assertEquals(updateRoomDTO.getName(), updatedRoom.getName());
        assertEquals(updateRoomDTO.getSeats(), updatedRoom.getSeats());
    }

    @Test
    void testUpdateRoomDoesNotExist() {
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> api.updateRoom(ConstantsTest.TEST_CLIENT_API_KEY, 1L, new UpdateRoomDTO().name("Room").seats(10))
        );
    }

    @Test
    void testUpdateRoomValidationError() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());

        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () ->
                api.updateRoom(
                    ConstantsTest.TEST_CLIENT_API_KEY,
                    room.getId(),
                    new UpdateRoomDTO().name(null).seats(10)
                )
        );
    }
}
