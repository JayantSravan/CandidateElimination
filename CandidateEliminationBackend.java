import java.util.ArrayList;
import java.io.*;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

//http://www2.cs.uregina.ca/~dbd/cs831/notes/ml/vspace/vs_prob1.html
//https://stackoverflow.com/questions/22625765/candidate-elimination-algorithm

public class CandidateEliminationBackend
{
	ArrayList<String[]> specificBoundary = new ArrayList<String[]>();
	ArrayList<String[]> genericBoundary = new ArrayList<String[]>();	
	String[][] dataSet;
	String outputFile;
	
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

	
public CandidateEliminationBackend(String classNum,String[][] dataSet,String outputFile)
{	
	specificBoundary.add(new String[]{"~","~","~","~","~","~","~","~","~","~","~","~","~","~","~","~"});
	genericBoundary.add(new String[]{"?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?"});
	this.outputFile = outputFile;
	invokeCandidateElimination(classNum, dataSet);		
}

public void invokeCandidateElimination(String classNum, String[][] dataSet)
{
	for(String[] dataRow : dataSet)
	{
		performCandidateElimination(dataRow, classNum);
	}
	try
	{
		writeClassConceptToFile(classNum);
	}
	catch(IOException ioe)
	{
		ioe.printStackTrace();
	}

}

private void performCandidateElimination(String[] dataRow, String classNum)
{	
	if(dataRow[dataRow.length-1].equals(classNum)) generalizeSpecificBoundary(dataRow);		
	if(!dataRow[dataRow.length-1].equals(classNum))	specializeGenericBoundary(genericBoundary, dataRow);
	checkSomeS_moreSpecific_than_G();
	removeRedundantGenericHypothesis();
	checkInconsistency(dataRow, classNum);
}

/**
 * generalize specific boundary
 * @param data
 */
private void generalizeSpecificBoundary(String[] data)
{
	if(specificBoundary.size()==0) return;
	if(specificBoundary.get(0)[0].equals("~"))
    {
		specificBoundary.remove(0);
        specificBoundary.add(data);
    }
    else
    {
       String[] temp=specificBoundary.get(0);
       for(int i=0;i<data.length-1;i++)
       {
    	   if(!(temp[i].equals(data[i])) && (!temp[i].equals("?")))
           {
               temp[i]="?";
           }
       }
       specificBoundary.remove(0);
       specificBoundary.add(temp);
     }
}
    
/**
 * Specialize General Boundary given negative data string
 * @param genericBoundary
 * @param data
 */
private void specializeGenericBoundary(ArrayList<String[]> genericBoundary, String[] data)
{
	ArrayList<String[]> bufferArray = new ArrayList<String[]>();
	ArrayList<String[]> hypothesisToBeRemoved = new ArrayList<String[]>();

	for(String[] genericHypothesis : genericBoundary)
	{
		if(!checkIfConsistentHypothesis(genericHypothesis, data))
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
						String[] newSpecHyp = new String[data.length-1];
						System.arraycopy(genericHypothesis,0,newSpecHyp,0, data.length-1);
						newSpecHyp[dataIterateVar] = attributeRanges;						
						bufferArray.add(newSpecHyp);  //add this to the genericBoundary arrayList
					}
				}
			}
			dataIterateVar++;
		}
		//remove the old Specific Boundary
		hypothesisToBeRemoved.add(genericHypothesis);
	}
	genericBoundary.addAll(bufferArray);
	for(String[] hypothesis : hypothesisToBeRemoved)
	{
		genericBoundary.remove(hypothesis);
	}
}

/**
 * Check if the Hypothesis is consistent with the data 
 * @param Hypothesis
 * @param data
 * @return
 */
private boolean checkIfConsistentHypothesis(String[] hypothesis , String[] data)
{
	int dataIterateVal = -1;
	boolean cond = true;
	for(String attributeVal : hypothesis)
	{
		dataIterateVal++;
		if(attributeVal.equals("?"))
		{
			continue;
		}
		else if(!attributeVal.equals(data[dataIterateVal]))
		{
			return false;
		}		
	}
	return cond;
}


/**
 * Check if Generic boundary contains elements which are less specific than some other hypothesis in the generic boundary
 * and relation between two hypothesis
 * @param hypothesis1
 * @param hypothesis2
 * @return
 */
private int specificGeneric_boundary_Relationship(String[] hypothesis1, String[] hypothesis2)//returns 1 if hyp1 is more generic, -1 if hyp2 is more generic, 0 if not related
{
	for(String attribute1 : hypothesis1)
	{
		if(attribute1.equals("~")) return -1;
	}

	for(String attribute2 : hypothesis2)
	{
		if(attribute2.equals("~"))	return 1;
	}
	int iterator=0;
	int ge = 0;
	int sp = 0;

	for(String att1 : hypothesis1)
	{
		if(att1.equals("?") && hypothesis2[iterator].equals("?"))
		{
			iterator++;
			continue;
		}
		else if(att1.equals("?")) ge++;
	
		else if(hypothesis2[iterator].equals("?")) sp++;
	
		else if(att1.equals(hypothesis2[iterator]))
		{
			iterator++;
			continue;
		}
		else return 0;	
		iterator++;
	}
	if(sp>0 && ge>0) return 0;
	else if(sp>0) return -1;
	else return 1;

}
/**
 * 
 */
private void removeRedundantGenericHypothesis()
{
	if(genericBoundary.size()==0) return;
	ArrayList<String[]> bufferArrayList = new ArrayList<String[]>();
	for(String[] hypothesis : genericBoundary)
	{
		for(String[] hypothesis2 : genericBoundary)
		{
			if(!hypothesis.equals(hypothesis2))
			{
				int compare = specificGeneric_boundary_Relationship(hypothesis, hypothesis2);

				if(compare == 1)
				{
					//genericBoundary.remove(hypothesis2);
					bufferArrayList.add(hypothesis2);
				}
				else if(compare==-1)
				{
					//genericBoundary.remove(hypothesis);
					bufferArrayList.add(hypothesis);
				}
			}
		}
	}

	for(String[] hypothesisToBeRemoved : bufferArrayList)
	{
		genericBoundary.remove(hypothesisToBeRemoved);
	}
}

/**
 * 
 */
private void checkSomeS_moreSpecific_than_G()
{
	if(specificBoundary.size()==0) return;
	ArrayList<String[]> hypothesisToBeRemoved = new ArrayList<String[]>();
	for(String[] genericHypothesis : genericBoundary)
	{
		if(specificGeneric_boundary_Relationship(genericHypothesis, specificBoundary.get(0))!=1) hypothesisToBeRemoved.add(genericHypothesis);
	}
	for(String[] hypothesis : hypothesisToBeRemoved)
	{
		genericBoundary.remove(hypothesis);
	}
}

/**
 * Run this before any specialisation or generalisation.
 * Remove from S any hypothesis inconsistent with d,d being a negative example
 * Remove from G any hypothesis inconsistent with d	,d being a positive eaxmple
 * @param data
 * @param type
 */
public void checkInconsistency(String[] data,String type)
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
private void checkGenericBoundary(String[] data)
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
private void checkSpecificBoundary(String[] data)
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
		if(dataIterateVar==(data.length-1)) specificBoundaryDeleteList.add(specificHypothesis);
	}

	for(String[] specificHypothesis_Delete : specificBoundaryDeleteList)
	{
		specificBoundary.remove(specificHypothesis_Delete);
	}
}

private boolean checkIfConceptCanBeLearned()
{
	if(specificBoundary.size()==0 || genericBoundary.size()==0) return false;
	return true;
}

private void writeClassConceptToFile(String classNum) throws IOException
{
	new FileWriter(outputFile);
	FileWriter fw = new FileWriter(outputFile,true);
	fw.write(classNum + "\n");
	
	if(!checkIfConceptCanBeLearned())
	{
		fw.write("Concept cannot be learnt for class " + classNum + "\n\n");
		fw.close();
		return;
	}
	
	fw.write("Specific Boundary\n");
	for(String[] specificHypothesis : specificBoundary)
	{
		String str = new String();
		for(String attribute : specificHypothesis)
		{
			str = str + attribute + " ";
		}
		str = str.substring(0, str.length()-2);
		fw.write(str + "\n");
	}
	
	fw.write("Generic Boundary\n");
	for(String[] genericHypothesis : genericBoundary)
	{
		String str = new String();
		for(String attribute : genericHypothesis)
		{
			str = str + attribute + " ";
		}
		fw.write(str + "\n");
	}
	fw.write("\n\n");
	fw.close();
}

}
