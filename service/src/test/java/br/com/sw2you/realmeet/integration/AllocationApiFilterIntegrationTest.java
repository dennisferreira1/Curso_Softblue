package br.com.sw2you.realmeet.integration;

import br.com.sw2you.realmeet.api.facade.AllocationApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.service.AllocationService;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

public class AllocationApiFilterIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private AllocationApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    private AllocationService allocationService;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testFilterAllAllocations() {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());
        var allocation1 = allocationRepository.saveAndFlush(
            TestDataCreator.newAllocationBuilderDefault().room(room).subject("Allocation 1").build()
        );
        var allocation2 = allocationRepository.saveAndFlush(
            TestDataCreator.newAllocationBuilderDefault().room(room).subject("Allocation 2").build()
        );
        var allocation3 = allocationRepository.saveAndFlush(
            TestDataCreator.newAllocationBuilderDefault().room(room).subject("Allocation 3").build()
        );

        var allocationsDTO = api.listAllocations(null, null, null, null, null, null, null);

        Assertions.assertEquals(3, allocationsDTO.size());
        Assertions.assertEquals(allocation1.getId(), allocationsDTO.get(0).getId());
        Assertions.assertEquals(allocation2.getId(), allocationsDTO.get(1).getId());
        Assertions.assertEquals(allocation3.getId(), allocationsDTO.get(2).getId());
    }

    @Test
    void testFilterAllocationsByRoomId() {
        var roomA = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().name("ROOM A").build());
        var roomB = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().name("ROOM B").build());
        var allocation1 = allocationRepository.saveAndFlush(
            TestDataCreator.newAllocationBuilderDefault().room(roomA).subject("Allocation 1").build()
        );
        var allocation2 = allocationRepository.saveAndFlush(
            TestDataCreator.newAllocationBuilderDefault().room(roomA).subject("Allocation 2").build()
        );
        allocationRepository.saveAndFlush(
            TestDataCreator.newAllocationBuilderDefault().room(roomB).subject("Allocation 3").build()
        );

        var allocationsDTO = api.listAllocations(null, roomA.getId(), null, null, null, null, null);

        Assertions.assertEquals(2, allocationsDTO.size());
        Assertions.assertEquals(allocation1.getId(), allocationsDTO.get(0).getId());
        Assertions.assertEquals(allocation2.getId(), allocationsDTO.get(1).getId());
    }

    @Test
    void testFilterAllocationsByEmployeeEmail() {
        var roomA = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().name("ROOM A").build());

        var employee1 = TestDataCreator.newEmployeeBuilderDefault().build();
        var employee2 = TestDataCreator
            .newEmployeeBuilderDefault()
            .email("_" + ConstantsTest.DEFAULT_EMPLOYEE_EMAIL)
            .build();

        var allocation1 = allocationRepository.saveAndFlush(
            TestDataCreator
                .newAllocationBuilderDefault()
                .room(roomA)
                .employee(employee1)
                .subject("Allocation 1")
                .build()
        );
        var allocation2 = allocationRepository.saveAndFlush(
            TestDataCreator
                .newAllocationBuilderDefault()
                .room(roomA)
                .employee(employee1)
                .subject("Allocation 2")
                .build()
        );
        allocationRepository.saveAndFlush(
            TestDataCreator
                .newAllocationBuilderDefault()
                .room(roomA)
                .employee(employee2)
                .subject("Allocation 3")
                .build()
        );

        var allocationsDTO = api.listAllocations(employee1.getEmail(), null, null, null, null, null, null);

        Assertions.assertEquals(2, allocationsDTO.size());
        Assertions.assertEquals(allocation1.getEmployee().getEmail(), allocationsDTO.get(0).getEmployeeEmail());
        Assertions.assertEquals(allocation2.getEmployee().getEmail(), allocationsDTO.get(1).getEmployeeEmail());
    }

    @Test
    void testFilterAllAllocationsDateRange() {
        var baseStartAt = DateUtils.now().plusDays(2).withHour(14).withMinute(0);
        var baseEndAt = DateUtils.now().plusDays(4).withHour(20).withMinute(0);

        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());

        var allocation1 = allocationRepository.saveAndFlush(
            TestDataCreator
                .newAllocationBuilderDefault()
                .room(room)
                .subject("Allocation 1")
                .startAt(baseStartAt.plusHours(2))
                .endAt(baseStartAt.plusHours(3))
                .build()
        );
        var allocation2 = allocationRepository.saveAndFlush(
            TestDataCreator
                .newAllocationBuilderDefault()
                .room(room)
                .subject("Allocation 2")
                .startAt(baseStartAt.plusHours(5))
                .endAt(baseStartAt.plusHours(6))
                .build()
        );
        var allocation3 = allocationRepository.saveAndFlush(
            TestDataCreator
                .newAllocationBuilderDefault()
                .room(room)
                .subject("Allocation 3")
                .startAt(baseEndAt.plusDays(1))
                .endAt(baseEndAt.plusDays(1).plusHours(1))
                .build()
        );

        var allocationsDTO = api.listAllocations(
            null,
            null,
            baseStartAt.toLocalDate(),
            baseEndAt.toLocalDate(),
            null,
            null,
            null
        );

        Assertions.assertEquals(2, allocationsDTO.size());
        Assertions.assertEquals(allocation1.getId(), allocationsDTO.get(0).getId());
        Assertions.assertEquals(allocation2.getId(), allocationsDTO.get(1).getId());
    }

    @Test
    void testFilterAllAllocationsUsingPagination() {
        persistAllocations(15);
        ReflectionTestUtils.setField(allocationService, "maxLimit", 10);

        var allocationsDTOPage1 = api.listAllocations(null, null, null, null, null, null, 0);
        var allocationsDTOPage2 = api.listAllocations(null, null, null, null, null, null, 1);

        Assertions.assertEquals(10, allocationsDTOPage1.size());
        Assertions.assertEquals(5, allocationsDTOPage2.size());
    }

    @Test
    void testFilterAllAllocationsUsingPaginationAndLimit() {
        persistAllocations(25);
        ReflectionTestUtils.setField(allocationService, "maxLimit", 50);

        var allocationsDTOPage1 = api.listAllocations(null, null, null, null, null, 10, 0);
        var allocationsDTOPage2 = api.listAllocations(null, null, null, null, null, 10, 1);
        var allocationsDTOPage3 = api.listAllocations(null, null, null, null, null, 10, 2);

        Assertions.assertEquals(10, allocationsDTOPage1.size());
        Assertions.assertEquals(10, allocationsDTOPage2.size());
        Assertions.assertEquals(5, allocationsDTOPage3.size());
    }

    @Test
    void testFilterAllAllocationsOrderByStartAtDesc() {
        var allocationsList = persistAllocations(3);
        var allocationsDTOList = api.listAllocations(null, null, null, null, "-startAt", null, null);

        Assertions.assertEquals(3, allocationsDTOList.size());
        Assertions.assertEquals(allocationsList.get(0).getId(), allocationsDTOList.get(2).getId());
        Assertions.assertEquals(allocationsList.get(1).getId(), allocationsDTOList.get(1).getId());
        Assertions.assertEquals(allocationsList.get(2).getId(), allocationsDTOList.get(0).getId());
    }

    @Test
    void testFilterAllAllocationsOrderByFieldInvalid() {
        Assertions.assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.listAllocations(null, null, null, null, "invalid", null, null)
        );
    }

    private List<Allocation> persistAllocations(int numberOfAllocations) {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());

        return IntStream
            .rangeClosed(1, numberOfAllocations)
            .mapToObj(
                i ->
                    allocationRepository.saveAndFlush(
                        TestDataCreator
                            .newAllocationBuilderDefault()
                            .room(room)
                            .subject(ConstantsTest.DEFAULT_ALLOCATION_SUBJECT + "-" + i)
                            .startAt(ConstantsTest.DEFAULT_ALLOCATION_START_AT.plusHours(i))
                            .endAt(ConstantsTest.DEFAULT_ALLOCATION_END_AT.plusHours(i))
                            .build()
                    )
            )
            .collect(Collectors.toList());
    }
}
