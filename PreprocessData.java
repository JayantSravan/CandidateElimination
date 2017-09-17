import java.io.*;
import java.util.ArrayList;

public class PreprocessData
{
	String classListFile;
	String rawDataSetFile;
	String processedDataFile;
	String line;
	String[] lineSplit;
	BufferedReader bfr;
	
	public PreprocessData(String classListFile, String rawDataSetFile,String outputDataSetFile)
	{
		this.classListFile = classListFile;
		this.rawDataSetFile = rawDataSetFile;
		this.processedDataFile = outputDataSetFile;
		try
		{
			bfr = new BufferedReader(new FileReader(classListFile));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}		
	
	
	public static void main(String[] args) {
		PreprocessData P = new PreprocessData("C://Users//SUBHADIP JANA//Desktop//classList.txt","C://Users//SUBHADIP JANA//Desktop//candidateDataSet.txt","C://Users//SUBHADIP JANA//Desktop//newData.txt");
		try{
		P.modifyDataSet();
		P.createDataPointsList();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}		
	}
	

	public void modifyDataSet() throws IOException
	{
		bfr = new BufferedReader(new FileReader(rawDataSetFile));
		FileWriter fw = new FileWriter(processedDataFile);		
		while((line=bfr.readLine())!=null)
		{
			lineSplit = line.split(",");
			lineSplit[0] = "";
			fw.write(line.substring(line.indexOf(',')+1) + "\n");			
		}
		fw.close();
	}
	
	public String[][] createDataPointsList() throws IOException
	{
		ArrayList<String[]> dataPoints_ArrayList = new ArrayList<String[]>();
		bfr = new BufferedReader(new FileReader(processedDataFile));
		
		while((line=bfr.readLine())!=null)
		{
			dataPoints_ArrayList.add(line.split(","));
		}
		
		return dataPoints_ArrayList.toArray(new String[0][0]);
	}

}
