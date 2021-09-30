package com.example.familymapclient.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.familymapclient.DataCash;
import com.example.familymapclient.HttpRequest;
import com.example.familymapclient.MainActivity;
import com.example.familymapclient.R;
import com.example.familymapclient.serverProxy;

import java.net.MalformedURLException;
import java.net.URL;

import Requests.EventRequest;
import Requests.LoginRequest;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

public class LoginRegisterFragment extends Fragment {
    private HttpRequest connection;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private PersonRequest personRequest;
    private EventRequest eventRequest;
    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private RadioGroup gender;
    private Button loginButton;
    private Button registerButton;
    private DataCash myCash;

    /**
     * checks if enough textFields are filled to login
     */
    private void verifyLoginFields() {
        if (serverHostEditText.getText().toString().equals("") ||
                serverPortEditText.getText().toString().equals("") ||
                userNameEditText.getText().toString().equals("") ||
                passwordEditText.getText().toString().equals("")) {
            loginButton.setEnabled(false);
        }
        else {
            loginButton.setEnabled(true);
        }
    }

    /**
     * checks if enough textFields are filled to register
     */
    private void verifyRegisterFields() {
        if (serverHostEditText.getText().toString().equals("") ||
                serverPortEditText.getText().toString().equals("") ||
                userNameEditText.getText().toString().equals("") ||
                passwordEditText.getText().toString().equals("") ||
                firstNameEditText.getText().toString().equals("") ||
                lastNameEditText.getText().toString().equals("") ||
                emailEditText.getText().toString().equals("")) {
            registerButton.setEnabled(false);
        }
        else {
            registerButton.setEnabled(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRequest = new LoginRequest();
        registerRequest = new RegisterRequest();
        personRequest = new PersonRequest();
        eventRequest = new EventRequest();
        connection = new HttpRequest();
        myCash = DataCash.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_screen, container, false);

        //login button
        loginButton = view.findViewById(R.id.login);
        loginButton.setEnabled(false);
        View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTask loginTask = new LoginTask(connection, loginRequest);
                loginTask.execute(loginRequest);
            }
        };
        loginButton.setOnClickListener(onClickListenerLogin);

        //register button
        registerButton = view.findViewById(R.id.register);
        registerButton.setEnabled(false);
        View.OnClickListener onClickListenerRegister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTask registerTask = new RegisterTask(connection, registerRequest);
                registerTask.execute(registerRequest);
            }
        };
        registerButton.setOnClickListener(onClickListenerRegister);

        //Server Host edit field
        serverHostEditText = view.findViewById(R.id.editTextServerHost);
        TextWatcher watchServerHostEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                connection.setServerHost(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        serverHostEditText.addTextChangedListener(watchServerHostEditText);

        //Server Port edit field
        serverPortEditText = view.findViewById(R.id.editTextServerPort);
        connection.setServerPort("8080");
        TextWatcher watchServerPortEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                connection.setServerPort(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        serverPortEditText.addTextChangedListener(watchServerPortEditText);

        //Username edit field
        userNameEditText = view.findViewById(R.id.editTextUserName);
        TextWatcher watchUserNameEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginRequest.setUserName(s.toString());
                registerRequest.setUserName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        userNameEditText.addTextChangedListener(watchUserNameEditText);

        //Password edit field
        passwordEditText = view.findViewById(R.id.editTextPassword);
        TextWatcher watchPasswordEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginRequest.setPassword(s.toString());
                registerRequest.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        passwordEditText.addTextChangedListener(watchPasswordEditText);

        //First name edit field
        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        TextWatcher watchFirstNameEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        firstNameEditText.addTextChangedListener(watchFirstNameEditText);

        //Last name edit field
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        TextWatcher watchLastNameEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setLastName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        lastNameEditText.addTextChangedListener(watchLastNameEditText);

        //Email edit field
        emailEditText = view.findViewById(R.id.editTextEmailAddress);
        TextWatcher watchEmailEditText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyLoginFields();
                verifyRegisterFields();
            }
        };
        emailEditText.addTextChangedListener(watchEmailEditText);

        gender = view.findViewById(R.id.genderGroup);
        RadioGroup.OnCheckedChangeListener checkRadioButtons = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleRadio) {
                    registerRequest.setGender("m");
                }
                else {
                    registerRequest.setGender("f");
                }
            }
        };
        gender.setOnCheckedChangeListener(checkRadioButtons);

        return view;
    }

    public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
        private HttpRequest portRequest;
        private LoginRequest request;

        public LoginTask(HttpRequest portRequest, LoginRequest request) {
            this.portRequest = portRequest;
            this.request = request;
        }

        @Override
        protected LoginResponse doInBackground(LoginRequest... loginRequests) {
            serverProxy serverProxy = new serverProxy();
            URL url = null;
            try {
                url = new URL("http://" + portRequest.getServerHost() + ":" + portRequest.getServerPort() + "/user/login");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            LoginResponse response = serverProxy.getLoginResponse(url, request);

            return response;
        }

        protected void onPostExecute(LoginResponse response) {
            Toast toast;
            getActivity();

            if (response.isSuccess()) {
                getUserData(response.getAuthToken());
                myCash.setCurrAuthToken(response.getAuthToken());
            }
            else {
                toast = Toast.makeText(getActivity(), "Failed to login", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {
        private HttpRequest portRequest;
        private RegisterRequest request;

        public RegisterTask(HttpRequest connectionRequest, RegisterRequest request) {
            this.portRequest = connectionRequest;
            this.request = request;
        }

        @Override
        protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {
            serverProxy serverProxy = new serverProxy();
            URL url = null;
            try {
                url = new URL("http://" + portRequest.getServerHost() + ":" + portRequest.getServerPort() + "/user/register");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            RegisterResponse response = serverProxy.getRegisterResponse(url, request);

            return response;
        }

        protected void onPostExecute(RegisterResponse response) {
            Toast toast;
            getActivity();

            if (response.isSuccess()) {
                getUserData(response.getAuthToken());
                myCash.setCurrAuthToken(response.getAuthToken());
            }
            else {
                toast= Toast.makeText(getActivity(), "Failed to register", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public class PersonTask extends AsyncTask<PersonRequest, Void, PersonResponse> {
        private HttpRequest portRequest;
        private PersonRequest request;

        public PersonTask(HttpRequest connectionRequest, PersonRequest request) {
            this.portRequest = connectionRequest;
            this.request = request;
        }

        @Override
        protected PersonResponse doInBackground(PersonRequest... personRequests) {
            serverProxy serverProxy = new serverProxy();
            URL url = null;
            try {
                url = new URL("http://" + portRequest.getServerHost() + ":" + portRequest.getServerPort() + "/person");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            PersonResponse response = serverProxy.getPersonResponse(url, request);

            return response;
        }

        protected void onPostExecute(PersonResponse response) {
            Toast toast;
            getActivity();

            if (response.isSuccess()) {
                myCash.addPeople(response.getData());
            }
            else {
                toast = Toast.makeText(getActivity(), "Failed to get user information", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public class EventTask extends AsyncTask<EventRequest, Void, EventResponse> {
        private HttpRequest portRequest;
        private EventRequest request;

        public EventTask(HttpRequest connectionRequest, EventRequest request) {
            this.portRequest = connectionRequest;
            this.request = request;
        }

        @Override
        protected EventResponse doInBackground(EventRequest... eventRequests) {
            serverProxy serverProxy = new serverProxy();
            URL url = null;
            try {
                url = new URL("http://" + portRequest.getServerHost() + ":" + portRequest.getServerPort() + "/event");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            EventResponse response = serverProxy.getEventResponse(url, request);

            return response;
        }

        @Override
        protected void onPostExecute(EventResponse response) {
            Toast toast;
            getActivity();

            if (response.isSuccess()) {
                myCash.addEvents(response.getData());
                toast = Toast.makeText(getContext(), "Welcome " + myCash.getUserFirstName() + " " + myCash.getUserLastName(), Toast.LENGTH_SHORT);
                toast.show();
//                myCash.organizeData();
            }
            else {
                toast = Toast.makeText(getActivity(), "Failed to get user information", Toast.LENGTH_SHORT);
                toast.show();
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void getUserData(String authToken) {
        personRequest.setAuthTokenID(authToken);
        PersonTask personTask = new PersonTask(connection, personRequest);
        personTask.execute(personRequest);
        eventRequest.setAuthTokenID(authToken);
        EventTask eventTask = new EventTask(connection,eventRequest);
        eventTask.execute(eventRequest);
    }
}