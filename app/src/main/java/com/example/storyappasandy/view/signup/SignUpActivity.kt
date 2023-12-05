package com.example.storyappasandy.view.signup

import AkunViewModel
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storyappasandy.databinding.ActivitySignUpBinding
import com.example.storyappasandy.view.AkunViewModelFactory
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AkunViewModel
    private lateinit var loadingBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = AkunViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AkunViewModel::class.java)

        setupActionSign()

        loadingBar = binding.loadingProgressBar
    }

    private fun setupActionSign() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (loadingBar.visibility != View.VISIBLE) {

                loadingBar.visibility = View.VISIBLE

                lifecycleScope.launch {
                    viewModel.registerUser(name, email, password)
                    

                    loadingBar.visibility = View.GONE

                    AlertDialog.Builder(this@SignUpActivity).apply {
                        setTitle("Selamat!")
                        setMessage("Akun dengan $email sudah dibuat. Ayo Login dan bagikan cerita mu !")
                        setPositiveButton("Lanjut") { _, _ ->
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
