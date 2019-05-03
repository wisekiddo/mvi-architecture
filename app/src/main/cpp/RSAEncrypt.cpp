#include <jni.h>
#include <string>
#include <stdio.h>
#include <android/log.h>
#include "openssl/md5.h"
#include "openssl/rsa.h"
#include "openssl/pem.h"

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, "rsaEncrypt",__VA_ARGS__)

extern "C" {

RSA *createRSA(unsigned char *key, int pub) {
    RSA *rsa = NULL;
    BIO *keybio;
    keybio = BIO_new_mem_buf(key, -1);
    if (keybio == NULL) {
        printf("Failed to create key BIO");
        return 0;
    }
    if (pub) {
        rsa = PEM_read_bio_RSA_PUBKEY(keybio, &rsa, NULL, NULL);
    } else {
        rsa = PEM_read_bio_RSAPrivateKey(keybio, &rsa, NULL, NULL);
    }
    if (rsa == NULL) {
        printf("Failed to create RSA");
    }

    return rsa;
}

char encoding_table[64] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                           'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                           'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                           'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                           'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                           'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                           'w', 'x', 'y', 'z', '0', '1', '2', '3',
                           '4', '5', '6', '7', '8', '9', '+', '/'};

/***
 * Base 64 conversion
 * @param env
 * @param encrypted
 * @param encrypted_length
 * @return
 */
jcharArray base64_encode(JNIEnv *env, unsigned char *encrypted, int encrypted_length) {

    int mod_table[] = {0, 2, 1};

    size_t output_length = 4 * ((encrypted_length + 2) / 3);

    //std::string encoded_data[output_length];
    char encoded_data[output_length];
    //if (encoded_data == NULL) return NULL;

    for (int i = 0, j = 0; i < encrypted_length;) {

        uint32_t octet_a = i < encrypted_length ? encrypted[i++] : 0;
        uint32_t octet_b = i < encrypted_length ? encrypted[i++] : 0;
        uint32_t octet_c = i < encrypted_length ? encrypted[i++] : 0;

        uint32_t triple = (octet_a << 0x10) + (octet_b << 0x08) + octet_c;

        if (output_length > j)
            encoded_data[j++] = encoding_table[(triple >> 3 * 6) & 0x3F];
        if (output_length > j)
            encoded_data[j++] = encoding_table[(triple >> 2 * 6) & 0x3F];
        if (output_length > j)
            encoded_data[j++] = encoding_table[(triple >> 1 * 6) & 0x3F];
        if (output_length > j)
            encoded_data[j++] = encoding_table[(triple >> 0 * 6) & 0x3F];
    }

    for (int i = 0; i < mod_table[encrypted_length % 3]; i++)
        encoded_data[output_length - 1 - i] = '=';

    jcharArray jcharArray1 = env->NewCharArray(output_length);
    jchar *j_version = (jchar *) calloc(sizeof(jchar), output_length);
    for (int i = 0; i <= output_length; i++) {
        j_version[i] = (jchar) encoded_data[i];
    }
    env->SetCharArrayRegion(jcharArray1, 0, output_length, j_version);
    return jcharArray1;
}

/***
 * base64 decode
 * @param data
 * @param input_length
 * @param output_length
 * @return
 */

unsigned char *base64_decode(const char *data,
                             size_t input_length, size_t *output_length) {
    char decoding_table[256];

    for (int i = 0; i < 64; i++)
        decoding_table[(unsigned char) encoding_table[i]] = i;

    if (input_length % 4 != 0) return NULL;

    *output_length = input_length / 4 * 3;
    if (data[input_length - 1] == '=') (*output_length)--;
    if (data[input_length - 2] == '=') (*output_length)--;

    unsigned char *decoded_data = (unsigned char *) (calloc(sizeof(unsigned char), *output_length));

    for (int i = 0, j = 0; i < input_length;) {

        uint32_t sextet_a = data[i] == '=' ? 0 & i++ : decoding_table[data[i++]];
        uint32_t sextet_b = data[i] == '=' ? 0 & i++ : decoding_table[data[i++]];
        uint32_t sextet_c = data[i] == '=' ? 0 & i++ : decoding_table[data[i++]];
        uint32_t sextet_d = data[i] == '=' ? 0 & i++ : decoding_table[data[i++]];

        uint32_t triple = (sextet_a << 3 * 6)
                          + (sextet_b << 2 * 6)
                          + (sextet_c << 1 * 6)
                          + (sextet_d << 0 * 6);

        if (j < *output_length) decoded_data[j++] = (triple >> 2 * 8) & 0xFF;
        if (j < *output_length) decoded_data[j++] = (triple >> 1 * 8) & 0xFF;
        if (j < *output_length) decoded_data[j++] = (triple >> 0 * 8) & 0xFF;
    }

    return decoded_data;
}

JNIEXPORT jcharArray JNICALL
Java_com_wisekiddo_feature_main_MainActivity_encrypt(
        JNIEnv *env,
        jobject /* this */, jstring string) {
    unsigned char publicKey[] = "-----BEGIN PUBLIC KEY-----\n"\
    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDuto6F7SI2Ykgt5a/6/vWqLYYL\n"\
    "BbC+lBKjEnNanoaM1M6xIzZJ+KM6RxDzyj03MhQ0C2oIFnOPhfYwAoLC3X2hnRjv\n"\
    "5BEt7msL1gD5IcPZ3/RpLzT5XhaDQ0P+dxwR+bSWEFKHbCh2EcktQtiA7CG0U/21\n"\
    "Accxu7Au9pM2nVsjAQIDAQAB\n"\
    "-----END PUBLIC KEY-----";

    unsigned char encrypted[4098] = {};

    const char *plain = env->GetStringUTFChars(string, 0);
    int fl = strlen(plain);

    //encryption
    RSA *rsa = createRSA(publicKey, 1);
    int encrypted_length = RSA_public_encrypt(fl, (unsigned char *) plain, encrypted, rsa,
                                              RSA_PKCS1_PADDING);

    return base64_encode(env, encrypted, encrypted_length);
}

JNIEXPORT jcharArray JNICALL
Java_com_wisekiddo_feature_main_MainActivity_decrypt(
        JNIEnv *env,
        jobject /* this */, jstring string) {
    unsigned char privateKey[] = "-----BEGIN RSA PRIVATE KEY-----\n"\
    "MIICXAIBAAKBgQDuto6F7SI2Ykgt5a/6/vWqLYYLBbC+lBKjEnNanoaM1M6xIzZJ\n"\
    "+KM6RxDzyj03MhQ0C2oIFnOPhfYwAoLC3X2hnRjv5BEt7msL1gD5IcPZ3/RpLzT5\n"\
    "XhaDQ0P+dxwR+bSWEFKHbCh2EcktQtiA7CG0U/21Accxu7Au9pM2nVsjAQIDAQAB\n"\
    "AoGAAwfOR2T7SjV1NR/1W/pgpAhv6zPnSoQWuHtDe6MHkkIhXZ5U0huV9udwD6hB\n"\
    "nrHED+UVM4UVcn0A4xslDdRCBATzIb/Ry2HK8LhYuMtT96BQFKSH83Cn73ndJfGQ\n"\
    "LGTSlwO1ARliodUpT7+cz+Dl5sw+hVSh18yZ4q7ue0MU/AECQQD974LoRgHYPbp1\n"\
    "4pNx23s8pmGukMRSLX5NtUr/EFglRXens6sQPjPqrDyHGjifZh4v4H2hYhOBOfxS\n"\
    "9bmSfqZ1AkEA8KddTmVDmSefIiL4IiKJoO73UHVyZfLTLL/9tinkmat6AnX/UOu6\n"\
    "1Ooy6Rv6PC9fU7VY3jAP8BjhKC4LVKGw3QJAEUnzUbkiewSI42nPvBrDYze4wtfc\n"\
    "ZfPu+39yYbdNKQx3vxU+elah1kJvxIsGe+PJ/lx8cTZqOYwnH8bXIaA52QJAYkRl\n"\
    "NGLjRIHKWD0hR4cE3gNWV7NtoDWUh5xqNF1M7Hb53lFijR+E7fYoDcoP102cxmix\n"\
    "viCwEWGHZ0nN9KQFvQJBAL4hq5iNYct9KrpZzs+WAu/26wLDNoWPSsMPm6cIc2Lh\n"\
    "ZuhdEXPze1mEN9wTeNEh+6VSTs8YpEc+xnhPWpI0D60=\n"\
    "-----END RSA PRIVATE KEY-----";
    unsigned char decrypted[4098] = {};

    const char *enc = env->GetStringUTFChars(string, 0);
    int fl = strlen(enc);
    size_t output_length;

    unsigned char *dec = base64_decode(enc, fl, &output_length);

    //decryption
    RSA *rsa = createRSA(privateKey, 0);
    int decrypted_length = RSA_private_decrypt(output_length, dec, decrypted, rsa,
                                               RSA_PKCS1_PADDING);


    jcharArray jcharArray1 = env->NewCharArray(decrypted_length);
    jchar *j_version = (jchar *) calloc(sizeof(jchar), decrypted_length);
    for (int i = 0; i <= decrypted_length; i++) {
        j_version[i] = (jchar) decrypted[i];
    }
    env->SetCharArrayRegion(jcharArray1, 0, decrypted_length, j_version);
    return jcharArray1;
}
}