/*
Copyright 2018 Gianfranco Giulioni

This file is part of the Commodity Market Simulator (CMS):

    CMS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CMS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package cms.agents;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import cms.Cms_builder;
import cms.utils.ElementOfSupplyOrDemandCurve;
import cms.agents.MarketSession;

import java.util.ArrayList;
/**
 * The Producer class hold all the relevant variable for a producer; It has methods for performing the producer's actions. The realization of production and the decision on the quantity offered in each market session are of particular importance.  
 * @author Gianfranco Giulioni
 *
 */
public class Producer {
	public String name,markets,varieties;
	public double latitude,longitude,productionShare,sizeInGuiDisplay;
	public boolean exportAllowed=true;
	public ArrayList<Double> supplyPrices=new ArrayList<Double>();
	public ArrayList<Double> marketSessionsPricesRecord=new ArrayList<Double>();
	public ArrayList<ElementOfSupplyOrDemandCurve> supplyCurve=new ArrayList<ElementOfSupplyOrDemandCurve>();
	public ArrayList<MarketSession> marketSessionsList=new ArrayList<MarketSession>();
	public double priceEarnedInLatestMarketSession,quantitySoldInLatestMarketSession;
	public String varietySoldInLatestMarketSession;
	int timeOfFirstProduction=1;
	int initialProduction,targetProduction,stock,numberOfMarkets,totalMarketSessions,remainingMarketSessions,offerInThisSession,production;
	double sumOfSellingPrices,averageSellingPrice;

/**
 * The Cms_builder calls the constructor giving as parameters the values found in a line of the producers.csv file located in the data folder.
 *<br>
 *The format of each line is the following:
 *<br>
 *name,ISO3code,latitude,longitude,productionShare,listOfMarkets,listOfProducedVarietyes,listOfPossiblePrices,timeOfProduction
 *<br>
 *the production share is the ratio between the producer production and the total production of the commodity.
 *<br>
 *When the producer sells in more than one market, the market names are separated by the | character in the listOfMarkets
 *<br>
 *When the producer makes more than one variety, the varieties names are separated by the | character in the listOfProducedVarieties. However, the present version of the model does not handle multiple products and the user should modify the code to achieve this result.
 *<br>
 *Note that the last element of the line is not used in this constructor. It is used by the setup method.
 *<br>
 *example:
 *<br>
 *China,39.9390731,120.1172706,0.05,market1|market 2,variety 1|variety 2,2 
 *<br>
 *This line gives the geographic coordinates of China and says that this country sells in two markets, makes two variety of the product and obtain the production in the second period (if periods corresponds to month, it realizes the production in February)
 * @param producerName name of the producer
 * @param producerLatitude the latitude
 * @param producerLongitude the longitude
 * @param producerProductionShare share of the total production produced by this producer
 * @param producerMarkets the list of markets in which the producer sells
 * @param producerVarieties the list of varieties produced by the producer
 * @param possiblePrices prices to compute the supply curve
 */
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
		marketSessionsPricesRecord.add(new Double(priceEarnedInLatestMarketSession));
		if(marketSessionsPricesRecord.size()>Cms_builder.producersPricesMemoryLength){
			marketSessionsPricesRecord.remove(0);
		}
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" state before session stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" price "+priceEarnedInLatestMarketSession+" quantity sold in this session "+quantitySoldInLatestMarketSession+" of "+varietySoldInLatestMarketSession);}
			stock+=-quantitySoldInLatestMarketSession;
			remainingMarketSessions--;
			if(Cms_builder.verboseFlag){System.out.println("           "+name+" state after session stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
	}

	public void makeProduction(){
			if(Cms_builder.verboseFlag){System.out.println(name+" state before production stock: "+stock+" remaining sessions: "+remainingMarketSessions);}
production=(new Double(targetProduction*(1+(RandomHelper.nextDouble()*2-1.0)*Cms_builder.productionRateOfChangeControl))).intValue();
/*
			if(RepastEssentials.GetTickCount()>119 && RepastEssentials.GetTickCount()<123){
				production=(new Double(targetProduction*(1-Cms_builder.productionRateOfChangeControl))).intValue();				
			}
			if(RepastEssentials.GetTickCount()>131 && RepastEssentials.GetTickCount()<135){
				production=targetProduction;				
			}
*/
			stock+=production;
			remainingMarketSessions=totalMarketSessions;
		if(Cms_builder.verboseFlag){System.out.println(name+" production realized: "+production);}
		if(Cms_builder.verboseFlag){System.out.println(name+" state after production stock: "+stock+" remaining sessions: "+remainingMarketSessions);}

		//plan production for the next period
		if(marketSessionsPricesRecord.size()==Cms_builder.producersPricesMemoryLength){
			if(Cms_builder.verboseFlag){System.out.println(name+" set target production for next production cycle");}
			//compute average price
			sumOfSellingPrices=0;
			for(Double tmpDouble : marketSessionsPricesRecord){
				sumOfSellingPrices+=tmpDouble;
			}
			averageSellingPrice=sumOfSellingPrices/marketSessionsPricesRecord.size();
			//increase production if average selling price higher than threshold
			if(averageSellingPrice>Cms_builder.priceThresholdToIncreaseTargetProduction){
				targetProduction=(int)(targetProduction*(1+Cms_builder.percentageChangeInTargetProduction));
				if(Cms_builder.verboseFlag){System.out.println(name+" target production increased to "+targetProduction);}
			}
			//increase production if average selling price higher than threshold
			if(averageSellingPrice<Cms_builder.priceThresholdToDecreaseTargetProduction){
				targetProduction=(int)(targetProduction*(1+Cms_builder.percentageChangeInTargetProduction));
				if(Cms_builder.verboseFlag){System.out.println(name+" target production decreased to "+targetProduction);}
			}
		}
		else{
			averageSellingPrice=0;
			if(Cms_builder.verboseFlag){System.out.println(name+" there are not enough data to set target production for next production cycle");}
		}
		
	}
	public void setup(int producerTimeOfFirstProduction){
		timeOfFirstProduction=producerTimeOfFirstProduction;
		initialProduction=(int)(productionShare*Cms_builder.globalProduction);
		targetProduction=initialProduction;
		production=targetProduction;
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
	public int getTargetProduction(){
		return targetProduction;
	}
	public double getAverageSellingPrice(){
		return averageSellingPrice;
	}


}
