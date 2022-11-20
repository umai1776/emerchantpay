package com.transactions.payment.model.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageResponse {
	private Object data;
	private String message;
	private String details;
	private Date timestamp;

	public MessageResponse(String message) {
	    this.message = message;
	  }

}
