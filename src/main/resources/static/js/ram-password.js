//##### 随机密码生成器 #####

// 0-9 0-9  10-35 A-Z 36-61 a-z 62-69 特殊符号
var data = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
    "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
    "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
    "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
    "w", "x", "y", "z", "!", "@", "#", "$", "%", "^", "&", "*"];

/**
 * 生成密码
 * @param num     包含0-9(boolean)
 * @param upA     包含A-Z(boolean)
 * @param lowA    包含a-z(boolean)
 * @param special 特殊符号(boolean)
 * @param length  密码长度(number)
 * @returns {string}
 */
function make(num, upA, lowA, special, length) {
    var pwd = "";

    if(!length){
        length = 10;
    }
    switch (true) {
        //单项
        case num && !upA && !lowA && !special: //仅当数字被选中
            pwd = makePW(length, 0, 9);
            break;
        case !num && upA && !lowA && !special: //仅当大写字母被选中
            pwd = makePW(length, 10, 35);
            break;
        case !num && !upA && lowA && !special: //仅当小写字母被选中
            pwd = makePW(length, 36, 61);
            break;
        case !num && !upA && !lowA && special: //仅当符号被选中
            pwd = makePW(length, 62, 69);
            break;
        //两项
        case !num && upA && lowA && !special: //当全部字母被选中
            pwd = makePW(length, 10, 61);
            break;
        case num && upA && !lowA && !special: //当大写字母与数字被选中
            pwd = makePW(length, 0, 35);
            break;
        case num && !upA && lowA && !special: //当小写字母与数字被选中
            pwd = specialMake(length, 0, 61, 10, 35);
            break;
        case !num && !upA && lowA && special: //当小写字母与符号被选中
            pwd = makePW(length, 36, 69);
            break;
        case !num && upA && !lowA && special: //当大写字母与符号被选中
            pwd = specialMake(length, 10, 69, 36, 61);
            break;
        case num && !upA && !lowA && special: //当数字与符号被选中
            pwd = specialMake(length, 0, 69, 10, 61);
            break;
        //三项
        case num && upA && lowA && !special: //当全部字母与数字被选中
            pwd = makePW(length, 0, 61);
            break;
        case !num && upA && lowA && special: //字母加符号
            pwd = makePW(length, 10, 69);
            break;
        case num && !upA && lowA && special: //数字、小写字母、特殊符号
            pwd = specialMake(length, 0, 69, 10, 35);
            break;
        case num && upA && !lowA && special: //数字、大写字母、特殊符号
            pwd = specialMake(length, 0, 69, 36, 61);
            break;
        //四项
        case num && upA && lowA && special: //全部被选中
            pwd = makePW(length, 0, 69);
            break;
        //未选择
        default :
            alert("至少选择一项");
            break;
    }
    return pwd;
}

//对于普通情况的函数
function makePW(len, startnum, endnum) {
    var str = "";
    for (var i = 0; i <= len; i++) {
        var ram = Math.floor(Math.random() * (endnum - startnum + 1) + startnum);
        str += data[ram];
    }
    return str;
}

//特殊情况
function specialMake(len, startnum, endnum, expnum1, expnum2) {
    var str = "";
    var curlength = len;
    for (var i = 0; i <= curlength; i++) {
        var ram = Math.floor(Math.random() * (endnum - startnum + 1) + startnum);
        if (ram >= expnum1 && ram <= expnum2) {
            curlength++;
            continue;
        }
        str += data[ram];
    }
    return str;
}


