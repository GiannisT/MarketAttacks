package root;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.auction.bid.DatacenterBrokerBid;
import org.cloudbus.cloudsim.auction.bidder.BidderDatacenterBroker;
import org.cloudbus.cloudsim.auction.vm.ClonedVm;

public class BidderBrokerFactory4 {

	static final long FIXED_DURATION = 10;
	static SecureRandom ran;
	static List<BidderDatacenterBroker> broke;
	static int counter=1;
	static List<BidRecord> record=new ArrayList<BidRecord>();
	static List<BidRecord> unpaidRecord=new ArrayList<BidRecord>();

	public static List<BidderDatacenterBroker> createBroker(int numOfBrokers) {
		BidderDatacenterBroker broker = null;
		UnpaidResources unpaid=new UnpaidResources();
		broke = new ArrayList<BidderDatacenterBroker>();
		ran = new SecureRandom();
		DatacenterBrokerBid brokerBid=null;
		int ShillBrokers=0;
		BidRecord ShillUserRecord=null;
		BidRecord UnpaidRecord=null;
		
		if(ShillBidding.ShillBrokers<=1){ //check in case that brokers are few and the existing percentage illustrates zero or less shill users
			ShillBrokers=1;
		}else{
			ShillBrokers=ShillBidding.ShillBrokers;
		}
	
		//**********UnpaidAttack**********************
		for (int i = 1; i <= numOfBrokers; i++) {
				
			broker = null;
			try {
				broker = new BidderDatacenterBroker("Broker_" + i);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
				
			
			//*****************************************************SHILL BIDDING & Unpaid ATTACK***********************************************************
			ShillUserRecord=new BidRecord();
			UnpaidRecord=new BidRecord();
			boolean notpaid=unpaid.getAttack();
			
			if(notpaid==true){ //records for unpaid attack
				brokerBid = new DatacenterBrokerBid(broker.getId(), ran.nextDouble() + 10.0308, FIXED_DURATION);
				UnpaidRecord.setFeedback(ran.nextInt(2));
				UnpaidRecord.setLostAuctions(ran.nextInt(10)+5);
				UnpaidRecord.setNumOfFeedback(ran.nextInt(2));
				UnpaidRecord.setTimeSinceRegistration(2); //1=longtime, 2=shortTime
				UnpaidRecord.setName(broker.getName());
				UnpaidRecord.setTimesNotPaidForResources(ran.nextInt(7)+6);
				UnpaidRecord.setDeviationFromLastBid(ran.nextInt(10)+6);
				unpaidRecord.add(UnpaidRecord); //record the bidding habits of users to detect unpaid attack
				
			}else{ //create legitimate bids
				
				brokerBid = new DatacenterBrokerBid(broker.getId(), ran.nextDouble() + 0.908, FIXED_DURATION);
				UnpaidRecord.setFeedback(ran.nextInt(7));
				UnpaidRecord.setLostAuctions(ran.nextInt(1)+1);
				UnpaidRecord.setNumOfFeedback(ran.nextInt(10));
				UnpaidRecord.setTimeSinceRegistration(1); //1=longtime, 2=shortTime
				UnpaidRecord.setName(broker.getName());
				UnpaidRecord.setTimesNotPaidForResources(ran.nextInt(2)+1);
				UnpaidRecord.setDeviationFromLastBid(ran.nextInt(2)+3);
				unpaidRecord.add(UnpaidRecord); //record the bidding habits of users to detect unpaid attack
			}
			
			 if (ShillBidding.getShillAttack()==true & counter<=ShillBrokers){ 
			   counter++;
			   brokerBid = new DatacenterBrokerBid(broker.getId(), ran.nextDouble() + 2, FIXED_DURATION);  //submit high price bids in order for the clear market price to increase
			   ShillUserRecord.setDeviationFromLastBid(ran.nextInt(2)+1); //usually shill users overbid legit users only by a very small difference so they do not discourage legit users to submit more bids 
			   ShillUserRecord.setFeedback(ran.nextInt(2));
			   ShillUserRecord.setLostAuctions(ran.nextInt(10)+4);
			   ShillUserRecord.setNumOfFeedback(ran.nextInt(3));
			   ShillUserRecord.setNumOfSubmittedBids(ran.nextInt(9)+5);
			   ShillUserRecord.setTimeSinceRegistration(2); //1=longtime, 2=shortTime
			   ShillUserRecord.setTimesOverbid(ran.nextInt(5)+2); 
			   ShillUserRecord.setName(broker.getName());
			   record.add(ShillUserRecord); //record the bidding habbits of users to detect shilling
			   
			}else{//create legitimate broker bids
		    	
				brokerBid = new DatacenterBrokerBid(broker.getId(), ran.nextDouble() + 0.0308, FIXED_DURATION);
		    	ShillUserRecord.setDeviationFromLastBid(ran.nextInt(5)+1);
				ShillUserRecord.setFeedback(ran.nextInt(2)+1);
				ShillUserRecord.setLostAuctions(ran.nextInt(3)+1);
				ShillUserRecord.setNumOfFeedback(ran.nextInt(15)+1);
				ShillUserRecord.setNumOfSubmittedBids(ran.nextInt(3)+1);
				ShillUserRecord.setTimeSinceRegistration(1); //1=longtime, 2=shortTime
				ShillUserRecord.setTimesOverbid(ran.nextInt(3)+1); 
				ShillUserRecord.setName(broker.getName());
				record.add(ShillUserRecord); //record the bidding habbits of users to detect shilling
		    }
				
			//*****************************************************SHILL BIDDING ATTACK***********************************************************
			
			
			
			int userId = broker.getId();
			String vmm = "Xen"; // VMM name

			for (int k = 0; k < ran.nextInt(2) + 1; k++) {
				brokerBid.addVM(new Vm(ClonedVm.getID(), userId,
						ran.nextInt(500) + 150, 
						ran.nextInt(2) + 1,
						ran.nextInt(512) + 128,
						ran.nextInt(1000) + 400,
						ran.nextInt(10000) + 1000, vmm,
						new CloudletSchedulerTimeShared()), 
						ran.nextInt(3) + 1);
			}


			if(notpaid==true && unpaid.getSolution()==false){ //records for unpaid attack
				Log.printLine("Resources are not paid. Cause: Denial of Payment!!");
			}else{
			  broker.submitBid(brokerBid);
			  broker.submitCloudletList(createCloudlet(broker.getId(),ran.nextInt(5) + 1)); // creating and submitting cloudlet
			  broke.add(broker);
			}
			
			
	}
		return broke;
 }

	/**
	 * Cloudlet unique ID
	 */
	private static int CLOUDLET_ID = 0;

	private static List<Cloudlet> createCloudlet(int userId, int cloudlets) {
		// Creates a container to store Cloudlets
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();
		UtilizationModel utilizationModel = new UtilizationModelFull();
		Cloudlet[] cloudlet = new Cloudlet[cloudlets];

		for (int i = 0; i < cloudlets; i++) {

			// cloudlet parameters
			long length = ran.nextInt(8000) + 2000;
			long fileSize = ran.nextInt(500) + 200;
			long outputSize = ran.nextInt(400) + 200;
			int pesNumber = ran.nextInt(2) + 1;

			cloudlet[i] = new Cloudlet(CLOUDLET_ID++, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			cloudlet[i].setUserId(userId);
			list.add(cloudlet[i]);
		}

		return list;
	}

}