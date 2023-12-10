package ru.kata.spring.boot_security.demo.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UsersDao;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService, UserDetailsService {
    private final UsersDao usersDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersServiceImpl(UsersDao usersDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersDao = usersDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> findAll() {
        return usersDao.findAll();
    }

    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersDao.save(user);
    }

    @Transactional
    public void update(User updatedUser) {
        updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        usersDao.update(updatedUser);
    }

    @Transactional
    public void delete(int id) {
        usersDao.delete(id);
    }

    public User show(int id) {
        return usersDao.show(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(usersDao.findByUsername(username));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User doesn't exist!");
        }
    }

    public User findByUsername(String username) {
        return usersDao.findByUsername(username);
    }

}
