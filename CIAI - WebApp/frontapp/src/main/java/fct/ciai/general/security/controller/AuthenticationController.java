package fct.ciai.general.security.controller;

import fct.ciai.general.security.controller.payload.ApiResponse;
import fct.ciai.general.security.controller.payload.JwtAuthenticationResponse;
import fct.ciai.general.security.controller.payload.LoginRequest;
import fct.ciai.general.security.controller.payload.SignupRequest;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.security.service.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @ApiOperation(value = "log in", nickname = "authenticateUser")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = authenticationService.authenticateUserAndGetTokenString(loginRequest);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @ApiOperation(value = "register ECMA user", nickname = "registerUser")
    @PostMapping("/signupUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        return registerUserProcess(request, List.of(RoleName.ROLE_USER));
    }

    @ApiOperation(value = "register ECMA admin user", nickname = "registerUserAdmin", notes = "Only ADMIN")
    @PostMapping("/signupUserAdmin")
    @Secured(RoleName.ADMIN)
    public ResponseEntity<?> registerUserAdmin(@Valid @RequestBody SignupRequest request) {
        return registerUserProcess(request, List.of(RoleName.ROLE_USER, RoleName.ROLE_ADMIN));
    }

    @ApiOperation(value = "register partner user", nickname = "registerPartnerUser")
    @PostMapping("/signupPartnerUser")
    public ResponseEntity<?> registerPartnerUser(@Valid @RequestBody SignupRequest request) {
        return registerUserProcess(request, List.of(RoleName.ROLE_PARTNER_USER));
    }

    @ApiOperation(value = "register partner admin", nickname = "registerPartnerAdmin", notes = "Only ADMIN")
    @PostMapping("/singupPartnerAdmin")
    @Secured(RoleName.ADMIN)
    public ResponseEntity<?> registerPartnerAdmin(@Valid @RequestBody SignupRequest request) {
        return registerUserProcess(request, List.of(RoleName.ROLE_PARTNER_ADMIN, RoleName.ROLE_PARTNER_USER));
    }

    private ResponseEntity<?> registerUserProcess(SignupRequest request, List<RoleName> roleNames) {
        try {
            authenticationService.validateRegisteredUser(request);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        AuthUser authUser = authenticationService.createUser(request, roleNames);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(authUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "AuthUser registered successfully"));
    }


}
