import java.util.concurrent.ConcurrentLinkedQueue;


public class Reporter {

	ConcurrentLinkedQueue<Message> messageList = new ConcurrentLinkedQueue<Message>();
	
	
	public void report(String message, int percent){
		Message m = new Message(message,percent);
		messageList.add(m);
	}

	public Message getFirst(){
		return messageList.remove();
	}
	public boolean hasNewMessage(){
		return messageList.size() != 0;
	}
	
}
