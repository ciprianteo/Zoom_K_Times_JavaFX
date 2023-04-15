
import java.io.File;

abstract class ImageZoomUtils implements ImageZoomInterface{
    
    private final String sourceFilePath;
    private final String destinationFilePath;
    private final String operation;
    private final String imageExtension;
    private long readTime;
    private long zoomTime;
    private long writeTime;
    
    /*Parsarea comenzii primite din linia de comanda si extragerea parametrilor*/
    protected ImageZoomUtils(String sourceFP, String destinationFP, String op) throws Exception
    {
        sourceFilePath = sourceFP;
        destinationFilePath = destinationFP;
        if("+".equals(op) || "-".equals(op))
            operation = op;
        else
            throw new Exception("Operatia trebuie sa fie + sau -");
        
        if(!sourceFP.isEmpty())
        	imageExtension = sourceFP.substring(sourceFP.lastIndexOf('.'));
        else
        	imageExtension = "";

    }
    /*Accesarea fisierului sursa*/
    protected final File getSourceFile()
    {
        try
        {
            return new File(sourceFilePath);
            
        }catch(NullPointerException e)
        {
            throw e;
        }
                
    }
    /*Crearea fisierului destinatie*/
    protected final File getDestFile()
    {
        try
        {
            return new File(destinationFilePath + imageExtension);
        }catch(NullPointerException e)
        {
            throw e;
        }
    }
    
    protected final String getSourceFilePath()
    {
        return sourceFilePath;
    }
    
    protected final String getDestinationFilePath()
    {
        return destinationFilePath;
    }
    
    protected final String getOperation()
    {
        return operation;
    }
    
    protected final String getExtension()
    {
        return imageExtension;
    }
    protected final long getReadTime()
    {
        return readTime;
    }
    protected final void setReadTime(long toSet)
    {
        readTime = toSet;
    }
    protected final long getZoomTime()
    {
        return zoomTime;
    }
    protected final void setZoomTime(long toSet)
    {
        zoomTime = toSet;
    }
    protected final long getWriteTime()
    {
        return writeTime;
    }
    protected final void setWriteTime(long toSet)
    {
        writeTime = toSet;
    }
    protected final long getTimes(long... varargs)
    {
        long s=0;
        for(long i : varargs)
            s+=i;
        return s;
    }
}
