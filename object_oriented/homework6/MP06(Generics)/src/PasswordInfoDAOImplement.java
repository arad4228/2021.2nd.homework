import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PasswordInfoDAOImplement extends DAOImplement<PasswordInfo, String>
{
    final static String DB_FILE_NAME = "PasswordDB_SMU";
    Connection connection = null;
    ResultSet rs = null;
    Statement statement = null;

    private String CreateTable()
    {
        final String table = " (url text PRIMARY KEY, id text, password text)";
        return table;
    }

    public PasswordInfoDAOImplement(String DB_TABLE_NAME)
    {
        super(DB_TABLE_NAME);
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE_NAME);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //table 생성
            statement.executeUpdate("DROP TABLE IF EXISTS "+DB_TABLE_NAME);
            statement.executeUpdate("CREATE TABLE " + DB_TABLE_NAME + CreateTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

    @Override
    public String getInsertValueStr(PasswordInfo data) {
        String fmt ="'%s','%s', '%s'";
        String q = String.format(fmt, data.getKey(), data.getId(), data.getPassword());
        return q;
    }

    @Override
    public PasswordInfo getNewData(ResultSet rs) {
        PasswordInfo temp = null;
        if(rs != null)
        {
            try{
                temp = new PasswordInfo(rs.getString("url"),
                        rs.getString("id"), rs.getString("password"));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return temp;
    }

    @Override
    public String getUpdateValueStr(PasswordInfo data) {
        String fmt ="id = '%s', password = '%s'";
        String q = String.format(fmt, data.getId(), data.getPassword());
        return q;
    }

    @Override
    public String getKeyColumnName() {
        return "url";
    }

}
