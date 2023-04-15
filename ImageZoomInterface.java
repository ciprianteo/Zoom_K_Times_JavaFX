
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Teo
 */
public interface ImageZoomInterface {
    
    void zoomImgIn();
    void zoomImgOut();
    void displayZoomedImage();
    void writeImage() throws IOException;
    
    
}
