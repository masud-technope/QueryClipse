package search;

import java.util.ArrayList;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import ca.usask.cs.srlab.queryclipse.views.QueryClipseView;
import ca.usask.cs.srlab.queryclipse.views.ViewContentProvider;
import core.Result;


public class SearchResultProvider {
	public SearchResultProvider() {
		// default constructor
	}

	protected Result[] provideResults(String searchQuery, int index) {
		if (searchQuery.isEmpty())
			return new Result[]{};
		
		ArrayList<Result> results = new ArrayList<>();
		if(index==0){
			GoogleAPI gapi=new GoogleAPI();
			results=gapi.find_Google_Results(searchQuery);
		}else if(index==1){
			BingAPI bapi=new BingAPI();
			results=bapi.find_Bing_Results(searchQuery);
		}else if(index==2){
			YahooAPI2 yapi=new YahooAPI2();
			results=yapi.get_Yahoo_Results(searchQuery);
		}
		Result[] rescoll = new Result[results.size()];
		System.out.println("Results returned:"+results.size());
		return results.toArray(rescoll);
	}
	
	public void collectNDisplayResults(String searchQuery, int index)
	{
		try {
			Result[] collectedResults = provideResults(searchQuery, index);
			// now update the IDE
			String viewID = "ca.usask.cs.srlab.queryclipse.views.QueryClipseView";
			IWorkbenchPage page = (IWorkbenchPage) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IViewPart vpart = page.findView(viewID);
			QueryClipseView qcView = (QueryClipseView) vpart;
			ViewContentProvider cprovider = new ViewContentProvider(collectedResults);
			qcView.viewer.setContentProvider(cprovider);
			qcView.viewer.setInput(collectedResults);
			
		} catch (Exception exc) {
			// handle the exception
			exc.printStackTrace();
		}
	}
		
}
