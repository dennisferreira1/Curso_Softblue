package br.com.sw2you.realmeet.integration;

import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.api.facade.AllocationApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

public class AllocationApiIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private AllocationApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testCreateAllocationSuccess() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());
        var createAllocationDTO = TestDataCreator.newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO = api.createAllocation(createAllocationDTO);

        Assertions.assertNotNull(allocationDTO.getId());
        assertEquals(room.getId(), allocationDTO.getRoomId());
        assertEquals(createAllocationDTO.getSubject(), allocationDTO.getSubject());
        assertEquals(createAllocationDTO.getEmployeeName(), allocationDTO.getEmployeeName());
        assertEquals(createAllocationDTO.getEmployeeEmail(), allocationDTO.getEmployeeEmail());
        assertTrue(createAllocationDTO.getStartAt().isEqual(allocationDTO.getStartAt()));
        assertTrue(createAllocationDTO.getEndAt().isEqual(allocationDTO.getEndAt()));
    }

    @Test
    void testCreateAllocationValidationError() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());
        var allocationDTO = TestDataCreator.newCreateAllocationDTO().roomId(room.getId()).subject(null);

        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.createAllocation(allocationDTO));
    }

    @Test
    void testCreateAllocationWhenRoomDoesNotExist() {
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> api.createAllocation(TestDataCreator.newCreateAllocationDTO())
        );
    }

    @Test
    void testDeleteAllocationSuccess() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());
        var allocation =
            this.allocationRepository.saveAndFlush(TestDataCreator.newAllocationBuilderDefault().room(room).build());
        api.deleteAllocation(allocation.getId());

        assertFalse(allocationRepository.findById(allocation.getId()).isPresent());
    }

    @Test
    void testDeleteAllocationWhenCannotBeDeleted() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());
        var allocation =
            this.allocationRepository.saveAndFlush(
                    TestDataCreator
                        .newAllocationBuilderDefault()
                        .room(room)
                        .startAt(ConstantsTest.DEFAULT_ALLOCATION_START_AT.minusDays(2))
                        .endAt(ConstantsTest.DEFAULT_ALLOCATION_END_AT)
                        .build()
                );

        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.deleteAllocation(allocation.getId())
        );
    }

    @Test
    void testDeleteAllocationDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteAllocation(1L));
    }
}
