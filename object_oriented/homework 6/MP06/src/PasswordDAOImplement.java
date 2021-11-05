import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PasswordDAOImplement implements PasswordDAO{
    private String DB_TABLE_NAME;

    Connection connection = null;
    ResultSet rs = null;
    Statement statement = null;

    private String CreateTable()
    {
        final String table = " (url text PRIMARY KEY, id text, password text)";
        return table;
    }

    public PasswordDAOImplement(String DB_FILE_NAME, String DB_TABLE_NAME)
    {
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE_NAME);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            this.DB_TABLE_NAME = DB_TABLE_NAME;

            //table 생성
            statement.executeUpdate("DROP TABLE IF EXISTS "+DB_TABLE_NAME);
            statement.executeUpdate("CREATE TABLE " + DB_TABLE_NAME + CreateTable());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(PasswordInfo p) {
        try {
            String fmt = "INSERT INTO %s VALUES('%s', '%s', '%s')";
            String q = String.format(fmt, DB_TABLE_NAME, p.getUrl(), p.getId(), p.getPassword());
            statement.execute(q);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<PasswordInfo> findAll() {
        ArrayList<PasswordInfo> passwordTable = new ArrayList<PasswordInfo>();

        try{
            rs =statement.executeQuery("SELECT * FROM " + DB_TABLE_NAME);
            while(rs.next())
            {
                passwordTable.add(new PasswordInfo(rs.getString("url"),
                        rs.getString("id"),rs.getString("password")));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordTable;
    }

    @Override
    public PasswordInfo findByKey(String url) {
        PasswordInfo temp_PasswordInfo = null;
        try{
            String fmt = "SELECT * FROM %s WHERE url = '%s'";
            String q = String.format(fmt, DB_TABLE_NAME, url);
            rs = statement.executeQuery(q);

            if(rs.next())
            {
                temp_PasswordInfo = new PasswordInfo(rs.getString("url"),
                        rs.getString("id"), rs.getString("password"));
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return temp_PasswordInfo;
    }

    @Override
    public void update(PasswordInfo p) {
        if(findByKey(p.getUrl()) != null)
        {
            try{
                String fmt = "UPDATE %s SET id = '%s', password = '%s' WHERE url = '%s'";
                String q = String.format(fmt, DB_TABLE_NAME, p.getId(), p.getPassword(), p.getUrl());

                statement.execute(q);
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String url) {
        try{
            String fmt = "DELETE FROM %s WHERE url = '%s'";
            String q = String.format(fmt, DB_TABLE_NAME, url);
            statement.execute(q);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(PasswordInfo p) {
        delete(p.getUrl());
    }
}
