package ontology.messages;


import java.util.LinkedList;

import ontology.Message;
import ontology.MessageType;
import ontology.TransportFrame;

/**
 *
 * @author Administrator
 */
public class SaveMessage extends Message{

	LinkedList<TransportFrame> trainingDataToSave = new LinkedList<TransportFrame>();
	int id;
    String qrString;
            
    public LinkedList<TransportFrame> getTrainingData() {
		return trainingDataToSave;
    }
    public String getQrString(){
        return qrString;
    }
    public int getId(){
    	return id;
    }

    public SaveMessage(String senderID, String receiverID, int UserId, LinkedList<TransportFrame> trainingData, String qrstring){
        super(senderID, receiverID, MessageType.SAVE_IN_DATA_BASE);
        this.trainingDataToSave = trainingData;
        this.id = UserId;
        this.qrString = qrstring;
    }
}