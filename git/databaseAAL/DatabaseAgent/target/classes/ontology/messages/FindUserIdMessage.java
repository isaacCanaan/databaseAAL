package ontology.messages;


import ontology.Message;
import ontology.MessageType;

/**
 *
 * @author Administrator
 */
public class FindUserIdMessage extends Message{

    String qrString;

    public String getQrString(){
        return qrString;
    }
    
    public FindUserIdMessage(String senderID, String receiverID, String qrstring){
        super(senderID, receiverID, MessageType.FIND_USER_ID);
        this.qrString = qrstring;
    }
}