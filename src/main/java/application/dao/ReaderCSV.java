package application.dao;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import application.models.Row;

public class ReaderCSV {

	private String fileName;
	
	private List<Row> rows = new ArrayList<Row>();

	public ReaderCSV(String fileName) {
		this.fileName = fileName;
	}
	
	public List<Row> getRows() {
		return rows;
	}

	public void read() {
		try {
			CSVReader reader = new CSVReader(new FileReader(fileName, StandardCharsets.UTF_8));
			String[] nextline = reader.readNext();
			while ((nextline = reader.readNext()) != null) {
				if (nextline != null) {
					if(nextline.length != 3 ) {
						String message = "Некорректная строка в файле.\n" 
					+ "Ожидается: <Начало периода>, <Конец периода>, <Средняя цена>";
						throw new IllegalStateException(message);
					}
					Row r = new Row(nextline[0], nextline[1], nextline[2]);
					rows.add(r);
				}
			}
			Collections.sort(rows);
		} catch (CsvValidationException | IOException e) {
			throw new IllegalStateException("Не удалось считать строку с файла: " + e.getMessage());
		}
	}
}