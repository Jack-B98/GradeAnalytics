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

import java.io.*;
import java.util.*;

public class WorkGUI
{

	protected Shell shell;
	private Text dataToAdd;
	private Button deleteData;
	private Text text_1;
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
	private ArrayList<Double> entries = new ArrayList();
	private Text dataDisp;

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
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
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
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		shell.setSize(1122, 737);
		shell.setText("SWT Application");
		
		Button addData = new Button(shell, SWT.NONE);
		addData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					double addOn = Double.parseDouble(dataToAdd.getText());
					entries.add(addOn);
					dataToAdd.setText("");
				}
				catch (NumberFormatException g)
				{
					trackErrors.append("Conversion Error: Data entered is NOT a number\n");
				}
			}
		});
		addData.setBounds(20, 46, 94, 27);
		addData.setText("Add Value");
		
		dataToAdd = new Text(shell, SWT.BORDER);
		dataToAdd.setBounds(20, 21, 94, 19);
		
		deleteData = new Button(shell, SWT.NONE);
		deleteData.setBounds(138, 46, 107, 27);
		deleteData.setText("Delete Value");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(138, 21, 94, 19);
		
		setBounds = new Button(shell, SWT.NONE);
		setBounds.setBounds(261, 46, 124, 27);
		setBounds.setText("Set Bounds");
		
		lowBound = new Text(shell, SWT.BORDER);
		lowBound.setBounds(251, 21, 72, 19);
		
		highBound = new Text(shell, SWT.BORDER);
		highBound.setBounds(329, 21, 72, 19);
		
		loadData = new Button(shell, SWT.NONE);
		loadData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String findFile = fileToLoad.getText();
				
				try
				{
					int lineCount = 0;
					scan = new BufferedReader(new FileReader(findFile));
					
					String content = scan.readLine();
					
					while (content != null)
					{
						lineCount++;
						try
						{
							int addNum = Integer.parseInt(content);
						}
						catch (NumberFormatException word)
						{
							trackErrors.append("Input Error File: " + "");
						}
					}
					
				}
				catch (FileNotFoundException notHere)
				{
					trackErrors.append("File Not Found: " + findFile + " could not be found\n");
				}
				catch (IOException f)
				{
					trackErrors.append("Input Error: Something happened with the file");
				}
				
			}
		});
		loadData.setBounds(20, 142, 112, 27);
		loadData.setText("Load from File");
		
		fileToLoad = new Text(shell, SWT.BORDER);
		fileToLoad.setBounds(20, 117, 107, 19);
		
		appendData = new Button(shell, SWT.NONE);
		appendData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				
			}
		});
		appendData.setBounds(138, 142, 141, 27);
		appendData.setText(" Append from File");
		
		fileToAppend = new Text(shell, SWT.BORDER);
		fileToAppend.setBounds(138, 117, 130, 19);
		
		trackErrors = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		trackErrors.setEditable(false);
		trackErrors.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		trackErrors.setBounds(21, 179, 302, 397);
		
		Label lblErrorLog = new Label(shell, SWT.NONE);
		lblErrorLog.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 16, SWT.NORMAL));
		lblErrorLog.setBounds(131, 582, 72, 19);
		lblErrorLog.setText("Error Log");
		
		Button analytics = new Button(shell, SWT.NONE);
		analytics.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		analytics.setBounds(444, 382, 160, 37);
		analytics.setText("Analytics");
		
		Button dispGraph = new Button(shell, SWT.NONE);
		dispGraph.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		dispGraph.setBounds(685, 380, 124, 37);
		dispGraph.setText("Display Graph");
		
		Button showDist = new Button(shell, SWT.NONE);
		showDist.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		showDist.setBounds(873, 380, 144, 37);
		showDist.setText("Show Distribution");
		
		Button genReport = new Button(shell, SWT.NONE);
		genReport.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		genReport.setBounds(873, 482, 144, 37);
		genReport.setText("Generate Report");
		
		dataDisp = new Text(shell, SWT.BORDER | SWT.MULTI);
		dataDisp.setBackground(SWTResourceManager.getColor(255, 239, 213));
		dataDisp.setEditable(false);
		dataDisp.setBounds(426, 21, 686, 325);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Collections.sort(entries);
				Collections.reverse(entries);
				
				int row = (int) Math.ceil(entries.size() / (double) 4);
				
				System.out.println(row);
				
				double[][] printOut = new double[row][4];
				
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
				
				int z = 0;
				for (int j = 0; j < row; j++)
				{
					for (int i = 0; i < 4; i++)
					{
						if (z < k)
						{
							dataDisp.append(Double.toString(printOut[j][i]) + " ");
						}
						z++;
					}
					dataDisp.append("\n");
				}
			}
		});
		btnNewButton.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		btnNewButton.setBounds(444, 482, 160, 37);
		btnNewButton.setText("Display Data");

	}
}
