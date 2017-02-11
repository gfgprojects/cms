package cms.agents;

import cms.Cms_builder;
import repast.simphony.random.RandomHelper;
public class ElNino {
double anomaly;

public ElNino(){

anomaly=1.0;
if(Cms_builder.verboseFlag){
	System.out.println("Created ElNino");
}
}
//@ScheduledMethod(start=1,interval=1,priority=2)
public void move(){
/*
if(RandomHelper.nextDouble()>0.5){
position++;
}
else{
position--;
}
*/
anomaly=RandomHelper.nextDouble()+0.5;
System.out.println(anomaly);
}
/*
public int getPosition(){
	return position;
}
*/
public double getAnomaly(){
	return anomaly;
}

}
