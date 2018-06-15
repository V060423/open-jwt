package org.apel.authority.repository;

import org.apel.authority.model.SystemUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wangbowen
 * @Description 用户dao
 * @Date 2018/6/15 14:06
 */
@Repository
public interface SystemUserRepository extends CrudRepository<SystemUser,String> {

    /**
     * 根据用户名和密码查询用户
     * @param userName  用户名
     * @return
     */
    SystemUser findByUserName(String userName);
}
