package com.c22ps099.relasiahelperapp.ui.missionDetail

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.c22ps099.relasiahelperapp.R
import com.c22ps099.relasiahelperapp.data.Mission
import com.c22ps099.relasiahelperapp.databinding.FragmentMissionDetailBinding
import com.c22ps099.relasiahelperapp.network.responses.MissionDataItem
import com.c22ps099.relasiahelperapp.ui.login.LoginFragment
import com.c22ps099.relasiahelperapp.utils.DateFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MissionDetailFragment : Fragment() {

    companion object {
        const val EXTRA_MISSION = "extra_mission"
    }

    private var binding: FragmentMissionDetailBinding? = null
    private lateinit var googleAuth: FirebaseAuth

    private val missionDetailViewModel by viewModels<MissionDetailViewModel> {
        MissionDetailViewModel.Factory(
            "volunteer.baru",
            arguments?.getParcelable<MissionDataItem>(EXTRA_MISSION) as MissionDataItem,
            activity?.applicationContext as Application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        googleAuth = Firebase.auth
        val firebaseUser = googleAuth.currentUser

        if (firebaseUser == null) {
            val navigateAction = MissionDetailFragmentDirections
                .actionMissionDetailFragmentToLoginFragment()
            findNavController().navigate(navigateAction)

            val mLoginFragment = LoginFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(
                    R.id.nav_host_fragment,
                    mLoginFragment,
                    LoginFragment::class.java.simpleName
                )
                setReorderingAllowed(true)
                commit()
            }
        }

        missionDetailViewModel.apply {
            isSuccess.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { success ->
                    if (success) {
                        showSuccessDialog()
                    }
                }
            }
            isBookMission.observe(viewLifecycleOwner) { book ->
                binding?.apply {
                    btnBookmark.visibility = swapBookButton(!book)
                    btnUnbookmark.visibility = swapBookButton(book)
                }
            }
        }

        val mission = arguments?.getParcelable<MissionDataItem>(EXTRA_MISSION) as MissionDataItem
        showMissionDetail(mission)
        val missionString = Mission(mission.id)
        binding?.apply {
            fabBack.setOnClickListener {
                val navigateAction = MissionDetailFragmentDirections
                    .actionMissionDetailFragmentToHomeFragment()
                findNavController().navigate(navigateAction)
            }
            btnApply.setOnClickListener {
                applyVolunteerToMission(missionString)
            }
            btnBookmark.setOnClickListener {
                missionDetailViewModel.setBookMission(mission, true)
                Toast.makeText(
                    context,
                    getString(R.string.add_bookmark),
                    Toast.LENGTH_SHORT
                ).show()
            }
            btnUnbookmark.setOnClickListener {
                missionDetailViewModel.setBookMission(mission, false)
                Toast.makeText(
                    context,
                    getString(R.string.delete_bookmark),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun swapBookButton(isBook: Boolean): Int {
        return if (isBook) View.VISIBLE else View.INVISIBLE
    }

    private fun showMissionDetail(mission: MissionDataItem) {
        binding?.apply {
            ivDetailMission.let {
                Glide.with(requireActivity())
                    .load(mission.featuredImage[0])
                    .into(it)
            }
            tvMissionTitle.text = mission.title
            tvMissionDate.text = DateFormatter.formatDate(mission.startDate) + " - " + DateFormatter.formatDate(mission.endDate)
            tvMissionCity.text = mission.city + ", " + mission.province
            tvApplicant.text = mission.numberOfNeeds
            tvMissionReq.text = mission.requirement
            tvMissionNote.text = mission.note
        }
    }

    private fun applyVolunteerToMission(mission: Mission) {
        missionDetailViewModel.applyMission("volunteer.baru", mission)
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_success)
        dialog.show()
        val btnHome = dialog.findViewById<Button>(R.id.btn_home)
        btnHome.setOnClickListener {
            dialog.dismiss()
            val navigateAction = MissionDetailFragmentDirections
                .actionMissionDetailFragmentToHomeFragment()
            findNavController().navigate(navigateAction)
            dialog.hide()
        }
    }
}