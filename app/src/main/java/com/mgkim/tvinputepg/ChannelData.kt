package com.mgkim.tvinputepg

data class ChannelData(
    val channelId: Long,
    val inputId: String?,
    val name: String?,
    val displayNumber: String?,
    val type: String?,
    val serviceType: String?,
    val internalProviderData: String?,
    val originalNetworkId: String? = null,
    val serviceId: String? = null,
    val transportStreamId: String? = null
)