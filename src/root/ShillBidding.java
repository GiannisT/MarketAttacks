package root;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.auction.auctioneer.Auctioneer;
import org.cloudbus.cloudsim.auction.auctioneer.BidCalculation;

/*SPs can create fake user accounts to set fake Bids for their asks (offers) so the price of the resources escalate.  
 * In this occasion as the market is build on top of combinatorial double auction, shill bidding is complex and requires
 * a lot of shill users to submit high prices (similar to sybil attacks) in order for the market to depict a price P that clears the market (nash
 * equilibrium), which is higher than the required.
 * We assume that fake bidders pay for their resources to avoid detection. The profit comes from the legitimate
 * users that still have to price the price P, which is significantly higher. 
*/

public class ShillBidding {
	
	public static boolean enableAttack;
	public static boolean solution;
	
	//the number of shillbrokers along with the price submitted (calculated in BidderBrokerFactory4) by them are analogously affecting the market prices.
	
	public static int ShillBrokers=Math.round((Controller.databrokers*30)/100); //determining how many users will act malicious
	
//it sets the attack in effect and passes the number of total users hence to calculate a proportion of them that will act maliciously
	public ShillBidding(boolean enableAttack){
		this.enableAttack=false;
		this.enableAttack=enableAttack;
		
		if(this.enableAttack==true){
		   Log.printLine("*******************************************");
		   Log.printLine("Performing Shill Bidding Attack...");
		   Log.printLine("Introducing "+ShillBrokers+" shill brokers in simulation");
		   Log.printLine("*******************************************");
		   Log.printLine();
		}
	}
	
	//used by BidderBrokerFactory4 class, function createBroker(int numOfBrokers) to perform attack
	public static boolean getShillAttack(){ 
	     return	enableAttack;
	}
	
	//the solution will analyse auction records from each user to determine if there is a case of shilling.
	//each user will be given a value (out of 6) for shilling which will determine if a user is malicious
	
	public static void solveShillAttack(boolean solve){
		
		solution=solve;
		
	 if (enableAttack==true & solution==true){
	
		Log.printLine();
		Log.printLine("************Shill Bidding Attack Solution**************");
		int shillValue=0;
		 
		for(int i=0; i<BidderBrokerFactory4.record.size(); i++){
			shillValue=0;
			
			//if a user has no or bad feedback we increase the chance of being shill bidder
			if(BidderBrokerFactory4.record.get(i).getFeedback()<2){
				shillValue++;
			}
			
			//if a user has lost a lot of auctions and its account is new increase the chance of being shill bidder
			if(BidderBrokerFactory4.record.get(i).getLostAuctions()>3 & BidderBrokerFactory4.record.get(i).getTimeSinceRegistration()==2){
				shillValue++;
			}
			
			
			//if a user has very few feedback increase the chance of being shill bidder
			if(BidderBrokerFactory4.record.get(i).getNumOfFeedback()<5){
				shillValue++;
			}
			
			
			//if a user has submitted a lot of bids in a single auction increase the chance of being shill bidder
			if(BidderBrokerFactory4.record.get(i).getNumOfSubmittedBids()>4){
				shillValue++;
			}
			
			
			//if a user has overbidded users repeadedly in a single auction increase the chance of being shill bidder
			if(BidderBrokerFactory4.record.get(i).getTimesOverbid()>4){
				shillValue++;
			}
			
			
			//if a user has overbidded users repeadedly and its bids have a small deviation to the last bid price increase the chance of being shill bidder
			if(BidderBrokerFactory4.record.get(i).getNumOfSubmittedBids()>4 & BidderBrokerFactory4.record.get(i).getDeviationFromLastBid()<3){
				shillValue++;
			}
			
			if(shillValue>4){
				Log.printLine(BidderBrokerFactory4.record.get(i).getName()+" is a shill bidder as its shill value is: "+shillValue+"/6");
			    Log.printLine("Account Deleted");
			}
			
		}
		Log.printLine("****************************************************************");
	 }	
	}
	
	/*
	public static void Test(){
		double total=0;
		for (BidCalculation obj: Auctioneer.matched.keySet()){
    	    BidCalculation WinnerBroker = obj;
			BidCalculation WinnerDatacenter = Auctioneer.matched.get(WinnerBroker); 
			total+=WinnerDatacenter.getCalculatedPrice();
		}
		Log.printLine(total);
	}
	*/
}
