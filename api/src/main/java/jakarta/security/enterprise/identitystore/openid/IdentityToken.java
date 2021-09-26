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
 * Identity tokens is a security token that issued in authentication flow
 * process.
 *
 * @author jGauravGupta
 * @author Rudy De Busscher
 */
public interface IdentityToken {

    /**
     * @return the identity token
     */
    String getToken();

    /**
     * Claims of this token
     * @return claims of this token
     */
    JwtClaims getJwtClaims();

    /**
     * Checks if the Identity Token is expired.
     *
     * @return {@code true}, if identity token is expired or it will be expired in
     * the next X milliseconds configured by user.
     */
    boolean isExpired();

    /**
     * @return the identity token's claims that was received from the OpenId
     * Connect provider
     */
    Map<String, Object> getClaims();

}
