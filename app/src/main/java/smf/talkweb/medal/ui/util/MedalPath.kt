package smf.talkweb.medal.ui.util

enum class MedalPath(val locate: String, val suffix: String) {
    MedalUserSingle("account/single",".msg"),
    MedalUserStore("account/store",".mst"),
    MedalCredential("account/credential",".mcd")
}