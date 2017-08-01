/*
 * Pre-image for Chi function
 * 
 * Author: Saumya Mehta
 * 
 * This  is the program for pre-image calculation for Chi Function. Chi pre-image calculation takes into considerations the five bits of a row into consideration and will perform the functions accordingly.
 * 
 * Steps for performing the calculation.
 * 1. Take the first five bits of the row and it will produce the first five bit of the output.
 * 2. Do the first step for z=0 to z=7
 * 3. Now take the next five bits in the row (i.e the bits will get shifted by 1 and produce the next five bits ) and that will produce the next bit for the output.
 * 4. The process will continue for the five bits (i.e Every time for the new output we will shifts the bits by 1 from the previous input) till it produces five output.
 * 5. For next row again repeat the steps from 1 to 4. 
 * 6. After calculating the output for every bit put them into a truth table and we will find a equation that will be equal to y = AB + AC'E' + AC'D + A'B'CE'+ A'B'CD + ACD'E + A'B'C'D'E (Minimized form)
 */
package CHI;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testchi {

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
			testchi var = new testchi();

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
					
					// Since the calculation requires wrapping of the bits for the output in the row instead of taking the modulus of the row, I have added the first four bits at the end again so that the requirment of wrapping the data gets fullfilled.
					combinationof5hexvalues.add(combinationof5hexvalues.get(0)); // Add the first bit of the row to the end
					combinationof5hexvalues.add(combinationof5hexvalues.get(1)); // Add the second bit to the end of the row
					combinationof5hexvalues.add(combinationof5hexvalues.get(2)); // Add the third bit to the end of the row
					combinationof5hexvalues.add(combinationof5hexvalues.get(3)); // Add the fourth bit to the row
					binaryvalueseperated.add(combinationof5hexvalues);
					combinationof5hexvalues = new ArrayList<>();
				}
			}
			System.out.println("Binary value with padding bits is: " + binaryvalueseperated);
			for (int i = 0; i < binaryvalueseperated.size(); i++) {
				for (int j = 0; j < 5; j++) {
					for (int k = 0; k < 8; k++) {

						int A = binaryvalueseperated.get(i).get(j).get(k);
						int B = binaryvalueseperated.get(i).get(j + 1).get(k);
						int C = binaryvalueseperated.get(i).get(j + 2).get(k);
						int D = binaryvalueseperated.get(i).get(j + 3).get(k);
						int E = binaryvalueseperated.get(i).get(j + 4).get(k);

						int var10 = (A & B) | (A & (C ^ 1) & (E ^ 1)) | (A & (C ^ 1) & D)
								| ((A ^ 1) & (B ^ 1) & C & (E ^ 1)) | ((A ^ 1) & (B ^ 1) & C & D)
								| (A & C & (D ^ 1) & E) | ((A ^ 1) & (B ^ 1) & (C ^ 1) & (D ^ 1) & E); // Algorithm for Chi Function

						var1.add(var10);
					}
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
