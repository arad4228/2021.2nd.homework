import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public abstract  class DAOImplement<D extends DBData<K>, K> implements DAO<D, K> {
    private String DB_TABLE_NAME;

    public DAOImplement(String DB_TABLE_NAME)
    {
        this.DB_TABLE_NAME = DB_TABLE_NAME;
    }

    public abstract Statement getStatement();

    public abstract String getInsertValueStr(D data);

    public abstract D getNewData(ResultSet rs);

    public abstract String getUpdateValueStr(D data);

    public abstract String getKeyColumnName();

    @Override
    public void insert(D data) {
        try {
            String fmt = "INSERT INTO %s VALUES(%s)";
            String q = String.format(fmt,DB_TABLE_NAME, getInsertValueStr(data));
            getStatement().execute(q);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<D> findAll() {
        ArrayList<D> dataList = new ArrayList<D>();
        ResultSet rs;
        try{
            rs = getStatement().executeQuery("SELECT * FROM " + DB_TABLE_NAME);
            while(rs.next())
            {
                dataList.add(getNewData(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return dataList;
    }

    @Override
    public D findByKey(K key) {
        D dataTemp = null;
        ResultSet rs;
        try{
            String fmt = "SELECT * FROM %s WHERE %s = '%s'";
            String q = String.format(fmt, DB_TABLE_NAME, getKeyColumnName(), key.toString());
            rs = getStatement().executeQuery(q);

            if(rs.next())
            {
                dataTemp = getNewData(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return dataTemp;
    }

    @Override
    public void update(D data) {
        D data2 = findByKey(data.getKey());
        if(data2 != null)
        {
            try{
                String fmt = "UPDATE %s SET %s WHERE %s = '%s'";
                String q = String.format(fmt, DB_TABLE_NAME, getUpdateValueStr(data),
                        getKeyColumnName(), data.getKey().toString());

                getStatement().execute(q);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteByKey(K key) {
        try{
            String fmt = "DELETE FROM %s WHERE %s = '%s'";
            String q = String.format(fmt, DB_TABLE_NAME, getKeyColumnName(), key.toString());
            getStatement().execute(q);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(D data)
    {
        deleteByKey(data.getKey());
    }
}
