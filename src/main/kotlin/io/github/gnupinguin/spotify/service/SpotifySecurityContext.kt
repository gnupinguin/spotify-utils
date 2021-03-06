package io.github.gnupinguin.spotify.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component

interface SpotifySecurityContext {

    fun getAccessToken(): String

    fun getUserId(): String

}

@Component
class SpotifySecurityContextImpl(private val clientService: OAuth2AuthorizedClientService) : SpotifySecurityContext {

    override fun getAccessToken(): String {
        return getClient().accessToken.tokenValue
    }

    override fun getUserId(): String {
        return getClient().principalName
    }

    private fun getClient(): OAuth2AuthorizedClient {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.javaClass.isAssignableFrom(OAuth2AuthenticationToken::class.java)) {
            val oauthToken = authentication as OAuth2AuthenticationToken
            val clientRegistrationId = oauthToken.authorizedClientRegistrationId
            if (clientRegistrationId == "spotify") {
                return clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.name)
            }
        }
        throw RuntimeException("OAuth2AuthorizedClient not found")
    }

}