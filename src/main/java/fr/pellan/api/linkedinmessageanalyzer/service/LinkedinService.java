package fr.pellan.api.linkedinmessageanalyzer.service;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import fr.pellan.api.linkedinmessageanalyzer.auth.LinkedinAuth;
import fr.pellan.api.linkedinmessageanalyzer.exception.LinkedinConnectorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class LinkedinService {

    @Autowired
    private LinkedinAppAuthService linkedinAppAuthService;

    private static final String PROTECTED_EMAIL_RESOURCE_URL
            = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    private static final String PROTECTED_PROFILE_RESOURCE_URL
            = "https://api.linkedin.com/v2/me";

    private static final String MESSAGES_RESSOURCES_URL = "https://www.linkedin.com/messaging";

    private static final Random RANDOM;

    static {
        try {
            RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("LinkedinService : error during service secret creation");
            throw new LinkedinConnectorException(e.getMessage());
        }
    }

    public String getLinkedinConnectUrl(String callback){

        linkedinAppAuthService.buildAuthService(callback);
        OAuth20Service linkedinService = LinkedinAuth.getInstance().getAuthService();

        String secretState = "secret" + RANDOM.nextInt(999_999);

        return linkedinService.getAuthorizationUrl(secretState);
    }

    public String getUserProfile(String authCode){

        return executeLinkedinApiCall(authCode, PROTECTED_PROFILE_RESOURCE_URL);
    }

    public String getUserMessages(String authCode){

        return executeLinkedinApiCall(authCode, MESSAGES_RESSOURCES_URL);
    }

    public String getUserEmail(String authCode) {

        return executeLinkedinApiCall(authCode, PROTECTED_EMAIL_RESOURCE_URL);
    }

    private String executeLinkedinApiCall(String authCode, String url){

        OAuth2AccessToken accessToken = null;
        OAuth20Service linkedinService = LinkedinAuth.getInstance().getAuthService();

        try {
            accessToken = linkedinService.getAccessToken(authCode);
        } catch (IOException | ExecutionException e) {
            log.error(e. getMessage());
        } catch (InterruptedException e){
            log.error(e. getMessage());
            Thread.currentThread().interrupt();
        }

        final OAuthRequest request = new OAuthRequest(Verb.GET, url);
        request.addHeader("x-li-format", "json");
        request.addHeader("Accept-Language", "fr-FR");
        linkedinService.signRequest(accessToken, request);

        try(Response response = linkedinService.execute(request)) {
            return response.getBody();
        } catch (IOException | ExecutionException e) {
            log.error(e. getMessage());
        } catch (InterruptedException e){
            log.error(e. getMessage());
            Thread.currentThread().interrupt();
        }

        return null;
    }
}