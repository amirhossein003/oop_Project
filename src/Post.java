import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Post {

    int postID;
    int userID;
    String caption;
    int likes;
    int comments;
    Date date;
    int views;


    public Post(int userID, String caption)
    {
        this.userID=userID;
        this.caption=caption;
        this.date= new Date();

    }

    public void edit(String editedCaption)
    {
        this.caption= editedCaption;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE posts SET caption='"+caption+"' where postID="+postID);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void like(int UserId)
    {

        try {
            Date date=new Date();
            DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            String dateString=format.format(date);
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            boolean flag=true;
            ResultSet resultSet=statement.executeQuery("select * from postlike where postID="+postID+"");
            while (resultSet.next())
                if(resultSet.getInt("userID")==UserId){
                    flag=false; break;
                }
            if(flag) {
                this.likes++;
                statement.executeUpdate("UPDATE posts SET likes=" + likes + " where postID=" + postID);
                statement.executeUpdate("INSERT INTO postlike (postID,userID,Date) values (" + postID + "," + UserId + ",'" + dateString + "')");
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

    }

    public void viewPost(int UserId){

        try {
            Date date=new Date();
            DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            String dateString=format.format(date);
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            boolean flag=true;
            ResultSet resultSet=statement.executeQuery("select * from views where postID="+postID+"");
            while (resultSet.next())
                if(resultSet.getInt("userID")==UserId){
                    flag=false; break;
                }
            if(flag) {
                this.views++;
                statement.executeUpdate("UPDATE posts SET views=" + views + " where postID=" + postID);
                statement.executeUpdate("INSERT INTO views (postID,userID,Date) values (" + postID + "," + UserId + ",'" + dateString + "')");
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void showComments() throws SQLException {

        ArrayList<Integer> commentId= new ArrayList<>();
        Map<Integer, Date> comments= new HashMap<>();


        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM comment_post");
        while (resultSet.next()){
            if(resultSet.getInt("postID")==postID)
                commentId.add(resultSet.getInt("commentID"));

        }

        resultSet=statement.executeQuery("SELECT * FROM comments");
        while (resultSet.next()) {
            for (Integer integer : commentId)
                if (resultSet.getInt("commentID") == integer)
                    comments.put(integer ,stringToDate(resultSet.getString("Date")));
        }

        comments= sortByValue2(comments);
        ArrayList<Integer> commentID=new ArrayList<>();
        commentID.addAll(comments.keySet());
        ArrayList<Integer> userID=new ArrayList<>();
        ArrayList<String> text=new ArrayList<>();

        for (int i=commentID.size()-1;i>=0;i--) {

            resultSet = statement.executeQuery("SELECT * FROM comments where commentID=" + commentID.get(i) + "");
            while (resultSet.next()) {
                userID.add(resultSet.getInt("userID"));
                text.add(resultSet.getString("text"));
            }
        }

        for (int i = 0; i < userID.size(); i++) {
            ResultSet resultSet1=statement.executeQuery("SELECT * from users where userID="+userID.get(i)+"");
            while (resultSet1.next()){
                System.out.println(resultSet1.getString("username")+" : "+text.get(i));
            }
        }

        System.out.println("______________________________________");

    }

    public void showLikes() throws SQLException {
        System.out.println("number of likes : "+this.likes);
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("select * from postlike where postID="+postID+" ");
        ArrayList<Integer> userID=new ArrayList<>();
        System.out.println("List of users who liked this post :");
        while (resultSet.next()){
            userID.add(resultSet.getInt("userID"));
        }
        for (Integer integer : userID) {
            ResultSet resultSet1 = statement.executeQuery("select * from users where userID=" + integer + "");
            while (resultSet1.next())
                System.out.println(resultSet1.getString("username"));
        }
        System.out.println("_______________________________________");
    }

    public void showState(String date) throws SQLException {

        int views=0,likes=0;
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");

        ArrayList<Integer> userID=new ArrayList<>();
        ResultSet resultSet=statement.executeQuery("SELECT * FROM postlike where Date='"+date+"' and postID="+postID+"");
        System.out.println("list of users who liked post in  "+date);
        while (resultSet.next()){
            likes++;
           userID.add(resultSet.getInt("userID"));
        }

        for (Integer integer : userID) {
            resultSet = statement.executeQuery("SELECT * from users where userID=" + integer + "");
            while (resultSet.next())
                System.out.println(resultSet.getString("username"));
        }


        ArrayList<Integer> userID1=new ArrayList<>();
        ResultSet resultSet1=statement.executeQuery("SELECT * FROM views where Date='"+date+"' and postID="+postID+"");
        System.out.println("*************************************");
        System.out.println("list of users who viewed post in  "+date);
        while (resultSet1.next()){
            views++;
            userID1.add(resultSet1.getInt("userID"));
        }

        for (Integer integer : userID1) {
            resultSet = statement.executeQuery("SELECT * from users where userID=" + integer + "");
            while (resultSet.next())
                System.out.println(resultSet.getString("username"));
        }

        System.out.println("*************************************");
        System.out.println("number of likes in : "+date +" is "+likes);
        System.out.println("number of views in : "+date +" is "+views);
        System.out.println("_________________________________________");
    }

    public void newComment(String text,int userID)
    {
        Comment comment=new Comment(this.postID,userID,text);
        comment.saveData();
        int commentID=comment.getCommentID();
        comments++;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE posts SET comments="+comments+" where postID="+postID);
            statement.executeUpdate("INSERT INTO comment_post (postID,commentID) values ("+postID+","+commentID+")");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveData()
    {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("INSERT INTO posts (userID,caption,likes,comments,date,views) values  " +
                            "           ("+userID+",'"+caption+"',"+likes+","+comments+",'"+dateToString(date)+"'," +
                            ""+views+")");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }


    public int getPostID(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM posts");
            while (resultSet.next()){
                if(resultSet.getString("caption").equals(caption) &&
                        resultSet.getInt("userID")==userID &&
                        resultSet.getString("date").equals(dateToString(date)))
                        {
                            postID=resultSet.getInt("postID");
                            break;
                        }
            }}
        catch (
                SQLException e) {
            e.printStackTrace();
        }


        return postID;
    }

    public void showPostPage(int userID) throws SQLException {


        System.out.println("**************************");
        System.out.println("1-showInfoPost");
        System.out.println("2-show likes");
        System.out.println("3-show comments");
        System.out.println("4-like comment");
        System.out.println("5-edit comment");
        System.out.println("6-edit post");
        System.out.println("7-show likeComment");
        System.out.println("8-new comment");
        System.out.println("9-like");
        System.out.println("10-show state");
        System.out.println("11-return to postPage");
        System.out.println("12-return to menu");

        Scanner scanner1=new Scanner(System.in);
        Scanner scanner=new Scanner(System.in);
        boolean flag=true;

        while (flag){

            int value= Integer.parseInt(scanner1.next());

            if(value==1){
               if(postType(userID).equals("commercialUser")){
                   System.out.println("commercialPost");
               }
                System.out.println("post created on : "+ dateToString(date));
                System.out.println(this.userID + " : ");
                System.out.println(caption);
                System.out.println("number of likes : " + likes);
                System.out.println("number of comments : " + comments);
                System.out.println("number of views : " + views);
                System.out.println("________________________________");

            }
            else if(value==2){
               showLikes();
            }
            else if(value==3){
               showComments();
            }
            else if(value==4){
               System.out.println("please enter a commentID to like it");
               int commentID=scanner.nextInt();
               Comment comment =  Comment.getComment(commentID);
               comment.like(userID);
            }
            else if(value==5){
              System.out.println("1-please enter a text to edit comment");
              System.out.println("2-please enter a commentID to edit it");
              String text=scanner.nextLine();
              int commentID=scanner.nextInt();
              Comment comment =  Comment.getComment(commentID);
              comment.edit(text);
            }
            else if(value==6){
              System.out.println("please enter a caption to edit caption");
              String editCaption=scanner.nextLine();
              edit(editCaption);
            }
            else if(value==7){
              System.out.println("please enter a commentID to showLike");
              int commentID=scanner.nextInt();
              Comment comment =  Comment.getComment(commentID);
              comment.showLikes();
            }
            else if(value==8){
              System.out.println("please enter a text to comment!");
              String text=scanner.nextLine();
              newComment(text,userID);
            }
            else if(value==9){
                like(userID);
            }
            else if(value==10){
              System.out.println("please select a date to showState!");
              String date=scanner.nextLine();
              showState(date);
            }
            else if(value==11){
              showPostPage(userID);
            }
            else if(value==12){
             InputProcess.showMenuPage();
             flag=false;
            }

        }

    }

   public static Post getPost(int postID) throws SQLException{
       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
       Statement statement=connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("SELECT * FROM posts where postID="+postID+"");

       while (resultSet.next()) {
           Post post = new Post(resultSet.getInt("userID"), resultSet.getString("caption"));
           post.postID = postID;
           post.userID = resultSet.getInt("userID");
           post.likes = resultSet.getInt("likes");
           post.comments = resultSet.getInt("comments");
           post.date = stringToDate(resultSet.getString("date"));
           return post;
       }

      return null;
   }

   public String postType(int userID) throws SQLException {
       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
       Statement statement=connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("SELECT * FROM users where userID="+userID+"");

       while (resultSet.next())
       return resultSet.getString("business");

       return null;
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

    public static HashMap<Integer, Date>
    sortByValue2(Map<Integer, Date> hm)
    {
        List<Map.Entry<Integer, Date> > list
                = new LinkedList<Map.Entry<Integer, Date> >(
                (Collection<? extends Map.Entry<Integer, Date>>) hm.entrySet());


        Collections.sort(
                list,
                (i1,
                 i2) -> i1.getValue().compareTo(i2.getValue()));

        HashMap<Integer, Date> temp
                = new LinkedHashMap<>();
        for (Map.Entry<Integer, Date> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}