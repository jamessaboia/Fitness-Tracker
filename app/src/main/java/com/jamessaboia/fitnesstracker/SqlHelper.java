package com.jamessaboia.fitnesstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SqlHelper extends SQLiteOpenHelper {

    //constantes
    private static final String DB_NAME = "fitness_tracker.db";
    private static final int DB_VERSION = 1;

    // variaveis
    private static SqlHelper INSTANCE;

    // Get resposavel por verificar se a variavel INSTANCE está vazia, se estiver vazia ele vai inserir o contexto nela
    // mas, caso ela ja tiver preenchida, ele vai apenas retornar o valor que ja esta nela
    static SqlHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new SqlHelper(context);
        return INSTANCE;
    }

    //construtor
    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //metodo onCreate
    // é executado sempre quando ainda nao houver um banco de dados dentro do app
    @Override
    public void onCreate(SQLiteDatabase db) {
        // criando  tabela de banco da dados, que armazena os dados abaixo, em sua devida coluna.
        db.execSQL(
                "CREATE TABLE calc (id INTEGER primary key, type_calc TEXT, res DECIMAL, created_date DATETIME)"
        );

    }

    // é executado quando user quiser salvar uma nova versão do banco de dados, ou seja fazer alterações no banco de dados.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Teste", "on Upgrade disparado");
    }


    List<Register> getRegisterBy(String type) {
        List<Register> registers = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase(); //getWritableDatabase() é usado p/ fazer a leitura de resgistros do banco de dados
        Cursor cursor = db.rawQuery("SELECT * FROM calc WHERE type_calc = ?", new String[]{ type });

        try {
            if (cursor.moveToFirst()) {
                do {
                    Register register = new Register();

                    register.type = cursor.getString(cursor.getColumnIndex("type_calc"));
                    register.response = cursor.getDouble(cursor.getColumnIndex("res"));
                    register.createdDate = cursor.getString(cursor.getColumnIndex("created_date"));

                    //add os dados armazenados no obj "register" e guardando-os na arraylist "registers" usando o comando "add"
                    registers.add(register);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return registers;

    }

    // função para criação de um novo item de resgistro no banco de dados
    long addItem(String type, double response) {
        SQLiteDatabase db = getWritableDatabase(); //getWritableDatabase() é usado p/ escrever/inserir/atualizar resgistros no banco de dados

        long calcId = 0;

        try {
            db.beginTransaction();

            // criando um obj do tipo "ContentValues" (padrão Android), para mostrar quais os conteudos que serão inseridos e em que coluna do banco de dados eles deverão ser armazenados
            ContentValues Values = new ContentValues();
            Values.put("type_calc", type);
            Values.put("res", response);
            // criando um obj do tipo "SimpleDateFormat" (classe padrão do Android), usamos essa classe para definir o tipo formatação
            // de datas que queremos e inserimos no obj... E depois criamos uma variavel que irá receber a data atual.
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            String currentDate = sdf.format(new Date());
            Values.put("created_date", currentDate);

            //comandos baixo p/ definitivamente inserir os valores acima (Values) na nossa tabela de banco de dados (calc)
            calcId = db.insertOrThrow("calc", null, Values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);

        } finally {
            if (db.isOpen())
                db.endTransaction();
        }
        // toda essas instruções vão retornar um ID
        return calcId;
    }


}
