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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Contract{
double price,quantity,transportCost,pricePlusTransport,priceMinusTransport;
String market,producer,buyer;

public Contract(String thisContractMarket,String thisContractProducer,String thisContractBuyer,double thisContractPrice,double thisContractTransportCost,double thisContractQuantity){
	market=thisContractMarket;
	producer=thisContractProducer;
	buyer=thisContractBuyer;
	price=thisContractPrice;
	transportCost=thisContractTransportCost;
	pricePlusTransport=(new BigDecimal(price+transportCost).setScale(2,RoundingMode.HALF_EVEN)).doubleValue();
	priceMinusTransport=(new BigDecimal(price-transportCost).setScale(2,RoundingMode.HALF_EVEN)).doubleValue();
	quantity=thisContractQuantity;
	if(Cms_builder.verboseFlag){System.out.println("           "+buyer+" contract stored, market: "+market+", producer: "+producer+", price: "+price+", transport cost "+transportCost+", quantity: "+quantity);}
}


public double getPrice(){
	return price;
}
public double getPricePlusTransport(){
	return pricePlusTransport;
}
public double getPriceMinusTransport(){
	return priceMinusTransport;
}
public double getQuantity(){
	return quantity;
}
public String getMarketName(){
	return market;
}
public String getProducerName(){
	return producer;
}
public String getBuyer(){
	return buyer;
}
}
