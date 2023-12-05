package com.example.storyappasandy.view.login
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappasandy.databinding.ActivityLoginBinding
import com.example.storyappasandy.view.ViewModelFactory
import com.example.storyappasandy.view.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingBar: ProgressBar

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLogin()
        setupActionLogin()

        loadingBar = binding.loadingProgressBar



        viewModel.loginResponse.observe(this) { loginResult ->
            if (loginResult != null) {
                loadingBar.visibility = View.GONE

                if (loginResult.error == false) {

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    if (loginResult.error == true && loginResult.message == "User not found") {

                        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {

                        showErrorDialog("Login gagal: ${loginResult.message}")
                    }
                }
            }
        }


        viewModel.errorMessage.observe(this) { loginResponse ->
            if (loginResponse != null) {
                if (loginResponse.error == true) {

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                    binding.loadingProgressBar.visibility = View.GONE
                } else {

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }



    private fun setupActionLogin() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.loginUser(email, password)

            loadingBar.visibility = View.VISIBLE


            viewModel.userToken.observe(this@LoginActivity) { token ->
                if (token != null) {
                    if (token.isNotEmpty()) {
                        loadingBar.visibility = View.GONE

                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle("Selamat!")
                            setMessage("Berhasil")
                            setPositiveButton("OK") { _, _ ->
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()

                        }
                    } else {

                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle("Maaf!")
                            setMessage("Akun tidak ditemukan")
                            setPositiveButton("Kembali") {_,_ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }


    private fun setupLogin() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this@LoginActivity).apply {
            setTitle("Login Error")
            setMessage(errorMessage)
            setPositiveButton("OK") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

}