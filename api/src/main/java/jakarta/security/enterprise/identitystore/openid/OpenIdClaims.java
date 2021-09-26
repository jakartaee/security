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

import java.util.Optional;

import static jakarta.security.enterprise.identitystore.openid.OpenIdConstant.*;

/**
 * User Claims received from the userinfo endpoint.
 *
 * @author Gaurav Gupta
 * @author Rudy De Busscher
 */
public interface OpenIdClaims extends Claims {

    default String getSubject() {
        return getStringClaim(SUBJECT_IDENTIFIER).orElseThrow(() -> new IllegalArgumentException("Payload does not represent correct UserInfo response"));
    }

    default Optional<String> getName() {
        return getStringClaim(NAME);
    }

    default Optional<String> getFamilyName() {
        return getStringClaim(FAMILY_NAME);
    }

    default Optional<String> getGivenName() {
        return getStringClaim(GIVEN_NAME);
    }

    default Optional<String> getMiddleName() {
        return getStringClaim(MIDDLE_NAME);
    }

    default Optional<String> getNickname() {
        return getStringClaim(NICKNAME);
    }

    default Optional<String> getPreferredUsername() {
        return getStringClaim(PREFERRED_USERNAME);
    }

    default Optional<String> getProfile() {
        return getStringClaim(PROFILE);
    }

    default Optional<String> getPicture() {
        return getStringClaim(PICTURE);
    }

    default Optional<String> getGender() {
        return getStringClaim(GENDER);
    }

    default Optional<String> getBirthdate() {
        return getStringClaim(BIRTHDATE);
    }

    default Optional<String> getZoneinfo() {
        return getStringClaim(ZONEINFO);
    }

    default Optional<String> getLocale() {
        return getStringClaim(LOCALE);
    }

    default Optional<String> getUpdatedAt() {
        return getStringClaim(UPDATED_AT);
    }

    default Optional<String> getEmail() {
        return getStringClaim(EMAIL);
    }

    default Optional<String> getEmailVerified() {
        return getStringClaim(EMAIL_VERIFIED);
    }

    default Optional<String> getAddress() {
        return getStringClaim(ADDRESS);
    }

    default Optional<String> getPhoneNumber() {
        return getStringClaim(PHONE_NUMBER);
    }

    default Optional<String> getPhoneNumberVerified() {
        return getStringClaim(PHONE_NUMBER_VERIFIED);
    }

    default Optional<String> getWebsite() {
        return getStringClaim(WEBSITE);
    }


}
