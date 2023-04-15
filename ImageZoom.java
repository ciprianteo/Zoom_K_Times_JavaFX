/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Teo
 */
public class ImageZoom extends ImageZoomUtils {
    
    private BufferedImage image;
    private BufferedImage zoomedImg;
    private final ProgressValue prog = new ProgressValue();
    
    public ImageZoom(BufferedImage img, String op) throws Exception
    {
    	super("", "", op);
    	image = img;
    	zoomedImg = image;
    }
    public ImageZoom(String sourceFP, String destinationFP, String op) throws Exception
    {
        super(sourceFP, destinationFP, op);
        /*Citirea fisierului sursa*/
        try{
            
            long startRead = System.currentTimeMillis();
            image = ImageIO.read(this.getSourceFile());
            long stopRead = System.currentTimeMillis();
            this.setReadTime(stopRead-startRead);
            System.out.println("Citire imagine data:"+ this.getReadTime() + "ms");
        }catch (IllegalArgumentException e){
            
            System.out.println("Null image File");
            
        } catch (IOException ex) {
            
             throw new IOException("Imaginea NU a putut fi citita! Posibil sa nu existe");
        }
        
        zoomedImg = image;
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
    
    public BufferedImage getZoomedImage()
    {
        return zoomedImg;
    }
    
    /*Aplicarea algoritmului pentru zoom pozitiv*/
    @Override
    public final void zoomImgIn()
    {
    	
        BufferedImage auxImg = new BufferedImage((2*zoomedImg.getWidth()) - 1, (2*zoomedImg.getHeight()) - 1,zoomedImg.getType());
        prog.setVal(0);
        /*Dispunerea pixelilor existenti la distante de 2 fata de pixelii vecini*/
        int currRow = 0;
        int currCol = 0;
        for (int i = 0; i < zoomedImg.getWidth(); i++) {
            currRow = 0;
            for (int j = 0; j < zoomedImg.getHeight(); j++) {
                int pixel = zoomedImg.getRGB(i, j);
                auxImg.setRGB(currCol,currRow , pixel);
                currRow += 2;
            }
           currCol += 2;
        }
        
		double currStatus = 0;
		double total = auxImg.getWidth() * auxImg.getHeight() / 2;
		
        /*Aplicarea metodei pe randuri*/
        for (int i = 0; i < auxImg.getWidth(); i=i+2) {
            for (int j= 2; j < auxImg.getHeight(); j = j + 2) {
                int k = j - 2;
               
                int pixel1 = auxImg.getRGB(i, k);
                Color c1 = new Color(pixel1);
                int pixel2 = auxImg.getRGB(i, j);
                Color c2 = new Color(pixel2);
                
                Color c3 = new Color((c1.getRed()+c2.getRed())/2, (c1.getGreen()+c2.getGreen())/2, (c1.getBlue()+c2.getBlue())/2, (c1.getAlpha()+ c2.getAlpha())/2);
                auxImg.setRGB(i, (j+k)/2, c3.getRGB());
                
                currStatus++;
                prog.setVal(currStatus/total);
            }
        }
        /*Aplicarea metodei pe coloane*/
        for (int i = 0; i < auxImg.getHeight(); i=i+2) {
            for (int j= 2; j < auxImg.getWidth(); j = j + 2) {
                int k = j - 2;
               
                int pixel1 = auxImg.getRGB(k, i);
                Color c1 = new Color(pixel1);
                int pixel2 = auxImg.getRGB(j, i);
                Color c2 = new Color(pixel2);
                
                Color c3 = new Color((c1.getRed()+c2.getRed())/2, (c1.getGreen()+c2.getGreen())/2, (c1.getBlue()+c2.getBlue())/2, (c1.getAlpha()+ c2.getAlpha())/2 );
                auxImg.setRGB((j+k)/2, i, c3.getRGB());
                
                currStatus++;
                prog.setVal(currStatus/total);
            }
        }
        
        zoomedImg = auxImg;
    }
    
    /*Metoda de zoom out*/
    @Override
    public final void zoomImgOut()
    {
    	double currStatus = 0;
		double total = zoomedImg.getWidth() * zoomedImg.getHeight() / 4;
		prog.setVal(0);
		
        BufferedImage auxImg = new BufferedImage((zoomedImg.getWidth() + 1) / 2, (zoomedImg.getHeight() + 1) / 2,zoomedImg.getType());
        for(int i = 0,col = 0; i< zoomedImg.getWidth(); i = i + 2,col++)
        {
            for(int j = 0, row = 0; j < zoomedImg.getHeight(); j = j + 2,row++)
            {
                auxImg.setRGB(col, row, zoomedImg.getRGB(i, j));
                
                currStatus++;
                prog.setVal(currStatus/total);
            }
        }
        
        zoomedImg = auxImg;
    }
    
    /*Metoda utila in cazul in care dorim doar afisarea imaginii, nu si scrierea ei pe disk*/
    @Override
    public final void displayZoomedImage()
    {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(zoomedImg);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation
        (JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    /*Metoda de scriere a imaginii pe disk*/
    @Override
    public final void writeImage() throws IOException
    {
        try
        {
            ImageIO.write(zoomedImg, this.getExtension().substring(1), this.getDestFile());
            
        }catch(NullPointerException ex)
        {
            throw ex;
            
        }catch(IOException ex)
        {
            throw ex;
        }
    }
    
    public ProgressValue getProgressValue()
    {
    	return this.prog;
    }
}
