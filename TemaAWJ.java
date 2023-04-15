
public class TemaAWJ {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            if(args.length != 3)
                throw new Exception("Sintaxa eronata! Apelati astfel: caleFisierSursa caleFisierDestinatie +/-(zoom IN sau zoom Out)");
            
            String sourceFilePath, destinationFilePath, operation;

            sourceFilePath = args[0];
            destinationFilePath = args[1];
            operation = args[2];
            
            ImageZoom img = new ImageZoom(sourceFilePath,destinationFilePath,operation);
            ImageZoomProcessor processImg = new ImageZoomProcessor(img);
            processImg.start();
            System.out.println("Zoom efectuat cu succes! Imaginea se afla la adresa: "+ img.getDestFile().getCanonicalPath());
            System.out.println("Timp Citire+Scriere: "+ img.getTimes(img.getReadTime() + img.getWriteTime()) + "ms");
            System.out.println("Timp total pentru executia programului: " + img.getTimes(img.getReadTime() + img.getWriteTime() + img.getZoomTime())+"ms");
        }catch(NullPointerException ex)
        {
            System.out.println(ex.getMessage());
        }catch(NumberFormatException ex)
        {
            System.out.println("Al treilea parametru trebuie sa fie un numar >0 si reprezinta numarul de zoomuri efectuate asupra fotografiei");
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        
        
    }
    
}
