package org.ospic.platform.tenant.config;

import org.ospic.platform.mastertenant.config.DBContextHolder;
import org.ospic.platform.mastertenant.entity.MasterTenant;
import org.ospic.platform.mastertenant.repository.MasterTenantRepository;
import org.ospic.platform.util.DataSourceUtil;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.ospic.platform.util.constants.MultiTenantConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author Md. Amran Hossain
 */
@Configuration
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);

    private static final long serialVersionUID = 1L;

    private Map<String, DataSource> dataSourcesMtApp = new TreeMap<>();
    private Map<Object, Object> targetDataSources = new HashMap<>();

    @Autowired
    private MasterTenantRepository masterTenantRepository;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected DataSource selectAnyDataSource() {
        // This method is called more than once. So check if the data source map
        // is empty. If it is then rescan master_tenant table for all tenant

        if (dataSourcesMtApp.isEmpty()) {
            List<MasterTenant> masterTenants = masterTenantRepository.findAll();
            LOG.info("selectAnyDataSource() method call...Total tenants:" + masterTenants.size());
            for (MasterTenant masterTenant : masterTenants) {
                DataSource dataSource = DataSourceUtil.createAndConfigureDataSource(masterTenant);
                new FlywayConfiguration(dataSource, MultiTenantConstants.TENANTS_CLASSPATH_LOCATION);
                dataSourcesMtApp.put(masterTenant.getTenantName(), DataSourceUtil.createAndConfigureDataSource(masterTenant));
                targetDataSources.put(masterTenant.getTenantName().toString(), DataSourceUtil.createAndConfigureDataSource(masterTenant));

            }

        }
        RoutingDatasource routingDatasource = new RoutingDatasource();
        routingDatasource.setTargetDataSources(targetDataSources);
        routingDatasource.setDefaultTargetDataSource(targetDataSources.get(MultiTenantConstants.DEFAULT_DATABASE_TENANT_NAME));
        routingDatasource.afterPropertiesSet();


        return this.dataSourcesMtApp.get(MultiTenantConstants.DEFAULT_DATABASE_TENANT_NAME);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        // If the requested tenant id is not present check for it in the master
        // database 'master_tenant' table
        tenantIdentifier = initializeTenantIfLost(tenantIdentifier);
        if (!this.dataSourcesMtApp.containsKey(tenantIdentifier)) {
            List<MasterTenant> masterTenants = masterTenantRepository.findAll();
            LOG.info("selectDataSource() method call...Tenant:" + tenantIdentifier + " Total tenants:" + masterTenants.size());
            for (MasterTenant masterTenant : masterTenants) {
                dataSourcesMtApp.put(masterTenant.getTenantName(), DataSourceUtil.createAndConfigureDataSource(masterTenant));

            }
        }
        //check again if tenant exist in map after rescan master_db, if not, throw UsernameNotFoundException
        if (!this.dataSourcesMtApp.containsKey(tenantIdentifier)) {
            LOG.warn("Trying to get tenant:" + tenantIdentifier + " which was not found in master db after rescan");
            throw new UsernameNotFoundException(String.format("Tenant not found after rescan, " + " tenant=%s", tenantIdentifier));
        }
        return this.dataSourcesMtApp.get(tenantIdentifier);
    }

    private String initializeTenantIfLost(String tenantIdentifier) {
        if (!Objects.equals(tenantIdentifier, DBContextHolder.getCurrentDb())) {
            tenantIdentifier = DBContextHolder.getCurrentDb();
        }
        return tenantIdentifier;
    }
}
