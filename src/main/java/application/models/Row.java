package application.models;

import java.time.LocalDate;

public class Row implements Comparable<Row>{
	private LocalDate startDate;
	private LocalDate finishDate;
	private double averagePrice;
	
	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}

	public double getAveragePrice() {
		return averagePrice;
	}
	
	public Row(LocalDate startDate, LocalDate finishDate, double averagePrice) {
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.averagePrice = averagePrice;
	}

	/**
	 * Конструктор для строк из файла в класс.
	 */
	public Row(String startDate, String finishDate, String averagePrice) {
		this.startDate = toDate(startDate);
		this.finishDate = toDate(finishDate);
		averagePrice = averagePrice.replace(',', '.');
		this.averagePrice = Double.parseDouble(averagePrice);
	}
	
	/**
	 * Метод сравнения строк из файла по дате начала периода.
	 */
	public int compareTo(Row r) {
		return startDate.compareTo(r.getStartDate());
	}

	/**
	 * Метод который возращает из даты вида "15.мар.13" LocaDate с этом датой.
	 * Такой формат взят из файла с данными.
	 */
	public static LocalDate toDate(String date) {
		String originalDate = date;
		date = date.replace('.', ',');
		String[] tokens = date.split(",");
		if(tokens.length != 3) {
			throw new IllegalStateException("Некоректная дата: " + originalDate);
		}
		int day, month, year;
		
		try {day = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Некоректный день: " + originalDate);
		}
		
		switch(tokens[1]) {
		case "янв":
			month = 1;
			break;
		case "фев":
			month = 2;
			break;
		case "мар":
			month = 3;
			break;
		case "апр":
			month = 4;
			break;
		case "май":
			month = 5;
			break;
		case "июн":
			month = 6;
			break;
		case "июл":
			month = 7;
			break;
		case "авг":
			month = 8;
			break;
		case "сен":
			month = 9;
			break;
		case "окт":
			month = 10;
			break;
		case "ноя":
			month = 11;
			break;
		case "дек":
			month = 12;
			break;
		default:
			throw new IllegalStateException("Некоректный месяц: " + originalDate);
		}

		try {year = 2000 + Integer.parseInt(tokens[2]);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Некоректный год: " + originalDate);
		}
		
		return LocalDate.of(year, month, day);
	}
}
