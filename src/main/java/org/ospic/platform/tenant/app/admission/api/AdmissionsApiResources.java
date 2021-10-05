package org.ospic.platform.tenant.app.admission.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ospic.platform.tenant.app.admission.data.AdmissionResponseData;
import org.ospic.platform.tenant.app.admission.repository.AdmissionRepository;
import org.ospic.platform.tenant.app.admission.service.AdmissionsReadService;
import org.ospic.platform.tenant.app.admission.service.AdmissionsWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.platform.tenant.app.admission.api
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@Component
@RequestMapping("/admissions")
@Api(value = "/admissions", tags = "Admissions", description = "Admissions API resources")
public class AdmissionsApiResources {
    private final AdmissionsWriteService admissionsWriteService;
    private final AdmissionsReadService admissionsReadService;
    private final AdmissionRepository admissionRepository;

    @Autowired
    public AdmissionsApiResources(AdmissionsWriteService admissionsWriteService,
                                  AdmissionsReadService admissionsReadService,
                                  AdmissionRepository admissionRepository) {
        this.admissionsWriteService = admissionsWriteService;
        this.admissionsReadService = admissionsReadService;
        this.admissionRepository = admissionRepository;
    }

    @PreAuthorize("hasAnyAuthority('ALL_FUNCTIONS','READ_CONSULTATION')")
    @ApiOperation(value = "RETRIEVE Admissions", notes = "RETRIEVE Admissions", response = AdmissionResponseData.class, responseContainer = "List")
    @RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> retrieveAllAdmissions() {
        return ResponseEntity.ok().body(admissionsReadService.retrieveAllAdmissions());
    }

}
