package cinderellaadmin.vajaralabs.com.admin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, Response {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private AsyncPOST asyncPOST;
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    Activity activity;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox chk_shop, chk_delivery, chk_factory, chk_admin;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.sky));
        }

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        activity = this;
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        chk_shop = (CheckBox) findViewById(R.id.chk_shop);
        chk_delivery = (CheckBox) findViewById(R.id.chk_delivery);
        chk_factory = (CheckBox) findViewById(R.id.chk_factory);
        chk_admin = (CheckBox) findViewById(R.id.chk_admin);
        chk_shop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chk_delivery.setChecked(false);
                    chk_factory.setChecked(false);
                    chk_admin.setChecked(false);
                    checker = "shop";
                }
            }
        });
        chk_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chk_shop.setChecked(false);
                    chk_factory.setChecked(false);
                    chk_admin.setChecked(false);
                    checker = "delivery";

                }
            }
        });
        chk_factory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chk_delivery.setChecked(false);
                    chk_shop.setChecked(false);
                    chk_admin.setChecked(false);
                    checker = "factory";

                }
            }
        });
        chk_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chk_delivery.setChecked(false);
                    chk_factory.setChecked(false);
                    chk_shop.setChecked(false);
                    checker = "admin";

                }
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEmailView.getText().toString().equals("")) {
                    if (!mPasswordView.getText().toString().equals("")) {
                        if (mEmailView.getText().toString().equals("admin") && mPasswordView.getText().toString().equals("admin")) {
                            if (checker.equals("admin")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Configg.storeDATA(activity, "type", "admin");
                                finish();
                                startActivity(intent);
                            } else {

                                Toast.makeText(getApplicationContext(), "Choose Valid Login Type.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Toast.makeText(getApplicationContext(), "Enter The Valid Credential.", Toast.LENGTH_SHORT).show();
                            if (!checker.equals("")) {
                                if (!checker.equals("admin")) {

                                    if (Configg.isNetworkAvailable(LoginActivity.this)) {

                                        nameValuePairs.clear();
                                        nameValuePairs.add(new BasicNameValuePair("mobile", mEmailView.getText().toString().trim()));
                                        nameValuePairs.add(new BasicNameValuePair("password", mPasswordView.getText().toString().trim()));
                                        nameValuePairs.add(new BasicNameValuePair("type", checker));
                                        asyncPOST = new AsyncPOST(nameValuePairs, LoginActivity.this, Configg.MAIN_URL + Configg.ADMIN_LOGIN, LoginActivity.this);
                                        asyncPOST.execute();
                                    } else {
                                        Configg.alert("Warning...!", "Please Check Your Internet Connection.", 0, LoginActivity.this);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Choose Valid Login Type.", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Choose Login Type.", Toast.LENGTH_SHORT).show();

                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter The Valid Credential.", Toast.LENGTH_SHORT).show();
                        mPasswordView.requestFocus();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Enter The Valid Credential.", Toast.LENGTH_SHORT).show();
                    mEmailView.requestFocus();
                }
//                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

//        mEmailView.setAdapter(adapter);
    }

    @Override
    public void processFinish(String output) {

        try {
            JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("1")) {
                Configg.storeDATA(activity, "type", jsonObject.getString("type"));


                if (jsonObject.getString("type").equals("shop")) {
                    Configg.storeDATA(activity, "shop_name", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("shop_name"));
                    Configg.storeDATA(activity, "sid", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("sid"));
                    Configg.storeDATA(activity, "shop_keeper", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("shop_keeper"));

                    Configg.storeDATA(activity, "email", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("email"));
                    Configg.storeDATA(activity, "mobile", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("mobile"));
                    Configg.storeDATA(activity, "address", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("address"));
                    Configg.storeDATA(activity, "password1", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("password"));
                    Configg.storeDATA(activity, "city", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("city"));
                    Configg.storeDATA(activity, "city_id", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("cid"));

                    Configg.storeDATA(activity, "locality_id", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("aid"));

                    Configg.storeDATA(activity, "locality", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("locality"));
                    Configg.storeDATA(activity, "shop_code", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("shop_code"));
                    Configg.storeDATA(activity, "shop_code_and_name", jsonObject.getJSONArray("shop_list").getJSONObject(0).getString("shop_code_and_name"));


                } else if (jsonObject.getString("type").equals("delivery")) {
                    Configg.storeDATA(activity, "delivery_person", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("delivery_person"));
                    Configg.storeDATA(activity, "did", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("did"));
                    Configg.storeDATA(activity, "email", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("email"));
                    Configg.storeDATA(activity, "address", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("address"));
                    Configg.storeDATA(activity, "mobile", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("mobile"));
                    Configg.storeDATA(activity, "password2", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("password"));
                    Configg.storeDATA(activity, "sid", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("sid"));
                    Configg.storeDATA(activity, "shop_code", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("shop_code"));
                    Configg.storeDATA(activity, "shop_code_and_name", jsonObject.getJSONArray("delivery_list").getJSONObject(0).getString("shop_code_and_name"));


                } else if (jsonObject.getString("type").equals("factory")) {
                    Configg.storeDATA(activity, "factory_person", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("contact_person"));
                    Configg.storeDATA(activity, "fid", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("fid"));
                    Configg.storeDATA(activity, "factory_name", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("factory_name"));
                    Configg.storeDATA(activity, "address", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("address"));
                    Configg.storeDATA(activity, "mobile", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("mobile"));
                    Configg.storeDATA(activity, "password3", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("password"));
                    Configg.storeDATA(activity, "email", jsonObject.getJSONArray("factory_list").getJSONObject(0).getString("email"));


                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } else {
                Configg.alert("Warning...!", jsonObject.getString("message"), 0, LoginActivity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

