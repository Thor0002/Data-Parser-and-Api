package application.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import application.exceptions.NoDataException;
import application.models.Row;
import application.services.PricesService;

@SpringBootTest
public class PricesRestControllerTest {

	@MockBean
	private PricesService pricesService;
	
	@Autowired
	private PricesRestController pricesRestController;
	
	@Test
	void priceTest() throws NoDataException {
		Double price = 10.0;
		String date = "date";
		when(pricesService.price(any(String.class))).thenReturn(price);
		ResponseEntity<Double> response = pricesRestController.price(date);
		verify(pricesService).price(eq(date));
		Assertions.assertEquals(response.getBody(), price);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void averageTest() throws NoDataException {
		Double price = 10.0;
		String first = "first", second = "second";
		when(pricesService.average(any(String.class), any(String.class))).thenReturn(price);
		ResponseEntity<Double> response = pricesRestController.average(first, second);
		verify(pricesService).average(eq(first), eq(second));
		Assertions.assertEquals(response.getBody(), price);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void minmaxTest() throws NoDataException {
		Map<String, Double> prices = new HashMap<>();
		prices.put("min", 1.0); prices.put("max", 2.0);
		String first = "first", second = "second";
		when(pricesService.minmax(any(String.class), any(String.class))).thenReturn(prices);
		ResponseEntity response = pricesRestController.minmax(first, second);
		verify(pricesService).minmax(eq(first), eq(second));
		Assertions.assertEquals(response.getBody(), prices);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	void allTest() {
		when(pricesService.all()).thenReturn(new ArrayList<Row>());
		ResponseEntity response = pricesRestController.all();
		verify(pricesService).all();
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
}
