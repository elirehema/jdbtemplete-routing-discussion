package org.ospic.platform.mastertenant.service;


import org.ospic.platform.mastertenant.entity.MasterTenant;
import org.springframework.stereotype.Component;

/**
 * @author Md. Amran Hossain
 */
@Component
public interface MasterTenantService {

    MasterTenant findByClientId(Integer clientId);
    MasterTenant findByTenantName(String dbName);
}
