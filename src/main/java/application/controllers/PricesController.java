package application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller 
@RequestMapping("/home")
public class PricesController {

	/**
	 * Начальная страница для API.
	 */
    @GetMapping()
    public String home() {
        return "home";
    }
}
