package org.ospic.platform.tenant.app.admission.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * This file was created by eli on 28/11/2020 for org.ospic.platform.tenant.app.admission.api
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
public class AdmissionsApiResourcesSwagger {
    public AdmissionsApiResourcesSwagger(){}
    @ApiModel(value = "GetAdmissionsApiResources")
    public static  class GetAdmissionsApiResources{
        private GetAdmissionsApiResources(){}
        @ApiModelProperty(example = "4")
        private  Long id;
        @ApiModelProperty(example = "28/11/2020")
        private  Date startDate;
        @ApiModelProperty(example = "28/11/2020")
        private  Date endDate;
        @ApiModelProperty(example = "true")
        private  Boolean isActive;
        @ApiModelProperty(example = "28")
        private  Long wardId;
        @ApiModelProperty(example = "211")
        private  Long bedId;
        @ApiModelProperty(example = "BD211WD28")
        private  String bedIdentifier;
        @ApiModelProperty(example = "WD28")
        private  String wardName;
    }
}
