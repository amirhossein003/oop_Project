import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class InputProcess {

    public static  User createAccount() throws SQLException {

        Scanner scanner=new Scanner(System.in);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM users");

        String username="";
        String password="";
        boolean flag=true;
        while (flag) {
            System.out.println("please enter username!");
            username = scanner.nextLine();
            while (resultSet.next()) {
                if (Objects.equals(resultSet.getString("username"), username)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println("username entered successfully!");
                flag=false;
            }
            else{
                System.out.println("username invalid!");
                flag=true;
            }
        }
        boolean flag2=true;
        while(flag2) {
            System.out.println("please enter password!");
            password=scanner.nextLine();
            if(password.length()>=8 ){
                boolean flag3=true;
                while (flag3) {
                    System.out.println("please enter password again correctly!");
                    String checkPassword = scanner.nextLine();
                    if (password.equals(checkPassword)) {
                        flag2 = false;
                        flag3=false;
                    }

                }
            }
            else {
                System.out.println("password invalid!");
            }

        }

        System.out.println("what was your favorite school teacher's name?");
        String passwordHint=scanner.nextLine();

        System.out.println("createAccount successfully!");

        User user=new User(username,password);
        user.forgetPassword=passwordHint;
        user.saveData();
        user.getUserID();

        return user;
    }

    public static User logIn() throws SQLException {

        Scanner scanner=new Scanner(System.in);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM users");
        boolean flag=true;
        String password="";
        System.out.println("please enter yourUsername!");
        String username=scanner.nextLine();
        while (resultSet.next()){
            int number=0;
            if(Objects.equals(resultSet.getString("username"), username)) {
                System.out.println("please enter yourPassword!");
                password = scanner.nextLine();

                while (flag) {
                    if (Objects.equals(resultSet.getString("password"), password)) {
                        System.out.println("login successfully!");
                        flag=false;


                    }
                    if(flag && number<3) {
                        System.out.println("incorrect password! please try again!");
                        number++;
                        password = scanner.nextLine();
                    }
                    else if(flag){
                        System.out.println("You forgot the password. Please enter the answer security question!");
                        System.out.println("what was your favorite school teacher's name?");
                        String passwordHint=scanner.nextLine();
                        if(Objects.equals(resultSet.getString("forgetPassword"), passwordHint)){
                            System.out.println("login successfully!");
                            flag=false;
                        }
                    }

                }
                break;
            }

        }
        if(flag) {
            System.out.println("username Not found! please try again!");
            return null;
        }
        else {
            return User.getUser(username);
        }
    }

    public static void selectAccountType(User user) throws SQLException {

        Scanner scanner=new Scanner(System.in);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "09012012492");
        Statement statement=connection.createStatement();
        statement.executeUpdate("use app");
        ResultSet resultSet=statement.executeQuery("SELECT * FROM users");

        while (resultSet.next()){
            if(Objects.equals(resultSet.getString("username"), user.username)) {

                System.out.println("please select yourAccountType!");
                System.out.println("1-commercialUser");
                System.out.println("2-personalUser");
                int value=scanner.nextInt();

                if (value==1) {
                    user.business = "commercialUser";
                    System.out.println("your accountType is commercialUser!");
                    statement.executeUpdate("UPDATE users SET business='"+"commercialUser"+"' WHERE username='" + user.username + "'");
                } else if (value==2){
                    user.business = "personalUser";
                    System.out.println("your accountType is personalUser!");
                    statement.executeUpdate("UPDATE users SET business='"+"personalUser"+"' WHERE username='" + user.username + "'");
                }
                break;
            }

        }
    }

    public static void signup(){

        System.out.println("****************************************");
        System.out.println("1-createAccount");
        System.out.println("2-login");
    }

    public static void showMenuPage(){

        System.out.println("****************************************");
        System.out.println("3-showUserPage");
        System.out.println("4-showMyUserPage");
        System.out.println("5-homePage");
        System.out.println("6-showPostPage");
        System.out.println("7-private chats");
        System.out.println("8-group chats");
        System.out.println("9-suggested users");
        System.out.println("10-advertisement posts");
        System.out.println("11-logout");
        System.out.println("12-exit");
    }

    public static void logout(){
        InputProcess.signup();
    }

}