public class Main {
    public static void main(String[] args)
    {
        PasswordInfo passwordInfo;
        DAO<PasswordInfo, String> passwordInfoDAO = new PasswordInfoDAOImplement("DB_Password_Table");

        System.out.println("Initialization DB and Inserting Password_Data");
        passwordInfo = new PasswordInfo("https://www.smu.ac.kr", "smu","abcde");
        passwordInfoDAO.insert(passwordInfo);
        passwordInfo = new PasswordInfo("https://www.smu2.ac.kr", "smu2","abcde");
        passwordInfoDAO.insert(passwordInfo);
        System.out.println("Printing all Password_DB");
        for(PasswordInfo passwordInfos : passwordInfoDAO.findAll())
        {
            System.out.println(passwordInfos);
        }

        System.out.println("Updating Password_DB");
        System.out.println("Change smu2 to smu1");
        passwordInfo = passwordInfoDAO.findByKey("https://www.smu2.ac.kr");
        passwordInfo.setId("smu1");
        passwordInfoDAO.update(passwordInfo);

        System.out.println("After Updating Password_Data");
        passwordInfo = passwordInfoDAO.findByKey("https://www.smu2.ac.kr");
        System.out.println(passwordInfo);

        System.out.println("Deleting Data from Password_DB");
        System.out.println("Delete id = smu2");
        passwordInfoDAO.deleteByKey("https://www.smu2.ac.kr");

        System.out.println("After Deleting Data from DB");
        for(PasswordInfo passwordInfos : passwordInfoDAO.findAll())
        {
            System.out.println(passwordInfos);
        }
    }
}
