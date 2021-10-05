package org.ospic.platform.tenant.app.admission.service;

import org.ospic.platform.tenant.app.admission.data.AdmissionResponseData;
import org.ospic.platform.tenant.app.admission.domain.Admission;
import org.ospic.platform.tenant.app.admission.repository.AdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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
public class AdmissionsReadServiceImpl implements AdmissionsReadService {
    @Autowired
    private final AdmissionRepository admissionRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public AdmissionsReadServiceImpl(
            AdmissionRepository admissionRepository, DataSource dataSource) {
        this.admissionRepository = admissionRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Collection<AdmissionResponseData>  retrieveAllAdmissions() {
        final AdmissionResponseDataRowMapper rm = new AdmissionResponseDataRowMapper();
        final String sql = "select distinct " + rm.schema() + " order by a.id DESC ";
        final List<AdmissionResponseData> response = this.jdbcTemplate.query(sql, rm, new Object[]{});

        return response;
    }

    @Override
    public AdmissionResponseData  retrieveAdmissionById(Long admissionId) {
        final AdmissionResponseDataRowMapper rm = new AdmissionResponseDataRowMapper();
        final String sql = "select distinct " + rm.schema() + " where a.id = ?  order by a.id DESC ";
        final List<AdmissionResponseData> responseData = this.jdbcTemplate.query(sql, rm, new Object[]{admissionId});
        return  responseData.get(0);
    }

    @Override
    public ResponseEntity<List<AdmissionResponseData>> retrieveListOfAdmissionInBedId(Long bedId) {
        final String sql = "";

        return null;
    }

    @Override
    public ResponseEntity<?> retrieveListOfServiceAdmission(Long serviceId) {
        final AdmissionResponseDataRowMapper rm = new AdmissionResponseDataRowMapper();
        final String sql = "select distinct " + rm.schema() + "  where a.cid = ? order by a.id DESC; ";
        Collection<AdmissionResponseData> responseData =  this.jdbcTemplate.query(sql, rm, new Object[]{serviceId});
        return ResponseEntity.ok().body(responseData);
    }

    private static final class AdmissionResponseDataRowMapper implements RowMapper<AdmissionResponseData> {

        public String schema() {
            return " a.id as id, a.is_active as isActive, DATE_FORMAT(a.start_date, \"%W, %M %e %Y \")  as startDate, " +
                    " DATE_FORMAT(a.end_date, \"%W, %M %e %Y \") as endDate, ab. bed_id as bedId, sa.id as serviceId, " +
                    " b.ward_id as wardId, b.identifier bedIdentifier, w.name as wardName from m_admissions a " +
                    " inner join  admission_bed  ab ON ab.admission_id = a.id " +
                    " inner join m_consultations sa ON sa.id = a.cid " +
                    " inner join m_beds b on ab. bed_id = b.id " +
                    " inner join m_wards w on b.ward_id = w.id  " +
                    " ";
        }

        @Override
        public AdmissionResponseData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final Long id = rs.getLong("id");
            final String startDate = rs.getString("startDate");
            final String endDate = rs.getString("endDate");
            final boolean isActive = rs.getBoolean("isActive");
            final Long bedId = rs.getLong("bedId");
            final Long wardId = rs.getLong("wardId");
            final Long serviceId = rs.getLong("serviceId");
            final String bedIdentifier = rs.getString("bedIdentifier");
            final String wardName = rs.getString("wardName");

            return AdmissionResponseData.responseTemplate(id, startDate, endDate, isActive, wardId, bedId, wardName, bedIdentifier, serviceId);
        }
    }



}
