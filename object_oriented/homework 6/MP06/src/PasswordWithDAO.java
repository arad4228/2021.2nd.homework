import java.util.List;

public class PasswordWithDAO
{
    final static String DB_FILE_NAME = "PasswordDB_SMU";
    final static String DB_TABLE_NAME = "DB_Password_Table";

    public static void main(String[] args)
    {
        PasswordInfo passwordInfo;
        PasswordDAO passwordDAO = new PasswordDAOImplement(DB_FILE_NAME,DB_FILE_NAME);

        System.out.println("Initialization DB and Inserting Password_Data");
        passwordInfo = new PasswordInfo("https://www.smu.ac.kr", "smu","abcde");
        passwordDAO.insert(passwordInfo);
        passwordInfo = new PasswordInfo("https://www.smu2.ac.kr", "smu2","abcde");
        passwordDAO.insert(passwordInfo);

        System.out.println("Printing all Password_DB");
        for(PasswordInfo passwordInfos : passwordDAO.findAll())
        {
            System.out.println(passwordInfos);
        }

        System.out.println("Updating Password_DB");
        System.out.println("Change smu2 to smu1");
        passwordInfo = passwordDAO.findByKey("https://www.smu2.ac.kr");
        passwordInfo.setId("smu1");
        passwordDAO.update(passwordInfo);

        System.out.println("After Updating Password_Data");
        passwordInfo = passwordDAO.findByKey("https://www.smu2.ac.kr");
        System.out.println(passwordInfo);

        System.out.println("Deleting Data from Password_DB");
        System.out.println("Delete id = smu2");
        passwordDAO.delete("https://www.smu2.ac.kr");

        System.out.println("After Deleting Data from DB");
        for(PasswordInfo passwordInfos : passwordDAO.findAll())
        {
            System.out.println(passwordInfos);
        }
    }
}
