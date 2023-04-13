//Wenting Yu wy2
package hw3;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ModifyCaseView extends CaseView{
    ModifyCaseView(String header) {
        super(header);
    }

    /**
     * Return modify case stage
     * @return Stage
     */
    @Override
    Stage buildView() {
        updateButton.setText("Modify Case");

        titleTextField.setText(CyberCop.currentCase.getCaseTitle());
        caseDatePicker.setValue(LocalDate.parse(CyberCop.currentCase.getCaseDate()));
        caseTypeTextField.setText(CyberCop.currentCase.getCaseType());
        caseNumberTextField.setText(CyberCop.currentCase.getCaseNumber());
        categoryTextField.setText(CyberCop.currentCase.getCaseCategory());
        caseLinkTextField.setText(CyberCop.currentCase.getCaseLink());
        caseNotesTextArea.setText(CyberCop.currentCase.getCaseNotes());

        Scene addCaseScene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(addCaseScene);
        return stage;
    }
}
