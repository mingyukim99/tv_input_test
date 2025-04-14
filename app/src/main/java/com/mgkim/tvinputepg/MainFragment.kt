package com.mgkim.tvinputepg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mgkim.tvinputepg.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    private val channelAdapter by lazy { ChannelAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val dummyChannel = mutableListOf<ChannelData>()
            repeat(10) { idx ->
                dummyChannel.add(
                    ChannelData(
                        channelId = idx.toLong(),
                        inputId = "$idx",
                        name = "채널$idx",
                        displayNumber = "$idx",
                        type = "$idx",
                        serviceType = "$idx",
                        internalProviderData = "$idx"
                    ),
                )
            }
            val validChannelList = EpgCapture.fetchChannels(requireContext().contentResolver)
            rv.adapter = channelAdapter
            if (validChannelList.isEmpty()) {
                channelAdapter.submitList(dummyChannel)
            }else{
                channelAdapter.submitList(validChannelList)
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentMainBinding.bind(view)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}