package com.bashkir777.authservice.data.dao;

import com.bashkir777.authservice.data.entities.User;
import com.bashkir777.authservice.data.repositories.RefreshTokenRepository;
import com.bashkir777.authservice.data.entities.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshTokenByUser(User user) throws BadCredentialsException {
        return refreshTokenRepository
                .getRefreshTokenByUser(user)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));
    }

    @Transactional
    public void deleteRefreshTokenByUser(User user){
        refreshTokenRepository.deleteRefreshTokenByUser(user);
    }

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken){
        deleteRefreshTokenByUser(refreshToken.getUser());
        refreshTokenRepository.save(refreshToken);
    }

}
