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

package cms.utils;

import cms.Cms_builder;


public class DemandFunctionParameters{
	int intercept;
	String market,producer;

	public DemandFunctionParameters(int theIntercept,String theMarket,String theProducer){
		intercept=theIntercept;
		market=theMarket;
		producer=theProducer;
		if(Cms_builder.verboseFlag){System.out.println("          new parameters; demand at p=0: "+intercept+", market: "+market+", producer: "+producer);}
	}

	public void setIntercept(int theNewIntercept){
		intercept=theNewIntercept;
		if(Cms_builder.verboseFlag){System.out.println("                 demand at p=0 set to "+theNewIntercept+", market: "+market+", producer: "+producer);}
	}
	public void increaseInterceptBy(int interceptIncrease){
		intercept+=interceptIncrease;
		if(Cms_builder.verboseFlag){System.out.println("                 demand increased by "+interceptIncrease+", market: "+market+", producer: "+producer);}
	}
	public void decreaseInterceptBy(int interceptDecrease){
		intercept+=-interceptDecrease;
		if(Cms_builder.verboseFlag){System.out.println("                 demand decreased by "+interceptDecrease+", market: "+market+", producer: "+producer);}
	}

	public int getIntercept(){
		return intercept;
	}
	public String getMarketName(){
		return market;
	}
	public String getProducerName(){
		return producer;
	}

}
