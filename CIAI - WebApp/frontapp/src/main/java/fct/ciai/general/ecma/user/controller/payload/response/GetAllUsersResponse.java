package fct.ciai.general.ecma.user.controller.payload.response;

import fct.ciai.general.ecma.user.model.dto.UserExtendedInfoDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "GetAllUsersResponse", description = "Gets pageable users")
public class GetAllUsersResponse {
    private List<UserExtendedInfoDTO> userDTOList;
    private int totalPages;
}
