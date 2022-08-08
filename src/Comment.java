import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Comment {

    int postID;
    int commentID;
    int userID;
    String text;
    Date date;
    int likes;


    public Comment(int postID, int userID, String text) {
        this.postID = postID;
        this.userID = userID;
        this.text = text;
        this.date = new Date();
    }


    public void edit(String text) {this.text=text;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE comments SET text='"+text+"' where commentID="+commentID);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }}


    public void like(int userID) {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            boolean flag=true;
            ResultSet resultSet=statement.executeQuery("select * from commentlike where commentID="+commentID+"");
            while (resultSet.next()){
                if(resultSet.getInt("userID")==userID){
                    flag=false; break;
                }
            }
            if (flag) {
                likes++;
                statement.executeUpdate("UPDATE comments SET likes=" + likes + " where commentID=" + commentID);
                statement.executeUpdate("INSERT into commentlike (commentID,userID) values (" + commentID + "," + userID + ")");
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }}

    public void showLikes() throws SQLException {

        System.out.println("number of likes : "+this.likes);
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");

        ArrayList<Integer> userID=new ArrayList<>();
        ResultSet resultSet=statement.executeQuery("select * from commentlike where commentID="+commentID+" ");
        System.out.println("List of users who liked this comment :");
        while (resultSet.next()){
            userID.add(resultSet.getInt("userID"));
        }

        for (Integer integer : userID) {
            ResultSet resultSet1 = statement.executeQuery("select * from users where userID=" + integer + " ");
            while (resultSet1.next())
                System.out.println(resultSet1.getString("username"));
        }
        System.out.println("__________________________________");
    }

    public void saveData()
    {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("INSERT INTO comments (postID,userID,text,date ,likes) values " +
                    "                     ("+postID+","+userID+",'"+text+"','"+dateToString(date)+"',"+likes+")");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCommentID(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM comments");
            while (resultSet.next()){
                if(resultSet.getString("text").equals(text) &&
                        resultSet.getInt("postID")==postID &&
                        resultSet.getInt("userID")==userID &&
                        resultSet.getString("date").equals(dateToString(date)))
                            {
                                commentID=resultSet.getInt("commentID");
                                break;
                            }
            }}
        catch (
                SQLException e) {
            e.printStackTrace();
        }


        return commentID;
    }

    public static Date stringToDate(String string)
    {
        DateFormat format= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        java.util.Date date= null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String dateToString(Date date){

        DateFormat format=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return format.format(date);
    }

    public static Comment getComment(int commentID) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM comments where commentID="+commentID+"");

        while (resultSet.next()) {
            Comment comment = new Comment(resultSet.getInt("postID"), resultSet.getInt("userID"),
                    resultSet.getString("text"));
            comment.commentID = commentID;
            comment.likes = resultSet.getInt("likes");
            comment.date = stringToDate(resultSet.getString("date"));

            return comment;
        }
        return null;
    }

}