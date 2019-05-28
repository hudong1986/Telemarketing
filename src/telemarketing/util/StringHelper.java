package telemarketing.util;

 
public class StringHelper {

	public static String trim(String str,char remove){
		if(str.length()==1){
			str = str.replace(String.valueOf(remove), "");
			return str;
		}
		
		str = trimLeft(str, remove);
		str = trimRight(str, remove);
		return str;
	}
	
	public static String trimLeft(String str,char remove){
		if(str.length()==1){
			str = str.replace(String.valueOf(remove), "");
			return str;
		}
		
		while(true){
			if(str.indexOf(remove)==0){
				str = str.substring(1);
			}
			else{
				break;
			}
		}
		
		 
		return str;
	}
	
	public static String trimRight(String str,char remove){
		if(str.length()==1){
			str = str.replace(String.valueOf(remove), "");
			return str;
		}
		
		while(true){
			if(str.lastIndexOf(remove)==(str.length()-1)){
				str = str.substring(0, str.length()-1);
			}
			else{
				break;
			}
		}
		
		 
		return str;
	}
	
	
	public static String turn(String str) {  

	    while (str.indexOf("\n") != -1) {  
	        str = str.substring(0, str.indexOf("\n")) + "<br>"  
	                + str.substring(str.indexOf("\n") + 1);  
	    }  
	    while (str.indexOf(" ") != -1) {  
	        str = str.substring(0, str.indexOf(" ")) + "&nbsp"  
	                + str.substring(str.indexOf(" ") + 1);  
	    }  
	    return str;  
	}  
}
