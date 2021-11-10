package com.util;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StringUtil {


    /**
     * 将一个字符串的首字母改为大写或小写
     * 
     * @param srcString
     *            源字符串
     * @return 改写后的新字符串
     */
    public static String capitalize(String srcString) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(srcString.charAt(0)));
        sb.append(srcString.substring(1));
        return sb.toString();
    }


    public static boolean isEmail(String email) {
        boolean isExist = false;

        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if ( b ) {
            isExist = true;
        }
        return isExist;
    }


    /**
     * 判断字符是否为空
     * 
     * @param str
     * @return boolean
     */
    public static boolean isNull(String str) {
        if ( str != null && str.length() > 0 && str.trim().length() > 0 )
            return false;
        return true;
    }


    /**
     * 判断对象是否为空
     * 
     * @param obj
     * @return boolean
     */
    public static boolean isNull(Object[] obj) {
        if ( obj != null && obj.length > 0 )
            return false;
        return true;
    }


    /**
     * 判断字符串是否为空或Null
     * 
     * @param value
     *            要判断的字符串
     * @return 如果为""或Null返回true，反之false
     */
    public static boolean isEmpty(String value) {
        return (value == null || "".equals(value) || value.trim().equals("-")) ? true : false;
    }


    /**
     * 判断字符串是否不为空
     * 
     * @param value
     *            要判断的字符串
     * @return 如果为""或Null返回false，反之true
     */
    public static boolean notEmpty(String str) {
        return str != null && str.length() > 0;
    }


    /**
     * 判断对象是否为空
     * 
     * @param value
     *            要判断的对象
     * @return 如果为Null或对象的值为""返回true，反之false
     */
    public static boolean isEmpty(Object value) {
        return (value == null || trim(value).equals("")) ? true : false;
    }


    /**
     * 判断集合是否为空
     * 
     * @param value
     *            要判断的对象
     * @return 如果为Null或对象的值为""返回true，反之false
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection value) {
        if ( value == null || value.isEmpty() ) {
            return true;
        }
        for ( Object object : value ) {
            if ( !isEmpty(object) ) {
                return false;
            }
        }
        return true;
    }


    /**
     * 删除obj数据的前后空格和回车
     * 
     * @param obj
     *            要计算的对象
     * @return 计算后的结果
     */
    public static final Object trim(Object obj) {
        if ( obj == null ) {
            return null;
        }
        if ( obj instanceof String ) {
            return ((String) obj).trim();
        } else {
            return obj;
        }
    }


    /**
     * 转换为整数
     * 
     * @param 保留999,返回-999表示不是数字
     */
    public static int isDigits(String str) {
        if ( isNull(str) )
            return -999;
        try {
            // int a=Integer.parseInt(str);
            int a = new Integer(str).intValue();
            return a;
        } catch ( NumberFormatException e ) {
            return -999;
        }
    }


    /**
     * 判断是否为数字组成的字串
     * 
     * @param validString
     *            要判断的字符串
     * @return boolen值，true或false
     */
    public static boolean isNumber(String validString) {
        if ( validString == null || validString.length() < 1 )
            return false;
        else {
            byte[] tempbyte = validString.getBytes();
            for ( int i = 0; i < validString.length(); i++ ) {
                // by=tempbyte;
                if ( (tempbyte[i] == 45) && (i == 0) ) {
                    continue;
                }
                if ( (tempbyte[i] < 48) || (tempbyte[i] > 57) )
                    return false;
            }
            return true;
        }
    }


    /**
     * 判断是否为数字及小数点组成的实数
     * 
     * @param validString
     *            要判断的字符串
     * @return boolen值，true或false
     */
    public static boolean isReal(String validString) {
        byte[] tempbyte = validString.getBytes();

        // 0~9的个数
        int a = 0;
        // 小数点的个数
        int b = 0;
        // 其他字符
        int c = 0;
        // 符号
        int d = 0;

        for ( int i = 0; i < validString.length(); i++ ) {
            if ( (tempbyte[i] == 45) && (i == 0) ) {
                d++;
                continue;
            }
            if ( (tempbyte[i] >= 48) && (tempbyte[i] <= 57) ) {
                a++;
                continue;
            }
            // 这是小数点
            if ( tempbyte[i] == 46 ) {
                b++;
                continue;
            }
            c++;
        }
        if ( c > 0 || b > 1 )
            return false;
        return true;
    }


    /**
     * 判断是否为允许有,号的货币弄字符串
     * 
     * @param validString
     *            要判断的字符串
     * @return boolen值，true或false
     */
    public static boolean isMoney(String validString) {
        if ( isNull(validString) )
            return false;
        else
            validString = validString.replaceAll(",", "");
        validString = validString.replaceAll("-", "");
        if ( isReal(validString) )
            return true;
        else
            return false;
    }


    /**
     * 判断是否符合指定长度条件
     * 
     * @param validString
     *            要判断的字符串
     * @long1 最小长度
     * @long2 最大长度
     * @return boolen值，true或false
     */
    public static boolean isLong(String validString, int long1, int long2) {
        if ( validString.length() > (long2 + 1) || validString.length() < (long1 + 1) )
            return false;
        // 字符串的长度有误
        return true;
    }


    /**
     * 判断字符串是否为只包括字母和数字
     * 
     * @param validString
     *            要判断的字符串
     * @return boolen值，true或false
     */
    public static boolean isChar(String validString) {
        byte[] tempbyte = validString.getBytes();
        for ( int i = 0; i < validString.length(); i++ ) {
            // by=tempbyte;
            if ( (tempbyte[i] < 48) || ((tempbyte[i] > 57) & (tempbyte[i] < 65)) || (tempbyte[i] > 122) || ((tempbyte[i] > 90) & (tempbyte[i] < 97)) )
                return false;
        }
        return true;
    }


    /**
     * 判断字符串是否只包括字母
     * 
     * @param validString
     *            要判断的字符串
     * @return boolen值，true或false
     */
    public static boolean isLetter(String validString) {
        byte[] tempbyte = validString.getBytes();
        for ( int i = 0; i < validString.length(); i++ ) {
            // by=tempbyte;
            if ( (tempbyte[i] < 65) || (tempbyte[i] > 122) || ((tempbyte[i] > 90) & (tempbyte[i] < 97)) )
                return false;
        }
        return true;
    }


    /**
     * 判断是否为合法的时间字符串
     * 
     * @param DATESTR
     * @return boolean
     */
    public static boolean isDate(String DATESTR) {
        boolean b = false;
        if ( !isNull(DATESTR) ) {
            try {
                b = java.text.DateFormat.getDateInstance().parse(DATESTR) != null;
            } catch ( java.text.ParseException e ) {
                b = false;
            }
        }
        return b;
    }


    /**
     * email地址是否合法
     * 
     * @param smail
     * @return boolean
     */
    public static boolean isMail(String smail) {
        boolean b = false;
        if ( smail == null || smail.length() < 5 ) {
        } else {
            int point = smail.indexOf("@");
            if ( point < 1 )
                return false;
            String temp1 = smail.substring(0, point);

            String temp2 = smail.substring(point + 1);
            if ( temp1.indexOf(".") == -1 ) {
                b = true;
            }
            if ( temp2.indexOf(".") == -1 ) {
                b = false;
            } else {
                // System.out.println(temp2.length() - temp2.lastIndexOf("."));
                if ( temp2.length() - temp2.lastIndexOf(".") > 5 ) {
                    b = false;
                }
            }
        }
        return b;
    }


    /**
     * 判断字符串是否含有中文
     * 
     * @param validString
     *            要判断的字符串
     * @return boolen值，true或false
     */
    public static boolean isCompriseGBK(String str) {
        // str = "中国chinese";
        str = getstr(str);
        boolean temp = false;
        for ( int i = 0; i < str.length(); i++ ) {
            if ( str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+") ) {
                temp = true;
                break;
            }
        }

        return temp;
    }


    /**
     * 是否为合法的电话号码
     * 
     * @param num
     * @return boolean
     */
    public static boolean isPhoneNumber(String num) {
        return Pattern.matches("(\\(\\d{3}\\)|\\d{3,4}-)?\\d{7,8}$", num);
    }


    /**
     * 是否为合法的手机号码
     * 
     * @param num
     * @return boolean
     */
    public static boolean isMobileNumber(String num) {
        return Pattern.matches("0?13\\d{9}", num);
    }


    /**
     * 是否为合法的邮编
     * 
     * @param num
     * @return boolean
     */
    public static boolean isZipCodeNumber(String num) {
        return Pattern.matches("\\d{6}", num);
    }


    /*
     * 2005新写字符判断函数
     */

    /**
     * @author Jacky wu Date 2005-1-29
     * @param 取文件扩展名
     * @param String
     *            fileName 文件名全名
     * @return String 文件扩展名
     */
    public static String getFileName(String fileName) {
        String tempName = "";
        if ( fileName != null ) {
            // 取文件名中最后一�?的位�?
            int pos = fileName.lastIndexOf(".");
            // 取pos后的字符�?
            tempName = fileName.substring(pos + 1, fileName.length());
        }
        return tempName;
    }


    /**
     * @author Jacky wu Date 2005-1-29
     * @param  文件类型
     * @param String
     *            fileName 要检测文件名
     * @param String[]
     *            files 允许的文件后缀
     * @return boolean True表示为允许文件类型
     */
    public static boolean isFileType(String fileName, String[] files) {
        boolean b = false;
        if ( fileName != null && files.length > 0 ) {
            String tempName = getFileName(fileName);
            // System.out.println(tempName);
            for ( int i = 0; i < files.length; i++ ) {
                if ( files.equals(tempName) ) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }


    /**
     * 将表单中文或者浏览中文转换为GBK格式
     * 
     * @param str
     * @return String
     */
    public static String getstr(String str) {
        try {
            String temp_p = str.trim();
            byte[] temp_t = temp_p.getBytes("ISO8859-1");
            String temp = new String(temp_t);
            return temp;
        } catch ( Exception e ) {
            return "";
        }
    }


    /**
     * 将数据库中文转换为GBK格式
     * 
     * @param str
     * @return String
     */
    public static String getDBstr(String str) {
        try {
            String temp_p = str;
            String temp = new String(temp_p.getBytes("GBK"), "ISO8859_1");
            return temp;
        } catch ( Exception e ) {
            return "";
        }
    }


    /**
     * 从数据库中得到字符串用于在网页控件中还原显示
     * 
     * @param str
     * @return String
     */
    public static String getSafeStrToHtml(String str) {
        try {
            // String theStr = this.getDBstr(str);
            String theStr = str;
            theStr = theStr.replaceAll("&#39", "'");
            theStr = theStr.replaceAll("&lt;", "<");
            theStr = theStr.replaceAll("&gt;", ">");
            theStr = theStr.replaceAll("<br>", "\n");
            // theStr = theStr.replaceAll("&nbsp", " ");
            return theStr;
        } catch ( Exception e ) {
            return "";
        }
    }


    /**
     * 从多行文本框内容转换为Sql安全字符串
     * 
     * @param str
     * @return String
     */
    public static String getSafeStrToDB(String str) {
        try {
            // String theStr = this.getDBstr(str);
            String theStr = str;
            theStr = theStr.replaceAll("'", "&#39");
            theStr = theStr.replaceAll("<", "&lt;");
            theStr = theStr.replaceAll(">", "&gt;");
            theStr = theStr.replaceAll("\n", "<br>");
            // theStr = theStr.replaceAll(" ", "&nbsp");
            // theStr = theStr.replaceAll("; ;", " ");
            return theStr;
        } catch ( Exception e ) {
            return "";
        }
    }


    /**
     * 从多行文本框内容转换为Sql安全字符串同时将中文的编码转换为GBK格式
     * 
     * @param str
     * @return
     */
    public static String getSafeStrToDBAndGBK(String str) {
        str = getstr(str);
        str = getSafeStrToDB(str);
        return str;
    }


    /**
     * 获取IP地址前三段
     * 
     * @param ip
     * @return String
     */
    public static String getPart(String ip) {
        int pos = ip.lastIndexOf(".");
        ip = ip.substring(0, pos) + ".*";
        return ip;
    }


    /**
     * 获取字符串的绝对长度(如果遇到汉字字符则算两个)
     * 
     * @param s
     *            传入的字符
     * @param return
     *            字串的绝对长度
     */
    public static int absoluteLength(String s) {
        if ( s == null )
            return 0;
        try {
            return new String(s.getBytes("GB2312"), "ISO8859_1").length();
        } catch ( Exception e ) {
            return s.length();
        }
    }


    /**
     * 将输入的数组转换为字符串
     * 
     * @param array
     *            传入数组
     * @param f
     *            分割符
     * @return String
     */
    public static String spliteToString(String[] array, String f) {
        String strs = new String();
        for ( int i = 0; i < array.length; i++ ) {
            // System.out.println(array);
            if ( i < array.length - 1 ) {
                strs += array + f;
            } else {
                strs += array;
            }
        }
        return strs;
    }


    /**
     * 对一个字符串的绝对长度进行拆分如果遇到汉字字符会把它当作两个字符处理
     * 
     * @param s
     *            传入的字符
     * @param start
     *            起始绝对位置
     * @param end
     *            终止绝对位置
     * @param endStr
     *            尾部省略字符,...",不需要则留空
     * @return 返回的字符
     */
    public static String absoluteSubstring(String s, int start, int end, String endStr) {

        if ( s == null )
            return null;
        try {
            String s2 = new String(s.getBytes("GB2312"), "ISO8859_1");
            String e2 = new String(endStr.getBytes("GB2312"), "ISO8859_1");

            if ( end >= s2.length() ) {
                end = s2.length();
                endStr = "";
            } else if ( !"".equals(e2) && e2 != null ) {
                int num = end - start; // 定义�?��的长�?
                end = num - e2.length();
            }

            s2 = s2.substring(start, end);
            return new String(s2.getBytes("ISO8859_1"), "GB2312") + endStr;
        } catch ( Exception e ) {
            return s.substring(start, end);
        }
    }


    /**
     * 扩充字串，使其绝对长度为指定的长度，如果过长就截断，过短就补充指定的字串
     * 
     * @param str
     *            传入的字符
     * @param updateStr
     *            填充的字符
     * @param num
     *            指定的长度
     * @param flag
     *            填补字符串的位置：true的话在前面填补?false在后面填?
     * @return 返回的字符
     */
    public static String updateAbsoluteLength(String str, String updateStr, int num, boolean flag) {
        if ( updateStr == null )
            return str;
        if ( str == null ) {
            str = "";
            for ( int i = 0; i < num; i++ ) {
                str += updateStr;
            }
            return str;
        }
        if ( absoluteLength(str) == num )
            return str;
        else if ( absoluteLength(str) < num ) {
            for ( int i = absoluteLength(str); i < num; i++ ) {
                if ( flag ) {
                    str = updateStr + str;
                } else {
                    str = str + updateStr;
                }
            }
            return str;
        } else
            return absoluteSubstring(str, 0, absoluteLength(str) - num, "");
    }

    public static Long[] strToLongArray(String str, String regex) {
        String[] strArray = str.split(regex);
        Long[] longs = new Long[strArray.length];
        for ( int i = 0; i < strArray.length; i++ ) {
            longs[i] = Long.parseLong(strArray[i]);
        }
        return longs;
    }

    public static List<Long> strToLongArrayList(String str, String regex) {
        List<Long> list = new ArrayList<Long>();
        String[] strArray = str.split(regex);
        for ( int i = 0; i < strArray.length; i++ ) {
            list.add(Long.parseLong(strArray[i]));
        }
        return list;
    }
    

    public static String subString(String text, int length, String endStr) {

        int textLength = text.length();
        if ( textLength <= length ) {
            return text;
        }
        return text.substring(0, length) + endStr;
    }


    /** 
     * the traditional io way  
     * @param filename 
     * @return 
     * @throws IOException 
     */
    public static byte[] toByteArray(File f)
        throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while ( -1 != (len = in.read(buffer, 0, buf_size)) ) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch ( IOException e ) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            bos.close();
        }
    }


    /**
     * 过滤特殊字符
     * 
     * @param str 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static String filterSpecialCharacters(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 集合转字符串
     *  "aa,bb"
     * 
     * @param col
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String convertToString(Collection col) {
        if ( isEmpty(col) ) {
            return Constants.EMPTY_STRING;
        }
        String detailIdList = col.toString();
        detailIdList = detailIdList.replaceAll("\\[", "");
        detailIdList = detailIdList.replaceAll("\\]", "");

        return detailIdList;
    }


    public static Long convertToLong(String value) {
        if ( isEmpty(value) ) {
            return null;
        }
        try {

            return Long.valueOf(value);
        } catch ( Exception e ) {
            return null;
        }
    }


    public static String convertToString(Long value) {
        if ( isEmpty(value) ) {
            return Constants.EMPTY_STRING;
        }
        try {

            return String.valueOf(value);
        } catch ( Exception e ) {
            return Constants.EMPTY_STRING;
        }
    }


    /**
     * 集合转字符串
     * "'aa','bb'"
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String convertToCharString(Collection col) {
        if ( isEmpty(col) ) {
            return Constants.EMPTY_STRING;
        }
        StringBuffer sb = new StringBuffer();
        for ( Object str : col ) {
            sb.append("'");
            sb.append(str);
            sb.append("'");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }


    /**
     * 格式化Double
     * 精度两位
     * 
     * @param d
     * @return
     */
    public static String formatDouble(Double d) {
        if ( isEmpty(d) ) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(d);
    }
    
    /**
     * 格式化Double
     * 精度4位
     * 
     * @param d
     * @return
     */
    public static String formatDoubleFor4Bit(Double d) {
        if ( isEmpty(d) ) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0.####");
        return df.format(d);
    }


    public static Long paseUseCodeId(String userCodeId) {
        Long userId = Long.valueOf(-1);
        try {
            userId = Long.valueOf(userCodeId);
        } catch ( Exception e ) {
        }

        return userId;
    }

}
