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

import java.util.LinkedHashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

/**
 *
 * @author Gaurav Gupta
 * @author Rudy De Busscher
 */
public class Scope extends LinkedHashSet<String> {

    public Scope() {
    }

    public Scope(final List<String> values) {
        addAll(values);
    }

    public static Scope parse(final String scopeValue) {

        if (isNull(scopeValue)) {
            return null;
        }

        Scope scope = new Scope();

        if (scopeValue.trim().isEmpty()) {
            return scope;
        }

        // Multiple scope values may be used by creating a space delimited
        scope.addAll(asList(scopeValue.split(" ")));
        return scope;
    }

    @Override
    public String toString() {
        return String.join(" ", this);
    }

}
