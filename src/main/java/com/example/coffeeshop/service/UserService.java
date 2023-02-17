package com.example.coffeeshop.service;

import com.example.coffeeshop.model.User;
import com.example.coffeeshop.model.dto.UserLoginDTO;
import com.example.coffeeshop.model.dto.UserRegisterDTO;
import com.example.coffeeshop.repository.UserRepo;
import com.example.coffeeshop.session.LoggedUserSession;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final LoggedUserSession loggedUserSession;
    private final HttpSession session;

    @Autowired
    public UserService(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                       LoggedUserSession loggedUserSession, HttpSession session) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.loggedUserSession = loggedUserSession;
        this.session = session;
    }

    public User getUserById(long id) {
        return this.userRepo.findUserById(id);
    }

    public User register(UserRegisterDTO userRegisterDTO) {
        User newUser = modelMapper.map(userRegisterDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return this.userRepo.save(newUser);
    }

    public User login(UserLoginDTO userLoginDTO) {
        User user = this.userRepo.findUserByUsername(userLoginDTO.getUsername()).orElse(null);

        if (user != null) {
            loggedUserSession.setId(user.getId());
            loggedUserSession.setUsername(user.getUsername());
            loggedUserSession.setEmail(user.getEmail());
        }

        return user;
    }

    public boolean checkUserData(UserLoginDTO userLoginDTO) {
        User user = this.userRepo.findUserByUsername(userLoginDTO.getUsername()).orElse(null);

        if (user == null) {
            return false;
        }

        return this.passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
    }

    public User userNameIsTaken(String name) {
        return this.userRepo.findUserByUsername(name).orElse(null);
    }

    public User emailIsTaken(String email) {
        return this.userRepo.findUserByEmail(email).orElse(null);
    }


    public void logout() {
        this.loggedUserSession.setId(0);
        this.loggedUserSession.setUsername(null);
        this.loggedUserSession.setEmail(null);
        this.session.invalidate();
    }

    public void initUsersDB() {
        if (userRepo.count() == 0) {
            User user = new User()
                    .setEmail("todor@abv.bg")
                    .setPassword(passwordEncoder.encode("123123"))
                    .setFirstName("Todor")
                    .setLastName("Todorov")
                    .setUsername("todor");

            this.userRepo.save(user);

            User user2 = new User()
                    .setEmail("gosho@abv.bg")
                    .setPassword(passwordEncoder.encode("123123"))
                    .setFirstName("Gosho")
                    .setLastName("Goshev")
                    .setUsername("goshko");

            this.userRepo.save(user2);

            User user3 = new User()
                    .setEmail("petko@abv.bg")
                    .setPassword(passwordEncoder.encode("123123"))
                    .setFirstName("Petko")
                    .setLastName("Petkov")
                    .setUsername("petko");

            this.userRepo.save(user3);
        }
    }

    public User findUserByUserId(Long id) {
        return this.userRepo.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        List<User> all = this.userRepo.findAll();
        if(all.size() > 0) {
          all.sort(new UserOrdersCountComparator());
        }

        return all;
    }


    private static class UserOrdersCountComparator implements Comparator<User> {

        @Override
        public int compare(User u1, User u2) {
            return Integer.compare(u2.getOrders().size(), u1.getOrders().size());
        }
    }
}
