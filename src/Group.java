import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Group {
    int groupID;
    String name;
    String bio;
    int admin;
    int pin;

    public Group(int UserId, String name) {
        this.admin=UserId;
        this.name = name;
    }

    public void addUser(int admin,int userID){
        if(this.admin==admin)
            try {
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
                Statement statement= connection.createStatement();
                statement.executeUpdate("use app");

                boolean flag=true;
                ResultSet resultSet=statement.executeQuery("select * from group_user where groupID="+groupID+"");
                while (resultSet.next())
                    if(resultSet.getInt("userID")==userID){
                        flag=false; break;
                    }
                if (flag)
                statement.executeUpdate("INSERT into group_user (groupID,userID) values ("+groupID+","+userID+")");

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
    }

    public void clearHistory(){
        try {

            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            Statement statement1=connection.createStatement();
            statement.executeUpdate("use app");



            ResultSet resultSet=statement.executeQuery("SELECT * FROM group_message WHERE groupID ="+groupID+"");
            while (resultSet.next())
            {
                statement1.executeUpdate("DELETE FROM messages WHERE messageID ="+resultSet.getInt("messageID")+"");
            }
            statement1.executeUpdate("DELETE FROM group_message WHERE groupID="+groupID+"");



        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public int addMessage(String text, int userID)
    {
        boolean Flag=true;

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            ResultSet resultSet=statement.executeQuery("SELECT * FROM group_user WHERE groupID="+groupID);
            while (resultSet.next())
            {
                if(resultSet.getInt("userID")==userID)
                    Flag=!resultSet.getBoolean("ban");
            }


        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        if(Flag)
        {
            Message message= new Message(text,userID,this.groupID);
            message.saveData();
           int messageID= message.getMessageID();

            try {
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
                Statement statement= connection.createStatement();
                statement.executeUpdate("use app");

                statement.executeUpdate("INSERT into group_message (groupID,messageID) values ("+groupID+","+messageID+")");

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
            return message.getMessageID();
        }
        System.out.println("you are ban!");
        return 0;
    }

    public void pinMessage(int messageID){
        this.pin= messageID;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE group_chat SET pin="+pin+" where groupID="+groupID);


        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(int userID,int admin){
        if(this.admin==admin)
        {
            try {
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
                Statement statement= connection.createStatement();
                statement.executeUpdate("use app");

                statement.executeUpdate("DELETE FROM group_user where userID ="+userID);

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void leaveUser(int UserId){
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("DELETE FROM group_user where userID ="+UserId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void editBio(String bio){
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE group_chat set bio='"+bio+"' where groupID ="+groupID);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void editName(String name){
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE group_chat set name='"+name+"' where groupID ="+groupID);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void banUser(int UserId,int admin) {
        if (this.admin == admin) {
            try {
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
                Statement statement= connection.createStatement();
                statement.executeUpdate("use app");

                statement.executeUpdate("UPDATE group_user set ban='"+1+"' where userID ="+UserId);

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeBanUser(int userId,int admin){
        if(this.admin==admin)
        {
            try {
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
                Statement statement= connection.createStatement();
                statement.executeUpdate("use app");

                statement.executeUpdate("UPDATE group_user set ban='"+0+"' where userID ="+userId);

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void saveData()
    {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("INSERT INTO group_chat (name,adminUser) values ('"+name+"',"+admin+")");

            ResultSet resultSet=statement.executeQuery("SELECT * FROM group_chat where name='"+name+"' and  adminUser="+admin+"");
            while (resultSet.next()) {
                statement.executeUpdate("INSERT INTO group_user (groupID,userID) values " +
                        "                     (" + resultSet.getInt("groupID") + "," + admin + ")");
            }


        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }


    public int getGroupID(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM group_chat ");
            while (resultSet.next()){
                if(resultSet.getString("name").equals(name))
                    if(resultSet.getInt("adminUser")==admin)
                    {
                        groupID=resultSet.getInt("groupID");
                        break;
                    }
            }}
        catch (
                SQLException e) {
            e.printStackTrace();
        }


        return groupID;
    }

    public static void showGroupChatPage(int userID) throws SQLException, ParseException {
        System.out.println("****************************");
        System.out.println("1-new group");
        System.out.println("2-select group");
        System.out.println("3-return to menu");

        Scanner scanner1=new Scanner(System.in);
        Scanner scanner=new Scanner(System.in);
        boolean flag=true;

        while (flag){


            int value= Integer.parseInt(scanner1.next());

            if (value==1){
                newGroup(userID);
            }
            if (value==2){
                System.out.println("please enter a groupID to showGroupChatInfo!");
                int groupID=scanner.nextInt();
                selectGroup(groupID,userID);
            }
            if (value==3){
                InputProcess.showMenuPage();
                flag=false;
            }
        }
    }

    public static void newGroup(int userID)  {

        Scanner scanner=new Scanner(System.in);
        System.out.println("please enter a name to groupName");
        String groupName=scanner.nextLine();
        Group group=new Group(userID,groupName);
        group.saveData();
        System.out.println("group created successfully!");

    }
    public static void selectGroup(int groupID,int userID) throws SQLException, ParseException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");

        boolean flag=false;
        ResultSet resultSet1=statement.executeQuery("SELECT * from group_user where groupID="+groupID+"");
        while (resultSet1.next())
            if(resultSet1.getInt("userID")==userID){
                flag=true;
                break;
            }

        if (flag) {
            ResultSet resultSet = statement.executeQuery("SELECT * from group_chat WHERE groupID=" + groupID + "");

            if (!resultSet.next())
                System.out.println("groupID not found! please try again");
            else
                showGroupChatInfo(groupID, userID);
        }
        else
            System.out.println("you aren't member in this group!");

    }

    public static void showGroupChatInfo(int groupID,int userID) throws SQLException, ParseException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * from group_chat WHERE groupID="+groupID+"");

        while (resultSet.next()) {
            int adminUser = resultSet.getInt("adminUser");
            String groupName = resultSet.getString("name");
            Group group = new Group(adminUser, groupName);
            group.getGroupID();


            System.out.println("***************************");
            System.out.println("0-showGroupInfo");
            System.out.println("1-show messages");
            System.out.println("2-send message");
            System.out.println("3-edit message");
            System.out.println("4-reply message");
            System.out.println("5-forward message");
            System.out.println("6-search message");
            System.out.println("7-leave group");
            System.out.println("8-showGroupUser");
            System.out.println("9-return to groupPage");
            System.out.println("10-return to menu");


            if (resultSet.getInt("adminUser") == userID) {
                System.out.println("11-editName");
                System.out.println("12-editBio");
                System.out.println("13-addUser");
                System.out.println("14-removeUser");
                System.out.println("15-addBanUser");
                System.out.println("16-removeBanUser");
                System.out.println("17-pin message");
                System.out.println("18-clear history");
                System.out.println("19-delete message");
            }

        Scanner scanner1=new Scanner(System.in);
        Scanner scanner=new Scanner(System.in);
        boolean flag=true;

        while (flag){


            int value= Integer.parseInt(scanner1.next());

            if(value==0){
             System.out.println("groupName : "+group.name);
             System.out.println("groupID : "+group.groupID);
             System.out.println("bio : "+group.bio);
             System.out.println("____________________________");
            }
            if(value==1){
            Message.showMessages(groupID,false);
            }
            if(value==2){
              System.out.println("please enter a text to send message");
              String text=scanner.nextLine();
              group.addMessage(text,userID);
            }
            if(value==3){

                System.out.println("1-please enter a text to edit message");
                System.out.println("2-please enter a messageID to edit message");
                String text=scanner.nextLine();
                int messageID=scanner.nextInt();
                Message message=Message.getMessage(messageID);
                message.edit(text);
            }
            if(value==4){
                System.out.println("1-please enter a text to reply message!");
                System.out.println("2-please enter a messageID to reply");
                String message=scanner.nextLine();
                int replyMessage=scanner.nextInt();
                int messageID=group.addMessage(message,userID);
                Message message1=Message.getMessage(messageID);
                message1.reply(replyMessage);
            }
            if(value==5){
                System.out.println("please select a messageID to forward");
                int messageID=scanner.nextInt();
                System.out.println("please select a chat to forwardMessage");
                int chatID1=scanner.nextInt();
                Message.forward(chatID1,messageID,false);
            }
            if (value==6){
                System.out.println("please enter a text to search message");
                String text = scanner.nextLine();
                Message.search(text, groupID, false);
            }
            if(value==7){
                group.leaveUser(userID);
            }
            if(value==8){
                group.showGroupUser();
            }
            if (value==9){
               showGroupChatInfo(groupID,userID);
            }
            if(value==10){
               showGroupChatPage(userID);
               flag=false;
            }
            if(resultSet.getInt("adminUser")==userID) {
                if (value == 11) {
                    System.out.println("please enter a name to edit groupName");
                    String name = scanner.nextLine();
                    group.editName(name);
                }
                if (value == 12) {
                    System.out.println("please enter a bio to edit groupBio");
                    String bio = scanner.nextLine();
                    group.editBio(bio);
                }
                if (value == 13) {
                    System.out.println("please enter a userID to add this group");
                    int addedUser = scanner.nextInt();
                    group.addUser(userID,addedUser);
                }
                if (value == 14) {
                    System.out.println("please enter a userID to remove this group");
                    int removedUser = scanner.nextInt();
                    group.removeUser(removedUser, userID);
                }
                if (value == 15) {
                    System.out.println("please enter a userID to banUser");
                    int banUser = scanner.nextInt();
                    group.banUser(banUser, userID);

                }
                if (value == 16) {
                    System.out.println("please enter a userID to removeBanUser");
                    int removeBanUser = scanner.nextInt();
                    group.removeBanUser(removeBanUser, userID);
                }
                if (value == 17) {
                    System.out.println("please enter a messageID to pin message");
                    int messageID = scanner.nextInt();
                    group.pinMessage(messageID);
                }
                if (value == 18) {
                    group.clearHistory();
                }
                if (value == 19) {
                    System.out.println("please enter a messageID to delete message");
                    int messageID = scanner.nextInt();
                    Message message = Message.getMessage(messageID);
                    message.deleteMessage(messageID, false);
                }
             }
          }
        }

    }

    public void showGroupUser() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * from group_user WHERE groupID="+groupID+"");
        ArrayList<Integer> groupUser=new ArrayList<>();
        while (resultSet.next()){
            groupUser.add(resultSet.getInt("userID"));
        }
        System.out.println("group users : ");
        for (Integer integer : groupUser) {
            ResultSet resultSet1 = statement.executeQuery("select * from users where userID=" + integer + "");
            while (resultSet1.next())
            System.out.println(resultSet1.getString("username"));
        }
        System.out.println("___________________________________________");
    }

    public static Group getGroup(int groupID) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM group_chat where groupID="+groupID+"");

        while (resultSet.next()) {
            Group group = new Group(resultSet.getInt("adminUser"), resultSet.getString("name"));
            group.bio = resultSet.getString("bio");
            group.groupID = groupID;
            group.pin = resultSet.getInt("pin");

            return group;
        }
        return null;
    }

}