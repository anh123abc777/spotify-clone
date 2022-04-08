import com.example.spotifyclone.network.Track

data class DataPlaylistItems(
    val items: List<PlaylistItems>
)

data class PlaylistItems(
    val added_at: String?,
    val is_local: Boolean?,
    val primary_color: String?,
    val track: Track,
)
