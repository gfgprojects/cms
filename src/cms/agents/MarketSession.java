package cms.agents;
import cms.Cms_builder;
import cms.agents.Producer;
import cms.agents.Buyer;
import cms.utils.ElementOfSupplyOrDemandCurve;

import java.util.ArrayList;
import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.util.collections.IndexedIterable;

public class MarketSession {
	public String variety,market;
	public double latitude,longitude,exchangesShare,sizeInGuiDisplay;
	public Context<Object> msContext;
	public Producer theProducer;
	public Buyer aBuyer;
	public IndexedIterable<Object> buyersList;
	public ArrayList<Double> bidAndAskPrices;
	public ArrayList<ElementOfSupplyOrDemandCurve> sessionSupplyCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
	public ArrayList<ElementOfSupplyOrDemandCurve> buyerDemandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
	public ArrayList<ElementOfSupplyOrDemandCurve> sessionDemandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
	private ElementOfSupplyOrDemandCurve tmpElement,tmpElement1;

	private Iterator<ElementOfSupplyOrDemandCurve> sessionSupplyCurveIterator,sessionDemandCurveIterator;
	private boolean lookingFroEquilibrium,northEastElementFound;
	public double marketPrice,quantityExchanged;
	double qsqdRatio;

	public MarketSession(String thisSessionMarket,Producer producer,String varietyName,Context<Object> theContext,ArrayList<Double> possiblePrices){
		market=thisSessionMarket;
		theProducer=producer;
		variety=varietyName;
		msContext=theContext;
		try{
			buyersList=msContext.getObjects(Class.forName("cms.agents.Buyer"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		bidAndAskPrices=possiblePrices;

		if(Cms_builder.verboseFlag){System.out.println("     market session, producer: "+theProducer.getName()+", product: "+variety);}
	}


	public void openSession(){
		if(Cms_builder.verboseFlag){System.out.println("       -----------------------------------------------------------");}
		if(Cms_builder.verboseFlag){System.out.println("       session opened for producer: "+theProducer.getName()+", product: "+variety);}
		sessionSupplyCurve=theProducer.getSupplyCurve(variety);

		//			ElementOfSupplyOrDemandCurve tmpElement;

		for(int i=0;i<sessionSupplyCurve.size();i+=100){
			tmpElement=(ElementOfSupplyOrDemandCurve)sessionSupplyCurve.get(i);
			if(Cms_builder.verboseFlag){System.out.println("                price "+tmpElement.getPrice()+" quantity "+tmpElement.getQuantity());}
		}

		sessionDemandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
		for(Double aPrice : bidAndAskPrices){
			sessionDemandCurve.add(new ElementOfSupplyOrDemandCurve(aPrice,0.0));
		}

		if(Cms_builder.verboseFlag){System.out.println("           BUYERS SEND DEMAND CURVE");}
		for(int i=0;i<buyersList.size();i++){
			aBuyer=(Buyer)buyersList.get(i);
			buyerDemandCurve=aBuyer.getDemandCurve(market,theProducer,variety);

			for(int j=0;j<buyerDemandCurve.size();j++){
				tmpElement=(ElementOfSupplyOrDemandCurve)buyerDemandCurve.get(j);
				tmpElement1=(ElementOfSupplyOrDemandCurve)sessionDemandCurve.get(j);
				tmpElement1.increaseQuantityBy(tmpElement.getQuantity());
			}
			/*
			   for(int j=0;j<buyerDemandCurve.size();j+=100){
			   tmpElement=(ElementOfSupplyOrDemandCurve)buyerDemandCurve.get(j);
			   System.out.println("                price "+tmpElement.getPrice()+" quantity "+tmpElement.getQuantity());
			   }
			   */
		}

		if(Cms_builder.verboseFlag){System.out.println("           AGGREGATING DEMAND CURVES");}
		if(Cms_builder.verboseFlag){System.out.println("           some points of the aggregate demand curve are:");}
		for(int j=0;j<sessionDemandCurve.size();j+=100){
			tmpElement=(ElementOfSupplyOrDemandCurve)sessionDemandCurve.get(j);
			if(Cms_builder.verboseFlag){System.out.println("                price "+tmpElement.getPrice()+" quantity "+tmpElement.getQuantity());}
		}

		if(Cms_builder.verboseFlag){System.out.println("           LOOKING FOR EQUILIBRIUM");}
		sessionSupplyCurveIterator=sessionSupplyCurve.iterator();
		sessionDemandCurveIterator=sessionDemandCurve.iterator();
		lookingFroEquilibrium=true;
		while(sessionSupplyCurveIterator.hasNext() && lookingFroEquilibrium){
			tmpElement=sessionSupplyCurveIterator.next();
			northEastElementFound=false;
			while(sessionDemandCurveIterator.hasNext() && !northEastElementFound){
				tmpElement1=sessionDemandCurveIterator.next();
				if(tmpElement1.getPrice()>tmpElement.getPrice() && tmpElement1.getQuantity()>tmpElement.getQuantity()){
					northEastElementFound=true;
				}
			}
			if(!northEastElementFound){
				lookingFroEquilibrium=false;
				marketPrice=tmpElement.getPrice();
			}
			
			qsqdRatio=1;
			//when demand curve is too high, resize quantity to that supplied
			if(marketPrice==sessionSupplyCurve.get(sessionSupplyCurve.size()-1).getPrice()){
				qsqdRatio=tmpElement.getQuantity()/tmpElement1.getQuantity();			
				if(qsqdRatio>1){
					qsqdRatio=1;
				}
			}

		}
		if(Cms_builder.verboseFlag){System.out.println("           market price is "+marketPrice);}
		if(Cms_builder.verboseFlag){System.out.println("           BUYERS COMPUTE BOUGHT QUANTITY ");}

		quantityExchanged=0;
		for(int i=0;i<buyersList.size();i++){
			aBuyer=(Buyer)buyersList.get(i);
			aBuyer.computeBoughtQuantity(market,theProducer,variety,marketPrice,qsqdRatio);
			quantityExchanged+=aBuyer.getQuantityBoughtInLatestMarketSession();
		}
		if(Cms_builder.verboseFlag){System.out.println("           SELLER COMPUTES SOLD QUANTITY ");}
		theProducer.setQuantitySoldInLatestMarketSession(variety,marketPrice,quantityExchanged);



	}

	public Producer getProducer(){
		return theProducer;
	}

	public String getMarketName(){
		return market;
	}

	public String getProducerName(){
		return theProducer.getName();
	}
	public double getMarketPrice(){
		return marketPrice;
	}
	public double getQuantityExchanged(){
		return quantityExchanged;
	}
	public String getSessionDescription(){
		return theProducer.getName()+" @ "+market;
	}
}
