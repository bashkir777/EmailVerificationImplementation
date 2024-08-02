package com.bashkir777.authservice.data.dao;

import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.data.repositories.RefreshTokenRepository;
import com.bashkir777.authservice.data.entities.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken getRefreshTokenByUser(User user) throws BadCredentialsException {
        return refreshTokenRepository
                .getRefreshTokenByUser(user)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));
    }

    @Transactional
    public void deleteRefreshTokenByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken){
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository
                .getRefreshTokenByUser(refreshToken.getUser());
        if(optionalRefreshToken.isPresent()){
            deleteRefreshTokenByUser(refreshToken.getUser());
        }
        refreshTokenRepository.save(refreshToken);
    }

}
