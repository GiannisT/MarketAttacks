package root;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.auction.auctioneer.Auctioneer;

import java.security.Security;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class UnpaidResources {
	
	static boolean attack=false;
	static boolean solution=false;
	static int i;
	static int NumofUnpaidBids=Controller.databrokers*35/100; //indicates the number (percentage) of bids that will remain unpaid
	int unpaidStatus=0;
	
	public UnpaidResources(){}
	
	public UnpaidResources(boolean attack){
		this.attack=attack;
		
		if (this.attack==true){
		Log.printLine("**********Unpaid-Resources Attack************");
		Log.printLine(NumofUnpaidBids+" bidders will not paid their won resources!!!!");
		Log.printLine("**********************************************");
		i=1;
		}
	}
	
	public boolean getAttack(){ //used by BidderBrokerFactory4
		
		if (i<=NumofUnpaidBids){ //this indicates that number of bids that will be intercepted and blocked
			i++;
			return attack;
		}
		
		return false;
	}
	
	public boolean getSolution(){
		return this.solution;
	}
	
		      
	//the bidding behaviour of each bidder is recorded and then these records are analyzed
		public void solveUnpaidResourceAttack(boolean solution) throws Exception{
			
			this.solution=solution;
			
		  if(this.solution==true & this.attack==true){	
			  
			  Log.printLine();
			  Log.printLine("**********Deploying Unpaid-Resources solution************");
		
			  for(int i=0; i<BidderBrokerFactory4.unpaidRecord.size(); i++){
					unpaidStatus=0;
					
					//if a user has no or bad feedback we increase the chance of being an "unpaid resource attacker"
					if(BidderBrokerFactory4.unpaidRecord.get(i).getFeedback()<2){
						unpaidStatus++;
					}
					
					//if a user has lost a lot of auctions and its account is new
					if(BidderBrokerFactory4.unpaidRecord.get(i).getLostAuctions()>3 & BidderBrokerFactory4.record.get(i).getTimeSinceRegistration()==2){
						unpaidStatus++;
					}
					
					
					//if a user has very few feedback increase the chance of being attacker
					if(BidderBrokerFactory4.unpaidRecord.get(i).getNumOfFeedback()<5){
						unpaidStatus++;
					}
					
					//if a bidders account is rearly new and has not paid multiple times for resources
					if(BidderBrokerFactory4.unpaidRecord.get(i).getTimesNotPaidForResources() > 4 & BidderBrokerFactory4.unpaidRecord.get(i).getTimeSinceRegistration()==2){
						unpaidStatus++;
					}
					
					//if a bidder has outbided the second highest bidder by a lot, this illustrates suspicious behavior 
					if (BidderBrokerFactory4.unpaidRecord.get(i).getDeviationFromLastBid() > 4){
						unpaidStatus++;
					}
					
					if(unpaidStatus>2){
						Log.printLine(BidderBrokerFactory4.unpaidRecord.get(i).getName()+" willingly submitted bids, won resources and did not purchase the resource. Its detection value is: "+unpaidStatus+"/5");
					}
					
				}
				Log.printLine("****************************************************************");
			
		  }	
	   
	   }
 }	


	
