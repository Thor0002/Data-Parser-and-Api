package application.models;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RowTest {

	@Test
	void toDateWithNoDateTest() {
		String date = "date";
		Exception exception = Assertions.assertThrows(IllegalStateException.class,
				() -> Row.toDate(date));
		Assertions.assertEquals(exception.getMessage(), "Некоректная дата: " + date);
	}
	
	@Test
	void toIncorrectDayTest() {
		String date = "3o.мар.13";
		Exception exception = Assertions.assertThrows(IllegalStateException.class,
				() -> Row.toDate(date));
		Assertions.assertEquals(exception.getMessage(), "Некоректный день: " + date);
	}
	
	@Test
	void toIncorrectMonthTest() {
		String date = "30.март.13";
		Exception exception = Assertions.assertThrows(IllegalStateException.class,
				() -> Row.toDate(date));
		Assertions.assertEquals(exception.getMessage(), "Некоректный месяц: " + date);
	}
	
	@Test
	void toDateWithIncorrectYearTest() {
		String date = "17.апр.1a3";
		Exception exception = Assertions.assertThrows(IllegalStateException.class,
				() -> Row.toDate(date));
		Assertions.assertEquals(exception.getMessage(), "Некоректный год: " + date);
	}
	
	@Test
	void toDateWithIncorrectDateTest() {
		String date = "31.апр.13";
		Assertions.assertThrows(DateTimeException.class,() -> Row.toDate(date));
	}
	
	@Test
	void toDateWithCorrectDateTest() {
		String date = "15.мар.13";
		LocalDate result = Row.toDate(date);
		Assertions.assertEquals(result.getYear(), 2013);
		Assertions.assertEquals(result.getMonth().getValue(), 3);
		Assertions.assertEquals(result.getDayOfMonth(), 15);
	}
}
