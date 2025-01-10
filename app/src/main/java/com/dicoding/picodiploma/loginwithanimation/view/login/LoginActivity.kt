package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.signup.SignupActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private var successDialog: AlertDialog? = null
    private var errorDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
        playAnimation()

        // Observasi status login selesai untuk menampilkan dialog sukses
        viewModel.isLoginComplete.observe(this) { isComplete ->
            if (isComplete) {
                showSuccessDialog(binding.emailEditText.text.toString())
                viewModel.resetLoginCompleteState() // Reset status agar dialog tidak muncul lagi
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Isi Email terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Isi Password terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    viewModel.login(email, password) // Memanggil login di ViewModel
                    // Menunggu dan mengambil token dari flow di ViewModel
                    viewModel.getToken.collectLatest { token ->
                        if (token != null) {
                            viewModel.saveSession(UserModel(email, token))
                        } else {
                            showErrorDialog("Gagal Login")
                        }
                    }
                }
            }
        }

        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    // Fungsi untuk menampilkan dialog sukses setelah login berhasil
    private fun showSuccessDialog(email: String) {
        successDialog = AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("Selamat datang kembali, $email")
            setPositiveButton("Lanjut") { _, _ ->
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }.create()
        successDialog?.show()
    }

    // Fungsi untuk menampilkan dialog error jika login gagal
    @Suppress("SameParameterValue")
    private fun showErrorDialog(message: String) {
        errorDialog = AlertDialog.Builder(this).apply {
            setTitle("Gagal")
            setMessage(message)
            setPositiveButton("Kembali", null)
        }.create()
        errorDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        successDialog?.dismiss()
        errorDialog?.dismiss()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
