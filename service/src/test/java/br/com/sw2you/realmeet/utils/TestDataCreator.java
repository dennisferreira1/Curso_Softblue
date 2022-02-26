package br.com.sw2you.realmeet.utils;

import static br.com.sw2you.realmeet.utils.ConstantsTest.*;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.model.Employee;

public final class TestDataCreator {

    private TestDataCreator() {}

    public static Room.RoomBuilder newRoomBuilderDefault() {
        return Room.newRoomBuilder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateRoomDTO newCreateRoomDTO() {
        return (CreateRoomDTO) new CreateRoomDTO().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static Allocation.AllocationBuilder newAllocationBuilderDefault() {
        return Allocation
            .newAllocationBuilder()
            .room(newRoomBuilderDefault().id(DEFAULT_ROOM_ID).build())
            .subject(DEFAULT_ALLOCATION_SUBJECT)
            .startAt(DEFAULT_ALLOCATION_START_AT)
            .endAt(DEFAULT_ALLOCATION_END_AT)
            .employee(newEmployeeBuilderDefault().build());
    }

    public static Employee.EmployeeBuilder newEmployeeBuilderDefault() {
        return Employee.newEmployeeBuilder().name(DEFAULT_EMPLOYEE_NAME).email(DEFAULT_EMPLOYEE_EMAIL);
    }

    public static CreateAllocationDTO newCreateAllocationDTO() {
        return new CreateAllocationDTO()
            .roomId(DEFAULT_ROOM_ID)
            .subject(DEFAULT_ALLOCATION_SUBJECT)
            .employeeName(DEFAULT_EMPLOYEE_NAME)
            .employeeEmail(DEFAULT_EMPLOYEE_EMAIL)
            .startAt(DEFAULT_ALLOCATION_START_AT)
            .endAt(DEFAULT_ALLOCATION_END_AT);
    }

    public static UpdateAllocationDTO newUpdateAllocationDTO() {
        return new UpdateAllocationDTO()
            .subject(DEFAULT_ALLOCATION_SUBJECT)
            .startAt(DEFAULT_ALLOCATION_START_AT)
            .endAt(DEFAULT_ALLOCATION_END_AT);
    }
}
