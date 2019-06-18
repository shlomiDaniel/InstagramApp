package com.shlomi.instagramapp.Models;

public class UserSetting {
    private User user;
    private UserAccountSetting userAccountSetting;

    public UserSetting(User user) {
        this.user = user;
    }

    public UserSetting(User user, UserAccountSetting userAccountSetting) {
        this.user = user;
        this.userAccountSetting = userAccountSetting;
    }

    public User getUser() {
        return user;
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }
}
