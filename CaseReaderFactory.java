//Wenting Yu wy2
package hw3;

public class CaseReaderFactory {
    /**
     * Instantiate a case reader based on input file type and return the case reader
     * @param filename
     * @return CaseReader
     */
    CaseReader createReader(String filename) {

        CaseReader caseReader = null;

        if (filename.endsWith(".csv")) {
            caseReader = new CSVCaseReader(filename);
        } 
        else if (filename.endsWith(".tsv")) {
            caseReader = new TSVCaseReader(filename);
        }
        
        return caseReader;
    }
}
