package packet

import api.logger
import getInt
import getString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.bouncycastle.crypto.engines.RijndaelEngine
import org.bouncycastle.crypto.modes.CBCBlockCipher
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.bouncycastle.crypto.paddings.ZeroBytePadding
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV
import parseObject
import toJsonString
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.MessageDigest
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

object Crypto {
    object Request {
        fun encrypt(data: Pair<String, JsonObject>): Pair<String, String> = data.first to TwNetwork(data.first).encrypt(data.second.toJsonString(JsonFeature.ImplicitNulls).toByteArray())
        fun decrypt(data: Pair<String, String>): Pair<String, JsonObject> = data.first to TwNetwork(data.first).decrypt(data.second).decodeToString().parseObject(JsonFeature.ImplicitNulls)
    }

    object Response {
        fun encrypt(data: String): String {
            val parse = Json.parseObject(data)
            return buildJsonObject {
                put("i", parse.getString("i"))
                put("r", parse.getInt("r"))
                put("e", TwNetwork(parse.getString("i")!!).encrypt(data.toByteArray()))
            }.toJsonString(JsonFeature.ImplicitNulls)
        }

        fun decrypt(data: String): String = try {
            val parse = Json.parseObject(data)
            TwNetwork(parse.getString("i")!!).decrypt(parse.getString("e")!!).decodeToString()
        } catch (_: Exception) {
            data
        }
    }

    object Gzip {
        private fun String.addQuotes(): String = "\"$this\""
        private fun String.subQuotes(): String = this.removeSurrounding("\"")

        fun encrypt(data: String): String = Base64.encrypt(
            ByteArrayOutputStream().use { out ->
                GZIPOutputStream(out).use { gzip ->
                    gzip.write(
                        Base64.encrypt(data.toByteArray(StandardCharsets.UTF_8))
                            .addQuotes()
                            .toByteArray(StandardCharsets.UTF_8)
                    )
                }
                out.toByteArray()
            }
        )

        fun decrypt(data: String): String = Base64.decrypt(
            GZIPInputStream(ByteArrayInputStream(Base64.decrypt(data))).use { gzip ->
                ByteArrayOutputStream().use { out ->
                    val buffer = ByteArray(256)
                    while (true) {
                        val n = gzip.read(buffer)
                        if (n < 0) break
                        out.write(buffer, 0, n)
                    }
                    out.toByteArray().toString(StandardCharsets.UTF_8).subQuotes()
                }
            }
        ).decodeToString()
    }

    object Base64 {
        fun encrypt(data: ByteArray): String = Chars.encrypt(java.util.Base64.getEncoder().encodeToString(data))
        fun decrypt(data: String): ByteArray = java.util.Base64.getDecoder().decode(Chars.decrypt(data))
    }

    object TwPay {
        private val HEX = "0123456789ABCDEF".toCharArray()
        private val IV_PARAMETER = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        private val cipher: (Int) -> Cipher = {
            Cipher.getInstance("DES/CBC/PKCS5Padding").apply {
                init(it, generateKey("TwPay001"), IvParameterSpec(IV_PARAMETER))
            }
        }

        @Suppress("SameParameterValue")
        private fun generateKey(password: String): Key {
            val dks = DESKeySpec(password.toByteArray(StandardCharsets.UTF_8))
            val keyFactory = SecretKeyFactory.getInstance("DES")
            return keyFactory.generateSecret(dks)
        }

        fun encrypt(data: String): String = buildString {
            cipher(Cipher.ENCRYPT_MODE).doFinal(data.toByteArray(StandardCharsets.UTF_8)).forEach {
                append(HEX[it.toInt() and 0xf0 shr 4])
                append(HEX[it.toInt() and 0xf])
            }
        }

        fun decrypt(data: String): String = String(cipher(Cipher.DECRYPT_MODE).doFinal(
            data.uppercase(Locale.getDefault())
                .chunked(2)
                .map {
                    val m = it[0].code - if (it[0] > '9') 55 else 48
                    val n = it[1].code - if (it[1] > '9') 55 else 48
                    ((m shl 4) + n).toByte()
                }
                .toByteArray()
        ), StandardCharsets.UTF_8)
    }

    object Number {
        fun encrypt(data: Int): Int = ((data xor 13) shl 13) or (data ushr 19)
        fun decrypt(data: Int): Int = ((data ushr 13) xor 13) or (data shl 19)
    }

    object Chars {
        fun encrypt(data: String): String = data.replace("=", ",").replace("+", "-").replace("/", "_")
        fun decrypt(data: String): String = data.replace(",", "=").replace("-", "+").replace("_", "/")
    }

    class TwNetwork(private val identifier: String) {
        private fun rijndael(
            data: ByteArray, key: ByteArray, iv: ByteArray, isEncryptor: Boolean,
        ): ByteArray {
            val rijndael = RijndaelEngine(192)
            val zeroBytePadding = ZeroBytePadding()
            val parametersWithIV = ParametersWithIV(KeyParameter(key), iv)
            val cipher =
                PaddedBufferedBlockCipher(CBCBlockCipher.newInstance(rijndael), zeroBytePadding)
            cipher.init(isEncryptor, parametersWithIV)
            return cipher(cipher, data)
        }

        private fun cipher(
            paddedBufferedBlockCipher: PaddedBufferedBlockCipher, bytes: ByteArray,
        ): ByteArray {
            val a = ByteArray(paddedBufferedBlockCipher.getOutputSize(bytes.size))
            val processBytes = paddedBufferedBlockCipher.processBytes(bytes, 0, bytes.size, a, 0)
            val doFinal = processBytes + paddedBufferedBlockCipher.doFinal(a, processBytes)
            val en = ByteArray(doFinal)
            System.arraycopy(a, 0, en, 0, doFinal)
            return en
        }

        fun encrypt(data: ByteArray): String = Base64.encrypt(rijndael(data, getKey(identifier), getIv(identifier), true))
        fun decrypt(data: String): ByteArray = rijndael(Base64.decrypt(data), getKey(identifier), getIv(identifier), false)
    }

    fun getKey(identifier: String): ByteArray = getMD5("`jou*$identifier)xoj'").toByteArray()

    fun getIv(identifier: String): ByteArray {
        val key = getKey(identifier).decodeToString()
        val pos = identifier.filter { it.isDigit() }.toIntOrNull() ?: 0
        val start = pos % 7
        val end = (start + 24).coerceAtMost(key.length)
        return key.substring(start, end).toByteArray()
    }

    fun getMD5(toBeHashed: String): String = MessageDigest.getInstance("MD5").digest(toBeHashed.toByteArray()).joinToString("") { "%02x".format(it) }
}