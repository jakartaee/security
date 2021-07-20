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
package jakarta.security.enterprise.identitystore;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link OpenIdProviderMetadata} annotation overrides the openid connect
 * provider's endpoint value, discovered using providerUri.
 *
 * @author Gaurav Gupta
 * @author Rudy De Busscher
 */
@Retention(RUNTIME)
public @interface OpenIdProviderMetadata {

    /**
     * Required. The URL for the OAuth2 provider to provide authentication.
     * This must be a https endpoint.
     *
     * @return URL for the OAuth2 provider.
     */
    String authorizationEndpoint() default "";

    /**
     * Required. The URL for the OAuth2 provider to give the authorization token

     *
     * @return URL for the OAuth2 provider.
     */
    String tokenEndpoint() default "";

    /**
     * Required. An OAuth 2.0 Protected Resource that returns Claims about the
     * authenticated End-User.
     *
     * @return URL for User Info.
     */
    String userinfoEndpoint() default "";

    /**
     * Optional. OP endpoint to notify that the End-User has logged out of the
     * site and might want to log out of the OP as well.
     *
     * @return URL for logging out of server session.
     */
    String endSessionEndpoint() default "";

    /**
     * Required. An OpenId Connect Provider's JSON Web Key Set document
     * <p>
     * This contains the signing key(s) the RP uses to validate signatures from
     * the OP. The JWK Set may also contain the Server's encryption key(s),
     * which are used by RPs to encrypt requests to the Server.
     * </p>
     *
     * @return URL pointing to the JWK Set.
     */
    String jwksURI() default "";

}
