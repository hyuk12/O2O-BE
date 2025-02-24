plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "o2o-be"

include("application-admin")
include("application-client")
include("application-oss")
include("common")
include("domains")
include("domains:member")
include("domains:order")
include("domains:review")
include("domains:store")
include("domains:payment")
include("domains:cart")
include("domains:favorite")
include("infrastructure")
include("infrastructure:store-redis")
include("infrastructure:store-mongo")
include("infrastructure:member-postgres")
include("infrastructure:member-redis")
include("infrastructure:order-postgres")
include("infrastructure:payment-postgres")
include("infrastructure:order-redis")
include("infrastructure:review-postgres")
include("infrastructure:cart-mongo")
include("infrastructure:favorite-postgres")
include("infrastructure:order-mongo")
