package edu.bpmanalysis.web.model;

import edu.bpmanalysis.web.model.api.UserRepository;
import edu.bpmanalysis.web.model.bean.UserBean;
import io.jsondb.JsonDBTemplate;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class UserRepositoryJsonDB implements UserRepository {
    private JsonDBTemplate jsonDBTemplate;

    public UserRepositoryJsonDB() {
        this.jsonDBTemplate = new JsonDBTemplate("processModelsStorage/",
                "edu.bpmanalysis.web.model.bean");

        if (!jsonDBTemplate.collectionExists(UserBean.class)) {
            jsonDBTemplate.createCollection(UserBean.class);
        }
    }

    @Override
    public void addUser(UserBean userBean) {
        userBean.setPassword(DigestUtils.md5Hex(userBean.getPassword()).toUpperCase());
        jsonDBTemplate.insert(userBean);
    }

    @Override
    public UserBean getUser(String login) {
        return jsonDBTemplate.findById(login, UserBean.class);
    }

    @Override
    public List<UserBean> getUsers() {
        return jsonDBTemplate.findAll(UserBean.class);
    }

    @Override
    public void deleteUser(String login) {
        UserBean userBean = new UserBean();
        userBean.setLogin(login);

        jsonDBTemplate.remove(userBean, UserBean.class);
    }
}
