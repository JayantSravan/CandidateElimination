public class Datapoint
{
	Datapoint(String name, boolean hair, boolean feathers, boolean eggs,boolean milk, boolean airborne, boolean aquatic, boolean predator,boolean toothed, boolean backbone, boolean breathes, boolean venomous,boolean fins, int legs,boolean tail,boolean domestic,boolean catsize, int type)
	{
		this.name= name;
		this.hair= hair;
		this.feathers= feathers;
		this.eggs= eggs;
		this.milk= milk;
		this.airborne= airborne;
		this.aquatic= aquatic;
		this.predator= predator;
		this.toothed= toothed;
		this.backbone= backbone;
		this.breathes= breathes;
		this.venomous= venomous;
		this.fins= fins;
		this.legs= legs;
		this.tail= tail;
		this.domestic= domestic;
	}
	String name;
	boolean hair;
	boolean feathers;
	boolean eggs;
	boolean milk;
	boolean airborne;
	boolean aquatic;
	boolean predator;
	boolean toothed;
	boolean backbone;
	boolean breathes;
	boolean venomous;
	boolean fins;
	int legs;
	boolean tail;
	boolean domestic;
	boolean catsize;
	int type;
	
	String[][] attributeRange = {{"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","2","4","5","6","8"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"0","1"},
			                     {"1","2","3","4","5","6","7"}
								};				
	
}
