package com.oneassist.serviceplatform.services.authentication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.oneassist.serviceplatform.commons.entities.UserEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Basic Authorization
 * 
 * @author surender.jain
 */
@Configuration
@Component
public class AuthenticationService {

    private List<UserEntity> userEntities;

    public AuthenticationService() throws IOException {

        // Loads the user entities from json file, later on this can be moved to database.
        this.loadEntities();
    }

    public boolean authenticate(String authCredentials) {

        return this.doBasicAuthentication(authCredentials);
    }

    // Validates a user against Basic authentication credentials
    private boolean doBasicAuthentication(String encodedAuthCredentials) {

        boolean isUserAuthenticated = false;

        if (null != encodedAuthCredentials) {
            // Header is in the format "Basic 5tyc0uiDat4"
            // We need to extract data before decoding it back to original string
            String[] authParts = encodedAuthCredentials.split("\\s+");

            // Strips "Basic "
            if (authParts != null && authParts.length > 1) {
                String authInfo = authParts[1];

                // Decode the base64 string.
                String decodedAuthString = StringUtils.newStringUtf8(Base64.decodeBase64(authInfo));

                // Gets the userlogin and pwd from decoded string.
                String[] userIdAndPwd = decodedAuthString.split(":");

                // here you include your logic to validate user authentication. It can be using ldap, or token
                // exchange mechanism or your custom authentication mechanism.
                for (UserEntity userEntity : this.userEntities) {
                    if (userEntity.getUserLoginId().equals(userIdAndPwd[0]) && userEntity.getUserPassword().equals(userIdAndPwd[1])) {

                        isUserAuthenticated = true;
                        break;
                    }
                }
            }
        }

        return isUserAuthenticated;
    }

    private void loadEntities() throws IOException {
        URL usersFileUrl = this.getClass().getClassLoader().getResource("users.json");

        try (Reader reader = new InputStreamReader(usersFileUrl.openStream(), "UTF-8")) {

            Gson gson = new GsonBuilder().create();
            this.userEntities = gson.fromJson(reader, new TypeToken<List<UserEntity>>() {
            }.getType());
        }
    }
}
