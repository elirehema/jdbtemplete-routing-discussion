package org.ospic.platform.util.constants;

public interface MultiTenantConstants {
    String DEFAULT_DATABASE_TENANT_NAME = "default";
    String CURRENT_TENANT_IDENTIFIER = "default";

    String TENANTS_CLASSPATH_LOCATION = "classpath:db/tenants";
    String MASTER_TENANT_CLASSPATH_LOCATION = "classpath:db/masterdb";
}
