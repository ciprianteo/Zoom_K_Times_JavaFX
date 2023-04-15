import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ZoomK extends Application{
	Stage window;
	Scene scene;
	String filePath;
	Button viewImg;
	ToggleButton darkButton;
	private double imgViewerWidth, imgViewerHeight;
	private static final ObservableList filesNames = FXCollections.observableArrayList();
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception
	{
		window = primaryStage;
		window.setTitle("Zoom K Times");
		
		TreeItem<String> root = new TreeItem<>();
		root.setExpanded(true);
		TreeView<String> tree = new TreeView<>();
		tree.setRoot(root);
		tree.setDisable(true);
		
		VBox vblayoutRight = new VBox();
		vblayoutRight.setAlignment(Pos.TOP_CENTER);
		vblayoutRight.setSpacing(15);
		Label listLabel = new Label("Uploaded Images:");
		ListView<String> list = new ListView<String>();
		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				filePath = newValue;
				root.getChildren().clear();
				createTree(root);
				tree.setDisable(false);
			}
		   
		    
		});
		
		list.setItems(filesNames);
		vblayoutRight.getChildren().addAll(listLabel,list);
		
		//File Menu
		Menu fileMenu = new Menu("_File");// _F daca apesi Alt + F e shortcut pt File
		
		MenuItem newFile = new MenuItem("Open...");
		newFile.setOnAction(e ->
		{
			filePath =  new String(openFile());
			filesNames.add(filePath);
			list.getSelectionModel().select(filesNames.size() - 1);
			
			root.getChildren().clear();
			createTree(root);
			tree.setDisable(false);
			
		});
		fileMenu.getItems().add(newFile);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu);
		
		
		viewImg = new Button("View Image");
		viewImg.setOnAction(e-> viewImage());
		viewImg.setDisable(true);
		viewImg.setStyle("-fx-border-color: black; -fx-text-fill: black; -fx-background-color: yellow;");
		
		Label tgLabel = new Label("Size of the image viewer scene:");
		ToggleGroup group = new ToggleGroup();
		RadioButton rb1 = new RadioButton("800x600  ");
		RadioButton rb2 = new RadioButton("1024x900");
		RadioButton rb3 = new RadioButton("1440x960");
		rb1.setSelected(true);
		imgViewerWidth = 800;
		imgViewerHeight = 600;
		rb1.setToggleGroup(group);
		rb2.setToggleGroup(group);
		rb3.setToggleGroup(group);
		group.selectedToggleProperty().addListener( 
				(v, oldValue, newValue) -> 
				{
					String text = ((RadioButton)(newValue.getToggleGroup().getSelectedToggle())).getText();
					switch(text)
					{
					case "800x600  " :
						imgViewerWidth = 800;
						imgViewerHeight = 600;
						break;
						
					case "1024x900" :
						imgViewerWidth = 1024;
						imgViewerHeight = 900;
						break;
					
					case "1440x960" :
						imgViewerWidth = 1440;
						imgViewerHeight = 960;
						break;	
					}
				});
		
		ScrollPane sp = new ScrollPane();
		sp.fitToWidthProperty().set(true);
		sp.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		sp.setContent(tree);
		sp.setPadding(new Insets(10,10,10,10));
		
		Label pathLabel = new Label("File path:");
		
		darkButton = new ToggleButton("Dark Mode");
		darkButton.setOnAction( e -> setDark(darkButton.isSelected()));
		
		VBox vblayoutCenter = new VBox();
		vblayoutCenter.setAlignment(Pos.CENTER);
		vblayoutCenter.getChildren().addAll(tgLabel, rb1, rb2, rb3, viewImg);
		vblayoutCenter.setSpacing(15);
		
		VBox vblayoutLeft = new VBox();
		vblayoutLeft.setAlignment(Pos.TOP_CENTER);
		vblayoutLeft.getChildren().addAll(pathLabel, sp, darkButton);
		vblayoutLeft.setSpacing(10);
		
		Hyperlink docLink = new Hyperlink("https://www.tutorialspoint.com/dip/zooming_methods.htm");
		docLink.setOnAction(e -> 
		{
			getHostServices().showDocument(docLink.getText());
		});
		Label docLabel = new Label();
		ImageView labelImg = new ImageView("book.png");
		labelImg.setFitHeight(35);
		labelImg.setFitWidth(35);
		docLabel.setPrefSize(35, 35);
		docLabel.setGraphic(labelImg);
		
		HBox layoutBottom = new HBox();
		layoutBottom.setSpacing(5);
		layoutBottom.setAlignment(Pos.BASELINE_LEFT);
		layoutBottom.getChildren().addAll(labelImg, docLink);
		
		BorderPane layout = new BorderPane();
		layout.setTop(menuBar);
		layout.setCenter(vblayoutCenter);
		layout.setLeft(vblayoutLeft);
		layout.setBottom(layoutBottom);
		layout.setRight(list);
		
		scene = new Scene(layout, 800, 600);
		window.setScene(scene);
		window.show();
		
	}
	
	private void setDark(boolean selected) 
	{
		if(selected)
		{
			scene.getStylesheets().add("Dark.css");
		}else
		{
			scene.getStylesheets().remove("Dark.css");
		}
	}

	private String openFile()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("./"));
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.bmp");
		fileChooser.getExtensionFilters().add(filter);
		
		File file = fileChooser.showOpenDialog(window);
		String path = new String();
		if (file != null)
		{
			path = file.getPath();
			viewImg.setDisable(false);
		}

		return path;
	}
	

	private void viewImage()
	{
		ImageViewer imgV = new ImageViewer();
		imgV.setSize(imgViewerWidth, imgViewerHeight);
		imgV.setImg(filePath);
		
		if(darkButton.isSelected())
			imgV.setDark();
		
		imgV.display();
	}
	
	private void createTree(TreeItem<String> root)
	{
		if(filePath != null)
		{
			StringTokenizer st = new StringTokenizer(filePath, File.separator);
			TreeItem<String> rt = root;
			while (st.hasMoreElements())
			{
				String token = (String)st.nextElement();
				TreeItem<String> item = new TreeItem<>(token);
				item.setExpanded(true);
				rt.getChildren().add(item);
				rt = item;
			}
		}
	}
}
