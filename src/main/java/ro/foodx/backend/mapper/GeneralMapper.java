package ro.foodx.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.foodx.backend.dto.dashboard.MenuCreateRequest;
import ro.foodx.backend.model.general.Menu;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeneralMapper {
    GeneralMapper INSTANCE = Mappers.getMapper(GeneralMapper.class);
    Menu convertToMenu(MenuCreateRequest menuCreateRequest);
}
