package root;

/*
 * This class will keep semi-random generated records for each broker user, from which the recorded information will be analyzed to 
 * determine if a user is malicious or not
 */
public class BidRecord {
	
	int FeedbackNumber=0;
	int FeedbackValue;
	int numOfSubmittedbids=0;
	int lostAuctions=0;
	int timeSinceReg=0;
	int timesOverBid=0;
	int notPaidResources=0;
	double deviatedPrice=0.0;
	String name="";
	
	//sets the name of the user
	public void setName(String userName){
		name=userName;
	}
	
	
	//how many feedback each user has
	public void setNumOfFeedback(int FeedbackNum){
		FeedbackNumber=FeedbackNum;
	}
	
	//describes if a user has feedback from SPs, 1=neutral, 0=bad, 2=good  
	public void setFeedback (int feed){
		this.FeedbackValue=feed;
	}
	
	//number of bids submitted to a certain SP
	public void setNumOfSubmittedBids(int submittedbids){
		numOfSubmittedbids=submittedbids;
	}
	
	//describes the lost auctions of a certain provider
	public void setLostAuctions(int lost){
		lostAuctions=lost;
	}
	
	//describes the time since when a user was registered in the market
    public void setTimeSinceRegistration(int time){
    	timeSinceReg=time;
    	
    }
    
    //times overbid another user in a single auction 
    public void setTimesOverbid(int times){
    	timesOverBid=times;
    	
    }
    
    public void setTimesNotPaidForResources(int times){
    	notPaidResources=times;
    }
    
    //illustrates the difference between the bid the outbidded user and the current user
    public void setDeviationFromLastBid(double deviationPrice){
         deviatedPrice=deviationPrice;	
    }
    
    //------------------------------------getters-------------------------------------------------------
    
   	public String getName(){
  		return name;
  	}
  	
    
  	public int getNumOfFeedback(){
  		return FeedbackNumber;
  	}
  	
  	  
  	public int getFeedback(){
  		return this.FeedbackValue;
  	}
  	
  	
  	public int getNumOfSubmittedBids(){
  		return numOfSubmittedbids;
  	}
  	
  	
  	public int getLostAuctions(){
  		return lostAuctions;
  	}
  	
  	
    public int getTimeSinceRegistration(){
      	return timeSinceReg;
    }
      
      
    public int getTimesOverbid(){
      	return timesOverBid;  	
    }
      
    
    public double getDeviationFromLastBid(){
        return deviatedPrice;	
    }
    
    public int getTimesNotPaidForResources(){
    	return notPaidResources;
    }

}
