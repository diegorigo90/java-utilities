package it.diegorigo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    public static String generateJwtToken(String mySecret,
                                          Map<String, String> claims,
                                          Date expirationDate) {
        JWTCreator.Builder builder = JWT.create();
        for (Map.Entry<String, String> claim : claims.entrySet()) {
            builder.withClaim(claim.getKey(), claim.getValue());
        }
        return builder.withExpiresAt(expirationDate)
                      .sign(Algorithm.HMAC512(mySecret));
    }

    public static JwtInfoDto decodeJwtToken(String jwtToken) {
        DecodedJWT decodedJWT = JWT.decode(jwtToken);

        JwtInfoDto dto = new JwtInfoDto();
        dto.setAudience(decodedJWT.getAudience());
        dto.setClaims(retrieveStringClaims(decodedJWT));
        dto.setAlgorithm(decodedJWT.getAlgorithm());
        dto.setExpiresAt(decodedJWT.getExpiresAt());
        return dto;
    }

    private static Map<String, String> retrieveStringClaims(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();

        Map<String, String> result = new HashMap<>();
        claims.forEach((key, value) -> result.put(key,
                                                  value.asString()));
        return result;
    }
}
