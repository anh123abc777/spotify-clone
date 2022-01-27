import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpotifyData (

	val href : String,
	val items : List<Items>,
	val limit : Int,
	val next : String?="",
	val offset : Int,
	val previous : String?="",
	val total : Int?=0
) : Parcelable