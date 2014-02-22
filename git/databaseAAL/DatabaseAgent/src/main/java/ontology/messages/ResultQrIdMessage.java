package ontology.messages;


import ontology.Message;
import ontology.MessageType;

/**
 *
 * @author Administrator
 */
public class ResultQrIdMessage extends Message{

	int id;

    public int getId(){
    	return id;
    }

    public ResultQrIdMessage(String senderID, String receiverID, int UserId){
        super(senderID, receiverID, MessageType.RESULT_QR_ID);
        this.id = UserId;
    }
}