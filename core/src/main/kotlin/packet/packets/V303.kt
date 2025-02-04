package packet.packets
import packet.PacketIdentifier
import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import manager.config
import packet.Packet
import service.model.User

/**
 * 进入活动
 *
 * @property al 活动 id 总集合
 */
@PacketIdentifier("V303")
class V303(override val user: User) : Packet() {
    override val identifier: String = "V303"

    /**
     * 活动 id 总集合
     *
     * 10613 - 挑战僵博切换活动
     *
     * 10621 - 充值有礼
     *
     * 10622 - 无尽挑战
     *
     * 10704 (type = 0) - 旅行原木界面二
     *
     * 10704 (type = 1) - 超 Z 联赛
     *
     * 10710 - 签到领装扮（永劫无间胡桃）
     *
     * 10741 - 充值 30 返利
     *
     * 10744 - 旅行原木界面一
     *
     * 10749 - 戴夫的神秘宝藏
     *
     * 10759 - 扭蛋活动
     *
     * 10799 - 挑战僵博立即前往（需配合 10613）
     *
     * 10800 - 潘妮追击
     *
     * 10802 - 神秘商店
     *
     * 10803 - 追击指南
     *
     * 10808 - 植物探险
     *
     * 10828 - 七日指南
     *
     * 10833 - 问卷调查
     *
     * 10835 - 限时活动（总入口）
     *
     * 10836 - 回忆之旅
     *
     * 10839 - 回忆成就
     *
     * 10840 - 创意庭院
     *
     * 10843 - 潘妮的课堂（新手）
     *
     * 10844 - 时空寻宝
     *
     * 10848 - 成长有礼（氪金金豆）
     *
     * 10849 - 时空秘境
     *
     * 10851 - 夏日庆典（夏令营）
     *
     * 10854 - 限时召唤
     *
     * 10858 - 狂欢嘉年华（刷新）
     *
     * 10859 - 双人对决
     *
     * 10860 - 超级自选
     *
     * 10861 - 双人对决任务面板
     *
     * 10868 - 邀请新用户
     *
     * 10871 - 植物培育
     *
     * 10873 - 派对助力
     *
     * 10874 - 旅行手册（新手）
     *
     * 10875 - 旅行手册 7 日签到（新手）
     *
     * 10876 - 新人折扣（牛仔手套）
     *
     * 10877 - 首充 6 元（新手）
     *
     * 10879 - 龙族宝库
     *
     * 10881 - 奇珍宝箱
     *
     * 10888 - 僵局逃脱
     *
     * [TODO] - 幸运许愿池
     *
     * ```
     * al = listof(12345)
     * ```
     */
    var al: List<Int> = listOf(12345)

    /**
     * 活动状态, 只有 id = 10704 打开 旅行原木界面二 时为 0, 默认为 1
     */
    var type: Int = 1

    @Serializable
    data class Data(
        @SerialName("al") val al: List<Al>,
        @SerialName("ci") val ci: String = "103",
        @SerialName("cs") val cs: String = "0",
        @SerialName("pack") val pack: String = config.channel.packageName,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("v") val v: String = config.channel.version,
    ) {
        @Serializable
        data class Al(
            @SerialName("abi") val abi: Int = 0,
            @SerialName("config_version") val configVersion: Int = 1,
            @SerialName("id") val id: Int,
            @SerialName("type") val type: Int,
        )
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            al = al.map { Data.Al(id = it, type = type) },
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

