/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   2021 : Payara Foundation and/or its affiliates
 *      Initially authored in Security Connectors
 */
package jakarta.security.enterprise.identitystore.openid;


import jakarta.json.JsonObject;
import jakarta.security.enterprise.identitystore.LogoutDefinition;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

/**
 * An injectable interface that provides access to access token, identity token,
 * claims and OpenId Connect provider related information.
 *
 * @author Gaurav Gupta
 */
public interface OpenIdContext extends Serializable {

    /**
     * @return the caller name of the validated caller
     */
    String getCallerName();

    /**
     * @return the groups associated with the caller
     */
    Set<String> getCallerGroups();

    /**
     * Subject Identifier. A locally unique and never reassigned identifier
     * within the Issuer for the End-User, which is intended to be consumed by
     * the Client
     *
     * @return the subject identifier
     */
    String getSubject();

    /**
     * Gets the token type value. The value MUST be Bearer or another token_type
     * value that the Client has negotiated with the Authorization Server.
     *
     * @return the token type value
     */
    String getTokenType();

    /**
     * @return the authorization token that was received from the OpenId Connect
     * provider
     */
    AccessToken getAccessToken();

    /**
     * @return the identity token that was received from the OpenId Connect
     * provider
     */
    IdentityToken getIdentityToken();

    /**
     * @return the refresh token that can be used to get a new access token
     */
    Optional<RefreshToken> getRefreshToken();

    /**
     * @return the time that the access token is granted for, if it is set to
     * expire
     */
    Optional<Long> getExpiresIn();

    /**
     * Gets the User Claims that was received from the userinfo endpoint
     *
     * @return the claims json
     */
    JsonObject getClaimsJson();

    /**
     * Gets the User Claims that was received from the userinfo endpoint
     *
     * @return the {@link OpenIdClaims} instance
     */
    OpenIdClaims getClaims();

    /**
     * @return the OpenId Connect Provider's metadata document fetched via provider URI.
     */
    JsonObject getProviderMetadata();

    /**
     * Invalidates the RP's active OpenId Connect session and if
     * {@link LogoutDefinition#notifyProvider}
     * set to {@code true} then redirect the End-User's User Agent to the
     * {@code end_session_endpoint} to notify the OP that the user has logged
     * out of the RP's application and ask the user whether they want to logout
     * from the OP as well. After successful logout, the End-User's User Agent
     * redirect back to the RP's {@code post_redirect_uri}
     * configured via
     * {@link LogoutDefinition#redirectURI}
     *
     * @param request
     * @param response
     */
    void logout(HttpServletRequest request, HttpServletResponse response);


    /**
     * Retrieves the Stored value from Storage Controller.
     * @param request
     * @param response
     * @param key
     * @param <T>
     * @return
     */
    <T> Optional<T> getStoredValue(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String key);
}
