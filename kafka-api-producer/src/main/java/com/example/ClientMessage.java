package com.example;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2860119972595564289L;
	private long id;
	private String text;
	private long date;

}
