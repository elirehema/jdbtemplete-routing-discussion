package org.ospic.platform.infrastructure.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This file was created by eli on 08/07/2021 for org.ospic.platform.infrastructure.fcm
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
@Service
public class FcmInitialization {
    Logger logger = LoggerFactory.getLogger(FcmInitialization.class);

    @PostConstruct
    void initialize() {
        try {
        FileInputStream fcmCredentials = new FileInputStream("./fcm_service_account.json");

        FirebaseOptions options  = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(fcmCredentials))
                .build();

        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
            logger.info("Firebase application has been initialized");
        }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
