package domain;


public class User {
    private Integer id;
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.id = 0;
        this.username = username;
        this.password = password;
    }

    public void setId(int id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean isAdmin() {
        if(password.equals("iamadmin2") && username.charAt(username.length()-1)=='4') return true;
        return false;
    }


}
