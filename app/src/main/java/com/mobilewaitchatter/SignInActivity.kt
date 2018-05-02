package com.mobilewaitchatter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.mobilewaitchatter.util.FireStoreUtil
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask


class SignInActivity : AppCompatActivity() {

    private val RC_SIGH_IN = 1

    private val signInProviders =
            listOf(AuthUI.IdpConfig.EmailBuilder()
                    .setAllowNewAccounts(true)
                    .setRequireName(true)
                    .build())

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

       account_sign_in.setOnClickListener{
           val intent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(signInProviders)
                   .setLogo(R.mipmap.ic_launcher)
                   .build()

           startActivityForResult(intent, RC_SIGH_IN)
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGH_IN){
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == Activity.RESULT_OK){
                val progressDialog = indeterminateProgressDialog("Configurando Conta")
                FireStoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                    progressDialog.dismiss()
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                if(response == null){
                    return
                }
                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK -> longSnackbar(constraint_layout, "Sem Internet")
                    ErrorCodes.UNKNOWN_ERROR -> longSnackbar(constraint_layout, "Erro Desconhecido")
                }
            }
        }

    }
}
