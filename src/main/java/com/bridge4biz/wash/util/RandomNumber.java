package com.bridge4biz.wash.util;

import java.util.Random;

public class RandomNumber {
	public static String GetPassword(int length) {
		char[] charaters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder sb = new StringBuilder("");
		Random rn = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(charaters[rn.nextInt(charaters.length)]);
		}
		return "p" + sb.toString();
	}

	public static String GetAuthCode(int length) {
		char[] charaters = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder sb = new StringBuilder("");
		Random rn = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(charaters[rn.nextInt(charaters.length)]);
		}
		return sb.toString();
	}
	
	public static String GetRandom(int length) {
		char[] charaters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder sb = new StringBuilder("");
		Random rn = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(charaters[rn.nextInt(charaters.length)]);
		}
		return sb.toString();
	}
}
