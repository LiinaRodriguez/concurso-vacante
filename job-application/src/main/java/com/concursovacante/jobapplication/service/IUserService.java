package com.concursovacante.jobapplication.service;
import com.concursovacante.jobapplication.model.User;
import java.util.List;
public interface IUserService {
    public List<User> getAllUsers();
    public User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);

}
