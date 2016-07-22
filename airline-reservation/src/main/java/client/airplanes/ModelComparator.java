/**
 * 
 */
package client.airplanes;

import java.util.Comparator;
/**
 * This class implements a Comparator for Airplanes based on the model.
 * It provides support for utilizing libraries to do sorting
 * such as Collections.
 * 
 * @author James
 * @version 1
 * @since 07/19/2016
 *
 */
public class ModelComparator implements Comparator<Airplane> {
	/**
	 * compare function of Airplane class based on model
	 * 
	 * @param r1 is first Airplane to compare
	 * @param r2 is the second Airplane to compare
	 * 
	 * @return -1 if less than, 0 if equal, 1 if greater than
	 */
	public int compare(Airplane r1, Airplane r2) {
		String model1 = r1.model();
		String model2 = r2.model();
		return model1.compareTo(model2);
	}
}
