package com.farhan.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class ListStory(
    @field: SerializedName("listStory")
    val listStory: List<Story>,
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
)
