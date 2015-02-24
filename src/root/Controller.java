package root;

import org.cloudbus.cloudsim.Log;

/**
 * contains the main method of the market sec attacks
 * to run each attack/solution (one at a time) just change the false conditions to true
 * 
 *
 */
public class Controller {

	//describes the number of datacenters and databrokers in the simulation
	static int datacenters=4;
	static int databrokers=40; //the number of brokers to datacenters should be proportional ....
	
	public static void main(String[] args) throws Exception {
	
	  //calls/enables the unpaid attack
	  UnpaidResources unpaid=new UnpaidResources(false); //setting the denial of payment attack to effect

	  //calls/enables the monopoly attack
	  Monopoly mono=new Monopoly(false);//setting the monopoly attack to effect
    
	  //calls/enables the shill bidding attack
	  ShillBidding shill=new ShillBidding(false);//setting the shill bidding attack to effect;
		
	  //generates the market entities and initiates auctions
	  Market mark = new Market(datacenters, databrokers);
	  mark.GenerateMarket();
		
	  mono.SolveMonopolyAttack(false);
	  unpaid.solveUnpaidResourceAttack(false);
      shill.solveShillAttack(false);
			
      //calls/enables the damage reputation attack (bad false feedback)
	  DamageReputation rep= new DamageReputation(false);  //setting the bad feedback attack to effect

	  rep.SolveReputationAttack(false);	 
		
	}

}








