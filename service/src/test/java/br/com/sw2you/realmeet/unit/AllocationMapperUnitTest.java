package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.MapperUtils.allocationMapper;
import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllocationMapperUnitTest extends BaseUnitTest {
    private AllocationMapper victim;

    @BeforeEach
    void setupEach() {
        victim = allocationMapper();
    }

    @Test
    void testAllocationToAllocationDto() {
        var allocation = TestDataCreator.newAllocationBuilderDefault().id(1L).build();
        var allocationDTO = victim.allocationToAllocationDto(allocation);

        assertNotNull(allocationDTO.getRoomId());
        assertEquals(allocation.getId(), allocationDTO.getId());
        assertEquals(allocation.getRoom().getId(), allocationDTO.getRoomId());
        assertEquals(allocation.getSubject(), allocationDTO.getSubject());
        assertEquals(allocation.getEmployee().getName(), allocationDTO.getEmployeeName());
        assertEquals(allocation.getEmployee().getEmail(), allocationDTO.getEmployeeEmail());
        assertEquals(allocation.getStartAt(), allocationDTO.getStartAt());
        assertEquals(allocation.getEndAt(), allocationDTO.getEndAt());
    }

    @Test
    void testCreateAllocationDTOToAllocation() {
        var createAllocationDTO = TestDataCreator.newCreateAllocationDTO();
        var allocation = victim.CreateAllocationDTOtoAllocation(
            createAllocationDTO,
            TestDataCreator.newRoomBuilderDefault().build()
        );

        assertNull(allocation.getId());
        assertEquals(createAllocationDTO.getSubject(), allocation.getSubject());
        assertEquals(createAllocationDTO.getEmployeeName(), allocation.getEmployee().getName());
        assertEquals(createAllocationDTO.getEmployeeEmail(), allocation.getEmployee().getEmail());
        assertEquals(createAllocationDTO.getStartAt(), allocation.getStartAt());
        assertEquals(createAllocationDTO.getEndAt(), allocation.getEndAt());
    }
}
