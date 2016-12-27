package framework.databus;

public interface DataChannel {
	
	public String pop();
	
	public void push(String value);

}
