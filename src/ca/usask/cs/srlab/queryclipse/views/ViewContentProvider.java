package ca.usask.cs.srlab.queryclipse.views;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import core.Result;

public class ViewContentProvider implements IStructuredContentProvider {

	Result[] results;
	public ViewContentProvider()
	{
		results=new Result[]{};
	}
	public ViewContentProvider(Result[] results)
	{
		this.results=results;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub	
	}
	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return this.results;
	}
	
	
}
