package miu.edu.csvservice.service;


import miu.edu.csvservice.domain.Authentication;

import java.util.List;

public interface UserService {
    Authentication getByUserName(String username);

    Authentication saveUser(Authentication authentication);


}
