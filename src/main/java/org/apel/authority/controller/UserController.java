package org.apel.authority.controller;

import org.apel.authority.model.SystemUser;
import org.apel.authority.repository.SystemUserRepository;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 在 @PreAuthorize 中我们可以利用内建的 SPEL 表达式：比如 'hasRole()' 来决定哪些用户有权访问。
 * 需注意的一点是 hasRole 表达式认为每个角色名字前都有一个前缀 'ROLE_'。所以这里的 'ADMIN' 其实在
 * 数据库中存储的是 'ROLE_ADMIN' 。这个 @PreAuthorize 可以修饰Controller也可修饰Controller中的方法。
 **/
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SystemUserRepository repository;

    /**
     * 获取全部用户
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<SystemUser> getUsers() {
        return (List<SystemUser>) repository.findAll();
    }

    /**
     * 新增用户
     * @param addedUser
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    SystemUser addUser(@RequestBody SystemUser addedUser) {
        return repository.save(addedUser);
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/find/{id}")
    public SystemUser getUser(@PathVariable String id) {
        return repository.findById(id).get();
    }

    /**
     * 更新用户
     * @param id
     * @param updatedUser
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/modify/{id}")
    public  SystemUser updateUser(@PathVariable String id, @RequestBody SystemUser updatedUser) {
        updatedUser.setId(id);
        return repository.save(updatedUser);
    }

    /**
     * 根据用户id删除用户
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public SystemUser removeUser(@PathVariable String id) {
        Optional<SystemUser> user = repository.findById(id);
        if(Objects.nonNull(user)) {
            repository.delete(user.get());
        }
        return user.get();
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/find/username")
    public SystemUser getUserByUsername(@RequestParam(value="username") String username) {
        return repository.findByUserName(username);
    }
}