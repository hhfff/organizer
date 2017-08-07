package scene.login;

import resources.database.entity.User;

/**
 * Created by Liu Woon Kit on 5/7/2017.
 */
public class LoginController {
    public boolean verifyUser(String userID, String password) {

        return User.verifyUser(userID, password);
    }
}