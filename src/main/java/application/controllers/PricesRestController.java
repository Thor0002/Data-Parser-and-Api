package application.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.exceptions.NoDataException;
import application.models.Row;
import application.services.PricesService;

@RestController 
@RequestMapping("/prices")
public class PricesRestController {

    private PricesService pricesService;

    @Autowired
    public PricesRestController(PricesService pricesService) {
        this.pricesService = pricesService;
    }
    
    /**
	 * Возращает HTTP ответ с ценой за кокретную дату.
	 * @param date - дата.
	 */
    @GetMapping("/price")
    public ResponseEntity price(@RequestParam String date) {
    	try {
    		Double price = pricesService.price(date);
    		return new ResponseEntity<Double>(price, HttpStatus.OK);
    	} catch (IllegalStateException | NoDataException e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Некорректно введены дата: " + e.getMessage());
		}
    }
    
    /**
	 * Возращает HTTP ответ с средней ценой в диапозон дат.
	 * @param first - начало диапозона.
	 * @param second - конец диапозона.
	 */
    @GetMapping("/price/average")
    public ResponseEntity average(@RequestParam String first, @RequestParam String second) {
    	try {
    		Double price = pricesService.average(first, second);
    		BigDecimal pr = new BigDecimal(price).setScale(3, RoundingMode.HALF_UP);
    		return new ResponseEntity(pr.doubleValue(), HttpStatus.OK);
    	} catch (IllegalStateException | NoDataException e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Некорректно введены даты: " + e.getMessage());
		}
    }
    
    /**
	 * Возращает HTTP ответ в виде JSON с миниальной и максимальной ценами в диапозон дат.
	 * @param first - начало диапозона.
	 * @param second - конец диапозона.
	 */
    @GetMapping(path = "/price/minmax", produces = "application/json")
    public ResponseEntity minmax(@RequestParam String first, @RequestParam String second) {
    	try {
    		Map<String, Double> map = pricesService.minmax(first, second);
    		return new ResponseEntity(map, HttpStatus.OK);
    	} catch (IllegalStateException | NoDataException e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body("Некорректно введены даты: " + e.getMessage());
		}
    }	
    
    /**
	 * Возращает HTTP ответ в виде JSON со всеми строками из файла.
	 */
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity all() {
    	try {
    		List<Row> rows = pricesService.all();
    		return new ResponseEntity(rows, HttpStatus.OK);
    	} catch (IllegalStateException e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
}