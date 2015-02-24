package root;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.auction.bid.DatacenterBid;
import org.cloudbus.cloudsim.auction.bidder.BidderDatacenter;
import org.cloudbus.cloudsim.auction.vm.DatacenterAbstractVm;
import org.cloudbus.cloudsim.auction.vm.VmCharacteristics;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class BidderDatacenterFactory4 {

	static List<BidderDatacenter> datac;
	public static double totalcostPerSec=0;
	public static double totalcostPerMem=0;
	public static double totalcostPerBw=0;
	public static double totalPerStorage=0;
	
	public static List<BidderDatacenter> createDatacenter(int numOfDatacenters) {

		datac = new ArrayList<BidderDatacenter>();
		BidderDatacenter datacenter = null;
		int ReputationStatus=5; //highest possible reputation for datacenters (sps) 
		Monopoly mono=new Monopoly();

		for (int i = 1; i <= numOfDatacenters; i++) {

			String datacenterName = "Datacenter_" + i;
			String arch = "x64"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located

			// Here are the steps needed to create a PowerDatacenter:
			// 1. We need to create a list to store one or more
			// Machines
			List<Host> hostList = new ArrayList<Host>();

			// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore,
			// should
			// create a list to store these PEs before creating a Machine.
			List<Pe> peList1 = new ArrayList<Pe>();

			// 3. Create PEs and add these into the list.
			// for a quad-core machine, a list of 4 PEs is required:
			int mips = 6950;

			peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
			
//******************************************MONOPOLY ATTACK******************
			// 4. Create Hosts with its id and list of PEs and add them to the list of machines
			SecureRandom ran = new SecureRandom();
			int hostId = 0;
			
			if(mono.getMonopolyAttackStatus()==true){ //this performs monopoly attack
				
				Log.printLine(datacenterName+" will acquire excessive amounts of computational resources for performing monopoly attack");
				 
				for (int j = 0; j <= 20; j++) {
					 int ram = 900000000; // host memory (MB)
					 long storage = 900000000; // host storage
					 int bw = 900000000;
					 Host host = new Host(hostId++, new RamProvisionerSimple(ram),
							new BwProvisionerSimple(bw), storage, peList1,
							new VmSchedulerTimeShared(peList1));
					        hostList.add(host);
				   }
				
			}else{
				
			   // create a random number of hosts for each datacenter
			   for (int j = 0; j <= ran.nextInt(12) + 1; j++) {
				 int ram = ran.nextInt(400000) + 12032; // host memory (MB)
				 long storage = ran.nextInt(6600000) + 260000; // host storage
				 int bw = ran.nextInt(2000000) + 8000;
				 Host host = new Host(hostId++, new RamProvisionerSimple(ram),
						new BwProvisionerSimple(bw), storage, peList1,
						new VmSchedulerTimeShared(peList1));
				        hostList.add(host);
			   }
			}

			LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now
			DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, 0, 0, 0, 0);

			// 6. Finally, we need to create a PowerDatacenter object.
			datacenter = null;
			try {
			  datacenter = new BidderDatacenter(datacenterName,characteristics,new VmAllocationPolicySimple(hostList),storageList,0,ReputationStatus);
			} catch (Exception e) {
				e.printStackTrace();
			}

			DatacenterBid bid = new DatacenterBid(datacenter.getId());
			double costPerSec=0, costPerMem=0, costPerBw=0, costPerStorage=0; 
			
			for (int k = 0; k < ran.nextInt(20) + 7; k++) {
				 
				if(mono.getMonopolyAttackStatus()==true){ //if monopoly attack is going to performed the certain SP will increase its prices
					 costPerSec=(double) ran.nextInt(2) + 4;
					 costPerMem=ran.nextDouble() + 0.5;
					 costPerBw=ran.nextDouble() + 0.5;
					 costPerStorage=ran.nextDouble()+0.5;
					bid.addVM(new DatacenterAbstractVm((double) ran.nextInt(90000) + 100,
							ran.nextInt(3) + 1, ran.nextInt(40480) + 512,
							ran.nextInt(500000) + 400, ran.nextInt(700000) + 10000,
							new VmCharacteristics(arch, os, vmm, time_zone,
							costPerSec,costPerMem,costPerBw,costPerStorage)), ran.nextInt(4) + 3);
				}else{
					costPerSec=(double) ran.nextInt(2) + 0.5;
					costPerMem=ran.nextDouble() + 0.02;
					costPerBw=ran.nextDouble() + 0.02;
					costPerStorage=ran.nextDouble()+0.02;
				   bid.addVM(new DatacenterAbstractVm((double) ran.nextInt(90000) + 100,
								ran.nextInt(3) + 1, ran.nextInt(40480) + 512,
								ran.nextInt(500000) + 400, ran.nextInt(700000) + 10000,
								new VmCharacteristics(arch, os, vmm, time_zone,
								costPerSec, costPerMem,costPerBw,costPerStorage)), ran.nextInt(4) + 2);
				}
				totalcostPerSec+=costPerSec;
				totalcostPerMem+=costPerMem;
				totalcostPerBw+=costPerBw;
				totalPerStorage+=costPerStorage;
			}

			datacenter.submitBid(bid);
			datac.add(datacenter);
			
		}
		return datac;
	}

}
