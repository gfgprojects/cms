/*
Copyright 2017 Gianfranco Giulioni

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
import cms.Cms_builder;
import cms.agents.MarketSession;
import cms.agents.Producer;

import java.util.ArrayList;

import repast.simphony.context.Context;
/**
 * A market class is a container for the various market sessions
 * @author Gianfranco Giulioni
 *
 */
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
