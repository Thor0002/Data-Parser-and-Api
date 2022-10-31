package application.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import application.dao.ReaderCSV;
import application.exceptions.NoDataException;
import application.models.Row;

@SpringBootTest
public class PricesServiceTest {
	
	@Mock
	private ReaderCSV readerCSV;
	
	@Autowired
	private PricesService pricesService;
	
	private List<Row> getRows(){
		List<Row> rows = new ArrayList<Row>();
		rows.add(new Row(LocalDate.of(2022, 10, 20), LocalDate.of(2022, 10, 21), 20.0));
		rows.add(new Row(LocalDate.of(2022, 10, 22), LocalDate.of(2022, 10, 23), 10.0));
		rows.add(new Row(LocalDate.of(2022, 10, 24), LocalDate.of(2022, 10, 25), 30.0));
		return rows;
	}
	
	@Test
	void priceWithWrongDateTest() throws NoDataException {
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(getRows());
		Assertions.assertThrows(NoDataException.class,
				() -> pricesService.price("2022-10-19"));
	}
	
	@Test
	void priceOkTest() throws NoDataException {
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(getRows());
		double price = pricesService.price("2022-10-22");
		verify(readerCSV).getRows();
		Assertions.assertEquals(price, getRows().get(1).getAveragePrice());
	}
	
	@Test
	void averageWithWrongDateTest() throws NoDataException {
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(getRows());
		Assertions.assertThrows(NoDataException.class,
				() -> pricesService.average("2022-10-19", "2022-10-23"));
	}
	
	@Test
	void averageOkTest() throws NoDataException {
		List<Row> rows = getRows();
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(rows);
		double price = pricesService.average("2022-10-20", "2022-10-23");
		verify(readerCSV).getRows();
		Assertions.assertEquals(price, 15.0);
	}
	
	@Test
	void minmaxWithWrongDateTest() throws NoDataException {
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(getRows());
		Assertions.assertThrows(NoDataException.class,
				() -> pricesService.minmax("2022-10-19", "2022-10-23"));
	}
	
	@Test
	void minmaxOkTest() throws NoDataException {
		List<Row> rows = getRows();
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(rows);
		Map<String, Double> prices = pricesService.minmax("2022-10-21", "2022-10-24");
		verify(readerCSV).getRows();
		Assertions.assertEquals(prices.get("min"), 10.0);
		Assertions.assertEquals(prices.get("max"), 30.0);
	}
	
	@Test
	void allTest() {
		pricesService.setReaderCSV(readerCSV);
		when(readerCSV.getRows()).thenReturn(getRows());
		pricesService.all();
		verify(readerCSV).getRows();
	}

}
