//引入js
$(document.body).append('<script src="../plugins/jsencrypt/jsencrypt.min.js"><\/script>');

/**
 * 安全工具类
 *
 * @type {{privkey: string, encrypt: (function(*=): (*|PromiseLike<ArrayBuffer>|string)), decrypt: (function(*=): (*|PromiseLike<ArrayBuffer>)), pubkey: string}}
 */
var SecurityUtil = {
    pubkey: 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVtjK92+hXigZovFixjtAdQhfCR4Vn3nKdHW66t/y/0Afi7UeRTkapE53O6LvAAs6t3szaMbILyR83DGeFPoWOttDXmrJFoaej/ZvESqWU7lnkXlMHPp0ROq9dgdFHZ2DKH7fC8xg4pQ2FzGd3JYZ5yFem4V3PZ5GAxmsOpVFyGwIDAQAB',

    /**
     * 公钥加密
     *
     * @param str
     * @returns {*|PromiseLike<ArrayBuffer>|PromiseLike<ArrayBuffer>|string}
     */
    encryptPublicKey: function (str) {
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(this.pubkey);
        return encrypt.encrypt(str);
    },

    /**
     * 公钥解密
     *
     * @param str
     * @returns {*|PromiseLike<ArrayBuffer>|PromiseLike<ArrayBuffer>}
     */
    decryptPublicKey: function (str, key) {
        var decrypt = new JSEncrypt();
        decrypt.setPrivateKey(this.pubkey);
        return decrypt.decrypt(str);
    },

}

