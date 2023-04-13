//Wenting Yu wy2
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList();       //a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//write your code here
		CaseReaderFactory caseReaderFactory = new CaseReaderFactory();
		CaseReader c = caseReaderFactory.createReader(filename);
		caseList.setAll(c.readCases());
		Collections.sort(caseList);
		for (Case each_c: caseList){
			caseMap.put(each_c.getCaseNumber(),each_c);
		}
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
		for (Case current_case: caseList){
			String case_year = current_case.getCaseDate().split("-")[0];
			List<Case> corresponding_list = yearMap.getOrDefault(case_year,new ArrayList<>());
			corresponding_list.add(current_case);
			yearMap.put(case_year,corresponding_list);
		}
		yearList.setAll(yearMap.keySet());
		Collections.sort(yearList);
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//write your code here
		List<Case> search_result = new ArrayList<>();

		for (Case each_c: caseList){
			if (title == null || title.equals("") || each_c.getCaseTitle().toLowerCase().contains(title.toLowerCase())){
				if (caseType == null || caseType.equals("") || each_c.getCaseType().toLowerCase().contains(caseType.toLowerCase())) {
					if (year == null || year.equals("") || each_c.getCaseDate().contains(year)){
						if (caseNumber == null || caseNumber.equals("") || each_c.getCaseNumber().toLowerCase().contains(caseNumber.toLowerCase())){
							search_result.add(each_c);
						}
					}
				}
			}
		}
		return search_result;
	}

	/**
	 * write cases to a new file
	 * @param filename
	 * @return true/false
	 */
	boolean writeCases(String filename){
		try {
			FileWriter fileWriter = new FileWriter(filename);
			BufferedWriter caseWrite = new BufferedWriter(fileWriter);

			for (Case aCase: caseList){
				caseWrite.write(aCase.getCaseDate() + "\t" + aCase.getCaseTitle() + "\t" + aCase.getCaseType() + "\t" + aCase.getCaseNumber() + "\t" + aCase.getCaseLink() + "\t" + aCase.getCaseCategory() + "\t" + aCase.getCaseNotes());
				caseWrite.newLine();
			}
			caseWrite.close();

		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
