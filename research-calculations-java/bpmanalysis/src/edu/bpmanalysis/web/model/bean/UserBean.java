package edu.bpmanalysis.web.model.bean;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "users", schemaVersion = "1.0")
public class UserBean {
    @Id
    private String login;
    private String password;
    private String profile;

    @Override
    public String toString() {
        return "UserBean{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBean userBean = (UserBean) o;

        if (login != null ? !login.equals(userBean.login) : userBean.login != null) return false;
        if (password != null ? !password.equals(userBean.password) : userBean.password != null) return false;
        return profile != null ? profile.equals(userBean.profile) : userBean.profile == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (profile != null ? profile.hashCode() : 0);
        return result;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
