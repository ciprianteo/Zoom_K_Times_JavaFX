

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Teo
 */
public class ImageZoomProcessor extends Thread {
    private ImageZoom img;
    
    public ImageZoomProcessor(ImageZoom img)
    {
        this.img = img;
    }
    
    /*Procesarea completa a citirii, zoomarii si scrierii imaginii rezultate pe un thread separat*/
    @Override
    public void start()
    {
        long startProc = System.currentTimeMillis();
        if("+".equals(img.getOperation()))
        {
            img.zoomImgIn();
            
        }else
        {
            if("-".equals(img.getOperation()))
            {
                img.zoomImgOut();
            }
        }
        long endProc = System.currentTimeMillis();
        img.setZoomTime(endProc - startProc);
        System.out.println("Zoom imagine: "+ img.getZoomTime() +"ms");
        try{
            
            long startWrite = System.currentTimeMillis();
            img.writeImage();
            long endWrite = System.currentTimeMillis();
            img.setWriteTime(endWrite - startWrite);
            System.out.println("Scriere imagine rezultata: "+img.getWriteTime()+"ms");
            
        }catch(IOException ex)
        {
            try {
                throw ex;
            } catch (IOException ex1) {
                Logger.getLogger(ImageZoomProcessor.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
