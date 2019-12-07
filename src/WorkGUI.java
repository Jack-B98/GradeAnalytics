import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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
	private Text dataToAdd;
	private Button deleteData;
	private Text dataToKill;
	private Button setBounds;
	private Text lowBound;
	private Text highBound;
	private Button loadData;
	private Button appendData;
	private Text trackErrors;
	
	//Stuff for file operations and analyzing the numbers
	private BufferedReader scan;
	private int numOfEntries = 0;
	private int highestVal = 0;
	private int lowestVal = 0;
	private int mean, median, mode;
	private ArrayList<Double> entries = new ArrayList<Double>();
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
		
		Button addData = new Button(shlGradeAnalyzer, SWT.NONE);
		addData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					double addOn = Double.parseDouble(dataToAdd.getText());
					
					if((addOn >= lowBoundVal) && (addOn <= highBoundVal))
						entries.add(addOn);
					else
						trackErrors.append("Out of Bounds Error: Data entered is NOT within the set boundaries\n");
				}
				catch (NumberFormatException g)
				{
					trackErrors.append("Conversion Error: Data entered is NOT a number\n");
				}
			}
		});
		addData.setBounds(10, 46, 117, 27);
		addData.setText("Add Value");

		dataToAdd = new Text(shlGradeAnalyzer, SWT.BORDER);
		dataToAdd.setBounds(20, 21, 94, 19);
		
		deleteData = new Button(shlGradeAnalyzer, SWT.NONE);
		deleteData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					double erase = Double.parseDouble(dataToKill.getText());
					
					for (int d = 0; d < entries.size(); d++)
					{
						if (entries.get(d) == erase)
						{
							entries.remove(d);
							d = entries.size();
						}
					}
					
				}
				catch (NumberFormatException x)
				{
					trackErrors.append("Conversion Error: Data to delete is NOT a number\n");
				}
			}
		});
		deleteData.setBounds(130, 46, 112, 27);
		deleteData.setText("Delete Value");
		
		dataToKill = new Text(shlGradeAnalyzer, SWT.BORDER);
		dataToKill.setBounds(138, 21, 94, 19);
		
		setBounds = new Button(shlGradeAnalyzer, SWT.NONE);
		setBounds.setBounds(261, 46, 124, 27);
		setBounds.setText("Set Bounds");
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
		
		lowBound = new Text(shlGradeAnalyzer, SWT.BORDER);
		lowBound.setBounds(251, 21, 72, 19);
		
		highBound = new Text(shlGradeAnalyzer, SWT.BORDER);
		highBound.setBounds(329, 21, 72, 19);
		
		
		loadData = new Button(shlGradeAnalyzer, SWT.NONE);
		loadData.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
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
		  					BufferedReader track = new BufferedReader(new FileReader(location));
		  					String content;
		  					
		  					while ((content = track.readLine()) != null)
		  					{
		  						try
		  						{
		  							
		  							if (content.contains(","))
		  							{
		  								String[] comSplit = content.split(",");
		  								
		  								for (int k = 0; k < comSplit.length; k++)
		  								{
		  									if((Double.parseDouble(comSplit[k]) >= lowBoundVal) && (Double.parseDouble(comSplit[k]) <= highBoundVal))
		  										entries.add(Double.parseDouble(comSplit[k]));
		  								}
		  							}
		  							else
		  							{
		  								double addNum = Double.parseDouble(content);
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
		  				}
		  				catch (FileNotFoundException eff)
		  				{
		  					trackErrors.append("File Error: The file could not be found\n");
		  				}
		  				catch (IOException gh)
  						{
  							trackErrors.append("Input Error\n");
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
		
		appendData = new Button(shlGradeAnalyzer, SWT.NONE);
		appendData.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
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
		  				try
		  				{
		  					BufferedReader track = new BufferedReader(new FileReader(location));
		  					String content;
		  					
		  					while ((content = track.readLine()) != null)
		  					{
		  						try
		  						{
		  							
		  							if (content.contains(","))
		  							{
		  								String[] comSplit = content.split(",");
		  								
		  								for (int k = 0; k < comSplit.length; k++)
		  								{
		  									if((Double.parseDouble(comSplit[k]) >= lowBoundVal) && (Double.parseDouble(comSplit[k]) <= highBoundVal))
		  										entries.add(Double.parseDouble(comSplit[k]));
		  								}
		  							}
		  							else
		  							{
		  								double addNum = Double.parseDouble(content);
		  								if((addNum >= lowBoundVal) && (addNum <= highBoundVal))
		  									entries.add(addNum);
		  							}
		  							
		  						}
		  						catch (NumberFormatException xz)
		  						{
		  							trackErrors.append("Input Error: Some of the data entered was NOT a number\n");
		  						}
		  					}
		  				}
		  				catch (FileNotFoundException eff)
		  				{
		  					trackErrors.append("File Error: The file could not be found\n");
		  				}
		  				catch (IOException gh)
  						{
  							trackErrors.append("Input Error\n");
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
		
		trackErrors = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		trackErrors.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 12, SWT.NORMAL));
		trackErrors.setEditable(false);
		trackErrors.setBackground(SWTResourceManager.getColor(255, 228, 181));
		trackErrors.setBounds(21, 85, 323, 414);
		
		Label lblErrorLog = new Label(shlGradeAnalyzer, SWT.NONE);
		lblErrorLog.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 16, SWT.NORMAL));
		lblErrorLog.setBounds(130, 510, 82, 19);
		lblErrorLog.setText("Status Log");
		
		Button analytics = new Button(shlGradeAnalyzer, SWT.NONE);
		analytics.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
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
					double min = entries.get(0);
					Collections.reverse(entries);
					double max = entries.get(0);
					double sum = 0;
					for (int i = 0; i < entries.size(); i++){
						sum = sum + entries.get(i);
					}
					double mean = sum/entries.size();
					Collections.sort(entries);
					double median;
					if (entries.size()%2 != 0){
						int index = entries.size()/2;
						median = entries.get(index);
					}
					else{
						int index1 = entries.size()/2;
						int index2 = index1 - 1;
						median = (entries.get(index1) + entries.get(index2))/2;
					}
					ArrayList<Double> mode = new ArrayList<Double>();
					Collections.sort(entries);
					
					
					for (int i = 1; i < entries.size(); i++){
						
							if (entries.get(i).compareTo(entries.get(i-1)) == 0){
								mode.add(entries.get(i));
							}
						
					}
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
									//modeindex.add(i);
								}
							
						}
						
						for(int i = 0; i < modecount.length; i++){
							
							if (modecount[i] >= maxmode){
								maxmode = modecount[i];
								modeindex.add(i);
							}
						
						}

					}
					ArrayList<Double> printmode = new ArrayList<Double>();
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
						showAnalysis.append("\nMode(s): ");
						for (int i = 0; i < printmode.size(); i ++){
							showAnalysis.append("\n" + printmode.get(i));
						}
					}
					else{
						showAnalysis.append("\nNo entries repeat, every entry is the mode.");
					}
					showAnalysis.append("\nNumber of Entries: " + numentries);
					showAnalysis.append("\nLow: " + min);
					showAnalysis.append("\nHigh: " + max);
					showAnalysis.append("\nMedian: " + median +"\n\n");
				}
			}
		});
		
		
		
		Button dispGraph = new Button(shlGradeAnalyzer, SWT.NONE);
		dispGraph.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		dispGraph.setBounds(714, 655, 124, 37);
		dispGraph.setText("Display Graph");
		dispGraph.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(!entries.isEmpty()) {
					showGraph.setText("");
					
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
						if (entries.get(i).compareTo(10.0) <= 0){
							count10++;
						}
						else if (entries.get(i).compareTo(20.0) <= 0){
							count20++;
						}
						else if (entries.get(i).compareTo(30.0) <= 0){
							count30++;
						}
						else if (entries.get(i).compareTo(40.0) <= 0){
							count40++;
						}
						else if (entries.get(i).compareTo(50.0) <= 0){
							count50++;
						}
						else if (entries.get(i).compareTo(60.0) <= 0){
							count60++;
						}
						else if (entries.get(i).compareTo(70.0) <= 0){
							count70++;
						}
						else if (entries.get(i).compareTo(80.0) <= 0){
							count80++;
						}
						else if (entries.get(i).compareTo(90.0) <= 0){
							count90++;
						}
						else if (entries.get(i).compareTo(100.0) <= 0){
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
					trackErrors.append("Graph Error: There is no data to output\n");
				}
			}
		});
		
		Button showDist = new Button(shlGradeAnalyzer, SWT.NONE);
		showDist.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		showDist.setBounds(767, 361, 144, 37);
		showDist.setText("Show Distribution");
		showDist.addSelectionListener(new SelectionAdapter(){

			@Override

			public void widgetSelected(SelectionEvent e) 

			{
				

				double total = entries.size();

				double less0Prec = 0;
				double sum0 = 0;
				double tenPrec = 0;
				double sum1 = 0;
				double twtyPrec = 0;
				double sum2 = 0;
				double tirtPrec = 0;
				double sum3 = 0;
				double fourtPrec = 0;
				double sum4 = 0;
				double fiftPrec = 0;
				double sum5 = 0;
				double sixPrec = 0;
				double sum6 = 0;
				double sevenPrec = 0;
				double sum7 = 0;
				double eightyPrec = 0;
				double sum8 = 0;
				double ninePrec = 0;
				double sum9 = 0;
				double above100Prec = 0;
				double sum10 = 0;
				if (entries.isEmpty())

				{

					trackErrors.append("Analytics Error: There is no data to analyze\n");

				}
				else {
					showAnalysis.setText("");
					
					Collections.sort(entries);
					for (int i = 0; i < entries.size(); i ++){
						if (entries.get(i).compareTo(10.0) <= 0){
							sum1 = sum1 + entries.get(i);
							tenPrec++;
						}
						else if (entries.get(i).compareTo(20.0) <= 0){
							sum2 = sum2 + entries.get(i);
							twtyPrec++;
						}
						else if (entries.get(i).compareTo(30.0) <= 0){
							sum3 = sum3 + entries.get(i);
							tirtPrec++;
						}
						else if (entries.get(i).compareTo(40.0) <= 0){
							sum4 = sum4 + entries.get(i);
							fourtPrec++;
						}
						else if (entries.get(i).compareTo(50.0) <= 0){
							sum5 = sum5 + entries.get(i);
							fiftPrec++;
						}
						else if (entries.get(i).compareTo(60.0) <= 0){
							sum6 = sum6 + entries.get(i);
							sixPrec++;
						}
						else if (entries.get(i).compareTo(70.0) <= 0){
							sum7 = sum7 + entries.get(i);
							sevenPrec++;
						}
						else if (entries.get(i).compareTo(80.0) <= 0){
							sum8 = sum8 + entries.get(i);
							eightyPrec++;
						}
						else if (entries.get(i).compareTo(90.0) <= 0){
							sum9 = sum9 + entries.get(i);
							ninePrec++;
	
						}
						else if (entries.get(i).compareTo(100.0) <= 0){
							sum10 = sum10 + entries.get(i);
							above100Prec++;
						}
					}
				

					double lessZero = (sum0/less0Prec);
					if (sum0 == 0){
						showAnalysis.append("There are no entries less than 0 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries less than 0 is " + lessZero + "%\n");
					}
	
					
	
					double ten = (sum1/tenPrec);
					if (sum1 == 0){
						showAnalysis.append("There are no entries between 0 and 10 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 0 and 10 % is " + ten + "%\n");
					}
	
					
	
					double twenty = (sum2/twtyPrec);
					if (sum2 == 0){
						showAnalysis.append("There are no entries between 10 and 20 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 10 and 20 % is " + twenty + "%\n");
					}
	
					double thirty = (sum3/tirtPrec);
					if (sum3 == 0){
						showAnalysis.append("There are no entries between 20 and 30 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 20 and 30 % is " + thirty + "%\n");
					}
					
	
					double forty = (sum4/fourtPrec);
					if (sum4 == 0){
						showAnalysis.append("There are no entries between 30 and 40 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 30 and 40 % is " + forty + "%\n");
					}
	
					double fifty = (sum5/fiftPrec);
					if (sum5 == 0){
						showAnalysis.append("There are no entries between 40 and 50 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 40 and 50 % is " + fifty + "%\n");
					}
	
					double sixty = (sum6/sixPrec);
					if (sum6 == 0){
						showAnalysis.append("There are no entries between 50 and 60 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 50 and 60 % is " + sixty + "%\n");
					}
					
	
					double seventy = (sum7/sevenPrec);
					if (sum7 == 0){
						showAnalysis.append("There are no entries between 60 and 70 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 60 and 70 % is " + seventy + "%\n");
					}
					
	
					double eighty = (sum8/eightyPrec);
					if (sum8 == 0){
						showAnalysis.append("There are no entries between 70 and 80 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 70 and 80 % is " + eighty + "%\n");
					}
	
					double ninty = (sum9/ninePrec);
					if (sum9 == 0){
						showAnalysis.append("There are no entries between 80 and 90 %\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 80 and 90 % is " + ninty + "%\n");
					}
	
					double hundredMore = (sum10/above100Prec);
					if (sum10 == 0){
						showAnalysis.append("There are no entries between 90 and 100 %\n\n");
					}
					else {
						showAnalysis.append("The average grade of entries between 90 and 100 % is " + hundredMore + "%\n\n");
					}
				}
			}

		});
		
		Button genReport = new Button(shlGradeAnalyzer, SWT.NONE);
		genReport.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		genReport.setBounds(109, 612, 144, 37);
		genReport.setText("Generate Report");
		genReport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				try
				{
					analytics.notifyListeners(SWT.Selection, new Event());
					showDist.notifyListeners(SWT.Selection, new Event());
					dispGraph.notifyListeners(SWT.Selection, new Event());
					File dir = new File("C:\\");
					Writer fileWriter = new FileWriter("output.txt");
		
					fileWriter.write("Grade Analytics Report: \n");
					fileWriter.write(showAnalysis.getText());
					fileWriter.write(showGraph.getText());
		
					fileWriter.flush();
					fileWriter.close();
				}
				catch (IOException d)
				{
					
				}
				
			}
			
		});
		
		dataDisp = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.CENTER | SWT.MULTI);
		dataDisp.setEditable(false);
		dataDisp.setFont(SWTResourceManager.getFont("Times New Roman", 12, SWT.BOLD));
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
					
					int row = (int) Math.ceil(entries.size() / (double) 4);
					
					double[][] printOut = new double[row][4];
					
					for (int u = 0; u < row; u++)
					{
						for (int v = 0; v < 4; v++)
						{
							printOut[u][v] = 0.9090909111;
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
							if (printOut[j][i] != 0.9090909111)
							{
								dataDisp.append(Double.toString(printOut[j][i]) + "     ");
							}
							
						}
						dataDisp.append("\n");
					}
				}
			}
		});
		showData.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		showData.setBounds(523, 361, 160, 37);
		showData.setText("Display Data");
		
		showAnalysis = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		showAnalysis.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 12, SWT.NORMAL));
		showAnalysis.setBackground(SWTResourceManager.getColor(255, 228, 181));
		showAnalysis.setEditable(false);
		showAnalysis.setBounds(778, 21, 294, 325);
		
		showGraph = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		showGraph.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 12, SWT.NORMAL));
		showGraph.setEditable(false);
		showGraph.setBackground(SWTResourceManager.getColor(255, 228, 181));
		showGraph.setBounds(441, 404, 652, 245);

	}
}