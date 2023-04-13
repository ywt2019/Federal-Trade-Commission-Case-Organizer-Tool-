//Wenting Yu wy2
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{

    private final StringProperty caseDate = new SimpleStringProperty();
    private final StringProperty caseTitle = new SimpleStringProperty();
    private final StringProperty caseType = new SimpleStringProperty();
    private final StringProperty caseNumber = new SimpleStringProperty();
    private final StringProperty caseLink = new SimpleStringProperty();
    private final StringProperty caseCategory = new SimpleStringProperty();
    private final StringProperty caseNotes = new SimpleStringProperty();

    Case(){
        caseDate.set("");
        caseTitle.set("");
        caseType.set("");
        caseNumber.set("");
        caseLink.set("");
        caseCategory.set("");
        caseNotes.set("");
    }

    Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory, String caseNotes) {
        this.caseDate.set(caseDate);
        this.caseTitle.set(caseTitle);
        this.caseType.set(caseType);
        this.caseNumber.set(caseNumber);
        this.caseLink.set(caseLink);
        this.caseCategory.set(caseCategory);
        this.caseNotes.set(caseNotes);
    }
    //get functions
    public final String getCaseDate() { return caseDate.get(); }
    public final String getCaseTitle() { return caseTitle.get(); }
    public final String getCaseType() { return caseType.get(); }
    public final String getCaseNumber() { return caseNumber.get(); }
    public final String getCaseLink() { return caseLink.get(); }
    public final String getCaseCategory() { return caseCategory.get(); }
    public final String getCaseNotes() { return caseNotes.get(); }

    //set functions
    public final void setCaseDate(String caseDate) { this.caseDate.set(caseDate); }
    public final void setCaseTitle(String caseTitle) { this.caseTitle.set(caseTitle); }
    public final void setCaseType(String caseType) { this.caseType.set(caseType); }
    public final void setCaseNumber(String caseNumber) { this.caseNumber.set(caseNumber); }
    public final void setCaseLink(String caseLink) { this.caseLink.set(caseLink); }
    public final void setCaseCategory(String caseCategory) { this.caseCategory.set(caseCategory); }
    public final void setCaseNotes(String caseNotes) { this.caseNotes.set(caseNotes); }

    public final StringProperty caseDateProperty() { return caseDate; }
    public final StringProperty caseTitleProperty() { return caseTitle; }
    public final StringProperty caseTypeProperty() { return caseType; }
    public final StringProperty caseNumberProperty() { return caseNumber; }
    public final StringProperty caseLinkProperty() { return caseLink; }
    public final StringProperty caseCategoryProperty() { return caseCategory; }
    public final StringProperty caseNotesProperty() { return caseNotes; }

    @Override
    public int compareTo(Case c) {
        String[] this_date_string = this.getCaseDate().split("-");
        int this_year = Integer.parseInt(this_date_string[0]);
        int this_month = Integer.parseInt(this_date_string[1]);
        int this_day = Integer.parseInt(this_date_string[2]);

        String[] date_string = c.getCaseDate().split("-");
        int year = Integer.parseInt(date_string[0]);
        int month = Integer.parseInt(date_string[1]);
        int day = Integer.parseInt(date_string[2]);

        if (this_year > year) return -1;
        else if (this_year < year) return 1;
        else { //equal years
            if (this_month > month) return -1;
            else if (this_month < month) return 1;
            else { //equal months
                if (this_day > day) return -1;
                else if (this_day < day) return 1;
                else return 0;
            }
        }
    }

    @Override
    public String toString() {
        return this.getCaseNumber();
    }
}
