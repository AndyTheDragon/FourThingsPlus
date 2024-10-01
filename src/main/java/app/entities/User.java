package app.entities;

public class User
{
    private final int user_id;
    private final String username;
    private final String password;

    public User(int user_id, String username, String password)
    {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
