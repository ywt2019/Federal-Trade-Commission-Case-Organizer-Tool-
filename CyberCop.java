//Wenting Yu wy2
package hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CyberCop extends Application {

    public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
    public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
    public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

    CCView ccView = new CCView();
    CCModel ccModel = new CCModel();

    CaseView caseView; //UI for Add/Modify/Delete menu option

    GridPane cyberCopRoot;
    Stage stage;

    static Case currentCase; //points to the case selected in TableView.

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * start the application and show the opening scene
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("Cyber Cop");
        cyberCopRoot = ccView.setupScreen();
        setupBindings();
        Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
        primaryStage.show();
    }

    /**
     * setupBindings() binds all GUI components to their handlers.
     * It also binds disableProperty of menu items and text-fields
     * with ccView.isFileOpen so that they are enabled as needed
     */
    void setupBindings() {

        ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
        ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
        ccView.saveFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());

        ccView.openFileMenuItem.setOnAction(new OpenMenuItemHandler());
        ccView.closeFileMenuItem.setOnAction(new CloseMenuItemHandler());
        ccView.saveFileMenuItem.setOnAction(new SaveFileMenuItemHandler());
        ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());

        ccView.exitMenuItem.setOnAction(new ExitMenuItemHandler());
        ccView.searchButton.setOnAction(new SearchButtonHandler());
        ccView.clearButton.setOnAction(new ClearButtonHandler());

        ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
        ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
        ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());

        ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
            if (newValue != null) {
                int index = 0;
                for (Case aCase : ccModel.caseList) {
                    if (aCase.getCaseNumber().equals(newValue.getCaseNumber())) {

                        currentCase = newValue;

                        ccView.titleTextField.setText(currentCase.getCaseTitle());
                        ccView.caseTypeTextField.setText(currentCase.getCaseType());
                        ccView.yearComboBox.setValue(currentCase.getCaseDate().split("-")[0]);
                        ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
                        ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

                        if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
                            URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
                            if (url != null) ccView.webEngine.load(url.toExternalForm());
                        } else if (currentCase.getCaseLink().toLowerCase().startsWith("http")) {  //if external link
                            ccView.webEngine.load(currentCase.getCaseLink());
                        } else {
                            URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
                            if (url != null) ccView.webEngine.load(url.toExternalForm());
                        }
                    }
                }
            }
        });
    }

    private class OpenMenuItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            File file = null;
            if ((file = fileChooser.showOpenDialog(stage)) != null) {
                stage.setTitle(APP_TITLE + ": " + file.getName());
                ccModel.readCases(file.getAbsolutePath());
                ccModel.buildYearMapAndList();
                ccView.caseTableView.setItems(ccModel.caseList);
                ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
                //set combobox
                ccView.yearComboBox.setItems(ccModel.yearList);

                //set default case
                ccView.caseTableView.getSelectionModel().selectFirst();

                ccView.isFileOpen.setValue(true);
            }
        }
    }

    private class CloseMenuItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            ccModel.caseList.clear();
            ccModel.caseMap.clear();
            ccModel.yearList.clear();
            ccModel.yearMap.clear();

            ccView.titleTextField.clear();
            ccView.caseTypeTextField.clear();
            ccView.yearComboBox.setValue(null);
            ccView.caseNumberTextField.clear();
            ccView.caseNotesTextArea.clear();
            ccView.caseTableView.setItems(null);

            currentCase = null;
            ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());

            ccView.isFileOpen.setValue(false);
        }
    }

    private class ExitMenuItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            Platform.exit();
        }
    }

    private class SearchButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            List<Case> searchedCaseList = ccModel.searchCases(ccView.titleTextField.getText(), ccView.caseTypeTextField.getText(), ccView.yearComboBox.getValue(), ccView.caseNumberTextField.getText());

            ObservableList<Case> searchedCaseObservableList = FXCollections.observableArrayList();
            searchedCaseObservableList.setAll(searchedCaseList);
//need some fix
            if (ccView.titleTextField.getText().isEmpty() && ccView.caseTypeTextField.getText().isEmpty() && ccView.yearComboBox.getValue() == null && ccView.caseNumberTextField.getText().isEmpty()) {
                ccView.caseTableView.setItems(ccModel.caseList);
                ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
            } else {
                ccView.caseTableView.setItems(searchedCaseObservableList);
                ccView.messageLabel.setText(searchedCaseObservableList.size() + " cases");
            }

            ccView.caseTableView.getSelectionModel().selectFirst();
        }
    }

    private class ClearButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            ccView.titleTextField.clear();
            ccView.caseTypeTextField.clear();
            ccView.yearComboBox.setValue(null);
            ccView.caseNumberTextField.clear();
        }
    }

    private class CaseMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            String button_name = ((MenuItem) actionEvent.getSource()).getText();
            switch (button_name) {
                case "Add case":
                    caseView = new AddCaseView("Add Case");
                    caseView.buildView().show();
                    caseView.updateButton.setOnAction(e -> {
                        Case addedCase = null;
                        if (caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).isBlank() || caseView.titleTextField.getText().isBlank() || caseView.caseTypeTextField.getText().isBlank() || caseView.caseNumberTextField.getText().isBlank()) {
                            try {
                                throw new DataException("Case must have date, title, type, and number");
                            } catch (DataException d) {
                            }
                        } else if (ccModel.caseMap.containsKey(caseView.caseNumberTextField.getText())) {
                            try {
                                throw new DataException("Duplicate case number");
                            } catch (DataException d) {
                            }
                        } else {
                            addedCase = new Case(caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), caseView.titleTextField.getText(), caseView.caseTypeTextField.getText(), caseView.caseNumberTextField.getText(), caseView.caseLinkTextField.getText(), caseView.categoryTextField.getText(), caseView.caseNotesTextArea.getText());
                            ccModel.caseList.add(addedCase);
                            ccModel.caseMap.put(caseView.caseNumberTextField.getText(), addedCase);
                            ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
                        }
                    });
                    break;
                case "Modify case":
                    caseView = new ModifyCaseView("Modify Case");
                    caseView.buildView().show();
                    caseView.updateButton.setOnAction(e -> {
                        int index_of_currentCase = ccModel.caseList.indexOf(currentCase);

                        String new_date = caseView.caseDatePicker.getValue().toString();
                        String new_title = caseView.titleTextField.getText();
                        String new_type = caseView.caseTypeTextField.getText();
                        String new_number = caseView.caseNumberTextField.getText();
                        String new_link = caseView.caseLinkTextField.getText();
                        String new_category = caseView.categoryTextField.getText();
                        String new_notes = caseView.caseNotesTextArea.getText();

                        if (caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).isBlank() || caseView.titleTextField.getText().isBlank() || caseView.caseTypeTextField.getText().isBlank() || caseView.caseNumberTextField.getText().isBlank()) {
                            try {
                                throw new DataException("Case must have date, title, type, and number");
                            } catch (DataException d) {
                            }
                        } else if (!currentCase.getCaseNumber().equals(caseView.caseNumberTextField.getText()) && ccModel.caseMap.containsKey(caseView.caseNumberTextField.getText())) {
                            try {
                                throw new DataException("Duplicate case number");
                            } catch (DataException d) {
                            }
                        } else {
                            ccModel.caseList.get(index_of_currentCase).setCaseDate(new_date);
                            ccModel.caseList.get(index_of_currentCase).setCaseTitle(new_title);
                            ccModel.caseList.get(index_of_currentCase).setCaseType(new_type);
                            ccModel.caseList.get(index_of_currentCase).setCaseNumber(new_number);
                            ccModel.caseList.get(index_of_currentCase).setCaseLink(new_link);
                            ccModel.caseList.get(index_of_currentCase).setCaseCategory(new_category);
                            ccModel.caseList.get(index_of_currentCase).setCaseNotes(new_notes);
                        }
                    });
                    break;
                case "Delete case":
                    caseView = new DeleteCaseView("Delete Case");
                    caseView.buildView().show();
                    caseView.updateButton.setOnAction(e -> {
                        ccModel.caseList.remove(currentCase);
                        ccModel.caseMap.remove(currentCase.getCaseNumber());
                        ccView.messageLabel.setText(ccModel.caseList.size() + " cases");
                    });
                    break;
                default:
                    break;
            }
            caseView.closeButton.setOnAction(e -> caseView.stage.close());
            caseView.clearButton.setOnAction(e -> {
                caseView.titleTextField.clear();
                caseView.caseDatePicker.setValue(null);
                caseView.caseTypeTextField.clear();
                caseView.caseNumberTextField.clear();
                caseView.categoryTextField.clear();
                caseView.caseLinkTextField.clear();
                caseView.caseNotesTextArea.clear();
            });
        }
    }

    /**
     * show the save dialogue and save file when the save button is triggered
     */
    private class SaveFileMenuItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
            fileChooser.setTitle("Save file");
            File f = fileChooser.showSaveDialog(stage);

            if (ccModel.writeCases(f.getAbsolutePath())) {
                ccView.messageLabel.setText(f.getName() + " saved");
            } else {
                ccView.messageLabel.setText("Save failed");
            }
        }
    }

    /**
     * Invoke the FTC Case Count Chart
     */
    private class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            ccView.showChartView(ccModel.yearMap);
        }
    }
}

