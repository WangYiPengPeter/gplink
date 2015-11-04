import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class GPLink
{

	public static Boolean debug = false;
	public static String hostName = "";

	public static void main(String[] args) throws SQLException
	{
		String targetConfig = "";
		String tableName = "";

		Connection conn = null;

		try
		{

			int argsCount = args.length;
			hostName = InetAddress.getLocalHost().getHostName();

			if (argsCount == 5)
			//Creating a table
			{
				String sourceConfig = args[0];
				String sql = args[1];
				String targetTable = args[2];
				targetConfig = args[3];
				debug = Boolean.valueOf(args[4]);

				conn = CommonDB.connect(targetConfig);
				Listener.startMissingPorts(conn, targetConfig);
				tableName = TargetData.createTable(conn, targetTable, sourceConfig, sql, targetConfig);
				Listener.stopOrphanedPorts(conn, targetConfig);
				conn.close();

				System.out.println("External Table: " + tableName + " CREATED.");
			}
			else if (argsCount == 3)
			{
				targetConfig = args[0];
				tableName = args[1];
				debug = Boolean.valueOf(args[2]);

				conn = CommonDB.connect(targetConfig);
				Listener.startMissingPorts(conn, targetConfig);
				TargetData.dropTable(conn, tableName);
				Listener.stopOrphanedPorts(conn, targetConfig);
				conn.close();
				System.out.println("External Table: " + tableName + " DROPPED.");
			}
			else if (argsCount == 2)
			{
				targetConfig = args[0];
				debug = Boolean.valueOf(args[1]);

				conn = CommonDB.connect(targetConfig);
				Listener.startMissingPorts(conn, targetConfig);
				conn.close();
				System.out.println("Started all ports needed.");
			}
		
		}
		catch (SQLException ex)
		{
			throw new SQLException(ex.getMessage());
		}
		catch (UnknownHostException exu)
		{
			throw new SQLException(exu.getMessage());
		}
		finally
		{
			if (conn != null)
				conn.close();
		}
	}
}
