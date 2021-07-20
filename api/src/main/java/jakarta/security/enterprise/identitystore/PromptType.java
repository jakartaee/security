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

import static java.util.Objects.isNull;

/**
 * Prompt specifies whether the Authorization Server prompts the End-User for
 * re-authentication and consent.
 *
 * @author Gaurav Gupta
 */
public enum PromptType {

    /**
     * The Authorization Server must not display any authentication or consent 
     * user interface pages. An error is returned if an End-User is not already 
     * authenticated or the Client does not have pre-configured consent for the 
     * requested Claims.
     * ErrorCode : login_required, interaction_required
     */
    NONE,

    /**
     * The Authorization Server SHOULD prompt the End-User for reauthentication.
     * If it cannot reauthenticate the End-User, it MUST return an error.
     * ErrorCode : login_required
     * 
     */
    LOGIN,

    /**
     * The Authorization Server SHOULD prompt the End-User for consent before 
     * returning information to the Client. If it cannot obtain consent, it must
     * return an error.
     * ErrorCode : consent_required
     * 
     */
    CONSENT,

    /**
     * The Authorization Server SHOULD prompt the End-User to select a user 
     * account. If it cannot obtain an account selection choice made by the 
     * end-user, it must return an error.
     * ErrorCode : account_selection_required
     * 
     */
    SELECT_ACCOUNT;

    public static PromptType fromString(String key) {
        return isNull(key) || key.trim().isEmpty() ? null : valueOf(key.toUpperCase());
    }

}
