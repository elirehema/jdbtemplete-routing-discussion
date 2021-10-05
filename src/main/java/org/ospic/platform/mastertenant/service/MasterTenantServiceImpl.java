package org.ospic.platform.mastertenant.service;

import org.ospic.platform.mastertenant.entity.MasterTenant;
import org.ospic.platform.mastertenant.repository.MasterTenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Md. Amran Hossain
 */
@Service
public class MasterTenantServiceImpl implements MasterTenantService{

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantServiceImpl.class);

    @Autowired
    MasterTenantRepository masterTenantRepository;


    @Override
    public MasterTenant findByClientId(Integer clientId) {
        //LOG.info("findByClientId() method call..");
        return masterTenantRepository.findByTenantClientId(clientId);
    }

    @Override
    public MasterTenant findByTenantName(String tenantName) {
       // LOG.info("Find Tenant by tenant-name: "+tenantName);
        return masterTenantRepository.findByTenantName(tenantName);
    }
}
