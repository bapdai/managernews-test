package ngoquangdai.managernews.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    private static Algorithm algorithm;

    private static JWTVerifier verifier;

    public static final String JWT_SECRET_KEY = "secret";

    public static final int ONE_SECOND = 1000;

    public static final int ONE_MINUTE = ONE_SECOND * 60;

    public static final int ONE_HOUR = ONE_MINUTE * 60;

    public static final int ONE_DAY = ONE_HOUR * 24;

    public static final String ROLE_CLAIM_KEY = "role";

    public static Algorithm getAlgorithm() {
        if (algorithm == null){
            algorithm = Algorithm.HMAC256((JWT_SECRET_KEY.getBytes()));
        }
        return algorithm;
    }

    public static JWTVerifier getVerifier(){
        if (verifier == null){
            verifier = JWT.require(getAlgorithm()).build();
        }
        return verifier;
    }

    public static DecodedJWT getDecodedJwt(String token){
        DecodedJWT decodedJWT = getVerifier().verify(token);
        return decodedJWT;
    }

    public static String generateToken(String subject, String role, String issuer, int expireAfter){
        if (role == null || role.length() == 0){
            return JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expireAfter))
                    .withIssuer(issuer)
                    .sign(getAlgorithm());
        }
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireAfter))
                .withIssuer(issuer)
                .withClaim(JwtUtil.ROLE_CLAIM_KEY, role)
                .sign(getAlgorithm());
    }
}
