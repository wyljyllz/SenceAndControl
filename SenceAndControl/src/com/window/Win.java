package com.window;

import com.chart.DataWindows;
import com.chart.Plaint;
import com.chart.chart;
import com.frame.*;/*数据帧超类*/
import com.utils.FormatConverter;/*将字节流转换为十六进制字符串*/

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
    public Center center;/*居中*/
//    private Center1 center1;
    private Bottom bottom; 
    private JLabel[] label;/*标签组*/
    public JCheckBox[] checkBox1;
    public JTextField textField1;/*文本框,当前温度*/
    public JTextField textField2;/*设置温度*/
    public JTextField textField3;/*压缩机状态*/
    private JTextField textField4;
    private JLabel label1;/*标签*/
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private String command;/*命令字符串*/
    private String Temp;
    private JTextArea dataView = new JTextArea();/*数据视图位置*/
    public int sequence=0;/*序列*/
    public  int deviceAddress=1;/*地址*/
    private JScrollPane scrollDataView = new JScrollPane(dataView);/*滚动窗格，滚动数据视图*/
    private BlockingQueue<Frame> uploadQueue = new LinkedBlockingQueue<Frame>(); // 堵塞队列用来存放上传的帧
    private BlockingQueue<Frame> downQueue = new LinkedBlockingQueue<Frame>(); //阻塞队列用来存放需要下发的帧
    public Win()
    {
        top=new Top();/*初始化对象*/
        center=new Center();
        bottom=new Bottom();
        setTitle("现场快递柜状态采集与控制系统");
        setBounds(100,50,800, 650);/*x轴起点300，y轴起点200，宽700，高300*/
        setVisible(true);/*可视化成功*/
        this.setLayout(new BorderLayout());/*设置布局、边界布局*/
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
            case 1:downQueue.add(new FindFrame(sequence++,deviceAddress));break;/*查询帧*/
            case 2:downQueue.add(new BootFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));break;/*启停压缩机*/
            case 3:downQueue.add(new OpenFrame(sequence++,deviceAddress, FormatConverter.hexStringToBytes(command)));break;/*开锁*/
            case 4:downQueue.add(new SetTemperatureFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));break;/*设置温度*/
            case 5:downQueue.add(new SetParameterFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)));break;/*设置参数*/
            case 6:downQueue.add(new SetBiasFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));break;/*设置温度偏差控制*/
            case 7:downQueue.add(new SetDeviceAddress(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)));break;/*设置设备地址*/
            default: return false;
        }
        return true;
    }
    
    private class Top extends JPanel
    {
        public Top()
        {
        	
            textField1 = new JTextField(10);
           
            textField2 = new JTextField(10);/*设定温度值*/
            
            textField3 = new JTextField(10);
            label1 = new JLabel("当前温度：");
            label2 = new JLabel("设定温度：");
            label3= new JLabel("工作状态(压缩机)：");
            this.setLayout(new FlowLayout());/*设置边界*/
            add(label1);
            add(textField1);
            add(label2);
            add(textField2);
            add(label3);
            add(textField3);
  
        }

    }
   
    public class Center extends JPanel /*抽屉组*/
    {
   
        public Center()
        {
            this.setLayout(new FlowLayout());
            checkBox1=new JCheckBox[10];
            for(int i=0;i<10;i++)
            {
                String name="抽屉"+Integer.toString(i+1);
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
    
    
    private class Bottom extends JPanel//四个按钮组合
    {
        private JButton button1;
        private JButton button2;
        private JButton button3;
        private JButton button4;
        public Bottom() {
            this.setLayout (new FlowLayout());
            button1 =new JButton("开关指定抽屉");
            button2 =new JButton("启动温度控制");
            button3 =new JButton("设置控制温度");
            button4 =new JButton("设置系统参数");
            button2.setBackground(Color.RED);//CYAN代表开始温度控制，粉色代表停止
            
            button1.setPreferredSize(new Dimension(150, 40));
            button2.setPreferredSize(new Dimension(150, 40));
            button3.setPreferredSize(new Dimension(150, 40));
            button4.setPreferredSize(new Dimension(150, 40));
            add(button1);
            add(button2);
            add(button3);
            add(button4);
            button1.addActionListener(new ActionListener() {/*开启监听事件*/
                @Override
                public void actionPerformed(ActionEvent e) {
                    Switch p1=new Switch();
                }
            });
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(button2.getText().equals("启动温度控制"))	{
                        button2.setBackground(Color.CYAN);
                        button2.setText("停止温度控制");
                        command="01";
                        createFrame(2);
                    }
                    else {
                        button2.setBackground(Color.RED);
                        button2.setText("启动温度控制");
                        command="00";
                        createFrame(2);/*启停压缩机指令*/
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
    private class Switch extends JFrame/*控制抽屉开关*/
    {
        private JButton button;
        private JCheckBox[] checkBox;
        public Switch()
        {
            this.setLayout (new FlowLayout());
            setTitle("开关指定抽屉");
            setBounds(100,100,700, 400);
            checkBox=new JCheckBox[10];
            for(int i=0;i<10;i++)
            {
                String name="抽屉"+Integer.toString(i+1);
                checkBox[i]=new JCheckBox(name);
                checkBox[i].setPreferredSize(new Dimension(110, 50));
                add(checkBox[i]);
                setBounds(100,100,700, 250);
                setVisible(true);
            }
            button =new JButton("确认");
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
    private class Control_tem extends JFrame{/*设置控制温度*/
        private JButton enter;
        private JTextField textField;
        private JLabel label;
        public Control_tem()
        {
            label = new JLabel("温度：");
            textField = new JTextField(15);
            enter=new JButton("确认");
            this.setLayout(new FlowLayout());
            add(label);
            add(textField);
            add(enter);
            setBounds(800,430,320,100);
            setTitle("设置控制温度");
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
                    System.out.println("设置温度command= "+command); 
                    downQueue.add(new SetTemperatureFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)[0]));/*设置温度*/
                
                 //   createFrame(4);
                }
            });
        }
    }
    private class Control_sys extends JFrame{/*设置参数*/
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
            label[0] = new JLabel("设备ID：");
            label[1] = new JLabel("设备地址：");
            label[2]= new JLabel("采集时间间隔：");
            label[3]= new JLabel("压缩机启动延时：");
            label[4]= new JLabel("控制温度：");
            label[5]= new JLabel("温度控制偏差：");
            enter=new JButton("上传");
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
            setTitle("设置系统参数");
            setVisible(true);
            enter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String[] str=new String[6];
                	command="";
                	for(int i=0;i<6;i++) {
                		
                		if(i==0)//设备编码
                			{
                				for(int j=textField[i].getText().length();j<10;j++)

                					command+="F";
                				command+=textField[i].getText();
                			}	
                    
                    	if(i==1)//设备地址
                    		command+=textField[i].getText();
                    	if(i==2)//上传间隔
                    	{
                    		command+="00";//备用空白帧
                    		command+=textField[i].getText();
                    		}
                    	if(i==3)//启动时延
                		{
                    	String tem=textField[i].getText();
                    	int mn=Integer.parseInt(tem);
                    	String hexs=new String();
                    	if(mn>15)
                    	hexs =FormatConverter.intToHex(mn);
                    	else
                    		 hexs="0"+FormatConverter.intToHex(mn);
                    	
                		command+=hexs;
                		command+="0000";//备用
                		}
                    	if(i==4)//设定温度
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
                    	if(i==5)//温度控制偏差
                		{
                		command+=textField[i].getText();
                		command+="FFFFFFFF00";
                		}        
                    	
                		
                		//command+=textField[i].getText();
                    }
                    System.out.println("设置参数command= "+command);   
                 dispose();
                 downQueue.add(new SetParameterFrame(sequence++,deviceAddress,FormatConverter.hexStringToBytes(command)));/*设置参数*/
                   //  	createFrame(5);
                    	
                }
            });            
            
        }
    }
}
