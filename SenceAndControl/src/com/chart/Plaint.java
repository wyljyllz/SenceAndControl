package com.chart;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/*author:命运的信徒
 * date:2019.7.18
 * arm:java动态绘图的方法
 */
public class Plaint extends JPanel {
	private List<Integer> values;                      // 存储数据的集合，线程不安全
	private static final int MAX_VALUE = 200;          // 最大值（限制里面的y数据）
	private static final int MAX_COUNT_OF_VALUES = 50; // 集合的最大容量
	public static JFrame frame = new JFrame("java动态绘制图形");
	public Plaint() {
		//一个线程安全的集合
		values = Collections.synchronizedList(new ArrayList<Integer>());
		// 使用线程的目的，动态的，可以往集合里面添加和去除数据
		new Thread(new Runnable() {
			public void run() {
				//一个随机数，准确的说是小于200的
				Random rand = new Random();
				try {
					while (true) {
						//生成的数添加集合，如果集合里面的数据多了就剔除数据
						addValue(rand.nextInt(MAX_VALUE)); // 产生一个数据，并模拟接收并放到容器里.
						//System.out.println("线程");
						//这个是一个重绘的方法，是线程安全的；可以调用paintComponent(Graphics g) 方法
						repaint();
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//准备绘制
		Graphics2D g2d = (Graphics2D) g;
		//设置绘制线条的质量
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//设置宽高
		int w = getWidth();
		int h = getHeight();
		//设置x轴方向的单位长度;如果宽400px，设置最大的数据容量是50;那么一个数据就占据8px
		int xDelta = w / MAX_COUNT_OF_VALUES;
		//这个是集合的实时长度，肯定是小于或等于MAX_COUNT_OF_VALUES
		int length = values.size();
		//对集合里面的数据进行绘制，第一个点和第二个点要连线
		for (int i = 0; i < length - 1; ++i) {
			/*
			 * x轴的位置：xDelta * (MAX_COUNT_OF_VALUES - length + i)  如果集合的数据没有溢出来，一定是最后一个是最右边，第一个是集合的最大长度-集合当前长度+1；
			 * y轴的位置：normalizeValueForYAxis(values.get(i), h)
			 * 前一个数据和后一个数据相连接
			 */
			g2d.drawLine(xDelta * (MAX_COUNT_OF_VALUES - length + i), normalizeValueForYAxis(values.get(i), h),
					xDelta * (MAX_COUNT_OF_VALUES - length + i + 1), normalizeValueForYAxis(values.get(i + 1), h));
		}
	}

	/**
	 * 接收到的数据放入内存.
	 * @param value
	 */
	private void addValue(int value) {
		// 循环的使用一个接收数据的空间.
		// 最好是实现一个循环数组，而不是偷懒的使用ArrayList.
		if (values.size() > MAX_COUNT_OF_VALUES) {
			values.remove(0);
		}

		values.add(value);
	}

	/**
	 * 规一化y轴方向的值. 使得value在y轴的值为[0, height]之间.
	 */
	private int normalizeValueForYAxis(int value, int height) {
		return (int) ((double) height / MAX_VALUE * value);
	}

	public static void createGuiAndShow() {
		//窗体的名称
		//JFrame frame = new JFrame("java动态绘制图形");
		//内容
		frame.getContentPane().add(new Plaint());
		//设置关闭窗体就是关闭整个程序
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗体的大小
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		//设置窗体可见
		frame.setVisible(true);
	}


}