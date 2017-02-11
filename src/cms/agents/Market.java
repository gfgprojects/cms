package cms.agents;
import cms.Cms_builder;
import cms.agents.MarketSession;
import cms.agents.Producer;

import java.util.ArrayList;

import repast.simphony.context.Context;

public class Market {
	public String name;
	public double latitude,longitude,exchangesShare,sizeInGuiDisplay;
	public ArrayList<MarketSession> marketSessionsList=new ArrayList<MarketSession>();
	MarketSession aMarketSession;

	public Market(String marketName,double marketLatitude,double marketLongitude,double marketExchangesShare){
		name=marketName;
		latitude=marketLatitude;
		longitude=marketLongitude;
		exchangesShare=marketExchangesShare;
		sizeInGuiDisplay=exchangesShare*100;
		if(Cms_builder.verboseFlag){System.out.println("Created market:   "+name+", latitude: "+latitude+", longitude: "+longitude);}
	}
	public void addMarketSessions(Producer producer,String varietyNames,Context<Object> theContext,ArrayList<Double> possiblePrices){

		String[] aProducerTmpVarieties=varietyNames.split("\\|");
		for(int i =0;i<aProducerTmpVarieties.length;i++){
			aMarketSession=new MarketSession(name,producer,aProducerTmpVarieties[i],theContext,possiblePrices);
			theContext.add(aMarketSession);
			marketSessionsList.add(aMarketSession);
			producer.addMarketSession(aMarketSession);
		}
		if(Cms_builder.verboseFlag){System.out.println("     market: "+name+", number of sessions till now: "+marketSessionsList.size());}
	}	
	public void performMarketSessions(){
	if(Cms_builder.verboseFlag){System.out.println("    --------------------------------------------------------------");}
		if(Cms_builder.verboseFlag){System.out.println("    "+name+" open");}
		for(MarketSession aSession : marketSessionsList){
			aSession.openSession();
		}
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
	public double getExchangesShare(){
		return exchangesShare;
	}
	public double getSizeInGuiDisplay(){
		return sizeInGuiDisplay;
	}
	public ArrayList<MarketSession> getMarketSessions(){
		return marketSessionsList;
	} 

}
