package theta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testtheta {

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
			testtheta var = new testtheta();

			if (hexadecimalvaluelength > 2) {
				if (hexadecimalvaluelength % 2 == 0) {
					ArrayList<String> splitvariable = splitInto(hexadecimalnumber, hexadecimalvaluelength / 2);
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
					combinationof5hexvalues.add(combinationof5hexvalues.get(0));
					combinationof5hexvalues.add(combinationof5hexvalues.get(1));
					combinationof5hexvalues.add(combinationof5hexvalues.get(2));
					combinationof5hexvalues.add(combinationof5hexvalues.get(3));
					binaryvalueseperated.add(combinationof5hexvalues);
					combinationof5hexvalues = new ArrayList<>();
				}
			}
			System.out.println("Binary value with padding bits is: " + binaryvalueseperated);
			ArrayList<Integer> vartest = new ArrayList<>();
			int vartemp1;
			int vartemp5;
			int vartemp2 = 1;
			int vartemp3 = 1;
		
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					for (int z = 0; z < 8; z++) {

						for (int q = 0; q < 5; q++) {
							vartemp1  = binaryvalueseperated.get(q).get( (((x-1) % 5 + 5) % 5) ).get(z);
							vartemp2 = (vartemp2 ^ vartemp1);
						}
					
						for (int q1 = 0; q1 < 5; q1++) {
							vartemp5  = binaryvalueseperated.get(q1).get( ((x+1)%5) ).get((((z-1) % 5 + 5) % 5));
							vartemp3 = (vartemp3 ^ vartemp5);
						}
		
						
						int vartesttheta = (binaryvalueseperated.get(y).get(x).get(z) ^ vartemp3 ^ vartemp2);
						
						var1.add(vartesttheta);
					
					}
				}
			}
			
			
			System.out.println(">>>>>>>>>>>    "   + vartest);
			System.out.println("Final Output: " + var1 + " Size: " + var1.size());

			// To convert List<Integer> to List<String>
			List<String> tempNewList = new ArrayList<String>(var1.size());
			for (Integer myInt : var1) {
				tempNewList.add(String.valueOf(myInt));
			}

			concatinatedBinaryString = concatStrings(tempNewList);
			var.bitsToHexConversion(concatinatedBinaryString);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public static String concatStrings(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s);
		}
		return sb.toString();
	}

	public static ArrayList<String> splitInto(String splitString, int parts) {
		int dlength = splitString.length() / parts;
		ArrayList<String> retVal = new ArrayList<String>();
		for (int i = 0; i < splitString.length(); i += dlength) {
			retVal.add(splitString.substring(i, i + dlength));
		}
		// System.out.println(retVal);
		return retVal;
	}

	public static String hexToBin(String hex) throws NumberFormatException {
		// String bin = Integer.toBinaryString(Integer.parseInt(hex, 16));
		String bin = new BigInteger(hex, 16).toString(2);
		int len = bin.length();
		// System.out.println("Length of Hexadecimal conversion: " + len);
		return len == 8 ? bin : "00000000".substring(len) + bin;
	}

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
