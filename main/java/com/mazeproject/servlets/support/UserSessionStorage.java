package com.mazeproject.servlets.support;

import com.mazeproject.database.UserEntity;

public class UserSessionStorage {
    
    private UserEntity userEntity;
    private static ThreadLocal<UserSessionStorage> instance;

    public UserSessionStorage(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
    
    public static UserSessionStorage getInstance() {
        return UserSessionStorage.getInstance(null);
    }
    
    public static UserSessionStorage getInstance(UserEntity userEntity) {
        if(UserSessionStorage.instance==null) {
            UserSessionStorage.instance = new ThreadLocal(){
                @Override
                public UserSessionStorage initialValue() {
                    return new UserSessionStorage(null);
                }
            };
        }
        if(userEntity != null) {
            UserSessionStorage.instance.get().setUserEntity(userEntity);
        }
        return UserSessionStorage.instance.get();
    }
    
}
