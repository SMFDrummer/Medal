package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import packet.Packet
import service.model.User
import packet.PacketIdentifier
/**
 * 刷新家族
 *
 * @property ad 家族刷新方式
 * @property f [TODO]
 * @property fi 植物家族 id
 * @property l 家族锁
 * @property lv 家族等级
 */
@PacketIdentifier("V326")
class V326(override val user: User) : Packet() {
    override val identifier: String = "V326"

    /**
     * 家族刷新方式
     *
     * ```
     * ad = "0" // 免费或钻石刷新
     * ad = "1" // 广告刷新
     * ```
     */
    var ad: String = "0"

    /**
     * [TODO]
     *
     * ```
     * f = "0" // ad 为 1
     * f = "1" // ad 为 0
     * ```
     */
    var f: String = "0"

    /**
     * 植物家族 id
     *
     * 50001 - 新人组
     *
     * 50002 - 光芒万丈
     *
     * 50003 - 不动如山
     *
     * 50004 - 真能打
     *
     * 50005 - 我要打十个
     *
     * 50006 - 火力全开
     *
     * 50007 - 冰力四射
     *
     * 50008 - 雷霆万钧
     *
     * 50009 - 能量武器
     *
     * 50010 - 精英豌豆
     *
     * 50011 - 军火库
     *
     * 50012 - 三分王
     *
     * 50013 - 神射手
     *
     * 50014 - 百步穿僵
     *
     * 50015 - 人多力量大
     *
     * 50016 - 踩僵尸的蘑菇
     *
     * 50017 - 暗影家族
     *
     * 50018 - 环保卫士
     *
     * 50019 - 文艺青年
     *
     * 50020 - 忍者小队
     *
     * 50021 - 大厨组合
     *
     * 50022 - 摧枯拉朽
     *
     * 50023 - 坚固防线
     *
     * 50024 - 控场大师
     *
     * 50025 - 魔法大师
     *
     * 50026 - 枝繁叶茂
     *
     * 50027 - 十二生肖
     *
     * 50028 - 繁花似锦
     *
     * 50029 - 打飞他们
     *
     * 50030 - 十万伏特
     *
     * 50031 - 动物世界
     *
     * 50032 - 炸个痛快
     *
     * 50033 - 小心脚下
     *
     * 50034 - 惊声尖笑
     *
     * 50035 - 运动健将
     *
     * 50036 - 不如跳舞
     *
     * 50037 - 头有点晕
     *
     * 50038 - 酸甜苦辣
     *
     * 50039 - 武林对决
     *
     * 50040 - 地爆天星
     *
     * 50041 - 光暗交织
     *
     * 50042 - 亿点控制
     *
     * 50043 - 冰与火
     *
     * 50044 - 未来科技
     *
     * 50045 - 花开富贵
     *
     * 50046 - 火力压制
     *
     * 50047 - 群卜荟萃
     *
     * ```
     * fi = "12345"
     * ```
     */
    var fi: String = "12345"

    /**
     * 家族锁
     *
     * ```
     * l = "" // 未解锁第二属性
     * l = " /* 具体锁 */ " // 提供锁的属性不会被刷新
     * ```
     */
    var l: String = ""

    /**
     * 家族等级
     *
     * ```
     * lv = "4"
     * ```
     */
    var lv: String = "4"

    @Serializable
    data class Data(
        @SerialName("ad") val ad: String,
        @SerialName("f") val f: String,
        @SerialName("fi") val fi: String,
        @SerialName("l") val l: String,
        @SerialName("lv") val lv: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ad = ad,
            f = f,
            fi = fi,
            l = l,
            lv = lv,
            pi = user.credential!!.personalId,
            sk = user.credential!!.securityKey,
            ui = user.credential!!.userId
        )
    ).jsonObject
}

