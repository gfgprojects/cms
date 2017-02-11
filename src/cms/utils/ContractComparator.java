package cms.utils;
import cms.utils.Contract;

import java.util.Comparator;

public class ContractComparator implements Comparator<Contract> {

public int compare(Contract contract1,Contract contact2){
	return Double.compare(contract1.getPricePlusTransport(),contact2.getPricePlusTransport());
}
}
