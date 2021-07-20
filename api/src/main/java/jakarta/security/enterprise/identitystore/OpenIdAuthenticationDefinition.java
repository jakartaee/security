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

import jakarta.security.enterprise.identitystore.openid.OpenIdConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link OpenIdAuthenticationDefinition} annotation defines openid connect
 * client configuration and The value of each parameter can be defined as Expression.
 *
 * @author Gaurav Gupta
 * @author Rudy De Busscher
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface OpenIdAuthenticationDefinition {

    /**
     * Required. The provider uri (
     * http://openid.net/specs/openid-connect-discovery-1_0.html ) to read /
     * discover the metadata of the openid provider.
     *
     * @return
     */
    String providerURI() default "";

    /**
     * To override the openid connect provider's metadata property discovered
     * via providerUri.
     *
     * @return OpenIdProviderMetadata instance.
     */
    OpenIdProviderMetadata providerMetadata() default @OpenIdProviderMetadata;

    /**
     * Optional. The claims definition defines the custom claims mapping of
     * caller name and groups.
     *
     * @return
     */
    ClaimsDefinition claimsDefinition() default @ClaimsDefinition;

    /**
     * Optional. The Logout definition defines the logout and RP session
     * management configuration.
     *
     * @return
     */
    LogoutDefinition logout() default @LogoutDefinition;

    /**
     * Required. The client identifier issued when the application was
     * registered.
     *
     * @return the client identifier
     */
    String clientId() default "";

    /**
     * Required. The client secret, it is recommended to set this using an Expression so that value
     * is not hardcoded within the code..
     *
     * @return
     */
    String clientSecret() default "";

    /**
     * The redirect URI to which the response will be sent by OpenId Connect
     * Provider. This URI must exactly match one of the Redirection URI values
     * for the Client pre-registered at the OpenID Provider.
     *
     * @return
     */
    String redirectURI() default "${baseURL}/Callback";

    /**
     * Optional. The scope value defines the access privileges. The basic (and
     * required) scope for OpenID Connect is the openid scope.
     *
     * @return
     */
    String[] scope() default {OpenIdConstant.OPENID_SCOPE, OpenIdConstant.EMAIL_SCOPE, OpenIdConstant.PROFILE_SCOPE};

    /**
     * Optional. Response Type value defines the processing flow to be used. By
     * default, the value is code (Authorization Code Flow).
     *
     * @return
     */
    String responseType() default OpenIdConstant.CODE;

    /**
     * Optional. Informs the Authorization Server of the mechanism to be used
     * for returning parameters from the Authorization Endpoint.
     *
     * @return
     */
    String responseMode() default "";

    /**
     * Optional. The prompt value specifies whether the authorization server
     * prompts the user for reauthentication and consent. If no value is
     * specified and the user has not previously authorized access, then the
     * user is shown a consent screen.
     *
     * @return
     */
    PromptType[] prompt() default {};

    /**
     * Optional. The display value specifying how the authorization server
     * displays the authentication and consent user interface pages.
     *
     * @return
     */
    DisplayType display() default DisplayType.PAGE;

    /**
     * Optional. Enables string value used to mitigate replay attacks.
     *
     * @return
     */
    boolean useNonce() default true;

    /**
     * Optional. If enabled state & nonce value stored in session otherwise in
     * cookies.
     *
     * @return
     */
    boolean useSession() default true;

    /**
     * An array of extra options that will be sent to the OAuth provider.
     * <p>
     * These must be in the form of {@code "key=value"} i.e.
     * <code> extraParameters={"key1=value", "key2=value2"} </code>
     *
     * @return
     */
    String[] extraParameters() default {};

    /**
     * Optional. Sets the connect timeout(in milliseconds) for Remote JWKS
     * retrieval. Value must not be negative and if value is zero then infinite
     * timeout.
     *
     * @return
     */
    int jwksConnectTimeout() default 500;

    /**
     * Optional. Sets the read timeout(in milliseconds) for Remote JWKS
     * retrieval. Value must not be negative and if value is zero then infinite
     * timeout.
     *
     * @return
     */
    int jwksReadTimeout() default 500;

    /**
     * Optional. Enables or disables the automatically performed refresh of
     * Access and Refresh Token.
     *
     * @return {@code true}, if Access and Refresh Token shall be refreshed
     * automatically when they are expired.
     */
    boolean tokenAutoRefresh() default false;

    /**
     * Optional. Sets the minimum validity time in milliseconds the Access Token
     * must be valid before it is considered expired. Value must not be
     * negative.
     *
     * @return
     */
    int tokenMinValidity() default 10 * 1000;
}
