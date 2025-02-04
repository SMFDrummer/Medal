package smf.talkweb.medal.pref

enum class MedalPreferences(val key: String, val initValue: Any) {
    Channel("platform_channel", 2),
    GameHost("platform_gameHost", 0)
}