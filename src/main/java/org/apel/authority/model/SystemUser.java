package org.apel.authority.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author wangbowen
 * @Description 系统用户
 * @Date 2018/6/15 12:27
 */
@Data
@Entity
@Table(name = "system_user")
public class SystemUser {
    @Id
    private String id;

    private String userName;

    private String password;

    private String email;

    private Date lastPasswordResetDate;

    private String roles;
}
