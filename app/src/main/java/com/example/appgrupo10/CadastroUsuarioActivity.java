package com.example.appgrupo10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nomeCompleto;
    private EditText email;
    private EditText cpf;
    private Button bt_cadastrar;
    private Button bt_central_ajuda;
    private Usuario usuario;
    private EditText lat;
    private EditText longit;
    private FirebaseAuth autenticacao;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nomeCompleto = (EditText) findViewById(R.id.nomeCompleto_id);
        email = (EditText) findViewById(R.id.email_id);
        cpf = (EditText) findViewById(R.id.cpf_id);
        lat = (EditText) findViewById(R.id.lat_id);
        longit = (EditText) findViewById(R.id.longit_id);
        bt_cadastrar = (Button) findViewById(R.id.botaoCadastrar_id);
        bt_central_ajuda = (Button) findViewById(R.id.bt_centralajuda);

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //instância da classe usuário
                usuario = new Usuario();

                //Set do objeto novousuario
                usuario.setNome(nomeCompleto.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setCpf(cpf.getText().toString());
                usuario.setLat(lat.getText().toString());
                usuario.setLongt(longit.getText().toString());
                cadastrarUsuario();
            }
        });

        bt_central_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCentralAjuda();
            }
        });


    }

    public void abrirCentralAjuda(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, CentralAjuda.class);
        startActivity(intent);
    }



    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
            usuario.getEmail(),usuario.getCpf()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this, "SUCESSO ao cadastrar!", Toast.LENGTH_SHORT).show();
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar(); //Persistência dos dados

                    Intent intent = new Intent(CadastroUsuarioActivity.this, MapsActivity.class);
                    intent.putExtra( "objeto", usuario);//Passando o objeto usuario para a próxima Activity
                    startActivity(intent);//Start localização no mapa
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this, "FALHA ao cadastrar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}