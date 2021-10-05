package org.ospic.platform.tenant.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
/**
 * This file was created by eli on 22/12/2020 for org.ospic.platform.configurations
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
public class FlywayConfiguration {

    @Autowired
    public FlywayConfiguration(DataSource dataSource, String classPathLocation) {
        Flyway.configure()
                .baselineOnMigrate(true)
                .table("schema_version")
                .sqlMigrationPrefix("V")
                .sqlMigrationSeparator("__")
                .sqlMigrationSuffixes(".sql")
                .locations(classPathLocation)
                .baselineOnMigrate(true)
                .connectRetries(10)
                .validateOnMigrate(false)
                .target(MigrationVersion.LATEST)
                .dataSource(dataSource).load().migrate();
    }
}

