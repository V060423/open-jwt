package org.apel.authority.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author wangbowen
 * @Description 系统用户
 * @Date 2018/6/15 12:27
 */
@Data
public class SystemUser {
    @Id
    private String id;

    private String userName;

    private String password;

    private String email;

    private Date lastPasswordResetDate;

    @Transient
    private List<String> roles;
}
