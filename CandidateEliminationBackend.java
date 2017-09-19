import java.util.*;
import java.io.*;

public class CandidateEliminationBackend
{
	static ArrayList<String[]> specificBoundary = new ArrayList<String[]>();
	static ArrayList<String[]> genericBoundary = new ArrayList<String[]>();

	public static void main(String[] args) {

	}








	/**
	 * Run this before any specialisation or generalisation.
	 * Remove from S any hypothesis inconsistent with d,d being a negative example
	 * Remove from G any hypothesis inconsistent with d	,d being a positive eaxmple
	 * @param data
	 * @param type
	 */
	public static void checkInconsistency(String[] data,String type)
	{
		if(data[data.length-1].equals(type))  // positive example, last value in dataset d is the class type
		{
			checkGenericBoundary(data);
		}
		else  //negative example
		{
			checkSpecificBoundary( data);
		}
	}

	/**
	 * Check GenericBoundary for positive examples
	 * @param data
	 */
	private static void checkGenericBoundary(String[] data)
	{
		ArrayList<String[]> genericBoundaryDeleteList = new ArrayList<String[]>();  //stores the hypothesis to be deleted
		for(String[] genericHypothesis : genericBoundary)
		{
			int dataInterateVar = 0;  // Integer iterator to iterate over the dataset d
			for(String attributeValue : genericHypothesis)
			{
				if(!attributeValue.equals("?"))
				{
					if(!attributeValue.equals(data[dataInterateVar]))
					{
						genericBoundaryDeleteList.add(genericHypothesis);
						break;
					}
				}
				dataInterateVar++;
			}
		}

		for(String[] genericHypothesis_Delete : genericBoundaryDeleteList)
		{
			genericBoundary.remove(genericHypothesis_Delete);
		}
	}

	/**
	 * Check specific Boundary for negative examples
	 * @param data
	 */
	private static void checkSpecificBoundary(String[] data)
	{
		ArrayList<String[]> specificBoundaryDeleteList = new ArrayList<String[]>();  //stores the hypothesis to be deleted

		for(String[] specificHypothesis : specificBoundary)
		{
			int dataIterateVar = 0;  // Integer iterator to iterate over the dataset d
			for(String attributeValue : specificHypothesis)
			{
				if(!attributeValue.equals("?") && !attributeValue.equals(data[dataIterateVar])) break;
				dataIterateVar++;
			}
			if(dataIterateVar==data.length) specificBoundaryDeleteList.add(specificHypothesis);
		}

		for(String[] specificHypothesis_Delete : specificBoundaryDeleteList)
		{
			genericBoundary.remove(specificHypothesis_Delete);
		}
	}
}
