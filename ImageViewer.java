import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ImageViewer {
	private String imgPath;
	private BufferedImage img;
	private ImageView imgView;
	private boolean isDark = false;
	private ProgressBar pb;
	private TextField textField;
	private StringTime time = new StringTime();
	private CheckBox cb;
	private double width, height;
	
	public void setImg(String path)
	{
		try
		{
			imgPath = new String(path);
			img = ImageIO.read(new File(imgPath));
			
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	public void setSize(double w, double h)
	{
		width = w;
		height = h;
	}
	public void display()
	{
		imgView = new ImageView();
		imgView.setPreserveRatio(true);
		imgView.setImage(SwingFXUtils.toFXImage(img, null));
		
		ScrollPane sp = new ScrollPane();
		sp.fitToWidthProperty().set(true);
		sp.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.setMaxSize(width * 0.95, height * 0.9);
		sp.setContent(imgView);
		sp.setPadding(new Insets(10,10,10,10));
		
		BorderPane layout = new BorderPane();
		layout.setCenter(sp);
		
		Button btnPlus = new Button();
		ImageView imgVbtnPlus = new ImageView("plus.png");
		imgVbtnPlus.setFitHeight(30);
		imgVbtnPlus.setFitWidth(30);
		btnPlus.setGraphic(imgVbtnPlus);
		btnPlus.setOnAction( e -> onPlus());
		
		Button btnMinus = new Button();
		ImageView imgVbtnMinus = new ImageView("minus.png");
		imgVbtnMinus.setFitHeight(30);
		imgVbtnMinus.setFitWidth(30);
		btnMinus.setGraphic(imgVbtnMinus);
		btnMinus.setOnAction( e -> onMinus());
		
		ToolBar menu = new ToolBar();
		menu.setOrientation(Orientation.HORIZONTAL);
		menu.getItems().addAll(btnMinus, btnPlus);
		
		HBox bottom = new HBox();
		bottom.setAlignment(Pos.BASELINE_CENTER);
		bottom.getChildren().add(menu);
		layout.setBottom(bottom);
		
		cb = new CheckBox("Show processing time (ms):");
		textField = new TextField ();
		textField.setDisable(true);
		textField.setStyle("-fx-opacity: 1;");
		textField.textProperty().bind(time.timeProperty());
		textField.setPrefWidth(50);
		
		HBox top = new HBox();
		top.setSpacing(50);
		top.setAlignment(Pos.BASELINE_CENTER);
		top.setPadding(new Insets(10, 0 ,10 ,0));
		pb = new ProgressBar(0);
		pb.setPrefHeight(20);
		top.getChildren().addAll(pb, cb, textField);
		layout.setTop(top);
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL); //nu te lasa sa apesi pe alta fereastra pana nu termini cu asta
		window.setTitle("Image Viewer");

		Scene scene = new Scene(layout, width, height);
		if(isDark)
			scene.getStylesheets().add("Dark.css");
		
		window.setScene(scene);
		window.show();
	}
	
	private void onPlus()
	{
		try {
			ImageZoom img = new ImageZoom(this.img, new String("+"));
			pb.progressProperty().bind(img.getProgressValue().valProperty());
			new Thread(() -> {
				
				long startProc = System.currentTimeMillis();
				img.zoomImgIn();
				this.img = img.getZoomedImage();
				imgView.setImage(SwingFXUtils.toFXImage(this.img, null));
				long endProc = System.currentTimeMillis();
				this.time.setTime(endProc-startProc);
				
			}).start();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private void onMinus()
	{
		try {
			ImageZoom img = new ImageZoom(this.img, new String("-"));
			pb.progressProperty().bind(img.getProgressValue().valProperty());
			new Thread(() -> {
				
				long startProc = System.currentTimeMillis();
				img.zoomImgOut();
				this.img = img.getZoomedImage();
				imgView.setImage(SwingFXUtils.toFXImage(this.img, null));
				long endProc = System.currentTimeMillis();
				this.time.setTime(endProc-startProc);
				
			}).start();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void setDark()
	{
		isDark = true;
	}
	
}
