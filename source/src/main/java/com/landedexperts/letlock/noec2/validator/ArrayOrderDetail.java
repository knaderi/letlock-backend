package com.landedexperts.letlock.noec2.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ArrayOrderDetail {
	public static List<OrderDetail> fromString(String input) {
		Boolean isMatch = Pattern.matches(
				"^\\s*(\\[\\s*\\]|\\[\\s*(\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*,\\s*)*\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*\\])\\s*$",
				input
			);

		List<OrderDetail> result = new ArrayList<OrderDetail>();

		if(isMatch) {
			String list = input.replaceAll("[\\[\\]\\(\\)]", "");
	
			String[] integers = list.split(",");

			System.out.println(integers);
			System.out.println(integers.length);
			System.out.println(integers.length % 2 == 0);

			if(integers.length % 2 == 0) {
				Boolean goOn = true;

				for(int i = 0; goOn && i < integers.length; i += 2) {
					String lhs = integers[i].trim();
					Integer productId = -1;
					try {
						productId = Integer.parseInt(lhs);
					} catch(NumberFormatException e) {
						goOn = false;
					}
	
					String rhs = integers[i+1].trim();
					Integer quantity = -1;
					try {
						quantity = Integer.parseInt(rhs);
					} catch(NumberFormatException e) {
						goOn = false;
					}
		
					result.add(new OrderDetail(productId, quantity));
				}
		
				if(!goOn) {
					result = new ArrayList<OrderDetail>();
				}
			}
		}

		return result;
	}
}
