package com.leon.htools;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final Bot bot = new Bot();
        final Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            bot.getJda().shutdown();
            scanner.close();
            System.exit(0);
        }
    }

}
