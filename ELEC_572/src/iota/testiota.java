/*
 * Pre-image for Iota
 * 
 * Author: Saumya Mehta
 * 
 * This is a code implementing pre-image for iota function in SHA-3.
 * The code is implemented considering only 2 rounds for Iota and is written for 200 bits with w=8 and l=3.
 * Here for the purposes of this code I have implemented the the algorithm 6 from paper SHA-3 Standard: Permutation-Based Hash and Extendable-Output Functions unto rc[17]. The calculations were done seperately and only the final output have been stored into an array called iotaarray. For calculation please refer to the paper.
 * The Iota function changes only the x=0 y=0. It remains the same for every other bits.
 * First we need to reverse the bit and than perform the equation RC[(2^j) - 1] = rc[j + 14] and update the necessary bits and then reverse the string again to get the desired output.
*/

package iota;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class testiota {

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
			testiota var = new testiota();

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

			int iotaarray[] = { 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1 }; // Values from algorithm 6

			Collections.reverse(binaryvalueseperated.get(0).get(0)); // to reverse the value at x=0 y=0

			for (int j = 0; j < 4; j++) {
				int var88 = binaryvalueseperated.get(0).get(0).get((int) (Math.pow(2, j) - 1)) ^ iotaarray[j + 14]; // Algorithm
																													// for
																													// Iota
																													// Function
				binaryvalueseperated.get(0).get(0).set((int) (Math.pow(2, j) - 1), var88);
			}

			Collections.reverse(binaryvalueseperated.get(0).get(0)); // To reverse the string and get the value for the
																		// output
			for (ArrayList<ArrayList<Integer>> sublist1 : binaryvalueseperated) {
				for (ArrayList<Integer> sublist2 : sublist1) {
					var1.addAll(sublist2); // To put the final output into one List
				}
			}

			System.out.println("Final Output: " + var1 + " Size: " + var1.size());

			// To convert List<Integer> to List<String>
			List<String> tempNewList = new ArrayList<String>(var1.size());
			for (Integer myInt : var1) {
				tempNewList.add(String.valueOf(myInt));
			}

			concatinatedBinaryString = concatStrings(tempNewList); // It adds all the individual arrays from the matrix
																	// to a single list and uses concatenate function to
																	// add all the List together
			var.bitsToHexConversion(concatinatedBinaryString); // Converts the binary output to hexadecimal number

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
		return retVal;
	}

	// Function used for converting the value from hexadecimal Number to Binary
	// Value
	public static String hexToBin(String hex) throws NumberFormatException {
		// String bin = Integer.toBinaryString(Integer.parseInt(hex, 16));
		String bin = new BigInteger(hex, 16).toString(2); // To convert Hexadecimal Number to Binary
		int len = bin.length();
		return len == 8 ? bin : "00000000".substring(len) + bin; // padding the bits. Will convert the binary value to 8
																	// bits.
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
