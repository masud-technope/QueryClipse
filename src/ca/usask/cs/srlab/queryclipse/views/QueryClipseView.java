package ca.usask.cs.srlab.queryclipse.views;

import java.util.ArrayList;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import search.SearchResultProvider;
import core.QueryRecommender;
import core.Result;

public class QueryClipseView extends ViewPart{

	public static final String ID = "ca.usask.cs.srlab.queryclipse.views.QueryClipseView";
	ContentProposalAdapter adapter = null;
	public TableViewer viewer;
	Button google, bing, yahoo, stackoverflow;
	GridLayout gridLayout=null;
	final int TEXT_MARGIN = 3;
	final int MIN = 60;
	final Display currDisplay = Display.getCurrent();
	final TextLayout textLayout = new TextLayout(currDisplay);
	Font font1 = new Font(currDisplay, "Arial", 12, SWT.BOLD);
	Font font2 = new Font(currDisplay, "Arial", 10, SWT.NORMAL);
	Font font3 = new Font(currDisplay, "Arial", 10, SWT.NORMAL);
	Color blue = currDisplay.getSystemColor(SWT.COLOR_BLUE);
	Color green = currDisplay.getSystemColor(SWT.COLOR_DARK_GREEN);
	Color gray = currDisplay.getSystemColor(SWT.COLOR_DARK_GRAY);
	TextStyle style1 = new TextStyle(font1, blue, null);
	TextStyle style2 = new TextStyle(font2, green, null);
	TextStyle style3 = new TextStyle(font3, gray, null);
	String[] proposals=null;

	
	protected void addSearchPanel(Composite parent)
	{
		//adding search query box
		final Composite composite = new Composite(parent, SWT.NONE);
		//gridLayout = new GridLayout(3, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 10;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		composite.setLayout(gridLayout);

		GridData gridData = new GridData(SWT.CENTER, SWT.FILL, true, false);
		composite.setLayoutData(gridData);

		// gridData = new GridData(SWT.DEFAULT, SWT.FILL, false, false);
		GridData gdata2 = new GridData();
		gdata2.heightHint = 25;
		gdata2.widthHint = 600;
		gdata2.horizontalAlignment = SWT.BEGINNING;
		gdata2.verticalAlignment = SWT.CENTER;
		gdata2.grabExcessHorizontalSpace = false;

		Label keywordlabel = new Label(composite, SWT.NONE);
		// final Image
		// image=ImageDescriptor.createFromFile(SurfClipseClientView.class,
		// "sclogo4.png").createImage();
		// keywordlabel.setImage(image);
		keywordlabel.setText("Query:");
		keywordlabel.setFont(new Font(composite.getDisplay(), "Arial", 11,
				SWT.BOLD));

		final Text input = new Text(composite, SWT.SINGLE | SWT.BORDER);
		input.setEditable(true);
		input.setToolTipText("Enter your search keywords (e.g., java.lang.ClassNotFoundException JDBC Driver)");
		Font myfont = new Font(composite.getDisplay(), "Arial", 11, SWT.NORMAL);
		input.setFont(myfont);
		input.setLayoutData(gdata2);
		
		//adding proposal to input box
		FocusListener focuslistener=new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				try{
					if(proposals.length>0){
					SimpleContentProposalProvider scp = new SimpleContentProposalProvider(
							proposals);
					//setting filtering
					scp.setFiltering(true);
					String autoactive="abcdefghijklmnopqrstuvwxyz0123456789";
					adapter = new ContentProposalAdapter(input,
							new TextContentAdapter(), scp, null, autoactive.toCharArray()); //keystroke is ignored
					adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
					}
				}
					catch(Exception exc){
					//handle the exception
				}
			}
		};
		if(!input.isListening(SWT.FOCUSED)){
			input.addFocusListener(focuslistener);
		}
		
		GridData gdata4 = new GridData();
		gdata4.heightHint = 30;
		gdata4.widthHint = 90;
		gdata4.horizontalAlignment = SWT.BEGINNING;
		// gdata2.grabExcessHorizontalSpace=true;

		Button refreshButton = new Button(composite, SWT.PUSH);
		refreshButton.setText("Refresh");
		refreshButton.setToolTipText("Refresh Queries");
		refreshButton.setFont(new Font(parent.getDisplay(), "Arial", 10,
				SWT.BOLD));
		refreshButton.setImage(getRefreshImage());
		refreshButton.setLayoutData(gdata4);
		refreshButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//clear input
				input.setText("");
				QueryRecommender recommender=new QueryRecommender();
				ArrayList<String> suggestions=recommender.recommendQueries();
				proposals = suggestions.toArray(new String[suggestions.size()]);
				//also show the stack graph: it is shown elsewhere in StackGraphManager
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		GridData gdata3 = new GridData();
		gdata3.heightHint = 30;
		gdata3.widthHint = 90;
		gdata3.horizontalAlignment = SWT.BEGINNING;
		// gdata2.grabExcessHorizontalSpace=true;

		Button searchButton = new Button(composite, SWT.PUSH);
		searchButton.setText("Search");
		searchButton.setToolTipText("Search with QueryClipse");
		searchButton.setFont(new Font(parent.getDisplay(), "Arial", 10,
				SWT.BOLD));
		searchButton.setImage(get_search_image());
		//searchButton.setImage(Display.getDefault().getSystemImage(SWT.ICON_SEARCH));
		// System.out.println("Search Icon:"+Display.getDefault().getSystemImage(SWT.ICON_SEARCH));
		searchButton.setLayoutData(gdata3);
		searchButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				String searchQuery=input.getText().trim();
				if(!searchQuery.isEmpty()){
					try{
					int index=0;
					if(google.getSelection()){
						index=0;
					}else if(bing.getSelection()){
						index=1;
					}else if(yahoo.getSelection()){
						index=2;
					}
					System.out.println("Query:"+searchQuery);
					SearchResultProvider provider=new SearchResultProvider();
					provider.collectNDisplayResults(searchQuery, index);
					//ViewContentProvider cprovider=new ViewContentProvider(searchQuery, index);
					//System.out.println(viewer);
					//viewer.setContentProvider(cprovider);
					}catch(Exception exc){
						exc.printStackTrace();
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub	
			}
		});
		
	}
	
	protected Image get_search_image() {
		return ImageDescriptor.createFromFile(QueryClipseView.class,
				"search16.png").createImage();
	}
	protected Image getRefreshImage()
	{
		return ImageDescriptor.createFromFile(QueryClipseView.class,
				"refresh.png").createImage();
	}
	
	protected void addSearchEngines(Composite parent)
	{
		//adding search engines
		final Composite composite2 = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(4, false);
		GridLayout gridLayout2 = new GridLayout(3, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 10;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		composite2.setLayout(gridLayout2);

		GridData gridData2 = new GridData(SWT.CENTER, SWT.FILL, true, false);
		composite2.setLayoutData(gridData2);
		
		//adding radio button list
		google=new Button(composite2, SWT.RADIO);
		google.setText("Google");
		bing=new Button(composite2, SWT.RADIO);
		bing.setText("Bing");
		yahoo=new Button(composite2, SWT.RADIO);
		yahoo.setText("Yahoo!");
		//stackoverflow=new Button(composite2, SWT.RADIO);
		//stackoverflow.setText("StackOverflow");
	}
	
	protected void addResultTable(Composite parent)
	{
		//adding result table
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		// viewer.setSorter(new MyTableSorter());
		final Table table = viewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		String[] columnNames = {"Search Result"};
		int[] colWidth = { 1000};
		int[] colAlignment = { SWT.LEFT};
		for (int i = 0; i < columnNames.length; i++) {
			// stored for sorting
			final int columnNum = i;
			TableColumn col = new TableColumn(table, colAlignment[i]);
			col.setText(columnNames[i]);
			col.setWidth(colWidth[i]);
			// col.setMoveable(true);
			col.addSelectionListener(new SelectionAdapter() {
			});
			// col.setImage(getDefaultImage());
		}

		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setInput(null);
		// resizing table item height
		setItemHeight(table, MIN);
		setPaintItem(table);

		
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			Result myresult = (Result) obj;
			String colText = new String();
			if(index==0)
			//if (myresult.title != null)
			colText = myresult.title + "\n" + myresult.resultURL + "\n"
						+ myresult.description;
			return colText;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	protected void setPaintItem(Table table) {
		table.addListener(SWT.PaintItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if (event.index == 0) {
					TableItem item = (TableItem) event.item;
					// item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
					String text = item.getText(event.index);
					/* center column 1 vertically */
					int yOffset = 0;
					if (event.index == 1) {
						Point size = event.gc.textExtent(text);
						yOffset = Math.max(0, (event.height - size.y) / 2);
					}
					// event.gc.drawText(text, event.x + TEXT_MARGIN, event.y
					// + yOffset, true);

					// redraw text layout
					String resultText = item.getText(0);
					int firstNL = resultText.indexOf('\n');
					int lastNL = resultText.lastIndexOf('\n');
					textLayout.setText(resultText);
					textLayout.setStyle(style1, 0, firstNL - 1);
					textLayout.setStyle(style2, firstNL + 1, lastNL - 1);
					textLayout.setStyle(style3, lastNL, resultText.length());
					textLayout.draw(event.gc, event.x, event.y);
				} }
		});

	}
	
	public void setItemHeight(Table table, final int min) {
		table.addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				TableItem item = (TableItem) event.item;
				String text = item.getText(event.index);
				Point size = event.gc.textExtent(text);
				event.width = size.x + 2 * TEXT_MARGIN;
				event.height = Math.max(min, size.y + TEXT_MARGIN);
			}
		});
		table.addListener(SWT.EraseItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				event.detail &= ~SWT.FOREGROUND;
			}
		});
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = 15;
		glayout.marginHeight = 10;
		parent.setLayout(glayout);

		GridData gdata = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayoutData(gdata);

		addSearchEngines(parent);
		addSearchPanel(parent);
		addResultTable(parent);

		// adding to the Help system
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
						"ca.usask.ca.srlab.queryclipse.QueryClipseView.viewer");
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}	
}
