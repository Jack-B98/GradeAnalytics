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
	private ArrayList<Double> entries = new ArrayList<Double>();
	private Text dataDisp;
	private Text text;
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
		shell.setBackground(SWTResourceManager.getColor(160, 82, 45));
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
		addData.setBounds(10, 46, 117, 27);
		addData.setText("Add Value");
		
		dataToAdd = new Text(shell, SWT.BORDER);
		dataToAdd.setBounds(20, 21, 94, 19);
		
		deleteData = new Button(shell, SWT.NONE);
		deleteData.setBounds(130, 46, 112, 27);
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
							trackErrors.append("Input Error File, Line " + lineCount + ": The data is NOT a number\n");
						}
					}
					
					trackErrors.append("Data Successfully Loaded\n");
					
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
		});
		loadData.setBounds(20, 142, 112, 27);
		loadData.setText("Load from File");
		
		fileToLoad = new Text(shell, SWT.BORDER);
		fileToLoad.setBounds(20, 117, 112, 19);
		
		appendData = new Button(shell, SWT.NONE);
		appendData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				
			}
		});
		appendData.setBounds(182, 142, 141, 27);
		appendData.setText(" Append from File");
		
		fileToAppend = new Text(shell, SWT.BORDER);
		fileToAppend.setBounds(182, 117, 141, 19);
		
		trackErrors = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		trackErrors.setEditable(false);
		trackErrors.setBackground(SWTResourceManager.getColor(255, 228, 181));
		trackErrors.setBounds(21, 179, 302, 397);
		
		Label lblErrorLog = new Label(shell, SWT.NONE);
		lblErrorLog.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 16, SWT.NORMAL));
		lblErrorLog.setBounds(123, 582, 82, 19);
		lblErrorLog.setText("Status Log");
		
		Button analytics = new Button(shell, SWT.NONE);
		analytics.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		analytics.setBounds(886, 361, 160, 37);
		analytics.setText("Analytics");
		
		Button dispGraph = new Button(shell, SWT.NONE);
		dispGraph.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		dispGraph.setBounds(716, 636, 124, 37);
		dispGraph.setText("Display Graph");
		
		Button showDist = new Button(shell, SWT.NONE);
		showDist.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		showDist.setBounds(10, 636, 144, 37);
		showDist.setText("Show Distribution");
		
		Button genReport = new Button(shell, SWT.NONE);
		genReport.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		genReport.setBounds(179, 636, 144, 37);
		genReport.setText("Generate Report");
		
		dataDisp = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.CENTER | SWT.MULTI);
		dataDisp.setEditable(false);
		dataDisp.setFont(SWTResourceManager.getFont("Times New Roman", 95, SWT.BOLD));
		dataDisp.setBackground(SWTResourceManager.getColor(255, 228, 181));
		dataDisp.setBounds(482, 21, 286, 325);
		
		Button showData = new Button(shell, SWT.NONE);
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
								dataDisp.append(Double.toString(printOut[j][i]) + "     ");
							}
							z++;
						}
						dataDisp.append("\n");
					}
				}
			}
		});
		showData.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		showData.setBounds(547, 361, 160, 37);
		showData.setText("Display Data");
		
		text = new Text(shell, SWT.BORDER);
		text.setBackground(SWTResourceManager.getColor(255, 228, 181));
		text.setEditable(false);
		text.setBounds(873, 21, 189, 325);
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(255, 228, 181));
		canvas.setBounds(482, 429, 579, 190);

	}
}
