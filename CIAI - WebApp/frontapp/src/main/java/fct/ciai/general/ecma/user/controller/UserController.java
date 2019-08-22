package fct.ciai.general.ecma.user.controller;

import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.persistence.repository.UserRepository;
import fct.ciai.general.ecma.user.controller.payload.ManagePartnerUserRequest;
import fct.ciai.general.ecma.user.controller.payload.ManageUserAccountRequest;
import fct.ciai.general.ecma.user.controller.payload.response.GetAllUsersResponse;
import fct.ciai.general.ecma.user.model.dto.UserExtendedInfoDTO;
import fct.ciai.general.ecma.user.service.UserService;
import fct.ciai.general.security.LoggedUser;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jline.internal.Nullable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "Manages partner users")
@Controller
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @ApiOperation(httpMethod = "POST",
            value = "Manages partner user account",
            nickname = "getAuthUser",
            notes = "Only ADMIN or PARTNER_ADMIN")
    @PostMapping("manageUserAccountAdmin")
    @Secured({RoleName.PARTNER_ADMIN, RoleName.ADMIN})
    public ResponseEntity manageUserAccountAdmin(@LoggedUser AuthUser loggedUser, @Valid @RequestBody ManagePartnerUserRequest managePartnerUserRequest) {
        try {
            userService.manageUserAccountAsAdmin(loggedUser, managePartnerUserRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Manages partner user account",
            nickname = "getAuthUser")
    @PostMapping("manageUserAccount")
    public ResponseEntity manageUserAccount(@LoggedUser AuthUser loggedUser, @Valid @RequestBody ManageUserAccountRequest manageUserAccountRequest) {
        userService.manageUserAccount(loggedUser, manageUserAccountRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "delete user by userId",
            nickname = "deleteUser")
    @DeleteMapping("deleteUser")
    public ResponseEntity deleteUser(@LoggedUser AuthUser loggedUser, Long userId) {
        try{
            userService.deleteUser(loggedUser, userId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "get all company users",
            nickname = "getAllCompanyUsers",
            notes = "Only for ADMIN or PARTNER_ADMIN")
    @GetMapping("getAllCompanyUsers")
    @Secured({RoleName.PARTNER_ADMIN, RoleName.ADMIN})
    public ResponseEntity<List<UserExtendedInfoDTO>> getAllCompanyUsers(@LoggedUser AuthUser loggedUser, @Nullable Long companyId) {
        return new ResponseEntity<>(
                userService.findAllByCompany(loggedUser, companyId).stream()
                        .map(user -> mapper.map(user, UserExtendedInfoDTO.class))
                        .collect(Collectors.toList()),
                HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "get all users",
            nickname = "getAllUsers",
            notes = "Only for ADMIN")
    @GetMapping("getAllUsers")
    @Secured({RoleName.ADMIN})
    public ResponseEntity<GetAllUsersResponse> getAllUsers(@RequestParam int page, @RequestParam int pageSize) {
        Page<User> pageResponse = userRepository.findAll(PageRequest.of(page, pageSize));
        List<UserExtendedInfoDTO> userDTOList = pageResponse.stream()
                .map(user -> mapper.map(user, UserExtendedInfoDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new GetAllUsersResponse(userDTOList, pageResponse.getTotalPages()),
                HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "get user profile",
            nickname = "getUserProfile")
    @GetMapping("getUserProfile")
    public ResponseEntity<UserExtendedInfoDTO> getUserProfile(@LoggedUser AuthUser loggedUser) {
        User user = userRepository.findById(loggedUser.getUser().getId())
                .orElseThrow(() -> new BadRequestException("Non existing user."));
        return new ResponseEntity<>(
               mapper.map(user, UserExtendedInfoDTO.class),
               HttpStatus.ACCEPTED);
    }
}
