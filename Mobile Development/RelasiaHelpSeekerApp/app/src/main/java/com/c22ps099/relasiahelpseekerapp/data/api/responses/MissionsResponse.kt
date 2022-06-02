package com.c22ps099.relasiahelpseekerapp.data.api.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MissionsResponse(

	@field:SerializedName("data")
	val data: List<MissionItem?>? = null,

	@field:SerializedName("length")
	val length: Int? = null
)

@Parcelize
data class MissionItem(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("requirement")
	val requirement: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("volunteers")
	val volunteers: Map<String,String>? = null,

	@field:SerializedName("featured_image")
	val featuredImage: List<String?>? = null,

	@field:SerializedName("number_of_needs")
	val numberOfNeeds: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
):Parcelable
