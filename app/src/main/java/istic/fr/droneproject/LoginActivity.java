package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import istic.fr.droneproject.model.User;
import istic.fr.droneproject.service.UserService;
import istic.fr.droneproject.service.impl.UserServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via login/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mLoginView = (EditText) findViewById(R.id.login_text_login);
        mPasswordView = (EditText) findViewById(R.id.login_text_password);

        Button mEmailSignInButton = (Button) findViewById(R.id.login_btn_connect);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User();
                user.login = mLoginView.getText().toString();
                user.password = mPasswordView.getText().toString();

                UserService service = new UserServiceImpl();
                service.login(user, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        //TODO Rediriger vers la bonne page en fonction du role
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Mot de passe correct", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}

