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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link ClaimsDefinition} annotation defines claims mapping in openid connect
 * client configuration.
 *
 * @author jGauravGupta
 * @author Rudy De Busscher
 */
@Retention(RUNTIME)
public @interface ClaimsDefinition {


    /**
     * Maps the callerNameClaim's value to caller name value in
     * jakarta.security.enterprise.identitystore.IdentityStore#validate
     *
     *
     * @return Claim name to be used as caller.
     */
    String callerNameClaim() default OpenIdConstant.PREFERRED_USERNAME;

    /**
     * Maps the callerGroupsClaim's value to caller groups value in
     * jakarta.security.enterprise.identitystore.IdentityStore#validate
     *
     *
     * @return Claim name to be used as caller Group.
     */
    String callerGroupsClaim() default OpenIdConstant.GROUPS;

}
