import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class Chat {

    int chatID;
    int userID1;
    int userID2;
    int pin;


    public Chat( int userID1, int userID2) {

        boolean Flag= false;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            ResultSet resultSet=statement.executeQuery("SELECT * FROM block ");
            while(resultSet.next())
            {
                if(resultSet.getInt("userID")==userID1 || resultSet.getInt("userID")==userID2)
                    if(resultSet.getInt("blockedUser")==userID1 || resultSet.getInt("blockedUser")==userID2)
                    {
                        Flag = true;
                        break;
                    }
            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        if(!Flag){

            this.userID1 = userID1;
            this.userID2 = userID2;}

    }

    public  void pinMessage(int messageID)
    {
        this.pin= messageID;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE chats SET pin="+pin+" where chatID="+chatID);

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

            this.pin=0;

            ResultSet resultSet=statement.executeQuery("SELECT * FROM chat_message WHERE chatID ="+chatID+"");
            while (resultSet.next())
            {
                statement1.executeUpdate("DELETE FROM messages WHERE messageID ="+resultSet.getInt("messageID")+"");
            }
            statement1.executeUpdate("DELETE FROM chat_message WHERE chatID="+chatID+"");


        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public int addMessage(String text, int userID) throws SQLException {

        boolean Flag= false;

            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            ResultSet resultSet=statement.executeQuery("SELECT * FROM block ");
            while(resultSet.next())
            {
                if(resultSet.getInt("userID")==userID2)
                    if(resultSet.getInt("blockedUser")==userID1)
                    {
                        Flag = true;
                        break;
                    }
            }


        if(!Flag){
            Message message= new Message(text,userID,this.chatID);
            message.saveData();
            message.getMessageID();

            statement.executeUpdate("INSERT into chat_message (chatID,messageID) values ("+chatID+","+message.messageID+")");


            return message.getMessageID();
        }
        return 0;
    }

    public void saveData()
    {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");
            String query="INSERT INTO chats (chatID,userID1,userID2) values ("+chatID+","+userID1+","+userID2+")";
            statement.executeUpdate(query);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public int getChatID(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM chats");
            while (resultSet.next()){
                if(resultSet.getInt("userID1")==userID1)
                    if(resultSet.getInt("userID2")==userID2)
                    {
                        chatID=resultSet.getInt("chatID");
                        break;
                    }
            }}
        catch (
                SQLException e) {
            e.printStackTrace();
        }


        return chatID;
    }

    public static void showPrivateChatPage(int UserID) throws SQLException, ParseException {

        System.out.println("*******************************");
        System.out.println("1-new chat");
        System.out.println("2-select chat");
        System.out.println("3-return to menu");

        Scanner scanner=new Scanner(System.in);
        Scanner scanner1=new Scanner(System.in);
        boolean flag=true;

        while (flag){

            int value= Integer.parseInt(scanner1.next());

            if(value==1){
                System.out.println("please enter a userID to chat with him!");
                int userID=scanner.nextInt();
                newChat(UserID,userID);
            }
            if (value==2){

                showPrivateChats(UserID);
                System.out.println("please enter a chatID to chat!");
                int chatID=scanner.nextInt();
                selectChat(chatID,UserID);
            }
            if(value==3){
                InputProcess.showMenuPage();
                flag=false;
            }
        }
    }

   public static void newChat(int UserID1,int userID2) throws SQLException {
       Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
       Statement statement= connection.createStatement();
       Statement statement1= connection.createStatement();
       Statement statement2= connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("SELECT * from users WHERE userID="+userID2+"");

           if (resultSet.next()) {

               ResultSet resultSet1=statement1.executeQuery("select * from chats where userID1="+UserID1+" and" +
                       " userID2="+userID2+"");
               ResultSet resultSet2=statement2.executeQuery("select * from chats where userID1="+userID2+" and" +
                       " userID2="+UserID1+"");
               if (resultSet1.next() || resultSet2.next()){
                   System.out.println("You have an active chat with this user!");
               }
               else {
                   statement.executeUpdate("INSERT INTO  chats (userID1,userID2) values (" + UserID1 + "," + userID2 + ")");
                   System.out.println("chat created successfully!");
               }
           } else
               System.out.println("userID not found! please try again");

   }

   public   static void selectChat(int chatID,int userID) throws SQLException, ParseException {
       Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
       Statement statement= connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("SELECT * from chats WHERE chatID="+chatID+"");
           while (resultSet.next())
           if ( (resultSet.getInt("userID1")==userID || resultSet.getInt("userID2")==userID))
               showChatInfo(chatID);
           else {
           System.out.println("chatID not found! please try again");
           }

   }

   public static void showPrivateChats(int userID) throws SQLException {

       Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
       Statement statement= connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("select * from chats where userID1="+userID+" or userID2="+userID+"");
       ArrayList<Integer> UserID=new ArrayList<>();
       ArrayList<Integer> chatID=new ArrayList<>();


       while (resultSet.next()){
           if (resultSet.getInt("userID1")!=userID){
               UserID.add(resultSet.getInt("userID1"));
               chatID.add(resultSet.getInt("chatID"));
           }
           if (resultSet.getInt("userID2")!=userID) {
               UserID.add(resultSet.getInt("userID2"));
               chatID.add(resultSet.getInt("chatID"));
           }
       }

       System.out.println("list of privateChats :");
       for (int i = 0; i < UserID.size(); i++) {
           System.out.println( chatID.get(i) + " : " + userIDToUsername(UserID.get(i)));
       }
       System.out.println("___________________________________________________");
   }

    public static String userIDToUsername(int userID) throws SQLException {

        String username="";
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("select * from users where userID="+userID+"");
        while (resultSet.next())
            username=resultSet.getString("username");

        return username;
    }

   public static void showChatInfo(int chatID) throws SQLException, ParseException {

       Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
       Statement statement= connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("SELECT * from chats WHERE chatID="+chatID+"");

       while (resultSet.next()){
       int userID1=resultSet.getInt("userID1");
       int userID2=resultSet.getInt("userID2");
       Chat chat=new Chat(userID1,userID2);
       chat.getChatID();

       System.out.println("***************************");
       System.out.println("1-show messages!");
       System.out.println("2-send message");
       System.out.println("3-edit message");
       System.out.println("4-delete message");
       System.out.println("5-reply message");
       System.out.println("6-forward message");
       System.out.println("7-search message");
       System.out.println("8-pin message");
       System.out.println("9-clear history");
       System.out.println("10-block user");
       System.out.println("11-return to chatPage");
       System.out.println("12-return to menu");

           Scanner scanner=new Scanner(System.in);
           Scanner scanner1=new Scanner(System.in);
           boolean flag=true;

       while (flag) {

           int value= Integer.parseInt(scanner1.next());

           if (value == 1) {
               Message.showMessages(chatID, true);
           }
           if (value == 2) {
               System.out.println("please enter a text to send message!");
               String message = scanner.nextLine();
               chat.addMessage(message, chat.userID1);
           }
           if (value == 3) {
               System.out.println("1-please enter a text to edit message");
               System.out.println("2-please enter a messageID to edit message");
               String text = scanner.nextLine();
               int messageID = scanner.nextInt();
               Message message = Message.getMessage(messageID);
               message.edit(text);
           }
           if (value == 4) {
               System.out.println("please enter a messageID to delete message");
               int messageID = scanner.nextInt();
               Message message = Message.getMessage(messageID);
               message.deleteMessage(messageID, true);

           }
           if (value == 5) {
               System.out.println("1-please enter a text to reply message!");
               System.out.println("2-please enter a messageID to reply");
               String message = scanner.nextLine();
               int replyMessage = scanner.nextInt();
               int messageID = chat.addMessage(message, chat.userID1);
               Message message1 = Message.getMessage(messageID);
               message1.reply(replyMessage);

           }
           if (value == 6) {
               System.out.println("please select a messageID to forward");
               int messageID = scanner.nextInt();
               System.out.println("please select a chat to forwardMessage");
               int chatID1 = scanner.nextInt();
               Message.forward(chatID1, messageID, true);
           }
           if (value == 7) {
               System.out.println("please enter a text to search message");
               String text = scanner.nextLine();
               Message.search(text, chatID, true);
           }
           if (value == 8) {
               System.out.println("please select a message to pin!");
               int messageID = scanner.nextInt();
               chat.pinMessage(messageID);
           }
           if (value == 9) {
               chat.clearHistory();
           }
           if (value == 10) {
               User user = User.getUser(userID1);
               user.block(userID2);
           }
           if (value == 11) {
               showChatInfo(chatID);
           }
           if (value == 12) {
               Chat.showPrivateChatPage(userID1);
               flag=false;
           }

         }
       }
   }

   public static Chat getChat(int chatID) throws SQLException {

       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
       Statement statement=connection.createStatement();
       statement.executeUpdate("use app");
       ResultSet resultSet=statement.executeQuery("SELECT * FROM chats where chatID="+chatID+"");

       while (resultSet.next()) {
           Chat chat = new Chat(resultSet.getInt("userID1"), resultSet.getInt("userID2"));
           chat.chatID = chatID;
           chat.pin = resultSet.getInt("pin");

           return chat;
       }
       return null;
   }

}