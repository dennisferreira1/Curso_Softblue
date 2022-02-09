package br.com.sw2you.realmeet.mapper;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AllocationMapper {

    @Mapping(source = "allocation.room.id", target = "roomId")
    @Mapping(source = "allocation.employee.name", target = "employeeName")
    @Mapping(source = "allocation.employee.email", target = "employeeEmail")
    public abstract AllocationDTO allocationToAllocationDto(Allocation allocation);

    @Mapping(source = "room", target = "room")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createAllocationDTO.employeeName", target = "employee.name")
    @Mapping(source = "createAllocationDTO.employeeEmail", target = "employee.email")
    public abstract Allocation CreateAllocationDTOtoAllocation(CreateAllocationDTO createAllocationDTO, Room room);
}
