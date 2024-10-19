package com.malqaa.androidappp.newPhase.presentation.fragments.nofication_fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentLogoutNotificationBinding
import com.malqaa.androidappp.newPhase.presentation.activities.signup.activity1.SignupConfirmNewUserActivity


class LogoutNotification : Fragment() {

    // Declare the binding variable
    private var _binding: FragmentLogoutNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentLogoutNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the toolbar title and color
        binding.toolbarLogoutNoti.title = "Notifications"
        binding.toolbarLogoutNoti.setTitleTextColor(Color.WHITE)

        // Create ClickableSpan for Signup
        val clickSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false // Remove underline
            }

            override fun onClick(view: View) {
                val intent = Intent(requireContext(), SignupConfirmNewUserActivity::class.java)
                startActivity(intent)
            }
        }

        // Create a SpannableString for the notification text
        val myId = binding.txNoti3.text.toString().trim()
        val spannableString = SpannableString(myId).apply {
            setSpan(clickSpan, 22, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(resources.getColor(R.color.greenspan)), 22, 31, 0)
            setSpan(StyleSpan(Typeface.BOLD), 22, 31, 0)
        }

        // Set the text to the TextView with the SpannableString
        binding.txNoti3.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = spannableString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }
}
