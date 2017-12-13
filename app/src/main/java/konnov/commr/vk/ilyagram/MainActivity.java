package konnov.commr.vk.ilyagram;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;


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
        SharedPreferenceHelper sharedPreference = new SharedPreferenceHelper(this);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);
//////////////////////////////////////////////////checking if user alread signed in before
        if(sharedPreference.getSharedPref() != null){
            ArrayList<String> lastUserData = sharedPreference.getSharedPref();
            usernameEditText.setText(lastUserData.get(0));
            passwordEditText.setText(lastUserData.get(1));
            usernameString = lastUserData.get(0);
            passwordString = lastUserData.get(1);
        }


        if(!isNetworkAvailable())
            Toast.makeText(this, "PLEASE CONNECT YOUR DEVICE TO INTERNET", Toast.LENGTH_LONG).show();
//////////////////////////////////////
    }

    private void logIn(View view){
        if(getEditText()){
            ParseUser.logInInBackground(usernameString, passwordString, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e == null){
                        SharedPreferenceHelper sharedPreference = new SharedPreferenceHelper(MainActivity.this);
                        sharedPreference.saveInSharedPref(usernameString, passwordString);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
            loginButtonClicked(view);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable())
            Toast.makeText(this, "PLEASE CONNECT YOUR DEVICE TO INTERNET", Toast.LENGTH_LONG).show();
    }
}


