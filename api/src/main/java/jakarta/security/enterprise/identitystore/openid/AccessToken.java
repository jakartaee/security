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

import java.util.Map;

/**
 * The Access Token is used by an application to access protected resources.
 *
 * @author jGauravGupta
 * @author Rudy De Busscher
 */
public interface AccessToken {

    /**
     * @return The access token
     */
    String getToken();

    /**
     * Signify, if access token is JWT based, or opaque.
     *
     * @return true if access token is JWT token.
     */
    boolean isJWT();

    /**
     * Access token's claims
     *
     * @return access token claims if it is a JWT Token, {@link JwtClaims#NONE} otherwise.
     */
    JwtClaims getJwtClaims();

    /**
     * @return the access token's claims that was received from the OpenId Connect
     * provider
     */
    Map<String, Object> getClaims();

    /**
     * @param key the claim key
     * @return the identity token's claim based on requested key type or null if not provided
     */
    Object getClaim(String key);

    /**
     * Optional. Expiration time of the Access Token in seconds since the
     * response was generated.
     *
     * @return the expiration time of the Access Token or null if expiration time is not known
     */
    Long getExpirationTime();

    /**
     * Checks if the Access Token is expired, taking into account the min
     * validity time configured by the user.
     *
     * @return {@code true}, if access token is expired or it will be expired in
     * the next X milliseconds configured by user.
     */
    boolean isExpired();

    /**
     * Optional. Scope of the Access Token.
     *
     * @return the scope of the Access Token
     */
    Scope getScope();

    /**
     * @return the Type of the Access Token
     */
    Type getType();

    enum Type {
        BEARER, // Json Web Token (JWT) format
        MAC; // Message Authentication Code format
    }
}
