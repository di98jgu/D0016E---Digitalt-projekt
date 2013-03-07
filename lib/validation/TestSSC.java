package validation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import ssc.*;

public class TestSSC {
   
   private SenseSmartCity ssc_good;
   private SenseSmartCity ssc_bad;
   
   private String user = "";
   private String pwd = "";
   
   private String sensor_good_a = "SKE-824224";
   private String sensor_good_b = "SKE-472478";
   private String sensor_bad = "THIS_IS_NOT_A_SENSOR";
   private String sensor_domain = "SKE-9993";
   
   private String period_good = "last-year";
   private String period_empty = "";
   private String period_bad = "Not a valid time period";

	@Before
	public void setUp() throws Exception {
      
		this.ssc_good = new SenseSmartCity(user, pwd);
      
	}

	@Test
	public void sscExist() {
      
		assertTrue("Sense Smart City don't exist", ssc_good != null);
      
	}
   
   @Test (expected = SSCException.NoUserCredentials.class)
	public void noUserCred() {
      
      ssc_bad = new SenseSmartCity(user, null);
      
	}
   
   @Test 
	public void sensorListEmpty() {
      
      List<Sensor> sensors =
         ssc_good.requestSensorList(new ArrayList<String>(), false);

      assertFalse("Returned sensor list is empty", sensors.isEmpty());
      
	}
   
   @Test 
	public void sensorListPublicEmpty() {
      
      List<Sensor> sensors =
         ssc_good.requestSensorList(new ArrayList<String>(), true);
      
      assertFalse("Returned public sensor list is empty", sensors.isEmpty());
      
	}

	@Test
	public void sensorListGood() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      
      List<Sensor> sensors =
         ssc_good.requestSensorList(args, false);
         
      int size = sensors.size();
      
		assertTrue("Number of sensors should be 2 but is " + size, (size == 2));
      
	}

	@Test
	public void sensorListBad() {
      
      try {
         
         List<String> args = new ArrayList<String>();
         
         args.add(sensor_bad);
         
         List<Sensor> sensors =
            ssc_good.requestSensorList(args, false);
            
         fail("Should have thrown SSCException.ClientError");
         
      } catch (SSCException.ClientError e) {
         
         // Good !!
         
      } catch (SSCException e) {
         
         fail(e.toString());
         
      }
      
	}

	@Test (expected = SSCException.ClientError.class)
	public void sensorListUgly() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      args.add(sensor_bad);
      args.add(sensor_domain);
      
      List<Sensor> sensors =
         ssc_good.requestSensorList(args, false);
      
	}
	
	@Test
	public void sensorListNull() {
      
      // Okej - We accept null values
      
      List<Sensor> sensors =
         ssc_good.requestSensorList(null, false);
         
      assertFalse("Returned public sensor list is empty", sensors.isEmpty());
      
	}
   
// ---- SnowPressure -------------------------------------------------

   @Test (expected = java.lang.NullPointerException.class)
	public void snowPressNullSensor() {
      
      List<String> q_args = new ArrayList<String>();
      
      Map<Sensor, List<SnowPressure>> snowdata = 
         ssc_good.requestSnowPressure(null, q_args, period_good);
      
	}
   
   @Test 
	public void snowPressNullquery() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      
      List<Sensor> s_args =
         ssc_good.requestSensorList(args, false);
      
      Map<Sensor, List<SnowPressure>> snowdata = 
         ssc_good.requestSnowPressure(s_args, null, period_good);
         
      assertFalse("Returned sensor list is empty", snowdata.isEmpty());
      
	}
   
   @Test 
	public void snowPressNullPeriod() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      
      List<Sensor> s_args =
         ssc_good.requestSensorList(args, false);
      
      List<String> q_args = new ArrayList<String>();
      
      Map<Sensor, List<SnowPressure>> snowdata = 
         ssc_good.requestSnowPressure(s_args, q_args, null);
      
      assertFalse("Returned sensor list is empty", snowdata.isEmpty());
      
	}
   
   @Test
   public void snowPressGood() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      
      List<Sensor> s_args =
         ssc_good.requestSensorList(args, false);
      
      List<String> q_args = new ArrayList<String>();
      
      Map<Sensor, List<SnowPressure>> snowdata = 
         ssc_good.requestSnowPressure(s_args, q_args, period_good);
         
      int size = snowdata.size();
      
		assertTrue("Number of readings should be 2 but is " + size, (size == 2));
      
      
   }
   
   @Test
   public void snowPressNoPeriod() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      
      List<Sensor> s_args =
         ssc_good.requestSensorList(args, false);
      
      List<String> q_args = new ArrayList<String>();
      
      Map<Sensor, List<SnowPressure>> snowdata = 
         ssc_good.requestSnowPressure(s_args, q_args, period_empty);
      
      assertFalse("Returned snowdata is empty", snowdata.isEmpty());
      
   }
   
   @Test (expected = SSCException.MalformedData.class)
   public void snowPressBadPeriod() {
      
      List<String> args = new ArrayList<String>();
      
      args.add(sensor_good_a);
      args.add(sensor_good_b);
      
      List<Sensor> s_args =
         ssc_good.requestSensorList(args, false);
      
      List<String> q_args = new ArrayList<String>();
      
      Map<Sensor, List<SnowPressure>> snowdata = 
         ssc_good.requestSnowPressure(s_args, q_args, period_bad);
         
      fail("Expecting MalformedData");
      
   }
   
   @Test 
   public void snowPressUglySensor() {
    
      try  {
         
         List<String> args = new ArrayList<String>();
         
         args.add(sensor_good_a);
         args.add(sensor_good_b);
         args.add(sensor_bad);
         args.add(sensor_domain);
         
         List<Sensor> s_args =
            ssc_good.requestSensorList(args, false);
         
         List<String> q_args = new ArrayList<String>();
         
         Map<Sensor, List<SnowPressure>> snowdata = 
            ssc_good.requestSnowPressure(s_args, q_args, period_good);
            
         fail("Expecting ClientError");
            
      } catch (SSCException.ClientError e) {
         
      } 
         
   }
   
}
