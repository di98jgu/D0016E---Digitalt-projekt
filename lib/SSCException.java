/**
 * A runtime exception for all well know exceptions. Main source
 * of exceptions is the restfulClient. All {@link SenseSmartCity} 
 * methods can throw this. 
 * 
 * The level of details in exception handling could be improved but
 * this is the first version. 
 * 
 * All exceptions is unchecked.
 * 
 * @author Jim Gunnarsson
 */

class SSCException extends RuntimeException {
   
   private static final long serialVersionUID = 2114723132L;
   
   /**
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
	 * Restful client, response code 4xx.
	 */
	public static class ClientError extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public ClientError(String msg) {
			super(msg);
		}
	}
   
   /**
	 * Restful client, response code 5xx.
	 */
	public static class ServerError extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public ServerError(String msg) {
			super(msg);
		}
	}
   
   /**
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
    * To get data from server user credentials is mandatory. Thus
    * lack of such, user name or password is null, is a exception.
    */
   public static class NoUserCredentials extends SSCException {
      
      private static final long serialVersionUID = 2114723132L;
      
		public NoUserCredentials(String msg) {
			super(msg);
		}
   }
   
}
