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
