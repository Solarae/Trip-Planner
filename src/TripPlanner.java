import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import java.io.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.beans.binding.Bindings;
import java.util.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

public class TripPlanner extends Application {
    private Stage primaryStage;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image mapImage;
    private Label labelTripStops;
    private Label labelPossibleStops;
    private Label labelTotalMileage;
    private TextField tfCity;
    private TextField tfState;
    private TextField tfLatDegree;
    private TextField tfLatMinutes;
    private TextField tfLongDegree;
    private TextField tfLongMinutes;
    private Button btNew;
    private Button btSave;
    private Button btLoad;
    private Button btAddTripStops;
    private Button btDeleteTripStops;
    private Button btAddPossibleStops;
    private Button btDeletePossibleStops;
    private Button btUpdate;
    private Scene scene;
    private ListView<Stop> tripStops;
    private Optional<String> fileName;
    private ObservableList<Stop> tripStopsList = FXCollections.observableArrayList();
    private ObservableList<Stop> possibleStopsList = FXCollections.observableArrayList();
    static ListView<Stop> possibleStops;

    @Override
    public void start(Stage initPrimaryStage) {
        possibleStops = new ListView<>(possibleStopsList);
        PossibleStopsFileEditor.checkExistenceOfPossibleStopsFile();
        primaryStage = initPrimaryStage;
        primaryStage.setResizable(false);
        layoutGUI();
        initHandlers();
    }

    public void layoutGUI() {
        fileName = Optional.empty();
        tripStops = new ListView<>(tripStopsList);
        tfCity = new TextField();
        tfCity.textProperty().addListener((observable, oldValue, newValue) -> tfCity.setText(newValue));
        tfState = new TextField();
        tfState.textProperty().addListener((observable, oldValue, newValue) -> tfState.setText(newValue));
        tfLatDegree = new TextField();
        tfLatDegree.textProperty().addListener((observable, oldValue, newValue) ->  tfLatDegree.setText(newValue));
        tfLatMinutes = new TextField();
        tfLatMinutes.textProperty().addListener((observable, oldValue, newValue) -> tfLatMinutes.setText(newValue));
        tfLongDegree = new TextField();
        tfLongDegree.textProperty().addListener((observable, oldValue, newValue) ->  tfLongDegree.setText(newValue));
        tfLongMinutes = new TextField();
        tfLongMinutes.textProperty().addListener((observable, oldValue, newValue) -> tfLongMinutes.setText(newValue));
        btAddTripStops = new Button("+");
        btAddTripStops.disableProperty().bind(possibleStops.getSelectionModel().selectedItemProperty().isNull());
        btDeleteTripStops = new Button("-");
        btDeleteTripStops.disableProperty().bind(tripStops.getSelectionModel().selectedItemProperty().isNull());
        btAddPossibleStops = new Button("+");
        btDeletePossibleStops = new Button("-");
        btDeletePossibleStops.disableProperty().bind(possibleStops.getSelectionModel().selectedItemProperty().isNull());
        btUpdate = new Button("Update");
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #ffffe0;");
        scene = new Scene(pane, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();

        btNew = new Button("New");
        btNew.setMinWidth(100);
        btSave = new Button("Save");
        btSave.setMinWidth(100);
        btLoad = new Button("Load");
        btLoad.setMinWidth(100);
        HBox fileOptionMenu = new HBox(20);
        fileOptionMenu.setPadding(new Insets(15));
        fileOptionMenu.getChildren().addAll(btNew, btSave, btLoad);
        pane.setTop(fileOptionMenu);

        mapImage = new Image("usa_map.jpg");
        ImageView mapView = new ImageView(mapImage);
        mapView.setFitWidth(735);
        mapView.setFitHeight(380);
        canvas = new Canvas(735, 380);
        gc = canvas.getGraphicsContext2D();

        StackPane sp = new StackPane();
        sp.getChildren().addAll(mapView, canvas);
        sp.setMaxSize(735,380);
        sp.setPadding(new Insets(5));
        pane.setLeft(sp);


        labelTripStops = new Label ("Trip Stops");
        labelTripStops.setFont(new Font("System", 15));
        HBox tripStopsHeader = new HBox(5);
        tripStopsHeader.getChildren().addAll(labelTripStops, btAddTripStops, btDeleteTripStops);
        tripStopsHeader.setAlignment(Pos.CENTER_LEFT);
        labelTotalMileage = new Label("Total Mileage: 0 ");
        labelTotalMileage.setFont(new Font("System", 15));

        VBox tripStopsPane = new VBox(5);
        tripStopsPane.setMaxSize(260, 380);
        tripStopsPane.setPadding(new Insets(0,5,0,0));
        tripStopsPane.getChildren().addAll(tripStopsHeader, tripStops, labelTotalMileage);
        pane.setRight(tripStopsPane);

        labelPossibleStops = new Label ("Possible Stops");
        labelPossibleStops.setFont(new Font("System", 15));
        VBox possibleStopsPane = new VBox(8);
        possibleStopsPane.getChildren().addAll(labelPossibleStops, possibleStops);


        HBox possibleStopsHeader = new HBox(5);
        possibleStopsHeader.getChildren().addAll(btAddPossibleStops, btDeletePossibleStops);

        GridPane stopInformation = new GridPane();
        stopInformation.setHgap(10);
        stopInformation.setVgap(10);
        stopInformation.add(new Label("City:"), 0, 0);
        stopInformation.add(tfCity, 1, 0);
        stopInformation.add(new Label("State:"), 0, 1);
        stopInformation.add(tfState, 1, 1);
        stopInformation.add(new Label("Latitude Degrees:"), 0, 2);
        stopInformation.add(tfLatDegree, 1, 2);
        stopInformation.add(new Label("Latitude Minutes:"), 0, 3);
        stopInformation.add(tfLatMinutes, 1, 3);
        stopInformation.add(new Label("Longitude Degrees:"), 0, 4);
        stopInformation.add(tfLongDegree, 1, 4);
        stopInformation.add(new Label("Longitude Minutes:"), 0, 5);
        stopInformation.add(tfLongMinutes, 1, 5);
        stopInformation.add(btUpdate, 0, 6);
        btUpdate.disableProperty().bind(possibleStops.getSelectionModel().selectedItemProperty().isNull());
        btUpdate.disableProperty().bind(Bindings.createBooleanBinding( () ->
        !(ValidInputChecker.isValidLatitudeDegree(tfLatDegree.getText()) && ValidInputChecker.isValidMinutes(tfLatMinutes.getText())
                && ValidInputChecker.isValidLongitudeDegree(tfLongDegree.getText())
                && ValidInputChecker.isValidMinutes(tfLongMinutes.getText())),
                tfLatDegree.textProperty(), tfLatMinutes.textProperty(), tfLongDegree.textProperty(), tfLongMinutes.textProperty()));
        VBox stopInformationPane = new VBox();
        stopInformationPane.getChildren().addAll(possibleStopsHeader, stopInformation);


        HBox bottomPane = new HBox(20);
        bottomPane.setPadding(new Insets(20));
        bottomPane.setMaxSize(900, 350);
        bottomPane.getChildren().addAll(possibleStopsPane, stopInformationPane);
        pane.setBottom(bottomPane);
    }

    public void initHandlers() {
        btNew.setOnAction(e -> newTrip());
        btSave.setOnAction(e -> saveTrip());
        btLoad.setOnAction(e -> loadTrip());
        btAddPossibleStops.setOnAction(e -> {
            clearAllDataFields();
            possibleStops.getItems().add(new Stop());
            possibleStops.getSelectionModel().select(possibleStops.getItems().get(possibleStopsList.size() - 1));
            btUpdate.disableProperty().bind(Bindings.createBooleanBinding( () ->
                            !(ValidInputChecker.isValidCityName(tfCity.getText(), tfLatDegree.getText(),
                                    tfLatMinutes.getText(), tfLongDegree.getText(),
                                    tfLongMinutes.getText()) && ValidInputChecker.isValidLatitudeDegree(tfLatDegree.getText()) && ValidInputChecker.isValidMinutes(tfLatMinutes.getText())
                                    && ValidInputChecker.isValidLongitudeDegree(tfLongDegree.getText())
                                    && ValidInputChecker.isValidMinutes(tfLongMinutes.getText())), tfCity.textProperty(),
                    tfLatDegree.textProperty(), tfLatMinutes.textProperty(), tfLongDegree.textProperty(), tfLongMinutes.textProperty()));
        });
        btDeletePossibleStops.setOnAction(event -> deleteStopFromPossibleStops());
        btUpdate.setOnAction(e -> updateStop());
        possibleStops.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                possibleStops.getSelectionModel().clearSelection();
            } else {
                loadDataIntoTextFields(possibleStops);
            }
        });
        tripStops.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                tripStops.getSelectionModel().clearSelection();
            } else {
               loadDataIntoTextFields(tripStops);
            }
        });
        btAddTripStops.setOnAction(e -> addStopToTripStops());
        btDeleteTripStops.setOnAction(e -> deleteStopFromTripStops());
    }
    private void newTrip() {
        gc.clearRect(0,0,735,380);
        labelTotalMileage.setText("Total Mileage: 0");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New File");
        dialog.setContentText("Please name the new file:");
        fileName = dialog.showAndWait();
        fileName.ifPresent(nameOfFile -> {
            if (!(ValidInputChecker.isValidFileName(nameOfFile))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("File Name Error");
                alert.setContentText("A file with this name exists. Please enter a different name for the file.");
                alert.showAndWait();
            } else {
                tripStops.getItems().clear();
                primaryStage.setTitle("Trip Planner - " + nameOfFile);
            }
        } );
    }
    private void saveTrip() {
        if (!(fileName.isPresent())) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.dir")));
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showSaveDialog(primaryStage);
            if (selectedFile != null) {
                TripFileEditor.writeStopArrayListToFile(selectedFile.getName(), tripStops);
            }
        } else {
            fileName.ifPresent(nameOfFile -> TripFileEditor.writeStopArrayListToFile(nameOfFile + ".txt", tripStops));
        }
    }

    private void loadTrip() {
        gc.clearRect(0,0,735,380);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            tripStops.getItems().clear();
            TripFileEditor.loadStopArrayListFromTextFile(selectedFile, tripStops);
            String fileTitle = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf("."));
            primaryStage.setTitle("Trip Planner - " + fileTitle);
            fileName = Optional.of(fileTitle);
            labelTotalMileage.setText("Total Mileage: " + TripFileEditor.totalMileage(tripStops));
        }
    }
    private void updateStop() {
        possibleStops.getItems().get(getItemIndex(possibleStops)).setCity(tfCity.getText());
        possibleStops.getItems().get(getItemIndex(possibleStops)).setState(tfState.getText());
        possibleStops.getItems().get(getItemIndex(possibleStops)).setLatDegree((tfLatDegree.getText()));
        possibleStops.getItems().get(getItemIndex(possibleStops)).setLatMinutes(tfLatMinutes.getText());
        possibleStops.getItems().get(getItemIndex(possibleStops)).setLongDegree(tfLongDegree.getText());
        possibleStops.getItems().get(getItemIndex(possibleStops)).setLongMinutes(tfLongMinutes.getText());
        PossibleStopsFileEditor.sort();
        PossibleStopsFileEditor.writeStopArrayListToFile();
        possibleStops.refresh();
        btUpdate.disableProperty().bind(Bindings.createBooleanBinding( () ->
                        !(ValidInputChecker.isValidLatitudeDegree(tfLatDegree.getText()) && ValidInputChecker.isValidMinutes(tfLatMinutes.getText())
                                && ValidInputChecker.isValidLongitudeDegree(tfLongDegree.getText())
                                && ValidInputChecker.isValidMinutes(tfLongMinutes.getText())), tfCity.textProperty(),
                tfLatDegree.textProperty(), tfLatMinutes.textProperty(), tfLongDegree.textProperty(), tfLongMinutes.textProperty()));

    }

    private void deleteStopFromPossibleStops() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Are you sure about deleting this from the possible stops list?");
        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK){
            Stop deleteStop = possibleStops.getItems().get(getItemIndex(possibleStops));
            possibleStops.getItems().remove(deleteStop);
            PossibleStopsFileEditor.sort();
            PossibleStopsFileEditor.writeStopArrayListToFile();
        }
    }

    private void addStopToTripStops() {
        int tripStopIndex = getItemIndex(tripStops);
        if (tripStopIndex != -1) {
            int possibleStopIndex = getItemIndex(possibleStops);
            tripStops.getItems().add(tripStopIndex + 1, possibleStops.getItems().get(possibleStopIndex));
            drawCircle();
            drawLine();
            labelTotalMileage.setText("Total Mileage: " + TripFileEditor.totalMileage(tripStops));
        } else {
            int possibleStopIndex = getItemIndex(possibleStops);
            tripStops.getItems().add(0, possibleStops.getItems().get(possibleStopIndex));
            drawCircle();
            drawLine();
            labelTotalMileage.setText("Total Mileage: " + TripFileEditor.totalMileage(tripStops));
        }
    }

    private void deleteStopFromTripStops() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Are you sure about deleting this stop from your trip?");
        Optional<ButtonType> decision = alert.showAndWait();
        if (decision.get() == ButtonType.OK){
            Stop deleteStop = tripStops.getItems().get(getItemIndex(tripStops));
            tripStops.getItems().remove(deleteStop);
            drawCircle();
            drawLine();
            labelTotalMileage.setText("Total Mileage: " + TripFileEditor.totalMileage(tripStops));
        }
    }

    private int getItemIndex(ListView<Stop> stopList) {
            return stopList.getSelectionModel().getSelectedIndex();
    }
    private void loadDataIntoTextFields(ListView<Stop> stopList) {
        try {
            int selectedIndex = getItemIndex(stopList);
            String[] stopData = stopList.getItems().get(selectedIndex).toFile().split("\\|");
            tfCity.setText(stopData[0]);
            tfState.setText(stopData[1]);
            tfLatDegree.setText(stopData[2].substring(0,stopData[2].lastIndexOf("\u00b0")));
            tfLatMinutes.setText(stopData[3].substring(0,stopData[3].lastIndexOf("'")));
            tfLongDegree.setText(stopData[4].substring(0,stopData[4].lastIndexOf("\u00b0")));
            tfLongMinutes.setText((stopData[5].substring(0,stopData[5].lastIndexOf("'"))));
        } catch (ArrayIndexOutOfBoundsException outOfBounds) {System.out.println(stopList.getSelectionModel().getSelectedItem()); }
    }

    private void clearAllDataFields() {
        tfCity.clear();
        tfState.clear();
        tfLatDegree.clear();
        tfLatMinutes.clear();
        tfLongDegree.clear();
        tfLongMinutes.clear();
    }

    private void drawCircle() {
        gc.clearRect(0,0,735,380);
        gc.setFill(Color.WHITE);
        for (int i = 0; i < tripStops.getItems().size(); i++ ) {
            gc.fillOval(tripStops.getItems().get(i).getCircleXCoordinate() - 2.5,
                    tripStops.getItems().get(i).getCircleYCoordinate() - 2.5, 5, 5);
        }
    }
    private void drawLine() {
        if (tripStops.getItems().size() > 1) {
            for (int i = 0; i < tripStops.getItems().size() - 1; i++) {
                gc.setStroke(Color.WHITE);
                gc.setLineWidth(2);
                gc.strokeLine(tripStops.getItems().get(i).getCircleXCoordinate(), tripStops.getItems().get(i).getCircleYCoordinate()
                        , tripStops.getItems().get(i + 1).getCircleXCoordinate(), tripStops.getItems().get(i + 1).getCircleYCoordinate());
            }
        }
    }

}
