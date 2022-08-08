import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Message {

    String text;
    int messageID;
    int userID;
    int chatID;
    Date date;
    boolean forwarded=false;
    int repliedTO;
    boolean seen;


    public Message(String text, int userID, int chatID) {
        this.text = text;
        this.userID = userID;
        this.chatID = chatID;
        this.date = new Date();
    }

    public void edit(String text){

        if(!forwarded) {
            this.text = text;
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
                Statement statement = connection.createStatement();
                statement.executeUpdate("use app");

                statement.executeUpdate("UPDATE messages SET text='" + text + "' where messageID=" + messageID);

            } catch (
                    SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteMessage(int messageId,boolean flag){
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            if (flag)
               statement.executeUpdate("DELETE FROM chat_message where messageID ="+messageId);
            else
               statement.executeUpdate("DELETE FROM group_message where messageID ="+messageId);
               statement.executeUpdate("DELETE FROM messages WHERE messageID ="+messageId);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }


    public void reply(int messageID) {this.repliedTO=messageID;
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("UPDATE messages SET repliedTo='"+repliedTO+"' where messageID="+this.messageID);

        } catch (
                SQLException e) {
            e.printStackTrace();
        }}

    public static void forward(int chatID,int messageID,boolean flag) throws SQLException, ParseException {

        Message forwardMessage=Message.getMessage(messageID);
        Message message= new Message(forwardMessage.text, forwardMessage.userID,chatID);
        message.forwarded=true;
        message.saveData();
        int MessageID=message.getMessageID();


        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
        Statement statement= connection.createStatement();
        statement.executeUpdate("use app");
        if (flag)
           statement.executeUpdate("INSERT into chat_message (chatID,messageID) values ("+chatID+","+MessageID+")");
        else
           statement.executeUpdate("INSERT into group_message (groupID,messageID) values ("+chatID+","+MessageID+")");

    }

    public void saveData()
    {
        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            statement.executeUpdate("INSERT INTO messages (text,userID,chatID,date ,forwarded,repliedTo,seen) values " +
                    "                     ('"+text+"',"+userID+","+chatID+",'"+dateToString(date)+"',"+forwarded+","+repliedTO+","+seen+")");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMessageID(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
            Statement statement=connection.createStatement();
            statement.executeUpdate("use app");
            ResultSet resultSet=statement.executeQuery("SELECT * FROM messages");
            while (resultSet.next()){
                if(resultSet.getString("text").equals(text)
                        && resultSet.getInt("userID")==userID
                && resultSet.getInt("chatID")==chatID
                        && resultSet.getString("date").equals(dateToString(date)))
                            {
                                messageID=resultSet.getInt("messageID");
                                break;
                            }
            }}
        catch (
                SQLException e) {
            e.printStackTrace();
        }


        return messageID;
    }

    public static Message getMessage(int messageID) throws SQLException, ParseException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM messages where messageID="+messageID+"");

       while (resultSet.next()) {
           Message message = new Message(resultSet.getString("text"), resultSet.getInt("userID"), resultSet.getInt("chatID"));
           message.messageID = messageID;
           message.text = resultSet.getString("text");
           message.userID = resultSet.getInt("userID");
           message.chatID = resultSet.getInt("chatID");
           message.forwarded = resultSet.getBoolean("forwarded");
           message.repliedTO = resultSet.getInt("repliedTO");
           message.date = stringToDate(resultSet.getString("date"));

           return message;
       }
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

    public static String dateToString2(Date date){

        DateFormat format=new SimpleDateFormat("hh:mm");
        return format.format(date);
    }

    public static String dateToString(Date date){

        DateFormat format=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return format.format(date);
    }

    public static void search(String string,int chatID,boolean flag)
    {
        ArrayList<Integer> message= new ArrayList<>();
        ArrayList<String> messageText= new ArrayList<>();
        ArrayList<Integer> messageID=new ArrayList<>();
        ArrayList<String> messageDate=new ArrayList<>();

        try {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","09012012492");
            Statement statement= connection.createStatement();
            statement.executeUpdate("use app");

            if(flag) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM chat_message ");

                while (resultSet.next()) {
                    if (resultSet.getInt("chatID") == chatID)
                        message.add(resultSet.getInt("messageID"));
                }
            }
            else {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM group_message ");

                while (resultSet.next()) {
                    if (resultSet.getInt("groupID") == chatID)
                        message.add(resultSet.getInt("messageID"));
                }
            }

         ResultSet  resultSet=statement.executeQuery("SELECT * FROM messages ");
            while(resultSet.next())
            {
                for (Integer integer : message) {
                    if (resultSet.getInt("messageID") == integer)
                        if (resultSet.getString("text").contains(string)){
                            messageText.add(resultSet.getString("text"));
                            messageID.add(resultSet.getInt("messageID"));
                            messageDate.add(resultSet.getString("date"));
                        }
                }
            }
            for (int i = 0; i < messageID.size(); i++) {
                System.out.println("messageID : "+messageID.get(i));
                System.out.println(messageDate.get(i));
                for (int i1=0;i1<5;i1++) {
                    System.out.print(messageText.get(i).charAt(i1));
                }
                System.out.println(" . . .");
                System.out.println("***************************");
            }

            Scanner scanner=new Scanner(System.in);
            System.out.println("please enter a messageID to show message");
            int messageID1=scanner.nextInt();

            for (int i = 0; i < messageID.size(); i++) {
                if(messageID.get(i)==messageID1){
                    System.out.println("messageID : "+messageID.get(i));
                    System.out.println(messageDate.get(i));
                    System.out.println(messageText.get(i));
                    break;
                }
            }
            System.out.println("______________________________________");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showMessages(int chatID,boolean flag) throws SQLException {

        ArrayList<Integer> messageId= new ArrayList<>();
        Map<Integer, Date> messages= new HashMap<>();

        ArrayList<Integer> userID=new ArrayList<>();
        ArrayList<String> text=new ArrayList<>();
        ArrayList<Date> date=new ArrayList<>();


        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        if(flag) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat_message");
            while (resultSet.next()) {
                if (resultSet.getInt("chatID") == chatID)
                    messageId.add(resultSet.getInt("messageID"));

            }
        }
        else {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM group_message");
            while (resultSet.next()) {
                if (resultSet.getInt("groupID") == chatID)
                    messageId.add(resultSet.getInt("messageID"));
            }
        }

        ResultSet resultSet=statement.executeQuery("SELECT * FROM messages");
        while (resultSet.next()) {
            for (Integer integer : messageId)
                if (resultSet.getInt("messageID") == integer)
                    messages.put(integer ,stringToDate(resultSet.getString("Date")));
        }


        messages= sortByValue2(messages);
        ArrayList<Integer> messageID=new ArrayList<>();
        messageID.addAll(messages.keySet());



        if(flag) {
            Chat chat = Chat.getChat(chatID);

            if (chat.pin != 0) {
                ResultSet resultSet1 = statement.executeQuery("SELECT * from messages where messageID=" + chat.pin + "");
                while (resultSet1.next()) {
                    System.out.println("pin message!"+" : "+resultSet1.getString("text"));
                }
            }
        }
        else {
            Group group = Group.getGroup(chatID);

            if (group.pin != 0) {
                ResultSet resultSet1 = statement.executeQuery("SELECT * from messages where messageID=" + group.pin + "");
                while (resultSet1.next())
                    System.out.println("pin message! : "+ resultSet1.getString("text"));
            }
        }

        for (Integer message : messageID) {
            ResultSet resultSet3 = statement.executeQuery("SELECT * from messages where messageID=" + message + "");
            while (resultSet3.next()) {
                if (resultSet3.getBoolean("forwarded")) {
                    System.out.println("message forwarded!");
                }

                if (resultSet3.getInt("repliedTo") != 0) {
                    int replyMessageID = resultSet3.getInt("repliedTo");
                    ResultSet resultSet2 = statement.executeQuery
                            ("SELECT * from messages where messageID=" + replyMessageID + "");
                    while (resultSet2.next()) {
                        String replyMessage = resultSet2.getString("text");
                        if (replyMessage.length() <= 10) {
                            for (int i = 0; i < replyMessage.length(); i++) {
                                System.out.print(replyMessage.charAt(i));
                            }
                            System.out.println("");
                        } else {
                            for (int i = 0; i < 10; i++) {
                                System.out.print(replyMessage.charAt(i));
                            }
                            System.out.println("  . . .");
                        }
                    }
                }


                resultSet3= statement.executeQuery("SELECT * from messages where messageID=" + message + "");
                while (resultSet3.next()) {
                    userID.add(resultSet3.getInt("userID"));
                    text.add(resultSet3.getString("text"));
                    date.add(stringToDate(resultSet3.getString("Date")));
                }

            }
        }

        ArrayList<String> username=new ArrayList<>();
        for (Integer integer : userID) {
            ResultSet resultSet2 = statement.executeQuery("SELECT * from users where userID=" + integer + "");
            while (resultSet2.next())
                username.add(resultSet2.getString("username"));
        }

        for (int i = 0; i < username.size(); i++) {
            System.out.println(username.get(i)+" : "+text.get(i)+"     "+dateToString2(date.get(i)));
        }

        System.out.println("________________________________________________");
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