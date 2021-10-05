package org.ospic.platform.tenant.app.admission.service;

import org.ospic.platform.tenant.app.domain.CustomReponseMessage;
import org.ospic.platform.tenant.app.admission.data.AdmissionRequest;
import org.ospic.platform.tenant.app.admission.data.EndAdmissionRequest;
import org.ospic.platform.tenant.app.admission.domain.Admission;
import org.ospic.platform.tenant.app.admission.exception.AdmissionEndDateException;
import org.ospic.platform.tenant.app.admission.exception.AdmissionNotFoundExceptionPlatform;
import org.ospic.platform.tenant.app.admission.exception.InactiveAdmissionPlatformException;
import org.ospic.platform.tenant.app.admission.repository.AdmissionRepository;
import org.ospic.platform.tenant.app.beds.domains.Bed;
import org.ospic.platform.tenant.app.beds.exception.BedNotFoundExceptionPlatform;
import org.ospic.platform.tenant.app.beds.exception.OccupiedBedPlatformException;
import org.ospic.platform.tenant.app.beds.repository.BedRepository;
import org.ospic.platform.tenant.app.consultation.exception.ConsultationNotFoundExceptionPlatform;
import org.ospic.platform.tenant.app.consultation.exception.InactiveMedicalConsultationsException;
import org.ospic.platform.tenant.app.consultation.repository.ConsultationResourceJpaRepository;
import org.ospic.platform.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * This file was created by eli on 09/11/2020 for org.ospic.platform.tenant.app.admission.service
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
@Repository
public class AdmissionsWriteServiceImpl implements AdmissionsWriteService {
    private static final Logger logger = LoggerFactory.getLogger(AdmissionsWriteServiceImpl.class);
    @Autowired
    AdmissionRepository admissionRepository;
    @Autowired
    BedRepository bedRepository;
    @Autowired
    ConsultationResourceJpaRepository consultationResourceJpaRepository;

    public AdmissionsWriteServiceImpl(
            AdmissionRepository admissionRepository,
            BedRepository bedRepository,
            ConsultationResourceJpaRepository consultationResourceJpaRepository) {
        this.admissionRepository = admissionRepository;
        this.bedRepository = bedRepository;
        this.consultationResourceJpaRepository = consultationResourceJpaRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<?> admitPatient(AdmissionRequest admissionRequest) {
        final LocalDateTime startLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(admissionRequest.getStartDateTime());
        final LocalDateTime endLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(admissionRequest.getEndDateTime());

        return consultationResourceJpaRepository.findById(admissionRequest.getServiceId()).map(consultation -> {
            CustomReponseMessage cm = new CustomReponseMessage();
            HttpHeaders httpHeaders = new HttpHeaders();
            if (endLocalDateTime.isBefore(startLocalDateTime)) {
                throw new AdmissionEndDateException();
            }
            if (!consultation.getIsActive()) {
                throw new InactiveMedicalConsultationsException(consultation.getId());
            }
            if (consultation.getPatient().getIsAdmitted()) {
                String message = "Consultation already has active admission";
                String code = "error.msg.consultation.have.active.admission";
                throw new InactiveMedicalConsultationsException(code, message);
            }

            Bed bed = bedRepository.findById(admissionRequest.getBedId()).orElseThrow(() -> new BedNotFoundExceptionPlatform(admissionRequest.getBedId()));
            if (bed.getIsOccupied()) {
                throw new OccupiedBedPlatformException(bed.getId());
            }
            /*
             * Create new admission
             * **/
            Admission admission = new Admission(admissionRequest.getIsActive(), startLocalDateTime, endLocalDateTime);
            admission.setService(consultation);
            admission.setIsActive(true);
            admission.addBed(bed);
            admissionRepository.save(admission);

            /*
             * Update Bed set it as active. Not open for new admission
             *  **/
            bed.setIsOccupied(true);
            bedRepository.save(bed);

            /*
             * Update patient set as admitted to prevent re-admission
             * **/
            consultation.setIsAdmitted(true);
            consultation.getPatient().setIsAdmitted(true);
            consultationResourceJpaRepository.save(consultation);


            /*
             * Return Message
             * **/

            return ResponseEntity.ok().body(admissionRequest.getServiceId());
        }).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(admissionRequest.getServiceId()));
    }

    @Transactional
    @Override
    public ResponseEntity<?> endPatientAdmission(EndAdmissionRequest request) {
        final LocalDateTime endLocalDateTime = new DateUtil().convertToLocalDateTimeViaInstant(request.getEndDateTime());
        return consultationResourceJpaRepository.findById(request.getServiceId()).map(service -> {
            return admissionRepository.findById(request.getAdmissionId()).map(admission -> {
                return bedRepository.findById(request.getBedId()).map(bed -> {
                    if (!(admission.getIsActive() || service.getPatient().getIsAdmitted())) {
                        throw new InactiveAdmissionPlatformException(request.getAdmissionId());
                    }
                    if (endLocalDateTime.isBefore(admission.getFromDateTime())) {
                        throw new AdmissionEndDateException();
                    }

                    /** Update this service set as no longer admitted
                     * Update patient under this service set as no longer admitted **/
                    service.setIsAdmitted(false);
                    service.getPatient().setIsAdmitted(false);
                    consultationResourceJpaRepository.save(service);

                    /** Update Admission set as no longer active **/
                    admission.setIsActive(false);
                    admission.setToDateTime(endLocalDateTime);
                    admissionRepository.save(admission);

                    /** Update bed set as active open for another admission **/
                    bed.setIsOccupied(false);
                    bedRepository.save(bed);

                    String response = String.format("Admission %2d for service %s has being ended on %s ", request.getAdmissionId(), service.getPatient().getName(), request.getEndDateTime());
                    return ResponseEntity.ok().body(response);
                }).orElseThrow(() -> new BedNotFoundExceptionPlatform(request.getBedId()));
            }).orElseThrow(() -> new AdmissionNotFoundExceptionPlatform(request.getAdmissionId()));
        }).orElseThrow(() -> new ConsultationNotFoundExceptionPlatform(request.getServiceId()));

    }

    @Transactional
    @Override
    public ResponseEntity<String> updatePatientAdmissionInfo() {
        return null;
    }
}
