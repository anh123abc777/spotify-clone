package com.example.tonezone

import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tonezone.adapter.GenreAdapter
import com.example.tonezone.adapter.PlaylistAdapter
import com.example.tonezone.adapter.TrackAdapter
import com.example.tonezone.network.*
import com.example.tonezone.player.PlayerScreenViewModel

//@BindingAdapter("imageUrl")
//fun bindImage(imgView : ImageView, imgUrl : String?){
//    imgUrl?.let {
//        val imgUri : Uri = ewImgUrl.toUri().buildUpon().scheme("https").build()
//
//        Glide.with(imgView.context)
//            .load(imgUri)
//            .apply(
//                RequestOptions())
//            .into(imgView)
//    }
//}

@BindingAdapter("valueGenres")
fun bindDataGenres(recyclerView: RecyclerView, data: Topic?){
    val adapter = recyclerView.adapter as GenreAdapter
    if (data!=null) {
        adapter.submitList(data.genres)
    }
}

@BindingAdapter("valueYourLibrary")
fun bindDataYourLibrary(recyclerView: RecyclerView, data: UserPlaylists?){
    val adapter = recyclerView.adapter as PlaylistAdapter
    if (data!=null) {
        adapter.submitList(data.items)
    }
}

@BindingAdapter("playlist")
fun bindDataPlaylist(recyclerView: RecyclerView,data: List<Track>?){
    val adapter = recyclerView.adapter as TrackAdapter
    if(data!=null){
        adapter.submitList(data)
    }
}

@BindingAdapter(value = ["imageUrl","listImageUrl"],requireAll = false)
fun bindImage(imageView: ImageView,imageUrl: String?,listImageUrl: List<Image>?){

    if(listImageUrl?.size!=0)
        Glide.with(imageView.context)
            .load(listImageUrl?.get(0)?.url)
            .apply(RequestOptions()
                .placeholder(R.drawable.ic_baseline_home_24)
                .error(R.drawable.ic_custom_play))
            .into(imageView)


    if(imageUrl!=null)
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.ic_baseline_home_24)
                .error(R.drawable.ic_custom_play))
            .into(imageView)
}

@BindingAdapter("formatTime")
fun bindTime(textView: TextView, timeInt : Long?){
    timeInt?.let {
        textView.text = DateUtils.formatElapsedTime(timeInt/1000)
    }
}

@BindingAdapter("artists")
fun bindTextView(textView: TextView, list: List<Artist>){
    var artists = ""
    list.forEachIndexed { index, artist ->
        artists += artist.name+ if(index!=list.size-1) ", " else ""
    }
    textView.text = artists
}

@BindingAdapter("playerState")
fun bindStatePlayButton(button: AppCompatButton, state: PlayerScreenViewModel.PlayerState){
    when(state){
        PlayerScreenViewModel.PlayerState.PLAY -> button.setBackgroundResource(R.drawable.ic_custom_pause)
        PlayerScreenViewModel.PlayerState.PAUSE -> button.setBackgroundResource(R.drawable.ic_custom_play)
    }
}

@BindingAdapter("isShuffling")
fun bindColorShuffleButton(imageView: ImageView, isShuffling: Boolean){
    if(isShuffling)
        imageView.setColorFilter(ContextCompat.getColor(imageView.context,R.color.primary))
    else
        imageView.setColorFilter(ContextCompat.getColor(imageView.context,R.color.gray))
}