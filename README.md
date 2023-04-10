Koji Buildroot Resolver
-----------------------
A desktop application for examining the buildroots of RPMs in [Koji][_koji]. This application was written using 
[Kotlin][_kotlin] and [Compose Multiplatform][_compose_multiplatform].

Application
-----------
This application is being built from the data layer up, and as such may not have a working `Main` (application entry point) at any given time.

Configuration
-------------
This application is intended to work with any Koji instance with a little configuration, however at this time it is limited to instances with `/kojihub` as the root resource for the API endpoint. This limitation can be overcome by dropping Retrofit in favor of 
directly using OkHttp it's just low priority.

Anyway, if you want to use an instance of Koji with `/kojihub` as the root API resource, here is an example configuration file:

```json
{
  "name": "Fedora",
  "api": "https://koji.fedoraproject.org/",
  "hub": "https://koji.fedoraproject.org/koji",
  "packages": "https://kojipkgs.fedoraproject.org"
}
```

> :grey_exclamation_mark: Notice the "api" URL does not include the root resource `/kojihub`. If you do include it, then the Retrofit client will attempt to make POST requests
> to `<api>/kojihub/kojihub`, which may not be what you want.

This configuration file needs to live at `~/config/kbr/config.json`.

[_koji]: https://docs.pagure.org/koji/
[_kotlin]: https://kotlinlang.org/docs/home.html
[_compose_multiplatform]: https://www.jetbrains.com/lp/compose-mpp/