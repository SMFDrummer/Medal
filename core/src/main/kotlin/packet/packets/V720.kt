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
 * 创意庭院关卡发布
 *
 * @suppress 自定义编辑
 * @see V720.Data
 */
@PacketIdentifier("V720")
class V720(override val user: User) : Packet() {
    override val identifier: String = "V720"

    @Serializable
    data class Data(
        // lvd 解密后的 Json SHA-256 校验
        @SerialName("checksum") val checksum: String = "dcc7e62855b0eb4953136b5a252aa6da2cc553fe8c9bd5bc339e1b99d231fc57",
        // 未知 MD5
        @SerialName("ci") val ci: String = "0627ab233c5a8af5799d555fb086bc74",
        // 下载次数
        @SerialName("dl") val dl: String = "1",
        // id
        @SerialName("id") val id: String = "0",
        // Gzip 加密的 关卡 Json 数据
        @SerialName("lvd") val lvd: String = "H4sIAAAAAAAAA-1dbXOqPBP-Q88HXup9xo-1RxCm0ilKgHwzpOcGDdY5-Ia__tmEgFprjz21rb2b6TitEJIrm-xeu0nYPpTuxBnPfji5V-Ab55-H0o2I6Yq_nRxNH6KO5mSrbGgjA4dr1-fXx9dGf0zTeOxs-kZfj4dpiseBeTdkE28Y6Dh3Vnc_cRZvkt1n7-PITUnUz-6mRYZsphE7yO4yJ7u9cbs48sZJzlbUZkuSQRuZUziTNaM5KoZTNI9zVDrjxxWUHVLbKqHcAuuezsv2x9f8ehRHmPm2pcUD0Y9hYlsmmXhL0kUL3p_-zSoLzE5KjdZPYqyreuR1ZFgGRt4Kh63JKGxNA7s9I1PfJblX4lBnZHqf3Y2vC4fRFDBB3VCnwNBZQnktKXfq4X1k8x_OhPe7paEeW-FBdT_M29AXxnj50Cya73UZZ9p5FHIy_uVyGRFDz0Yhuie2NcbIXVLZTiXTFWCy2re9L1BPrusk1Ft8PtT3AnudxgYaEDOB57rNeMM4r38N1j8cNluS0J1h5LcSMVfc1QjGLA7bj1-nHucr1jOnoT5_QO00yfVlMu4eH_vyTHPowurxzqQb71VPNFhl96E_GUX-jBitlFidIgZbg_P2srZ1PthDYbfA3gaGtcCWp4Ftnwt7z-1iD83JZGsb7412AeO-CEw_TRp7J-eGsGlFxufV0Q_ToLycQ-NOm9vqxGYTHLmDOGSL17XrrUahzxIoT6Ffte2X9dUyuovBNsO9zJnytjvLZOqnpOKxe-irFodrbuvB9lzJemUZa2u7wNYDn1hFMx6Zy7F3wa4vgymSuEHOebvSoUxbw_1gBG0PuMwj14jFuFwv-j2Ow00BO5SnnPO0B8lH1LA2I9vVY2NewDM_iYkWnH9uhxZvz07y9oZaFJ6rxgwZwC89wSX014Dj5LrtPdLIZ0FuzTj3Qb2yfy1LPAfYeNn7nOv83IF7rJF7t6UTVM2TQeR1k9yaYgMB_z_O-5HeFrbgbziLc3VjQ3j9HuBrm6LewQnz_63P_5Fbrk-wec5R3bsdnsIFb31e2tzBUVnwOdfHXKe53kRaAWNftxeAn1bpwTEZVHP24p8nEdiGaPI2-_7Z8_ELPa94RPHIxfCItOORYZUkbJf94_N49RVsmXr-HFzAfsHcnxPTKd8W01w8v28-Qabr4zK9P2ucqOIXxTsXyztfyF97z_ip4d03xiCn25gPtLuNjTpeXz87q6zK88qq_wU47t05Ujuv3_CE4xDYvok1wTbTRmF7AbaX92FBbbwkhrTr0r4Im8S8lOT-kIL9JYNnbJkoL_mCeRq3pZIHaswbqIcI_T36cSm31bJvK2FHzc4M26g8akOPt1vzT2O75Z6IqK-W0TC35js8InloVfGw5Jqa82S9NVdt11santrODY7dt9mC6K4ucVcctWOrkM3mOGAgc1TS3CpE_RUXcw6WezNWSXuTCo9J08RIMxp6vwk8A7h04TNIHvGn7jIx_Qieq_a5GOcnX8g9igTngl2wxqMe0mCuAu9diXrr_vniOc4vfG64fC7-HtjtAm_H-w7KVfOkyzZ-z02xQRmWvPirmqMWzLEFrWVx4JfM30OXGDbZilrtKYz_lHMmDa0XdOGUNbI_6udJcdkf13guYs3wv8ItL8j5bzAdl_uFxS8fwZenj_9J-0Bq3U7FT18xfjrdbr2CH07R8Vdw1ik2-sw6-kZ7-iFrbDikraTn_4K5vPqwMwMn8U519igy2Dy5eRsPXtg65heP8Rq_Ehzoa7Xep_jq6_HVx9nwk_juNBv8EWcYTtPtr8hzp9vgv-G6N8Z9H7d-e1KMdl6-OnOMdHpf_2at8PT9MLVWqNYKL2at8BW-93l17BPiuXOv2bwgq0_gufflqbfGZOddn7ysuOrMZ95Pnxcn8dTHxbA655AOtv2ZeBcH7CovT6Z-Toz2ROp8ZaOquGUI_vkEYqFlkmvP2UNevuacvXdu9vz0kuv18Q-PHxsfX8QsMt5iR-3w0XZrDmvs_278Vo9RADFquOWimssqXpB81ZW8me3HZTu2sea67TjwuE_GZlTiljy3tWEM7ofIBZmzZIpFfNi_uar4HHhcvncEsaffkr6FGUfeYzxF4xHETzsxqOQiXBLT00R8Jt_x4hwn5K7rgrf5-wexkerApUdjXxn33XBdGGnpUry_JeXO39GS88RNNB_iPjrFoeRWiNW4jlTxvd_4J099m889Q_YJa4nf6tzFmbnnuD0987mLT3hf6sx7bt8pnlM8p3hO8dyl8Vy9lwPxd0657PQ4eqHOz4-7XpDff-VcyLnO1b-ir5_AffXaOob64LOgPZQmH1vn-65Dnpmj1T6d2qc7-z4dEtw7xCGa3OfWYqT5KbW7FadW9fVHkaeJdVaOp-eyxEDa0GYb3o9X-UzVuC9AP1owTk29Ytz4uqtYV90Z34zn7EAzbKT1XGh0Vcivx9exBd8LXd2ud3N9p2wU0sfa32r0SMiK62UlE4Ftx8-CNiIcsumod_-Uj-fSPiisx7CKfRdaEMMFv3QfC8zFR9DLAoetqVxzP5JXBvTf8MoR-CQjwLTX9m4dwXaeDm0LdMjT4mZvoNorqfYY9uIGsTdR2Ryhq0Ph_1Z2ywO9Ef4oYBN_-ybYwEoXCfR1dfvzutj_3aG3VvHMdfhtaUW4eeY6_I4G67-oT6_sAWAHTmPD0PpHynGII54LBy93fGmIjVjh25ZRlznIyyNsoJBNl-shyG5c6ehVFTOIfRCwVb1mb4Pu8NS2vT_pv9ib8ZfUQKVvoxw3Y9QpYfwZlRz2OnviMhysZ4l5X-N6ml9IfH96xuDp92d8-UPff7rnExyutTyzNrPP-4d7xod7zO6ev6VwKVznxhXt-7ZPY91vJw-F67vi4jy6087Bvv13k4fC9W1xIX2vHeUfKVzfFZfyjxQuhUv5RwqXwqX8I4VL4VL-kcKlcCn_SOFSuJR_pHApXC_7R3J_Pm-ne-d3-HkeY80GtrXANjp2PmHvrKn4PzU75xv2_m-N4h2F67vjEueM0jTR988_4twqEgMpTlK4FC7OSXAdeGezfwZulsfheoNVPK9wKVwinofrThx5-zlCcpySnsdUrK9wKVzK51K4FC7lcylcCpfyudQYKlzK57owWSlc3xaX8rkULoVL-VwKl8L13j6XzDVc5dMIWzkxt3v9KLc2OHBLHFq_ceRUuQlCtqhznUD5CY7-rfF6cZTWeTxckvvs4abGhVNoa_uOPfh1uzk0nskDsc0D3RN93s1vdZjDYSe_SjUOu_mMD3NFbHOUdcQYfAssSBc5n50JKjC0D37FRuYtOBh7qKuX5GwyiHxGAraAObOp86XU8_VJeZ4bpxDXqnwT4ntsWGWTWzpzy-219piYNCOm-H9houw9v97db0vkh9m9Vs1VkXsB2azAwZN7E7SIQZ4iJ0yVN8FNtP1r1bwPeM5s_QF5Osn1JckRz_mmJVPEAM8Njdg-p0z9kobymUAX7dflK5m2GO3RZZLPq7wjXdaVY3KHI98kpvu7yiuybsae5_bo_3TK_ri_gZ9stywy2iWx93K6Pa3HI4Z_cM8PW2mcr5lvt02Sr5dxuIu9wdihkf8I5ffm4TBHGoV2R5qlUztdJpOUxaG_GdltbRDU-ViaOlBi8_p9hi2eV6-Zn9s-RJ1JHB3cr-XDzw5dSfycw-1R2JolRspwk2elGQ9-f9veze4YwjXDKg7aGnfN_jBYe0K2QWXfIiZyHUGbLe_m6sobOnp_w7hO_O__iaFrIZiMAAA,",
        // 关卡号, 也包含在关卡 Json 数据中
        @SerialName("n") val n: String = "and02223333",
        @SerialName("pi") val pi: String,
        // 未知变量
        @SerialName("pk") val pk: String = "0",
        // 未知变量
        @SerialName("s") val s: String = "95",
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        // 世界序号
        @SerialName("w") val w: String = "0",
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

