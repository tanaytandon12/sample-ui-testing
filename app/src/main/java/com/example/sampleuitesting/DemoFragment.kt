package com.example.sampleuitesting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sampleuitesting.databinding.FragmentDemoBinding
import kotlinx.coroutines.launch

class DemoFragment : Fragment() {

    private var _binding: FragmentDemoBinding? = null
    private val mBinding get() = _binding!!

    private val mFactory by lazy {
        DemoViewModel.Factory()
    }

    private val mViewModel: DemoViewModel by lazy {
        ViewModelProvider(requireActivity(), mFactory)[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDemoBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchInfo()
        mBinding.btnRetry.setOnClickListener {
            fetchInfo()
        }
    }

    private fun fetchInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.fetchInfo(randomId()).collect {
                when (it) {
                    is DemoDataStatus.Loading -> {
                        mBinding.pbDemo.visibility = View.VISIBLE
                        mBinding.grpSuccess.visibility = View.GONE
                        mBinding.grpError.visibility = View.GONE
                    }
                    is DemoDataStatus.Success -> {
                        mBinding.tvTitle.text = it.data.title
                        mBinding.tvDescription.text = it.data.description
                        mBinding.grpSuccess.visibility = View.VISIBLE
                        mBinding.pbDemo.visibility = View.GONE
                        mBinding.grpError.visibility = View.GONE
                    }
                    is DemoDataStatus.Error -> {
                        mBinding.pbDemo.visibility = View.GONE
                        mBinding.grpSuccess.visibility = View.GONE
                        mBinding.tvError.text = it.msg
                        mBinding.grpError.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun randomId(): Long = (Math.random() * 100).toLong()
}