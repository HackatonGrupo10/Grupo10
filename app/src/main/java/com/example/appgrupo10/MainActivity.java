package com.example.appgrupo10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText cpf;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //verificarUsuarioLogado(); //Verifica se o usuário já está logado no app ou não.
        email = (EditText) findViewById(R.id.edit_email_id);
        cpf = (EditText) findViewById(R.id.edit_cpf_id);
        botaoLogar = (Button) findViewById(R.id.botaoLogar_id);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setCpf(cpf.getText().toString());
                validarLogin();
            }
        });
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            Toast.makeText(MainActivity.this, "usuario LOGADO", Toast.LENGTH_SHORT).show();
            //Usuario está logado

        }else{
            Toast.makeText(MainActivity.this, "usuario NÃO está LOGIN!", Toast.LENGTH_SHORT).show();
            //Usuario NÃO está logado
        }
    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getCpf()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sucesso ao fazer LOGIN!", Toast.LENGTH_SHORT).show();
                    //LOGADO
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);//OK
                }else{
                    Toast.makeText(MainActivity.this, "ERRO ao fazer LOGIN!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }


}