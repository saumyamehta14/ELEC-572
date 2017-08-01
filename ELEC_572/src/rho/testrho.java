/*
 * Pre-image for Rho Function
 * 
 * Author: Saumya Mehta
 * 
 * This is the program for rho function of SHA-3. It is  used to calculate the pre-image of the function.
 * The Rho functions gets shifted by a certain number of bits from input to output. So for calculating the pre-image we shifts the bits by a certain number as defined by the table in the report attached with the solution.
 * 		X=3	X=4	X=0	X=1	X=2
 *	Y=2	1	7	3	2	3
 *	Y=1	7	4	4	4	6
 *	Y=0	4	3	0	1	6
 *	Y=4	0	6	2	2	5
 *	Y=3	5	0	1	5	7
 *
 * Note: The bits do not get reversed for the binary values. It will produce the desired ouput if the bits are shifted by the above table.
 */
package rho;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testrho {

	public static void main(String[] args) {

		try {
			System.out.println("Please enter Hexadecimal number : ");

			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String hexadecimalnumber = scanner.nextLine();
			int hexadecimalvaluelength = hexadecimalnumber.length();
			String concatinatedBinaryString;

			ArrayList<String> hexadecimalcombinednumber = new ArrayList<String>();
			ArrayList<ArrayList<ArrayList<Integer>>> binaryvalueseperated = new ArrayList<ArrayList<ArrayList<Integer>>>();
			ArrayList<ArrayList<Integer>> combinationof5hexvalues = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> individualbinarybits = new ArrayList<Integer>();
			ArrayList<Integer> var1 = new ArrayList<>();
			testrho var = new testrho();

			// This will take only even number of hexadecimal values and will give error for
			// odd number of hexadecimal values
			if (hexadecimalvaluelength > 2) {
				if (hexadecimalvaluelength % 2 == 0) {
					ArrayList<String> splitvariable = splitInto(hexadecimalnumber, hexadecimalvaluelength / 2); // Will create a group of hexadecimal values (eg: 2fabcd32 will be gouped as (2f, ab, cd , 32))
					for (String t : splitvariable) {
						hexadecimalcombinednumber.add(hexToBin(t));
					}
				} else {
					System.out.println("Wrong Format!");
				}
			} else {
				System.out.println(hexToBin(hexadecimalnumber));
			}

			System.out.println("Binary conversion is: " + hexadecimalcombinednumber);

			// For converting from List<String> to List<Integer> and making sure that the
			// zeros at the front gets added too. This function will create the final input
			// that will be required for calculation purposes. The input is constructed ina
			// a way that it will take [[[[0,0,0] ----- [0,0,8]] ------ [4,0,8]], [ [[0,1,0]
			// ---- [0,1,8]] ----- [4,1,8]] ----- [4,4,8]]
			for (int i = 0; i < hexadecimalcombinednumber.size(); i++) {
				for (int j = 0; j < 8; j++) {
					if (individualbinarybits.size() > 7) {
						individualbinarybits = new ArrayList<>();
					}
					String individualbitsfrombinaryvalues = hexadecimalcombinednumber.get(i).substring(j, j + 1);
					int convertingtointeger = Integer.parseInt(individualbitsfrombinaryvalues);
					individualbinarybits.add(convertingtointeger);
				}
				combinationof5hexvalues.add(individualbinarybits);
				if (combinationof5hexvalues.size() >= 5) {
					binaryvalueseperated.add(combinationof5hexvalues);
					combinationof5hexvalues = new ArrayList<>();
				}
			}
			System.out.println("Binary value with padding bits is: " + binaryvalueseperated);

			int table[] = { 0, 1, 6, 4, 3, 4, 4, 6, 7, 4, 3, 2, 3, 1, 7, 1, 5, 7, 5, 0, 2, 2, 5, 0, 6 }; // Values for the table stored in an array which can be used later for calculation purposes.

			for (int b = 0; b < 25; b++) {
				for (int a = 0; a < 8; a++) {
					ArrayList<Integer> temp_var_rho_plane = binaryvalueseperated.get(b / 5).get(b % 5); // Algorithm For Rho function
					int individual_rho_bits = temp_var_rho_plane.get((a + (8 - table[b])) % 8); // The bits gets shifted by the table mentioned above.
					var1.add(individual_rho_bits); // To add all the bits to a single list
				}
			}

			System.out.println("Final Output: " + var1 + " Size: " + var1.size());

			// To convert List<Integer> to List<String>
			List<String> tempNewList = new ArrayList<String>(var1.size());
			for (Integer myInt : var1) {
				tempNewList.add(String.valueOf(myInt));
			}

			concatinatedBinaryString = concatStrings(tempNewList); // It adds all the individual arrays from the matrix to a single list and uses concatenate function to add all the List together
			var.bitsToHexConversion(concatinatedBinaryString); // Final output in hexadecimal number

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	// To concatenate strings
	public static String concatStrings(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s);
		}
		return sb.toString();
	}

	// Used to split the string into desired number of parts. Used mainly for
		// separating the hexadecimal value into several parts.
	public static ArrayList<String> splitInto(String splitString, int parts) {
		int dlength = splitString.length() / parts;
		ArrayList<String> retVal = new ArrayList<String>();
		for (int i = 0; i < splitString.length(); i += dlength) {
			retVal.add(splitString.substring(i, i + dlength));
		}
		// System.out.println(retVal);
		return retVal;
	}

	// Function used for converting the value from hexadecimal Number to Binary Value
	public static String hexToBin(String hex) throws NumberFormatException {
		// String bin = Integer.toBinaryString(Integer.parseInt(hex, 16));
		String bin = new BigInteger(hex, 16).toString(2); // To convert Hexadecimal Number to Binary
		int len = bin.length();
		// System.out.println("Length of Hexadecimal conversion: " + len);
		return len == 8 ? bin : "00000000".substring(len) + bin; // padding the bits. Will convert the binary value to 8 bits.
	}

	// Function used to reverse the string
	public static String reverse(String input) {
		char[] in = input.toCharArray();
		int begin = 0;
		int end = in.length - 1;
		char temp;
		while (end > begin) {
			temp = in[begin];
			in[begin] = in[end];
			in[end] = temp;
			end--;
			begin++;
		}
		return new String(in);
	}

	// Function used to converts the numbers from binary to hexadecimal value
	private void bitsToHexConversion(String bitStream) {

		int byteLength = 4;
		int bitStartPos = 0, bitPos = 0;
		String hexString = "";
		int sum = 0;

		// pad '0' to make input bit stream multiple of 4

		if (bitStream.length() % 4 != 0) {
			int tempCnt = 0;
			int tempBit = bitStream.length() % 4;
			while (tempCnt < (byteLength - tempBit)) {
				bitStream = "0" + bitStream;
				tempCnt++;
			}
		}

		// Group 4 bits, and find Hex equivalent

		while (bitStartPos < bitStream.length()) {
			while (bitPos < byteLength) {
				sum = (int) (sum + Integer.parseInt("" + bitStream.charAt(bitStream.length() - bitStartPos - 1))
						* Math.pow(2, bitPos));
				bitPos++;
				bitStartPos++;
			}
			if (sum < 10)
				hexString = Integer.toString(sum) + hexString;
			else
				hexString = (char) (sum + 55) + hexString;

			bitPos = 0;
			sum = 0;
		}
		System.out.println("Hex String > " + hexString);
	}

}
