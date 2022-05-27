package fr.pellan.api.linkedinmessageanalyzer.service;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import fr.pellan.api.linkedinmessageanalyzer.auth.LinkedinAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class LinkedinService {

    private static final String clientId = "X";

    private static final String clientSecret = "X";
    private static final String PROTECTED_EMAIL_RESOURCE_URL
            = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    private void buildAuthService(){

        OAuth20Service linkedinAuthService = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope(new ScopeBuilder("r_liteprofile", "r_emailaddress"))
                .callback("http://localhost:8080/linkedin-connect/execute")
                .build(LinkedInApi20.instance());

        LinkedinAuth.getInstance(linkedinAuthService);
    }

    public String getLinkedinConnectUrl(){

        buildAuthService();
        OAuth20Service linkedinService = LinkedinAuth.getInstance().getAuthService();

        final String secretState = "secret" + new Random().nextInt(999_999);
        return linkedinService.getAuthorizationUrl(secretState);
    }

    public String getUserEmail(String authCode) {

        OAuth2AccessToken accessToken = null;
        OAuth20Service linkedinService = LinkedinAuth.getInstance().getAuthService();

        log.info(authCode);

        try {
            accessToken = linkedinService.getAccessToken(authCode);
        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error(e. getMessage());
        }

        final OAuthRequest emailRequest = new OAuthRequest(Verb.GET, PROTECTED_EMAIL_RESOURCE_URL);
        emailRequest.addHeader("x-li-format", "json");
        emailRequest.addHeader("Accept-Language", "ru-RU");
        linkedinService.signRequest(accessToken, emailRequest);

        String emailResp = null;

        try(Response emailResponse = linkedinService.execute(emailRequest)) {
            emailResp = emailResponse.getBody();
        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error(e. getMessage());
        }

        return emailResp;
    }
}