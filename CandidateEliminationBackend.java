import java.util.*;
import java.io.*;

public class CandidateEliminationBackend
{
	static ArrayList<String[]> specificBoundary = new ArrayList<String[]>();
	static ArrayList<String[]> genericBoundary = new ArrayList<String[]>();
	static String[][] dataSet; 
	static String[][] attributeRange = {{"0","1"},
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

	public static void main(String[] args)
	{
		//specificBoundary.add(new String[]{"~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"});
		CandidateEliminationBackend C = new CandidateEliminationBackend();
		try
		{				
			dataSet = new PreprocessData("/classList.txt","candidateDataSet.txt","newData.txt").createDataPointsList();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		System.out.println(dataSet[0].toString());
	}
/************************************************************************************************************/
    
    
    public static void modifiedSpecificBoundry(String[] specific)
    {
        if(specificBoundry.size()==0)
        {
            specificBoundry.add(specific);
            
        }
        else
        {
            String []temp=specificBoundry.get(0);
            for(int i=0;i<attributeRange.length-1;i++)
            {
                if(!(temp[i].equals(check[i])))
                {
                    temp[i]="?";
                }
            }
            specificBoundry.remove(0);
            specificBoundry.add(temp);
        }
    } 
/************************************************************************************************************/






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
///Specialize General Boundary given the negative data string
public void specializeGenericBoundary(ArrayList<String[]> genericBoundary, String[] data)
{
	ArrayList<String[]> bufferArray = new ArrayList<String[]>;
	for(String[] genericHypothesis : genericBoundary)
	{
		if(checkIfConsistentHypothesis(genericHypothesis, data))
			continue;

		int dataIterateVar = 0; //Integer to iterate over data d
		for(String attributeValue : genericHypothesis)
		{

			if(attributeValue.equals("?")) // ? has to be split otherwise go to next string
			{
				for(String attributeRanges : attributeRange[dataIterateVar])
				{
					if(!attributeRanges.equals(data[dataIterateVar]))
					{
						String[] newSpecHyp = genericHypothesis;
						newSpecHyp[dataIterateVar] = attributeRanges;
						//add this to the genericBoundary arrayList
						bufferArray.add(newSpecHyp);
					}
				}
			}

			dataIterateVar++;
		}
		//remove the old Specific Boundary
		genericBoundary.remove(genericHypothesis);
	}
	genericBoundary.addAll(bufferArray);
}

private boolean checkIfConsistentHypothesis(String[] Hypothesis , String[] data)
{
	int dataIterateVal = 0;
	boolean cond = true;
	for(String attributeVal : Hypothesis)
	{
		if(attributeVal.equals("?"))
		{
			continue;
		}
		else if(!attributeVal.equals(data[dataIterateVal]))
		{
			return false;
		}
		dataIterateVal++;
	}
	return cond;
}
//////

/////////////Check if Generic boundary contains elements which are less specific than some other hypothesis in the generic boundary
	///////// and relation between two hypothesis

public int specificGenericRelaionship(String[] hyp1, String[] hyp2)//returns 1 if hyp1 is more generic, -1 if hyp2 is more generic, 0 if not related
{
	for(String att1 : hyp1)
	{
		if(att1.equal("~"))
			return -1;
	}

	for(String att2 : hyp2)
	{
		if(att2.equal("~"))
			return 1;
	}
	int iterator=0;
	int ge = 0;
	int sp = 0;

	for(String att1 : hyp1)
	{
		if(att1.equals("?") && hyp2[iterator].equals("?"))
		{
			iterator++;
			continue;
		}
		else if(att1.equals("?"))
		{
			ge++;
		}
		else if(hyp2[iterator].equals("?"))
		{
			sp++;
		}
		else if(att1.equals(hyp2[iterator]))
		{
			iterator++;
			continue;
		}
		else
		{
			return 0;
		}
		iterator++;
	}
	if(sp>0 && ge>0)
	{
		return 0;
	}
	else if(sp>0)
	{
		return -1;
	}
	else
	{
		return 1;
	}
}

public void removeGenRedundant(ArrayList<String[]> genericBoundary)
{
	for(String[] hypothesis : genericBoundary)
	{
		for(String[] hypothesis2 : genericBoundary)
		{
			if(!hypothesis.equals(hypothesis2))
			{
				int compare = specificGenericRelaionship(hypothesis, hypothesis2);

				if(compare == 1)
				{
					genericBoundary.remove(hypothesis2);
				}
				else if(compare==-1)
				{
					genericBoundary.remove(hypothesis);
				}
			}
		}
	}
}

//////////