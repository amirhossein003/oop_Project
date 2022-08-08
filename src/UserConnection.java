import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class UserConnection {
    public static void Start() throws SQLException, ParseException {

        Scanner scanner=new Scanner(System.in);
        Scanner scanner1=new Scanner(System.in);
        User user=new User();
        InputProcess.signup();

        while (true){


            int value= Integer.parseInt(scanner1.next());

            if(value==1){
                user=InputProcess.createAccount();
                InputProcess.selectAccountType(user);
                InputProcess.showMenuPage();
            }
            else if(value==2){
                user=InputProcess.logIn();
                if (user==null)
                    InputProcess.signup();
                else
                    InputProcess.showMenuPage();
            }
            else if(value==3){

                System.out.println("please enter a userID to showUserPage!");
                int userID=scanner.nextInt();
                User user2=User.getUser(userID);
                user2.viewPage();
                user.showUserPage(user2);

            }
            else if(value==4){
                User.showMyUserPage(user);
            }
            else if(value==5){
                System.out.println("HomePage : +\n");
                user.recentPost(user.userId);
            }
            else if(value==6){

                System.out.println("please enter a postID to showPostPage!");
                int postID=scanner.nextInt();
                Post post=Post.getPost(postID);
                post.viewPost(user.userId);
                post.showPostPage(user.userId);

            }
            else if(value==7){
               Chat.showPrivateChatPage(user.userId);
            }
            else if(value==8){
              Group.showGroupChatPage(user.userId);
            }
            else if (value==9){
              user.suggestion();
            }
            else if(value==10){
              InputProcess.logout();
            }
            else if(value==11){
              System.exit(0);
            }

        }

    }

}
