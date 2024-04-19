package com.example.techpowerhousebackend.support;

import com.example.techpowerhousebackend.support.exceptions.KeycloackRegistrationException;
import com.example.techpowerhousebackend.user.User;
import lombok.experimental.UtilityClass;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class Registration {

    // Metodo per registrare un utente su Keycloak
    public void keycloakRegistration(RegistrationRequest registrationRequest) throws KeycloackRegistrationException {
        try {
            // Parametri di configurazione per la connessione a Keycloak
            String usernameAdmin = "Romeodemetrio01@gmail.com";
            String passwordAdmin = "13072001";
            String clientName = "springboot-keycloak";
            String role = "user";
            String serverUrl = "http://localhost:8080";
            String realm = "techpowerhouse";

            // Creazione dell'istanza Keycloak
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientName)
                    .username(usernameAdmin)
                    .password(passwordAdmin)
                    .build();

            // Ottieni i dettagli dell'utente dalla richiesta di registrazione
            User user = registrationRequest.getUser();

            // Creazione della rappresentazione dell'utente per Keycloak
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setEnabled(true);
            userRepresentation.setUsername(user.getEmail());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setAttributes(Collections.singletonMap("origin", List.of("demo")));

            // Ottenimento delle risorse di Keycloak per il realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Creazione dell'utente su Keycloak
            Response response = usersResource.create(userRepresentation);
            System.out.printf("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);
            System.out.printf("User created with userId: %s%n", userId);

            // Impostazione della password per l'utente creato
            CredentialRepresentation passwordCredential = new CredentialRepresentation();
            passwordCredential.setTemporary(false);
            passwordCredential.setType(CredentialRepresentation.PASSWORD);
            passwordCredential.setValue(registrationRequest.getPassword());
            UserResource userResource = usersResource.get(userId);
            userResource.resetPassword(passwordCredential);

            // Assegnazione del ruolo all'utente
            RoleRepresentation userRealmRole = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(userRealmRole));

        } catch (Exception e) {
            // Gestione delle eccezioni
            throw new KeycloackRegistrationException();
        }
    }
}
