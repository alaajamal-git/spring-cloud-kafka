package com.example;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyMessage implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5646651237776997413L;
	private long id;
	private boolean flag;
	private long date;
	private long recieverDate;

}
