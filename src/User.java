import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class User {

    int userId;
    String username;
    String password;
    int followers;
    int followings;
    int post;
    String bio;
    int views;
    String business;
    String forgetPassword;

    public User(String username, String password,String business){

        this.business= business;
        this.username=username;
        this.password=password;
    }

    public User() {

    }

    public User(String username,String password){
        this.username=username;
        this.password=password;
    }

    public void addFollowing(User user){

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            boolean flag=true;
            ResultSet resultSet=statement.executeQuery("select * from followers where follower="+userId+"");
            while (resultSet.next())
                if(resultSet.getInt("userID")==user.userId){
                    flag=false; break;
                }
            if (flag) {
                this.followings += 1;
                statement.executeUpdate("UPDATE users SET followings =" + followings + " where userID=" + userId);
                user.addFollower();
                statement.executeUpdate("Insert into followers (userID,follower) values (" + user.userId + "," + userId + ")");
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFollowing(User user){

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            if (this.followings>0) {
                this.followings -= 1;
                statement.executeUpdate("UPDATE users SET followings =" + followings + " where userID=" + userId);
                user.removeFollower();
                statement.executeUpdate("DELETE from followers where userID=" + user.userId + " and follower= " + userId);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFollower(){
        this.followers+=1;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            statement.executeUpdate("UPDATE users SET followers ="+followers+" where userID="+userId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFollower(){
        this.followers-=1;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            statement.executeUpdate("UPDATE users SET followers ="+followers+" where userID="+userId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPost(String caption){
        this.post+=1;
        Post post=new Post(userId,caption);
        post.saveData();

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            statement.executeUpdate("UPDATE users SET post ="+this.post+" where userID= "+userId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePost(int postId){

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            if (this.post>0) {
                this.post -= 1;
                statement.executeUpdate("DELETE FROM posts where postID =" + postId);
                statement.executeUpdate("DELETE FROM postlike where postID =" + postId);
                statement.executeUpdate("delete from comment_post where postID=" + postId);
                statement.executeUpdate("UPDATE users set post =" + this.post + " where userID =" + userId);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void editBio(String bio){
        this.bio=bio;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            statement.executeUpdate("UPDATE users SET bio ='"+bio+"' where userID="+userId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void editUsername(String username){
        this.username=username;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            statement.executeUpdate("UPDATE users SET userName='"+username+"' where userID="+userId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void editPassword(String password){

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            if (password.length()>=8) {
                this.password = password;
                statement.executeUpdate("UPDATE users SET password ='" + password + "' where userID=" + userId);
            }
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

            statement.executeUpdate("INSERT INTO users (userName,password,followers,followings,post,bio,business,views,forgetPassword) values " +
                    "                     ('"+username+"','"+password+"',"+followers+","+followings+","+post+"," +
                    "'"+bio+"',"+business+","+views+",'"+forgetPassword+"')");


        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(String username) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM users where username='"+username+"'");

        while (resultSet.next()) {
            User user = new User(resultSet.getString("username"), resultSet.getString("password"),
                            resultSet.getString("business"));
            user.userId = resultSet.getInt("userID");
            user.followers = resultSet.getInt("followers");
            user.followings = resultSet.getInt("followings");
            user.post = resultSet.getInt("post");
            user.views = resultSet.getInt("views");
            user.bio = resultSet.getString("bio");
            user.forgetPassword = resultSet.getString("forgetPassword");


            return user;
        }
        return null;

    }

    public void block(int blockUserID)
    {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            boolean flag=true;
            ResultSet resultSet=statement.executeQuery("select * from block where userID="+userId+"");
            while (resultSet.next())
                if(resultSet.getInt("blockedUser")==blockUserID){
                    flag=false; break;
                }
            if (flag)
            statement.executeUpdate("INSERT INTO block (userID,blockedUser) values " +
                    "                     ("+userId+","+blockUserID+")");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }


    public int getUserID(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()){
                if(resultSet.getString("username").equals(username)){
                    userId=resultSet.getInt("userID");
                    break;
                }
            }}
        catch (
                SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public static User getUser(int userId) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM users where userID="+userId+"");

        while (resultSet.next()) {
            User user = new User(resultSet.getString("username"), resultSet.getString("password"),
                    resultSet.getString("business"));
            user.userId = userId;
            user.followings = resultSet.getInt("followings");
            user.followers = resultSet.getInt("followers");
            user.bio = resultSet.getString("bio");
            user.post = resultSet.getInt("post");
            user.views = resultSet.getInt("views");
            user.forgetPassword = resultSet.getString("forgetPassword");
            return user;
        }
        return null;
    }

    public void showUserPage(User user) throws SQLException {


        System.out.println("*******************************");
        System.out.println("1-show info userPage");
        System.out.println("2-follow");
        System.out.println("3-unfollow");
        System.out.println("4-show followersList");
        System.out.println("5-show followingList");
        System.out.println("6-show posts");
        System.out.println("7-return to userPage");
        System.out.println("8-return to menu");


        Scanner scanner=new Scanner(System.in);
        boolean flag=true;

        while (flag) {

            int value= Integer.parseInt(scanner.next());

            if (value==1){

                if (user.business.equals("commercialUser")) {System.out.println("commercialAccount");
                    System.out.println("pageView : " + user.views);
                }
                else System.out.println("personalAccount");
                System.out.println("username: " + user.username);
                System.out.println("posts: " + user.post + "     followers: " + user.followers +
                        "     followings: " + user.followings);
                System.out.println("bio: " + user.bio);
            }
            if (value==2){
                addFollowing(user);
            }
            if (value==3){
               removeFollowing(user);
            }
            if (value==4){
                user.showFollowersList();
            }
            if (value==5){
                user.showFollowingsList();
            }
            if (value==6){
                user.showPostsList();
            }
            if (value==7){
               showUserPage(user);
            }
            if (value==8){
              InputProcess.showMenuPage();
              flag=false;
            }
        }


    }

    public static  void showMyUserPage(User user) throws SQLException {


        System.out.println("*******************************");
        System.out.println("1-show info userPage");
        System.out.println("2-show followersList");
        System.out.println("3-show followingList");
        System.out.println("4-show posts");
        System.out.println("5-create post");
        System.out.println("6- remove post");
        System.out.println("7-edit username");
        System.out.println("8-edit password");
        System.out.println("9-edit bio");
        System.out.println("10-return to myUserPage");
        System.out.println("11-return to menu");

        Scanner scanner1=new Scanner(System.in);
        Scanner scanner=new Scanner(System.in);
        boolean flag=true;

        while (flag) {

            int value= Integer.parseInt(scanner1.next());

            if (value == 1) {
                if (user.business.equals("commercialUser")) {System.out.println("commercialAccount");
                    System.out.println("pageView : " + user.views);
                }
                else System.out.println("personalAccount");
                System.out.println("username: " + user.username);
                System.out.println("posts: " + user.post + "     followers: " + user.followers +
                        "     followings: " + user.followings);
                System.out.println("bio: " + user.bio);
            }

            if (value == 2) {
               user.showFollowersList();
            }
            if (value == 3) {
               user.showFollowingsList();
            }
            if (value==4){
               user.showPostsList();
            }
            if (value==5){
                System.out.println("please enter a text to post");
                String text=scanner.nextLine();
                user.createPost(text);
            }
            if (value==6){
                System.out.println("please enter a postID to delete post!");
                int postId=scanner.nextInt();
                user.removePost(postId);
            }
            if (value==7){
                System.out.println("please enter a username to edit username");
                String username=scanner.nextLine();
                user.editUsername(username);
            }
            if (value==8){
                System.out.println("please enter a password to edit password");
                String password=scanner.nextLine();
                user.editPassword(password);
            }
            if (value==9){
                System.out.println("please enter a bio to edit bio");
                String bio=scanner.nextLine();
                user.editBio(bio);
            }
            if(value==10){
                showMyUserPage(user);
            }
            if (value==11){
                InputProcess.showMenuPage();
                flag=false;
            }

        }

    }

    public void viewPage() throws SQLException {


        views++;
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");
        statement.executeUpdate("UPDATE users set views="+views+" where userID="+userId+"");
    }

    public void showFollowersList() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");
        ArrayList<Integer> userID=new ArrayList<>();

        System.out.println("list of followers :");
        ResultSet resultSet=statement.executeQuery("SELECT (follower) FROM followers WHERE userID="+userId+" ");
        while (resultSet.next()){
            userID.add(resultSet.getInt("follower"));
        }
        for (Integer integer : userID) {
            ResultSet resultSet1 = statement.executeQuery("select * from users where userID=" + integer + "");
            while (resultSet1.next())
                System.out.println(resultSet1.getString("username"));
        }
        System.out.println("____________________________________");
    }

    public void showFollowingsList() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");

        ArrayList<Integer> userID=new ArrayList<>();
        System.out.println("list of followings :");
        ResultSet resultSet=statement.executeQuery("SELECT (userID) FROM followers WHERE follower="+userId+" ");
        while (resultSet.next()){
            userID.add(resultSet.getInt("userID"));
        }
        for (Integer integer : userID) {
            ResultSet resultSet1 = statement.executeQuery("select * from users where userID=" + integer + "");
            while (resultSet1.next())
                System.out.println(resultSet1.getString("username"));
        }
        System.out.println("________________________________");

    }

    public void showPostsList() throws SQLException {
        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * from posts where userID="+userId+"");

        ArrayList<Integer> userID=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> caption=new ArrayList<>();
        ArrayList<String> username=new ArrayList<>();

        while (resultSet.next()){
            userID.add(resultSet.getInt("userID"));
            date.add(resultSet.getString("date"));
            caption.add(resultSet.getString("caption"));
        }

        for (Integer integer : userID) {
            ResultSet resultSet1 = statement.executeQuery("SELECT * from users where userID=" + integer + "");
            while (resultSet1.next())
                username.add(resultSet1.getString("username"));
        }

        for (int i=userID.size()-1;i>=0;i--){
            System.out.println(username.get(i)+" : ");
            System.out.println(caption.get(i));
            System.out.println("post created on : "+date.get(i));
            System.out.println("-------------------------------");

        }
        System.out.println("_________________________________________");

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

    public void suggestion() throws SQLException {

        ArrayList<Integer> followings= new ArrayList<>();
        ArrayList<Integer> followingUsers= new ArrayList<>();
        Map<Integer,Integer> suggestUser= new HashMap<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM followers");
            while (resultSet.next()){

                if(resultSet.getInt("follower")==userId)
                    followings.add(resultSet.getInt("userID"));

            }
            resultSet=statement.executeQuery("SELECT * FROM followers");
            while (resultSet.next()){

                for (Integer following : followings) {
                    if (resultSet.getInt("follower") == following) {

                        if (resultSet.getInt("userID") == userId) {
                            break;
                        }
                        followingUsers.add(resultSet.getInt("userID"));
                    }
                }

            }
        }
        catch (
                SQLException e) {
            e.printStackTrace();
        }


        int number=1;
        for(int i=0;i<followingUsers.size();i++)
        {
            for(int j=i+1;j<followingUsers.size();j++)
                if(followingUsers.get(i)==followingUsers.get(j)) {
                    number++;
                    followingUsers.remove(j);
                    j--;
                }

            suggestUser.put(followingUsers.get(i),number);
            number=1;
        }


        suggestUser= sortByValue(suggestUser);

        ArrayList<Integer> suggestionUser=new ArrayList<>();
        ArrayList<String> username=new ArrayList<>();

        int size=suggestUser.size();
        suggestionUser.addAll(suggestUser.keySet());

        for (Integer integers : suggestionUser) {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement = connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet = statement.executeQuery("select * from users where userID=" + integers + " ");
            while (resultSet.next())
            username.add(resultSet.getString("username"));
        }

        int size2=suggestionUser.size();

        System.out.println("suggested users :");

        if(size2<=3){
           for (int i = size2-1; i>size2-2; i--){
               System.out.println(username.get(i));
           }
        }
        else if(size2<=10){
          for (int i = size2-1; i>size2-4; i--){
              System.out.println(username.get(i));
          }
        }
        else {
         for (int i = size2-1; i>size2-7; i--){
             System.out.println(username.get(i));
         }

        }
        System.out.println("_________________________________");

    }

    public void adPosts() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM followers where follower="+this.userId+"");
        ArrayList<Integer> followingUserID=new ArrayList<>();

        while (resultSet.next())
            followingUserID.add(resultSet.getInt("userID"));

        ArrayList<Integer> commercialUsers=new ArrayList<>();

        for (Integer integer : followingUserID) {

            ResultSet resultSet1 = statement.executeQuery("SELECT * FROM users where userID=" + integer + "");
            while (resultSet1.next())
                if (resultSet1.getString("business").equals("commercialUser"))
                    commercialUsers.add(resultSet1.getInt("userID"));
        }

        ArrayList<String> username=new ArrayList<>();
        ArrayList<String> caption=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();

        for (Integer commercialUser : commercialUsers) {

            ResultSet resultSet1 = statement.executeQuery("select * from posts where userID=" + commercialUser + "");
            while (resultSet1.next()) {

                username.add(userIDToUsername(resultSet1.getInt("userID")));
                caption.add(resultSet1.getString("caption"));
                date.add(resultSet1.getString("date"));
            }
        }

        if (username.size()<=3){

            System.out.println("ad Posts :");
            for (int i = 0; i < username.size(); i++) {
                System.out.println(username.get(i)+" : "+caption.get(i)+"       "+date.get(i));
                System.out.println("-------------------------------------------");
            }
            System.out.println("____________________________________________________");
        }
        else {

            System.out.println("ad Posts :");
            for (int i=username.size()-1;i>=username.size()-3;i--){
                System.out.println(username.get(i)+" : "+caption.get(i)+"       "+date.get(i));
                System.out.println("-------------------------------------------");
            }
            System.out.println("____________________________________________________");
        }


    }

    public String userIDToUsername(int userID) throws SQLException {

        String username="";
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("select * from users where userID="+userID+"");
        while (resultSet.next())
            username=resultSet.getString("username");

        return username;
    }

    public static HashMap<Integer, Integer>
    sortByValue(Map<Integer, Integer> hm)
    {
        List<Map.Entry<Integer, Integer> > list
                = new LinkedList<Map.Entry<Integer, Integer> >(
                (Collection<? extends Map.Entry<Integer, Integer>>) hm.entrySet());


        Collections.sort(
                list,
                (i1,
                 i2) -> i1.getValue().compareTo(i2.getValue()));

        HashMap<Integer, Integer> temp
                = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
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


    public void recentPost(int userId) throws SQLException {
        ArrayList<Integer> followersAndFollowing= new ArrayList<>();

        followersAndFollowing.add(userId);

        Map<Integer, Date> posts= new HashMap<>();


            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM followers");
            while (resultSet.next()){
                if(resultSet.getInt("userID")==userId)
                    followersAndFollowing.add(resultSet.getInt("follower"));

                if(resultSet.getInt("follower")==userId)
                    followersAndFollowing.add(resultSet.getInt("userID"));

            }

            resultSet=statement.executeQuery("SELECT * FROM posts");
            while (resultSet.next()) {
                for (Integer integer : followersAndFollowing)
                    if (resultSet.getInt("userID") == integer)
                        posts.put(resultSet.getInt("postID") ,stringToDate(resultSet.getString("Date")));
            }


           posts= sortByValue2(posts);
           ArrayList<Integer> postID=new ArrayList<>();
           ArrayList<Date> dates=new ArrayList<>();
           postID.addAll(posts.keySet());
           dates.addAll(posts.values());
           ArrayList<Integer> userID=new ArrayList<>();
           ArrayList<String> caption=new ArrayList<>();
           ArrayList<String> username=new ArrayList<>();
           ArrayList<String> date=new ArrayList<>();

         if(postID.size()>10) {
             for (int i = postID.size() - 1; i >= postID.size() - 11; i--) {

                 resultSet = statement.executeQuery("SELECT * FROM posts where postID=" + postID.get(i) + "");
                 while (resultSet.next()) {
                     userID.add(resultSet.getInt("userID"));
                     caption.add(resultSet.getString("caption"));
                     date.add(dateToString(dates.get(i)));
                 }

             }

             for (Integer integer : userID) {
                 ResultSet resultSet1 = statement.executeQuery("SELECT * from users where userID=" + integer + "");
                 while (resultSet1.next())
                     username.add(resultSet1.getString("username"));
             }

             for (int i = 0; i < username.size(); i++) {
                 System.out.println(username.get(i)+" :");
                 System.out.println(caption.get(i));
                 System.out.println("post created on : "+date.get(i));
                 System.out.println("------------------------------------------");
             }
         }

        else {
             for (int i = postID.size() - 1; i >= 0; i--) {

                 resultSet = statement.executeQuery("SELECT * FROM posts where postID=" + postID.get(i) + "");
                 while (resultSet.next()) {
                     userID.add(resultSet.getInt("userID"));
                     caption.add(resultSet.getString("caption"));
                     date.add(dateToString(dates.get(i)));
                 }
             }

             for (Integer integer : userID) {
                 ResultSet resultSet1 = statement.executeQuery("SELECT * from users where userID=" + integer + "");
                 while (resultSet1.next())
                     username.add(resultSet1.getString("username"));
             }

             for (int i = 0; i < username.size(); i++) {
                 System.out.println(username.get(i)+" :");
                 System.out.println(caption.get(i));
                 System.out.println("post created on : "+date.get(i));
                 System.out.println("------------------------------------------");
             }

         }

        System.out.println("____________________________________________");

    }

    public void showUsers() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement = connection.createStatement();
        statement.executeUpdate("use app");

        System.out.println("list of users:");

        ResultSet resultSet=statement.executeQuery("SELECT * from users ");
        while (resultSet.next())
            if (resultSet.getInt("userID")!=this.userId)
            System.out.println(resultSet.getInt("userID")+" : "+resultSet.getString("username"));

        System.out.println("___________________________________________");
    }

}