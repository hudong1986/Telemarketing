package telemarketing.exception;

public class CustomException extends Exception {
	//“Ï≥£–≈œ¢  
    private String message;  
      
    public CustomException(String message){  
        super(message);  
        this.message=message;  
    }  
  
  
    public String getMessage() {  
        return message;  
    }  
  
  
    public void setMessage(String message) {  
        this.message = message;  
    }  


}
