package telemarketing.model;

//ר�����ڷ�ҳ��Ϣ�ķ�װ��
public class Pager {

	private int totalCount=0; //������
	private int pageCounts=0; //�ܵ�ҳ��
	private int pageSize=0;  //��ǰҳ��ʾ������
	private int currentPageNum=1; //��ǰ��ҳ��
	private Object data;
	private String prePageString; //��һҳ��ѯ��ַ
	private String nextPageString; //��һҳ��ѯ��ַ
	private String firstPageString; //��ҳ
	private String endPageString;  //βҳ
	
	private String orderString1;
	private String orderString2;
	private String orderString3;
	private String orderString4;
	private String orderString5;
	private String orderString6;
	private String orderString7;
	private String orderString8;
	private String orderString9;
	
	
	public String getOrderString1() {
		return orderString1;
	}
	public void setOrderString1(String orderString1) {
		this.orderString1 = orderString1;
	}
	public String getOrderString2() {
		return orderString2;
	}
	public void setOrderString2(String orderString2) {
		this.orderString2 = orderString2;
	}
	public String getOrderString3() {
		return orderString3;
	}
	public void setOrderString3(String orderString3) {
		this.orderString3 = orderString3;
	}
	public String getOrderString4() {
		return orderString4;
	}
	public void setOrderString4(String orderString4) {
		this.orderString4 = orderString4;
	}
	public String getOrderString5() {
		return orderString5;
	}
	public void setOrderString5(String orderString5) {
		this.orderString5 = orderString5;
	}
	public String getOrderString6() {
		return orderString6;
	}
	public void setOrderString6(String orderString6) {
		this.orderString6 = orderString6;
	}
	public String getOrderString7() {
		return orderString7;
	}
	public void setOrderString7(String orderString7) {
		this.orderString7 = orderString7;
	}
	public String getOrderString8() {
		return orderString8;
	}
	public void setOrderString8(String orderString8) {
		this.orderString8 = orderString8;
	}
	public String getOrderString9() {
		return orderString9;
	}
	public void setOrderString9(String orderString9) {
		this.orderString9 = orderString9;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPageNum() {
		return currentPageNum;
	}
	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getPrePageString() {
		return prePageString;
	}
	public void setPrePageString(String prePageString) {
		this.prePageString = prePageString;
	}
	public String getNextPageString() {
		return nextPageString;
	}
	public void setNextPageString(String nextPageString) {
		this.nextPageString = nextPageString;
	}
	public int getPageCounts() {
		return pageCounts;
	}
	public void setPageCounts(int pageCounts) {
		this.pageCounts = pageCounts;
	}
	public String getFirstPageString() {
		return firstPageString;
	}
	public void setFirstPageString(String firstPageString) {
		this.firstPageString = firstPageString;
	}
	public String getEndPageString() {
		return endPageString;
	}
	public void setEndPageString(String endPageString) {
		this.endPageString = endPageString;
	}
}
