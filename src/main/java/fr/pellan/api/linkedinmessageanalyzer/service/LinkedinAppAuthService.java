package fr.pellan.api.linkedinmessageanalyzer.service;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import fr.pellan.api.linkedinmessageanalyzer.auth.LinkedinAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class LinkedinAppAuthService {

    private static final String CREDENTIALS_DATA_SPLITTER = ":";
    private static final String AUTH_FILE_PATH = "auth/credentials.txt";

    public void buildAuthService(){

        OAuth20Service linkedinAuthService = new ServiceBuilder(getAppClientId())
                .apiSecret(getAppClientSecret())
                .defaultScope(new ScopeBuilder("r_liteprofile", "r_emailaddress"))
                .callback("http://localhost:8080/linkedin-connect/execute")
                .build(LinkedInApi20.instance());

        LinkedinAuth.getInstance(linkedinAuthService);
    }

    private String getAppClientId(){

        String content = readCredentialsFile();

        if(StringUtils.isNotBlank(content)){
            return content.split(CREDENTIALS_DATA_SPLITTER)[0];
        }

        log.error("getAppClientId: credentials file was empty");
        return null;
    }

    private String getAppClientSecret(){

        String content = readCredentialsFile();

        if(StringUtils.isNotBlank(content)){
            String[] credentials = content.split(CREDENTIALS_DATA_SPLITTER);
            if(credentials.length > 1){
                return credentials[1];
            }
        }

        log.error("getAppClientSecret : file does not contains both id and secret or is empty");
        return null;
    }

    private String readCredentialsFile(){

        String content = null;
        InputStream is = getClass().getClassLoader().getResourceAsStream(AUTH_FILE_PATH);

        if (is == null) {
            log.error("readCredentialsFile : could not read credentials file");
            return content;
        }

        try {
            content = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).readLine();
        } catch (IOException e) {
            log.error("readCredentialsFile : error during credentials file parse");
        }
        return content;
    }
}
