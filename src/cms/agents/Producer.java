package cms.agents;

import repast.simphony.random.RandomHelper;
import cms.Cms_builder;
import cms.utils.ElementOfSupplyOrDemandCurve;
import cms.agents.MarketSession;

import java.util.ArrayList;

public class Producer {
	public String name,markets,varieties;
	public double latitude,longitude,productionShare,sizeInGuiDisplay;
	public boolean exportAllowed=true;
	public ArrayList<Double> supplyPrices=new ArrayList<Double>();
	public ArrayList<ElementOfSupplyOrDemandCurve> supplyCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
	public ArrayList<MarketSession> marketSessionsList=new ArrayList<MarketSession>();
	public double priceEarnedInLatestMarketSession,quantitySoldInLatestMarketSession;
	public String varietySoldInLatestMarketSession;
	int timeOfFirstProduction=1;
	int initialProduction,stock,numberOfMarkets,totalMarketSessions,remainingMarketSessions,offerInThisSession,production;


	public Producer(String producerName,double producerLatitude,double producerLongitude,double producerProductionShare,String producerMarkets,String producerVarieties,ArrayList<Double> possiblePrices){
		name=producerName;
		latitude=producerLatitude;
		longitude=producerLongitude;
		productionShare=producerProductionShare;
		sizeInGuiDisplay=productionShare*100;
		markets=producerMarkets;
		String[] partsTmpMarkets=markets.split("\\|");
		numberOfMarkets=partsTmpMarkets.length;
		varieties=producerVarieties;
		if(Cms_builder.verboseFlag){System.out.println("Created producer: "+name+", latitude: "+latitude+", longitude: "+longitude);}
		if(Cms_builder.verboseFlag){System.out.println("        sells in "+numberOfMarkets+" market(s): "+markets);}
		if(Cms_builder.verboseFlag){System.out.println("        produces: "+varieties);}
		supplyPrices=possiblePrices;
	}

	public void stepExportAllowedFlag(){
			if(RandomHelper.nextDouble()<Cms_builder.probabilityToAllowExport){
			exportAllowed=true;
		}
		else{
			exportAllowed=false;
		}
			if(Cms_builder.verboseFlag){System.out.println("         producer:    "+name+" exportAllowed "+exportAllowed);}

	}

	public ArrayList<ElementOfSupplyOrDemandCurve> getSupplyCurve(String theVariety){
		offerInThisSession=(int)stock/remainingMarketSessions;
		supplyCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
		for(Double aPrice : supplyPrices){
			supplyCurve.add(new ElementOfSupplyOrDemandCurve(aPrice,(double)offerInThisSession));
		}
		if(Cms_builder.verboseFlag){System.out.println("           "+name+" stock "+stock+" remaining market sessions "+remainingMarketSessions);}
		if(Cms_builder.verboseFlag){System.out.println("           supply curve sent to market by "+name+" for product "+theVariety+" (some points)");}
		return supplyCurve;
	}
	public void setQuantitySoldInLatestMarketSession(String theVariety, double marketPrice, double soldQuantity){
		varietySoldInLatestMarketSession=theVariety;
		priceEarnedInLatestMarketSession=marketPrice;
		quantitySoldInLatestMarketSession=soldQuantity;
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" state before session stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" price "+priceEarnedInLatestMarketSession+" quantity sold in this session "+quantitySoldInLatestMarketSession+" of "+varietySoldInLatestMarketSession);}
			stock+=-quantitySoldInLatestMarketSession;
			remainingMarketSessions--;
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" state after session stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
	}

	public void makeProduction(){
			if(Cms_builder.verboseFlag){System.out.println(name+" state before production stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
			production=(new Double(initialProduction*(1+(RandomHelper.nextDouble()*2-1.0)*Cms_builder.productionRateOfChangeControl))).intValue();
			stock+=production;
			remainingMarketSessions=totalMarketSessions;
		if(Cms_builder.verboseFlag){System.out.println(name+" production realized: "+production);}
			if(Cms_builder.verboseFlag){System.out.println(name+" state after production stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
	}
	public void setup(int producerTimeOfFirstProduction){
		timeOfFirstProduction=producerTimeOfFirstProduction;
		initialProduction=(int)(productionShare*Cms_builder.globalProduction);
		stock=(int)(productionShare*Cms_builder.globalProduction*((double)timeOfFirstProduction/Cms_builder.productionCycleLength));
		totalMarketSessions=numberOfMarkets*Cms_builder.productionCycleLength;
		remainingMarketSessions=numberOfMarkets*timeOfFirstProduction;
		if(Cms_builder.verboseFlag){System.out.println("        time of First production "+timeOfFirstProduction+" production "+(int)(productionShare*Cms_builder.globalProduction)+" stock "+stock+" total market sessions "+totalMarketSessions+" remaining market sessions "+remainingMarketSessions);}
	}
	public void addMarketSession(MarketSession aNewMarketSession){
		marketSessionsList.add(aNewMarketSession);
		if(Cms_builder.verboseFlag){System.out.println("     producer "+name+" market Session added");}
	}
	public ArrayList<MarketSession> getMarkeSessions(){
		return marketSessionsList;
	}
	public String getName(){
		return name;
	}
	public String getMarkets(){
		return markets;
	}
	public String getVarieties(){
		return varieties;
	}
	public double getLatitude(){
		return latitude;
	}
	public double getLongitude(){
		return longitude;
	}
	public double getProductionShare(){
		return productionShare;
	}
	public double getSizeInGuiDisplay(){
		return sizeInGuiDisplay;
	}
	public boolean getExportAllowerFlag(){
		return exportAllowed;
	}
	public int getTimeOfFirstProduction(){
		return timeOfFirstProduction;
	}
	public int getStock(){
		return stock;
	}
	public int getProduction(){
		return production;
	}

}
