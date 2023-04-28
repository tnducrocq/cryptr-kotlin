# cryptr-kotlin

This Kotlin library provides tools for Cryptr API

## Documentation

See the [Cryptr API Reference](https://docs.cryptr.co)

## Installation

### Apache Maven

```xml

<dependency>
    <groupId>org.cryptr</groupId>
    <artifactId>lib</artifactId>
    <version>VERSION</version>
</dependency>


```

### Gradle (Groovy DSL)

```groovy
dependencies {
    implementation 'org.cryptr:lib:VERSION'
}
```

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("org.cryptr:lib:VERSION")
}
```

## Installation

```kotlin

import cryptr.kotlin.Library

// if you use system properties you call just init like this
val cryptr = Library()

val cryptr = Library(
    "my-saas-company",
    "https://my-saas-company.authent.me",
    "https://saas-company.app.com/callback",
    "api-key-id",
    "api-key-secret"
)
```

### SSO SAML Headless process

This process allows you to generate a challenge to start a SSO SAML authent process without using a front-end for the
entire process

```kotlin

// 1. generate a challenge from any point of your app (requires network) and retrieve authorization URL
val createSSOSamlChallengePayload =
    cryptr.createSSOSamlChallenge(
        redirectUri = "https://localhost:8080/some-callback-endpoint",
        orgDomain = orgDomain,
        userEmail = userEmail
    )

val authorizationUrl = createSSOSamlChallengePayload.get("authorization_url")

// 2. Give this authorization URL to the end-user (ex: by email or just by a redirection)

// 3. handle the redirection on the chosen enpoint (here '/some-callback-endpoint)
// on this enpoint you get a `code` parameter
val callbackResp = cryptr.consumeSSOSamlChallengeCallback(call.parameters.get("code"))
val endUserAccessToken = callbackResp.get("access_token")
```

## System property keys

| key                              | sample value                      | purpose                                                      |
|----------------------------------|-----------------------------------|--------------------------------------------------------------|
| **CRYPTR_TENANT_DOMAIN**         | `your-tenant-domain`              | Your Account domain                                          |
| **CRYPTR_BASE_URL**              | `https://company;authent.me`      | Your Cryptr service URL                                      |
| **CRYPTR_DEFAULT_REDIRECT_URL**  | `https://localhost:8080/callback` | The URL where to redirect end-user after SSO authent process |
| **CRYPTR_API_KEY_CLIENT_ID**     | `xxx`                             | Your API Key client ID                                       |
| **CRYPTR_API_KEY_CLIENT_SECRET** | `xxx`                             | Your API Key client Secret                                   |