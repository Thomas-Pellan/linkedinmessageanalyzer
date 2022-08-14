package fr.pellan.api.linkedinmessageanalyzer.auth;

import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinkedinAuth {

    private OAuth20Service authService;

    private static LinkedinAuth instance;

    public static LinkedinAuth getInstance(OAuth20Service authService)
    {
        if (instance == null)
        {
            instance = new LinkedinAuth(authService);
        }
        return instance;
    }

    public static LinkedinAuth getInstance()
    {
        return instance;
    }
}
