package telemarketing.model;

public class AjaxReturn {

	private int code=0;
	private String retMsg;
	private int retInt1;
	private int retInt2;
	private String retStr1;
	private String retStr2;
	private Object object1;
	private Object object2;
	 
	public AjaxReturn(){
		
		
	}
	public AjaxReturn(int code,String msg){
		this.code = code;
		this.retMsg = msg;
	}
	
	public AjaxReturn(int code,String msg,int i1,int i2,String st1,String str2,Object ob1,Object ob2){
		this.code = code;
		this.retMsg = msg;
		this.retInt1=i1;
		this.retInt2=i2;
		this.retStr1 = st1;
		this.retStr2 = str2;
		this.object1=ob1;
		this.object2 = ob2;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Object getObject1() {
		return object1;
	}

	public void setObject1(Object object1) {
		this.object1 = object1;
	}

	public Object getObject2() {
		return object2;
	}

	public void setObject2(Object object2) {
		this.object2 = object2;
	}
}
