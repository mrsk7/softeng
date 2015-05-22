package gov.nist.sip.proxy;


public class BillingObject {

	
	public double calculateCost(String policy,long duration) {
		double cost = -1;
		if (policy == "Basic") {
			cost = 0.10 + duration * 0.01;
			/* Every call costs 0.10euro fixed cost plus 1 cent per second */
		}
		else if (policy == "Premium") {
			cost = duration/10 * 0.12 ;
		}
		//TODO Add new policies whenever-wherever
		return cost;
	}

}
