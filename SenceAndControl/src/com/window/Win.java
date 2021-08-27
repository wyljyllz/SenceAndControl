package com.window;

import com.chart.DataWindows;
import com.chart.Plaint;
import com.chart.chart;
import com.frame.*;/*����֡����*/
import com.utils.FormatConverter;/*���ֽ���ת��Ϊʮ�������ַ���*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;

public class Win extends JFrame {
    private Top top;
    public Center center;/*����*/
//    private Center1 center1;
    private Bottom bottom; 
    private JLabel[] label;/*��ǩ��*/
    public JCheckBox[] checkBox1;
    public JTextField textField1;/*�ı���,��ǰ�¶�*/
    public JTextField textField2;/*�����¶�*/
    public JTextField textField3;/*ѹ����״̬*/
    private JTextField textField4;
    private JLabel label1;/*��ǩ*/
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private String command;/*�����ַ���*/
    private String Temp;
    private JTextArea dataView = new JTextArea();/*������ͼλ��*/
    public int sequence=0;/*����*/
    public  int deviceAddress=1;/*��ַ*/
    private JScrollPane scrollDataView = new JScrollPane(dataView);/*�������񣬹���������ͼ*/
    private BlockingQueue<Frame> uploadQueue = new LinkedBlockingQueue<Frame>(); // ����������������ϴ���֡
    private BlockingQueue<Frame> downQueue = new LinkedBlockingQueue<Frame>(); //�����������������Ҫ�·���֡
    public Win()
    {
        top=new Top();/*��ʼ������*/
        center=new Center();
        bottom=new Bottom();
        setTitle("�ֳ���ݹ�״̬�ɼ������ϵͳ");
        setBounds(100,50,800, 650);/*x�����300��y�����200����700����300*/
        setVisible(true);/*���ӻ��ɹ�*/
        this.setLayout(new BorderLayout());/*���ò��֡��߽粼��*/
        add(top,BorderLayout.NORTH);
        add(center,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
    }

    public BlockingQueue<Frame> getUploadQueue() {
        return uploadQueue;
    }

    public BlockingQueue<Frame> getDownQueue() {
        return downQueue;
    }

    public Boolean createFrame(int type)
    {
        switch(type)
        {
            case 1:downQueue.add(new FindFrame(sequence++,deviceAddress));break;/*��ѯ֡*/
            case 2:downQueue.add(new BootFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));break;/*��ͣѹ����*/
            case 3:downQueue.add(new OpenFrame(sequence++,deviceAddress, FormatConverter.hexStringToBytes(command)));break;/*����*/
            case 4:downQueue.add(new SetTemperatureFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));break;/*�����¶�*/
            case 5:downQueue.add(new SetParameterFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)));break;/*���ò���*/
            case 6:downQueue.add(new SetBiasFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));break;/*�����¶�ƫ�����*/
            case 7:downQueue.add(new SetDeviceAddress(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)));break;/*�����豸��ַ*/
            default: return false;
        }
        return true;
    }
    
    private class Top extends JPanel
    {
        public Top()
        {
        	
            textField1 = new JTextField(10);
           
            textField2 = new JTextField(10);/*�趨�¶�ֵ*/
            
            textField3 = new JTextField(10);
            label1 = new JLabel("��ǰ�¶ȣ�");
            label2 = new JLabel("�趨�¶ȣ�");
            label3= new JLabel("����״̬(ѹ����)��");
            this.setLayout(new FlowLayout());/*���ñ߽�*/
            add(label1);
            add(textField1);
            add(label2);
            add(textField2);
            add(label3);
            add(textField3);
  
        }

    }
   
    public class Center extends JPanel /*������*/
    {
   
        public Center()
        {
            this.setLayout(new FlowLayout());
            checkBox1=new JCheckBox[10];
            for(int i=0;i<10;i++)
            {
                String name="����"+Integer.toString(i+1);
                checkBox1[i]=new JCheckBox(name);
                checkBox1[i].setPreferredSize(new Dimension(150, 80));
                add(checkBox1[i]);
            }
           
             chart ct = new chart();
             ChartPanel chartf = new ChartPanel(ct.freeChart,true);
             chartf.setPreferredSize(new Dimension(500, 300));
        //    double []dat= {1,2,5,7,9,6,4,8,3,6,4,7,8,4,2,8,7,4,6,1,9,8};
         //   double[][] dat= {{7,8,10,-2,20,-10,30,15,33,0,8,7,-9,15,2,2,20,-10,30,15}};
             // ct.SetDATA(dat);
              //ct.createDataset(dat);
            
               chartf.setVisible(true);
              add(chartf);
        
        }
    }
    
    
    private class Bottom extends JPanel//�ĸ���ť���
    {
        private JButton button1;
        private JButton button2;
        private JButton button3;
        private JButton button4;
        public Bottom() {
            this.setLayout (new FlowLayout());
            button1 =new JButton("����ָ������");
            button2 =new JButton("�����¶ȿ���");
            button3 =new JButton("���ÿ����¶�");
            button4 =new JButton("����ϵͳ����");
            button2.setBackground(Color.RED);//CYAN����ʼ�¶ȿ��ƣ���ɫ����ֹͣ
            
            button1.setPreferredSize(new Dimension(150, 40));
            button2.setPreferredSize(new Dimension(150, 40));
            button3.setPreferredSize(new Dimension(150, 40));
            button4.setPreferredSize(new Dimension(150, 40));
            add(button1);
            add(button2);
            add(button3);
            add(button4);
            button1.addActionListener(new ActionListener() {/*���������¼�*/
                @Override
                public void actionPerformed(ActionEvent e) {
                    Switch p1=new Switch();
                }
            });
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(button2.getText().equals("�����¶ȿ���"))	{
                        button2.setBackground(Color.CYAN);
                        button2.setText("ֹͣ�¶ȿ���");
                        command="01";
                        createFrame(2);
                    }
                    else {
                        button2.setBackground(Color.RED);
                        button2.setText("�����¶ȿ���");
                        command="00";
                        createFrame(2);/*��ͣѹ����ָ��*/
                    }
                }
            });
            button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Control_tem b3=new Control_tem();
                }
            });
            button4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Control_sys b4=new Control_sys();
                }
            });
        }
    }
    private class Switch extends JFrame/*���Ƴ��뿪��*/
    {
        private JButton button;
        private JCheckBox[] checkBox;
        public Switch()
        {
            this.setLayout (new FlowLayout());
            setTitle("����ָ������");
            setBounds(100,100,700, 400);
            checkBox=new JCheckBox[10];
            for(int i=0;i<10;i++)
            {
                String name="����"+Integer.toString(i+1);
                checkBox[i]=new JCheckBox(name);
                checkBox[i].setPreferredSize(new Dimension(110, 50));
                add(checkBox[i]);
                setBounds(100,100,700, 250);
                setVisible(true);
            }
            button =new JButton("ȷ��");
            button.setPreferredSize(new Dimension(150, 40));
            add(button);
            setVisible(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	command="";
                    for(int i=0;i<10;i++) {
                        if(checkBox[i].isSelected()) {
                        	checkBox1[i].setSelected(true);
                            command+=checkBox[i].getText();
                        }
                    }
                    String regEx="[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(command);
                    command=m.replaceAll("").trim();
                    System.out.println(command);
                    long sum=0;
                    for(int i=0;i<command.length();i++)
                    {
                        char s=command.charAt(i);
                        System.out.println(s);
                        switch(s)
                        {
                           
                            case '1':  if((command.length()==1)||command.charAt(i+1)!='0')sum+=256;else sum+=2;break;
                            case '2':sum+=512;break;
                            case '3':sum+=1024;break;
                            case '4':sum+=2048;break;
                            case '5':sum+=4096;break;
                            case '6':sum+=8192;break;
                            case '7':sum+=16384;break;
                            case '8':sum+=32768;break;
                            case '9':sum+=1;break;
                            default:break;
                        }
                    }
                    command=Long.toHexString(sum);
                    command=command.toUpperCase();
                    while(command.length()!=4) {
                        command="0"+command;}
                    createFrame(3);
                    dispose();
                }
            });
        }
    }
    private class Control_tem extends JFrame{/*���ÿ����¶�*/
        private JButton enter;
        private JTextField textField;
        private JLabel label;
        public Control_tem()
        {
            label = new JLabel("�¶ȣ�");
            textField = new JTextField(15);
            enter=new JButton("ȷ��");
            this.setLayout(new FlowLayout());
            add(label);
            add(textField);
            add(enter);
            setBounds(800,430,320,100);
            setTitle("���ÿ����¶�");
            setVisible(true);
            enter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String Tem=textField.getText();
                    dispose();
                    float tem=Float.parseFloat(Tem);
                    int t=(int)tem;
                    String s = Integer.toString(t);
                    textField2.setText(s);
                    boolean flag=false;
                    t=t*2;
                       if(tem<0)
                    {
                    	t=t*(-1);
                    	 t=t+128;
                     //   t=t+1;
                    }
                    
                    if((tem%1)==0.5)
                    {
                        
                        t=t+1;
                        flag=true;
                        
                    }
                                      
                
                    if(t<15) {
                        command="0"+Integer.toHexString((int)t);
                    } else {
                        command=Integer.toHexString((int)t);
                    }
                    command=command.toUpperCase();
                    System.out.println(tem);
                    System.out.println("�����¶�command= "+command); 
                    downQueue.add(new SetTemperatureFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));/*�����¶�*/
                
                 //   createFrame(4);
                }
            });
        }
    }
    private class Control_sys extends JFrame{/*���ò���*/
        private JTextField[] textField;
        private JButton enter;
        private String command;
        public  Control_sys()
        {
            textField=new JTextField[6];
            label=new JLabel[6];
            for(int i=0;i<6;i++) {
                textField[i]=new JTextField(10);
            }
            label[0] = new JLabel("�豸ID��");
            label[1] = new JLabel("�豸��ַ��");
            label[2]= new JLabel("�ɼ�ʱ������");
            label[3]= new JLabel("ѹ����������ʱ��");
            label[4]= new JLabel("�����¶ȣ�");
            label[5]= new JLabel("�¶ȿ���ƫ�");
            enter=new JButton("�ϴ�");
            this.setLayout(new FlowLayout());
            for(int i=0;i<6;i++)
            {
            	add(label[i]);
                add(textField[i]);
            }
            textField[0].setText("FFFFFFFFFF");
            textField[1].setText("01");
            textField[2].setText("01");
            textField[3].setText("10");
            textField[4].setText("10");
            textField[5].setText("03");
            
            
            
            add(enter);
            setBounds(650,300,150,450);
            setTitle("����ϵͳ����");
            setVisible(true);
            enter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String[] str=new String[6];
                	command="";
                	for(int i=0;i<6;i++) {
                		
                		if(i==0)//�豸����
                			{
                				for(int j=textField[i].getText().length();j<10;j++)

                					command+="F";
                				command+=textField[i].getText();
                			}	
                    
                    	if(i==1)//�豸��ַ
                    		command+=textField[i].getText();
                    	if(i==2)//�ϴ����
                    	{
                    		command+="00";//���ÿհ�֡
                    		command+=textField[i].getText();
                    		}
                    	if(i==3)//����ʱ��
                		{
                    	String tem=textField[i].getText();
                    	int mn=Integer.parseInt(tem);
                    	String hexs=new String();
                    	if(mn>15)
                    	hexs =FormatConverter.intToHex(mn);
                    	else
                    		 hexs="0"+FormatConverter.intToHex(mn);
                    	
                		command+=hexs;
                		command+="0000";//����
                		}
                    	if(i==4)//�趨�¶�
                		{
                    	
                    		String Tem=textField[i].getText();
                            dispose();
                            String s=new String();
                            float tem=Float.parseFloat(Tem);
                            int t=(int)tem;
                            boolean flag=false;
                            t=t*2;
                               if(tem<0)
                            {
                            	t=t*(-1);
                            	 t=t+128;
                             //   t=t+1;
                            }
                            
                            if((tem%1)==0.5)
                            {
                                
                                t=t+1;
                                flag=true;
                                
                            }
                                              
                        
                            if(t<15) {
                                s="0"+Integer.toHexString((int)t);
                            } else {
                                s=Integer.toHexString((int)t);
                            }
                           s=s.toUpperCase();
                    		
                		command+=s;
                		
                		}
                    	if(i==5)//�¶ȿ���ƫ��
                		{
                		command+=textField[i].getText();
                		command+="FFFFFFFF00";
                		}        
                    	
                		
                		//command+=textField[i].getText();
                    }
                    System.out.println("���ò���command= "+command);   
                 dispose();
                 downQueue.add(new SetParameterFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)));/*���ò���*/
                   //  	createFrame(5);
                    	
                }
            });            
            
        }
    }
}
