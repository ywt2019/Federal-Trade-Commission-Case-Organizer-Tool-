//Wenting Yu wy2
package hw3;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddCaseView extends CaseView {
    AddCaseView(String header) {
        super(header);
    }

    /**
     * Return add case stage
     *
     * @return Stage
     */
    @Override
    Stage buildView() {
        updateButton.setText("Add Case");
        caseDatePicker.setValue(LocalDate.now());
        Scene addCaseScene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(addCaseScene);
        return stage;
    }
}
