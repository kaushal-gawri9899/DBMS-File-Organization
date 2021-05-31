import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class AddToDerby {

	private final String F_NAME = "Pedestrian_Counting_System_-_Monthly__counts_per_hour_.csv";
	private  final String D_REQ = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String D_URL = "jdbc:derby:assignOne;create=true";


	public void GenerateTableCount(Connection connect) throws SQLException
	{
		String toTableDrop = "DROP TABLE PEDESTRIANCOUNT";
		String generateTable = "CREATE TABLE PEDESTRIANCOUNT (ID INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				"HOURLY_COUNT VARCHAR(10))";

		String generateIndex = "CREATE INDEX pedCount ON PEDESTRIANCOUNT(HOURLY_COUNT)";

		try{
			PreparedStatement dp = connect.prepareStatement(toTableDrop);
			dp.execute();
		}
		catch (Exception ep)
		{
			System.out.println("Message: Creating a Table as table does not exist");
		}

		PreparedStatement generate = connect.prepareStatement(generateTable);
		generate.execute();
		PreparedStatement index = connect.prepareStatement(generateIndex);
		index.execute();
	}


	public void GenerateTableSensor(Connection connect) throws SQLException
	{
		String toTableDrop = "DROP TABLE PEDESTRIANSENSOR";
		String generateTable = "CREATE TABLE PEDESTRIANSENSOR (ID INTEGER       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				"SENSOR_ID VARCHAR(10), SENSOR_NAME VARCHAR(100))";

		String generateIndex = "CREATE INDEX pedID ON PEDESTRIANSENSOR(SENSOR_ID)";
		try{
			PreparedStatement dp = connect.prepareStatement(toTableDrop);
			dp.execute();
		}
		catch (Exception ep)
		{
			System.out.println("Message: Creating a Table as table does not exist");
		}

		PreparedStatement generate = connect.prepareStatement(generateTable);
		generate.execute();
		PreparedStatement index = connect.prepareStatement(generateIndex);
		index.execute();
	}

	public void GenerateTableTime(Connection connect) throws SQLException
	{
		String toTableDrop = "DROP TABLE PEDESTRIANTIME";
		String generateTable = "CREATE TABLE PEDESTRIANTIME (ID INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
				"DATE_TIME VARCHAR(35),  YE_AR VARCHAR(10), MONTH VARCHAR(15), M_DATE VARCHAR(10), DAY VARCHAR(10), TIME VARCHAR(10))";

		String generateIndex = "CREATE INDEX pedTime ON PEDESTRIANTIME(YE_AR)";
		try{
			PreparedStatement dp = connect.prepareStatement(toTableDrop);
			dp.execute();
		}
		catch (Exception ep)
		{
			System.out.println("Message: Creating a Table as table does not exist");
		}

		PreparedStatement generate = connect.prepareStatement(generateTable);
		generate.execute();
		PreparedStatement index = connect.prepareStatement(generateIndex);
		index.execute();
	}




	public void insertEachRowTime(Connection connect) throws SQLException, IOException
	{
		//String row = "INSERT INTO PEDESTRIAN(DATE_TIME, YEAR, MONTH, M_DATE, DAY, TIME, SENSOR_ID, SENSOR_NAME, HOURLY_COUNTS) VALUES (?,?,?,?,?,?,?,?,?)";
		String row = "INSERT INTO PEDESTRIANTIME(DATE_TIME, YE_AR, MONTH, M_DATE, DAY, TIME) VALUES (?,?,?,?,?,?)";
		final int size_of_batch = 20000;
		int batchCount = 0;
		connect.setAutoCommit(false);
		@SuppressWarnings({"depreciation","resource"})
		CSVReader read = new CSVReader(new FileReader(F_NAME),'\t');

		String[] list;

		PreparedStatement addTo = connect.prepareStatement(row);
		list = read.readNext();


		while((list= read.readNext())!=null)
		{
			System.out.println(list[0]);
			String newList = list[0];
			String[] arr = newList.split("[,]",0);

			for(int i=1; i<7; i++)
			{
				addTo.setString(i, arr[i]);
			}
			addTo.addBatch();

			//Once batch reached the sizeofbatch, we execute it
			if(++batchCount % size_of_batch ==0)
			{
				addTo.executeBatch();
			}

		}
		addTo.executeBatch();

		addTo.close();
		connect.commit();
		connect.close();


	}

	public void insertToPedestrianSensor(Connection connect) throws SQLException, IOException {
		String row = "INSERT INTO PEDESTRIANSENSOR(SENSOR_ID, SENSOR_NAME) VALUES (?,?)";
		final int size_of_batch = 20000;
		int batchCount = 0;
		connect.setAutoCommit(false);
		@SuppressWarnings({"depreciation","resource"})
		CSVReader read = new CSVReader(new FileReader(F_NAME),'\t');

		String[] list;

		PreparedStatement addTo = connect.prepareStatement(row);
		list = read.readNext();


		while((list= read.readNext())!=null)
		{
			System.out.println(list[0]);
			String newList = list[0];
			String[] arr = newList.split("[,]",0);

			addTo.setString(1, arr[7]);
			addTo.setString(2, arr[8]);

			addTo.addBatch();

			//Once batch reached the sizeofbatch, we execute it
			if(++batchCount % size_of_batch ==0)
			{
				addTo.executeBatch();
			}

		}
		addTo.executeBatch();

		addTo.close();
		connect.commit();
		connect.close();

	}

	public void insertToPedestrianCount(Connection connect) throws SQLException, IOException {
		String row = "INSERT INTO PEDESTRIANCOUNT(HOURLY_COUNT) VALUES (?)";
		final int size_of_batch = 20000;
		int batchCount = 0;
		connect.setAutoCommit(false);
		@SuppressWarnings({"depreciation","resource"})
		CSVReader read = new CSVReader(new FileReader(F_NAME),'\t');

		String[] list;

		PreparedStatement addTo = connect.prepareStatement(row);
		list = read.readNext();


		while((list= read.readNext())!=null)
		{
			System.out.println(list[0]);
			String newList = list[0];
			String[] arr = newList.split("[,]",0);

			addTo.setString(1, arr[9]);

			addTo.addBatch();

			//Once batch reached the sizeofbatch, we execute it
			if(++batchCount % size_of_batch ==0)
			{
				addTo.executeBatch();
			}

		}
		addTo.executeBatch();

		addTo.close();
		connect.commit();
		connect.close();

	}

	public static void main(String[] args)
	{
		long initialTime = System.currentTimeMillis();

		AddToDerby derby = new AddToDerby();
		try {
			Class.forName(derby.D_REQ);
			Connection connect = DriverManager.getConnection(derby.D_URL);

			//Uncomment this and comment the rest of statements in try clause below this
			//To use derby and create table without normalization
			//      derby.GenerateTable(connect);
			//      derby.insertEachRow(connect);

			derby.GenerateTableTime(connect);
			Connection connectSensor = DriverManager.getConnection(derby.D_URL);
			derby.GenerateTableSensor(connectSensor);
			Connection connectCount = DriverManager.getConnection(derby.D_URL);
			derby.GenerateTableCount(connectCount);

			derby.insertEachRowTime(connect);
			derby.insertToPedestrianSensor(connectSensor);
			derby.insertToPedestrianCount(connectCount);

		} catch (Exception e) {
			e.printStackTrace();
		}

		long  finalTime = System.currentTimeMillis();
		System.out.println("Total Time For Loading Data: "+ (finalTime-initialTime) + "ms");

		try{
			Class.forName(derby.D_REQ);
			Connection con = DriverManager.getConnection(derby.D_URL);

			Statement stmt = con.createStatement();
			//      String queryOne = "SELECT PEDESTRIANCOUNT.ID, PEDESTRIANSENSOR.SENSOR_ID, PEDESTRIANSENSOR.SENSOR_NAME, PEDESTRIANCOUNT.HOURLY_COUNT FROM PEDESTRIANCOUNT INNER JOIN PEDESTRIANSENSOR ON PEDESTRIANCOUNT.ID = PEDESTRIANSENSOR.ID FETCH FIRST 10 ROWS ONLY";
			//      String queryTwo = "SELECT PEDESTRIANTIME.ID, PEDESTRIANTIME.DATE_TIME, PEDESTRIANSENSOR.SENSOR_NAME FROM PEDESTRIANTIME INNER JOIN PEDESTRIANSENSOR ON PEDESTRIANTIME.ID = PEDESTRIANSENSOR.ID WHERE PEDESTRIANSENSOR.SENSOR_NAME LIKE 'Flagstaff Station' FETCH FIRST 10 ROWS ONLY";
			//      String queryThree = "SELECT PEDESTRIANTIME.ID, PEDESTRIANTIME.DATE_TIME, PEDESTRIANTIME.M_DATE, PEDESTRIANSENSOR.SENSOR_ID, PEDESTRIANCOUNT.HOURLY_COUNT FROM PEDESTRIANTIME INNER JOIN PEDESTRIANSENSOR ON PEDESTRIANTIME.ID = PEDESTRIANSENSOR.ID INNER JOIN PEDESTRIANCOUNT ON PEDESTRIANCOUNT.ID = PEDESTRIANSENSOR.ID FETCH FIRST 10 ROWS ONLY";

//                      String queryOne = "SELECT PEDESTRIANCOUNT.ID,
//                      long inOne = System.currentTimeMillis();
//                      stmt.execute(queryOne);
//                      long fiOne = System.currentTimeMillis();
//                      long inTwo = System.currentTimeMillis();
//                      stmt.execute(queryTwo);
//                      long fiTwo = System.currentTimeMillis();
//                      long inThree = System.currentTimeMillis();
//                      stmt.execute(queryThree);
//                      long fiThree = System.currentTimeMillis();
//
//                      System.out.println("Time Taken for query 1 : "+ (fiOne-inOne) + " ms");
//                       System.out.println("Time Taken for query 2 : "+ (fiTwo-inTwo) + " ms");
//                       System.out.println("Time Taken for query 3 : "+ (fiThree-inThree) + " ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Use these methods and call inside main for without normalization
/*        public void GenerateTable(Connection connect) throws SQLException
        {
                String toTableDrop = "DROP TABLE PEDESTRIAN";
                String generateTable = "CREATE TABLE PEDESTRIAN (ID INTEGER     NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                                "DATE_TIME VARCHAR(35),  YE_AR VARCHAR(10), MONTH VARCHAR(15), M_DATE VARCHAR(10), DAY VARCHAR(10), TIME VARCHAR(10), SENSOR$

                try{
                        PreparedStatement dp = connect.prepareStatement(toTableDrop);
                        dp.execute();
                }
                catch (Exception ep)
                {
                        System.out.println("Message: Creating a Table as table does not exist");
                }

                PreparedStatement generate = connect.prepareStatement(generateTable);
                generate.execute();
        }

        public void insertEachRow(Connection connect) throws SQLException, IOException
        {
                String row = "INSERT INTO PEDESTRIAN(DATE_TIME, YE_AR, MONTH, M_DATE, DAY, TIME, SENSOR_ID, SENSOR_NAME, HOURLY_COUNTS) VALUES (?,?,?,?,?,?,?,?,?)";

                final int size_of_batch = 20000;
                int batchCount = 0;
                connect.setAutoCommit(false);
                @SuppressWarnings({"depreciation","resource"})
                CSVReader read = new CSVReader(new FileReader(F_NAME),'\t');

                //CSVReaderBuilder read = new CSVReaderBuilder();
                String[] list;

                PreparedStatement addTo = connect.prepareStatement(row);
                //Skip the first row which would be the header of csv file
                list = read.readNext();
                while((list = read.readNext())!=null)
                {
                        System.out.println(list[0]);
                        String newList = list[0];
                        String[] arr = newList.split("[,]", 0);

                        //Starting from 1 as we don't need ID, it will be autogenerated
                        for(int i=1; i<10; i++)
                        {
                                addTo.setString(i, arr[i]);
                        }

                        addTo.addBatch();
                        //Once batch reached the sizeofbatch, we execute it
                        if(++batchCount % size_of_batch ==0)
                        {
                                addTo.executeBatch();
                        }
                }
                addTo.executeBatch();

                addTo.close();
                connect.commit();
                connect.close();
        }
*/
}
