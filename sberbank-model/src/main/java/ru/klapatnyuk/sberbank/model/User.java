package ru.klapatnyuk.sberbank.model;

import ru.klapatnyuk.sberbank.model.api.EnumType;

/**
 * @author klapatnyuk
 */
public class User extends AbstractEntity {

    private String login;
    private String password;
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @author klapatnyuk
     */
    public enum Role implements EnumType {
        ADMIN,
        CLIENT;

        public static Role find(String source) {
            if (source == null || source.isEmpty()) {
                return getDefault();
            }
            for (Role constant : values()) {
                if (constant.toString().toLowerCase().equals(source.toLowerCase())) {
                    return constant;
                }
            }
            return getDefault();
        }

        public static Role getDefault() {
            return CLIENT;
        }
    }
}
