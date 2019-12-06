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
	private Text dataToAdd;
	private Button deleteData;
	private Text dataToKill;
	private Button setBounds;
	private Text lowBound;
	private Text highBound;
	private Button loadData;
	private Text fileToLoad;
	private Button appendData;
	private Text fileToAppend;
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
					entries.add(addOn);
					dataToAdd.setText("");
					trackErrors.append("Data entered from KEYBOARD succesfully\n");
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
							d = -1;
						}
					}
					
					trackErrors.append("The entry " + erase + " was successfully removed\n");
					deleteData.setText("");
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
		
		lowBound = new Text(shlGradeAnalyzer, SWT.BORDER);
		lowBound.setBounds(251, 21, 72, 19);
		
		highBound = new Text(shlGradeAnalyzer, SWT.BORDER);
		highBound.setBounds(329, 21, 72, 19);
		
		
		loadData = new Button(shlGradeAnalyzer, SWT.NONE);
		loadData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String findFile = fileToLoad.getText();
				
				String[] checkExt = findFile.split("\\.");
				
				if(!findFile.isEmpty())
				{
					try
					{
						if (!checkExt[1].equals("txt") && !checkExt[1].equals("csv"))
						{
							trackErrors.append("Invalid File Type: Must be either .txt or .csv file\n");
						}
						else if (checkExt[1].equals("csv"))
						{
							try
							{
								int lineCount = 0;
								int error = 0;
								File newF = new File(findFile);
								Scanner track = new Scanner(newF);
								//scan = new BufferedReader(new FileReader(findFile));
								entries.clear();

								//String content;

								while (track.hasNext())
								{
									lineCount++;
									try
									{
										String[] commaDel = track.nextLine().split(",");

										for (int iter = 0; iter < commaDel.length; iter++)
										{
											double addNum = Double.parseDouble(commaDel[iter]);
											entries.add(addNum);
										}
									}
									catch (NumberFormatException word)
									{
										error = 1;
										trackErrors.append("Input Error File, Line " + lineCount + ": The data is NOT a number\n");
									}
								}

								if (error != 1)
								{
									trackErrors.setText("");
								}
								fileToLoad.setText("");
								track.close();

							}
							catch (FileNotFoundException notHere)
							{
								trackErrors.append("File Not Found: " + findFile + " could not be found\n");
							}
							/*catch (IOException f)
							{
								trackErrors.append("Input Error: Something happened with the file\n");
							}*/
						}
						else
						{
						
							try
							{
								int lineCount = 0;
								int error = 0;
								scan = new BufferedReader(new FileReader(findFile));
								entries.clear();
								
								String content;
								
								while ((content = scan.readLine()) != null)
								{
									lineCount++;
									try
									{
										double addNum = Integer.parseInt(content);
										entries.add(addNum);
									}
									catch (NumberFormatException word)
									{
										error = 1;
										trackErrors.append("Input Error File, Line " + lineCount + ": The data is NOT a number\n");
									}
								}
								
								if (error != 1)
								{
									trackErrors.setText("");
								}
								fileToLoad.setText("");
								
							}
							catch (FileNotFoundException notHere)
							{
								trackErrors.append("File Not Found: " + findFile + " could not be found\n");
							}
							catch (IOException f)
							{
								trackErrors.append("Input Error: Something happened with the file\n");
							}
						}
						
					}
					catch (ArrayIndexOutOfBoundsException missingExtension)
					{
						trackErrors.append("Missing File Extension: Please make sure to include the\n.txt or .csv at the end of your file name.\n");
					}
				}
			}
		});
		loadData.setBounds(20, 142, 112, 27);
		loadData.setText("Load from File");
		
		fileToLoad = new Text(shlGradeAnalyzer, SWT.BORDER);
		fileToLoad.setBounds(20, 117, 112, 19);
		
		appendData = new Button(shlGradeAnalyzer, SWT.NONE);
		appendData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String findFile = fileToAppend.getText();
				
				String[] checkExt = findFile.split("\\.");
				
				if(!findFile.isEmpty())
				{
					try
					{
						if (!(checkExt[1].equals("txt")) && !(checkExt[1].equals("csv")))
						{
							trackErrors.append("Invalid File Type: Must be either .txt or .csv file\n");
						}
						else
						{
						
							try
							{
								int lineCount = 0;
								scan = new BufferedReader(new FileReader(findFile));
								
								String content;
								
								while ((content = scan.readLine()) != null)
								{
									lineCount++;
									try
									{
										double addNum = Double.parseDouble(content);
										entries.add(addNum);
									}
									catch (NumberFormatException word)
									{
										trackErrors.append("Input Error File, Line " + lineCount + ": The data is NOT a number\n");
									}
								}
								
								fileToAppend.setText("");
								
							}
							catch (FileNotFoundException notHere)
							{
								trackErrors.append("File Not Found: " + findFile + " could not be found\n");
							}
							catch (IOException f)
							{
								trackErrors.append("Input Error: Something happened with the file\n");
							}
						}
					}
					catch (ArrayIndexOutOfBoundsException missingExtension)
					{
						trackErrors.append("Missing File Extension: Please make sure to include the\n.txt or .csv at the end of your file name.\n");
					}
				}
			}
		});
		appendData.setBounds(182, 142, 141, 27);
		appendData.setText(" Append from File");
		
		fileToAppend = new Text(shlGradeAnalyzer, SWT.BORDER);
		fileToAppend.setBounds(182, 117, 141, 19);
		
		trackErrors = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		trackErrors.setEditable(false);
		trackErrors.setBackground(SWTResourceManager.getColor(255, 228, 181));
		trackErrors.setBounds(21, 179, 302, 397);
		
		Label lblErrorLog = new Label(shlGradeAnalyzer, SWT.NONE);
		lblErrorLog.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 16, SWT.NORMAL));
		lblErrorLog.setBounds(123, 582, 82, 19);
		lblErrorLog.setText("Status Log");
		
		Button analytics = new Button(shlGradeAnalyzer, SWT.NONE);
		analytics.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		analytics.setBounds(874, 361, 160, 37);
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
					ArrayList<Integer> modeindex = new ArrayList<Integer>();
					Collections.sort(entries);
					for (int i = 0; i < entries.size(); i ++){
						if (i == 0){
							mode.add(entries.get(i));
						}
						else {
							if (entries.get(i) == entries.get(i-1)){
								mode.add(entries.get(i));
							}
						}
					}
					if (mode.size() > 1){
						int[] modecount = new int[mode.size()]; 
						for (int i = 0; i < mode.size(); i++){
							for (int j = 0; j < entries.size(); j++){
								if (mode.get(i) == entries.get(j)){
									modecount[i] = modecount[i] + 1;
								}
							}
						}
						int maxmode = 0;
						for(int i = 0; i < modecount.length; i++){
							if (i == 0){
								maxmode = modecount[i];
								modeindex.add(i);
							}
							else{
								if (modecount[i] >= maxmode){
									maxmode = modecount[i];
									modeindex.add(i);
								}
							}
						}

					}
					if(mode.size() > 1){
						showAnalysis.append("\n Median(s): ");
						for (int i = 0; i < modeindex.size(); i ++){
							showAnalysis.append("\n" + mode.get(modeindex.get(i)));
						}
					}
					else{
						showAnalysis.append("\n No entries repeat, every entry is the median.");
					}
					showAnalysis.append("\n Number of Entries: " + numentries);
					showAnalysis.append("\n Low: " + min);
					showAnalysis.append("\n High: " + max);
					showAnalysis.append("\n Median: " + median);
				}
			}
		});
		
		
		Button dispGraph = new Button(shlGradeAnalyzer, SWT.NONE);
		dispGraph.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		dispGraph.setBounds(714, 655, 124, 37);
		dispGraph.setText("Display Graph");
		
		Button showDist = new Button(shlGradeAnalyzer, SWT.NONE);
		showDist.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		showDist.setBounds(10, 636, 144, 37);
		showDist.setText("Show Distribution");
		
		Button genReport = new Button(shlGradeAnalyzer, SWT.NONE);
		genReport.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		genReport.setBounds(179, 636, 144, 37);
		genReport.setText("Generate Report");
		
		dataDisp = new Text(shlGradeAnalyzer, SWT.BORDER | SWT.V_SCROLL | SWT.CENTER | SWT.MULTI);
		dataDisp.setEditable(false);
		dataDisp.setFont(SWTResourceManager.getFont("Times New Roman", 18, SWT.BOLD));
		dataDisp.setBackground(SWTResourceManager.getColor(255, 228, 181));
		dataDisp.setBounds(482, 21, 271, 325);
		
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
		showData.setBounds(535, 361, 160, 37);
		showData.setText("Display Data");
		
		showAnalysis = new Text(shlGradeAnalyzer, SWT.BORDER);
		showAnalysis.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 11, SWT.NORMAL));
		showAnalysis.setBackground(SWTResourceManager.getColor(255, 228, 181));
		showAnalysis.setEditable(false);
		showAnalysis.setBounds(841, 21, 219, 325);
		
		Text text = new Text(shlGradeAnalyzer, SWT.BORDER);
		text.setEditable(false);	
		text.setBackground(SWTResourceManager.getColor(255, 228, 181));
		text.setBounds(441, 404, 652, 245);

	}
}
