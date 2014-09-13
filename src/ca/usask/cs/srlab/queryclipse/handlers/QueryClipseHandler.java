package ca.usask.cs.srlab.queryclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class QueryClipseHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		//show the QueryClipse view
		String SEviewID="ca.usask.cs.srlab.queryclipse.views.QueryClipseView";
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SEviewID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String SCBviewID="ca.usask.ca.srlab.surfclipse.client.views.SurfClipseBrowser";
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SCBviewID);
		
		System.out.println("QueryClipse windows shown successfully");
		
		return null;
	}

}
