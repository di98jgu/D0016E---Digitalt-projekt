/*
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 */
package ssc;
/**
 * Class SSCException
 * 
 * A runtime exception wrapper for all well know exceptions. Main 
 * source of exceptions is the restfulClient. All {@link SenseSmartCity} 
 * methods can throw this.
 * 
 * This exception wrapper may not follow the narrow road of Java 
 * wisdom, each exception in its own class. But this helps to keep 
 * things neat and easy to maintain. I have yet see why static inner
 * exception classes is such a bad idea.
 * 
 * All exceptions is unchecked.
 * 
 * @author Jim Gunnarsson
 */

public class SSCException extends RuntimeException {
   
   private static final long serialVersionUID = 2114723132L;
   
   /**
    * 
	 * Wrap an exception as a SSCException.
	 */
   public SSCException(Exception e) {
      super(e);
      
   }
   
   /**
	 * Wrap an exception as a SSCException.
	 */
   public SSCException(String msg) {
      super(msg);
      
   }
   
   /**
	 * Something has gone very wrong...
	 */
	public static class Mystery extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public Mystery(String msg) {
			super(msg);
		}
      
      public Mystery(Exception e) {
			super(e);
		}
	}
   
   /**
    * Class ConnectionFailed
    * 
	 * Restful client failed to establish connection with
    * server.
	 */
	public static class ConnectionFailed extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public ConnectionFailed(String msg) {
			super(msg);
		}
      
      public ConnectionFailed(Exception e) {
			super(e);
		}
	}
   
   /**
    * Class ClientError
    * 
	 * Restful client, in case of response code 4xx.
    * 
    * FIXME: Divide this class for different 4xx codes.
	 */
	public static class ClientError extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public ClientError(String msg) {
			super(msg);
		}
	}
   
   /**
    * Class ServerError
    * 
	 * Restful client, in case of response code 5xx.
	 */
	public static class ServerError extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public ServerError(String msg) {
			super(msg);
		}
	}
   
   /**
    * Class MalformedData
    * 
	 * Malformed data exception is throw then data is in wrong form.
    * 
    * Normal get operations, fetching data from server, should not
    * generate this kind of exception. It would indicate changes in
    * the API or, more likely, errors in this library.
	 */
	public static class MalformedData extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public MalformedData(String msg) {
			super(msg);
		}
      
      public MalformedData(Exception e) {
			super(e);
		}
	}
   
   /**
    * Class NoUserCredentials
    * 
    * In order to get data from server user credentials is mandatory. 
    * Thus lack of such, user name or password is null or missing, is 
    * an exception.
    */
   public static class NoUserCredentials extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public NoUserCredentials(String msg) {
			super(msg);
		}
   }
   
}
