package org.anupam.ccnumgen.ccnumgen.resource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/generate")
public class CreditCardNumberGenResource {
	
private Random random = new Random(System.currentTimeMillis());
	
	/**
     
     * @param bin
     *            The bank identification number, a set digits at the start of the credit card
     *            number, used to identify the bank that is issuing the credit card.
     * @param length
     *            The total length (i.e. including the BIN) of the credit card number.
     * @return
     *            A randomly generated, valid, credit card number.
     */
	
	@GET
	@Path("/{bin}/{length}")
	@Produces(MediaType.TEXT_PLAIN)
	 public String generate(@PathParam("bin")String bin, @PathParam("length")int length) {

	        // The number of random digits that we need to generate is equal to the
	        // total length of the card number minus the start digits given by the
	        // user, minus the check digit at the end.
	        int randomNumberLength = length - (bin.length() + 1);

	        StringBuilder builder = new StringBuilder(bin);
	        for (int i = 0; i < randomNumberLength; i++) {
	            int digit = this.random.nextInt(10);
	            builder.append(digit);
	        }

	        
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	    	LocalDate localDate = LocalDate.now();
	    	System.out.println(dtf.format(localDate)); 
	        // Do the Luhn algorithm to generate the check digit.
	        int checkDigit = this.getCheckDigit(builder.toString());
	        builder.append(checkDigit).append(" ").append(dtf.format(localDate));
	        
	        


	        return builder.toString();
	    }
	 
	 
	 
	 private int getCheckDigit(String number) {

	        // Get the sum of all the digits, however we need to replace the value
	        // of the first digit, and every other digit, with the same digit
	        // multiplied by 2. If this multiplication yields a number greater
	        // than 9, then add the two digits together to get a single digit
	        // number.
	        //
	        // The digits we need to replace will be those in an even position for
	        // card numbers whose length is an even number, or those is an odd
	        // position for card numbers whose length is an odd number. This is
	        // because the Luhn algorithm reverses the card number, and doubles
	        // every other number starting from the second number from the last
	        // position.
	        int sum = 0;
	        for (int i = 0; i < number.length(); i++) {

	            // Get the digit at the current position.
	            int digit = Integer.parseInt(number.substring(i, (i + 1)));

	            if ((i % 2) == 0) {
	                digit = digit * 2;
	                if (digit > 9) {
	                    digit = (digit / 10) + (digit % 10);
	                }
	            }
	            sum += digit;
	        }

	        // The check digit is the number required to make the sum a multiple of
	        // 10.
	        int mod = sum % 10;
	        return ((mod == 0) ? 0 : 10 - mod);
	    }

}
