package com.mazeproject.servlets.support;

public class UserSessionStorage {
    
    private String name;
    private String password;
    private static ThreadLocal<UserSessionStorage> instance;

    private UserSessionStorage(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static UserSessionStorage getInstance() {
        return UserSessionStorage.getInstance(null, null);
    }
    
    public static UserSessionStorage getInstance(String name, String password) {
        if(UserSessionStorage.instance==null) {
            UserSessionStorage.instance = new ThreadLocal(){
                @Override
                public UserSessionStorage initialValue() {
                    return new UserSessionStorage(null, null);
                }
            };
        }
        if(name != null) {
            UserSessionStorage.instance.get().setName(name);
        }
        if(password != null) {
            UserSessionStorage.instance.get().setPassword(password);
        }
        return UserSessionStorage.instance.get();
    }
    
    /*
    public String getSerializedValue(HttpSession s) {
        ObjectOutputStream oo = null;
        try {
            OutputStream os = s.get
            oo = new ObjectOutputStream;
            oo.writeObject(serOut);
        } catch (IOException ex) {
            Logger.getLogger(UserSessionStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oo.close();
            } catch (IOException ex) {
                Logger.getLogger(UserSessionStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    */
    
}
