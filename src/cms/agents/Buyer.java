package cms.agents;

import cms.Cms_builder;
import cms.agents.Producer;
import cms.utils.ElementOfSupplyOrDemandCurve;
import cms.utils.Contract;
import cms.utils.ContractComparator;
import cms.utils.DemandFunctionParameters;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Collections;
import java.math.BigDecimal;
import java.math.RoundingMode;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.IndexedIterable;
//import repast.simphony.essentials.RepastEssentials;
/**
 * The Buyer class hold all the relevant variable for a Buyer; It has methods for performing the Buyer's actions. The evolution of buying strategy and of the import policy are of particular importance.  
 * 
 * @author giulioni
 *
 */
public class Buyer {
	public String name;
	public double latitude,longitude,demandShare,sizeInGuiDisplay;
	public boolean importAllowed=true;
	public ArrayList<Double> demandPrices=new ArrayList<Double>();
	public ArrayList<ElementOfSupplyOrDemandCurve> demandCurve,tmpDemandCurve;
	public ArrayList<Contract> latestContractsList=new ArrayList<Contract>();
	public ArrayList<Contract> latestContractsInPossibleMarketSessionsList=new ArrayList<Contract>();
	public ArrayList<MarketSession> tmpMarketSessionsList;
	public ArrayList<MarketSession> possibleMarketSessionsList,continueBuyingMarketSessionsList,startBuyingMarketSessionsList;
	public ArrayList<DemandFunctionParameters> demandFunctionParametersList=new ArrayList<DemandFunctionParameters>();


	double tmpDemandedQuantity;
	double transportCosts;
	private ElementOfSupplyOrDemandCurve tmpElement;
	int distanceFromSellerInKm;
	ListIterator<ElementOfSupplyOrDemandCurve> demandCurveIterator;
	private boolean demandPriceLowerThanMarketPrice,mustImport;
	public double quantityBoughtInLatestMarketSession;
	public double pricePayedInLatestMarketSession;
	public String varietyBoughtInLatestMarketSession,latestMarket;
	public int minimumConsumption,realizedConsumption,gapToTarget,gapToChargeToEachPossibleMarketSession,stock,demandToBeReallocated;
	Producer aProducer;
	boolean latestPeriodVisitedMarketSessionNotFound,reallocateDemand,parametersHoldeNotFound;
	Contract aContract,aContract1;
	DemandFunctionParameters aParametersHolder;
	int interceptOfTheDemandFunction,initialInterceptOfTheDemandFunction,slopeOfTheDemandFunction,demandToBeMoved;



	public Buyer(String buyerName,double buyerLatitude,double buyerLongitude,double buyerDemandShare,ArrayList<Double> possiblePrices,int demandFunctionIntercept,int demandFunctionSlope){
		name=buyerName;
		latitude=buyerLatitude;
		longitude=buyerLongitude;
		demandShare=buyerDemandShare;
		minimumConsumption=(int)(Cms_builder.consumptionShareToSetMinimumConsumption*demandShare*Cms_builder.globalProduction/Cms_builder.productionCycleLength);
//		stockTargetLevel=(int)(desiredConsumption*Cms_builder.consumptionShareToSetInventoriesTarget);
//		stock=stockTargetLevel;
		stock=0;
		sizeInGuiDisplay=demandShare*100;
		initialInterceptOfTheDemandFunction=demandFunctionIntercept;
		slopeOfTheDemandFunction=demandFunctionSlope;
		if(Cms_builder.verboseFlag){System.out.println("Created buyer:    "+name+", latitude: "+latitude+", longitude: "+longitude+" minimum consumption "+minimumConsumption+" stock "+stock);}
		demandPrices=possiblePrices;
	}


	public void stepImportAllowedFlag(){
		if(mustImport){
			importAllowed=true;
		}
		else{
			if(RandomHelper.nextDouble()<Cms_builder.probabilityToAllowImport){
				importAllowed=true;
			}
			else{
				importAllowed=false;
			}
		}
		if(Cms_builder.verboseFlag){System.out.println("         buyer:    "+name+" importAllowed "+importAllowed);}

	}




	public void stepBuyingStrategy(IndexedIterable<Object> theProducersList){
		if(Cms_builder.verboseFlag){System.out.println("         buyer: "+name+" step buying strategy.");}
		possibleMarketSessionsList=new ArrayList<MarketSession>();
		continueBuyingMarketSessionsList=new ArrayList<MarketSession>();
		startBuyingMarketSessionsList=new ArrayList<MarketSession>();
		latestContractsInPossibleMarketSessionsList=new ArrayList<Contract>();
		demandToBeReallocated=0;
		//identify market section in which it is possible to buy
		for(int i=0;i<theProducersList.size();i++){
			aProducer=(Producer)theProducersList.get(i);
			if(aProducer.getExportAllowerFlag()){
				tmpMarketSessionsList=aProducer.getMarkeSessions();
				for(MarketSession aMarketSession : tmpMarketSessionsList){
					possibleMarketSessionsList.add(aMarketSession);
				}
			}
			else{
				if(aProducer.getName().equals(name)){
					tmpMarketSessionsList=aProducer.getMarkeSessions();
					for(MarketSession aMarketSession : tmpMarketSessionsList){
						possibleMarketSessionsList.add(aMarketSession);
					}
				}
			}	
		}
		if(demandFunctionParametersList.size()>0){
			//identify what are the market sessions in which the buyer bought in the previous period and in which it is possible to buy in the next period 
			for(MarketSession aMarketSession : possibleMarketSessionsList){
				latestPeriodVisitedMarketSessionNotFound=true;
				for(Contract aContract : latestContractsList){
					if(aMarketSession.getMarketName().equals(aContract.getMarketName()) && aMarketSession.getProducerName().equals(aContract.getProducerName())){
						latestPeriodVisitedMarketSessionNotFound=false;
						continueBuyingMarketSessionsList.add(aMarketSession);
						latestContractsInPossibleMarketSessionsList.add(aContract);
					}
				}
				if(latestPeriodVisitedMarketSessionNotFound){
					startBuyingMarketSessionsList.add(aMarketSession);
				}

			}

			if(Cms_builder.verboseFlag){System.out.println("         buyer: "+name+" possible market sessions "+possibleMarketSessionsList.size()+" continue buying in "+continueBuyingMarketSessionsList.size()+" start buying in "+startBuyingMarketSessionsList.size()+" of them");}
				if(Cms_builder.verboseFlag){System.out.println("         buyer: "+name+"; in previous period "+name+" bought in "+latestContractsList.size()+" sessions. Here are the data: ");}

			Collections.sort(latestContractsList,new ContractComparator());
			for(Contract aContract : latestContractsList){
				reallocateDemand=true;

				if(Cms_builder.verboseFlag){System.out.println("                 "+aContract.getPricePlusTransport()+" price: "+aContract.getPrice()+" market: "+aContract.getMarketName()+" producer: "+aContract.getProducerName()+" quantity "+aContract.getQuantity());}

				for(Contract anOldNewContract : latestContractsInPossibleMarketSessionsList){
					if(aContract.getMarketName().equals(anOldNewContract.getMarketName()) && aContract.getProducerName().equals(anOldNewContract.getProducerName())){
						reallocateDemand=false;
					}  
				}
				if(reallocateDemand){
					demandToBeReallocated+=(int)aContract.getQuantity();
				}
			}
			if(Cms_builder.verboseFlag){System.out.println("         buyer: "+name+" must reallocate demand for "+demandToBeReallocated+" because some markets session were closed");}

			Collections.sort(latestContractsInPossibleMarketSessionsList,new ContractComparator());

			if(latestContractsInPossibleMarketSessionsList.size()<1){  //buyers with no producer can find all the markets closed and had no contracts in the previous period
				if(startBuyingMarketSessionsList.size()<1){
					if(Cms_builder.verboseFlag){System.out.println("              No available market sessions");}
				}
				else{
					//build an history for newly available market sessions making them appear as if the buyer bought there (creating contracts for them)
					for(MarketSession aMarketSession : startBuyingMarketSessionsList){
						aProducer=aMarketSession.getProducer();
						Cms_builder.distanceCalculator.setStartingGeographicPoint(longitude, latitude);
						Cms_builder.distanceCalculator.setDestinationGeographicPoint(aProducer.getLongitude(),aProducer.getLatitude());
						distanceFromSellerInKm=(int) Math.round(Cms_builder.distanceCalculator.getOrthodromicDistance()/1000);
						transportCosts= ((new BigDecimal(distanceFromSellerInKm/100.0)).divide(new BigDecimal(100.0)).setScale(2,RoundingMode.HALF_EVEN)).doubleValue();
						latestContractsInPossibleMarketSessionsList.add(new Contract(aMarketSession.getMarketName(),aMarketSession.getProducerName(),name,aMarketSession.getMarketPrice(),transportCosts,0));
					}
					Collections.sort(latestContractsInPossibleMarketSessionsList,new ContractComparator());
					for(Contract aContract : latestContractsInPossibleMarketSessionsList){
						if(Cms_builder.verboseFlag){System.out.println("                 renew "+aContract.getPricePlusTransport()+" price: "+aContract.getPrice()+" market: "+aContract.getMarketName()+" producer: "+aContract.getProducerName()+" quantity "+aContract.getQuantity());}
					}

					//setting the intercept for all existing parameters holders and creating the new parameters holders
					aContract=latestContractsInPossibleMarketSessionsList.get(0);
					for(MarketSession aMarketSession : startBuyingMarketSessionsList){
						parametersHoldeNotFound=true;
						for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
							if(aMarketSession.getMarketName().equals(aParametersHolder.getMarketName()) && aMarketSession.getProducerName().equals(aParametersHolder.getProducerName())){
								parametersHoldeNotFound=false;
								aParametersHolder.setIntercept((int)(slopeOfTheDemandFunction*aContract.getPriceMinusTransport()));
							}
						}
						if(parametersHoldeNotFound){
							aParametersHolder=new DemandFunctionParameters((int)(slopeOfTheDemandFunction*aContract.getPriceMinusTransport()),aMarketSession.getMarketName(),aMarketSession.getProducerName());
							demandFunctionParametersList.add(aParametersHolder);
						}
					}

					//overwriting the intercept for the parameter holder of the cheapest among the new available market sessions with the demandToBeReallocated (this can be avoided because the byers has no previous contracts so the demandToBeReallocated is 0)
					aContract=latestContractsInPossibleMarketSessionsList.get(0);
					parametersHoldeNotFound=true;
					for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
						if(aContract.getMarketName().equals(aParametersHolder.getMarketName()) && aContract.getProducerName().equals(aParametersHolder.getProducerName())){
							parametersHoldeNotFound=false;
							aParametersHolder.setIntercept((int)(demandToBeReallocated+slopeOfTheDemandFunction*aContract.getPriceMinusTransport()));
						}
					}
					if(parametersHoldeNotFound){
						aParametersHolder=new DemandFunctionParameters((int)(demandToBeReallocated+slopeOfTheDemandFunction*aContract.getPriceMinusTransport()),aContract.getMarketName(),aContract.getProducerName());
						demandFunctionParametersList.add(aParametersHolder);
					}

					//increasing the intercept of the new available market sessions parameters holder to fill the gap to the target  
					gapToChargeToEachPossibleMarketSession=gapToTarget/startBuyingMarketSessionsList.size();
					if(Cms_builder.verboseFlag){System.out.println("          gap to target in each market session "+gapToChargeToEachPossibleMarketSession);}
					for(MarketSession aMarketSession : startBuyingMarketSessionsList){
						for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
							if(aMarketSession.getMarketName().equals(aParametersHolder.getMarketName()) && aMarketSession.getProducerName().equals(aParametersHolder.getProducerName())){
								aParametersHolder.increaseInterceptBy(gapToChargeToEachPossibleMarketSession);
							}
						}
					}

				}
			}
			else{ //buyers with at leas one contract in the previous period concluded in a market session that will be open in the current period
				for(Contract aContract : latestContractsInPossibleMarketSessionsList){
					if(Cms_builder.verboseFlag){System.out.println("                 renew "+aContract.getPricePlusTransport()+" price: "+aContract.getPrice()+" market: "+aContract.getMarketName()+" producer: "+aContract.getProducerName()+" quantity "+aContract.getQuantity());}
				}
				//buyers move the demand to be reallocated to the producer with the lowest price
				if(Cms_builder.verboseFlag){System.out.println("              moving quantity bought in closed market session(s) to cheapest market session");}
				aContract=latestContractsInPossibleMarketSessionsList.get(0);
				for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
					if(aContract.getMarketName().equals(aParametersHolder.getMarketName()) && aContract.getProducerName().equals(aParametersHolder.getProducerName())){
						aParametersHolder.increaseInterceptBy(demandToBeReallocated);
					}
				}
				//buyers with more than one contract move demand from the highest price to the lowest price session  
				if(latestContractsInPossibleMarketSessionsList.size()>1){ 
				if(Cms_builder.verboseFlag){System.out.println("              moving quantity bought in most expensive market session to cheapest market session");}
					aContract=latestContractsInPossibleMarketSessionsList.get(0);
					aContract1=latestContractsInPossibleMarketSessionsList.get(latestContractsInPossibleMarketSessionsList.size()-1);
					demandToBeMoved=0;
//					if((1+Cms_builder.toleranceInMovingDemand)*aContract.getPrice()<aContract1.getPrice()){
					if((Cms_builder.toleranceInMovingDemand)*aContract.getPrice()<aContract1.getPrice()){
					demandToBeMoved=(int)(aContract1.getQuantity()*Cms_builder.shareOfDemandToBeMoved);
						for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
							if(aContract.getMarketName().equals(aParametersHolder.getMarketName()) && aContract.getProducerName().equals(aParametersHolder.getProducerName())){
								aParametersHolder.increaseInterceptBy(demandToBeMoved);
							}
							if(aContract1.getMarketName().equals(aParametersHolder.getMarketName()) && aContract1.getProducerName().equals(aParametersHolder.getProducerName())){
								aParametersHolder.decreaseInterceptBy(demandToBeMoved);
							}
						}
					}
				}

				if(Cms_builder.verboseFlag){System.out.println("              setting demand function for newly open market sessions");}
				//buyers update parameter in sessions that was not available in the previous period
				aContract=latestContractsInPossibleMarketSessionsList.get(0);
				for(MarketSession aMarketSession : startBuyingMarketSessionsList){
					parametersHoldeNotFound=true;
					for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
						if(aMarketSession.getMarketName().equals(aParametersHolder.getMarketName()) && aMarketSession.getProducerName().equals(aParametersHolder.getProducerName())){
							parametersHoldeNotFound=false;
							aParametersHolder.setIntercept((int)(slopeOfTheDemandFunction*aContract.getPricePlusTransport()*(1-Cms_builder.percentageOfPriceMarkDownInNewlyAccessibleMarkets)));
						}
					}
					if(parametersHoldeNotFound){
						aParametersHolder=new DemandFunctionParameters((int)(slopeOfTheDemandFunction*aContract.getPricePlusTransport()),aMarketSession.getMarketName(),aMarketSession.getProducerName());
						demandFunctionParametersList.add(aParametersHolder);
					}
				}
				//increasing the intercept of the available market sessions parameters holder to fill the gap to minimum consumption  
				if(Cms_builder.verboseFlag){System.out.println("              moving demand functions to fill the gap to target level of inventories"); }
				gapToChargeToEachPossibleMarketSession=gapToTarget/possibleMarketSessionsList.size();
				if(Cms_builder.verboseFlag){System.out.println("                gap to target in each market session "+gapToChargeToEachPossibleMarketSession);}
				for(MarketSession aMarketSession : possibleMarketSessionsList){
					for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
						if(aMarketSession.getMarketName().equals(aParametersHolder.getMarketName()) && aMarketSession.getProducerName().equals(aParametersHolder.getProducerName())){
							aParametersHolder.increaseInterceptBy(gapToChargeToEachPossibleMarketSession);
						}
					}
				}


			}


			if(Cms_builder.verboseFlag){System.out.println("         -----------------------------------------------------------------");}
			latestContractsList=new ArrayList<Contract>();
		}
		else{
			for(MarketSession aMarketSession : possibleMarketSessionsList){
				demandFunctionParametersList.add(new DemandFunctionParameters(initialInterceptOfTheDemandFunction,aMarketSession.getMarketName(),aMarketSession.getProducerName()));
			}
		}
	}




	public ArrayList<ElementOfSupplyOrDemandCurve> getDemandCurve(String theMarketName,Producer theProducer,String theVariety){
		Cms_builder.distanceCalculator.setStartingGeographicPoint(longitude, latitude);
		Cms_builder.distanceCalculator.setDestinationGeographicPoint(theProducer.getLongitude(),theProducer.getLatitude());
		distanceFromSellerInKm=(int) Math.round(Cms_builder.distanceCalculator.getOrthodromicDistance()/1000);

		if(Cms_builder.verboseFlag){System.out.println("           "+name+" distance From "+theProducer.getName()+" "+distanceFromSellerInKm+" kilometers");}

		transportCosts= ((new BigDecimal(distanceFromSellerInKm/100.0)).divide(new BigDecimal(100.0)).setScale(2,RoundingMode.HALF_EVEN)).doubleValue();
		if(Cms_builder.verboseFlag){System.out.println("           "+name+" transport cost "+transportCosts);}

		parametersHoldeNotFound=true;
		for(DemandFunctionParameters aParametersHolder : demandFunctionParametersList){
			if(aParametersHolder.getMarketName().equals(theMarketName) && aParametersHolder.getProducerName().equals(theProducer.getName())){
				interceptOfTheDemandFunction=aParametersHolder.getIntercept();
				parametersHoldeNotFound=false;
				if(Cms_builder.verboseFlag){System.out.println("           "+name+" new intercept of the demand function "+interceptOfTheDemandFunction);}
			}
		}
		if(parametersHoldeNotFound){
			interceptOfTheDemandFunction=initialInterceptOfTheDemandFunction;
		}

// create and fill a dummy demand curve
		tmpDemandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
		for(Double aPrice : demandPrices){
			tmpDemandedQuantity=interceptOfTheDemandFunction-aPrice*slopeOfTheDemandFunction;
//			tmpDemandCurve.add(new ElementOfSupplyOrDemandCurve((new BigDecimal(aPrice-transportCosts)).setScale(2,RoundingMode.HALF_EVEN).doubleValue(),(new BigDecimal(tmpDemandedQuantity)).setScale(2,RoundingMode.HALF_EVEN).doubleValue()));
			tmpDemandCurve.add(new ElementOfSupplyOrDemandCurve((new BigDecimal(aPrice)).setScale(2,RoundingMode.HALF_EVEN).doubleValue(),(new BigDecimal(tmpDemandedQuantity)).setScale(2,RoundingMode.HALF_EVEN).doubleValue()));
		}
		//negative quantities of the dummy demand curve are set to 0
		for(ElementOfSupplyOrDemandCurve tmpElement : tmpDemandCurve){
			if(tmpElement.getQuantity()<0){
				tmpElement.setQuantityToZero();
			}
		}
		

//due to transport costs the dummy demand curve can have negative prices
//the final demand curve is created
		demandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
//the elements of the dummy demand curve with positive price is copied into the final demand curve
		for(ElementOfSupplyOrDemandCurve tmpElement : tmpDemandCurve){
			if(tmpElement.getPrice()>=0){
				demandCurve.add(tmpElement);
			}
		}
//the final demand curve is completed
		if(demandCurve.size()<tmpDemandCurve.size()){
			for(int i=demandCurve.size();i<tmpDemandCurve.size();i++){
				tmpElement=(ElementOfSupplyOrDemandCurve)demandCurve.get(i-1);
				demandCurve.add(new ElementOfSupplyOrDemandCurve((new BigDecimal(tmpElement.getPrice()+0.01)).setScale(2,RoundingMode.HALF_EVEN).doubleValue(),BigDecimal.ZERO.doubleValue()));
			}
		}
		//System.out.println("size "+demandCurve.size());

//revise demand curve for minimumImportQuantity
		if(!name.equals(theProducer.getName())){
			for(ElementOfSupplyOrDemandCurve tmpElement : tmpDemandCurve){
				if(tmpElement.getQuantity()<Cms_builder.minimumImportQuantity){
					tmpElement.setQuantityToZero();
				}
			}			
		}


		if(importAllowed){
			if(theProducer.getExportAllowerFlag()){
				if(Cms_builder.verboseFlag){System.out.println("           demand curve is sent by "+name+" for product "+theVariety);}
			}
			else{
				if(name.equals(theProducer.getName())){
					if(Cms_builder.verboseFlag){System.out.println("           demand curve is sent by "+name+" for product "+theVariety);}
				}
				else{
					if(Cms_builder.verboseFlag){System.out.println("           demand curve is not sent by "+name+" because producer's exportAllowed flag is false");}
					demandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
				}

			}
		}
		else{
			if(name.equals(theProducer.getName())){
				if(Cms_builder.verboseFlag){System.out.println("           demand curve is sent by "+name+" for product "+theVariety);}
			}
			else{
				if(Cms_builder.verboseFlag){System.out.println("           demand curve is not sent by "+name+" because importAllowed is "+importAllowed);}
				demandCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
			}
		}
		return demandCurve;
	}

	public void computeBoughtQuantity(String theMarket,Producer theProducer,String theVariety, double marketPrice,double rescalingFactor){
		demandPriceLowerThanMarketPrice=true;
		if(demandCurve.size()<1){
			if(Cms_builder.verboseFlag){System.out.println("           demand curve was not sent by "+name);}
			quantityBoughtInLatestMarketSession=0;
			pricePayedInLatestMarketSession=0;
			varietyBoughtInLatestMarketSession=null;
		}
		else{
			demandCurveIterator=demandCurve.listIterator();
			while(demandCurveIterator.hasNext() && demandPriceLowerThanMarketPrice){
				tmpElement=demandCurveIterator.next();
				if(tmpElement.getPrice()>=marketPrice){
					demandPriceLowerThanMarketPrice=false;
				}
			}
			quantityBoughtInLatestMarketSession=tmpElement.getQuantity()*rescalingFactor;
			pricePayedInLatestMarketSession=tmpElement.getPrice();
			varietyBoughtInLatestMarketSession=theVariety;
			latestMarket=theMarket;
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock before: "+stock); }
			stock+=quantityBoughtInLatestMarketSession;
			if(quantityBoughtInLatestMarketSession>0){
				latestContractsList.add(new Contract(latestMarket,theProducer.getName(),name,pricePayedInLatestMarketSession,transportCosts,quantityBoughtInLatestMarketSession));
			}
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" price "+pricePayedInLatestMarketSession+" quantity "+quantityBoughtInLatestMarketSession+" of "+varietyBoughtInLatestMarketSession);}
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock after: "+stock+" minimum consumption: "+minimumConsumption);}
		}
	}
	/**
	 *Decreases the existing stock by the minimum between the desired consumption and the existing stock
	 */
	public void accountConsumption(){
		if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock before: "+stock+" minimum Consumption: "+minimumConsumption);}
		gapToTarget=minimumConsumption-stock;
		if(gapToTarget<0){
			gapToTarget=0;
		}
		realizedConsumption=stock;
		stock=0;
//		if(gapToTarget>0){System.out.println("           time "+RepastEssentials.GetTickCount()+" "+name+" consumption: "+realizedConsumption+" minC "+minimumConsumption);}

		if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock after: "+stock+" minC - C "+gapToTarget);}
/*
		if(desiredConsumption<=stock){
			realizedConsumption=desiredConsumption;	       
			stock+=-realizedConsumption;
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock after: "+stock+" target: "+stockTargetLevel+" gap to target "+gapToTarget);}
		}
		else{
			realizedConsumption=stock;
			stock=0;
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock after: "+stock+" target: "+stockTargetLevel+" gap to target "+gapToTarget);}
		}	
*/
	}
	
	public void setMustImportFlag(boolean buyerMustImport){
		mustImport=buyerMustImport;
		if(mustImport){
			if(Cms_builder.verboseFlag){System.out.println(name+" must import because has no producers");}
		}
		else{
			if(Cms_builder.verboseFlag){System.out.println(name+" can forbid imports because has internal producers");}
		}

	}
	public double getQuantityBoughtInLatestMarketSession(){
		return quantityBoughtInLatestMarketSession;
	}
	public String getName(){
		return name;
	}
	public double getLatitude(){
		return latitude;
	}
	public double getLongitude(){
		return longitude;
	}
	public double getDemandShare(){
		return demandShare;
	}
	public double getSizeInGuiDisplay(){
		return sizeInGuiDisplay;
	}
	public int getStock(){
		return stock;
	}
	public int getRealizedConsumption(){
		return realizedConsumption;
	}
	/**
	 * 
	 * The gap between the target level of the stock and the level of the stock that would be observed if the desired consumption is achieved. It is equal to the stock target level if the desired consumption could not be achieved.
	 *   
	 */
	public int getGapToTarget(){
		return gapToTarget;
	}
	


}
