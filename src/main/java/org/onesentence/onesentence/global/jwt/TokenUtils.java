package org.onesentence.onesentence.global.jwt;

import io.jsonwebtoken.*;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.onesentence.onesentence.domain.user.entity.User;
import org.springframework.stereotype.Service;
import javax.xml.bind.DatatypeConverter;

@Service
@RequiredArgsConstructor
public class TokenUtils {

	private final String SECRET_KEY = "secretKey";
	private final String REFRESH_KEY = "refreshKey";
	private final String DATA_KEY = "nickName";

	public String generateJwtToken(User user) {
		return Jwts.builder()
			.setSubject(user.getNickName())
			.setHeader(createHeader())
			.setClaims(createClaims(user))
			.setExpiration(createExpireDate(1000 * 60 * 60 * 12)) // 12시간
			.signWith(SignatureAlgorithm.HS256, createSigningKey(SECRET_KEY))
			.compact();
	}

	public String saveRefreshToken(User user) {
		return Jwts.builder()
			.setSubject(user.getNickName())
			.setHeader(createHeader())
			.setClaims(createClaims(user))
			.setExpiration(createExpireDate(1000 * 60 * 60 * 24 * 7 * 2)) // 2주
			.signWith(SignatureAlgorithm.HS256, createSigningKey(REFRESH_KEY))
			.compact();
	}



	public boolean isValidToken(String token) {
		System.out.println("isValidToken is : " +token);
		try {
			Claims accessClaims = getClaimsFormToken(token);
			System.out.println("Access expireTime: " + accessClaims.getExpiration());
			System.out.println("Access userId: " + accessClaims.get("userId"));
			return true;
		} catch (ExpiredJwtException exception) {
			System.out.println("Token Expired UserNickName : " + exception.getClaims().getSubject());
			return false;
		} catch (JwtException exception) {
			System.out.println("Token Tampered");
			return false;
		} catch (NullPointerException exception) {
			System.out.println("Token is null");
			return false;
		}
	}
	public boolean isValidRefreshToken(String token) {
		try {
			Claims accessClaims = getClaimsToken(token);
			System.out.println("Access expireTime: " + accessClaims.getExpiration());
			System.out.println("Access userId: " + accessClaims.get("nickName"));
			return true;
		} catch (ExpiredJwtException exception) {
			System.out.println("Token Expired UserNickName : " + exception.getClaims().getSubject());
			return false;
		} catch (JwtException exception) {
			System.out.println("Token Tampered");
			return false;
		} catch (NullPointerException exception) {
			System.out.println("Token is null");
			return false;
		}
	}


	private Date createExpireDate(long expireDate) {
		long curTime = System.currentTimeMillis();
		return new Date(curTime + expireDate);
	}

	private Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();

		header.put("typ", "ACCESS_TOKEN");
		header.put("alg", "HS256");
		header.put("regDate", System.currentTimeMillis());

		return header;
	}

	private Map<String, Object> createClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(DATA_KEY, user.getNickName());
		return claims;
	}

	private Key createSigningKey(String key) {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
		return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
	}

	private Claims getClaimsFormToken(String token) {
		return Jwts.parser()
			.setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
			.parseClaimsJws(token)
			.getBody();
	}
	private Claims getClaimsToken(String token) {
		return Jwts.parser()
			.setSigningKey(DatatypeConverter.parseBase64Binary(REFRESH_KEY))
			.parseClaimsJws(token)
			.getBody();
	}

}
