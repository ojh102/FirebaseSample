package com.github.ojh.firebaseauthsample


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {


    val TAG = "LoginFragment"

    val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    val mGoogleApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(context)
                .enableAutoManage(activity, { Log.e(TAG, "google api client connect fail") })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    val mAuthListener: FirebaseAuth.AuthStateListener by lazy {
        FirebaseAuth.AuthStateListener {
            if (it.currentUser != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + it.currentUser?.uid);
                navigateToMain()
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001

        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_google.setSize(SignInButton.SIZE_STANDARD)
        btn_google.setOnClickListener { signIn() }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            Log.d(TAG, "User is signed in")
            navigateToMain()
        } else {
            // No user is signed in
            Log.d(TAG, "No user is signed in")
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener { mAuthListener }
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener { mAuthListener }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                firebaseAuth(credential)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuth(credential: AuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)

                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        navigateToMain()
                    }
                }
    }

    private fun navigateToMain() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity.finish()
    }
}
