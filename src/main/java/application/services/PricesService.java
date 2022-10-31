package application.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import application.dao.ReaderCSV;
import application.exceptions.NoDataException;
import application.models.Row;

@Component
public class PricesService {
	
	@Value("${reader.filename}")
	private String fileName;
	
	private ReaderCSV readerCSV;
	
	public void setReaderCSV(ReaderCSV readerCSV) {
		this.readerCSV = readerCSV;
	}

	private void initReader() {	
		readerCSV = new ReaderCSV(fileName);
		readerCSV.read();
	}
   
	/**
	 * Возращает цену за кокретную дату.
	 * @param date - дата.
	 */
	@Cacheable("price")
    public double price(String stringDate) throws NoDataException {
    	if(readerCSV == null) {initReader();}
    	List<Row> rows = readerCSV.getRows();
    	
    	LocalDate date = LocalDate.parse(stringDate);
    	int pos = Collections.binarySearch(rows, new Row(date, date, 0.0));
    	if(pos >= 0) {return rows.get(pos).getAveragePrice();}
    	else {
    		pos = fixPos(pos, "Цена в эту дату не известна");
    		Row row = rows.get(pos);
    		if(date.compareTo(row.getFinishDate()) < 0) {
    			return row.getAveragePrice();
    		} else {
    			throw new NoDataException("Цена на эту дату нет");
    		}
    	}
    }
    
	/**
	 * Возращает средную цену в диапозон дат.
	 * @param first - начало диапозона.
	 * @param second - конец диапозона.
	 */
	@Cacheable("average")
    public double average(String first, String second) throws NoDataException {
    	if(readerCSV == null) {initReader();}
    	List<Row> rows = readerCSV.getRows();
    	
    	LocalDate startDate = LocalDate.parse(first), finishDate = LocalDate.parse(second);
    	if(startDate.isAfter(finishDate)) {
    		throw new IllegalStateException("Дата начала периода должна быть меньше или равна дате конца периода");
    	}
    	String errorMessage = "Не все цены в этот диапазон известны";
    	int start = Collections.binarySearch(rows, new Row(startDate, startDate, 0.0));
    	int finish = Collections.binarySearch(rows, new Row(finishDate, finishDate, 0.0));
    	start = fixPos(start, errorMessage);
    	finish = fixPos(finish, errorMessage);
    	if(finishDate.isAfter(rows.get(finish).getFinishDate())) {
    		throw new NoDataException(errorMessage);
    	}
    	if(start == finish) {
    		return rows.get(start).getAveragePrice();
    	} 
    	
    	long k = getDays(startDate, rows.get(start).getFinishDate());
    	double sum = rows.get(start).getAveragePrice() * k;
    	long countDays = k;
    	k = getDays(rows.get(finish).getStartDate(), finishDate);
    	sum += rows.get(finish).getAveragePrice() * k;
    	countDays += k;
    	for(int i = start + 1; i < finish; i++) {
    		k = getDays(rows.get(i).getStartDate(), rows.get(i).getFinishDate());
    		sum += rows.get(i).getAveragePrice() * k;
    		countDays += k;
    	}
    	return sum / countDays;
    }
    
	/**
	 * Возращает Map с миниальной и максимальной ценами в диапозон дат.
	 * @param first - начало диапозона.
	 * @param second - конец диапозона.
	 */
	@Cacheable("minmax")
    public Map<String, Double> minmax(String first, String second) throws NoDataException {
    	if(readerCSV == null) {initReader();}
    	List<Row> rows = readerCSV.getRows();
    	
    	LocalDate startDate = LocalDate.parse(first), finishDate = LocalDate.parse(second);
    	if(startDate.isAfter(finishDate)) {
    		throw new IllegalStateException("Дата начала должна быть меньше или равна дате конца периода");
    	}
    	
    	String errorMessage = "Не все цены в этот диапазон известны";
    	int start = Collections.binarySearch(rows, new Row(startDate, startDate, 0.0));
    	int finish = Collections.binarySearch(rows, new Row(finishDate, finishDate, 0.0));
    	start = fixPos(start, errorMessage);
    	finish = fixPos(finish, errorMessage);
    	if(finishDate.isAfter(rows.get(finish).getFinishDate())) {
    		throw new NoDataException(errorMessage);
    	}
    	
    	double min = rows.get(start).getAveragePrice();
    	double max = min;
    	for(int i = start; i <= finish; i++) {
    		min = Math.min(min, rows.get(i).getAveragePrice());
    		max = Math.max(max, rows.get(i).getAveragePrice());
    	}
    	Map<String, Double> map = new HashMap<>();
    	map.put("min", min);
    	map.put("max", max);
    	return map;
    }
    
	/**
	 * Возращает список со всеми строками из файла.
	 */
    public List<Row> all() {
    	if(readerCSV == null) {initReader();}
    	return readerCSV.getRows();
    }
    
    /**
	 * Возращает количество дней между двумя датами.
	 */
    private long getDays(LocalDate start, LocalDate finish) {
    	return ChronoUnit.DAYS.between(start, finish) + 1;
    }
    
    private int fixPos(int pos, String errorMessage) throws NoDataException {
    	if(pos < 0) {
    		pos = -pos - 1;
    		if(pos == 0) {
    			throw new NoDataException(errorMessage);
    		}
    		pos--;
    	}
    return pos;
    }

}
