package com.mgkim.tvinputepg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.tvprovider.media.tv.TvContractCompat
import com.mgkim.tvinputepg.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        EpgCapture.apply {
            fetchChannelByUri(contentResolver, 199)
            Log.d(TAG, "channels fetch: ${TvContractCompat.Channels.CONTENT_URI}")
            listTvInputs(this@MainActivity)
//            addTestChannel(contentResolver, "$packageName/.TestTvInputService") // 그냥 추가는 됨
//            removeAllChannelsByInputId(contentResolver, null) // 채널 삭제는 system app만 가능하다.
//            contentResolver.delete(TvContractCompat.buildChannelUri(932), null, null) // 이 방식으로는 삭제됨 -> selection을 안하기 때문에
            fetchPrograms(contentResolver, 11)
            val res = fetchChannels(contentResolver)
            Log.d(TAG, "onCreate: ${res}")
            fetchChannels(contentResolver, "com.humaxdigital.corona.tvinput.jcom/.HJcomAribInput")
            fetchChannels(contentResolver, "tv.abema/.components.service.AbemaLiveTvInputService")
//            fetchPrograms(contentResolver, 11)
        }
    }
}