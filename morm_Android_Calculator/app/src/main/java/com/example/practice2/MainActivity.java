package com.example.practice2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstInput = true;
    private boolean isOperating = false;
    private boolean isShowingResult = false;

    private float lastNumber = 0;
    private float sumNumber = 0;
    private char operator = '+';

    private Button lastOperatingButton;

    //텍스트 뷰의 경우 레이아웃이 로딩되고나서 불러져야 하기 때문에
    //필드에서 선언 불가
    //이런 형식의 변수를 참조변수라 부름
    private TextView processTextView;
    private String processText = "0";

    //결과뷰
    private float resultNumber = 0;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processTextView = findViewById(R.id.result_process_text);
        resultTextView = findViewById(R.id.result_text);
    }

    public void numberFunctionalButtonClick(View view) {
        isOperating = false;
        if(isShowingResult) {
            isShowingResult = false;
            processTextViewUpdate(true, true);
        }

        Button currentButton = findViewById(view.getId());

        if (currentButton.getId() == R.id.decimal_button){
            if(!resultTextView.getText().toString().contains(".")){
                resultTextViewUpdate(currentButton.getText().toString(), false);
                processTextUpdate(currentButton.getText().toString(), false);
            }
        }
        else{
            resultTextViewUpdate(currentButton.getText().toString(), false);
            processTextUpdate(currentButton.getText().toString(), false);
        }

    }


    public void operatingFunctionalButtonClick(View view) {
        if(isOperating) return;
        isOperating = true;


        Button currentButton = findViewById(view.getId());
        resultNumber = Float.parseFloat( resultTextView.getText().toString());
        operator = currentButton.getText().charAt(0);

        //숫자를 입력하지 않았으면 연산자 입력이 불가능하다
        if(resultNumber == 0){
            //다만 Minus 버튼에 한해서 마이너스 계산을 위해 사용이 가능하다
            if(currentButton.getId() == R.id.minus_button){
                resultTextViewUpdate("-", true);
                processTextUpdate(String.valueOf(operator), false);
            }
            return;
        }

        //게산과정 업데이트시킴
        processTextUpdate(String.valueOf(operator), false);
        processTextViewUpdate(false, true);


        if(lastNumber != 0){
            switch (currentButton.getId()){
                case R.id.division_button :
                    sumNumber =  OperateFunc(lastOperatingButton);
                    resultTextViewUpdate(String.valueOf(sumNumber), true);
                    isFirstInput = true;
                    break;

                case R.id.multiply_button :
                    sumNumber = OperateFunc(lastOperatingButton);;
                    resultTextViewUpdate(String.valueOf(sumNumber), true);
                    isFirstInput = true;
                    break;

                case R.id.minus_button:
                    sumNumber = OperateFunc(lastOperatingButton);;
                    resultTextViewUpdate(String.valueOf(sumNumber), true);
                    isFirstInput = true;
                    break;

                case R.id.plus_button:
                    sumNumber = OperateFunc(lastOperatingButton);;
                    resultTextViewUpdate(String.valueOf(sumNumber), true);
                    isFirstInput = true;
                    break;

                case R.id.equal_button:
                    resultTextViewUpdate(String.valueOf(OperateFunc(lastOperatingButton)), true);
                    sumNumber = 0;
                    processText = "0";
                    lastNumber = 0;
                    isShowingResult = true;
                    break;
            }
        }
        else{
            //맨처음 수를 한번만 입력하고 이퀄했을때 초기화
            if(currentButton.getId() == R.id.equal_button){
                sumNumber = 0;
                processText = "0";
                lastNumber = 0;
                isShowingResult = true;
            }
            else{
                sumNumber = resultNumber;
            }
        }
        isFirstInput = true;
        lastNumber = sumNumber;
        lastOperatingButton = currentButton;
    }

    private float OperateFunc(Button operateButton){
        switch (operateButton.getId()){
            case R.id.division_button :
                sumNumber = lastNumber / resultNumber;
                break;
            case R.id.multiply_button:
                sumNumber = lastNumber * resultNumber;
                break;
            case R.id.minus_button:
                sumNumber = lastNumber - resultNumber;
                break;
            case R.id.plus_button:
                sumNumber = lastNumber + resultNumber;
                break;
        }
        return sumNumber;
    }


    public void functionalButtonClick(View view) {
        isOperating = false;
        Button currentButton = findViewById(view.getId());

        //equl 버튼으로 결과를 보여줄때, 초기화 시켜줌
        if(isShowingResult) {
            isShowingResult = false;

            isFirstInput = true;
            sumNumber = 0;
            processText = "0";
            lastNumber = 0;
            resultNumber = 0;
            operator = '+';
            resultTextView.setText("0");
            processTextViewUpdate(true, true);
            return;
        }

        switch (currentButton.getId()){
            case R.id.all_clear_button :
                isFirstInput = true;
                sumNumber = 0;
                processText = "0";
                lastNumber = 0;
                resultNumber = 0;
                operator = '+';
                resultTextView.setText("0");
                processTextViewUpdate(true, true);
                break;

            case R.id.clear_entity_button :
                if(resultTextView.getText().toString() != "0" && !isFirstInput){
                    isFirstInput = true;
                    //클리어 해준 스트링만큼 과정 텍스트를 제거한다.
                    processText = processText.substring(0, processText.length() - resultTextView.getText().toString().length());
                    processTextUpdate(processText, true);
                    resultTextView.setText("0");
                }

                break;

            case R.id.back_space_button :
                if(!isFirstInput) {
                    String currentResultText = resultTextView.getText().toString();
                    if(currentResultText.length() > 1){
                        currentResultText = currentResultText.substring(0, currentResultText.length()-1);
                        processTextUpdate(processText.substring(0,processText.length()-1), true);
                    }
                    else {
                        if(resultTextView.getText().toString() != "0"){
                            isFirstInput = true;
                            currentResultText = "0";
                            processText = processText.substring(0,processText.length()-1);   //한자리수가 남았을때 백스페이스를 할 경우 0으로 바꿔줘야함
                            processTextUpdate(processText, true);
                        }
                    }

                    resultTextView.setText(currentResultText);
                }
                break;
        }
    }


    //계산결과뷰 원하는 문자열로 업데이트
    private void resultTextViewUpdate(String resultText, boolean isNewSet){
        if (isFirstInput || isNewSet){
            isFirstInput = false;
            resultTextView.setText(resultText);
            if(resultTextView.getText().toString() == "."){
                resultTextView.setText("0.");
            }
        }
        else{
            if(resultTextView.getText().toString() == "0") {

            }
            else {
                resultTextView.append(resultText);
            }
        }
    }

    //계산과정뷰에 들어갈 텍스트 업데이트 함수
    private void processTextUpdate(String lastString, boolean isNew){
        if(processText == "0" || isNew){
            processText = lastString;
        }
        else{
            processText += lastString;
        }
    }

    //계산과정뷰 업데이트 함수
    private void processTextViewUpdate(boolean isClear, boolean isShow){
        if(isShow) {
            if (isClear){
                processTextView.setText("0");
            }
            else{
                processTextView.setText(processText);
            }
        }
    }


}
