var dname = document.URL;
var comName = "";
var comName2 = "";
if(!Array.prototype.indexOf)
{
	Array.prototype.indexOf = function(searchElement, fromIndex){
		var k;
		if(this == null) {
			throw new TypeError('"this" is null or not defined');
		}

		var O = Object(this);
		var len = O.length >>> 0;

		if(len === 0) {
			return -1;
		}

		var n = +fromIndex || 0;

		if(Math.abs(n) === Infinity) {
			n=0;
		}

		if(n >= len) {
			return -1;
		}

		k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);

		while(k < len) {
			if( k in O && O[k] === searchElement) {
				return k;			
			}
			k++;
		}

		return -1;
	};
}
try{
	if(dname.indexOf("www.auction.co.kr")!=-1){
		comName = "������";
		comName2 = "���ǿ�";
	}else if(dname.indexOf("11st.co.kr")!=-1){
		comName = "11������";
		comName2 = "11������";
	}else if(dname.indexOf("www.gmarket.co.kr")!=-1){
		comName = "��������";
		comName2 = "�����Ͽ�";
	}else if(dname.indexOf("shinsegaemall.ssq.com/?ckwhere=kbmall")!=-1){
		comName = "�ż������";
		comName2 = "�ż������";
	}else if(dname.indexOf("www.homeplus.co.kr")!=-1){
		comName = "Ȩ�÷��� �¶��θ�Ʈ��";
		comName2 = "Ȩ�÷��� �¶��θ�Ʈ��";
	}else if(dname.indexOf("direct.homeplus.co.kr")!=-1){
		comName = "Ȩ�÷��� �¶��θ���";
		comName2 = "Ȩ�÷��� �¶��θ���";
	}else if(dname.indexOf("emart.ssq.com")!=-1){
		comName = "�̸�Ʈ����";
		comName2 = "�̸�Ʈ����";
	}else if(dname.indexOf("www.gsshop.com")!=-1){
		comName = "GS SHOP��";
		comName2 = "GS SHOP��";
	}else if(dname.indexOf("kbcard.lotte.com")!=-1){
		comName = "�Ե�������";
		comName2 = "�Ե����Ŀ�";
	}else if(dname.indexOf("www.yes24.com")!=-1){
		comName = "YES24��";
		comName2 = "YES24��";
	}else if(dname.indexOf("www.namall.com")!=-1){
		comName = "NS MALL��";
		comName2 = "NS MALL��";
	}else if(dname.indexOf("www.ssq.com")!=-1){
		comName = "�ż��������";
		comName2 = "�ż�����Ŀ�";
	}else if(dname.indexOf("www.interpark.com")!=-1){
		comName = "������ũ������";
		comName2 = "������ũ���ο�";
	}else if(dname.indexOf("www.shilladfs.com")!=-1){
		comName = "�Ŷ�鼼����";
		comName2 = "�Ŷ�鼼����";
	}else if(dname.indexOf("www.cjmall.com")!=-1){
		comName = "CJ MALL��";
		comName2 = "CJ MALL��";
	}else if(dname.indexOf("giftishop.kr")!=-1){
		comName = "����������� ����Ƽ����";
		comName2 = "����������� ����Ƽ����";
	}else if(dname.indexOf("www.hyundaihmall.com")!=-1){
		comName = "����Hmall��";
		comName2 = "����Hmall��";
	}else if(dname.indexOf("www.e-himart.co.kr")!=-1){
		comName = "���̸�Ʈ��";
		comName2 = "���̸�Ʈ��";
	}else if(dname.indexOf("www.bestestore.co.kr")!=-1){
		comName = "BESTeSTORE��";
		comName2 = "BESTeSTORE��";
	}else if(dname.indexOf("ticket.yes24.com")!=-1){
		comName = "YES24 ������";
		comName2 = "YES24 ������";
	}else if(dname.indexOf("www.akmall.com")!=-1){
		comName = "AK MALL��";
		comName2 = "AK MALL��";
	}else if(dname.indexOf("shinsegaemall.ssq.com/special/happybuyrus.ssq")!=-1){
		comName = "���ǹ��̷�����";
		comName2 = "���ǹ��̷�����";
	}else if(dname.indexOf("kb.hanashop.com")!=-1){
		comName = "�ϳ�����";
		comName2 = "�ϳ�����";
	}else if(dname.indexOf("www.cesti.com")!=-1){
		comName = "�ſ� CESTI��";
		comName2 = "�ſ� CESTI��";
	}else if(dname.indexOf("kbcard.ipointmall.com")!=-1){
		comName = "��������Ʈ����";
		comName2 = "��������Ʈ����";
	}else if(dname.indexOf("www.brapra.com")!=-1){
		comName = "������������";
		comName2 = "���������Ŀ�";
	}else if(dname.indexOf("kbcard.leedongsooshop.com")!=-1){
		comName = "�̵��� ��������";
		comName2 = "�̵��� ��������";
	}else if(dname.indexOf("www.kbpointreemall.com")!=-1){
		comName = "(��)�ѱ���ũ�����۴ϰ�";
		comName2 = "(��)�ѱ���ũ�����۴Ͽ�";
	}else if(dname.indexOf("kbcardbiz.com")!=-1){
		comName = "(��)������ũ�������";
		comName2 = "(��)������ũ����Ͽ�";
	}else{
		comName = "�ش� ���޻簡";
		comName2 = "�� ���޻翡";
	}
}catch(ex){
	comName = "�ش� ���޻簡";
	comName2 = "�� ���޻翡";
}

document.write('<style type="text/css">')
document.write('* {padding:0; margin:0;}')
document.write('</style>')
document.write('<div style="width:100%">')
document.write('    <div style="width:100%; position:relative; z-index:1000; ">')
document.write('        <div style="width:100%; background:#a19687; text-align:center;">')
document.write('            <div style="width:1200px; height:25px; margin:0 auto; overflow:hidden; text-align:right;">')
document.write('                <h1 style="float:left; margin:0; padding:0;">')
document.write('					<a target="_blank" title="��â����" style="text-decoration:none" href="https://card.kbcard.com">')
document.write('						<img style="vertical-align:top; border:0 none;" src="https://img1.kbcard.com/LT/cxl/2015/common/logo_global.gif" alt="KB����ī��" />')
document.write('					</a>')
document.write('				</h1>')
document.write('                <ul style="display:inline; float:right; margin:0; padding:0; zoom:1; vertical-align:top; list-style:none;">')
document.write('                    <li style="display:inline-block;float:left; list-style:none; margin:0; padding:0 1px 0 0; height:25px; zoom:1; background:url(\'https://img1.kbcard.com/LT/cxl/2015/common/line_global_menu.gif\') no-repeat 100% 50%;">')
document.write('						<a href="http://life.kbcard.com/event/CXLREEVC0001.cms?gnbNm=ev" target="_blank" title="��â����">')
document.write('							<img style="border:0" src="https://img1.kbcard.com/LT/cxl/2015/common/global_m_04.gif" alt="�̺�Ʈ" style="vertical-align:middle;" />')
document.write('						</a>')
document.write('					</li>')
document.write('                    <li style="display:inline-block;float:left; list-style:none; margin:0; padding:0 0 0 0; height:25px; zoom:1;">')
document.write('						<a href="http://life.kbcard.com/helpdesk/CXLRECUC0001.cms?gnbNm=cu" target="_blank" title="��â����">')
document.write('							<img style="border:0" src="https://img1.kbcard.com/LT/cxl/2015/common/global_m_05.gif" alt="������" style="vertical-align:middle;" />')
document.write('						</a>')
document.write('					</li>')
document.write('                </ul>')
document.write('            </div>')
document.write('        </div>')
document.write('        <div style="width:100%; background:#fff; border-bottom:1px solid #e4e4e4; position:relative; z-index:1000; text-align:center;">')
document.write('            <div style="position:relative; width:1200px; height:64px; margin:0 auto; z-index:1000;">')
document.write('                <h2 style="position:absolute; top:17px; left:0; margin:0; padding:0;">')
document.write('					<a target="_blank" title="��â����" href="http://life.kbcard.com" style="text-decoration:none;">')
document.write('						<img style="border:0 none;" src="https://img1.kbcard.com/LT/cxl/2015/common/logo_life.gif" alt="LIFE #" />')
document.write('					</a>')
document.write('				</h2>')
document.write('                <ul style="position:absolute; left:151px; text-align:left; padding:22px 0; margin:0; list-style:none;">')
document.write('                    <li style="list-style:none; margin:0 30px 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block; width:36px; height:20px;" href="http://life.kbcard.com/tour/CXLRETRC0005.cms?gnbNm=tr" onmouseover="document.kb_menu1.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_01_over.gif\'" onmouseout="document.kb_menu1.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_01.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_01.gif" name="kb_menu1" alt="����" style="border:0" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                    <li style="list-style:none; margin:0 30px 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block; width:35px; height:20px;" href="http://life.kbcard.com/pointree/CXLRESPC0001.cms" onmouseover="document.kb_menu2.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_02_over.gif\'" onmouseout="document.kb_menu2.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_02.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_02.gif" name="kb_menu2" alt="����" style="border:0" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                    <li style="list-style:none; margin:0 30px 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block;" href="http://life.kbcard.com/CXLREFFC0002.cms?gnbNm=ff" onmouseover="document.kb_menu3.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_03_over.gif\'" onmouseout="document.kb_menu3.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_03.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_03.gif" name="kb_menu3" alt="�ؿ�����" style="border:0" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                    <li style="list-style:none; margin:0 30px 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block;" href="http://life.kbcard.com/CXLREGOC0001.cms?gnbNm=go" onmouseover="document.kb_menu4.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_04_over.gif\'" onmouseout="document.kb_menu4.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_04.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_04.gif" name="kb_menu4" alt="����" style="border:0" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                    <li style="list-style:none; margin:0 30px 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block;" href="http://life.kbcard.com/CXLREWDC0001.cms?gnbNm=wd" onmouseover="document.kb_menu5.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_05_over.gif\'" onmouseout="document.kb_menu5.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_05.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_05.gif" name="kb_menu5" alt="����" style="border:0" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                    <li style="list-style:none; margin:0 30px 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block;" href="http://life.kbcard.com/CXLRELVC0001.cms?gnbNm=lv" onmouseover="document.kb_menu6.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_06_over.gif\'" onmouseout="document.kb_menu6.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_06.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_06.gif" name="kb_menu6" alt="�����ɾ�" style="border:0" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                    <li style="list-style:none; margin:0 0 0 0; float:left;">')
document.write('                        <a target="_blank" title="��â����" style="display:block;" href="http://life.kbcard.com/CXLREISC0001.cms?gnbNm=is" onmouseover="document.kb_menu7.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_07_over.gif\'" onmouseout="document.kb_menu7.src=\'https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_07.gif\'">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/layout_gnbmenu_07.gif" name="kb_menu7" alt="����" style="border:0;" />')
document.write('                        </a>')
document.write('                    </li>')
document.write('                </ul>')
document.write('                <div style="position:absolute; top:0; right:0; padding:19px 0; z-index:1000; overflow:hidden;">')
document.write('                    <a target="_blank" title="��â����" style="display:block;float:left;height:26px;margin-right:11px;" href="http://www.ruyizhu.co.kr/" onmouseover="document.kb_menu_ruyizhu.src=\'https://img1.kbcard.com/LT/cxl/2015/common/ruyizhu_on.png\'" onmouseout="document.kb_menu_ruyizhu.src=\'https://img1.kbcard.com/LT/cxl/2015/common/ruyizhu_off.png\'">')
document.write('                        <img src="https://img1.kbcard.com/LT/cxl/2015/common/ruyizhu_off.png" name="kb_menu_ruyizhu" alt="������" style="border:0;margin-top:2px;" />')
document.write('                    </a>')
document.write('                    <span style="float:left; margin-left:4px;">')
document.write('                        <a href="#none" style="float:left; margin-left:0;" onclick="window.alert(\'ǥ�� ������������ Ȯ��/��Ҵ� �������� ����� �̿��մϴ�.\nȭ�� Ȯ��: Ű������ controlŰ�� \'+\'�� ���ÿ� �����ϴ�. \nȭ�� ���: Ű������ controlŰ�� \'-\'�� ���ÿ� �����ϴ�.\'); return false;">')
document.write('                            <img src="https://img1.kbcard.com/LT/cxl/2015/common/btn_zoomin.gif" alt="zoom in" style="border:0; vertical-align:top;" />')
document.write('                        </a>')
document.write('                        <a href="#none" style="float:left; margin-left:2px;" onclick="window.alert(\'ǥ�� ������������ Ȯ��/��Ҵ� �������� ����� �̿��մϴ�.\nȭ�� Ȯ��: Ű������ controlŰ�� \'+\'�� ���ÿ� �����ϴ�. \nȭ�� ���: Ű������ controlŰ�� \'-\'�� ���ÿ� �����ϴ�.\'); return false;">')
document.write('							<img src="https://img1.kbcard.com/LT/cxl/2015/common/btn_zoomout.gif" alt="zoom out" style="border:0; vertical-align:top;" />')
document.write('						</a>')
document.write('                    </span>')
document.write('                </div>')
document.write('            </div>')
document.write('        </div>')
document.write('        <div style="width:100%; border-bottom:1px solid #e4e4e4; background:#f7f7f7; padding:8px 0; text-align:center;">')
document.write('            <div style="width:1200px; margin:0 auto; overflow:hidden;">')
document.write('                <p style="float:left; margin:0; padding:0 0 0 30px; background:url(\'https://img1.kbcard.com/LT/cxl/2015/common/icon_label_type02.png\') no-repeat 0 55%; line-height:150%; font-family:\'NanumGothic\', \'�������\', \'����\', dotum, sans-serif; font-size:12px; color:#6e6e6e;">KB����ī��� ���޿� ���� <strong style="font-weight:bold;" id="KBCompName1"></strong> ���θ��� ��ϰ� ������, ���� ���� ��� ����å���� <strong style="font-weight:bold;" id="KBCompName2"></strong> �ֽ��ϴ�.</p>')
document.write('            </div>')
document.write('        </div>')
document.write('    </div>')
document.write('  </div>')

document.getElementById("KBCompName1").innerHTML = comName;
document.getElementById("KBCompName2").innerHTML = comName2;