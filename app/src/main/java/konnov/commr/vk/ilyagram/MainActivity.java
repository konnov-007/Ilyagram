package konnov.commr.vk.ilyagram;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    final private String TAG = "instaLogs";
    String usernameString;
    String passwordString;
    EditText usernameEditText;
    EditText passwordEditText;
    boolean isItSignUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);
//////////////////////////////////////////////////
//        if(ParseUser.getCurrentUser() != null)
//            showUserList();
        //////////////////////////////////////
    }

    private void logIn(View view){
        if(getEditText()){
            ParseUser.logInInBackground(usernameString, passwordString, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e == null){
                        showUserList();
                    }
                    else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(this, "ya nigga ain't provide no shit", Toast.LENGTH_SHORT).show();

    }

    private void signUp(View view){
        if(getEditText()){
            ParseUser user = new ParseUser();
            user.setUsername(usernameString);
            user.setPassword(passwordString);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null)
                        Toast.makeText(MainActivity.this, "Account has been successfully created, please log in", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(this, "ya nigga ain't provide no shit", Toast.LENGTH_SHORT).show();
    }

    private boolean getEditText(){
        usernameString = usernameEditText.getText().toString();
        passwordString = passwordEditText.getText().toString();
        if(usernameString.isEmpty() || passwordString.isEmpty()){
            return false;
        }else {
            //Log.i(TAG, usernameString);
            //Log.i(TAG, passwordString);
            return true;
        }
    }

    public void switchLoginWithSigning(View view) {
        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView switcher = (TextView) findViewById(R.id.loginSigningSwitcher);
        if(!isItSignUp){
            loginButton.setText("Sign up");
            switcher.setText("or Log in");
            isItSignUp = true;
        }else {
            loginButton.setText("Log in");
            switcher.setText("or Sign up");
            isItSignUp = false;
        }

    }

    public void loginButtonClicked(View view) {
        if(isItSignUp)
            signUp(view);
        else
            logIn(view);
    }

    private void showUserList(){
        Intent intent = new Intent(MainActivity.this, UserListActivity.class);
        startActivity(intent);
    }

    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
            loginButtonClicked(view);
        return false;
    }

}


