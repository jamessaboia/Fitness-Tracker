package com.jamessaboia.fitnesstracker;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImcActivity extends AppCompatActivity {

    private EditText editHeight;
    private EditText editWeight;
    private Button btnImcSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);
        btnImcSend = findViewById(R.id.btn_imc_send);

        btnImcSend.setOnClickListener(view -> {
            // condição que envia uma msg para o usuario em caso o formulário não for validado:
            if (!validate()) {
                Toast.makeText(ImcActivity.this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            //caso seja validado, o app segue para os codigos abaixo...

            // coletando as informações que o usuario digitar e armazenando nas variaveis abaixo (em string ainda)
            String sheight = editHeight.getText().toString();
            String sweight = editWeight.getText().toString();

            //convertendo para int as informações digitadas pelo o usuario, que antes haviam sido capturadas em string e armazenadas nas variaveis acima.
            int height = Integer.parseInt(sheight);
            int weight = Integer.parseInt(sweight);

            // a variavel do tipo double "result", recebe o calculo IMC armazenado na função "calculateImc"
            double result = calculateImc(height, weight);

            Log.d("TEXTE", "Resultado " + result);

            int imcResponseId = imcResponse(result);

            //criando um PopUp p/ mostrar o resultado/infos do imc na tela de uma maneira mais elegante
            // dentro do PopUp também criamos um botão ok (padrão do resource Android)
            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                    .setTitle(getString(R.string.imc_response, result))
                    .setMessage(imcResponseId)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    })
                    .create();
            dialog.show();

            // escondendo teclado (após clicar no botão "calcular"), para isso usamos o gerenciador de teclado (InputMethodManager),
            // porém, precisamos buscar o teclado usando o "getSystemService" e jogar ele pra dentro do gerenciador (InputMethodManager)
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
        });


    }

    @StringRes
    // o "imc", como vc pode ver lá acima, é o "result"
    private int imcResponse(double imc) {
        if (imc < 15)
            return R.string.imc_severely_low_weight;
        else if (imc < 16)
            return R.string.imc_very_low_weight;
        else if (imc < 18.5)
            return R.string.imc_low_weight;
        else if (imc < 25)
            return R.string.normal;
        else if (imc < 30)
            return R.string.imc_high_weight;
        else if (imc < 35)
            return R.string.imc_so_high_weight;
        else if (imc < 40)
            return R.string.imc_severely_high_weight;
        else
            return R.string.imc_extreme_weight;


    }

    // função para realizar o calculo IMC
    private double calculateImc(int height, int weight){
        //o calculo imc é feito assim: peso / (altura * altura)
        return weight / ( ((double) height / 100) * ((double) height / 100) );


    }
    // função responsavel pela a validação dos formulários.
    // para que que seja validado, a condições abaixo devem ser verdadeiras:
    private boolean validate() {
        return (!editWeight.getText().toString().startsWith("0") // n pode começar com zero
                && !editHeight.getText().toString().startsWith("0") // n pode começar com zero
                && !editWeight.getText().toString().isEmpty() // o campo n pode estar vazio
                && !editHeight.getText().toString().isEmpty()); // o campo n pode estar vazio

    }

}