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

package cms.dynamics;

import cms.Cms_builder;
import cms.agents.Producer;

import repast.simphony.context.Context;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.engine.schedule.DefaultActionFactory;
import repast.simphony.engine.schedule.IAction;
/**
 * The class schedules the events of the main loop.
 * @author giulioni
 *
 */
public class Cms_scheduler{
	public IndexedIterable<Object> buyersList,producersList,marketsList;
	public Context<Object> cmsContext;
	ScheduleParameters scheduleParameters;
	DefaultActionFactory statActionFactory;
	IAction statAction;
	Producer aProducer;

//	public int productionFreq=10;

	public Cms_scheduler(Context<Object> theContext){
		cmsContext=theContext;
		try{
			buyersList=cmsContext.getObjects(Class.forName("cms.agents.Buyer"));
			producersList=cmsContext.getObjects(Class.forName("cms.agents.Producer"));
			marketsList=cmsContext.getObjects(Class.forName("cms.agents.Market"));
		}
		catch(ClassNotFoundException e){
			System.out.println("Class not found");
		}
		statActionFactory = new DefaultActionFactory();

	}
	
	/**
	 * this method calls all the other schedule method of this class.
	 */

	public void scheduleEvents(){

		if(Cms_builder.verboseFlag){
			scheduleParameters=ScheduleParameters.createRepeating(1,1,100.0);
			Cms_builder.schedule.schedule(scheduleParameters,this,"schedulePrintStartSimulationTimeStep");
			scheduleParameters=ScheduleParameters.createRepeating(1,1,0.0);
			Cms_builder.schedule.schedule(scheduleParameters,this,"schedulePrintEndSimulationTimeStep");
		}

		scheduleParameters=ScheduleParameters.createRepeating(1,Cms_builder.importPolicyDecisionInterval,40.0);
		Cms_builder.schedule.schedule(scheduleParameters,this,"scheduleStepBuyersImportPolicy");

		scheduleParameters=ScheduleParameters.createRepeating(1,Cms_builder.exportPolicyDecisionInterval,39.0);
		Cms_builder.schedule.schedule(scheduleParameters,this,"scheduleStepProducersExportPolicy");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,38.0);
		Cms_builder.schedule.schedule(scheduleParameters,this,"scheduleBuyersStepBuyingStrategy");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,37.0);
		Cms_builder.schedule.schedule(scheduleParameters,this,"scheduleMarketsPerformSessions");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,36.0);
		Cms_builder.schedule.schedule(scheduleParameters,this,"scheduleBuyersAccountConsumption");

		scheduleParameters=ScheduleParameters.createRepeating(1,1,34.0);
		Cms_builder.schedule.schedule(scheduleParameters,this,"schedulePrintProductionIfVerbouse");
		for(int i=0;i<producersList.size();i++){
			aProducer=(Producer)producersList.get(i);
			scheduleParameters=ScheduleParameters.createRepeating(aProducer.getTimeOfFirstProduction(),Cms_builder.productionCycleLength,33.0);
			Cms_builder.schedule.schedule(scheduleParameters,aProducer,"makeProduction");
		}
	}

	public void schedulePrintStartSimulationTimeStep(){
		System.out.println();
		System.out.println("===================================================================");
		System.out.println("START SIMULATION TIME STEP: "+RepastEssentials.GetTickCount());
		System.out.println("====================================================================");
		System.out.println();
	}

	public void schedulePrintEndSimulationTimeStep(){
		System.out.println();
		System.out.println("===================================================================");
		System.out.println("END SIMULATION TIME STEP: "+RepastEssentials.GetTickCount());
		System.out.println("====================================================================");
		System.out.println();
	}
	public void scheduleStepBuyersImportPolicy(){
		if(Cms_builder.verboseFlag){
			System.out.println();
			System.out.println("BUYERS: STEP IMPORT POLICY");
		}
		statAction=statActionFactory.createActionForIterable(buyersList,"stepImportAllowedFlag",false);
		statAction.execute();
	}
	public void scheduleStepProducersExportPolicy(){
		if(Cms_builder.verboseFlag){
			System.out.println();
			System.out.println("PRODUCERS: STEP EXPORT POLICY");
		}
		statAction=statActionFactory.createActionForIterable(producersList,"stepExportAllowedFlag",false);
		statAction.execute();
	}
	public void scheduleBuyersStepBuyingStrategy(){
		if(Cms_builder.verboseFlag){
			System.out.println();
			System.out.println("BUYERS: STEP BUYING STRATEGY");
		}
		statAction=statActionFactory.createActionForIterable(buyersList,"stepBuyingStrategy",false,producersList);
		statAction.execute();
	}

	public void scheduleMarketsPerformSessions(){
		if(Cms_builder.verboseFlag){
			System.out.println();
			System.out.println("MARKETS: PERFORM SESSIONS");
		}
		statAction=statActionFactory.createActionForIterable(marketsList,"performMarketSessions",false);
		statAction.execute();
	}
	public void scheduleBuyersAccountConsumption(){
		if(Cms_builder.verboseFlag){
			System.out.println();
			System.out.println("BUYERS: ACCOUNT CONSUMPTION");
		}
		statAction=statActionFactory.createActionForIterable(buyersList,"accountConsumption",false);
		statAction.execute();
	}

	public void schedulePrintProductionIfVerbouse(){
		if(Cms_builder.verboseFlag){
			System.out.println();
			System.out.println("PRODUCERS: MAKE PRODUCTION");
		}
	}


}
