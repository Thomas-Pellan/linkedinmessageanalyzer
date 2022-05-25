package fr.pellan.api.linkedinmessageanalyzer.controller;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("linkedin-connect")
public class LinkedinConnectorController {

    private static final String clientId = "X";

    private static final String clientSecret = "X";
    private static final String PROTECTED_RESOURCE_URL = "https://api.linkedin.com/v2/me";
    private static final String PROTECTED_EMAIL_RESOURCE_URL
            = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    OAuth20Service service= null;

    @GetMapping(value = "/init")
    public ResponseEntity<String> getLinkedinConnectUrl() {

        service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope(new ScopeBuilder("r_liteprofile", "r_emailaddress")) // replace with desired scope
                .callback("http://localhost:8080/linkedin-connect/execute")
                .build(LinkedInApi20.instance());

        final String secretState = "secret" + new Random().nextInt(999_999);
        final String authorizationUrl = service.getAuthorizationUrl(secretState);

        return new ResponseEntity(authorizationUrl, HttpStatus.OK);
    }

    @GetMapping(value = "/execute")
    public ResponseEntity<Boolean> executeLinkedinCommand(@RequestParam(name="code") String code) {

        if(service == null){
            log.error("OAuth2 service not initialized !");
            return null;
        }

        OAuth2AccessToken accessToken = null;

        log.info(code);

        try {
            accessToken = service.getAccessToken(code);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        final OAuthRequest emailRequest = new OAuthRequest(Verb.GET, PROTECTED_EMAIL_RESOURCE_URL);
        emailRequest.addHeader("x-li-format", "json");
        emailRequest.addHeader("Accept-Language", "ru-RU");
        service.signRequest(accessToken, emailRequest);
        System.out.println();
        try (Response emailResponse = service.execute(emailRequest)) {
            System.out.println(emailResponse.getCode());
            System.out.println(emailResponse.getBody());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        System.out.println();

        System.out.println("Now we're going to access a protected profile resource...");

        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        request.addHeader("x-li-format", "json");
        request.addHeader("Accept-Language", "ru-RU");
        service.signRequest(accessToken, request);
        System.out.println();
        try (Response response = service.execute(request)) {
            System.out.println(response.getCode());
            System.out.println(response.getBody());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        return new ResponseEntity(true, HttpStatus.OK);
    }

}
