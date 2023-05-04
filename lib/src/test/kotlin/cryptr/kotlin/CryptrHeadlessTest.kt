package cryptr.kotlin

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import cryptr.kotlin.enums.ChallengeType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@WireMockTest(proxyMode = true)
class CryptrHeadlessTest {

    var cryptrHeadless: CryptrHeadless? = null

    @BeforeEach
    fun init() {
        val tenantDomain = "shark-academy"
        val baseUrl = "http://dev.cryptr.eu"
        val defaultRedirectUrl = "http://dev.cryptr.eu:8080/callback"
        val apiKeyClientId = "my-api-key-client-id"
        val apiKeyClientSecret = "my-api-key-client-secret"
        cryptrHeadless =
            CryptrHeadless(tenantDomain, baseUrl, defaultRedirectUrl, apiKeyClientId, apiKeyClientSecret)
        System.setProperty("CRYPTR_API_KEY_TOKEN", "stored-api-key")
    }

    @Test
    fun createSSOChallengeForauth() {
        stubFor(
            post("/api/v2/sso-oauth-challenges")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"api_key_id\": \"some-api-key-id\",\n" +
                                "    \"authorization_url\": \"https://dev.cryptr.eu/org/acme-company/oauth2/saml?request_id=request-id\",\n" +
                                "    \"database\": \"sandbox\",\n" +
                                "    \"expired_at\": 1682672619,\n" +
                                "    \"redirect_uri\": \"http://dev.cryptr.eu:8080/callback\",\n" +
                                "    \"request_id\": \"request-id\",\n" +
                                "    \"saml_idp_id\": \"acme_company_BS8RohkywSxjoDnE2SEygL\"\n" +
                                "}"
                    )
                )
        )

        val challenge =
            cryptrHeadless?.createSSOChallenge(orgDomain = "acme-company", authType = ChallengeType.OAUTH)
        assertNotNull(challenge)
        assertEquals("request-id", challenge.requestId)
        assertEquals("sandbox", challenge.database)
        assertEquals("http://dev.cryptr.eu:8080/callback", challenge.redirectUri)
        assertEquals("acme_company_BS8RohkywSxjoDnE2SEygL", challenge.samlIdpId)
    }

    @Test
    fun createSSOOauthChallengeForauth() {
        stubFor(
            post("/api/v2/sso-oauth-challenges")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"api_key_id\": \"some-api-key-id\",\n" +
                                "    \"authorization_url\": \"https://dev.cryptr.eu/org/acme-company/oauth2/saml?request_id=request-id\",\n" +
                                "    \"database\": \"sandbox\",\n" +
                                "    \"expired_at\": 1682672619,\n" +
                                "    \"redirect_uri\": \"http://dev.cryptr.eu:8080/callback\",\n" +
                                "    \"request_id\": \"request-id\",\n" +
                                "    \"saml_idp_id\": \"acme_company_BS8RohkywSxjoDnE2SEygL\"\n" +
                                "}"
                    )
                )
        )

        val challenge = cryptrHeadless?.createSSOOauthChallenge(orgDomain = "acme-company")
        assertNotNull(challenge)
        assertEquals("request-id", challenge.requestId)
    }

    @Test
    fun createSSOSamlChallengeShouldReturnAuthorizationUrlIfRightOrga() {
        stubFor(
            post("/api/v2/sso-saml-challenges")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"api_key_id\": \"62847327-2101-4a36-a51c-e7016098ee18\",\n" +
                                "    \"authorization_url\": \"https://dev.cryptr.eu/org/acme-company/oauth2/saml?request_id=1546bfcf-9849-448c-a56a-265d1ef9c30d\",\n" +
                                "    \"database\": \"sandbox\",\n" +
                                "    \"expired_at\": 1682672619,\n" +
                                "    \"redirect_uri\": \"http://dev.cryptr.eu:8080/callback\",\n" +
                                "    \"request_id\": \"1546bfcf-9849-448c-a56a-265d1ef9c30d\",\n" +
                                "    \"saml_idp_id\": \"acme_company_BS8RohkywSxjoDnE2SEygL\"\n" +
                                "}"
                    )
                )
        )
        val challenge = cryptrHeadless?.createSSOSamlChallenge(orgDomain = "acme-company")
        assertNotNull(challenge, "should return object")
        assertNotNull(challenge.authorizationUrl)
        assertNotNull(challenge.requestId)
        assertTrue(URL(challenge.authorizationUrl).query.endsWith("1546bfcf-9849-448c-a56a-265d1ef9c30d"))
    }

    @Test
    fun createSSOSamlChallengeShouldReturnAuthorizationUrlIfRightEmail() {
        stubFor(
            post("/api/v2/sso-saml-challenges")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"api_key_id\": \"62847327-2101-4a36-a51c-e7016098ee18\",\n" +
                                "    \"authorization_url\": \"https://dev.cryptr.eu/org/blablabus/oauth2/saml?request_id=5b2c5427-d5c3-4057-9cdc-b8d9914d2a7e\",\n" +
                                "    \"database\": \"sandbox\",\n" +
                                "    \"expired_at\": 1682673845,\n" +
                                "    \"redirect_uri\": \"http://localhost:8080/callback\",\n" +
                                "    \"request_id\": \"5b2c5427-d5c3-4057-9cdc-b8d9914d2a7e\",\n" +
                                "    \"saml_idp_id\": \"blablabus_JdZyJveBnj5kP6QstzF8LQ\"\n" +
                                "}"
                    )
                )
        )
        val challenge = cryptrHeadless?.createSSOSamlChallenge(userEmail = "john@blablabus.fr")
        assertNotNull(challenge, "should return object")
        assertNotNull(challenge.authorizationUrl)
        assertNotNull(challenge.requestId)
        assertTrue(URL(challenge.authorizationUrl).query.endsWith("b2c5427-d5c3-4057-9cdc-b8d9914d2a7e"))
    }
//
//    @Test
//    fun createSSOSamlChallengeShouldFailIfUnmatchingInput() {
//        stubFor(
//            post("/api/v2/sso-saml-challenges")
//                .withHost(equalTo("dev.cryptr.eu"))
//                .willReturn(
//                    ok("Not Found")
//                )
//        )
//        val challengeResponse = cryptrHeadless?.createSSOSamlChallenge(orgDomain = "azerty")
//        assertEquals("{\"error\":\"Not Found\"}", challengeResponse.toString())
//    }
//
//    @Test
//    fun consumeSSOSamlChallengeCallback() {
//        stubFor(
//            post("/oauth/token")
//                .withHost(equalTo("dev.cryptr.eu"))
//                .willReturn(
//                    ok(
//                        "{\"code\": \"sso-challenge-auth-code\"}"
//                    )
//                )
//        )
//        val resp = cryptrHeadless?.consumeSSOSamlChallengeCallback("some-code")
//        assertEquals("sso-challenge-auth-code", resp?.getString("code"))
//    }
}