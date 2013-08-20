// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DataBase.java

package net.hsg.sql;

import java.io.IOException;
import java.sql.*;
// Referenced classes of package net.rile.sql:
//			DbManager

public class DataBase
{

	private Connection conn;
	private String sql;
	private Statement ste;
	private ResultSet rs;
	private PreparedStatement ps;

	public DataBase()
	{
		try
		{
			conn = DbManager.getInstance().getConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		try
		{
			conn = DbManager.getInstance().getConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public void createStatement(String sql)
		throws SQLException, IOException
	{
		if (conn == null)
			conn = DbManager.getInstance().getConnection();
		ps = conn.prepareStatement(sql);
	}

	public void setAttr(int index, String value)
		throws SQLException
	{
		ps.setString(index, value);
	}

	public void setAttr(int index, int value)
		throws SQLException
	{
		ps.setInt(index, value);
	}

	public void setAttr(int index, Date value)
		throws SQLException
	{
		ps.setDate(index, value);
	}

	public void setAttr(int index, Timestamp value)
		throws SQLException
	{
		ps.setTimestamp(index, value);
	}

	public int pexeute()
		throws SQLException
	{
		return ps.executeUpdate();
	}

	public boolean pquery()
	{
		try
		{
			rs = ps.executeQuery();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean execute()
	{
		try
		{
			if (conn == null)
				conn = DbManager.getInstance().getConnection();
			if (ste != null)
				ste.close();
			ste = conn.createStatement();
			ste.executeUpdate(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean execute(String sql)
	{
		setSql(sql);
		try
		{
			if (conn == null)
				conn = DbManager.getInstance().getConnection();
			if (ste != null)
				ste.close();
			ste = conn.createStatement();
			ste.executeUpdate(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String executeStr()
	{
		if (query())
			try
			{
				next();
				return new String(getString(1));
			}
			catch (Exception e)
			{
				return null;
			}
		else
			return null;
	}

	public int executeInt()
	{
		if (query())
		{
			if (next())
				return getInt(1);
			else
				return 0;
		} else
		{
			return 0;
		}
	}

	public int executeInt(String sql)
	{
		setSql(sql);
		if (query())
		{
			if (next())
				return getInt(1);
			else
				return 0;
		} else
		{
			return 0;
		}
	}

	public String executeStr(String sql)
	{
		setSql(sql);
		if (query())
			try
			{
				next();
				return new String(getString(1));
			}
			catch (Exception e)
			{
				return null;
			}
		else
			return null;
	}

	public boolean lastRaw()
	{
		if (rs == null)
			return false;
		try
		{
			return rs.last();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean isLast()
	{
		if (rs == null)
			return false;
		try
		{
			return rs.isLast();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean query()
	{
		try
		{
			if (conn == null)
				conn = DbManager.getInstance().getConnection();
			if (ste != null)
				ste.close();
			if (rs != null)
				rs.close();
			ste = conn.createStatement();
			rs = ste.executeQuery(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean query(String sql)
	{
		setSql(sql);
		try
		{
			if (conn == null)
				conn = DbManager.getInstance().getConnection();
			if (ste != null)
				ste.close();
			if (rs != null)
				rs.close();
			ste = conn.createStatement();
			rs = ste.executeQuery(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean next()
	{
		if (rs == null)
			return false;
		try
		{
			return rs.next();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public String getString(String name)
	{
		try
		{
			return rs.getString(name);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public String getString(int index)
	{
		try
		{
			return rs.getString(index);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public int getInt(String name)
	{
		try
		{
			return rs.getInt(name);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	public int getInt(int index)
	{
		try
		{
			return rs.getInt(index);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	public float getFloat(String name)
	{
		try
		{
			return rs.getFloat(name);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1F;
	}

	public float getFloat(int index)
	{
		try
		{
			return rs.getFloat(index);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1F;
	}

	public boolean close()
	{
		try
		{
			if (rs != null)
				rs.close();
			if (ste != null)
				ste.close();
			if (conn != null)
				conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public String[] getColumns()
	{
		try
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String name[] = new String[count];
			for (int i = 0; i < count; i++)
				name[i] = rsmd.getColumnName(i + 1);

			return name;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public int update(String sql)
	{
		setSql(sql);
		try
		{
			if (conn == null)
				conn = DbManager.getInstance().getConnection();
			if (ste != null)
				ste.close();
			ste = conn.createStatement();
			int res = ste.executeUpdate(sql);
			return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
}
