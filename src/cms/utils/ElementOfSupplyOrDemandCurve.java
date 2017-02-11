package cms.utils;


public class ElementOfSupplyOrDemandCurve{
	double price,quantity;

	public ElementOfSupplyOrDemandCurve(double thisElementPrice,double thisElementQuantity){
		price=thisElementPrice;
		quantity=thisElementQuantity;
	}


	public double getPrice(){
		return price;
	}
	public double getQuantity(){
		return quantity;
	}
	public void increaseQuantityBy(double quantityIncrease){
		quantity+=quantityIncrease;
	}
	public void setQuantityToZero(){
		quantity=0.0;
	}

}
