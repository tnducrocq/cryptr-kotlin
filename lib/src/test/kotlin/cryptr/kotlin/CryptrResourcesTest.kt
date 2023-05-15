package cryptr.kotlin

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import cryptr.kotlin.enums.ApplicationType
import cryptr.kotlin.enums.EnvironmentStatus
import cryptr.kotlin.models.*
import cryptr.kotlin.models.List
import cryptr.kotlin.models.deleted.DeletedApplication
import cryptr.kotlin.models.deleted.DeletedUser
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.text.Normalizer
import kotlin.test.*

@WireMockTest(proxyMode = true)
class CryptrResourcesTest {
    lateinit var cryptr: Cryptr

    @BeforeEach
    fun init() {
        val tenantDomain = "shark-academy"
        val baseUrl = "http://dev.cryptr.eu"
        val defaultRedirectUrl = "http://localhost:8080/callback"
        val apiKeyClientId = "my-api-key-client-id"
        val apiKeyClientSecret = "my-api-key-client-secret"
        cryptr = Cryptr(tenantDomain, baseUrl, defaultRedirectUrl, apiKeyClientId, apiKeyClientSecret)
        System.setProperty("CRYPTR_API_KEY_TOKEN", "stored-api-key")
    }


    @Test
    fun listOrganizations() {
        stubFor(
            get("/api/v2/organizations")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"data\": [\n" +
                                "        {\n" +
                                "            \"__type__\": \"Organization\",\n" +
                                "            \"domain\": \"thibaud-paco\",\n" +
                                "            \"environments\": [\n" +
                                "                {\n" +
                                "                    \"id\": \"57f6e6a5-e833-49c5-8172-a94ec7a91b50\",\n" +
                                "                    \"name\": \"production\",\n" +
                                "                    \"status\": \"down\"\n" +
                                "                },\n" +
                                "                {\n" +
                                "                    \"id\": \"40ce67ff-baa5-49bb-b20c-7ddefc7e205e\",\n" +
                                "                    \"name\": \"sandbox\",\n" +
                                "                    \"status\": \"down\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"inserted_at\": \"2023-04-27T13:54:42\",\n" +
                                "            \"name\": \"Thibaud Paco\",\n" +
                                "            \"updated_at\": \"2023-04-27T13:55:03\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"__type__\": \"Organization\",\n" +
                                "            \"domain\": \"thibaud-java\",\n" +
                                "            \"environments\": [\n" +
                                "                {\n" +
                                "                    \"id\": \"7389e2d4-e49c-4371-8bfb-1f6bc243fe74\",\n" +
                                "                    \"name\": \"production\",\n" +
                                "                    \"status\": \"down\"\n" +
                                "                },\n" +
                                "                {\n" +
                                "                    \"id\": \"f3751057-724d-41e8-9057-73067d46e715\",\n" +
                                "                    \"name\": \"sandbox\",\n" +
                                "                    \"status\": \"down\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"inserted_at\": \"2023-04-27T13:50:29\",\n" +
                                "            \"name\": \"thibaud-java\",\n" +
                                "            \"updated_at\": \"2023-04-27T13:50:49\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"pagination\": {\n" +
                                "        \"current_page\": 1,\n" +
                                "        \"next_page\": 2,\n" +
                                "        \"per_page\": 2,\n" +
                                "        \"prev_page\": null,\n" +
                                "        \"total_pages\": 12\n" +
                                "    },\n" +
                                "    \"total\": 23\n" +
                                "}"
                    )
                )
        )
        val organizationResponse = cryptr.listOrganizations()
        assertNotNull(organizationResponse)
        if (organizationResponse is APISuccess) {
            val organizationListing = organizationResponse.value
            assertEquals(2, organizationListing.data.size)
            assertEquals(23, organizationListing.total)
            assertContains(
                organizationListing.data,
                Organization(
                    domain = "thibaud-paco",
                    name = "Thibaud Paco",
                    updatedAt = "2023-04-27T13:55:03",
                    insertedAt = "2023-04-27T13:54:42",
                    environments = setOf(
                        Environment(
                            name = "production",
                            status = EnvironmentStatus.DOWN
                        ),
                        Environment(
                            name = "sandbox",
                            status = EnvironmentStatus.DOWN
                        )
                    )
                )
            )
            assertContains(
                organizationListing.data,
                Organization(
                    domain = "thibaud-java",
                    name = "thibaud-java",
                    updatedAt = "2023-04-27T13:50:49",
                    insertedAt = "2023-04-27T13:50:29",
                    environments = setOf(
                        Environment(
                            name = "production",
                            status = EnvironmentStatus.DOWN
                        ),
                        Environment(
                            name = "sandbox",
                            status = EnvironmentStatus.DOWN
                        )
                    )
                )
            )
        }

    }

    @Test
    fun getOrganization() {
        stubFor(
            get("/api/v2/organizations/thibaud-java")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__type__\": \"Organization\",\n" +
                                "    \"domain\": \"thibaud-java\",\n" +
                                "    \"environments\": [\n" +
                                "        {\n" +
                                "            \"id\": \"7389e2d4-e49c-4371-8bfb-1f6bc243fe74\",\n" +
                                "            \"name\": \"production\",\n" +
                                "            \"status\": \"down\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"id\": \"f3751057-724d-41e8-9057-73067d46e715\",\n" +
                                "            \"name\": \"sandbox\",\n" +
                                "            \"status\": \"down\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"inserted_at\": \"2023-04-27T13:50:29\",\n" +
                                "    \"name\": \"thibaud-java\",\n" +
                                "    \"updated_at\": \"2023-04-27T13:50:49\"\n" +
                                "}"
                    )
                )

        )
        val resp = cryptr.getOrganization("thibaud-java")
        if (resp is APISuccess) {
            assertEquals(
                Organization(
                    name = "thibaud-java",
                    domain = "thibaud-java",
                    updatedAt = "2023-04-27T13:50:49",
                    insertedAt = "2023-04-27T13:50:29",
                    environments = setOf(
                        Environment(
                            name = "production",
                            status = EnvironmentStatus.DOWN
                        ),
                        Environment(
                            name = "sandbox",
                            status = EnvironmentStatus.DOWN
                        )
                    )
                ), resp.value
            )
        }

    }

    @Test
    fun createOrganization() {
        stubFor(
            post("/api/v2/organizations")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__type__\": \"Organization\",\n" +
                                "    \"domain\": \"another-organization\",\n" +
                                "    \"environments\": [\n" +
                                "        {\n" +
                                "            \"id\": \"3aeaaa7d-9c9f-409b-b598-b08975673907\",\n" +
                                "            \"name\": \"production\",\n" +
                                "            \"status\": \"down\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"id\": \"410b9e25-95d8-4694-b0c1-4e3d29f490f5\",\n" +
                                "            \"name\": \"sandbox\",\n" +
                                "            \"status\": \"down\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"inserted_at\": \"2023-05-03T14:58:45\",\n" +
                                "    \"name\": \"Another organization\",\n" +
                                "    \"updated_at\": \"2023-05-03T14:58:45\"\n" +
                                "}"
                    )
                )
        )
        val org = Organization(name = "Another organization")
        val createdOrgaResp = cryptr.createOrganization(org)
        assertNotNull(createdOrgaResp)
        if (createdOrgaResp is APISuccess) {
            val createdOrga = createdOrgaResp.value
            assertEquals(org.name, createdOrga.name)

            val domain = Normalizer
                .normalize(org.name, Normalizer.Form.NFD)
                .replace("[^\\p{ASCII}]".toRegex(), "")
                .replace("[^a-zA-Z0-9\\s]+".toRegex(), "")
                .trim().replace("\\s+".toRegex(), "-")
                .lowercase()
            assertEquals(domain, createdOrga.domain)
        }
    }

    @Test
    fun deleteOrganization() {
        stubFor(
            delete("/api/v2/organizations/my-organization")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "  \"deleted\": true,\n" +
                                "  \"resource\": {\n" +
                                "    \"__type__\": \"Organization\",\n" +
                                "    \"domain\": \"thibaud-java\",\n" +
                                "    \"environments\": [\n" +
                                "      {\n" +
                                "        \"id\": \"7389e2d4-e49c-4371-8bfb-1f6bc243fe74\",\n" +
                                "        \"name\": \"production\",\n" +
                                "        \"status\": \"down\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"id\": \"f3751057-724d-41e8-9057-73067d46e715\",\n" +
                                "        \"name\": \"sandbox\",\n" +
                                "        \"status\": \"down\"\n" +
                                "      }\n" +
                                "    ],\n" +
                                "    \"inserted_at\": \"2023-04-27T13:50:29\",\n" +
                                "    \"name\": \"thibaud-java\",\n" +
                                "    \"updated_at\": \"2023-04-27T13:50:49\"\n" +
                                "  }\n" +
                                "}"
                    )
                )
        )

        val result = cryptr.deleteOrganization(Organization(name = "my Organization", domain = "my-organization"))
        assertIs<DeletedResource>(result)
        assertNotNull(result)
        assertTrue(result.deleted)
    }

    @Test
    fun listUsers() {
        stubFor(
            get("/api/v2/org/acme-company/users")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__type__\": \"List\",\n" +
                                "    \"data\": [\n" +
                                "        {\n" +
                                "            \"__domain__\": \"acme-company\",\n" +
                                "            \"__environment__\": \"sandbox\",\n" +
                                "            \"__type__\": \"User\",\n" +
                                "            \"address\": {\n" +
                                "                \"country\": \"FR\",\n" +
                                "                \"formatted\": \"165 avenue de Bretagne\\n59000, France\",\n" +
                                "                \"locality\": null,\n" +
                                "                \"postal_code\": \"59000\",\n" +
                                "                \"region\": \"Nord\",\n" +
                                "                \"street_address\": \"165 avenue de Bretagne\"\n" +
                                "            },\n" +
                                "            \"email\": \"nedra_boehm@hotmail.com\",\n" +
                                "            \"email_verified\": false,\n" +
                                "            \"id\": \"61254d31-3a33-4b10-bc22-f410f4927d42\",\n" +
                                "            \"inserted_at\": \"2023-05-02T12:09:41\",\n" +
                                "            \"meta_data\": [],\n" +
                                "            \"phone_number\": \"+1 555-415-1337\",\n" +
                                "            \"phone_number_verified\": false,\n" +
                                "            \"profile\": {\n" +
                                "                \"birthdate\": \"1943-01-19\",\n" +
                                "                \"family_name\": \"Joplin\",\n" +
                                "                \"gender\": \"female\",\n" +
                                "                \"given_name\": \"Janis\",\n" +
                                "                \"locale\": \"fr\",\n" +
                                "                \"nickname\": \"Jany\",\n" +
                                "                \"picture\": \"http://www.example.com/avatar.jpeg\",\n" +
                                "                \"preferred_username\": null,\n" +
                                "                \"website\": \"http://www.example.com\",\n" +
                                "                \"zoneinfo\": \"America/Los_Angeles\"\n" +
                                "            },\n" +
                                "            \"updated_at\": \"2023-05-02T12:09:41\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"__domain__\": \"acme-company\",\n" +
                                "            \"__environment__\": \"sandbox\",\n" +
                                "            \"__type__\": \"User\",\n" +
                                "            \"address\": null,\n" +
                                "            \"email\": \"omvold7jx62g@acme-company.io\",\n" +
                                "            \"email_verified\": false,\n" +
                                "            \"id\": \"d5f20c7c-c151-4177-8ee8-071d32317ea8\",\n" +
                                "            \"inserted_at\": \"2023-04-28T15:24:55\",\n" +
                                "            \"meta_data\": [],\n" +
                                "            \"phone_number\": null,\n" +
                                "            \"phone_number_verified\": false,\n" +
                                "            \"profile\": {\n" +
                                "                \"birthdate\": null,\n" +
                                "                \"family_name\": null,\n" +
                                "                \"gender\": null,\n" +
                                "                \"given_name\": null,\n" +
                                "                \"locale\": null,\n" +
                                "                \"nickname\": null,\n" +
                                "                \"picture\": null,\n" +
                                "                \"preferred_username\": null,\n" +
                                "                \"website\": null,\n" +
                                "                \"zoneinfo\": null\n" +
                                "            },\n" +
                                "            \"updated_at\": \"2023-04-28T15:24:55\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"pagination\": {\n" +
                                "        \"current_page\": 1,\n" +
                                "        \"current_pages\": [\n" +
                                "            1,\n" +
                                "            2,\n" +
                                "            3,\n" +
                                "            4,\n" +
                                "            5\n" +
                                "        ],\n" +
                                "        \"next_page\": 2,\n" +
                                "        \"per_page\": 2,\n" +
                                "        \"prev_page\": null,\n" +
                                "        \"total_pages\": 5\n" +
                                "    },\n" +
                                "    \"total\": 10\n" +
                                "}"
                    )
                )
        )
        val userListingResponse = cryptr.listUsers("acme-company")
        assertNotNull(userListingResponse)
        if (userListingResponse is APISuccess) {
            val userListing = userListingResponse.value
            assertIs<List<User>>(userListing)
            assertEquals(10, userListing.total)
            assertEquals(2, userListing.data.size)
            assertContains(userListing.data.map { u -> u.email }, "omvold7jx62g@acme-company.io")
            assertContains(userListing.data.map { u -> u.email }, "nedra_boehm@hotmail.com")
            assertContains(userListing.data.map { u -> u.address }, null)
        }
    }

    @Test
    fun getUserShouldReturnUser() {
        stubFor(
            get("/api/v2/org/acme-company/users/61254d31-3a33-4b10-bc22-f410f4927d42")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__domain__\": \"acme-company\",\n" +
                                "    \"__environment__\": \"sandbox\",\n" +
                                "    \"__type__\": \"User\",\n" +
                                "    \"address\": {\n" +
                                "        \"country\": \"FR\",\n" +
                                "        \"formatted\": \"165 avenue de Bretagne\\n59000, France\",\n" +
                                "        \"locality\": null,\n" +
                                "        \"postal_code\": \"59000\",\n" +
                                "        \"region\": \"Nord\",\n" +
                                "        \"street_address\": \"165 avenue de Bretagne\"\n" +
                                "    },\n" +
                                "    \"email\": \"nedra_boehm@hotmail.com\",\n" +
                                "    \"email_verified\": false,\n" +
                                "    \"id\": \"61254d31-3a33-4b10-bc22-f410f4927d42\",\n" +
                                "    \"inserted_at\": \"2023-05-02T12:09:41\",\n" +
                                "    \"meta_data\": [],\n" +
                                "    \"phone_number\": \"+1 555-415-1337\",\n" +
                                "    \"phone_number_verified\": false,\n" +
                                "    \"profile\": {\n" +
                                "        \"birthdate\": \"1943-01-19\",\n" +
                                "        \"family_name\": \"Joplin\",\n" +
                                "        \"gender\": \"female\",\n" +
                                "        \"given_name\": \"Janis\",\n" +
                                "        \"locale\": \"fr\",\n" +
                                "        \"nickname\": \"Jany\",\n" +
                                "        \"picture\": \"http://www.example.com/avatar.jpeg\",\n" +
                                "        \"preferred_username\": null,\n" +
                                "        \"website\": \"http://www.example.com\",\n" +
                                "        \"zoneinfo\": \"America/Los_Angeles\"\n" +
                                "    },\n" +
                                "    \"updated_at\": \"2023-05-02T12:09:41\"\n" +
                                "}"
                    )
                )
        )

        val resp = cryptr.getUser("acme-company", "61254d31-3a33-4b10-bc22-f410f4927d42")
        assertNotNull(resp)
        if (resp is APISuccess) {
            val user = resp.value
            assertEquals(user.email, "nedra_boehm@hotmail.com")
            assertEquals("FR", user.address?.country)
            assertEquals("165 avenue de Bretagne\n59000, France", user.address?.formatted)
            assertNull(user.address?.locality)
            assertEquals("59000", user.address?.postalCode)
            assertEquals("165 avenue de Bretagne", user.address?.streetAddress)

        }
    }

    @Test
    fun createUserByEmail() {
        stubFor(
            post("/api/v2/org/acme-company/users")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__domain__\": \"acme-company\",\n" +
                                "    \"__environment__\": \"sandbox\",\n" +
                                "    \"__type__\": \"User\",\n" +
                                "    \"address\": null,\n" +
                                "    \"email\": \"aryanna.stroman@gmail.com\",\n" +
                                "    \"email_verified\": false,\n" +
                                "    \"id\": \"9ef8cc11-40e0-432a-8816-6a3b5034519f\",\n" +
                                "    \"inserted_at\": \"2023-05-03T14:03:09\",\n" +
                                "    \"meta_data\": [],\n" +
                                "    \"phone_number\": null,\n" +
                                "    \"phone_number_verified\": false,\n" +
                                "    \"profile\": {\n" +
                                "        \"birthdate\": null,\n" +
                                "        \"family_name\": null,\n" +
                                "        \"gender\": null,\n" +
                                "        \"given_name\": null,\n" +
                                "        \"locale\": null,\n" +
                                "        \"nickname\": null,\n" +
                                "        \"picture\": null,\n" +
                                "        \"preferred_username\": null,\n" +
                                "        \"website\": null,\n" +
                                "        \"zoneinfo\": null\n" +
                                "    },\n" +
                                "    \"updated_at\": \"2023-05-03T14:03:09\"\n" +
                                "}"
                    )
                )
        )

        val resp = cryptr.createUser("acme-company", "aryanna.stroman@gmail.com")
        assertNotNull(resp)
        if (resp is APISuccess) {
            val user = resp.value
            assertEquals("aryanna.stroman@gmail.com", user.email)
            assertNull(user.profile?.birthdate)
            assertNull(user.address)
            assertNull(user.profile?.familyName)
            assertNull(user.profile?.gender)
            assertNull(user.profile?.givenName)
            assertNull(user.profile?.locale)
            assertNull(user.profile?.nickname)
            assertNull(user.phoneNumber)
            assertNull(user.profile?.picture)
            assertNull(user.profile?.website)
            assertNull(user.profile?.zoneInfo)
        }
    }

    @Test
    fun createUser() {
        stubFor(
            post("/api/v2/org/acme-company/users")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__domain__\": \"acme-company\",\n" +
                                "    \"__environment__\": \"sandbox\",\n" +
                                "    \"__type__\": \"User\",\n" +
                                "    \"address\": null,\n" +
                                "    \"email\": \"giuseppe.schoen@hotmail.com\",\n" +
                                "    \"email_verified\": false,\n" +
                                "    \"id\": \"d1e61734-514d-4755-8697-91143d11e528\",\n" +
                                "    \"inserted_at\": \"2023-05-03T14:10:52\",\n" +
                                "    \"meta_data\": [],\n" +
                                "    \"phone_number\": null,\n" +
                                "    \"phone_number_verified\": false,\n" +
                                "    \"profile\": {\n" +
                                "        \"birthdate\": null,\n" +
                                "        \"family_name\": null,\n" +
                                "        \"gender\": null,\n" +
                                "        \"given_name\": null,\n" +
                                "        \"locale\": null,\n" +
                                "        \"nickname\": null,\n" +
                                "        \"picture\": null,\n" +
                                "        \"preferred_username\": null,\n" +
                                "        \"website\": null,\n" +
                                "        \"zoneinfo\": null\n" +
                                "    },\n" +
                                "    \"updated_at\": \"2023-05-03T14:10:52\"\n" +
                                "}"
                    )
                )
        )

        val user = User(email = "omvold7jx62g@acme-company.io")
        val resp = cryptr.createUser("acme-company", user)
        assertNotNull(resp)
        if (resp is APISuccess) {
            assertNull(resp.value.address?.postalCode)
        }
    }

    @Test
    fun updateUser() {
        stubFor(
            put("/api/v2/org/acme-company/users/d1e61734-514d-4755-8697-91143d11e528")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__domain__\": \"acme-company\",\n" +
                                "    \"__environment__\": \"sandbox\",\n" +
                                "    \"__type__\": \"User\",\n" +
                                "    \"address\": null,\n" +
                                "    \"email\": \"giuseppe.schoen@hotmail.com\",\n" +
                                "    \"email_verified\": false,\n" +
                                "    \"id\": \"d1e61734-514d-4755-8697-91143d11e528\",\n" +
                                "    \"inserted_at\": \"2023-05-03T14:10:52\",\n" +
                                "    \"meta_data\": [],\n" +
                                "    \"phone_number\": null,\n" +
                                "    \"phone_number_verified\": false,\n" +
                                "    \"profile\": {\n" +
                                "        \"birthdate\": null,\n" +
                                "        \"family_name\": \"SCHOEN\",\n" +
                                "        \"gender\": null,\n" +
                                "        \"given_name\": \"Giuseppe\",\n" +
                                "        \"locale\": null,\n" +
                                "        \"nickname\": null,\n" +
                                "        \"picture\": null,\n" +
                                "        \"preferred_username\": null,\n" +
                                "        \"website\": null,\n" +
                                "        \"zoneinfo\": null\n" +
                                "    },\n" +
                                "    \"updated_at\": \"2023-05-03T14:10:52\"\n" +
                                "}"
                    )
                )
        )

        val response = cryptr.updateUser(
            User(
                resourceDomain = "acme-company",
                id = "d1e61734-514d-4755-8697-91143d11e528",
                email = "giuseppe.schoen@hotmail.com",
                profile = Profile(
                    familyName = "SCHOEN",
                    givenName = "Giuseppe"
                )
            )
        )

        assertNotNull(response)
        if (response is APISuccess) {
            assertIs<User>(response.value)
            assertEquals("SCHOEN", response.value.profile?.familyName)
            assertEquals("Giuseppe", response.value.profile?.givenName)
        }
    }

    @Test
    fun deleeteUser() {
        stubFor(
            delete("/api/v2/org/acme-company/users/d1e61734-514d-4755-8697-91143d11e528")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "  \"deleted\": true,\n" +
                                "  \"resource\": {\n" +
                                "  \"__domain__\": \"acme-company\",\n" +
                                "  \"__environment__\": \"sandbox\",\n" +
                                "  \"__type__\": \"User\",\n" +
                                "  \"address\": null,\n" +
                                "  \"email\": \"giuseppe.schoen@hotmail.com\",\n" +
                                "  \"email_verified\": false,\n" +
                                "  \"id\": \"d1e61734-514d-4755-8697-91143d11e528\",\n" +
                                "  \"inserted_at\": \"2023-05-03T14:10:52\",\n" +
                                "  \"meta_data\": [],\n" +
                                "  \"phone_number\": null,\n" +
                                "  \"phone_number_verified\": false,\n" +
                                "  \"profile\": {\n" +
                                "    \"birthdate\": null,\n" +
                                "    \"family_name\": \"SCHOEN\",\n" +
                                "    \"gender\": null,\n" +
                                "    \"given_name\": \"Giuseppe\",\n" +
                                "    \"locale\": null,\n" +
                                "    \"nickname\": null,\n" +
                                "    \"picture\": null,\n" +
                                "    \"preferred_username\": null,\n" +
                                "    \"website\": null,\n" +
                                "    \"zoneinfo\": null\n" +
                                "  },\n" +
                                "  \"updated_at\": \"2023-05-03T14:10:52\"\n" +
                                "}\n" +
                                "}"
                    )
                )
        )

        val response = cryptr.deleteUser(
            User(
                resourceDomain = "acme-company",
                id = "d1e61734-514d-4755-8697-91143d11e528",
                email = "giuseppe.schoen@hotmail.com"
            )
        )
        assertIs<DeletedUser>(response)
        assertTrue(response.deleted)
        assertEquals("giuseppe.schoen@hotmail.com", response.resource.email)
        assertEquals("acme-company", response.resource.resourceDomain)
    }

    @Test
    fun deleteUserShouldThrowError() {
        stubFor(
            delete("/api/v2/org/acme-company/users/d1e61734-514d-4755-869-91143d11e528")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(notFound())
        )

        val response = cryptr.deleteUser(
            User(
                resourceDomain = "acme-company",
                id = "d1e61734-514d-4755-869-91143d11e528",
                email = "giuseppe.schoen@hotmail.com"
            )
        )
        assertNull(response)
    }

    @Test
    fun listApplications() {
        stubFor(
            get("/api/v2/org/acme-company/applications")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__type__\": \"List\",\n" +
                                "    \"data\": [\n" +
                                "        {\n" +
                                "            \"__access__\": \"limited_to:acme-company\",\n" +
                                "            \"__domain__\": \"acme-company\",\n" +
                                "            \"__environment__\": \"sandbox\",\n" +
                                "            \"__managed_by__\": \"shark-academy\",\n" +
                                "            \"__type__\": \"Application\",\n" +
                                "            \"allowed_logout_urls\": [\n" +
                                "                \"https://d5ee-78-117-68-62.eu.ngrok.io\"\n" +
                                "            ],\n" +
                                "            \"allowed_origins_cors\": [\n" +
                                "                \"https://d5ee-78-117-68-62.eu.ngrok.io\"\n" +
                                "            ],\n" +
                                "            \"allowed_redirect_urls\": [\n" +
                                "                \"https://d5ee-78-117-68-62.eu.ngrok.io\"\n" +
                                "            ],\n" +
                                "            \"application_type\": \"ruby_on_rails\",\n" +
                                "            \"client_id\": \"bc3583eb-59e3-4edf-83c4-96bd308430cc\",\n" +
                                "            \"default_origin_cors\": \"https://d5ee-78-117-68-62.eu.ngrok.io\",\n" +
                                "            \"default_redirect_uri_after_login\": \"https://d5ee-78-117-68-62.eu.ngrok.io\",\n" +
                                "            \"default_redirect_uri_after_logout\": \"https://d5ee-78-117-68-62.eu.ngrok.io\",\n" +
                                "            \"description\": null,\n" +
                                "            \"id\": \"bc3583eb-59e3-4edf-83c4-96bd308430cc\",\n" +
                                "            \"inserted_at\": \"2023-05-02T16:06:47\",\n" +
                                "            \"name\": \"Max Rails app\",\n" +
                                "            \"updated_at\": \"2023-05-02T16:06:47\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"pagination\": {\n" +
                                "        \"current_page\": 1,\n" +
                                "        \"next_page\": null,\n" +
                                "        \"per_page\": 8,\n" +
                                "        \"prev_page\": null,\n" +
                                "        \"total_pages\": 1\n" +
                                "    },\n" +
                                "    \"total\": 1\n" +
                                "}"
                    )
                )
        )

        val applicationListingResponse = cryptr.listApplications("acme-company")
        assertNotNull(applicationListingResponse)
        if (applicationListingResponse is APISuccess) {
            val applicationListing = applicationListingResponse.value
            assertEquals(1, applicationListing.data.size)
            assertEquals(1, applicationListing.total)
            assertNull(applicationListing.pagination.nextPage)
            assertNull(applicationListing.pagination.prevPage)
            assertContains(applicationListing.data.map { a -> a.applicationType }, ApplicationType.RUBY_ON_RAILS)
        }
    }

    @Test
    fun getApplication() {
        stubFor(
            get("/api/v2/org/acme-company/applications/bc3583eb-59e3-4edf-83c4-96bd308430cc")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__access__\": \"limited_to:acme-company\",\n" +
                                "    \"__domain__\": \"acme-company\",\n" +
                                "    \"__environment__\": \"sandbox\",\n" +
                                "    \"__managed_by__\": \"shark-academy\",\n" +
                                "    \"__type__\": \"Application\",\n" +
                                "    \"allowed_logout_urls\": [\n" +
                                "        \"https://communitiz-app-vuejs.onrender.com\"\n" +
                                "    ],\n" +
                                "    \"allowed_origins_cors\": [\n" +
                                "        \"https://communitiz-app-vuejs.onrender.com\"\n" +
                                "    ],\n" +
                                "    \"allowed_redirect_urls\": [\n" +
                                "        \"https://communitiz-app-vuejs.onrender.com\"\n" +
                                "    ],\n" +
                                "    \"application_type\": \"ruby_on_rails\",\n" +
                                "    \"client_id\": \"bc3583eb-59e3-4edf-83c4-96bd308430cc\",\n" +
                                "    \"default_origin_cors\": \"https://communitiz-app-vuejs.onrender.com\",\n" +
                                "    \"default_redirect_uri_after_login\": \"https://communitiz-app-vuejs.onrender.com\",\n" +
                                "    \"default_redirect_uri_after_logout\": \"https://communitiz-app-vuejs.onrender.com\",\n" +
                                "    \"description\": null,\n" +
                                "    \"id\": \"bc3583eb-59e3-4edf-83c4-96bd308430cc\",\n" +
                                "    \"inserted_at\": \"2023-05-02T16:06:47\",\n" +
                                "    \"name\": \"Community App Communitiz Real QA App\",\n" +
                                "    \"updated_at\": \"2023-05-02T16:06:47\"\n" +
                                "}"
                    )
                )
        )

        val appResponse = cryptr.getApplication("acme-company", "bc3583eb-59e3-4edf-83c4-96bd308430cc")
        assertNotNull(appResponse)
        if (appResponse is APISuccess) {
            val app = appResponse.value
            assertEquals("bc3583eb-59e3-4edf-83c4-96bd308430cc", app.id)
            app.allowedLogoutUrls?.let { assertContains(it.asIterable(), "https://communitiz-app-vuejs.onrender.com") }
            app.allowedOriginsCors?.let { assertContains(it.asIterable(), "https://communitiz-app-vuejs.onrender.com") }
            app.allowedRedirectUrls?.let {
                assertContains(
                    it.asIterable(),
                    "https://communitiz-app-vuejs.onrender.com"
                )
            }
            assertEquals(ApplicationType.RUBY_ON_RAILS, app.applicationType)
            assertEquals("bc3583eb-59e3-4edf-83c4-96bd308430cc", app.clientId)
            assertEquals("https://communitiz-app-vuejs.onrender.com", app.defaultOriginCors)
            assertEquals("https://communitiz-app-vuejs.onrender.com", app.defaultRedirectUriAfterLogin)
            assertEquals("https://communitiz-app-vuejs.onrender.com", app.defaultRedirectUriAfterLogout)
            assertNull(app.description)
            assertEquals("2023-05-02T16:06:47", app.insertedAt)
            assertEquals("Community App Communitiz Real QA App", app.name)
            assertEquals("2023-05-02T16:06:47", app.updatedAt)
        }
    }

    @Test
    fun deleteApplication() {
        stubFor(
            delete("/api/v2/org/acme-company/applications/bc3583eb-59e3-4edf-83c4-96bd308430cc")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "  \"deleted\": true,\n" +
                                "  \"resource\": {\n" +
                                "  \"__access__\": \"limited_to:acme-company\",\n" +
                                "  \"__domain__\": \"acme-company\",\n" +
                                "  \"__environment__\": \"sandbox\",\n" +
                                "  \"__managed_by__\": \"shark-academy\",\n" +
                                "  \"__type__\": \"Application\",\n" +
                                "  \"allowed_logout_urls\": [\n" +
                                "    \"https://communitiz-app-vuejs.onrender.com\"\n" +
                                "  ],\n" +
                                "  \"allowed_origins_cors\": [\n" +
                                "    \"https://communitiz-app-vuejs.onrender.com\"\n" +
                                "  ],\n" +
                                "  \"allowed_redirect_urls\": [\n" +
                                "    \"https://communitiz-app-vuejs.onrender.com\"\n" +
                                "  ],\n" +
                                "  \"application_type\": \"ruby_on_rails\",\n" +
                                "  \"client_id\": \"bc3583eb-59e3-4edf-83c4-96bd308430cc\",\n" +
                                "  \"default_origin_cors\": \"https://communitiz-app-vuejs.onrender.com\",\n" +
                                "  \"default_redirect_uri_after_login\": \"https://communitiz-app-vuejs.onrender.com\",\n" +
                                "  \"default_redirect_uri_after_logout\": \"https://communitiz-app-vuejs.onrender.com\",\n" +
                                "  \"description\": null,\n" +
                                "  \"id\": \"bc3583eb-59e3-4edf-83c4-96bd308430cc\",\n" +
                                "  \"inserted_at\": \"2023-05-02T16:06:47\",\n" +
                                "  \"name\": \"Community App Communitiz Real QA App\",\n" +
                                "  \"updated_at\": \"2023-05-02T16:06:47\"\n" +
                                "}\n" +
                                "}"
                    )
                )
        )

        val appResponse = cryptr.deleteApplication(
            Application(
                id = "bc3583eb-59e3-4edf-83c4-96bd308430cc",
                resourceDomain = "acme-company",
                applicationType = ApplicationType.REGULAR_WEB,
                name = "Community App Communitiz Real QA App"
            )
        )
        assertNotNull(appResponse)
        assertIs<DeletedApplication>(appResponse)
        val app = appResponse.resource
        assertEquals("bc3583eb-59e3-4edf-83c4-96bd308430cc", app.id)
        app.allowedLogoutUrls?.let { assertContains(it.asIterable(), "https://communitiz-app-vuejs.onrender.com") }
        app.allowedOriginsCors?.let { assertContains(it.asIterable(), "https://communitiz-app-vuejs.onrender.com") }
        app.allowedRedirectUrls?.let {
            assertContains(
                it.asIterable(),
                "https://communitiz-app-vuejs.onrender.com"
            )
        }
        assertEquals(ApplicationType.RUBY_ON_RAILS, app.applicationType)
        assertEquals("bc3583eb-59e3-4edf-83c4-96bd308430cc", app.clientId)
        assertEquals("https://communitiz-app-vuejs.onrender.com", app.defaultOriginCors)
        assertEquals("https://communitiz-app-vuejs.onrender.com", app.defaultRedirectUriAfterLogin)
        assertEquals("https://communitiz-app-vuejs.onrender.com", app.defaultRedirectUriAfterLogout)
        assertNull(app.description)
        assertEquals("2023-05-02T16:06:47", app.insertedAt)
        assertEquals("Community App Communitiz Real QA App", app.name)
        assertEquals("2023-05-02T16:06:47", app.updatedAt)
    }

    @Test
    fun createApplication() {
        stubFor(
            post("/api/v2/org/acme-company/applications")
                .withHost(equalTo("dev.cryptr.eu"))
                .willReturn(
                    ok(
                        "{\n" +
                                "    \"__access__\": \"limited_to:acme-company\",\n" +
                                "    \"__domain__\": \"acme-company\",\n" +
                                "    \"__environment__\": \"sandbox\",\n" +
                                "    \"__managed_by__\": \"shark-academy\",\n" +
                                "    \"__type__\": \"Application\",\n" +
                                "    \"allowed_logout_urls\": [\n" +
                                "        \"https://angular.saas.io\"\n" +
                                "    ],\n" +
                                "    \"allowed_origins_cors\": [\n" +
                                "        \"https://angular.saas.io\"\n" +
                                "    ],\n" +
                                "    \"allowed_redirect_urls\": [\n" +
                                "        \"https://angular.saas.io\"\n" +
                                "    ],\n" +
                                "    \"application_type\": \"angular\",\n" +
                                "    \"client_id\": \"22bf3525-c3d7-4b0f-b28c-d126c801c9e5\",\n" +
                                "    \"default_origin_cors\": \"https://angular.saas.io\",\n" +
                                "    \"default_redirect_uri_after_login\": \"https://angular.saas.io\",\n" +
                                "    \"default_redirect_uri_after_logout\": \"https://angular.saas.io\",\n" +
                                "    \"description\": null,\n" +
                                "    \"id\": \"22bf3525-c3d7-4b0f-b28c-d126c801c9e5\",\n" +
                                "    \"inserted_at\": \"2023-05-03T16:29:32\",\n" +
                                "    \"name\": \"Some Angular app\",\n" +
                                "    \"updated_at\": \"2023-05-03T16:29:32\"\n" +
                                "}"
                    )
                )
        )
        val appResponse = cryptr.createApplication(
            "acme-company",
            Application(
                name = "Some Angular App",
                applicationType = ApplicationType.ANGULAR,
                allowedRedirectUrls = setOf("https://angular.saas.io"),
                allowedLogoutUrls = setOf("https://angular.saas.io"),
                allowedOriginsCors = setOf("https://angular.saas.io")
            )
        )
        assertNotNull(appResponse)
        if (appResponse is APISuccess) {
            val app = appResponse.value
            assertEquals("22bf3525-c3d7-4b0f-b28c-d126c801c9e5", app.id)
            app.allowedLogoutUrls?.let { assertContains(it.asIterable(), "https://angular.saas.io") }
            app.allowedOriginsCors?.let { assertContains(it.asIterable(), "https://angular.saas.io") }
            app.allowedRedirectUrls?.let { assertContains(it.asIterable(), "https://angular.saas.io") }
            assertEquals(ApplicationType.ANGULAR, app.applicationType)
            assertEquals("22bf3525-c3d7-4b0f-b28c-d126c801c9e5", app.clientId)
            assertEquals("https://angular.saas.io", app.defaultOriginCors)
            assertEquals("https://angular.saas.io", app.defaultRedirectUriAfterLogin)
            assertEquals("https://angular.saas.io", app.defaultRedirectUriAfterLogout)
            assertNull(app.description)
            assertEquals("2023-05-03T16:29:32", app.insertedAt)
            assertEquals("Some Angular app", app.name)
            assertEquals("2023-05-03T16:29:32", app.updatedAt)
        }
    }
}