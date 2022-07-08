package ngoquangdai.managernews.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import ngoquangdai.managernews.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ApiAuthorizationFilter extends OncePerRequestFilter {

    public static final String[] IGNORE_PATHS = {"/api/v1/login", "/api/v1/register", "/api/v1/news"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String requestPath = request.getServletPath();
        if (Arrays.asList(IGNORE_PATHS).contains(requestPath)){
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ( authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        try{

            String token = authorizationHeader.replace("Bearer", "").trim();

            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);

            String username = decodedJWT.getSubject();

            String role = decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asString();

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception e){
            response.setStatus((HttpStatus.FORBIDDEN.value()));
            Map<String , String > errors = new HashMap<>();
            errors.put("error", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(new Gson().toJson(errors));
        }
    }
}
