package org.ospic.platform.tenant.app.admission.data;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * This file was created by eli on 28/11/2020 for org.ospic.platform.tenant.app.admission.data
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
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString
public class EndAdmissionRequest implements Serializable {
    private Long serviceId;
    private Long admissionId;
    private Date endDateTime;
    private Long bedId;

    public EndAdmissionRequest(Long serviceId, Long admissionId, Long bedId, Date endDateTime) {
        this.serviceId = serviceId;
        this.admissionId = admissionId;
        this.endDateTime = endDateTime;
        this.bedId  = bedId;
    }
}
