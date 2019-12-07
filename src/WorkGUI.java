import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import javax.swing.JFileChooser;

import java.io.*;
import java.util.*;
import javax.swing.JButton;

public class WorkGUI
{

	protected Shell shlGradeAnalyzer;
	private Text trackErrors;
	
	//Stuff for file operations and analyzing the numbers
	//TODO: Need to have the program store the values in here instead of within the method
	private BufferedReader scan;
	private int numOfEntries = 0;
	private int highestVal = 0;
	private int lowestVal = 0;
	private int mean, median, mode;
	private ArrayList<Float> entries = new ArrayList<Float>();
	private Text dataDisp;
	private Text showAnalysis;
	private Text showGraph;
	private float lowBoundVal = 0f;
	private float highBoundVal = 100f;
	/**
	 * @wbp.nonvisual location=329,171
	 */

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			WorkGUI window = new WorkGUI();
			window.open();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shlGradeAnalyzer.open();
		shlGradeAnalyzer.layout();
		while (!shlGradeAnalyzer.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		
		shlGradeAnalyzer = new Shell();
		shlGradeAnalyzer.setBackground(SWTResourceManager.getColor(160, 82, 45));
		shlGradeAnalyzer.setSize(1122, 737);
		shlGradeAnalyzer.setText("Grade Analyzer");
		
		//Moved the text box blocks above their button blocks in the code for visual clarity
		Text dataToAdd = new Text(shlGradeAnalyzer, SWT.BORDER);
		dataToAdd.setBounds(20, 21, 94, 19);
		
		Button addData = new Button(shlGradeAnalyzer, SWT.NONE);
		addData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					float addOn = Float.parseFloat(dataToAdd.getText());
					
					if((addOn >= lowBoundVal) && (addOn <= highBoundVal))
						entries.add(addOn);
					else
						trackErrors.append("Out of Bounds Error: Data entered is NOT within the set boundaries\n");
					
					//TODO: Removes the text from the button...
					//dataToAdd.setText("");
					//TODO: Only need to display error message, these logs need to go into the report string
					//trackErrors.append("Data entered from KEYBOARD succesfully\n");
				}
				catch (NumberFormatException g)
				{
					trackErrors.append("Conversion Error: Data entered is NOT a number\n");
				}
			}
		});
		addData.setBounds(10, 46, 117, 27);
		addData.setText("Add Value");
		
		//Moved the text box blocks above their button blocks in the code for visual clarity
		Text dataToKill = new Text(shlGradeAnalyzer, SWT.BORDER);
		dataToKill.setBounds(138, 21, 94, 19);
		
		Button deleteData = new Button(shlGradeAnalyzer, SWT.NONE);
		deleteData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					float erase = Float.parseFloat(dataToKill.getText());
					
					for (int d = 0; d < entries.size(); d++)
					{
						if (entries.get(d) == erase)
						{
							//TODO: This is only supposed to delete the first instance of the value and thats all
							entries.remove(d);
							d = entries.size();
						}
					}
					
					//TODO: Only need to display error message, these logs need to go into the report string
					//trackErrors.append("The entry " + erase + " was successfully removed\n");
					//TODO: This gets rid of the button text
					//deleteData.setText("");
				}
				catch (NumberFormatException x)
				{
					trackErrors.append("Conversion Error: Data to delete is NOT a number\n");
				}
			}
		});
		deleteData.setBounds(130, 46, 112, 27);
		deleteData.setText("Delete Value");
		
		//TODO: Need something to show the user the left box is the lower bound box, and the right is the upper bound box
		Text lowBound = new Text(shlGradeAnalyzer, SWT.BORDER);
		lowBound.setBounds(251, 21, 72, 19);
		lowBound.setText("0");
		
		Text highBound = new Text(shlGradeAnalyzer, SWT.BORDER);
		highBound.setBounds(329, 21, 72, 19);
		highBound.setText("100");
		
		Button setBounds = new Button(shlGradeAnalyzer, SWT.NONE);
		setBounds.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				String lowBoundText = lowBound.getText();
				String highBoundText = highBound.getText();
				
				if((!lowBoundText.isEmpty()) && (!highBoundText.isEmpty())) {
					try
					{
						float lowBoundTemp = Float.parseFloat(lowBoundText);
						float highBoundTemp = Float.parseFloat(highBoundText);
						
						if(lowBoundTemp <= highBoundTemp) {
							lowBoundVal = lowBoundTemp;
							highBoundVal = highBoundTemp;
									
							for (int d = 0; d < entries.size(); d++)
							{
								if ((entries.get(d) < lowBoundVal) || (entries.get(d) > highBoundVal))
								{
									entries.remove(d);
									d -= 1;
								}
							}
						}
						else
							trackErrors.append("Boundary Values Error: Please make sure the lower\nboundary value is less than or equal to the upper\nboundary value\n");
					}
					catch (NumberFormatException x)
					{
						trackErrors.append("Conversion Error: Desired boundaries must ONLY\nbe numbers\n");
					}
				}
				else {
					if(lowBoundText.isEmpty())
						trackErrors.append("Lower Bound Blank: Please enter a value for the\nlower bound before trying to set the boundaries\n");
					
					if(highBoundText.isEmpty())
						trackErrors.append("Upper Bound Blank: Please enter a value for the\nupper bound before trying to set the boundaries\n");
				}
			}
		});
		setBounds.setBounds(261, 46, 124, 27);
		setBounds.setText("Set Bounds");
		
		Button loadData = new Button(shlGradeAnalyzer, SWT.NONE);
		loadData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String[] exts = {"*.txt" , "*.csv"};
				FileDialog dialog = new FileDialog(shlGradeAnalyzer, SWT.NULL);
				dialog.setFilterExtensions(exts);
	  			String location = dialog.open();
	  			
	  			if (location != null) 
	  			{
		  			File input = new File(location);
		  			if (input.isFile())
		  			{
		  				entries.clear();
		  				int error = 0;
		  				try
		  				{
		  					Scanner track = new Scanner(input);
		  					
		  					while (track.hasNext())
		  					{
		  						try
		  						{
		  							String content = track.nextLine();
		  							
		  							if (content.contains(","))
		  							{
		  								String[] comSplit = content.split(",");
		  								
		  								
		  								for (int j = 0; j < comSplit.length; j++)
		  								{
		  									//TODO: Why is this empty
		  								}
		  							}
		  							else
		  							{
		  								float addNum = Float.parseFloat(content);
		  								if((addNum >= lowBoundVal) && (addNum <= highBoundVal))
		  									entries.add(addNum);
		  							}
		  							
		  						}
		  						catch (NumberFormatException xz)
		  						{
		  							error = 1;
		  							trackErrors.append("Input Error: Some of the data entered was NOT a number\n");
		  						}
		  					}
		  					
		  					if (error != 1)
		  					{
		  						trackErrors.setText("");
		  					}
		  					track.close();
		  				}
		  				catch (FileNotFoundException eff)
		  				{
		  					trackErrors.append("File Error: The file could not be found\n");
		  				}
		  			}
		  			else
		  			{
		 				System.out.print("File is NOT there\n");
		  			}

	  			}
				
				
			}
		});
		loadData.setBounds(20, 546, 141, 37);
		loadData.setText("Load from File");
		
		Button appendData = new Button(shlGradeAnalyzer, SWT.NONE);
		appendData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String[] exts = {"*.txt" , "*.csv"};
				FileDialog dialog = new FileDialog(shlGradeAnalyzer, SWT.NULL);
				dialog.setFilterExtensions(exts);
	  			String location = dialog.open();
	  			
	  			if (location != null) 
	  			{
		  			File input = new File(location);
		  			if (input.isFile())
		  			{
		  				int error = 0;
		  				try
		  				{
		  					Scanner track = new Scanner(input);
		  					
		  					while (track.hasNext())
		  					{
		  						try
		  						{
		  							String content = track.nextLine();
		  							
		  							if (content.contains(","))
		  							{
		  								String[] comSplit = content.split(",");
		  								
		  								
		  								for (int j = 0; j < comSplit.length; j++)
		  								{
		  									//TODO: Why is this empty
		  								}
		  							}
		  							else
		  							{
		  								float addNum = Float.parseFloat(content);
		  								if((addNum >= lowBoundVal) && (addNum <= highBoundVal))
		  									entries.add(addNum);
		  							}
		  							
		  						}
		  						catch (NumberFormatException xz)
		  						{
		  							error = 1;
		  							trackErrors.append("Input Error: Some of the data entered was NOT a number\n");
		  						}
		  					}
		  					
		  					if (error != 1)
		  					{
		  						trackErrors.setText("");
		  					}
		  					track.close();
		  				}
		  				catch (FileNotFoundException eff)
		  				{
		  					trackErrors.append("File Error: The file could not be found\n");
		  				}
		  			}
		  			else
		  			{
		 				System.out.print("File is NOT there\n");
		  			}
	  			}
			}
		});
		appendData.setBounds(196, 546, 141, 37);
		appendData.setText(" Append from File");
		
		Text trackErrors = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		trackErrors.setEditable(false);
		trackErrors.setBackground(SWTResourceManager.getColor(255, 228, 181));
		trackErrors.setBounds(21, 85, 323, 414);
		
		Label lblErrorLog = new Label(shlGradeAnalyzer, SWT.NONE);
		lblErrorLog.setBounds(130, 510, 82, 19);
		lblErrorLog.setText("Error Log");
		
		Text showAnalysis = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		showAnalysis.setEditable(false);
		showAnalysis.setBackground(SWTResourceManager.getColor(255, 228, 181));
		showAnalysis.setBounds(778, 21, 294, 325);
		
		Button analytics = new Button(shlGradeAnalyzer, SWT.NONE);
		analytics.setBounds(922, 361, 160, 37);
		analytics.setText("Analytics");
		analytics.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (entries.isEmpty())
				{
					trackErrors.append("Analytics Error: There is no data to analyze\n");
				}
				else {
					showAnalysis.setText("");
					int numentries = entries.size();
					Collections.sort(entries);
					float min = entries.get(0);
					Collections.reverse(entries);
					float max = entries.get(0);
					float sum = 0;
					for (int i = 0; i < entries.size(); i++){
						sum = sum + entries.get(i);
					}
					//change again
					float mean = sum/entries.size();
					Collections.sort(entries);
					float median;
					if (entries.size()%2 != 0){
						int index = entries.size()/2;
						median = entries.get(index);
					}
					else{
						int index1 = entries.size()/2;
						int index2 = index1 - 1;
						median = (entries.get(index1) + entries.get(index2))/2;
					}
					ArrayList<Float> mode = new ArrayList<Float>();
					Collections.sort(entries);
					
					for (int fuh = 0; fuh < entries.size(); fuh++)
					{
						System.out.print(entries.get(fuh) + " ");
					}
					System.out.print("\n");
					
					for (int i = 1; i < entries.size(); i++){
						
							if (entries.get(i).compareTo(entries.get(i-1)) == 0){
								mode.add(entries.get(i));
							}
						
					}
					System.out.println("List Size: " + mode.size());
					ArrayList<Integer> modeindex = new ArrayList<Integer>();
					if (mode.size() > 0){
						int[] modecount = new int[mode.size()]; 
						for (int i = 0; i < mode.size(); i++){
							for (int j = 0; j < entries.size(); j++){
								if (mode.get(i).compareTo(entries.get(j)) == 0){
									modecount[i] = modecount[i] + 1;
								}
							}
						}
						int maxmode = 0;
						for(int i = 0; i < modecount.length; i++){
							
								if (modecount[i] >= maxmode){
									maxmode = modecount[i];
									modeindex.add(i);
								}
							
						}
						//commit comment

					}
					ArrayList<Float> printmode = new ArrayList<Float>();
					for (int i = 0; i < modeindex.size(); i++) {
						boolean found = false;
						for (int j = 0; j < printmode.size(); j ++) {
							if (mode.get(modeindex.get(i)).compareTo(printmode.get(j)) == 0) {
								found = true;
							}
						}
						if (found == false) {
							printmode.add(mode.get(modeindex.get(i)));
						}
					}
					if(printmode.size() > 0){
						showAnalysis.append("\n Mode(s): ");
						for (int i = 0; i < printmode.size(); i ++){
							showAnalysis.append("\n" + printmode.get(i));
						}
					}
					else{
						showAnalysis.append("\n No entries repeat, every entry is the mode.");
					}
					showAnalysis.append("\n Number of Entries: " + numentries);
					showAnalysis.append("\n Low: " + min);
					showAnalysis.append("\n High: " + max);
					showAnalysis.append("\n Median: " + median);
				}
			}
		});
		
		Button showDist = new Button(shlGradeAnalyzer, SWT.NONE);
		showDist.setBounds(767, 361, 144, 37);
		showDist.setText("Show Distribution");
		showDist.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) 
			{

				float total = entries.size();

				float less0Prec = 0;
				float sum0 = 0;
				float tenPrec = 0;
				float sum1 = 0;
				float twtyPrec = 0;
				float sum2 = 0;
				float tirtPrec = 0;
				float sum3 = 0;
				float fourtPrec = 0;
				float sum4 = 0;
				float fiftPrec = 0;
				float sum5 = 0;
				float sixPrec = 0;
				float sum6 = 0;
				float sevenPrec = 0;
				float sum7 = 0;
				float eightyPrec = 0;
				float sum8 = 0;
				float ninePrec = 0;
				float sum9 = 0;
				float above100Prec = 0;
				float sum10 = 0;
				if (entries.isEmpty())

				{

					trackErrors.append("Analytics Error: There is no data to analyze\n");

				}
				else {
					Collections.sort(entries);
					for (int i = 0; i < entries.size(); i ++){
						if (entries.get(i).compareTo(10.0f) <= 0f){
							sum1 = sum1 + entries.get(i);
							tenPrec++;
						}
						else if (entries.get(i).compareTo(20.0f) <= 0f){
							sum2 = sum2 + entries.get(i);
							twtyPrec++;
						}
						else if (entries.get(i).compareTo(30.0f) <= 0f){
							sum3 = sum3 + entries.get(i);
							tirtPrec++;
						}
						else if (entries.get(i).compareTo(40.0f) <= 0f){
							sum4 = sum4 + entries.get(i);
							fourtPrec++;
						}
						else if (entries.get(i).compareTo(50.0f) <= 0f){
							sum5 = sum5 + entries.get(i);
							fiftPrec++;
						}
						else if (entries.get(i).compareTo(60.0f) <= 0f){
							sum6 = sum6 + entries.get(i);
							sixPrec++;
						}
						else if (entries.get(i).compareTo(70.0f) <= 0f){
							sum7 = sum7 + entries.get(i);
							sevenPrec++;
						}
						else if (entries.get(i).compareTo(80.0f) <= 0f){
							sum8 = sum8 + entries.get(i);
							eightyPrec++;
						}
						else if (entries.get(i).compareTo(90.0f) <= 0f){
							sum9 = sum9 + entries.get(i);
							ninePrec++;
	
						}
						else if (entries.get(i).compareTo(100.0f) <= 0f){
							sum10 = sum10 + entries.get(i);
							above100Prec++;
						}
					}

					float lessZero = (sum0/less0Prec);
					if (sum0 == 0){
						showAnalysis.append("There are no entries less than 0 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries less than 0 is " + lessZero + "%\n");
					}
	
					
	
					float ten = (sum1/tenPrec);
					if (sum1 == 0){
						showAnalysis.append("There are no entries between 0 and 10 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 0 and 10 % is " + ten + "%\n");
					}
	
					
	
					float twenty = (sum2/twtyPrec);
					if (sum2 == 0){
						showAnalysis.append("There are no entries between 10 and 20 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 10 and 20 % is " + twenty + "%\n");
					}
	
					float thirty = (sum3/tirtPrec);
					if (sum3 == 0){
						showAnalysis.append("There are no entries between 20 and 30 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 20 and 30 % is " + thirty + "%\n");
					}
					
	
					float forty = (sum4/fourtPrec);
					if (sum4 == 0){
						showAnalysis.append("There are no entries between 30 and 40 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 30 and 40 % is " + forty + "%\n");
					}
	
					float fifty = (sum5/fiftPrec);
					if (sum5 == 0){
						showAnalysis.append("There are no entries between 40 and 50 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 40 and 50 % is " + fifty + "%\n");
					}
	
					float sixty = (sum6/sixPrec);
					if (sum6 == 0){
						showAnalysis.append("There are no entries between 50 and 60 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 50 and 60 % is " + sixty + "%\n");
					}
					
	
					float seventy = (sum7/sevenPrec);
					if (sum7 == 0){
						showAnalysis.append("There are no entries between 60 and 70 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 60 and 70 % is " + seventy + "%\n");
					}
					
	
					float eighty = (sum8/eightyPrec);
					if (sum8 == 0){
						showAnalysis.append("There are no entries between 70 and 80 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 70 and 80 % is " + eighty + "%\n");
					}
	
					float ninty = (sum9/ninePrec);
					if (sum9 == 0){
						showAnalysis.append("There are no entries between 80 and 90 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 80 and 90 % is " + ninty + "%\n");
					}
	
					float hundredMore = (sum10/above100Prec);
					if (sum10 == 0){
						showAnalysis.append("There are no entries between 90 and 100 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 90 and 100 % is " + hundredMore + "%\n");
					}
				}
			}

		});
		
		Text showGraph = new Text(shlGradeAnalyzer, SWT.BORDER);
		showGraph.setEditable(false);
		showGraph.setBackground(SWTResourceManager.getColor(255, 228, 181));
		showGraph.setBounds(441, 404, 652, 245);
		
		Button dispGraph = new Button(shlGradeAnalyzer, SWT.NONE);
		dispGraph.setBounds(714, 655, 124, 37);
		dispGraph.setText("Display Graph");
		dispGraph.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(!entries.isEmpty()) {
					int count10 = 0;
					int count20 = 0;
					int count30 = 0;
					int count40 = 0;
					int count50 = 0;
					int count60 = 0;
					int count70 = 0;
					int count80 = 0;
					int count90 = 0;
					int count100 = 0;
	
					Collections.sort(entries);
					for (int i = 0; i < entries.size(); i ++){
						if (entries.get(i).compareTo(10.0f) <= 0f){
							count10++;
						}
						else if (entries.get(i).compareTo(20.0f) <= 0f){
							count20++;
						}
						else if (entries.get(i).compareTo(30.0f) <= 0f){
							count30++;
						}
						else if (entries.get(i).compareTo(40.0f) <= 0f){
							count40++;
						}
						else if (entries.get(i).compareTo(50.0f) <= 0f){
							count50++;
						}
						else if (entries.get(i).compareTo(60.0f) <= 0f){
							count60++;
						}
						else if (entries.get(i).compareTo(70.0f) <= 0f){
							count70++;
						}
						else if (entries.get(i).compareTo(80.0f) <= 0f){
							count80++;
						}
						else if (entries.get(i).compareTo(90.0f) <= 0f){
							count90++;
						}
						else if (entries.get(i).compareTo(100.0f) <= 0f){
							count100++;
						}
					}
	
					showGraph.append("\tGrade Distribution Graph: \n");
					showGraph.append("0 - 10% :: ");
					for (int i = 0; i < count10; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("10 - 20% :: ");
					for (int i = 0; i < count20; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("20 - 30% :: ");
					for (int i = 0; i < count30; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("30 - 40% :: ");
					for (int i = 0; i < count40; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("40 - 50% :: ");
					for (int i = 0; i < count50; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("50 - 60% :: ");
					for (int i = 0; i < count60; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("60 - 70% :: ");
					for (int i = 0; i < count70; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("70 - 80% :: ");
					for (int i = 0; i < count80; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("80 - 90% :: ");
					for (int i = 0; i < count90; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
					showGraph.append("90 - 100% :: ");
					for (int i = 0; i < count100; i ++){
						showGraph.append("#");
					}
					showGraph.append("\n");
				}
				else {
					trackErrors.append("Graph Error: There is no data to display\n");
				}
			}
		});
		
		Button genReport = new Button(shlGradeAnalyzer, SWT.NONE);
		genReport.setBounds(109, 612, 144, 37);
		genReport.setText("Generate Report");
		
		Text dataDisp = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.V_SCROLL | SWT.CENTER | SWT.MULTI);
		dataDisp.setEditable(false);
		dataDisp.setBackground(SWTResourceManager.getColor(255, 228, 181));
		dataDisp.setBounds(467, 21, 271, 325);
		
		Button showData = new Button(shlGradeAnalyzer, SWT.NONE);
		showData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (entries.isEmpty())
				{
					trackErrors.append("Display Error: There is no data to display\n");
				}
				else
				{
					dataDisp.setText("");
					Collections.sort(entries);
					Collections.reverse(entries);
					
					int row = (int) Math.ceil(entries.size() / (float) 4);
					
					float[][] printOut = new float[row][4];
					
					for (int u = 0; u < row; u++)
					{
						for (int v = 0; v < 4; v++)
						{
							printOut[u][v] = 0.9090909111f;
						}
					}
					
					int k = 0;
					for (int j = 0; j < 4; j++)
					{
						for (int i = 0; i < row; i++)
						{
							if (k < entries.size())
							{
								printOut[i][j] = entries.get(k);
								k++;
							}
						}
					}
					
					for (int j = 0; j < row; j++)
					{
						for (int i = 0; i < 4; i++)
						{
							if (printOut[j][i] != 0.9090909111f)
							{
								dataDisp.append(Float.toString(printOut[j][i]) + "     ");
							}
							
						}
						dataDisp.append("\n");
					}
				}
			}
		});
		showData.setBounds(523, 361, 160, 37);
		showData.setText("Display Data");

	}
}
