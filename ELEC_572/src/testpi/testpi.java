/*
 * Pre-image for Pi Function.
 * 
 * Author: Saumya Mehta
 * 
 * This program is used for calculating pre-image for the pi function for SHA-3. The pre-image conversion is done using the equation A[x,y,z]=A'[y',2(x'-y'+ 5)mod5,z]. Here the thing to consider is that while calculating the modulus for 5 sometimes the value of (x' - y') is negative and java does produces negative value for the modulus of a negative number (eg: -1mod5 = -1). So for calculation purposes we are adding 5 to the value so that we do not get negative values.
 * Here for the purposes of this code we are not reversing the bits since the operation is carried out on a plane, non need to reverse the bits since after the final operation the bits will be reversed again and it will produce the same output as obtained without reversing the bits.
 */
package testpi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testpi {

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
			testpi var = new testpi();

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

			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					for (int z = 0; z < 8; z++) {
						Integer tempvar = binaryvalueseperated.get((2 * (x - y + 5)) % 5).get(y).get(z); // Function for Pi
						var1.add(tempvar); // To put the final output into one List
					}
				}
			}

			System.out.println("Final Output: " + var1 + " Size: " + var1.size());

			// To convert List<Integer> to List<String>
			List<String> tempNewList = new ArrayList<String>(var1.size());
			for (Integer myInt : var1) {
				tempNewList.add(String.valueOf(myInt));
			}

			concatinatedBinaryString = concatStrings(tempNewList);// It adds all the individual arrays from the matrix to a single list and uses concatenate function to add all the List together
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
