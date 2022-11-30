package it.diegorigo.jwt;

import it.diegorigo.jwt.JwtInfoDto;
import it.diegorigo.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtilsTest {

    @Test
    void generateJwtTokenTest(){
        String mySecret = "cippalippa";
        Map<String, String> claims = new HashMap<>();
        claims.put("name", "Ciccio");
        claims.put("surname","Panza");
        Date expirationDate = new Date();
        String token = JwtUtils.generateJwtToken(mySecret, claims, expirationDate);

        Assertions.assertNotNull(token);

        JwtInfoDto dto = JwtUtils.decodeJwtToken(token);
        Assertions.assertEquals("HS512",dto.getAlgorithm());
        dto.getClaims().entrySet().forEach(item -> {
            Assertions.assertEquals(claims.get(item.getKey()),item.getValue());
        });
    }
}
