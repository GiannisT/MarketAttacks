package root;
import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.auction.bidder.BidderDatacenter;


public class DamageReputation {
	
  boolean badFeedback=false;
  boolean solution=false;
  int BadFeedbackUserId;
  String AccusedSP;
  
	
	public DamageReputation(boolean attack){
						
		if (attack==true){ //find which sp servs what users, and then the user to give bad reputation to that SP
			Iterator<Entry<String, ArrayList>> i = BidderDatacenter.record.entrySet().iterator(); 
			ArrayList <Integer> list=new ArrayList();
			
			while(i.hasNext()){
			    String key = i.next().getKey();
			    list=BidderDatacenter.record.get(key);
			    //Log.printLine(key+", "+list);
			    
			    if((! key.equals("") & ! list.isEmpty())){
			    	AccusedSP=key; // the SP that will be accused of bad service 
			    	BadFeedbackUserId=list.get(0); //the user that will submit the bad feedback to the SP
			    	//Log.printLine(AccusedSP+", "+BadFeedbackUserId);
			    	Log.printLine();
			    	Log.printLine("**********Damage Reputation Attack************");
			    	Log.printLine("Bad Feedback was submitted by BrokerID:"+BadFeedbackUserId+" for Service Provider:"+AccusedSP+" !!!!!");
			    	badFeedback=true;
			    	break;		        
			     }    
			}	
		}
	}
	
	//this function aims to solve damage reputation attack
	public void SolveReputationAttack(boolean solve){
	solution=solve;
	
	 if(badFeedback==true && solution==true){
		 
		boolean condition1=false;
		boolean condition2=false;
		boolean condition3=false;
		int indexOfUser=0;
		
		if(badFeedback==true){
			
			badFeedback=false;
			Log.printLine();
			Log.printLine("**********Damage Reputation Solution************");
			Log.printLine("Analyzing the validity of the submitted Feedback:");
			
			for(int i=0; i<BidderBrokerFactory4.broke.size(); i++){
				
			  if (BidderBrokerFactory4.broke.get(i).getId() == BadFeedbackUserId & //find the recorded data for the user submitted the bad feedback
				 BidderBrokerFactory4.broke.get(i).getCloudletSubmittedList().containsAll(BidderBrokerFactory4.broke.get(i).getCloudletReceivedList())){ //check if all cloudlets send by the user were received by the SP
				  Log.printLine("Submitted Cloudlets received by SP: Sucess");
				  indexOfUser=i;	  
				  condition1=true;
				  break;
			  }
		   }
		}
	
	  //check the status of each cloudlet, if all cloudlets were satisfied
	 if (condition1==true){
		int counter=0;
		
		for (int j=0; j<BidderBrokerFactory4.broke.get(indexOfUser).getCloudletReceivedList().size(); j++){
				     
			if(BidderBrokerFactory4.broke.get(indexOfUser).getCloudletReceivedList().get(j).getCloudletStatusString().equals("Success")
			& BidderBrokerFactory4.broke.get(indexOfUser).getCloudletReceivedList().get(j).isFinished()==true){
			Log.printLine("Completion status of CloudletID:"+BidderBrokerFactory4.broke.get(indexOfUser).getCloudletReceivedList().get(j).getCloudletId()+ "--> Finished");
			counter++;
		   }
	    }
		 
		if (counter==BidderBrokerFactory4.broke.get(indexOfUser).getCloudletReceivedList().size()){
		    condition2=true;
		} 
   }
	 
	
	 int counter2=0;
	 
   if (condition2==true){
		
	 for(int k=0; k<BidderBrokerFactory4.broke.get(indexOfUser).getVmList().size(); k++){
			    		 
	   	long reqBW=BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getCurrentRequestedBw();
	   	long AllocBW=BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getBw();
	   	java.util.List<Double> reqMIPS=BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getCurrentRequestedMips();
	   	double AllocMIPS=BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getMips();
	   	int reqRAM=BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getCurrentRequestedRam();
	   	int AllocRAM=BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getCurrentRequestedRam();
				    	    
		if (reqBW<=AllocBW & reqMIPS.contains(AllocMIPS) & reqRAM==AllocRAM){
			Log.printLine("Requested Bandwidth for VM:"+BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getId()+"--> Sucessfully Allocated");
			Log.printLine("Requested MIPS for VM:"+BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getId()+"--> Sucessfully Allocated");
			Log.printLine("Requested RAM for VM:"+BidderBrokerFactory4.broke.get(indexOfUser).getVmList().get(k).getId()+"--> Sucessfully Allocated");
		    counter2++;  
		}
	}			    	   
 }
	 
  if (counter2==BidderBrokerFactory4.broke.get(indexOfUser).getVmList().size()){
	condition3=true;
  }
	 
  if ((condition1 & condition2 & condition3)==true){
	Log.printLine("The bad feedback submitted by BrokerID:"+BadFeedbackUserId+" is not valid. The feedback is discarted and a notification its send to the user!!!!"); 
	Log.printLine();
	Log.printLine("**********************************************"); 
  }else{  
	Log.printLine("The bad feedback submitted by BrokerID:"+BadFeedbackUserId+" is valid");
		  
	loop:
	  for (int z=0; z<BidderDatacenterFactory4.datac.size(); z++){
	
		  if(BidderDatacenterFactory4.datac.get(z).getName().equals(AccusedSP)){
	   	  	  BidderDatacenterFactory4.datac.get(z).setReputation(BidderDatacenterFactory4.datac.get(z).getReputation()-1);
		   	  Log.printLine("The reputation of SP:"+BidderDatacenterFactory4.datac.get(z).getName()+" lowered to Rep:"+BidderDatacenterFactory4.datac.get(z).getReputation());
		   	  break loop;
	      }	 
	  }
	Log.printLine();
	Log.printLine("***********************************************");
	}
  
  }				  
 }
}
	
	

