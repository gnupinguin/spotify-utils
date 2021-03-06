package io.github.gnupinguin.spotify.web.rest

import io.github.gnupinguin.spotify.service.SpotifyService
import io.github.gnupinguin.spotify.client.SpotifyTrack
import io.github.gnupinguin.spotify.service.importer.UserTracksLoader
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class ImportEndpoint(private val userTracksLoader: UserTracksLoader,
                     private val spotifyService: SpotifyService
) {

    @PostMapping(value = ["import"])
    fun importFromFile(@RequestParam("file") file: MultipartFile) {
        val userTracks = userTracksLoader.loadTracks(file)
        val spotifyTracks = spotifyService.searchTracks(userTracks)
        spotifyService.addToSpotify(spotifyTracks)
    }

    //TODO unlogin periodically happens
    @PostMapping("search", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchSongs(@RequestParam("file") file: MultipartFile): List<SpotifyTrack> {
        val userTracks = userTracksLoader.loadTracks(file)
        return spotifyService.searchTracks(userTracks)
    }

    @GetMapping("user")
    fun getUser(@AuthenticationPrincipal principal: OAuth2User): Map<String, String> {
        return mapOf(
            "name" to principal.attributes["display_name"].toString()
        )
    }


}