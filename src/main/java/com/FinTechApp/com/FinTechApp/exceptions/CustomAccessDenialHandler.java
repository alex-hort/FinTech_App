
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;




@Component
@RequiredArgsConstructor
public class CustomAccessDenialHandler implements AccesssDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, 
    HttpServletResponse response, \
    AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Response<?> errorResponse = Response.builder()
        .statusCode(HttpStatus.FORBIDDEN.value())
        .message(accessDeniedException.getMessage())
        .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));


   
    }
    
}