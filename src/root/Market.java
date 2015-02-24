package root;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.auction.auctioneer.Auctioneer;
import org.cloudbus.cloudsim.core.CloudSim;

public class Market {

	int DatacenterNum, DatacenterBrokerNum;

	public Market(int DatacenterNum, int DatacenterBrokerNum) {
		this.DatacenterBrokerNum = DatacenterBrokerNum;
		this.DatacenterNum = DatacenterNum;
	}

	public void GenerateMarket() {

		Log.printLine("Generating the Market...");

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = DatacenterBrokerNum; // number of grid users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);
			Auctioneer.initAuctioneer();

			// Second step: Create bidder datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			List<Datacenter> datacenters = new ArrayList<Datacenter>();
			datacenters.addAll(BidderDatacenterFactory4
					.createDatacenter(DatacenterNum));

			// Third step: Create bidder broker
			// DatacentreBroker represents a broker acting on behalf of a user.
			// It hides VM management, as vm creation, sumbission of cloudlets
			// to this VMs and destruction of VMs.
			List<DatacenterBroker> datacenterbrokers = new ArrayList<DatacenterBroker>();
			datacenterbrokers.addAll(BidderBrokerFactory4.createBroker(DatacenterBrokerNum));

			// Forth step: Starts the simulation
			CloudSim.startSimulation();

			// Final step: Print results when simulation is over
			CloudSim.stopSimulation();

			for (int i = 0; i < datacenterbrokers.size(); i++) {
				printCloudletList(datacenterbrokers.get(i)
						.getCloudletReceivedList());
			}

			// Print the debt of each user to each datacenter
			for (int i = 0; i < datacenters.size(); i++) {
				datacenters.get(i).printDebts();
			}

			Log.printLine("Auction Completed!");
		} catch (Exception e) {
			Log.printLine("Unwanted errors happened");
			e.printStackTrace();
		}
	}

	/**
	 * Prints the Cloudlet objects
	 * 
	 * @param list
	 *            list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + indent
				+ "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}

	}

}
