package root;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;


public class Monopoly {
	
	static boolean performAttack=false;
	static boolean solution=false;
	static int i;
	
	public Monopoly() {}
	
	public Monopoly(boolean attack){
		this.performAttack=attack;
		
		if(performAttack==true)
		Log.printLine("Initiating Monopoly attack");
		
		i=1;
	}
	
	public boolean getMonopolyAttackStatus(){//Used by BidderDatacenterFactory4	
		if (i<=1){ //how many SPs will deploy monopoly attack (usually in the real world 1)
			i++;
			return performAttack;
		}
		    return false;
	}
	
	
	public void SolveMonopolyAttack(boolean solve){
		solution=solve;
		
	  if(solution==true & performAttack==true){
			
		//if someone holds more than 60% of the market is suspicious.... then check number of bids (randomly generate), utilized resources, idle resources
		BigDecimal TotalMarketram = BigDecimal.valueOf(0);
		BigDecimal TotalMarketstorage = BigDecimal.valueOf(0);
		BigDecimal TotalMarketbandwidth = BigDecimal.valueOf(0);
		BigDecimal RamPerSP = BigDecimal.valueOf(0);
		BigDecimal StoragePerSP = BigDecimal.valueOf(0);
		BigDecimal BandwidthPerSP = BigDecimal.valueOf(0);
		ArrayList <BigDecimal> RAM=new ArrayList <BigDecimal>();
		ArrayList <BigDecimal> STORAGE=new ArrayList <BigDecimal>();
		ArrayList <BigDecimal> BANDWIDTH=new ArrayList <BigDecimal>();
		
		for (int i=0; i<BidderDatacenterFactory4.datac.size(); i++){ //it will sum up all available resources in the market
			for (int j=0; j<BidderDatacenterFactory4.datac.get(i).getHostList().size(); j++){
				
				TotalMarketram=TotalMarketram.add(BigDecimal.valueOf(BidderDatacenterFactory4.datac.get(i).getHostList().get(j).getRam())); //it returns all available ram in a market
				RamPerSP=RamPerSP.add(BigDecimal.valueOf(BidderDatacenterFactory4.datac.get(i).getHostList().get(j).getRam()));//it returns all available ram of a certain sp
				
				TotalMarketstorage=TotalMarketstorage.add(BigDecimal.valueOf(BidderDatacenterFactory4.datac.get(i).getHostList().get(j).getStorage())); //it returns all available storage in a market
				StoragePerSP=StoragePerSP.add(BigDecimal.valueOf(BidderDatacenterFactory4.datac.get(i).getHostList().get(j).getStorage())); //it returns all available storage of a certain sp
				
				TotalMarketbandwidth=TotalMarketbandwidth.add(BigDecimal.valueOf(BidderDatacenterFactory4.datac.get(i).getHostList().get(j).getBw())); //it returns all available bandwidth in a market
				BandwidthPerSP=BandwidthPerSP.add(BigDecimal.valueOf(BidderDatacenterFactory4.datac.get(i).getHostList().get(j).getBw())); //it returns all available bandwidth of a certain sp
			}
			
			//record the ram, storage and bandwidth for each SP separately
			RAM.add(RamPerSP);
			STORAGE.add(StoragePerSP);
			BANDWIDTH.add(BandwidthPerSP);
			//re-initialize attributes for next SP 
			RamPerSP= BigDecimal.valueOf(0);
			StoragePerSP= BigDecimal.valueOf(0);
			BandwidthPerSP= BigDecimal.valueOf(0);
		}
		
		BigDecimal threshold= BigDecimal.valueOf(30);
		boolean flagSP=false;
		int index=0;
		
		for (int i=0; i<BidderDatacenterFactory4.datac.size(); i++){
		
			//check if a SP contains more than 30% (depending on threshold) of the total resources in the market
			 if(RAM.get(i).doubleValue() / TotalMarketram.doubleValue()*100 >threshold.doubleValue()|
			   STORAGE.get(i).doubleValue() / TotalMarketstorage.doubleValue()*100 >threshold.doubleValue()|
			   BANDWIDTH.get(i).doubleValue() / TotalMarketbandwidth.doubleValue()*100 >threshold.doubleValue()){
				 
				 Log.printLine();
				 Log.printLine("**********Monopoly Attack Solution************");
				 Log.printLine(BidderDatacenterFactory4.datac.get(i).getName()+" owns more than "+threshold+"% of the overall market resources");
				 Log.printLine(BidderDatacenterFactory4.datac.get(i).getName()+" is flaged as suspicious");
				 flagSP=true;
				 index=i;
			 }
		}
		
        if (flagSP==true){		
                long IdleRam=0;
                long IdleBandwidth=0;
                long IdleStorage=0;
                double IdleMIPS=0;
                int NumOfBorrowedRes=20;
                long totalRam=0;
                long totalBandwidth=0;
                long totalStorage=0;
                double totalMIPS=0;
                
				 for(int j=0; j<BidderDatacenterFactory4.datac.get(index).getHostList().size();j++){
				   
					 IdleRam=IdleRam+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getRamProvisioner().getAvailableRam();  
				     IdleBandwidth=IdleBandwidth+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getBwProvisioner().getAvailableBw();
				     IdleStorage=IdleStorage+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getStorage();
				     IdleMIPS=IdleMIPS+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getAvailableMips();
				     totalRam=totalRam+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getRamProvisioner().getRam();
				     totalBandwidth=totalBandwidth+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getBw();
				     totalStorage=totalStorage+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getStorage();
				     totalMIPS=totalMIPS+BidderDatacenterFactory4.datac.get(index).getHostList().get(j).getTotalMips();
				 }			 
				 
				 //check if 90% of the resources of the SP are available
				  if(((IdleRam >= (totalRam*90)/100) | (IdleBandwidth >= (totalBandwidth*90)/100) |
				  (IdleStorage >= (totalStorage*90)/100) | (IdleMIPS >= (totalMIPS*90)/100)) & NumOfBorrowedRes>20) {
					  Log.printLine("IdleRam found:"+ IdleRam+ " Mbytes (>90% of the total RAM resource in market)");
					  Log.printLine("IdleBandwidth found:"+ IdleBandwidth+ " Mbits (>90% of the total Bandwidth resource in market)");
					  Log.printLine("IdleStorage found:"+ IdleStorage+ " Gbytes (>90% of the total Storage resource in market)");
					  Log.printLine("Number of Requests for borrowing resources from rival SPs:"+ NumOfBorrowedRes);
					  Log.printLine(BidderDatacenterFactory4.datac.get(index).getName()+" found guilty of requesting extra resources that were not needed");
					  int time=10000;
					  Log.printLine("The operations of "+BidderDatacenterFactory4.datac.get(index).getName()+" will be paused for:"+time);
					  Log.printLine("*************************************************");
					  BidderDatacenterFactory4.datac.get(index).pause(time);
					  Log.printLine();
				  }		  
	    }
        
      //test
		  Log.printLine("The average price of Ram per second in the market is: " +(double) BidderDatacenterFactory4.totalcostPerMem/Controller.datacenters);
		  Log.printLine("The average price of Storage per second in the market is: " +(double) BidderDatacenterFactory4.totalPerStorage/Controller.datacenters);
		  Log.printLine("The average price of Bandwidth per second in the market is: " +(double) BidderDatacenterFactory4.totalcostPerBw/Controller.datacenters);
  
   }
 }
	
	public boolean getSolution(){
		return solution;
	}
}
