package ru.klapatnyuk.sberbank.model.entity;

import ru.klapatnyuk.sberbank.model.type.api.EnumType;

/**
 * @author klapatnyuk
 */
public class User extends AbstractEntity {

    private String login;
    private String password;
    private Role role;

    private User() {
    }

    public static Builder newBuilder() {
        return new User().new Builder();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    /**
     * @author klapatnyuk
     */
    public class Builder extends AbstractBuilder {

        private Builder() {
        }

        public Builder setLogin(String login) {
            User.this.login = login;
            return this;
        }

        public Builder setPassword(String password) {
            User.this.password = password;
            return this;
        }

        public Builder setRole(Role role) {
            User.this.role = role;
            return this;
        }

        @Override
        public Builder setId(int id) {
            super.setId(id);
            return this;
        }

        @Override
        public Builder setTitle(String title) {
            super.setTitle(title);
            return this;
        }

        @Override
        public User build() {
            return User.this;
        }
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
