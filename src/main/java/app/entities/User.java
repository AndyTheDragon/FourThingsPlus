package app.entities;

public class User
{
    private final int userId;
    private final String username;
    private final String password;

    public User(int userId, String username, String password)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId()
    {
        return userId;
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
                "user_id=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
