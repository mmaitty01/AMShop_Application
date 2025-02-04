package org.swu.aeymai.amshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import org.swu.aeymai.amshop.Util.getLoading


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var tvSwapMode: TextView
    lateinit var nextBtn: Button
    lateinit var navController: NavController
    var isLoginForm: Boolean = true

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        navController = findNavController()

        nextBtn = view.findViewById(R.id.goToOnbaordBtn) as Button;
        etEmail = view.findViewById<EditText>(R.id.etEmailInput)
        etPassword = view.findViewById<EditText>(R.id.etPasswordInput)
        tvSwapMode = view.findViewById<TextView>(R.id.swapToRegisterBtn)
        nextBtn.setOnClickListener {
            val email = etEmail.text.toString().trim { it <= ' ' }
            val password = etPassword.text.toString().trim { it <= ' ' }
            if(password.length < 6) {
                Toast.makeText(activity, "minimum password length is 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (isLoginForm) {
                    login(email, password)
                } else {
                    register(email, password)
                }
            } else {
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(activity, "Please enter your email address and password.", Toast.LENGTH_SHORT).show()
                } else if (email.isEmpty()) {
                    Toast.makeText(activity, "Please enter your email address.", Toast.LENGTH_SHORT).show()
                } else if (password.isEmpty()) {
                    Toast.makeText(activity, "Please enter your password.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvSwapMode.setOnClickListener {
            isLoginForm = !isLoginForm;
            if (isLoginForm) {
                nextBtn.text = "LOGIN"
                tvSwapMode.text = "SignUp"
            } else {
                nextBtn.text = "REGISTER"
                tvSwapMode.text = "SignIn"
            }
        }
    }

    private fun login(email: String, password: String) {
        var loadingDialog = getLoading()
        loadingDialog.show()
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loadingDialog.dismiss()
            if (!task.isSuccessful) {
                Toast.makeText(activity, "Authentication Failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
            } else {
                // Case Login Success
                navController.navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
    }


    private fun register(email: String, password: String) {
        var loadingDialog = getLoading()
        loadingDialog.show()
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loadingDialog.dismiss()
            if (task.isSuccessful) {
                // Case Success
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                    Toast.makeText(activity, "Create account successfully!, Please check your email for verification", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}