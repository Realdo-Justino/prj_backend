package com.example.tarefas.controller.login;

import com.example.tarefas.controller.login.dto.LoginDto;
import com.example.tarefas.service.auth.TokenService;
import com.example.tarefas.service.login.LoginService;
import com.example.tarefas.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Autorização")
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;
    private final TokenService tokenService;

    public LoginController(
            LoginService loginService,
            TokenService tokenService
    ) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Realizar Login", description = "Realiza o login do usuario")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sucesso",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Usuario Valido")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Usuario/Senha invalido\" }"
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDto loginDto) {
        loginService.validate(loginDto);

        String token = tokenService.generateToken(loginDto.email());
        String refreshToken = tokenService.generateAndSaveRefreshToken(loginDto.email());

        ResponseCookie refreshTokenCookie = tokenService.createRefreshTokenCookie(refreshToken);
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("accessToken", token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(accessToken);
    }
}
