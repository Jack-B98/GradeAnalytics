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
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import org.eclipse.swt.awt.SWT_AWT;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JRootPane;

import java.io.*;

public class WorkGUI
{

	protected Shell shell;
	private Text text;
	private Button btnDeleteValue;
	private Text text_1;
	private Button btnSetBounds;
	private Text text_2;
	private Text text_3;
	private Button btnLoadFromFile;
	private Text text_4;
	private Button btnAppendFrom;
	private Text text_5;
	private Text text_6;
	
	private InputStreamReader newIn;
	private BufferedReader scan;

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
		shell.setSize(870, 568);
		shell.setText("SWT Application");
		
		Button btnAddValue = new Button(shell, SWT.NONE);
		btnAddValue.setBounds(20, 46, 94, 27);
		btnAddValue.setText("Add Value");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(20, 21, 94, 19);
		
		btnDeleteValue = new Button(shell, SWT.NONE);
		btnDeleteValue.setBounds(138, 46, 107, 27);
		btnDeleteValue.setText("Delete Value");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(138, 21, 94, 19);
		
		btnSetBounds = new Button(shell, SWT.NONE);
		btnSetBounds.setBounds(261, 46, 124, 27);
		btnSetBounds.setText("Set Bounds");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(251, 21, 72, 19);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(329, 21, 72, 19);
		
		btnLoadFromFile = new Button(shell, SWT.NONE);
		btnLoadFromFile.setBounds(20, 142, 112, 27);
		btnLoadFromFile.setText("Load from File");
		
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(20, 117, 107, 19);
		
		btnAppendFrom = new Button(shell, SWT.NONE);
		btnAppendFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAppendFrom.setBounds(138, 142, 141, 27);
		btnAppendFrom.setText(" Append from File");
		
		text_5 = new Text(shell, SWT.BORDER);
		text_5.setBounds(138, 117, 130, 19);
		
		text_6 = new Text(shell, SWT.BORDER | SWT.MULTI);
		text_6.setEditable(false);
		text_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_6.setBounds(10, 203, 258, 285);
		
		Label lblErrorLog = new Label(shell, SWT.NONE);
		lblErrorLog.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 16, SWT.NORMAL));
		lblErrorLog.setBounds(104, 494, 72, 19);
		lblErrorLog.setText("Error Log");
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBounds(285, 79, 560, 409);

	}
}
