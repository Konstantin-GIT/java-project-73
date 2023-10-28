package hexlet.code.mapper;

import hexlet.code.dto.taskstatus.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatus map(TaskStatusDto dto);
    public abstract TaskStatusDto map(TaskStatus model);
    public abstract void update(TaskStatusDto dto, @MappingTarget TaskStatus model);
}

