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
 * Display specifies how the Authorization Server displays the authentication
 * and consent user interface pages to the End-User.
 *
 * @author Gaurav Gupta
 */
public enum DisplayType {

    /**
     * The Authorization Server should display authentication and consent UI
     * consistent with a full User-Agent page view. If the display parameter is
     * not specified this is the default display mode.
     *
     */
    PAGE,
    /**
     * The Authorization Server should display authentication and consent UI
     * consistent with a popup User-Agent window.
     *
     */
    POPUP,
    /**
     * The Authorization Server should display authentication and consent UI
     * consistent with a device that leverages a touch interface.
     *
     */
    TOUCH,
    /**
     * The Authorization Server should display authentication and consent UI
     * consistent with a "feature phone" type display.
     *
     */
    WAP;

    public static DisplayType fromString(String key) {
        return isNull(key) || key.trim().isEmpty() ? null : valueOf(key.toUpperCase());
    }

}
