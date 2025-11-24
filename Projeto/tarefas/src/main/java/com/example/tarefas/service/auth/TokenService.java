package com.example.tarefas.service.auth;

import com.example.tarefas.exceptions.RefreshTokenExpiredExpection;
import com.example.tarefas.exceptions.RefreshTokenNotExistsException;
import com.example.tarefas.model.RefreshToken;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.RefreshTokenRepository;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import com.example.tarefas.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private long accessTokenExpirationMinutes = 15;

    private long refreshTokenExpirationDays = 7;

    public String generateToken(String email) {
        Instant now = Instant.now();


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("headmatter")
                .subject(email)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES))
                .build();


        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public Jwt validateToken(String token) {
        try {
            if (token == null) {
                throw new RuntimeException("Token não pode ser nulo");
            }
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    public String generateAndSaveRefreshToken(String email) {
        Usuario user = usuarioService.findByEmail(email);

        Optional<RefreshToken> optionalRefreshToken = this.refreshTokenRepository.findByUsuario(user);
        RefreshToken refreshToken;

        if (optionalRefreshToken.isPresent()) {
            refreshToken = optionalRefreshToken.get();
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUsuario(user);
        }

        refreshToken.setExpirationDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refresh-token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpirationDays * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie generateResponseRefreshTokeCookieLogout() {
        return ResponseCookie.from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    public String validateRefreshTokenReturnAccessToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);

        if (refreshTokenOpt.isEmpty()) {
            throw new RefreshTokenNotExistsException("O refresh token não existe");
        }

        RefreshToken refreshToken = refreshTokenOpt.get();
        if (refreshToken.getExpirationDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredExpection("O refresh token está expirado");
        }

        return this.generateToken(refreshToken.getUsuario().getEmail());
    }

}
