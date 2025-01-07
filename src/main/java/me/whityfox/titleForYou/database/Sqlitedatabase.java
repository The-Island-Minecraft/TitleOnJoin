package me.whityfox.titleForYou.database;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class Sqlitedatabase {
    private final Connection connection;

    public Sqlitedatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS current_message(id INT PRIMARY KEY, top_message TEXT, bottom_message TEXT, duratiom TEXT);
                """);
        }
    }
    public void change_message(String top, String bot, String duration) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO current_message(id, top_message, bottom_message, duration) VALUES (?, ?, ?, ?)")){
            preparedStatement.setInt(1, 1) ;
            preparedStatement.setString(2, top);
            preparedStatement.setString(3, bot);
            preparedStatement.setString(1, duration);
            preparedStatement.executeUpdate();
        }
    }
    public Boolean IsThereIsAMessage() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM current_message WHERE id = 1")){
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
    public void replace_message(String top, String bot, String duration) throws  SQLException{
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM current_message WHERE id = 1")){
            preparedStatement.executeUpdate();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO current_message(id, top_message, bottom_message, duration) VALUES (?, ?, ?, ?)")){
            preparedStatement.setInt(1, 1) ;
            preparedStatement.setString(2, top);
            preparedStatement.setString(3, bot);
            preparedStatement.setString(1, duration);
            preparedStatement.executeUpdate();
        }
    }
    public void remove_message() throws  SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM current_message WHERE id = 1")) {
            preparedStatement.executeUpdate();
        }
    }
    public List<String> get_message(){
        List<String> info = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM message"
            );
            ResultSet resultSet = statement.executeQuery();
            info.add(resultSet.getString("top_message"));
            info.add(resultSet.getString("bottom_message"));
            info.add(resultSet.getString("duration"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }
    public void closeConnection() throws SQLException{
        if (connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}