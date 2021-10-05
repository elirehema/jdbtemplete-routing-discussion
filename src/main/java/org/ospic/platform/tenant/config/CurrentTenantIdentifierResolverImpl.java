package org.ospic.platform.tenant.config;

import org.ospic.platform.mastertenant.config.DBContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.ospic.platform.util.constants.MultiTenantConstants;

/**
 * @author Md. Amran Hossain
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT_ID = MultiTenantConstants.DEFAULT_DATABASE_TENANT_NAME;

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = DBContextHolder.getCurrentDb();
        //System.out.println("Tenant: "+ tenant);
        return StringUtils.isNotEmpty(tenant) ? tenant : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
