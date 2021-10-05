package org.ospic.platform.infrastructure.app.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This file was created by eli on 19/01/2021 for org.ospic.platform.infrastructure.app.exceptions
 * --
 * --
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformDataIntegrityException extends RuntimeException {

    private final String globalisationMessageCode;
    private final String defaultUserMessage;
    private final String parameterName;
    private final Object[] defaultUserMessageArgs;

    protected PlatformDataIntegrityException(final String globalisationMessageCode, final String defaultUserMessage,
                                          final Object... defaultUserMessageArgs) {
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.parameterName = null;
        this.defaultUserMessageArgs = defaultUserMessageArgs;
    }

    protected PlatformDataIntegrityException(final String globalisationMessageCode, final String defaultUserMessage,
                                          final String parameterName, final Object... defaultUserMessageArgs) {
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.parameterName = parameterName;
        this.defaultUserMessageArgs = defaultUserMessageArgs;
    }
}
