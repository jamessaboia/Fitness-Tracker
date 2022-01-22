package com.jamessaboia.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // criando varivel "rvMain"
    private RecyclerView rvMain;
    // private View btnImc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // variavel "rvMain" recebendo, lá do front, o conteudo da RecyclerView atraves do link via ID
        // btnImc = findViewById(R.id.btn_imc);
        rvMain = findViewById(R.id.main_rv);

        //criando um Arraylist da classe MainItem e armazenando nele os itens dinamicos das nossas views reciclaveis
        // é nessa lista que estarão listados todos os itens/dados dinamicos de cada view reciclada.
        List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1, R.drawable.ic_baseline_wb_sunny_24, R.string.label_imc, Color.GREEN));
        mainItems.add(new MainItem(2, R.drawable.ic_baseline_visibility_24, R.string.label_tmb, Color.YELLOW));
        mainItems.add(new MainItem(3, R.drawable.ic_baseline_directions_run_24, R.string.label_gasto_cal, Color.LTGRAY));

        /* 1- LayoutManager, é o responsavel por setar qual será o tipo de exibição do layout da RecyclerView
        Se vai ser Linear (vertical ou horizontal), mosaico ou grid */
        rvMain.setLayoutManager(new LinearLayoutManager(this));

        /* 2 - Adapter, é o responsavel por fazer a conexão entre a lista de itens/dados dinamicos (ArrayList) e a View/Celula, contida no ViewHolder.
        abaixo criamos um obj "adapter" e usamos o construtor da nossa classe "MainAdapter" para pegar a lista de itens/dados dinamicos (ArrayList mainItems) e enviar para dentro da classe. (MainAdapter)
        faremos isso para posteriormente enviar essa lista de itens/dados dinamicos direto para nosso ViewHolder.
        Usando o setAdpter abaixo, nos conseguimos definir o nosso adapter criado como o adapitador "oficial" da nossa ReciclerView (rvMain) */
        MainAdapter adapter = new MainAdapter(mainItems);
        rvMain.setAdapter(adapter);

        //criando evento de click para nosso botão imc. Tudo que tiver dentro desse bloco de codigo
        //ocorrerá somente quando o usuario clicar no botão.
        // btnImc.setOnClickListener(view -> {

            //fazendo a segunda tela abrir
            //Intent intent = new Intent(MainActivity.this, ImcActivity.class);
            //startActivity(intent);
        //});
    }
// abaixo criamos 2 classes internas, a MainAdapter e a MainViewHolder que são componentes da nossa RecyclerView

    // criando a classe MainAdapter para que consigamos pegar os dados dinamicos das Views reciclaveis (contidas na ArrayList "mainItems") e enviar para o ViewHolder
    // O Adapter é o cara que vai fazer a conexão entre os dados dinamicos da RecyclerView (que estao no Arraylist "mainItems") e a celula/view padrão (main_item.xml), que está contida no nosso ViewHolder (MainViewHolder).
    private class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {


        private List<MainItem> mainItems;

        // o construtor abaixo é o responsavel por trazer aqui para a classe Adapter a nossa ArrayList "mainItems", que está populada c/ os itens dinamicos da nossas views reciclaveis.
        public MainAdapter (List<MainItem> mainItems){
            this.mainItems = mainItems;
        }

        @NonNull
        @Override
        //O método onCreateViewHolder() serve para para inflar o layout do item. Basicamente é chamado quando é necessário criar um novo item.
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
        }

        @Override
        //O método onBindViewHolder() tem a finalidade de definir os atributos de exibição com base nos dados. Basicamente é invocado quando um novo view precisa ser exibido para o usuário.
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            // toda view que aparece na tela tem sua devida "posição"(view 0, view 1 e etc..), essa posição é controlada pela onBindViewHolder que conta e armazenada na variavel "position"
            // os metodos abaixo, então, pegarão a posição de cada nova view e vão jogar dentro da ArrayList ("mainItems"), assim cada view nova terá também um novo dado dinamico inserido, graças a ArrayList ("mainItems")
            // depois a gente passa essa nova View com esses novos dados dinamicos lá pra nosso ViewHolder, usando o "holder.bind(), e lá ele sera modificado com novos dados dinamicos"
            MainItem mainCurrentItem = mainItems.get(position);
            holder.bind(mainCurrentItem);

        }

        @Override
        public int getItemCount() {

            return mainItems.size();
        }
    }


    // entenda o ViewHolder como o cara que contém a referencia para a nossa View/Celula padrão de reciclagem (main_item.xml)
    // ou seja, toda vez que quisermos gerenciar uma celula, nos utilizaremos o ViewHolder para fazer isso.
    private class MainViewHolder extends RecyclerView.ViewHolder {

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(MainItem item) {
            // Aqui nos pegamos, via ID, as referencias das três propriedades do Layout da minha celula/view padrão.
            // e em seguida armazenaremos essas propriedades nas variaveis abaixo
            // Pq fizemos isso? para possamos recicla-las com os novos itens dinamicos contidos na ArrayList ("mainItems").
            TextView txtName = itemView.findViewById(R.id.item_txt_name);
            ImageView imgIcon = itemView.findViewById(R.id.item_img_icon);
            LinearLayout container = (LinearLayout) itemView;

            //agora, temos armazenados em variaveis todas as referencias de cada propriedades da celula que iremos reciclar.
            // usando o "set", iremos substituir os dados padrões de cada uma dessas propriedades da celula padrão, pelos novos dados.
            // Para pegar esses novos dados, nos usamos o "get" (get da classe MainItem). Pronto! view recliclada com novos dados!
            txtName.setText(item.getTextStringId());
            imgIcon.setImageResource(item.getDrawableId());
            container.setBackgroundColor(item.getColor());


            // cada rolagem (cada nova posição), nos temos novos dados/itens dinamicos aparecendo na view padrão, graças a
            // todos os processos feitos acima.

        }
    }
}