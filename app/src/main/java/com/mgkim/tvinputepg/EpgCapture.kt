package com.mgkim.tvinputepg

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.media.tv.TvContract
import android.media.tv.TvInputManager
import android.util.Log
import androidx.tvprovider.media.tv.TvContractCompat

private const val TAG = "EpgCapture"

object EpgCapture {


    /**
     * id를 직접 qurey하기 보다 uri로 채널데이터 가져오기
     */
    fun fetchChannelByUri(cr: ContentResolver, channelId: Long) {
        val uri = TvContract.buildChannelUri(channelId)
        Log.d(TAG, "fetchChannelByUri: uri is ${uri}")

        try {
            cr.query(uri, null, null, null, null).use { cursor ->
                if ((cursor != null) && (cursor.count > 0)) {
                    if (cursor.moveToFirst()) {
                        Log.d(TAG, "fetchChannelByUri: $cursor")
                    }
                }else{
                    Log.d(TAG, "fetchChannelByUri: no data")
                }
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Unable to get programs for " + uri + "code:" + e.toString()
            )
        }
    }

    /**
     *  프로그램 목록 가져오기
     */
    fun fetchPrograms(cr: ContentResolver, channelId: Long) {
        val proj = arrayOf(
            TvContract.Programs.COLUMN_TITLE,
            TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS,
            TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS,
            TvContract.Programs.COLUMN_SHORT_DESCRIPTION
        )

        val select = "${TvContract.Programs.COLUMN_CHANNEL_ID} = ?"
        val sArgs = arrayOf(channelId.toString())

        try {
            cr.query(
                TvContract.Programs.CONTENT_URI,
                proj,
                null, // select,
                null, // sArgs,
                null // "${TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS} ASC"
            ).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        val title =
                            cursor.getString(cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_TITLE))
                        val startTime =
                            cursor.getLong(cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS))
                        val endTime =
                            cursor.getLong(cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS))
                        val description =
                            cursor.getString(cursor.getColumnIndexOrThrow(TvContract.Programs.COLUMN_SHORT_DESCRIPTION))
                        Log.d(
                            TAG,
                            "Program: $title\n" +
                                    "Start: $startTime\n" +
                                    "End: $endTime\n" +
                                    "Desc: $description"
                        )
                    } while (cursor.moveToNext())
                } else {
                    Log.d(TAG, "No programs found for channel ID: $channelId")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching programs: ", e)
        }
    }

    /**
     * inputId에 따른 채널 목록 가져오기
     */
    fun fetchChannels(cr: ContentResolver, inputId: String? = null): List<ChannelData> {
        val channelList = mutableListOf<ChannelData>()
        val proj = arrayOf(
            TvContractCompat.Channels._ID,
            TvContractCompat.Channels.COLUMN_INPUT_ID,
            TvContractCompat.Channels.COLUMN_DISPLAY_NAME,
            TvContractCompat.Channels.COLUMN_DISPLAY_NUMBER,
            TvContractCompat.Channels.COLUMN_TYPE,
            TvContractCompat.Channels.COLUMN_SERVICE_TYPE,
            TvContractCompat.Channels.COLUMN_INTERNAL_PROVIDER_DATA,
            TvContractCompat.Channels.COLUMN_ORIGINAL_NETWORK_ID,
            TvContractCompat.Channels.COLUMN_SERVICE_ID,
            TvContractCompat.Channels.COLUMN_TRANSPORT_STREAM_ID
        )

        val select =
            if (inputId != null) "${TvContractCompat.Channels.COLUMN_INPUT_ID} = ?" else null
        val sArgs = if (inputId != null) arrayOf(inputId) else null

        try {
            cr.query(
                TvContractCompat.Channels.CONTENT_URI,
                proj,
                null,
                // select,
                null,
                // sArgs,
                null
            ).use { cursor ->
                Log.d(TAG, "size: ${cursor?.count ?: -1}")
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        val channelData = ChannelData(
                            channelId = cursor.getLong(cursor.getColumnIndexOrThrow(TvContractCompat.Channels._ID)),
                            inputId = cursor.getString(cursor.getColumnIndexOrThrow(TvContractCompat.Channels.COLUMN_INPUT_ID)),
                            name = cursor.getString(cursor.getColumnIndexOrThrow(TvContractCompat.Channels.COLUMN_DISPLAY_NAME)),
                            displayNumber = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TvContractCompat.Channels.COLUMN_DISPLAY_NUMBER
                                )
                            ),
                            type = cursor.getString(cursor.getColumnIndexOrThrow(TvContractCompat.Channels.COLUMN_TYPE)),
                            serviceType = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TvContractCompat.Channels.COLUMN_SERVICE_TYPE
                                )
                            ),
                            internalProviderData = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TvContractCompat.Channels.COLUMN_INTERNAL_PROVIDER_DATA
                                )
                            ),
                            originalNetworkId = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TvContractCompat.Channels.COLUMN_ORIGINAL_NETWORK_ID
                                )
                            ),
                            serviceId = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TvContractCompat.Channels.COLUMN_SERVICE_ID
                                )
                            ),
                            transportStreamId = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TvContractCompat.Channels.COLUMN_TRANSPORT_STREAM_ID
                                )
                            )
                        )

                        channelList.add(channelData)
                        Log.d(
                            TAG,
                            "Channel ID: ${channelData.channelId},\n" +
                                    "Input: ${channelData.inputId}\n" +
                                    "Name: ${channelData.name},\n" +
                                    "Display Number: ${channelData.displayNumber}\n" +
                                    "Type: ${channelData.type}\n" +
                                    "Service Type: ${channelData.serviceType}\n" +
                                    "Internal Provider Data: ${channelData.internalProviderData}"
                        )
                    } while (cursor.moveToNext())
                } else {
                    Log.d(TAG, "채널 없음 $inputId")
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException: ${e.message}", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}", e)
        }
        return channelList
    }

    /**
     *  tvInput 목록 가져오기
     */
    fun listTvInputs(context: Context) {
        val tvInputManager = context.getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager
        val inputs = tvInputManager.tvInputList
        if (inputs.isEmpty()) {
            Log.d(TAG, "no tv input")
        } else {
            inputs.forEach { input ->
                Log.d(
                    TAG,
                    "listTvInputs: " +
                            "${input.id} \n " +
                            "${input.type} \n " +
                            "${input.serviceInfo} \n "
                )
            }
        }
    }

    fun removeChannelsByInputId(contentResolver: ContentResolver, inputId: String?): Int {
        val selection = "${TvContract.Channels.COLUMN_INPUT_ID} = ?"
        val selectionArgs = arrayOf(inputId)

        return try {
            val rowsDeleted = contentResolver.delete(
                TvContract.Channels.CONTENT_URI,
                selection,
                selectionArgs
            )
            Log.d("DELETE", "Channels removed: $rowsDeleted for inputId: $inputId")
            rowsDeleted
        } catch (e: SecurityException) {
            Log.e("DELETE", "SecurityException: ${e.message}. Check WRITE_EPG_DATA permission.", e)
            0
        } catch (e: Exception) {
            Log.e("DELETE", "Error deleting channels: ${e.message}", e)
            0
        }
    }

    fun addTestChannel(contentResolver: ContentResolver, inputId: String) {
        val values = ContentValues().apply {
            put(TvContract.Channels.COLUMN_INPUT_ID, inputId)
            put(TvContract.Channels.COLUMN_DISPLAY_NAME, "Test Channel")
            put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, "1")
            put(TvContract.Channels.COLUMN_TYPE, TvContract.Channels.TYPE_OTHER)
            put(
                TvContract.Channels.COLUMN_SERVICE_TYPE,
                TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO
            )
        }

        try {
            val uri = contentResolver.insert(TvContract.Channels.CONTENT_URI, values)
            Log.d(TAG, "Test channel added: $uri")
        } catch (e: SecurityException) {
            Log.e(
                TAG,
                "SecurityException adding channel: ${e.message}. Check WRITE_EPG_DATA permission.",
                e
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error adding channel: ${e.message}", e)
        }
    }
}