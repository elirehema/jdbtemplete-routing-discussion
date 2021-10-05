package org.ospic.platform.mastertenant.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Md. Amran Hossain
 */
@Entity
@Table(name = "tenants")
public class MasterTenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_client_id")
    private Integer tenantClientId;

    @Size(max = 50)
    @Column(name = "tenant_name",nullable = false)
    private String tenantName;

    @Size(max = 100)
    @Column(name = "url",nullable = false)
    private String url;

    @Size(max = 50)
    @Column(name = "user_name",nullable = false)
    private String userName;
    @Size(max = 100)
    @Column(name = "password",nullable = false)
    private String password;
    @Size(max = 100)
    @Column(name = "driver_class",nullable = false)
    private String driverClass;
    @Size(max = 10)
    @Column(name = "status",nullable = false)
    private String status;

    public MasterTenant() {
    }

    public MasterTenant(@Size(max = 50) String tenantName, @Size(max = 100) String url, @Size(max = 50) String userName, @Size(max = 100) String password, @Size(max = 100) String driverClass, @Size(max = 10) String status) {
        this.tenantName = tenantName;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.driverClass = driverClass;
        this.status = status;
    }

    public Integer getTenantClientId() {
        return tenantClientId;
    }

    public MasterTenant setTenantClientId(Integer tenantClientId) {
        this.tenantClientId = tenantClientId;
        return this;
    }

    public String getTenantName() {
        return tenantName;
    }

    public MasterTenant setTenantName(String tenantName) {
        this.tenantName = tenantName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MasterTenant setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public MasterTenant setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MasterTenant setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public MasterTenant setDriverClass(String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MasterTenant setStatus(String status) {
        this.status = status;
        return this;
    }
}
