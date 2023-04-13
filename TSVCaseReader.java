//Wenting Yu wy2
package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSVCaseReader extends CaseReader{


    int num_of_missing_cases;
    boolean dataIsMissing;
    boolean caseIsOk = true;

    TSVCaseReader(String filename) { super(filename);}

    /**
     * read cases from file, store the cases in a case list and return it
     * @return List of cases
     */
    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();

        try {
            Scanner input = new Scanner(new File(filename));
            StringBuilder fileContent = new StringBuilder();

            while (input.hasNextLine()) {
                fileContent.append(input.nextLine() + "\n");
            }

            String[] fileData = fileContent.toString().split("\n");

            for (String c: fileData){
                String[] c_info = c.split("\t"); //check if this tab separate works
                Case c_new = new Case();

                for (int i = 0; i < c_info.length; i++){
                    switch(i){
                        case 0:
                            if (c_info[0].isBlank()){
                                caseIsOk = false;
                            } else {
                                c_new.setCaseDate(c_info[0].trim());
                            }
                            break;
                        case 1:
                            if (c_info[1].isBlank()){
                                caseIsOk = false;
                            } else {
                                c_new.setCaseTitle(c_info[1].trim());
                            }
                            break;
                        case 2:
                            if (c_info[2].isBlank()){
                                caseIsOk = false;
                            } else {
                                c_new.setCaseType(c_info[2].trim());
                            }
                            break;
                        case 3:
                            if (c_info[3].isBlank()){
                                caseIsOk = false;
                            } else {
                                c_new.setCaseNumber(c_info[3].trim());
                            }
                            break;
                        case 4: c_new.setCaseLink(c_info[4].trim()); break;
                        case 5: c_new.setCaseCategory(c_info[5].trim()); break;
                        case 6: c_new.setCaseNotes(c_info[6].trim()); break;
                        default: break;
                    }
                }
                if (caseIsOk) {
                    caseList.add(c_new);
                }
                else {
                    num_of_missing_cases++;
                    dataIsMissing = true;
                }
                caseIsOk = true;
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
            if (dataIsMissing){
                throw new DataException(num_of_missing_cases + " cases rejected.\nThe file must have cases with\ntab separated date, title, type, and case number!");
            }
        }
        catch (DataException d){
        }

        return caseList;
    }
}
