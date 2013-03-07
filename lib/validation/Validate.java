package validation;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.*;
import org.junit.runner.Description;

public class Validate {
   
	public static void main(String[] args) {
      
      JUnitCore core = new JUnitCore();
      
      core.addListener(new RunListener() {
         String line = "";
         String class_name = "";
         public void testStarted(Description d) {
            if (class_name.equals(d.getClassName())) {
               line = line.concat("Try: " + d.getDisplayName());
            }
            else {
               class_name = d.getClassName();
               line = line.concat("\nRun: " + d.getClassName()
                  + "\nTry: " + d.getDisplayName());
            }
         }
         public void testFinished(Description d) {
            System.out.println(line);
            line = "";
         }
         public void testFailure(Failure f) {
            line = line.concat(" - Faild!");
         }
      });
      
      System.out.println("Run tests...");
      
      Result result = core.run(TestSSC.class);
      
      if (result.wasSuccessful()) {
         System.out.println("\nAll tests - OK!");
      }
      else {
         int i = 1;
         System.out.println("\nResult of failed test(s):");
         for (Failure failure : result.getFailures()) {
            System.out.println(
               "(" + i + ") " + failure.toString() + "\n");
            i++;
         }
      }
      
	}
   
}
