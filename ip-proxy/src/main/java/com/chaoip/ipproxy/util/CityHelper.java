package com.chaoip.ipproxy.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public final class CityHelper {
    private static Map<String, String[]> citys = new HashMap<>();

    static {
        citys.put("010", new String[]{"北京市", "北京市"});
        citys.put("021", new String[]{"上海市", "上海市"});
        citys.put("022", new String[]{"天津市", "天津市"});
        citys.put("023", new String[]{"重庆市", "重庆市"});
        citys.put("852", new String[]{"香港", "香港"});
        citys.put("853", new String[]{"澳门", "澳门"});

        citys.put("0310", new String[]{"河北省邯郸市", "河北"});
        citys.put("0311", new String[]{"河北省石家庄", "河北"});
        citys.put("0312", new String[]{"河北省保定市", "河北"});
        citys.put("0313", new String[]{"河北省张家口", "河北"});
        citys.put("0314", new String[]{"河北省承德市", "河北"});
        citys.put("0315", new String[]{"河北省唐山市", "河北"});
        citys.put("0316", new String[]{"河北省廊坊市", "河北"});
        citys.put("0317", new String[]{"河北省沧州市", "河北"});
        citys.put("0318", new String[]{"河北省衡水市", "河北"});
        citys.put("0319", new String[]{"河北省邢台市", "河北"});
        citys.put("0335", new String[]{"河北省秦皇岛", "河北"});

        citys.put("0570", new String[]{"浙江省衢州市", "浙江"});
        citys.put("0571", new String[]{"浙江省杭州市", "浙江"});
        citys.put("0572", new String[]{"浙江省湖州市", "浙江"});
        citys.put("0573", new String[]{"浙江省嘉兴市", "浙江"});
        citys.put("0574", new String[]{"浙江省宁波市", "浙江"});
        citys.put("0575", new String[]{"浙江省绍兴市", "浙江"});
        citys.put("0576", new String[]{"浙江省台州市", "浙江"});
        citys.put("0577", new String[]{"浙江省温州市", "浙江"});
        citys.put("0578", new String[]{"浙江省丽水市", "浙江"});
        citys.put("0579", new String[]{"浙江省金华市", "浙江"});
        citys.put("0580", new String[]{"浙江省舟山市", "浙江"});

        citys.put("024", new String[]{"辽宁省沈阳市", "辽宁"});
        citys.put("0410", new String[]{"辽宁省铁岭市", "辽宁"});
        citys.put("0411", new String[]{"辽宁省大连市", "辽宁"});
        citys.put("0412", new String[]{"辽宁省鞍山市", "辽宁"});
        citys.put("0413", new String[]{"辽宁省抚顺市", "辽宁"});
        citys.put("0414", new String[]{"辽宁省本溪市", "辽宁"});
        citys.put("0415", new String[]{"辽宁省丹东市", "辽宁"});
        citys.put("0416", new String[]{"辽宁省锦州市", "辽宁"});
        citys.put("0417", new String[]{"辽宁省营口市", "辽宁"});
        citys.put("0418", new String[]{"辽宁省阜新市", "辽宁"});
        citys.put("0419", new String[]{"辽宁省辽阳市", "辽宁"});
        citys.put("0421", new String[]{"辽宁省朝阳市", "辽宁"});
        citys.put("0427", new String[]{"辽宁省盘锦市", "辽宁"});
        citys.put("0429", new String[]{"辽宁省葫芦岛", "辽宁"});

        citys.put("027", new String[]{"湖北省武汉市", "湖北"});
        citys.put("0710", new String[]{"湖北省襄城市", "湖北"});
        citys.put("0711", new String[]{"湖北省鄂州市", "湖北"});
        citys.put("0712", new String[]{"湖北省孝感市", "湖北"});
        citys.put("0713", new String[]{"湖北省黄州市", "湖北"});
        citys.put("0714", new String[]{"湖北省黄石市", "湖北"});
        citys.put("0715", new String[]{"湖北省咸宁市", "湖北"});
        citys.put("0716", new String[]{"湖北省荆沙市", "湖北"});
        citys.put("0717", new String[]{"湖北省宜昌市", "湖北"});
        citys.put("0718", new String[]{"湖北省恩施市", "湖北"});
        citys.put("0719", new String[]{"湖北省十堰市", "湖北"});
        citys.put("0722", new String[]{"湖北省随枣市", "湖北"});
        citys.put("0724", new String[]{"湖北省荆门市", "湖北"});
        citys.put("0728", new String[]{"湖北省江汉市", "湖北"});

        citys.put("025", new String[]{"江苏省南京市", "江苏"});
        citys.put("0510", new String[]{"江苏省无锡市", "江苏"});
        citys.put("0511", new String[]{"江苏省镇江市", "江苏"});
        citys.put("0512", new String[]{"江苏省苏州市", "江苏"});
        citys.put("0513", new String[]{"江苏省南通市", "江苏"});
        citys.put("0514", new String[]{"江苏省扬州市", "江苏"});
        citys.put("0515", new String[]{"江苏省盐城市", "江苏"});
        citys.put("0516", new String[]{"江苏省徐州市", "江苏"});
// citys.put("0517", new String[]{"江苏省淮阴市","江苏"});
        citys.put("0517", new String[]{"江苏省淮安市", "江苏"});
        citys.put("0518", new String[]{"江苏省连云港", "江苏"});
        citys.put("0519", new String[]{"江苏省常州市", "江苏"});
        citys.put("0523", new String[]{"江苏省泰州市", "江苏"});

        citys.put("0470", new String[]{"内蒙古海拉尔", "内蒙古"});
        citys.put("0471", new String[]{"内蒙古呼和浩特", "内蒙古"});
        citys.put("0472", new String[]{"内蒙古包头市", "内蒙古"});
        citys.put("0473", new String[]{"内蒙古乌海市", "内蒙古"});
        citys.put("0474", new String[]{"内蒙古集宁市", "内蒙古"});
        citys.put("0475", new String[]{"内蒙古通辽市", "内蒙古"});
        citys.put("0476", new String[]{"内蒙古赤峰市", "内蒙古"});
        citys.put("0477", new String[]{"内蒙古东胜市", "内蒙古"});
        citys.put("0478", new String[]{"内蒙古临河市", "内蒙古"});
        citys.put("0479", new String[]{"内蒙古锡林浩特", "内蒙古"});
        citys.put("0482", new String[]{"内蒙古乌兰浩特", "内蒙古"});
        citys.put("0483", new String[]{"内蒙古阿拉善左旗", "内蒙古"});

        citys.put("0790", new String[]{"江西省新余市", "江西"});
        citys.put("0791", new String[]{"江西省南昌市", "江西"});
        citys.put("0792", new String[]{"江西省九江市", "江西"});
        citys.put("0793", new String[]{"江西省上饶市", "江西"});
        citys.put("0794", new String[]{"江西省临川市", "江西"});
        citys.put("0795", new String[]{"江西省宜春市", "江西"});
        citys.put("0796", new String[]{"江西省吉安市", "江西"});
        citys.put("0797", new String[]{"江西省赣州市", "江西"});
        citys.put("0798", new String[]{"江西省景德镇", "江西"});
        citys.put("0799", new String[]{"江西省萍乡市", "江西"});
        citys.put("0701", new String[]{"江西省鹰潭市", "江西"});

        citys.put("0350", new String[]{"山西省忻州市", "山西"});
        citys.put("0351", new String[]{"山西省太原市", "山西"});
        citys.put("0352", new String[]{"山西省大同市", "山西"});
        citys.put("0353", new String[]{"山西省阳泉市", "山西"});
        citys.put("0354", new String[]{"山西省榆次市", "山西"});
        citys.put("0355", new String[]{"山西省长治市", "山西"});
        citys.put("0356", new String[]{"山西省晋城市", "山西"});
        citys.put("0357", new String[]{"山西省临汾市", "山西"});
        citys.put("0358", new String[]{"山西省离石市", "山西"});
        citys.put("0359", new String[]{"山西省运城市", "山西"});

        citys.put("0930", new String[]{"甘肃省临夏市", "甘肃"});
        citys.put("0931", new String[]{"甘肃省兰州市", "甘肃"});
        citys.put("0932", new String[]{"甘肃省定西市", "甘肃"});
        citys.put("0933", new String[]{"甘肃省平凉市", "甘肃"});
        citys.put("0934", new String[]{"甘肃省西峰市", "甘肃"});
        citys.put("0935", new String[]{"甘肃省武威市", "甘肃"});
        citys.put("0936", new String[]{"甘肃省张掖市", "甘肃"});
        citys.put("0937", new String[]{"甘肃省酒泉市", "甘肃"});
        citys.put("0938", new String[]{"甘肃省天水市", "甘肃"});
        citys.put("0941", new String[]{"甘肃省甘南州", "甘肃"});
        citys.put("0943", new String[]{"甘肃省白银市", "甘肃"});

        citys.put("0530", new String[]{"山东省菏泽市", "山东"});
        citys.put("0531", new String[]{"山东省济南市", "山东"});
        citys.put("0532", new String[]{"山东省青岛市", "山东"});
        citys.put("0533", new String[]{"山东省淄博市", "山东"});
        citys.put("0534", new String[]{"山东省德州市", "山东"});
        citys.put("0535", new String[]{"山东省烟台市", "山东"});
        citys.put("0536", new String[]{"山东省淮坊市", "山东"});
        citys.put("0537", new String[]{"山东省济宁市", "山东"});
        citys.put("0538", new String[]{"山东省泰安市", "山东"});
        citys.put("0539", new String[]{"山东省临沂市", "山东"});

        citys.put("0450", new String[]{"黑龙江阿城市", "黑龙江"});
        citys.put("0451", new String[]{"黑龙江哈尔滨", "黑龙江"});
        citys.put("0452", new String[]{"黑龙江齐齐哈尔", "黑龙江"});
        citys.put("0453", new String[]{"黑龙江牡丹江", "黑龙江"});
        citys.put("0454", new String[]{"黑龙江佳木斯", "黑龙江"});
        citys.put("0455", new String[]{"黑龙江绥化市", "黑龙江"});
        citys.put("0456", new String[]{"黑龙江黑河市", "黑龙江"});
        citys.put("0457", new String[]{"黑龙江加格达奇", "黑龙江"});
        citys.put("0458", new String[]{"黑龙江伊春市", "黑龙江"});
        citys.put("0459", new String[]{"黑龙江大庆市", "黑龙江"});

        citys.put("0591", new String[]{"福建省福州市", "福建"});
        citys.put("0592", new String[]{"福建省厦门市", "福建"});
        citys.put("0593", new String[]{"福建省宁德市", "福建"});
        citys.put("0594", new String[]{"福建省莆田市", "福建"});
        citys.put("0595", new String[]{"福建省泉州市", "福建"});
// citys.put("0595", new String[]{"福建省晋江市","福建"});
        citys.put("0596", new String[]{"福建省漳州市", "福建"});
        citys.put("0597", new String[]{"福建省龙岩市", "福建"});
        citys.put("0598", new String[]{"福建省三明市", "福建"});
        citys.put("0599", new String[]{"福建省南平市", "福建"});

        citys.put("020", new String[]{"广东省广州市", "广东"});
        citys.put("0751", new String[]{"广东省韶关市", "广东"});
        citys.put("0752", new String[]{"广东省惠州市", "广东"});
        citys.put("0753", new String[]{"广东省梅州市", "广东"});
        citys.put("0754", new String[]{"广东省汕头市", "广东"});
        citys.put("0755", new String[]{"广东省深圳市", "广东"});
        citys.put("0756", new String[]{"广东省珠海市", "广东"});
        citys.put("0757", new String[]{"广东省佛山市", "广东"});
        citys.put("0758", new String[]{"广东省肇庆市", "广东"});
        citys.put("0759", new String[]{"广东省湛江市", "广东"});
        citys.put("0760", new String[]{"广东省中山市", "广东"});
        citys.put("0762", new String[]{"广东省河源市", "广东"});
        citys.put("0763", new String[]{"广东省清远市", "广东"});
        citys.put("0765", new String[]{"广东省顺德市", "广东"});
        citys.put("0766", new String[]{"广东省云浮市", "广东"});
        citys.put("0768", new String[]{"广东省潮州市", "广东"});
        citys.put("0769", new String[]{"广东省东莞市", "广东"});
        citys.put("0660", new String[]{"广东省汕尾市", "广东"});
        citys.put("0661", new String[]{"广东省潮阳市", "广东"});
        citys.put("0662", new String[]{"广东省阳江市", "广东"});
        citys.put("0663", new String[]{"广东省揭西市", "广东"});

        citys.put("028", new String[]{"四川省成都市", "四川"});
        citys.put("0810", new String[]{"四川省涪陵市", "四川"});
        citys.put("0811", new String[]{"四川省重庆市", "四川"});
        citys.put("0812", new String[]{"四川省攀枝花", "四川"});
        citys.put("0813", new String[]{"四川省自贡市", "四川"});
        citys.put("0814", new String[]{"四川省永川市", "四川"});
        citys.put("0816", new String[]{"四川省绵阳市", "四川"});
        citys.put("0817", new String[]{"四川省南充市", "四川"});
        citys.put("0818", new String[]{"四川省达县市", "四川"});
        citys.put("0819", new String[]{"四川省万县市", "四川"});
        citys.put("0825", new String[]{"四川省遂宁市", "四川"});
        citys.put("0826", new String[]{"四川省广安市", "四川"});
        citys.put("0827", new String[]{"四川省巴中市", "四川"});
        citys.put("0830", new String[]{"四川省泸州市", "四川"});
        citys.put("0831", new String[]{"四川省宜宾市", "四川"});
        citys.put("0832", new String[]{"四川省内江市", "四川"});
        citys.put("0833", new String[]{"四川省乐山市", "四川"});
        citys.put("0834", new String[]{"四川省西昌市", "四川"});
        citys.put("0835", new String[]{"四川省雅安市", "四川"});
        citys.put("0836", new String[]{"四川省康定市", "四川"});
        citys.put("0837", new String[]{"四川省马尔康", "四川"});
        citys.put("0838", new String[]{"四川省德阳市", "四川"});
        citys.put("0839", new String[]{"四川省广元市", "四川"});
        citys.put("0840", new String[]{"四川省泸州市", "四川"});

        citys.put("0730", new String[]{"湖南省岳阳市", "湖南"});
        citys.put("0731", new String[]{"湖南省长沙市", "湖南"});
        citys.put("0732", new String[]{"湖南省湘潭市", "湖南"});
        citys.put("0733", new String[]{"湖南省株州市", "湖南"});
        citys.put("0734", new String[]{"湖南省衡阳市", "湖南"});
        citys.put("0735", new String[]{"湖南省郴州市", "湖南"});
        citys.put("0736", new String[]{"湖南省常德市", "湖南"});
        citys.put("0737", new String[]{"湖南省益阳市", "湖南"});
        citys.put("0738", new String[]{"湖南省娄底市", "湖南"});
        citys.put("0739", new String[]{"湖南省邵阳市", "湖南"});
        citys.put("0743", new String[]{"湖南省吉首市", "湖南"});
        citys.put("0744", new String[]{"湖南省张家界", "湖南"});
        citys.put("0745", new String[]{"湖南省怀化市", "湖南"});
        citys.put("0746", new String[]{"湖南省永州冷", "湖南"});

        citys.put("0370", new String[]{"河南省商丘市", "河南"});
        citys.put("0371", new String[]{"河南省郑州市", "河南"});
        citys.put("0372", new String[]{"河南省安阳市", "河南"});
        citys.put("0373", new String[]{"河南省新乡市", "河南"});
        citys.put("0374", new String[]{"河南省许昌市", "河南"});
        citys.put("0375", new String[]{"河南省平顶山", "河南"});
        citys.put("0376", new String[]{"河南省信阳市", "河南"});
        citys.put("0377", new String[]{"河南省南阳市", "河南"});
        citys.put("0378", new String[]{"河南省开封市", "河南"});
        citys.put("0379", new String[]{"河南省洛阳市", "河南"});
        citys.put("0391", new String[]{"河南省焦作市", "河南"});
        citys.put("0392", new String[]{"河南省鹤壁市", "河南"});
        citys.put("0393", new String[]{"河南省濮阳市", "河南"});
        citys.put("0394", new String[]{"河南省周口市", "河南"});
        citys.put("0395", new String[]{"河南省漯河市", "河南"});
        citys.put("0396", new String[]{"河南省驻马店", "河南"});
        citys.put("0398", new String[]{"河南省三门峡", "河南"});

        citys.put("0870", new String[]{"云南省昭通市", "云南"});
        citys.put("0871", new String[]{"云南省昆明市", "云南"});
        citys.put("0872", new String[]{"云南省大理市", "云南"});
        citys.put("0873", new String[]{"云南省个旧市", "云南"});
        citys.put("0874", new String[]{"云南省曲靖市", "云南"});
        citys.put("0875", new String[]{"云南省保山市", "云南"});
        citys.put("0876", new String[]{"云南省文山市", "云南"});
        citys.put("0877", new String[]{"云南省玉溪市", "云南"});
        citys.put("0878", new String[]{"云南省楚雄市", "云南"});
        citys.put("0879", new String[]{"云南省思茅市", "云南"});
        citys.put("0691", new String[]{"云南省景洪市", "云南"});
        citys.put("0692", new String[]{"云南省潞西市", "云南"});
        citys.put("0881", new String[]{"云南省东川市", "云南"});
        citys.put("0883", new String[]{"云南省临沧市", "云南"});
        citys.put("0886", new String[]{"云南省六库市", "云南"});
        citys.put("0887", new String[]{"云南省中甸市", "云南"});
        citys.put("0888", new String[]{"云南省丽江市", "云南"});

        citys.put("0550", new String[]{"安徽省滁州市", "安徽"});
        citys.put("0551", new String[]{"安徽省合肥市", "安徽"});
        citys.put("0552", new String[]{"安徽省蚌埠市", "安徽"});
        citys.put("0553", new String[]{"安徽省芜湖市", "安徽"});
        citys.put("0554", new String[]{"安徽省淮南市", "安徽"});
        citys.put("0555", new String[]{"安徽省马鞍山", "安徽"});
        citys.put("0556", new String[]{"安徽省安庆市", "安徽"});
        citys.put("0557", new String[]{"安徽省宿州市", "安徽"});
        citys.put("0558", new String[]{"安徽省阜阳市", "安徽"});
        citys.put("0559", new String[]{"安徽省黄山市", "安徽"});
        citys.put("0561", new String[]{"安徽省淮北市", "安徽"});
        citys.put("0562", new String[]{"安徽省铜陵市", "安徽"});
        citys.put("0563", new String[]{"安徽省宣城市", "安徽"});
        citys.put("0564", new String[]{"安徽省六安市", "安徽"});
        citys.put("0565", new String[]{"安徽省巢湖市", "安徽"});
        citys.put("0566", new String[]{"安徽省贵池市", "安徽"});

        citys.put("0951", new String[]{"宁夏银川市", "宁夏"});
        citys.put("0952", new String[]{"宁夏石嘴山", "宁夏"});
        citys.put("0953", new String[]{"宁夏吴忠市", "宁夏"});
        citys.put("0954", new String[]{"宁夏固原市", "宁夏"});

        citys.put("0431", new String[]{"吉林省长春市", "吉林"});
        citys.put("0432", new String[]{"吉林省吉林市", "吉林"});
        citys.put("0433", new String[]{"吉林省延吉市", "吉林"});
        citys.put("0434", new String[]{"吉林省四平市", "吉林"});
        citys.put("0435", new String[]{"吉林省通化市", "吉林"});
        citys.put("0436", new String[]{"吉林省白城市", "吉林"});
        citys.put("0437", new String[]{"吉林省辽源市", "吉林"});
        citys.put("0438", new String[]{"吉林省松原市", "吉林"});
        citys.put("0439", new String[]{"吉林省浑江市", "吉林"});
        citys.put("0440", new String[]{"吉林省珲春市", "吉林"});

        citys.put("0770", new String[]{"广西省防城港", "广西"});
        citys.put("0771", new String[]{"广西省南宁市", "广西"});
        citys.put("0772", new String[]{"广西省柳州市", "广西"});
        citys.put("0773", new String[]{"广西省桂林市", "广西"});
        citys.put("0774", new String[]{"广西省梧州市", "广西"});
        citys.put("0775", new String[]{"广西省玉林市", "广西"});
        citys.put("0776", new String[]{"广西省百色市", "广西"});
        citys.put("0777", new String[]{"广西省钦州市", "广西"});
        citys.put("0778", new String[]{"广西省河池市", "广西"});
        citys.put("0779", new String[]{"广西省北海市", "广西"});

        citys.put("0851", new String[]{"贵州省贵阳市", "贵州"});
        citys.put("0852", new String[]{"贵州省遵义市", "贵州"});
        citys.put("0853", new String[]{"贵州省安顺市", "贵州"});
        citys.put("0854", new String[]{"贵州省都均市", "贵州"});
        citys.put("0855", new String[]{"贵州省凯里市", "贵州"});
        citys.put("0856", new String[]{"贵州省铜仁市", "贵州"});
        citys.put("0857", new String[]{"贵州省毕节市", "贵州"});
        citys.put("0858", new String[]{"贵州省六盘水", "贵州"});
        citys.put("0859", new String[]{"贵州省兴义市", "贵州"});

        citys.put("029", new String[]{"陕西省西安市", "陕西"});
        citys.put("0910", new String[]{"陕西省咸阳市", "陕西"});
        citys.put("0911", new String[]{"陕西省延安市", "陕西"});
        citys.put("0912", new String[]{"陕西省榆林市", "陕西"});
        citys.put("0913", new String[]{"陕西省渭南市", "陕西"});
        citys.put("0914", new String[]{"陕西省商洛市", "陕西"});
        citys.put("0915", new String[]{"陕西省安康市", "陕西"});
        citys.put("0916", new String[]{"陕西省汉中市", "陕西"});
        citys.put("0917", new String[]{"陕西省宝鸡市", "陕西"});
        citys.put("0919", new String[]{"陕西省铜川市", "陕西"});

        citys.put("0971", new String[]{"青海省西宁市", "青海"});
        citys.put("0972", new String[]{"青海省海东市", "青海"});
        citys.put("0973", new String[]{"青海省同仁市", "青海"});
        citys.put("0974", new String[]{"青海省共和市", "青海"});
        citys.put("0975", new String[]{"青海省玛沁市", "青海"});
        citys.put("0976", new String[]{"青海省玉树市", "青海"});
        citys.put("0977", new String[]{"青海省德令哈", "青海"});

        citys.put("0890", new String[]{"海南省儋州市", "海南"});
        citys.put("0898", new String[]{"海南省海口市", "海南"});
        citys.put("0899", new String[]{"海南省三亚市", "海南"});

        citys.put("0891", new String[]{"西藏拉萨市", "西藏"});
        citys.put("0892", new String[]{"西藏日喀则", "西藏"});
        citys.put("0893", new String[]{"西藏山南市", "西藏"});
    }

    private CityHelper() {
    }


    public static Map<String, String[]> getCitys() {
        return citys;
    }

    /**
     * 根据区号找城市名
     *
     * @param code 区号
     * @return 城市或未知
     */
    public static String getByAreaCode(String code) {
        String[] city = getArrByAreaCode(code);
        return city == null ? "" : city[0];
    }

    /**
     * 根据区号找省市名
     *
     * @param code 区号
     * @return 0为城市 1为省份
     */
    public static String[] getArrByAreaCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return citys.getOrDefault(code, null);
    }
}
