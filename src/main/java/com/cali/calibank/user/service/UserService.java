package com.cali.calibank.user.service;

import com.cali.calibank.user.controller.UserDto;
import com.cali.calibank.user.controller.UserDto.AuthRequest;
import com.cali.calibank.user.controller.UserDto.TokenResponse;
import com.cali.calibank.user.domain.common.TokenType;
import com.cali.calibank.user.domain.entity.Token;
import com.cali.calibank.user.domain.entity.User;
import com.cali.calibank.user.exception.DuplicatedEmailException;
import com.cali.calibank.user.exception.NotExistedUserException;
import com.cali.calibank.user.repository.TokenRepository;
import com.cali.calibank.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public TokenResponse signup(UserDto.SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new DuplicatedEmailException();
        }

        User user = signupRequest.toEntity(passwordEncoder);
        User savedUser = userRepository.save(user);
        String jws = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jws);

        return TokenResponse.builder()
            .accessToken(jws)
            .refreshToken(refreshToken)
            .build();
    }

    @Transactional
    public TokenResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(NotExistedUserException::new);
        String jwtToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return TokenResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(Token::setTokenExpiration);
        tokenRepository.saveAll(validUserTokens);
    }
}
