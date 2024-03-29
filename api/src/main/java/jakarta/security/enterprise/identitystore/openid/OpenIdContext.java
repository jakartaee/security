/*
 * Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 *
 */

/*
 *
 * Contributors:
 *   2021 : Payara Foundation and/or its affiliates
 *      Initially authored in Security Connectors
 */
package jakarta.security.enterprise.identitystore.openid;


import java.io.Serializable;
import java.util.Optional;

import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * An injectable interface that provides access to access token, identity token,
 * claims and OpenId Connect provider related information.
 *
 * @author Gaurav Gupta
 */
public interface OpenIdContext extends Serializable {

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
     * Gets the User Claims that were received from the userinfo endpoint
     *
     * @return the claims json
     */
    JsonObject getClaimsJson();

    /**
     * Gets the User Claims that were received from the userinfo endpoint
     *
     * @return the {@link OpenIdClaims} instance
     */
    OpenIdClaims getClaims();

    /**
     * @return the OpenId Connect Provider's metadata document fetched via provider URI.
     */
    JsonObject getProviderMetadata();

    /**
     * Retrieves the Stored value from Storage Controller.
     *
     * @param request
     * @param response
     * @param key
     * @param <T>
     * @return
     */
    <T> Optional<T> getStoredValue(HttpServletRequest request, HttpServletResponse response, String key);
}
