import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringTime {
	private final StringProperty time = new SimpleStringProperty();
	
	public StringProperty timeProperty()
	{
		return time;
	}
	
	public final String getTime()
	{
		return time.get();
	}
	
	public final void setTime(long timeValue)
	{
		time.set(String.valueOf(timeValue));
	}
}
