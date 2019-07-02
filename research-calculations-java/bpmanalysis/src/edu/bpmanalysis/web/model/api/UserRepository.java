package edu.bpmanalysis.web.model.api;

import edu.bpmanalysis.web.model.bean.UserBean;

import java.util.List;

public interface UserRepository {

    void addUser(UserBean userBean);

    UserBean getUser(String login);

    List<UserBean> getUsers();

    void deleteUser(String login);
}
