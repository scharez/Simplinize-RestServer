package utils;

import entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Arrays;
import java.util.Date;

public class JwtHelper {

    private final long ACCESS_TOKEN_VALIDITY_SECONDS = 10_0000;
    private final String URL = "https://api.scharez.at/simplinize";

    private PropertyLoader pl = new PropertyLoader();


    /**
     *
     * @param subject
     * @param roles
     * @return
     */

    public String create(String subject, Object[] roles) {

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, pl.prop.getProperty("jwt.key"))
                .setIssuer(URL)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .claim("role", Arrays.toString(roles))
                .compact();
    }

    /**
     *
     * @param token
     * @return
     */

    public String checkSubject(String token) {

        try {
            return Jwts.parser()
                    .setSigningKey(pl.prop.getProperty("jwt.key"))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            return null;
        }
    }

    /**
     *
     * @param token
     * @return
     */

    public Role [] getRoles(String token) {
        String roles;
        roles =  Jwts.parser()
                .setSigningKey(pl.prop.getProperty("jwt.key"))
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);

        String[] SrolesArray = roles
                .replace("[", "")
                .replace("]", "")
                .split(", ");

        Role [] rolesArray = new Role[SrolesArray.length];

        for (int i = 0; i < rolesArray.length; i++) {
            rolesArray[i] = Role.valueOf(SrolesArray[i]);
        }

        return rolesArray;
    }
}
