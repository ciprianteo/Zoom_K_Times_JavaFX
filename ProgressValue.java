import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ProgressValue {
	private DoubleProperty val;
	
	public final double getVal()
	{
		if(val != null)
			return val.get();
		
		return 0;
	}
	
	public final void setVal(double number)
	{
		this.valProperty().set(number);
	}
	
	public final DoubleProperty valProperty()
	{
		if(val == null)
			val = new SimpleDoubleProperty(0);
		
		return val;
	}
}
