package lh.wordtree.plugin;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestWTPlugin implements WTPlugin {
    @Override
    public void init() {

    }

    @Override
    public void apply() {

    }

    @Override
    public void end() {

    }

    @Override
    public WTPluginConfig config() {
        return new WTPluginConfig() {

            public String name() {
                return "�Ķ������";
            }

            public String version() {
                return "1.4.1";
            }

            public String author() {
                return "�ֺ�";
            }

            public Image icon() {
                try {
                    return new Image(new FileInputStream("C:\\Users\\28322\\Pictures\\Saved Pictures\\lz.jpg"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public String introduce() {
                return """
                        �����塷�����ҽ��ϴ�����ϵ�г�ƪ����С˵��
                        �ɡ�����񣺻�֮���ء��������������֮ͫ��������������֮��������������¶�֮Ԩ������������������ߵĹ�������ɣ�
                        2009��10��1�տ�ʼ��С˵�������أ���һ����2010��04���״γ��棬�ڶ�����2011��05�³��棬
                        ��������ƪ��2012��12�³��棬��������ƪ��2013��07�³��棬��������ƪ��2013��12�³���
                        �����Ĳ�����2015��10�³��棬���岿��2018��5��15����QQ�Ķ�ƽ̨��ʼ���ء�
                        """;
            }

            @Override
            public WTPluginType type() {
                return WTPluginType.language;
            }
        };
    }
}
