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

import java.time.Instant;
import java.util.*;

public interface Claims {
    /**
     * Get String claim of given name
     *
     * @param name
     * @return value, or empty optional if not present
     * @throws IllegalArgumentException when value of claim is not a string
     */
    Optional<String> getStringClaim(String name);

    /**
     * Get Numeric Date claim of given name
     *
     * @param name
     * @return value, or empty optional if not present
     * @throws IllegalArgumentException when value of claim is not a number that represents an epoch seconds
     */
    Optional<Instant> getNumericDateClaim(String name);

    /**
     * Get String List claim of given name
     *
     * @param name
     * @return a list with values of the claim, or empty list if value is not present.
     * @throws IllegalArgumentException when value of claim is neither string or array of strings
     */
    List<String> getArrayStringClaim(String name);

    /**
     * Get integer claim of given name
     *
     * @param name
     * @return value, or empty optional if not present
     * @throws IllegalArgumentException when value of claim is not a number
     */
    OptionalInt getIntClaim(String name);

    /**
     * Get long claim of given name
     *
     * @param name
     * @return value, or empty optional if not present
     * @throws IllegalArgumentException when value of claim is not a number
     */
    OptionalLong getLongClaim(String name);

    /**
     * Get double claim of given name
     *
     * @param name
     * @return value, or empty optional if not present
     * @throws IllegalArgumentException when value of claim is not a number
     */
    OptionalDouble getDoubleClaim(String name);

    /**
     * Get nested claims of given name.
     * @param name
     * @return Claims instance represented nested values within that claim, or empty optional if not present
     * @throws IllegalArgumentException when value is not a nested object
     */
    Optional<Claims> getNested(String name);
}
