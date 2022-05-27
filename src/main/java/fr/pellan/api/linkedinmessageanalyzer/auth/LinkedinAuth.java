package fr.pellan.api.linkedinmessageanalyzer.auth;

import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LinkedinAuth {

    private OAuth20Service authService;

    private static LinkedinAuth INSTANCE;

    public static LinkedinAuth getInstance(OAuth20Service authService)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new LinkedinAuth(authService);
        }
        return INSTANCE;
    }

    public static LinkedinAuth getInstance()
    {
        return INSTANCE;
    }
}
