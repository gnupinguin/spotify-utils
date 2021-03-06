package io.github.gnupinguin.spotify.service.importer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

interface UserTracksLoader {

    fun loadTracks(file: MultipartFile): List<UserTrack>

}

@Component
class UserTracksLoaderImpl(private val objectMapper: ObjectMapper) : UserTracksLoader {

    override fun loadTracks(file: MultipartFile): List<UserTrack> {
        return objectMapper.readValue(file.bytes)
    }

}

data class UserTrack(val band: String, val song: String)