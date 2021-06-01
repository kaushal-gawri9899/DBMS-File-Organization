import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DerbyQuery {

	private final String F_NAME = "Pedestrian_Counting_System_-_Monthly__counts_per_hour_.csv";
	private final String D_REQ = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String D_URL = "jdbc:derby:assignOne;create=true";


	/*
	* The main method initializes 4 different queries
	* 2 queries are based on the secondary index created for different tables in Derby
	* The rest 2 queries do not have anything to do with secondary index
	* Make a connection to derby database and run the queries
	* Compute the time taken for report and analysis
	* */
	public static void main(String[] args)
	{
		DerbyQuery derby = new DerbyQuery();

		try {
			Class.forName(derby.D_REQ);
			Connection connect = DriverManager.getConnection(derby.D_URL);

			Statement stmt = connect.createStatement();

			String queryOne = "SELECT PEDESTRIANCOUNT.ID, PEDESTRIANSENSOR.SENSOR_ID, PEDESTRIANSENSOR.SENSOR_NAME, PEDESTRIANCOUNT.HOURLY_COUNT FROM PEDESTRIANCOUNT INNER JOIN PEDESTRIANSENSOR ON PEDESTRIANCOUNT.ID = PEDESTRIANSENSOR.ID WHERE PEDESTRIANCOUNT.HOURLY_COUNT LIKE '100' FETCH FIRST 30 ROWS ONLY";

			String queryTwo = "SELECT PEDESTRIANTIME.M_DATE, PEDESTRIANTIME.MONTH, PEDESTRIANTIME.DATE_TIME, PEDESTRIANSENSOR.SENSOR_NAME , PEDESTRIANSENSOR.SENSOR_ID FROM PEDESTRIANSENSOR INNER JOIN PEDESTRIANTIME ON PEDESTRIANSENSOR.ID = PEDESTRIANTIME.ID WHERE PEDESTRIANTIME.YE_AR like '2010' AND PEDESTRIANSENSOR.SENSOR_ID like '10'";

			String queryThree = "SELECT PEDESTRIANTIME.ID, PEDESTRIANTIME.YE_AR, PEDESTRIANSENSOR.SENSOR_ID, PEDESTRIANSENSOR.SENSOR_NAME, PEDESTRIANTIME.TIME, PEDESTRIANCOUNT.HOURLY_COUNT FROM PEDESTRIANTIME INNER JOIN PEDESTRIANSENSOR ON PEDESTRIANTIME.ID = PEDESTRIANSENSOR.ID INNER JOIN PEDESTRIANCOUNT ON PEDESTRIANCOUNT.ID = PEDESTRIANSENSOR.ID WHERE PEDESTRIANTIME.DAY LIKE 'TUESDAY' FETCH FIRST 30 ROWS ONLY";

			String queryFour = "SELECT PEDESTRIANTIME.ID, PEDESTRIANTIME.DATE_TIME, PEDESTRIANCOUNT.HOURLY_COUNT FROM PEDESTRIANTIME INNER JOIN PEDESTRIANCOUNT ON PEDESTRIANTIME.ID = PEDESTRIANCOUNT.ID INNER JOIN PEDESTRIANSENSOR ON PEDESTRIANSENSOR.ID = PEDESTRIANCOUNT.ID WHERE PEDESTRIANSENSOR.SENSOR_NAME LIKE 'Alfred Place'";

			long timeOneStart = System.currentTimeMillis();
			stmt.execute(queryOne);
			long timeOneEnd = System.currentTimeMillis();


			System.out.println("Time Taken By Query 1:" + (timeOneEnd-timeOneStart) + " ms");

			long timeTwoStart = System.currentTimeMillis();
			stmt.execute(queryTwo);
			long timeTwoEnd = System.currentTimeMillis();

			System.out.println("Time Taken By Query 2:" + (timeTwoEnd-timeTwoStart) + " ms");

			long timeThreeStart = System.currentTimeMillis();
			stmt.execute(queryThree);
			long timeThreeEnd = System.currentTimeMillis();

			System.out.println("Time Taken By Query 3:" + (timeThreeEnd-timeThreeStart) + " ms");


			long timeFourStart = System.currentTimeMillis();
			stmt.execute(queryFour);
			long timeFourEnd = System.currentTimeMillis();

			System.out.println("Time Taken By Query 4:" + (timeFourEnd-timeFourStart) + " ms");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
