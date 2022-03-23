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
package jakarta.security.enterprise.authentication.mechanism.http;

import static jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdConstant.CODE;
import static jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdConstant.EMAIL_SCOPE;
import static jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdConstant.OPENID_SCOPE;
import static jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdConstant.PROFILE_SCOPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.security.enterprise.authentication.mechanism.http.openid.ClaimsDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.openid.DisplayType;
import jakarta.security.enterprise.authentication.mechanism.http.openid.LogoutDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdProviderMetadata;
import jakarta.security.enterprise.authentication.mechanism.http.openid.PromptType;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;



/**
 * Annotation used to define a container authentication mechanism that implements
 * the Authorization Code flow and Refresh tokens as defined by the OpenId Connect specification
 * and make that implementation available as an enabled CDI bean.
 *
 * <p>
 * Attributes on this annotation make up the OpenID connect client configuration. Expression Language
 * expressions in attributes of type <code>String</code> are evaluated.
 *
 * <p>
 * It can make use of the user endpoint for retrieving claims about the user.
 *
 * <p>
 * Note that in the OpenID terminology the authentication mechanism becomes a "Relying Party" (RP)
 * that redirects the "End-User" (caller) to an "OpenId Connect Provider" (Identity Provider).
 * Authentication takes place between the user and the Identity Provider, where the result of this
 * authentication is communicated back to the authentication mechanism.
 *
 * <p>
 * This is depicted in the following non-normative diagram:
 *
 * <pre>
 *  +--------+                                                       +--------+
 *  |        |                                                       |        |
 *  |        |---------------(1) Authentication Request------------->|        |
 *  |        |                                                       |        |
 *  |        |       +--------+                                      |        |
 *  |        |       |  End-  |&lt;--(2) Authenticates the End-User---->|        |
 *  |   RP   |       |  User  |                                      |   OP   |
 *  |        |       +--------+                                      |        |
 *  |        |                                                       |        |
 *  |        |&lt;---------(3) Returns Authorization code---------------|        |
 *  |        |                                                       |        |
 *  |        |---------(3b)                                          |        |
 *  |        |           | Redirect to original resource (if any)    |        |
 *  |        |&lt;----------+                                           |        |
 *  |        |                                                       |        |
 *  |        |------------------------------------------------------>|        |
 *  |        |   (4) Request to TokenEndpoint for Access / Id Token  |        |
 *  | OpenId |&lt;------------------------------------------------------| OpenId |
 *  | Connect|                                                       | Connect|
 *  | Client | ----------------------------------------------------->|Provider|
 *  |        |   (5) Fetch JWKS to validate ID Token                 |        |
 *  |        |&lt;------------------------------------------------------|        |
 *  |        |                                                       |        |
 *  |        |------------------------------------------------------>|        |
 *  |        |   (6) Request to UserInfoEndpoint for End-User Claims |        |
 *  |        |&lt;------------------------------------------------------|        |
 *  |        |                                                       |        |
 *  +--------+                                                       +--------+
 * </pre>
 *
 * <p>
 * Because of the way this authentication mechanism and protocol works, there is no
 * requirement to explicitly define an identity store. However, the authentication
 * mechanism MUST validate the token received from the "TokenEndpoint" by calling
 * the {@link IdentityStoreHandler}. This allows for extra identity stores and/or
 * a custom IdentityStoreHandler to participate in the final authentication result
 * (e.g. adding extra groups).
 *
 *
 * @see https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth
 * @see https://openid.net/specs/openid-connect-core-1_0.html#RefreshTokens
 *
 * @author Gaurav Gupta
 * @author Rudy De Busscher
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface OpenIdAuthenticationMechanismDefinition {

    /**
     * Required, unless providerMetadata is specified.
     * The provider URI to read / discover the metadata of the openid provider.
     *
     * @see http://openid.net/specs/openid-connect-discovery-1_0.html
     *
     * @return provider URI to read from which to read metadata
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
     * Required. The client identifier issued when the application was
     * registered.
     *
     * @return the client identifier
     */
    String clientId() default "";

    /**
     * Required. The client secret.
     *
     * <p>
     * Note that it is strongly recommended to set this using an Expression so that the value
     * is not hardcoded within the code.
     *
     * @return The client secret
     */
    String clientSecret() default "";

    /**
     * Optional. The claims definition defines the custom claims mapping of
     * caller name and groups.
     *
     * @return
     */
    ClaimsDefinition claimsDefinition() default @ClaimsDefinition;

    /**
     * Optional. The Logout definition defines the logout and Relaying Party session
     * management configuration.
     *
     * @return
     */
    LogoutDefinition logout() default @LogoutDefinition;

    /**
     * The redirect URI (callback URI) to which the response will be sent by the OpenId
     * Connect Provider. This URI must exactly match one of the Redirection URI values
     * for the Client pre-registered at the OpenID Provider.
     *
     * @return
     */
    String redirectURI() default "${baseURL}/Callback";

    /**
     * Optional. Automatically redirects the caller (the end-user) from
     * the redirect URI defined by the <code>redirectURI</code> attribute
     * to the resource the end-user originally requested in a "login to continue"
     * scenario.
     *
     * <p>
     * After arriving at the original requested resource, the runtime restores
     * the request as it originally happened, including cookies, headers, the
     * request method and the request parameters in the same way as done when
     * using the {@link LoginToContinue} feature.
     *
     * @return
     */
    boolean redirectToOriginalResource() default false;

    /**
     * Optional. Allows the <code>redirectToOriginalResource</code> to be specified as
     * Jakarta Expression Language expression.
     * If set, overrides the value defined by the <code>redirectToOriginalResource</code> value.
     *
     * @return
     */
    String redirectToOriginalResourceExpression() default "";

    /**
     * Optional. The scope value defines the access privileges. The basic (and
     * required) scope for OpenID Connect is the openid scope.
     *
     * @return
     */
    String[] scope() default {OPENID_SCOPE, EMAIL_SCOPE, PROFILE_SCOPE};

    /**
     * Optional. Allows The scope value to be specified as Jakarta Expression Language expression.
     * If Set, overrides any values set by scope.
     *
     * @return
     */
    String scopeExpression() default "";

    /**
     * Optional. Response Type value defines the processing flow to be used. By
     * default, the value is code (Authorization Code Flow).
     *
     * @return
     */
    String responseType() default CODE;

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
     * Optional. Allows the  prompt value to be specified as Jakarta Expression Language expression.
     * If Set, overirdes the value defined by the prompt value.
     *
     * @return
     */
    String promptExpression() default "";

    /**
     * Optional. The display value specifying how the authorization server
     * displays the authentication and consent user interface pages.
     *
     * @return
     */
    DisplayType display() default DisplayType.PAGE;

    /**
     * Optional. Allows the display value to be specified as Jakarta Expression Language expression.
     * If set, overrides the value defined by display.
     *
     * @return
     */
    String displayExpression() default "";

    /**
     * Optional. Enables string value used to mitigate replay attacks.
     *
     * @return
     */
    boolean useNonce() default true;

    /**
     * Optional. Allows the nonce activation to be specified as Jakarta Expression Language expression.
     * If set, overrides the value defined by the useNonce value.
     *
     * @return
     */
    String useNonceExpression() default "";

    /**
     * Optional. If enabled the state, nonce values and original requested resource data are stored in an HTTP session
     * otherwise in cookies.
     *
     * @return
     */
    boolean useSession() default true;

    /**
     * Optional. Allows the configuration of the session through a Jakarta Expression Language expression.
     * If set, overwrites the value of useSession value.
     *
     * @return
     */
    String useSessionExpression() default "";

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
     * Allows the extra parameters to be defined as a Jakarta Expression Language expression.
     * If set, overrides the extraParameters value.
     *
     * @return
     */
    String extraParametersExpression() default "";

    /**
     * Optional. Sets the connect timeout(in milliseconds) for Remote JWKS
     * retrieval. Value must not be negative and if value is zero then infinite
     * timeout.
     *
     * @return
     */
    int jwksConnectTimeout() default 500;

    /**
     * Optional. Allows the connect timeout(in milliseconds) for Remote JWKS to be defined as
     * Jakarta Expression Language expression.
     * If set, overwrites the  jwksConnectTimeout value.
     *
     * @return
     */
    String jwksConnectTimeoutExpression() default "";

    /**
     * Optional. Sets the read timeout(in milliseconds) for Remote JWKS
     * retrieval. Value must not be negative and if value is zero then infinite
     * timeout.
     *
     * @return
     */
    int jwksReadTimeout() default 500;

    /**
     * Optional. Allows the read timeout(in milliseconds) for Remote JWKS
     * retrieval to be defined as Jakarta Expression Language expression.
     * If set, overwrites the jwksReadTimeout value.
     *
     * @return
     */
    String jwksReadTimeoutExpression() default "";

    /**
     * Optional. Enables or disables the automatically performed refresh of
     * Access and Refresh Token.
     *
     * @return {@code true}, if Access and Refresh Token shall be refreshed
     * automatically when they are expired.
     */
    boolean tokenAutoRefresh() default false;

    /**
     * Optional. Allows the automatically performed refresh of
     * Access and Refresh Token to be defined as Jakarta Expression Language expression.
     * If set, overwrites the value of  tokenAutoRefresh.
     */
    String tokenAutoRefreshExpression() default "";

    /**
     * Optional. Sets the minimum validity time in milliseconds the Access Token
     * must be valid before it is considered expired. Value must not be
     * negative.
     *
     * @return
     */
    int tokenMinValidity() default 10 * 1000;

    /**
     * Optional. Allows the minimum validity time in milliseconds the Access Token
     * must be valid before it is considered expired to be defined as Jakarta Expression Language expression.
     * If Set, overwrites the tokenMinValidity value.
     *
     * @return
     */
    String tokenMinValidityExpression() default "";
}
