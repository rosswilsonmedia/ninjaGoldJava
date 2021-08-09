package com.codingdojo.ninjagold;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoldRoutes {

	
	@RequestMapping("/")
	public String home(HttpSession session, Model model) {
		if(session.getAttribute("gold")==null || session.getAttribute("gold").equals("")) {
			session.setAttribute("gold", 0);
			ArrayList<String> emptyArr = new ArrayList<String>();
			emptyArr.add("");
			session.setAttribute("activities", emptyArr);
		}
		
		model.addAttribute("gold", session.getAttribute("gold"));
		model.addAttribute("activities", session.getAttribute("activities"));
		return "index.jsp";
	}
	
	@RequestMapping(value="/gold", method=RequestMethod.POST)
	public String findGold(HttpSession session, @RequestParam(value="type") String type) {
		Random random = new Random();
		int max=0;
		int min=0;
		if(type.equals("farm")) {
			max=20;
			min=10;
		} else if (type.equals("cave")){
			max=10;
			min=5;
		} else if (type.equals("house")) {
			max=5;
			min=2;
		} else if (type.equals("casino")) {
			max=50;
			min=-50;
		}
		
		int amount = random.nextInt(max+1-min)+min;
		String message = "";
		if(amount>=0) {
			message = "You gained " + amount + " gold from the " + type;
		} else {
			message = "You lost " + amount + " gold from the " + type;
		}
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy hh':'mm aa");
		message += " (" + dateFormat.format(date) + ")";
		
		System.out.println(message);
		
		session.setAttribute("gold", (int)session.getAttribute("gold")+amount);
		ArrayList<String> activities = (ArrayList<String>)session.getAttribute("activities");
		activities.add(0, message);
		session.setAttribute("activities", activities);
		
		return "redirect:/";
	}
}
