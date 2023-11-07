package hexlet.code.mapper;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
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
public abstract class TaskMapper {
    public abstract Task map(TaskDto dto);
    public abstract TaskDto map(Task model);
    public abstract void update(TaskDto dto, @MappingTarget Task model);
}
