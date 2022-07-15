package com.j_lappay.lotto_6_45_Simulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;

    BottomSheetDialog dialog;

    private ImageButton addBtn;
    private Spinner multi;

    private static final int[] btn_idArray ={
 R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_10, R.id.btn_11, R.id.btn_12, R.id.btn_13, R.id.btn_14, R.id.btn_15, R.id.btn_16, R.id.btn_17, R.id.btn_18, R.id.btn_19, R.id.btn_20, R.id.btn_21, R.id.btn_22, R.id.btn_23, R.id.btn_24, R.id.btn_25, R.id.btn_26, R.id.btn_27, R.id.btn_28, R.id.btn_29, R.id.btn_30, R.id.btn_31, R.id.btn_32, R.id.btn_33, R.id.btn_34, R.id.btn_35, R.id.btn_36, R.id.btn_37, R.id.btn_38, R.id.btn_39, R.id.btn_40, R.id.btn_41, R.id.btn_42, R.id.btn_43, R.id.btn_44, R.id.btn_45
    };

    private static final int[] lottoNumbers ={
        R.id.input_1, R.id.input_2, R.id.input_3, R.id.input_4, R.id.input_5, R.id.input_6,
    };

    private static final int[] winningNumbers ={
            R.id.draw_1, R.id.draw_2, R.id.draw_3, R.id.draw_4, R.id.draw_5, R.id.draw_6,
    };
    private final TextView[] winningNum = new TextView[winningNumbers.length];
    private final TextView[] btn = new TextView[btn_idArray.length];
    private final TextView[] inputs = new TextView[lottoNumbers.length];


    int[] input_Count = new int[lottoNumbers.length];
    int[] btn_State = new int[lottoNumbers.length];

    ConstraintLayout mainLayout;

    TextView displayBalance;
    EditText inputBet;
    LottoApp lottoApp = new LottoApp();
    Boolean isEmptyInputs = true;
    ListView listBet;

    ArrayAdapter arrayAdapter;
    private String Tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new BottomSheetDialog(this);

        ImageButton walletBtn = findViewById(R.id.walletBtn);
        TextView draw_btn = findViewById(R.id.btnDraw);

        createDialog();

        mainLayout = findViewById(R.id.mainLayout);

        this.gestureDetector = new GestureDetector(MainActivity.this, this);

        for (int i = 0; i < btn_idArray.length; i++ ){
            btn[i] = findViewById(btn_idArray[i]);
            btn[i].setText(String.valueOf(i+1));
        }

        for (int i = 0; i < winningNum.length; i++ ){
            winningNum[i] = findViewById(winningNumbers[i]);
        }

        for (int i = 0; i < lottoNumbers.length; i++ ){
            inputs[i] = findViewById(lottoNumbers[i]);
            int finalI = i;
            inputs[i].setOnClickListener(view -> {
                input_Count[finalI] = 0;
                inputs[finalI].setText("");
                btn[btn_State[finalI]].setBackgroundResource(R.drawable.circle_blue_200);
            });

        }

        for (int i = 0; i < btn_idArray.length; i++ ){
            int finalI = i;
            btn[i].setOnClickListener(view -> {
                for(int j = 0; j < input_Count.length; j++){
                    if(input_Count[j]==0){
                        btn[finalI].setBackgroundResource(R.drawable.circle);
                        input_Count[j] = Integer.parseInt((String) btn[finalI].getText());
                        inputs[j].setText(String.valueOf(input_Count[j]));
                        btn_State[j] = finalI;
                        break;
                    }
                }
            });
        }

        draw_btn.setOnClickListener(view -> {
            if(lottoApp.betList.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please place your bet\nPlease Select 6 numbers\nMinimum bet is ₱ 10.00", Toast.LENGTH_SHORT).show();
                return;
            }

            lottoApp.generateWinningNumbers();

            for(int i=0; i < winningNum.length; i++){
                winningNum[i].setText(String.valueOf(lottoApp.winningNumbers[i]));
                winningNum[i].setBackgroundResource(R.drawable.circle);
            }
            lottoApp.startDraw();

            ArrayList<String> combination = new ArrayList<>();
            String combiList = "";

            for(int i = 0; i < lottoApp.getCountBet(); i++){
                for(int j = 0; j < input_Count.length; j++){
                    String tempCombi = lottoApp.setNumbers[i][j] + ", ";
                    combiList = combiList.concat(tempCombi);
                }
                combination.add(combiList);
                combiList = "";
            }

            int count = 1;
            String summaryTemp = "";
            for(Integer num : lottoApp.matchCount){
                String draw_summary = "Bet " + count + ": " + num + " matches\n" + "Combination : " + combination.get(count-1) + "\n" + "Price: " + lottoApp.price.get(count-1) + "\n\n";
                summaryTemp = summaryTemp.concat(draw_summary);
                count++;
            }

            combination.clear();
            summaryTemp = summaryTemp.concat("Your Total Balance: " + lottoApp.getBalance() +"\n\nJockpot Price: 9,000,000");

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Summary")
                    .setMessage(summaryTemp)
                    .setPositiveButton("Ok", (dialogInterface, i1) -> {
                        String text = "₱ "+ lottoApp.getBalance();
                        displayBalance.setText(text);
                        lottoApp.matchCount.clear();
                        lottoApp.price.clear();
                        lottoApp.betList.clear();
                        listBet.setAdapter(arrayAdapter);
                        for (TextView textView : winningNum) {
                            textView.setText("");
                            textView.setBackgroundResource(R.drawable.circle_blue_200);
                        }
                    })
                    .show();
        });

        walletBtn.setOnClickListener(view -> {
            dialog.show();
            String text = "₱ "+ lottoApp.getBalance();
            displayBalance.setText(text);
        });

        listBet.setOnItemLongClickListener((adapterView, view, i, l) -> {

            final int which_item = i;

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Are you sure")
                    .setMessage("Do you want to remove your bet")
                    .setPositiveButton("Yes", (dialogInterface, i1) -> {
                        lottoApp.betList.remove(which_item);
                        redeemBalance(which_item);
                        listBet.setAdapter(arrayAdapter);
                        String text = "₱ "+ lottoApp.getBalance();
                        displayBalance.setText(text);
                        lottoApp.countBet--;
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;
        });

        addBtn.setOnClickListener(view -> {
            checkInputs();
            if(!String.valueOf(inputBet.getText()).equals("") && !isEmptyInputs && Double.parseDouble(String.valueOf(inputBet.getText())) > 10.0){
                if(!isBalanceEmpty()){
                    lottoApp.totalBet[lottoApp.getCountBet()] = Double.parseDouble(String.valueOf(inputBet.getText()));
                    String getMulti = multi.getSelectedItem().toString();
                    getMulti = getMulti.substring(1);
                    lottoApp.multiplier[lottoApp.getCountBet()] = Integer.parseInt(getMulti);

                    String text = "₱ "+ lottoApp.getBalance();
                    displayBalance.setText(text);
                }else{
                    return;
                }
                for(int j = 0; j < input_Count.length; j++){
                    lottoApp.setNumbers[lottoApp.getCountBet()][j] = input_Count[j];
                }

                String foo ="Numbers: "+ Arrays.toString(input_Count) + "\n" + "Bet: " + lottoApp.totalBet[lottoApp.getCountBet()] + "\n" + "Multiplier: " + lottoApp.multiplier[lottoApp.getCountBet()];

                lottoApp.betList.add(foo);

                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,lottoApp.betList);

                listBet.setAdapter(arrayAdapter);

                lottoApp.countBet++;

                for(int i=0; i < input_Count.length; i++){
                    inputs[i].setText("");
                    input_Count[i] = 0;
                }
                for (int i = 0; i < btn_idArray.length; i++ ){
                    btn[i].setBackgroundResource(R.drawable.circle_blue_200);
                }

            }else if(String.valueOf(inputBet.getText()).equals("") && !isEmptyInputs){
                Toast.makeText(getApplicationContext(), "Please place your bet ", Toast.LENGTH_SHORT).show();
            }else if(!String.valueOf(inputBet.getText()).equals("") && isEmptyInputs){
                Toast.makeText(getApplicationContext(), "Please Select 6 numbers ", Toast.LENGTH_SHORT).show();
            }else if(!String.valueOf(inputBet.getText()).equals("") && !isEmptyInputs && Double.parseDouble(String.valueOf(inputBet.getText())) < 10.0){
                Toast.makeText(getApplicationContext(), "Minimum bet is ₱ 10.00", Toast.LENGTH_SHORT).show();
            }else if(!String.valueOf(inputBet.getText()).equals("") && isEmptyInputs && Double.parseDouble(String.valueOf(inputBet.getText())) < 10.0){
                Toast.makeText(getApplicationContext(), "Please Select 6 numbers\nMinimum bet is ₱ 10.00", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Please place your bet\nPlease Select 6 numbers\nMinimum bet is ₱ 10.00", Toast.LENGTH_SHORT).show();
            }

            inputBet.setText("");

       });

    }

    public void redeemBalance(int index){
        String temp = "Bet: " + lottoApp.totalBet[index] + "\n" + "Multi: " + lottoApp.multiplier[index];
        Log.i(Tag, "index: "+ index + "\n");
        Log.i(Tag, temp);
        double totalBet =  lottoApp.totalBet[index] *  lottoApp.multiplier[index];
        totalBet += lottoApp.getBalance();
        Log.i(Tag, "totalBet: "+ totalBet + "\n");
        for(int j = 0; j < input_Count.length; j++){
            lottoApp.setNumbers[index][j] = input_Count[j];
        }

        lottoApp.setBalance(totalBet);
    }

    public void  checkInputs(){
        for(int j = 0; j < input_Count.length; j++){
            if(input_Count[j]==0){
                isEmptyInputs = true;
                break;
            }else {
                isEmptyInputs = false;
            }
        }

    }

    private boolean isBalanceEmpty(){
        String getMulti = multi.getSelectedItem().toString();
        getMulti = getMulti.substring(1);

        double totalBet =  Double.parseDouble(String.valueOf(inputBet.getText())) *  Integer.parseInt(getMulti);
        double balance = lottoApp.getBalance() - totalBet;

        if(balance < 0){
            Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
            return true;
        }

        lottoApp.setBalance(balance);
        return false;
    }

    private void createDialog(){
        View view = getLayoutInflater().inflate(R.layout.activity_wallet, null, false);

        displayBalance = view.findViewById(R.id.balance);
        inputBet = view.findViewById(R.id.setBet);
        addBtn = view.findViewById(R.id.addListBet);
        multi = view.findViewById(R.id.multiplier);
        listBet = view.findViewById(R.id.bet);

        dialog.setContentView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        gestureDetector.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                float valueY = y2 - y1;

                if(Math.abs(valueY)> MIN_DISTANCE){
                    if(y2 < y1){
                        dialog.show();
                        String text = "₱ "+ lottoApp.getBalance();
                        displayBalance.setText(text);
                    }
                }
        }
        return super.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}