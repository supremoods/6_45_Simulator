package com.j_lappay.lotto_6_45_Simulator;

import java.util.ArrayList;
import java.util.Random;

public class LottoApp {
    public double balance;

    public int[][] setNumbers = new int[99][99];
    public double[] totalBet = new double[99];
    public int[] multiplier = new int[99];

    public int[] winningNumbers = new int[6];

    public int countBet;


    public int count = 0;
    Random generator = new Random();

    ArrayList<String> betList = new ArrayList<>();
    ArrayList<Integer> matchCount = new ArrayList<>();
    ArrayList<Double> price = new ArrayList<>();

    public LottoApp(double balance, int countBet) {
        this.balance = balance;
        this.countBet = countBet;
    }

    public LottoApp() {
        balance = 1000.00;
        countBet = 0;
    }

    public double getBalance() {
        return balance;
    }

    public int getCountBet() {
        return countBet;
    }

    public void setCountBet(int countBet) {
        this.countBet = countBet;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void generateWinningNumbers(){
        for (int i = 0; i < winningNumbers.length; i++) {
            winningNumbers[i] = generator.nextInt(45) + 1;

        }
    }

    public void startDraw(){
        for(int i=0;  i < countBet; i++){
            for(int j=0; j < 6; j++){
                for(int k=0; k < 6; k++){
                    if(setNumbers[i][j] == winningNumbers[k]){
                        count++;
                    }
                }
            }
            matchCount.add(count);
            count=0;
        }
        totalPrice();
    }

    public void totalPrice(){

        int i = 0;

        for (Integer num : matchCount) {
            if(num == 6){
                price.add(9000000.0);
            }else if(num == 5){
                price.add( 50000 * (totalBet[i]*multiplier[i]) );
            }else if(num == 4){
                price.add( 1500 * (totalBet[i]*multiplier[i]) );
            }else if(num == 3){
                price.add( 100 * (totalBet[i]*multiplier[i]) );
            }else{
                price.add(0.0);
            }
            i++;
        }

        for (Double num : price){
            balance += num;
        }

    }



}
