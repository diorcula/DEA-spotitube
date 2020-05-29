package nl.han.ica.dea.fedor.services;

import nl.han.ica.dea.fedor.dao.UserDAO;
import nl.han.ica.dea.fedor.dto.UserDTO;

import javax.inject.Inject;

public class UserService {
    @Inject
    UserDAO userDAO;

    public boolean isValidLogin(String userName, String password) {
        UserDTO user = userDAO.getUserDTO(userName);

        if (password.equals(user.getPassword())) {
            return true;
        } else {
            System.out.println("Login credentials do not match");
            return false;
        }
    }

}
